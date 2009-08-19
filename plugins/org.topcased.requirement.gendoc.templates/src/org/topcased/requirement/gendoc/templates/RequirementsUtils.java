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
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.topcased.model2doc.templates.acceleo.UMLUtils;
import org.topcased.requirement.generic.Injector;
import org.topcased.sam.requirement.Attribute;
import org.topcased.sam.requirement.AttributeLink;
import org.topcased.sam.requirement.CurrentRequirement;
import org.topcased.sam.requirement.HierarchicalElement;
import org.topcased.sam.requirement.RequirementProject;
import org.topcased.ttm.Requirement;

/**
 * Class that manages objects of the requirement metamodel (org.topcased.sam.requirement)
 * 
 * @author eperico
 *
 */
public class RequirementsUtils
{
    
    /**
     * Gets the associated requirements of elements in the diagram of the rootContainer
     * 
     * @param currentEObject the selected rootContainer
     * 
     * @return the requirements
     */
    public List<CurrentRequirement> getCurrentRequirements(EObject currentEObject)
    {        
        List<CurrentRequirement> currentRequirements = new ArrayList<CurrentRequirement>();
        ResourceSet set = currentEObject.eResource().getResourceSet();
        
        // get the requirement resource in the resource set
        Resource diagramResource = set.getResource(URI.createURI(currentEObject.eResource().getURI().toString() + "di"), true);
        if (diagramResource != null)
        {
            RequirementProject project = Injector.getRequirementProject(diagramResource.getContents().get(0));
            if (project != null)
            {
                set.getResources().add(project.eResource());
                EcoreUtil.resolveAll(set);            
                
                List<EObject> elementsForADiagram = new UMLUtils().getElementsForADiagram(currentEObject);
                for (EObject eobject: elementsForADiagram)
                {
                    currentRequirements.addAll(getCurrentRequirementForEObject(eobject, set));
                }                
            }
        }
        return currentRequirements;        
    }

    /**
     * Get the current requirements list for a specified EObject
     * 
     * @param eobject the current object
     * @param set the resource set
     * 
     */
    private List<CurrentRequirement> getCurrentRequirementForEObject(EObject eobject, ResourceSet set)
    {
        List<CurrentRequirement> requirements = new ArrayList<CurrentRequirement>();
        for (Setting setting: UMLUtils.getUsages(eobject, set))
        {
            if (setting.getEObject() instanceof HierarchicalElement)
            {
                for (org.topcased.sam.requirement.Requirement req: ((HierarchicalElement) setting.getEObject()).getRequirement())
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
     * Gets the current requirement for a specified EObject.
     * 
     * @param eobject the eobject
     * 
     * @return the list of current requirements
     */
    public List<CurrentRequirement> getCurrentRequirementForEObject(EObject eobject)
    {
        return getCurrentRequirementForEObject(eobject, eobject.eResource().getResourceSet());
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
        for (Attribute att: requirement.getAttribute())
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
     * Checks if is first link_to attribute.
     * 
     * @param attribute the specified attribute
     * 
     * @return true, if is first link
     */
    public boolean isFirstLink(Attribute attribute)
    {
        CurrentRequirement req = (CurrentRequirement) attribute.eContainer();
        // 3 is the position of Link_to Attribute
        return req.getAttribute().indexOf(attribute) == 3 ;
    }
    
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

}
