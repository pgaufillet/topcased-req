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
import java.util.Map.Entry;
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
import org.eclipse.emf.ecore.util.EcoreUtil;
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

    private RequirementDifferenceCalculator calc;

    public MergeImpactProcessor(Set<URI> resources, ResourceSet resourceSet, RequirementDifferenceCalculator calculator)
    {
        impact = new HashMap<EObject, List<EObject>>();
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
                    processElementImpact(diff, moved);
                }
            }
        }
        // Additions
        for (DiffElement diff : calc.getAdditions())
        {
            if (diff instanceof ModelElementChangeLeftTarget)
            {
                EObject added = ((ModelElementChangeLeftTarget) diff).getLeftElement();
                // a hierarchical element or an attribute has been added.
                if (added instanceof UpstreamModel || added instanceof HierarchicalElement || added instanceof ttm.Attribute || added instanceof ttm.Text)
                {
                    processElementImpact(diff, ((ModelElementChangeLeftTarget) diff).getRightParent());
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
                    processElementImpact(diff, modifiedObject);
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
                    processElementImpact(diff, removedElement.eContainer());
                }
                // a hierarchical element has been removed
                else if (removedElement instanceof Document || removedElement instanceof Section)
                {
                    for (EObject o : RequirementUtils.getUpstreams(removedElement))
                    {
                        processElementImpact(diff, o);
                    }
                }
                else if (removedElement instanceof ttm.Requirement)
                {
                    // the element is marked as deleted
                    processElementImpact(diff, removedElement);
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
    private void processElementImpact(DiffElement diff, EObject element)
    {
        AdapterFactoryLabelProvider factory = new AdapterFactoryLabelProvider(RequirementUtils.getAdapterFactory());
        List<EObject> foundList = null;
        if (element instanceof ttm.Requirement)
        {
            // when deleting a requirement the objects are not equal in a Java sense -> check idents
            ttm.Requirement requirement = (ttm.Requirement) element;
            for (EObject impactObject : impact.keySet())
            {
                if (impactObject instanceof ttm.Requirement)
                {
                    ttm.Requirement impactRequirement = (ttm.Requirement) impactObject;
                    if (impactRequirement.getIdent().equals(requirement.getIdent()))
                    {
                        foundList = impact.get(impactRequirement);
                        break;
                    }
                }
            }
        }
        else if (impact.containsKey(element))
        {
            foundList = impact.get(element);
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
                        Document d = getDocument(currentReq);
                        currentReq.setImpacted(true);
                        // the information is logged
                        String requirement = factory.getText(currentReq);
                        String reason = factory.getText(diff);
                        if (d != null)
                        {
                            addMarkerFor(linkTo, requirement + " : " + reason, IMarker.SEVERITY_WARNING, d); //$NON-NLS-1$
                        }
                        else
                        {
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
        // if the old resource does not exist, element is intangible and should not be modified
        else
        {
            try
            {
                String infoMsg = factory.getText(diff);
                Document document = getDocument(element);
                Document newDocument = null;
                for (Entry<Document, Document> entry : calc.getMergedDocuments().entrySet())
                {
                    if (entry.getValue().equals(document))
                    {
                        newDocument = entry.getKey();
                    }
                }
                if (newDocument != null)
                {
                    EMFMarkerUtil.addMarkerFor(newDocument, infoMsg, IMarker.SEVERITY_INFO);
                }
            }
            catch (CoreException e)
            {
                RequirementCorePlugin.log("Information message cannot be logged", IStatus.ERROR, e); //$NON-NLS-1$
            }
        }
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
        if (upstream != null)
        {
            EObject parent = upstream.eContainer();
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
            StringBuffer uriMessage = new StringBuffer();
            int error = IStatus.ERROR;
            if (toLog.eResource() != null && toLog.eResource().getURI() != null)
            {
                URI uri = toLog.eResource().getURI();
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
}