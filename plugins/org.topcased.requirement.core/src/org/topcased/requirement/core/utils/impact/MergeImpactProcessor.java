/***********************************************************************************************************************
 * Copyright (c) 2011 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Philippe ROLAND (Atos) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.utils.impact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeAllocate;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementDifferenceCalculator;
import org.topcased.requirement.core.utils.RequirementUtils;

import ttm.Document;
import ttm.HierarchicalElement;
import ttm.Section;
import ttm.Text;

/**
 * Analyzes the impact of a merge on the given resources<br>
 * An initial impact map is created with the resources passed to the constructor, and actual impacts are later processed
 * by using the RequirementDifferenceCalculator Update : 29 november 2011<br>
 * 
 * @author <a href="mailto:philippe.roland@atos.net">Philippe ROLAND</a>
 * @since Topcased 5.2.0
 */
public class MergeImpactProcessor
{

    private Set<URI> resources;

    private Map<EObject, List<EObject>> impact;

    private Map<String, EObject> ids;

    private RequirementDifferenceCalculator calc;

    private Resource modelResource;

    public MergeImpactProcessor(Set<URI> resources, ResourceSet resourceSet, RequirementDifferenceCalculator calculator)
    {
        impact = new HashMap<EObject, List<EObject>>();
        ids = new HashMap<String, EObject>();
        calc = calculator;
        this.resources = resources;
        for (URI uri : this.resources)
        {
            buildImpactRequirementMap(resourceSet.getResource(uri, false));
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
            if (key instanceof ttm.Requirement)
            {
                ttm.Requirement req = (ttm.Requirement) key;
                ids.put(req.getIdent(), req);
            }
        }
        else
        {
            temp = impact.get(key);
        }
        temp.add(value);
    }

