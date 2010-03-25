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

import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm;
import org.topcased.requirement.core.preferences.NamingRequirementPreferenceHelper;

/**
 * @author maudrain
 *
 */
public class SAMRequirementCountingAlgorithm implements IRequirementCountingAlgorithm
{
    
    /**
     * @see org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm#getCurrentIndex(org.topcased.requirement.Requirement)
     */
    public long getCurrentIndex(Requirement currentRequirement)
    {
        if (currentRequirement.eContainer() instanceof HierarchicalElement)
        {
            HierarchicalElement parent = (HierarchicalElement)currentRequirement.eContainer();
            if (parent.getNextReqIndex() == 0)
            {
                //First time when the default step value hasn't been put
                parent.setNextReqIndex(NamingRequirementPreferenceHelper.getRequirementStep());
                return parent.getNextReqIndex();
            }
            else
            {
                return parent.getNextReqIndex();                
            }          
        }
        return 0;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm#increaseIndexWhenCreateRequirement(org.topcased.requirement.Requirement, long)
     */
    public void increaseIndexWhenCreateRequirement(Requirement createdRequirement, long index)
    {
        index += NamingRequirementPreferenceHelper.getRequirementStep();
        if (createdRequirement.eContainer() != null && createdRequirement.eContainer() instanceof HierarchicalElement)
        {
            HierarchicalElement hierarchicalElementContainer = (HierarchicalElement)createdRequirement.eContainer();
            hierarchicalElementContainer.setNextReqIndex(index);
        }
    }

}
