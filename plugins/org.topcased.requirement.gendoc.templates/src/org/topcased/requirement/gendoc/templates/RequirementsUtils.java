/*****************************************************************************
 * Copyright (c) 2009 atos origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  eperico (atos origin) emilien.perico@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.gendoc.templates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.topcased.requirement.generic.Injector;
import org.topcased.sam.requirement.Attribute;
import org.topcased.sam.requirement.AttributeLink;
import org.topcased.sam.requirement.CurrentRequirement;
import org.topcased.sam.requirement.HierarchicalElement;
import org.topcased.sam.requirement.RequirementProject;
import org.topcased.sam.requirement.SpecialChapter;
import org.topcased.sam.requirement.TrashChapter;
import org.topcased.ttm.Requirement;

/**
 * Class that manages objects of the requirement metamodel (org.topcased.sam.requirement)
 * 
 * @author eperico
 * 
 */
public class RequirementsUtils
{
    /** tab characters for spaces */
    private static String tabChars = "&#160;&#160;&#160;&#160;&#160;";

    /** to know if current "Link To" is the first attribute link. */
    private boolean isFirstAttributeLink = true;

    /** The map to store requirement project of a di file */
    private Map<URI, RequirementProject> map = new HashMap<URI, RequirementProject>();

    /**
     * Gets the current requirement for a specified EObject.
     * 
     * @param eobject the eobject
     * 
     * @return the list of current requirements
     */
    public List<CurrentRequirement> getCurrentRequirementsForEObject(EObject eobject)
    {
        RequirementProject project = loadRequirementProject(eobject);
        if (project != null)
        {
            return getCurrentRequirementsForEObject(eobject, eobject.eResource().getResourceSet());
        }
        return Collections.emptyList();
    }
    
    /**
     * Get the current requirements list for a specified EObject
     * 
     * @param eobject the current object
     * @param set the resource set
     * 
     */
    private List<CurrentRequirement> getCurrentRequirementsForEObject(EObject eobject, ResourceSet set)
    {
        List<CurrentRequirement> requirements = new ArrayList<CurrentRequirement>();
        for (Setting setting : getUsages(eobject, set))
        {
            if (setting.getEObject() instanceof HierarchicalElement)
            {
                for (org.topcased.sam.requirement.Requirement req : ((HierarchicalElement) setting.getEObject()).getRequirement())
                {
                    if (req instanceof CurrentRequirement)
                    {
                        requirements.add((CurrentRequirement) req);
                    }
                }
            }
        }
        return requirements;
    }
    
    /**
     * Gets the not affected requirements.
     * 
     * @param currentEObject the current eobject
     * 
     * @return the list of unaffected current requirements
     */
    public List<CurrentRequirement> getNotAffectedRequirements(EObject currentEObject)
    {
        List<CurrentRequirement> notAffectedRequirements = new ArrayList<CurrentRequirement>();
        RequirementProject project = loadRequirementProject(currentEObject);
        if (project != null)
        {
            for (SpecialChapter chapter : project.getChapter())
            {
                if (chapter instanceof TrashChapter)
                {
                    for (HierarchicalElement elt : chapter.getHierarchicalElement())
                    {
                        for (org.topcased.sam.requirement.Requirement req : elt.getRequirement())
                        {
                            if (req instanceof CurrentRequirement)
                            {
                                notAffectedRequirements.add((CurrentRequirement) req);
                            }
                        }
                    }
                }
            }
        }
        return notAffectedRequirements;
    }
    
