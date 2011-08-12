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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
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
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
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
import org.topcased.requirement.core.extensions.IMergeRequirementProcessor;
import org.topcased.requirement.core.utils.ContainerAssigner.ContainerAssignerFactory;

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

    private EList<DiffElement> deletions;

    private EList<DiffElement> changes;

    private EList<DiffElement> additions;

    private EList<DiffElement> moves;

    private Map<EObject, List<EObject>> impact;

    private Document deletedDoc;

    /** The extra processors from extensions to execute on requirement merge */
    private List<IMergeRequirementProcessor> processors;

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
        processDeleted(false);

    }

    public void merge(Map<Document, Document> documentsToMerge, Resource current, boolean isPartialImport, IProgressMonitor monitor) throws InterruptedException
    {
        // resets the three lists
        moves.clear();
        additions.clear();
        changes.clear();
        deletions.clear();
        deletedDoc = null;

        // Call the EMF comparison service
        Map<String, Object> options = new HashMap<String, Object>();
        options.put(MatchOptions.OPTION_IGNORE_ID, false);
        options.put(MatchOptions.OPTION_IGNORE_XMI_ID, true);
        options.put(MatchOptions.OPTION_PROGRESS_MONITOR, monitor);
        List<Document> upstreamDocuments = RequirementUtils.getUpstreamDocuments(current);
        for (int i = 0; i < upstreamDocuments.size(); i++)
        {
            Document d = upstreamDocuments.get(i);
            for (Document d2 : documentsToMerge.keySet())
            {
                if (d.getIdent().equals(d2.getIdent()))
                {
                    // build the map
                    buildImpactRequirementMap(d.eResource());

                    Document currentRoot = d;
                    Document mergeRoot = documentsToMerge.get(d2);

                    ContainerAssignerFactory factory = new ContainerAssignerFactory();
                    ContainerAssigner container1 = factory.create(mergeRoot);
                    Resource r1dummy = new XMIResourceImpl();
                    r1dummy.getContents().add(mergeRoot);

                    ContainerAssigner container2 = factory.create(currentRoot);
                    Resource r2dummy = new XMIResourceImpl();
                    r2dummy.getContents().add(currentRoot);

                    MatchModel match = MatchService.doMatch(mergeRoot, currentRoot, options);
                    DiffModel diff = DiffService.doDiff(match);
                    for (DiffElement aDifference : diff.getOwnedElements())
                    {
                        buildDifferenceLists(aDifference);
                    }
                    container1.backup();
                    container2.backup();
                }
            }
        }
        processMoved();
        processAdded();
        processModified();
        processDeleted(isPartialImport);
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
            // process from extensions
            for (IMergeRequirementProcessor processor : processors)
            {
                processor.processMoved(diff);
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
                if (added instanceof UpstreamModel || added instanceof HierarchicalElement || added instanceof ttm.Attribute || added instanceof ttm.Text)
                {
                    processImpact(diff, ((ModelElementChangeLeftTarget) diff).getRightParent());
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
     */
    private void processDeleted(boolean isPartialImport)
    {
        for (DiffElement diff : deletions)
        {
            if (diff instanceof ModelElementChangeRightTarget)
            {
                EObject removedElement = ((ModelElementChangeRightTarget) diff).getRightElement();
                // an attribute has been removed. We need to mark its parent
                if (removedElement instanceof ttm.Attribute || removedElement instanceof ttm.Text)
                {
                    processImpact(diff, removedElement.eContainer());
                    MergeService.merge(diff, true);
                }
                // a hierarchical element has been removed
                else if (removedElement instanceof Document || removedElement instanceof Section)
                {
                    for (EObject o : RequirementUtils.getUpstreams(removedElement))
                    {
                        processImpact(diff, o);
                        addRequirementToDeleted((ttm.Requirement) o);
                    }
                    MergeService.merge(diff, true);
                }
                else if (removedElement instanceof ttm.Requirement && !isPartialImport)
                {
                    // the element is marked as deleted
                    processImpact(diff, removedElement);
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
            // process from extensions
            for (IMergeRequirementProcessor processor : processors)
            {
                processor.processModified(diff);
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
                        Document d = getDocument(currentReq); 
                        currentReq.setImpacted(true);
                        // the information is logged
                        String requirement = factory.getText(currentReq);
                        String reason = factory.getText(diff);
                        if(d != null) {
                            addMarkerFor(linkTo, requirement + " : " + reason, IMarker.SEVERITY_WARNING, d); //$NON-NLS-1$
                        }
                        else {
                            EMFMarkerUtil.addMarkerFor(linkTo, requirement + " : " + reason, IMarker.SEVERITY_WARNING); //$NON-NLS-1$
                        }
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
                Document document = getDocument(element);
                if(document != null) {
                    addMarkerFor(element, infoMsg, IMarker.SEVERITY_INFO, document);
                }
                else {
                    EMFMarkerUtil.addMarkerFor(element, infoMsg, IMarker.SEVERITY_INFO);
                }
            }
            catch (CoreException e)
            {
                RequirementCorePlugin.log("Information message cannot be logged", IStatus.ERROR, e); //$NON-NLS-1$
            }
        }
    }
    
    /**
     * Get the upstream requirement with the link attribute of the current requirement
     * @param element the current requirement
     * @return the upstream requirement link to the element
     */
    private ttm.Requirement getUpstreamRequirement(Requirement element) {
        List<Attribute> atts = element.getAttribute();
        for(Attribute a : atts) {
            if(a instanceof AttributeLink) {
                AttributeLink link = (AttributeLink)a;
                Object o = link.getValue();
                if(o instanceof ttm.Requirement) {
                    return (ttm.Requirement) o;
                }
            }
        }
        return null;
    }
    
    /**
     * Get the document of the element, element can be current requirement, upstream requirement or document
     * @param element the element from which found document
     * @return the document of element
     */
    private Document getDocument(EObject element) {
        ttm.Requirement upstream = null;
        Document d = null;
        if(element instanceof Requirement) {
            upstream = getUpstreamRequirement((Requirement) element);
        }
        if(element instanceof ttm.Requirement) {
            upstream = (ttm.Requirement) element;
        }
        if(upstream != null) {
            EObject parent = upstream.eContainer();
            while(parent != null && !(parent instanceof Document)) {
                parent = parent.eContainer();
            }
            if(parent instanceof Document) {
                d = (Document) parent;
            }
        }
        if(element instanceof Document) {
            d = (Document) element;
        }
        return d;
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
    
    /**
     * Add marker for element toLog with document impacted at location
     * @param toLog the element impacted
     * @param message the message
     * @param severity the severity
     * @param d the document impacted
     * @throws CoreException if cannot create marker
     */
    private void addMarkerFor(EObject toLog, String message, int severity, Document d) throws CoreException
    {
        // find the concerned element.
        IResource resource = findResourceFor(toLog.eResource());
        if (resource != null)
        {
            IMarker marker = resource.createMarker(EValidator.MARKER);
            marker.setAttribute(IMarker.SEVERITY, severity);
            marker.setAttribute(EValidator.URI_ATTRIBUTE, EcoreUtil.getURI(toLog).toString());
            marker.setAttribute(IMarker.MESSAGE, message);
            marker.setAttribute(IMarker.LOCATION, d.getIdent());
        }
        else
        {
            StringBuffer uriMessage = new StringBuffer() ;
            int error = IStatus.ERROR;
            if (toLog.eResource() != null && toLog.eResource().getURI() != null)
            {
                URI uri = toLog.eResource().getURI();
                uriMessage.append(" : ").append(uri.toString());
                if (!(uri.isFile() || uri.isPlatform()))
                {
                    error = IStatus.WARNING ;
                }
            }
            IStatus status = new Status(error, RequirementCorePlugin.PLUGIN_ID,error, "Cannot create marker from a null resource" + uriMessage.toString(),null); //$NON-NLS-1$
            throw new CoreException(status);
        }
    }
    
    /**
     * Find resource for toResolve
     * @param toResolve the resource to find
     * @return the resource find or null
     */
    private static IResource findResourceFor(Resource toResolve)
    {
        if(toResolve != null)
        {
            String relativePath = toResolve.getURI().toPlatformString(true);
            if (relativePath != null)
            {
                return ResourcesPlugin.getWorkspace().getRoot().findMember(relativePath);
            }
        }
        return null;
    }

}
