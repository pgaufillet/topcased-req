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
    private static final String REQUIREMENT_EXTENSION = "requirement";

    /** The map to store requirement project of a di file */
    private Map<URI, RequirementProject> requirementProjectsMap = new HashMap<URI, RequirementProject>();

    /**
     * Gets the current requirement for a specified EObject.
     * 
     * @param eObject the eobject
     * 
     * @return the list of current requirements
     */
    public List<CurrentRequirement> getCurrentRequirementsForEObject(EObject eObject)
    {
        loadRequirementProjects(eObject);
        if (eObject.eResource() != null)
        {
            return getCurrentRequirementsForEObject(eObject, eObject.eResource().getResourceSet());            
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
        RequirementProject project = loadCorrespondingRequirementProject(currentEObject);
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
     * Load requirement projects.
     * 
     * @param eObject the eObject for which we search attached requirements
     */
    private void loadRequirementProjects(EObject eObject)
    {
        RequirementProject project = null;
        if (eObject.eResource() != null)
        {
            ResourceSet set = eObject.eResource().getResourceSet();            
            if (set != null)
            {
                for (int i = 0; i < set.getResources().size(); i++)
                {
                    Resource resource = set.getResources().get(i);
                    Resource diResource = null;
                    URI uri = resource.getURI();
                    
                    if (uri != null && !REQUIREMENT_EXTENSION.equals(uri.fileExtension()))
                    {
                        if (uri.fileExtension().endsWith("di"))
                        {
                            diResource = resource;
                        }
                        else
                        {
                            URI createURI = URI.createURI(uri.toString() + "di");
                            diResource = set.getResource(createURI, true);
                        }
                    }
                    
                    if (diResource != null && diResource.getErrors().size() == 0)
                    {
                        URI diagramURI = diResource.getURI();
                        project = requirementProjectsMap.get(diagramURI);
                        if (project == null)
                        {
                            project = Injector.getRequirementProject(diResource.getContents().get(0));
                            if (project != null)
                            {
                                requirementProjectsMap.put(diagramURI, project);                            
                                boolean found = false;
                                for (Iterator<Resource> j = set.getResources().iterator() ; j.hasNext() && ! found ;)
                                {
                                    found |= j.next().getURI().equals(project.eResource().getURI());
                                }
                                if (!found)
                                {
                                    set.getResources().add(project.eResource());
                                    // resolve resource to reach controlled resources if any
                                    //EcoreUtil.resolveAll(resource);
                                    // resolve diResource to reach requirement project
                                    EcoreUtil.resolveAll(diResource);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
    * Load corresponding requirement project.
    * 
    * @param currentEObject the current e object
    * 
    * @return the requirement project
    */
    public RequirementProject loadCorrespondingRequirementProject(EObject currentEObject)
    {
        loadRequirementProjects(currentEObject);
        URI uri = null;
        if (currentEObject.eResource() != null)
        {
            uri = URI.createURI(currentEObject.eResource().getURI().toString() + "di");
            // TODO only get requirement project for currentEObjet's model
            return requirementProjectsMap.get(uri);
        }
        return null;
    }
    
}