    /**
     * Gets the upstream requirements linked to the current requirement.
     * 
     * @param requirement the current requirement
     * 
     * @return the linked upstream requirements
     */
    public List<Requirement> getLinkedUpstreamRequirements(CurrentRequirement requirement)
    {
        List<Requirement> links = new ArrayList<Requirement>();
        for (Attribute att : requirement.getAttribute())
        {
            if (att instanceof AttributeLink)
            {
                EObject value = ((AttributeLink) att).getValue();
                if (value instanceof Requirement)
                {
                    links.add((Requirement) value);
                }
            }
        }
        return links;
    }

//    /**
//     * Gets the associated requirements of elements in the diagram of the rootContainer
//     * 
//     * @param currentEObject the selected rootContainer
//     * 
//     * @return the requirements
//     */
//    public List<CurrentRequirement> getCurrentRequirementsForADiagram(EObject currentEObject)
//    {
//        List<CurrentRequirement> currentRequirements = new ArrayList<CurrentRequirement>();
//        RequirementProject project = loadRequirementProject(currentEObject);
//        if (project != null)
//        {
//            List<EObject> elementsForADiagram = new TemplateServices().getModelElementsForADiagram(currentEObject);
//            for (EObject eobject : elementsForADiagram)
//            {
//                currentRequirements.addAll(getCurrentRequirementsForEObject(eobject, currentEObject.eResource().getResourceSet()));
//            }
//        }
//        return currentRequirements;
//    }

    /**
     * Clean the attribute name to have the right formatter
     * 
     * @param eObject the e object
     * @param name the old name
     * 
     * @return the formatted name
     */
    public String getFormattedName(EObject eObject, String name)
    {
        return name.replaceFirst("#", "").replaceAll("_", " ");
    }

    /**
     * To reach related elements for a specified source element in a resource set
     * 
     * @param source the element for which we search references
     * @param set the specified resourceSet
     * @return collection of references
     */
    public static Collection<EStructuralFeature.Setting> getUsages(EObject source, ResourceSet set)
    {
        Collection<EStructuralFeature.Setting> collection = null;
        ECrossReferenceAdapter crossReferenceAdapter = ECrossReferenceAdapter.getCrossReferenceAdapter(source);
        if (crossReferenceAdapter != null)
        {
            collection = crossReferenceAdapter.getNonNavigableInverseReferences(source);
        }
        else
        {
            collection = EcoreUtil.UsageCrossReferencer.find(source, set);
        }
        return collection;
    }

    /**
     * Gets the requirement project for a specified eObject
     * 
     * @param eObject the eObject
     * 
     * @return the associated requirement project if any
     */
    private RequirementProject loadRequirementProject(EObject eObject)
    {
        RequirementProject project = null;
        if (eObject.eResource() != null)
        {
            ResourceSet set = eObject.eResource().getResourceSet();            
            if (set != null)
            {
                URI createURI = URI.createURI(eObject.eResource().getURI().toString() + "di");
                Resource diagramResource = set.getResource(createURI, true);
                if (diagramResource != null)
                {
                    project = map.get(createURI);
                    if (project == null)
                    {
                        project = Injector.getRequirementProject(diagramResource.getContents().get(0));
                        map.put(createURI, project);
                    }
                    boolean found = false;
                    for (Iterator<Resource> i = set.getResources().iterator() ; i.hasNext() && ! found ;)
                    {
                        found |= i.next().getURI().equals(project.eResource().getURI());
                    }
                    if (!found)
                    {
                        set.getResources().add(project.eResource());
                        // EcoreUtil.resolveAll(set);
                        EcoreUtil.resolveAll(eObject.eResource());
                    }
                }
            }
        }
        return project;
    }

    /**
     * Gets the style name for the specified name.
     * 
     * @param eObject the current eObject
     * @param name the name
     * 
     * @return the style name for the name
     */
    public String getStyleNameFromName(EObject eObject, String name)
    {
        String formattedName = getFormattedName(eObject, name);
        formattedName = formattedName.replaceAll("[^1-z]", "");
        return formattedName;
    }

    /**
     * Gets the tabCharacter for spaces
     * 
     * @param currentEObject the current eObject
     * 
     * @return the tab char
     */
    public String getTabChar(EObject currentEObject)
    {
        return tabChars;
    }

    /**
     * Checks if is first attribute link.
     * 
     * @param attribute the attribute
     * 
     * @return true, if is first attribute link
     */
    public boolean isFirstAttributeLink(Attribute attribute)
    {
        return isFirstAttributeLink;
    }

    /**
     * Sets the first attribute link.
     * 
     * @param eObject the e object
     * @param newBooleanValue the new boolean value
     */
    public void setFirstAttributeLink(EObject eObject, boolean newBooleanValue)
    {
        isFirstAttributeLink = newBooleanValue;
    }

}
