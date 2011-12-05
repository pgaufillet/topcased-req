/***********************************************************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.utils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.ecore.EObject;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.extensions.IMergeRequirementProcessor;

import ttm.Document;
import ttm.HierarchicalElement;
import ttm.Section;
import ttm.Text;
import ttm.TtmFactory;

/**
 * Process the merge of documents with an existing model of requirement.<br>
 * Four types of operations are handled : <b>add, remove, modify and move</b>. Merge operations and impact analysis are
 * performed at the same time. Finally, markers are set on corresponding resources.<br>
 * Update : 06 may 2009<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.2.0
 */
public final class MergeRequirement
{
    public static final MergeRequirement INSTANCE = new MergeRequirement();

    private static final String MERGE_PROCESSORS_EXTENSION_POINT = "org.topcased.requirement.core.mergeRequirementProcessor";

    private static final String MERGE_PROCESSOR_ELEMENT_NAME = "mergeProcessor";

    private static final String CLASS_ATTRIBUTE_NAME = "class";

    private Document deletedDoc;

    /** The extra processors from extensions to execute on requirement merge */
    private List<IMergeRequirementProcessor> processors;

    /**
     * Constructor
     */
    private MergeRequirement()
    {
        // Initialize extra processors from extension point
        processors = new LinkedList<IMergeRequirementProcessor>();
        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(MERGE_PROCESSORS_EXTENSION_POINT);
        for (IExtension extension : extensionPoint.getExtensions())
        {
            for (IConfigurationElement cfg : extension.getConfigurationElements())
            {
                if (MERGE_PROCESSOR_ELEMENT_NAME.equals(cfg.getName()))
                {
                    try
                    {
                        Object processor = cfg.createExecutableExtension(CLASS_ATTRIBUTE_NAME);
                        if (processor instanceof IMergeRequirementProcessor)
                        {
                            processors.add((IMergeRequirementProcessor) processor);
                        }
                    }
                    catch (CoreException e)
                    {
                        RequirementCorePlugin.log(e);
                    }
                }
            }
        }
    }

    /**
     * Merges two models of requirements
     * 
     * @param current : the existing model of requirement
     * @param toMerge : the new model of requirement to merge
     * @param monitor : the monitor that should control the processing
     * @throws InterruptedException if the merge operation failed (match + merge).
     */
    public void merge(RequirementDifferenceCalculator calc, IProgressMonitor monitor) throws InterruptedException
    {
        merge(calc, false, monitor);
    }

    public void merge(RequirementDifferenceCalculator calc, boolean isPartialImport, IProgressMonitor monitor) throws InterruptedException
    {
        // resets the three lists
        deletedDoc = null;

        processMoved(calc);
        processAdded(calc);
        processModified(calc);
        processDeleted(calc, isPartialImport);
    }

    /**
     * Processes 'move' operations.
     * @param calc 
     */
    private void processMoved(RequirementDifferenceCalculator calc)
    {
        for (DiffElement diff : calc.getMoves())
        {
            if (diff instanceof MoveModelElement)
            {
                EObject moved = ((MoveModelElement) diff).getRightElement();
                // a hierarchical element has been added.
                if (moved instanceof HierarchicalElement)
                {
                    // the hierarchical element is marked as added
                    MergeService.merge(diff, true);
                }
            }
            // process from extensions
            for (IMergeRequirementProcessor processor : processors)
            {
                processor.processMoved(diff);
            }
        }
    }

    /**
     * Processes 'addition' operations.
     * @param calc 
     */
    private void processAdded(RequirementDifferenceCalculator calc)
    {
        for (DiffElement diff : calc.getAdditions())
        {
            if (diff instanceof ModelElementChangeLeftTarget)
            {
                EObject added = ((ModelElementChangeLeftTarget) diff).getLeftElement();
                // a hierarchical element or an attribute has been added.
                if (added instanceof UpstreamModel || added instanceof HierarchicalElement || added instanceof ttm.Attribute || added instanceof ttm.Text)
                {
                    // the hierarchical element is marked as added
                    MergeService.merge(diff, true);
                }
            }
            // process from extensions
            for (IMergeRequirementProcessor processor : processors)
            {
                processor.processAdded(diff);
            }
        }
    }

    /**
     * Processes 'deletion' operations.
     * @param calc 
     */
    private void processDeleted(RequirementDifferenceCalculator calc, boolean isPartialImport)
    {
        for (DiffElement diff : calc.getDeletions())
        {
            if (diff instanceof ModelElementChangeRightTarget)
            {
                EObject removedElement = ((ModelElementChangeRightTarget) diff).getRightElement();
                // an attribute has been removed. We need to mark its parent
                if (removedElement instanceof ttm.Attribute || removedElement instanceof ttm.Text)
                {
                    MergeService.merge(diff, true);
                }
                // a hierarchical element has been removed
                else if (removedElement instanceof Document || removedElement instanceof Section)
                {
                    for (EObject o : RequirementUtils.getUpstreams(removedElement))
                    {
                        addRequirementToDeleted((ttm.Requirement) o);
                    }
                    MergeService.merge(diff, true);
                }
                else if (removedElement instanceof ttm.Requirement && !isPartialImport)
                {
                    // the element is marked as deleted
                    addRequirementToDeleted((ttm.Requirement) removedElement);
                }
            }
            // process from extensions
            for (IMergeRequirementProcessor processor : processors)
            {
                processor.processDeleted(diff);
            }
        }
    }

    /**
     * Processes the 'modified' operations.
     * @param calc 
     */
    private void processModified(RequirementDifferenceCalculator calc)
    {
        for (DiffElement diff : calc.getChanges())
        {
            if (diff instanceof UpdateAttribute)
            {
                EObject modifiedObject = ((UpdateAttribute) diff).getRightElement();
                // an attribute has been modified. We need to mark its parent
                if (modifiedObject instanceof ttm.Attribute || modifiedObject instanceof Text)
                {
                    modifiedObject = modifiedObject.eContainer();
                }
                if (modifiedObject instanceof HierarchicalElement)
                {
                    // the element is marked as modified
                    MergeService.merge(diff, true);
                }
            }
            // process from extensions
            for (IMergeRequirementProcessor processor : processors)
            {
                processor.processModified(diff);
            }
        }
    }

    /**
     * Adds a hierarchical element to a virtual <b>'deleted'</b> document.
     * 
     * @param element A {@link HierarchicalElement} that will be most of time a {@link Requirement}
     */
    private void addRequirementToDeleted(HierarchicalElement element)
    {
        if (deletedDoc == null)
        {
            UpstreamModel model = RequirementUtils.getUpstreamModel(element.eResource());
            deletedDoc = TtmFactory.eINSTANCE.createDocument();
            String ident = RequirementUtils.getDeletedDocumentIdent(new Date());
            deletedDoc.setIdent(ident);
            model.getDocuments().add(deletedDoc);
        }
        deletedDoc.getChildren().add(element);
    }
    
}
