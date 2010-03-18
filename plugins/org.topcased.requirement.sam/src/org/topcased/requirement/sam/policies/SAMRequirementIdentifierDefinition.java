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

package org.topcased.requirement.sam.policies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.extensions.IRequirementIdentifierDefinition;
import org.topcased.requirement.core.preferences.NamingRequirementPreferenceHelper;
import org.topcased.requirement.core.utils.RequirementUtils;

public class SAMRequirementIdentifierDefinition implements IRequirementIdentifierDefinition
{

    public long increaseIndexWhenCreateRequirement(HierarchicalElement hierarchicalElementContainer, long index)
    {
        if (hierarchicalElementContainer != null)
        {
            index += NamingRequirementPreferenceHelper.getRequirementStep();
            hierarchicalElementContainer.setNextReqIndex(index);
        }
        return index;
    }

    public long resetIndexWhenCreateNewContainer(HierarchicalElement hierarchicalElementContainer, long index)
    {
        return index;
    }

    public long getCurrentIndex(HierarchicalElement hierarchicalElementContainer)
    {
        return hierarchicalElementContainer.getNextReqIndex();
    }


    public Map<String, String> addValuesToPatterns(EditingDomain editingDomain, Map<String, String> alreadyCreatedMap)
    {
        Resource requirement = RequirementUtils.getRequirementModel(editingDomain);
        
        alreadyCreatedMap.put("{Upstream Model Ident}", ((RequirementProject) requirement.getContents().get(0)).getUpstreamModel().getIdent());
        return alreadyCreatedMap;
    }

    public List<String> addPatterns()
    {   
        List<String> keyWords = new ArrayList<String>();
        keyWords.add("{Upstream Model Ident}");
        return keyWords;
    }

//    public void decreaseIndexWhenDeleteRequirement(Requirement theDeletedRequirement)
//    {
//        long index = ((HierarchicalElement) theDeletedRequirement.eContainer()).getNextReqIndex();
//        if (theDeletedRequirement.getIdentifier().contains(String.valueOf(index)))
//        {
//            long nextIndex = index - NamingRequirementPreferenceHelper.getRequirementStep();
//            ((HierarchicalElement) theDeletedRequirement.eContainer()).setNextReqIndex(nextIndex);
//        }
//    }

}
