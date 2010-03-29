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

import org.topcased.requirement.Requirement;

/**
 * This interface defines the behavior to implement for the "requirementCountingAlgorithm" extension point 
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public interface IRequirementCountingAlgorithm
{

    /**
     * Get the current index of your counting algorithm For the Default counting algorithm, there is no need for a
     * hierarchical element parameter as the index is stored somewhere else
     * 
     * @param requirementContainer if you choose to store the index in the hierarchical element
     * @return current index of your counting algorithm
     */
    public long getCurrentIndex(Requirement currentRequirement);

    /**
     * Implement here the increasing of your index on current requirement creation
     * 
     * @param requirementContainer if you choose to store the index in the hierarchical element
     * @param index the current index
     */
    public void increaseIndexWhenCreateRequirement(Requirement createdRequirement, long index);

    /**
     * Implement here the decreasing of your index on current requirement deletion
     * 
     * @param theDeletedRequirement deleted requirement
     * @param index the current index
     */
    // public void decreaseIndexWhenDeleteRequirement(Requirement theDeletedRequirement);

    /**
     * Set the first index here before the first current requirement is created
     * 
     * @param firstCreatedRequirement the first requirement of the model
     */
    public void setFirstIndex(Requirement firstCreatedRequirement);
}