    /**
     * Proceeds with impact analysis processing
     */
    public void processImpact()
    {
        // Moves
        for (DiffElement diff : calc.getMoves())
        {
            if (diff instanceof MoveModelElement)
            {
                EObject moved = ((MoveModelElement) diff).getRightElement();
                // a hierarchical element has been added.
                if (moved instanceof HierarchicalElement)
                {
                    processElementImpact(diff, moved, moved);
                }
            }
        }
        // Additions
        for (DiffElement diff : calc.getAdditions())
        {
            if (diff instanceof ModelElementChangeLeftTarget)
            {
                ModelElementChangeLeftTarget addDiff = (ModelElementChangeLeftTarget) diff;
                EObject added = addDiff.getLeftElement();
                // a hierarchical element or an attribute has been added.
                if (added instanceof UpstreamModel || added instanceof ttm.Attribute || added instanceof ttm.Text)
                {
                    processElementImpact(diff, addDiff.getRightParent(), addDiff.getRightParent());
                }
                else if (added instanceof HierarchicalElement)
                {
                    processElementImpact(diff, addDiff.getLeftElement(), addDiff.getRightParent());
                }
            }
        }
        // Modifications
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
                    processElementImpact(diff, modifiedObject, modifiedObject);
                }
            }
        }
        // Deletion
        for (DiffElement diff : calc.getDeletions())
        {
            if (diff instanceof ModelElementChangeRightTarget)
            {
                EObject removedElement = ((ModelElementChangeRightTarget) diff).getRightElement();
                // an attribute has been removed. We need to mark its parent
                if (removedElement instanceof ttm.Attribute || removedElement instanceof ttm.Text)
                {
                    processElementImpact(diff, removedElement.eContainer(), removedElement);
                }
                // a hierarchical element has been removed
                else if (removedElement instanceof Document || removedElement instanceof Section)
                {
                    for (EObject o : RequirementUtils.getUpstreams(removedElement))
                    {
                        processElementImpact(diff, o, removedElement);
                    }
                }
                else if (removedElement instanceof ttm.Requirement)
                {
                    // the element is marked as deleted
                    processElementImpact(diff, removedElement, removedElement);
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
    private void processElementImpact(DiffElement diff, EObject elementToMark, EObject modifiedElement)
    {
        AdapterFactoryLabelProvider factory = new AdapterFactoryLabelProvider(RequirementUtils.getAdapterFactory());
        List<EObject> foundList = null;
        String req_upstream = "";
        String currentMarkerText = null;

        if (modifiedElement instanceof ttm.Requirement)
        {
            // when deleting a requirement the objects are not equal in a Java sense -> check idents
            ttm.Requirement ttm2 = (ttm.Requirement) modifiedElement;
            req_upstream = ttm2.getIdent();
        }
        if (elementToMark instanceof ttm.Requirement)
        {
            // when deleting a requirement the objects are not equal in a Java sense -> check idents
            ttm.Requirement ttm = (ttm.Requirement) elementToMark;
            if (req_upstream == null || req_upstream.equals(""))
            {
                req_upstream = ttm.getIdent();
            }
            EObject eObject = ids.get(ttm.getIdent());
            if (eObject != null)
            {
                foundList = impact.get(eObject);
            }
        }
        else if (impact.containsKey(elementToMark))
        {
            foundList = impact.get(elementToMark);
        }

        if (foundList != null)
        {
            for (EObject linkTo : foundList)
            {
                try
                {
                    if (linkTo.eContainer() != null && linkTo.eContainer() instanceof CurrentRequirement)
                    {
                        CurrentRequirement currentReq = (CurrentRequirement) linkTo.eContainer();
                        Document d = getDocument(modifiedElement);
                        currentReq.setImpacted(true);
                        // the information is logged
                        String requirement = factory.getText(currentReq);
                        currentMarkerText = requirement + "/" + req_upstream + " : " + getDescription(diff);

                        addMarkerFor(linkTo, currentMarkerText, IMarker.SEVERITY_WARNING, d, false); //$NON-NLS-1$
                    }
                }
                catch (CoreException e)
                {
                    RequirementCorePlugin.log("Warning message cannot be logged", IStatus.ERROR, e); //$NON-NLS-1$
                }
            }
        }
        // if the old resource does not exist, element is intangible and should not be modified -> find new one
        else
        {
            try
            {
                Document document = getDocument(modifiedElement);
                currentMarkerText = req_upstream + " : " + getDescription(diff);

                addMarkerFor(elementToMark, currentMarkerText, IMarker.SEVERITY_INFO, document, true);
            }
            catch (CoreException e)
            {
                RequirementCorePlugin.log("Information message cannot be logged", IStatus.ERROR, e); //$NON-NLS-1$
            }
        }
    }

    /**
     * Put a description with minimum data instead of text from factoryLabel when it possible.
     * 
     * @param diff The difference extracted from the EMF Compare diff model
     * @return description for marker
     */
    protected String getDescription(DiffElement diff)
    {
        int textLength = 15;

        AdapterFactoryLabelProvider factory = new AdapterFactoryLabelProvider(RequirementUtils.getAdapterFactory());
        String reason = factory.getText(diff);

        if (diff instanceof MoveModelElement)
        {
            EObject moved = ((MoveModelElement) diff).getRightElement();
            if (moved instanceof HierarchicalElement)
            {
                return diff.getKind().getName() + " : " + reason;
            }
        }
        else if (diff instanceof ModelElementChangeLeftTarget)
        {
            ModelElementChangeLeftTarget addDiff = (ModelElementChangeLeftTarget) diff;
            EObject added = addDiff.getLeftElement();
            // a hierarchical element or an attribute has been added.
            if (added instanceof UpstreamModel)
            {
                return diff.getKind().getName() + " : " + reason;
            }
            if (added instanceof ttm.Attribute)
            {
                return diff.getKind().getName() + " : Attribute " + ((ttm.Attribute) added).getName();
            }
            if (added instanceof Text)
            {
                String localText = ((Text) added).getValue();
                return diff.getKind().getName() + " : Text (" + localText.substring(0, Math.min(textLength, localText.length())) + "...)";
            }
            else if (added instanceof HierarchicalElement)
            {
                return diff.getKind().getName() + " : " + reason;
            }
        }
        else if (diff instanceof UpdateAttribute)
        {
            EObject modifiedObject = ((UpdateAttribute) diff).getRightElement();
            // an attribute has been modified. We need to mark its parent
            if (modifiedObject instanceof ttm.Attribute)
            {
                return diff.getKind().getName() + " : Attribute " + ((ttm.Attribute) modifiedObject).getName();
            }
            else if (modifiedObject instanceof Text)
            {
                String localText = ((Text) modifiedObject).getValue();
                return diff.getKind().getName() + " : Text " + ((UpdateAttribute) diff).getAttribute().getName() + " (" + localText.substring(0, Math.min(textLength, localText.length())) + ")";
            }
            else if (modifiedObject instanceof HierarchicalElement)
            {
                return diff.getKind().getName() + " : " + modifiedObject.getClass().getSimpleName().replace("Impl", "") + " " + ((UpdateAttribute) diff).getAttribute().getName();
            }
        }
        else if (diff instanceof ModelElementChangeRightTarget)
        {
            EObject removedElement = ((ModelElementChangeRightTarget) diff).getRightElement();
            if (removedElement instanceof ttm.Attribute)
            {
                return diff.getKind().getName() + " : Attribute " + ((ttm.Attribute) removedElement).getName();
            }
            else if (removedElement instanceof Text)
            {
                String localText = ((Text) removedElement).getValue();
                return diff.getKind().getName() + " : Text (" + localText.substring(0, Math.min(textLength, localText.length())) + "...)";
            }
            // a hierarchical element has been removed
            else if (removedElement instanceof Document || removedElement instanceof Section)
            {
                return diff.getKind().getName() + " : " + reason;
            }
            else if (removedElement instanceof ttm.Requirement)
            {
                return diff.getKind().getName() + " : Requirement " + factory.getText(removedElement);
            }
        }
        return diff.getKind().getName() + " : " + reason;
    }

    /**
     * Get the document of the element, element can be current requirement, upstream requirement or document
     * 
     * @param element the element from which found document
     * @return the document of element
     */
    private Document getDocument(EObject element)
    {
        ttm.Requirement upstream = null;
        Document d = null;
        if (element instanceof Requirement)
        {
            upstream = getUpstreamRequirement((Requirement) element);
        }
        if (element instanceof ttm.Requirement)
        {
            upstream = (ttm.Requirement) element;
        }
        if (upstream != null || element instanceof Section)
        {
            EObject parent = null;
            if (upstream == null)
            {
                parent = element.eContainer();
            }
            else
            {
                parent = upstream.eContainer();
            }
            while (parent != null && !(parent instanceof Document))
            {
                parent = parent.eContainer();
            }
            if (parent instanceof Document)
            {
                d = (Document) parent;
            }
        }
        if (element instanceof Document)
        {
            d = (Document) element;
        }
        return d;
    }

    /**
     * Get the upstream requirement with the link attribute of the current requirement
     * 
     * @param element the current requirement
     * @return the upstream requirement link to the element
     */
    private ttm.Requirement getUpstreamRequirement(Requirement element)
    {
        List<Attribute> atts = element.getAttribute();
        for (Attribute a : atts)
        {
            if (a instanceof AttributeLink)
            {
                AttributeLink link = (AttributeLink) a;
                Object o = link.getValue();
                if (o instanceof ttm.Requirement)
                {
                    return (ttm.Requirement) o;
                }
            }
        }
        return null;
    }

    /**
     * Add marker for element toLog with document impacted at location
     * 
     * @param toLog the element impacted
     * @param message the message
     * @param severity the severity
     * @param d the document impacted
     * @param docRes use document for uri (or not)
     * @throws CoreException if cannot create marker
     */
    private void addMarkerFor(EObject toLog, String message, int severity, Document d, boolean docRes) throws CoreException
    {
        // find the concerned element.
        IResource resource = null;
        URI uri = null;
        if (docRes && d != null && d.eResource() != null)
        {
            resource = findResourceFor(d.eResource());
            uri = d.eResource().getURI();
        }
        else if (toLog.eResource() != null)
        {
            resource = findResourceFor(toLog.eResource());
            uri = toLog.eResource().getURI();
        }
        
        // Retrieve the original model if the one containing changes is a temporary file
        if (resource == null && modelResource != null)
        {
            resource = findResourceFor(modelResource);
            uri = modelResource.getURI();
        }

        if (resource != null)
        {
            IMarker marker = resource.createMarker(EValidator.MARKER);
            marker.setAttribute(IMarker.SEVERITY, severity);
            marker.setAttribute(EValidator.URI_ATTRIBUTE, uri.appendFragment(toLog.eResource().getURIFragment(toLog)).toString());
            marker.setAttribute(IMarker.MESSAGE, message);
            if (d != null)
            {
                marker.setAttribute(IMarker.LOCATION, d.getIdent());
            }
        }
        else
        {
            StringBuffer uriMessage = new StringBuffer();
            int error = IStatus.ERROR;
            if (uri != null)
            {
                uriMessage.append(" : ").append(uri.toString());
                if (!(uri.isFile() || uri.isPlatform()))
                {
                    error = IStatus.WARNING;
                }
            }
            IStatus status = new Status(error, RequirementCorePlugin.PLUGIN_ID, error, "Cannot create marker from a null resource" + uriMessage.toString(), null); //$NON-NLS-1$
            throw new CoreException(status);
        }
    }

    /**
     * Find resource for toResolve
     * 
     * @param toResolve the resource to find
     * @return the resource find or null
     */
    private static IResource findResourceFor(Resource toResolve)
    {
        if (toResolve != null)
        {
            String relativePath = toResolve.getURI().toPlatformString(true);
            if (relativePath != null)
            {
                return ResourcesPlugin.getWorkspace().getRoot().findMember(relativePath);
            }
        }
        return null;
    }

    //Use this method if changes are already applied to the original model 
    // and the model containing changes is a temporary file
    public void processImpact(Resource modelResource)
    {
        this.modelResource = modelResource;
        processImpact();
    }
}
