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
package org.topcased.requirement.core.extensions;

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
import org.topcased.requirement.core.preferences.ComputeRequirementIdentifier;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Default added variables for the requirement naming pattern in the preference page
 * 
 * @author maudrain
 *
 */
public class DefaultRequirementIdentifierVariables implements IRequirementIdentifierVariables
{

    private static final String DEFAULT_HIERARCHICAL_ELEMENT_NAME = "xxx";

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementIdentifierVariables#setValuesToVariables(org.eclipse.emf.edit.domain.EditingDomain, java.util.Map)
     */
    public Map<String, String> setValuesToVariables(EditingDomain editingDomain, Map<String, String> alreadyCreatedMap)
    {
        
        // Number's formatter
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(5);
        nf.setGroupingUsed(false);

        Resource requirement = RequirementUtils.getRequirementModel(editingDomain);
        
        //Default key word map
        alreadyCreatedMap.put("{project}", ((RequirementProject) requirement.getContents().get(0)).getIdentifier());
        alreadyCreatedMap.put("{resource name}", requirement.getURI().trimFileExtension().lastSegment());
        alreadyCreatedMap.put("{hierarchical element}", getHierarchicalElementIdentifier(ComputeRequirementIdentifier.INSTANCE.getIdentifierHierarchicalElement()));
        alreadyCreatedMap.put("{upstream requirement ident}", ComputeRequirementIdentifier.INSTANCE.getIdentifierUpstreamIdent());
        alreadyCreatedMap.put("{number}", nf.format(ComputeRequirementIdentifier.INSTANCE.getIdentifierRequirementIndex()));
        
        return alreadyCreatedMap;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementIdentifierVariables#addVariables()
     */
    public List<String> addVariables()
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
