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
 * The process for creating a requirement is this way:
 * 1) Create the requirement
 * 2) Attach it to the requirement model
 * 3) In case this is the first requirement of the requirement model, we call setFirstIndex method
 * 4) Call getCurrentIndex for this requirement
 * 5) Set the requirement identifier
 * 6) Call increaseIndexWhenCreateRequirement method
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public interface IRequirementCountingAlgorithm
{

    /**
     * Get the current index of your counting algorithm, the requirement in parameter
     * is the newly created requirement (or the requirement to rename) with NO new identifier put at this point
     * 
     * @param currentRequirement the newly created requirement
     * @return current index of your counting algorithm
     */
    public long getCurrentIndex(Requirement currentRequirement);

    /**
     * Implement here the increasing of your index on current requirement creation
     * This is an AFTER requirement creation (or requirement renaming) increasing method
     * 
     * @param createdRequirement the newly created requirement
     * @param index the current index
     */
    public void increaseIndexWhenCreateRequirement(Requirement createdRequirement, long index);

    /**
     * Set the first index here when the first current requirement is created
     * 
     * @param firstCreatedRequirement the first requirement of the model
     */
    public void setFirstIndex(Requirement firstCreatedRequirement);
}
