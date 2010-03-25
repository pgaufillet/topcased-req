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

import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm;
import org.topcased.requirement.core.preferences.NamingRequirementPreferenceHelper;

/**
 * @author maudrain
 *
 */
public class DefaultRequirementCountingAlgorithm implements IRequirementCountingAlgorithm
{
    /** The first increment when the root has not already been created */
    private static long firstIncrement = 0;
    
    /**
     * @see org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm#getCurrentIndex(org.topcased.requirement.Requirement)
     */
    public long getCurrentIndex(Requirement currentRequirement)
    {
        HierarchicalElement root = RequirementHelper.INSTANCE.getHierarchicalElementRoot();
        if (root != null)
        {
            if (firstIncrement == -1)
            {
                return root.getNextReqIndex();
            }
            else
            {
                root.setNextReqIndex(firstIncrement);
                firstIncrement = -1;
                return root.getNextReqIndex();
            }
        }
        else
        {
            firstIncrement = NamingRequirementPreferenceHelper.getRequirementStep(); 
            return firstIncrement;
        }

    }

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm#increaseIndexWhenCreateRequirement(org.topcased.requirement.Requirement, long)
     */
    public void increaseIndexWhenCreateRequirement(Requirement createdRequirement, long index)
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
    }

}
