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

import org.eclipse.emf.common.util.URI;
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
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public final class DefaultRequirementIdentifierVariables implements IRequirementIdentifierVariables
{

    public static final String PROJECT_VAR = "{project name}"; //$NON-NLS-1$
 
    public static final String RESOURCE_VAR = "{resource name}"; //$NON-NLS-1$

    public static final String HIERARCHICAL_ELEMENT_VAR = "{hierarchical element}"; //$NON-NLS-1$

    public static final String UPSTREAM_REQUIREMENT_VAR = "{upstream requirement}"; //$NON-NLS-1$

    public static final String INDEX_VAR = "{index}"; //$NON-NLS-1$

    private static final String DEFAULT_HIERARCHICAL_ELEMENT_NAME = "xxx"; //$NON-NLS-1$

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementIdentifierVariables#setValuesToVariables(org.eclipse.emf.edit.domain.EditingDomain,
     *      java.util.Map)
     */
    public Map<String, String> setValuesToVariables(EditingDomain editingDomain, Map<String, String> alreadyCreatedMap)
    {

        // Number's formatter
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(5);
        nf.setGroupingUsed(false);

        Resource requirement = RequirementUtils.getRequirementModel(editingDomain);

        // Default key word map
        alreadyCreatedMap.put(PROJECT_VAR, ((RequirementProject) requirement.getContents().get(0)).getIdentifier());
        alreadyCreatedMap.put(RESOURCE_VAR, URI.decode(requirement.getURI().trimFileExtension().lastSegment()));
        alreadyCreatedMap.put(HIERARCHICAL_ELEMENT_VAR, getHierarchicalElementIdentifier(ComputeRequirementIdentifier.INSTANCE.getIdentifierHierarchicalElement()));
        alreadyCreatedMap.put(UPSTREAM_REQUIREMENT_VAR, ComputeRequirementIdentifier.INSTANCE.getIdentifierUpstreamIdent());
        alreadyCreatedMap.put(INDEX_VAR, nf.format(ComputeRequirementIdentifier.INSTANCE.getIdentifierRequirementIndex()));

        return alreadyCreatedMap;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementIdentifierVariables#getVariables()
     */
    public List<String> getVariables()
    {
        List<String> keyWords = new ArrayList<String>();
        keyWords.add(PROJECT_VAR);
        keyWords.add(RESOURCE_VAR);
        keyWords.add(HIERARCHICAL_ELEMENT_VAR);
        keyWords.add(UPSTREAM_REQUIREMENT_VAR);
        keyWords.add(INDEX_VAR);
        return keyWords;
    }

    /**
     * Gets the identifier of the target element.
     * 
     * @param hierarchicalElt : the target hierarchical element
     * @return the identifier of the target element
     */
    private String getHierarchicalElementIdentifier(HierarchicalElement hierarchicalElt)
    {
        String result = ""; //$NON-NLS-1$

        if (hierarchicalElt != null)
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
