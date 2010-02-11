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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DifferenceKind;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.topcased.facilities.util.EMFMarkerUtil;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeAllocate;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.RequirementCorePlugin;

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

    private EList<DiffElement> deletions;

    private EList<DiffElement> changes;

    private EList<DiffElement> additions;

    private EList<DiffElement> moves;

    private Map<EObject, List<EObject>> impact;

    private Document deletedDoc;

    /**
     * Constructor
     */
    private MergeRequirement()
    {
        deletions = new BasicEList<DiffElement>();
        changes = new BasicEList<DiffElement>();
        additions = new BasicEList<DiffElement>();
        moves = new BasicEList<DiffElement>();
        impact = new HashMap<EObject, List<EObject>>();
    }

    /**
     * Merges two models of requirements
     * 
     * @param current : the existing model of requirement
     * @param toMerge : the new model of requirement to merge
     * @param monitor : the monitor that should control the processing
     * @throws InterruptedException if the merge operation failed (match + merge).
     */
    public void merge(Resource current, Resource toMerge, IProgressMonitor monitor) throws InterruptedException
    {
        // resets the three lists
        moves.clear();
        additions.clear();
        changes.clear();
        deletions.clear();
        deletedDoc = null;

        // build the map
        buildImpactRequirementMap(current);

        // Call the EMF comparison service
        Map<String, Object> options = new HashMap<String, Object>();
        options.put(MatchOptions.OPTION_IGNORE_ID, false);
        options.put(MatchOptions.OPTION_IGNORE_XMI_ID, true);
        options.put(MatchOptions.OPTION_PROGRESS_MONITOR, monitor);
        EObject currentRoot = RequirementUtils.getUpstreamModel(current);
        EObject mergeRoot = RequirementUtils.getUpstreamModel(toMerge);
        MatchModel match = MatchService.doMatch(mergeRoot, currentRoot, options);// currentRoot, mergeRoot, options);
        DiffModel diff = DiffService.doDiff(match);
        for (DiffElement aDifference : diff.getOwnedElements())
        {
            buildDifferenceLists(aDifference);
        }
        processMoved();
        processAdded();
        processModified();
        processDeleted();

    }

    /**
     * According to the difference kind, the element itself is included in on of the four lists.
     * 
     * @param difference A difference object
     */
    private void buildDifferenceLists(DiffElement difference)
    {
        if (!(difference instanceof DiffGroup))
        {
            if (difference.getKind().equals(DifferenceKind.MOVE))
            {
                moves.add(difference);
            }

            if (difference.getKind().equals(DifferenceKind.DELETION))
            {
                deletions.add(difference);
            }

            if (difference.getKind().equals(DifferenceKind.CHANGE))
            {
                changes.add(difference);
            }

            if (difference.getKind().equals(DifferenceKind.ADDITION))
            {
                additions.add(difference);
            }
        }

        // build by sorting differences according to their kind
        if (difference.getSubDiffElements() != null)
        {
            for (DiffElement subDiff : difference.getSubDiffElements())
            {
                buildDifferenceLists(subDiff);
            }
        }
    }

    /**
     * Builds the impact map.
     * 
     * @param current The current loaded resource
     */
    private void buildImpactRequirementMap(Resource current)
    {
        Collection<Requirement> allRequirements = RequirementUtils.getAllCurrents(current);
        for (Requirement requirement : allRequirements)
        {
            for (Attribute attribute : requirement.getAttribute())
            {
                // If it is a #Link_to or a #Ref
                if (attribute instanceof AttributeLink || (attribute instanceof ObjectAttribute && !(attribute instanceof AttributeAllocate)))
                {
                    buildMapAttributeToRequirement(attribute);
                }
            }
        }
    }

    /**
     * In charge of filling the impact analysis map.
     * 
     * @param attribute An attribute belonging to a CurrentRequirement.
     */
    private void buildMapAttributeToRequirement(Attribute attribute)
    {
        // Attribute is of type #Link_to and may lead to an Upstream Req.
        if (attribute instanceof AttributeLink)
        {
            AttributeLink attributeLink = (AttributeLink) attribute;
            if (attributeLink.getValue() instanceof ttm.Requirement)
            {
                insertIntoMap(attributeLink.getValue(), attribute);
            }
        }
        // Attribute is of type #Ref and may lead to an Upstream Req.
        else if (attribute instanceof ObjectAttribute && !(attribute instanceof AttributeAllocate))
        {
            ObjectAttribute attributeLink = (ObjectAttribute) attribute;
            if (attributeLink.getValue() instanceof ttm.Requirement)
            {
                insertIntoMap(attributeLink.getValue(), attribute);
            }
        }
    }

    /**
     * Inserts into the the impact map, the key associated with the value
     * 
     * @param key An EObject representing an Upstream Requirement.
     * @param value
     */
    private void insertIntoMap(EObject key, EObject value)
    {
        List<EObject> temp = null;
        if (impact.get(key) == null)
        {
            temp = new ArrayList<EObject>();
            impact.put(key, temp);
        }
        else
        {
            temp = impact.get(key);
        }
        temp.add(value);
    }

    /**
     * Processes 'move' operations.
     */
    private void processMoved()
    {
        for (DiffElement diff : moves)
        {
            if (diff instanceof MoveModelElement)
            {
                EObject moved = ((MoveModelElement) diff).getRightElement();
                // a hierarchical element has been added.
                if (moved instanceof HierarchicalElement)
                {
                    processImpact(diff, moved);
                    // the hierarchical element is marked as added
                    MergeService.merge(diff, true);
                }
            }
        }
    }

    /**
     * Processes 'addition' operations.
     */
    private void processAdded()
    {
        for (DiffElement diff : additions)
        {
            if (diff instanceof ModelElementChangeLeftTarget)
            {
                EObject added = ((ModelElementChangeLeftTarget) diff).getLeftElement();
                // a hierarchical element or an attribute has been added.
                if (added instanceof UpstreamModel || added instanceof HierarchicalElement || added instanceof ttm.Attribute)
                {
                    processImpact(diff, ((ModelElementChangeLeftTarget) diff).getRightParent());
                    // the hierarchical element is marked as added
                    MergeService.merge(diff, true);
                }
            }
        }
    }

    /**
     * Processes 'deletion' operations.
     */
    private void processDeleted()
    {
        for (DiffElement diff : deletions)
        {
            if (diff instanceof ModelElementChangeRightTarget)
            {
                EObject removedElement = ((ModelElementChangeRightTarget) diff).getRightElement();
                // an attribute has been removed. We need to mark its parent
                if (removedElement instanceof ttm.Attribute)
                {
                    processImpact(diff, removedElement.eContainer());
                    MergeService.merge(diff, true);
                }
                // a hierarchical element has been removed
                else if (removedElement instanceof Document || removedElement instanceof Section)
                {
                    for (EObject o : RequirementUtils.getUpstream(removedElement))
                    {
                        processImpact(diff, o);
                        addRequirementToDeleted((ttm.Requirement) o);
                    }
                    MergeService.merge(diff, true);
                }
                else if (removedElement instanceof ttm.Requirement)
                {
                    // the element is marked as deleted
                    processImpact(diff, removedElement);
                    addRequirementToDeleted((ttm.Requirement) removedElement);
                }
            }
        }
    }

    /**
     * Processes the 'modified' operations.
     */
    private void processModified()
    {
        for (DiffElement diff : changes)
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
                    processImpact(diff, modifiedObject);
                    MergeService.merge(diff, true);
                }
            }
        }
    }

    /**
     * Processes potential impacts between upstream requirements changes and current requirements defined into the
     * requirement model.<br>
     * A warning message is logged for each impact found. If no impact found, the processed {@link DiffElement} is
     * logged as a simple information message<br>
     * The current requirement is marked as impacted.
     * 
     * @param diff The difference extracted from the EMF Compare diff model
     * @param element An EObject involved into the difference.
     */
    private void processImpact(DiffElement diff, EObject element)
    {
        AdapterFactoryLabelProvider factory = new AdapterFactoryLabelProvider(RequirementUtils.getAdapterFactory());
        if (impact.containsKey(element))
        {
            for (EObject linkTo : impact.get(element))
            {
                try
                {
                    if (linkTo.eContainer() != null && linkTo.eContainer() instanceof CurrentRequirement)
                    {
                        CurrentRequirement currentReq = (CurrentRequirement) linkTo.eContainer();
                        currentReq.setImpacted(true);
                        // the information is logged
                        String requirement = factory.getText(currentReq);
                        String reason = factory.getText(diff);
                        EMFMarkerUtil.addMarkerFor(linkTo, requirement + " : " + reason, IMarker.SEVERITY_WARNING); //$NON-NLS-1$
                    }
                }
                catch (CoreException e)
                {
                    RequirementCorePlugin.log("Warning message cannot be logged", IStatus.ERROR, e); //$NON-NLS-1$
                }
            }
        }
        else
        {
            try
            {
                String infoMsg = factory.getText(diff);
                EMFMarkerUtil.addMarkerFor(element, infoMsg, IMarker.SEVERITY_INFO);
            }
            catch (CoreException e)
            {
                RequirementCorePlugin.log("Information message cannot be logged", IStatus.ERROR, e); //$NON-NLS-1$
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
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
            deletedDoc.setIdent("deleted_".concat(dateFormat.format(new Date()))); //$NON-NLS-1$
            model.getDocuments().add(deletedDoc);
        }
        deletedDoc.getChildren().add(element);
    }
}
