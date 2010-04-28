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
 * This algorithm attach the counting index on the hierarchical element root of the requirement model.
 * Each current requirement created in whatever container got his index increased
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
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
     * @see org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm#increaseIndexWhenCreateRequirement(org.topcased.requirement.Requirement,
     *      long)
     */
    public void increaseIndexWhenCreateRequirement(Requirement createdRequirement, long index)
    {
        HierarchicalElement root = RequirementHelper.INSTANCE.getHierarchicalElementRoot();
        index += ComputeRequirementIdentifier.getRequirementStep();
        root.setNextReqIndex(index);
    }

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm#setFirstIndex(org.topcased.requirement.Requirement)
     */
    public void setFirstIndex(Requirement firstCreatedRequirement)
    {
        HierarchicalElement root = RequirementHelper.INSTANCE.getHierarchicalElementRoot();
        if (root.getNextReqIndex() == 0)
        {
            // First time when the default step value hasn't been put
            root.setNextReqIndex(ComputeRequirementIdentifier.getRequirementStep());
        }
    }

}
