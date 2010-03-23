/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/
package org.topcased.requirement.core.utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.extensions.IRequirementIdentifierDefinition;
import org.topcased.requirement.core.preferences.ComputeRequirementIdentifier;
import org.topcased.requirement.core.preferences.NamingRequirementPreferenceHelper;

/**
 * Default behavior for the requirement counting algorithm
 * This algorithm put the index step on the root of the hierarchical element tree.
 * This provide a global increment for all the requirements
 * 
 * @author maudrain
 *
 */
public class DefaultRequirementIdentifierDefinition implements IRequirementIdentifierDefinition
{

    private static final String DEFAULT_HIERARCHICAL_ELEMENT_NAME = "xxx";

    /** the shared instance */
    private static DefaultRequirementIdentifierDefinition definition;

    /** The first increment when the root is empty */
    private static long firstIncrement;

    /**
     * Private constructor
     */
    private DefaultRequirementIdentifierDefinition()
    {
        // avoid instantiation
    }

    /**
     * Gets the shared instance.
     * 
     * @return the default requirement identifier definition
     */
    public static DefaultRequirementIdentifierDefinition getInstance()
    {
        if (definition == null)
        {
            definition = new DefaultRequirementIdentifierDefinition();
        }
        return definition;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementIdentifierDefinition#increaseIndexWhenCreateRequirement(org.topcased.requirement.HierarchicalElement, long)
     */
    public long increaseIndexWhenCreateRequirement(HierarchicalElement hierarchicalElementContainer, long index)
    {
        HierarchicalElement root = RequirementHelper.INSTANCE.getHierarchicalElementRoot();
        index += NamingRequirementPreferenceHelper.getRequirementStep();
        if (root != null)
        {
            if (firstIncrement == -1)
            {
                root.setNextReqIndex(index);
            }
            else
            {
                index = index + firstIncrement;
                root.setNextReqIndex(index);
                firstIncrement = -1;
            }
        }
        else
        {
            firstIncrement = index;
        }
        return index;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementIdentifierDefinition#resetIndexWhenCreateNewContainer(org.topcased.requirement.HierarchicalElement, long)
     */
    public long resetIndexWhenCreateNewContainer(HierarchicalElement hierarchicalElementContainer, long index)
    {
        return index;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementIdentifierDefinition#getCurrentIndex(org.topcased.requirement.HierarchicalElement)
     */
    public long getCurrentIndex(HierarchicalElement hierarchicalElementContainer)
    {
        HierarchicalElement root = RequirementHelper.INSTANCE.getHierarchicalElementRoot();
        if (root != null)
        {
            return root.getNextReqIndex();
        }
        return 0;
    }

    public Map<String, String> addValuesToPatterns(EditingDomain editingDomain, Map<String, String> alreadyCreatedMap)
    {
        
        // Number's formatter
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(5);
        nf.setGroupingUsed(false);

        Resource requirement = RequirementUtils.getRequirementModel(editingDomain);
        
        //Default key word map
        alreadyCreatedMap.put("{project}", ((RequirementProject) requirement.getContents().get(0)).getIdentifier());
        alreadyCreatedMap.put("{resource name}", requirement.getURI().lastSegment().replace(".requirement", ""));
        alreadyCreatedMap.put("{hierarchical element}", getHierarchicalElementIdentifier(ComputeRequirementIdentifier.INSTANCE.getIdentifierHierarchicalElement()));
        alreadyCreatedMap.put("{upstream requirement ident}", ComputeRequirementIdentifier.INSTANCE.getIdentifierUpstreamIdent());
        alreadyCreatedMap.put("{number}", nf.format(ComputeRequirementIdentifier.INSTANCE.getIdentifierRequirementIndex()));
        
        return alreadyCreatedMap;
    }

    public List<String> addPatterns()
    {
        List<String> keyWords = new ArrayList<String>();
        keyWords.add("{project}");
        keyWords.add("{resource name}");
        keyWords.add("{hierarchical element}");
        keyWords.add("{upstream requirement ident}");
        keyWords.add("{number}");
        return keyWords;
    }
    
    /**
     * Get the identifier of the target element
     * 
     * @param hierarchicalElt : the target hierarchical element
     * 
     * @return the identifier of the target element
     */
    private String getHierarchicalElementIdentifier(HierarchicalElement hierarchicalElt)
    {
        String result = "";

        if (hierarchicalElt.getElement() != null)
        {
            EObject obj = hierarchicalElt.getElement();
            for (EAttribute attribute : obj.eClass().getEAllAttributes())
            {
                if (attribute.getName().equals(EcorePackage.eINSTANCE.getENamedElement_Name().getName()))
                {
                    result = (String) obj.eGet(attribute);
                    break;
                }
            }
        }
        else
        {
            result += DEFAULT_HIERARCHICAL_ELEMENT_NAME;
        }

        return result;
    }

    
}
