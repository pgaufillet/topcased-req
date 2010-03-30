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

import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.core.preferences.ComputeRequirementIdentifier;
import org.topcased.requirement.core.utils.RequirementHelper;

/**
 * @author maudrain
 *
 */
public class DefaultRequirementCountingAlgorithm implements IRequirementCountingAlgorithm
{    
    /**
     * @see org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm#getCurrentIndex(org.topcased.requirement.Requirement)
     */
    public long getCurrentIndex(Requirement currentRequirement)
    {
        HierarchicalElement root = RequirementHelper.INSTANCE.getHierarchicalElementRoot();
        return root.getNextReqIndex();
    }

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm#increaseIndexWhenCreateRequirement(org.topcased.requirement.Requirement, long)
     */
    public void increaseIndexWhenCreateRequirement(Requirement createdRequirement, long index)
    {
        HierarchicalElement root = RequirementHelper.INSTANCE.getHierarchicalElementRoot();
        index += ComputeRequirementIdentifier.getRequirementStep();
        root.setNextReqIndex(index);
    }

    public void setFirstIndex(Requirement firstCreatedRequirement)
    {
        HierarchicalElement root = RequirementHelper.INSTANCE.getHierarchicalElementRoot();
        if (root.getNextReqIndex() == 0)
        {
          //First time when the default step value hasn't been put
            root.setNextReqIndex(ComputeRequirementIdentifier.getRequirementStep());
        }
    }

}
