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

package org.topcased.requirement.core.wizards;

import org.topcased.requirement.core.wizards.operation.AbstractRequirementModelOperation;
import org.topcased.requirement.core.wizards.operation.MergeRequirementModelOperation;

/**
 * 
 * Defines the wizard for updating Requirement Models.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class MergeRequirementModelWizard extends AbstractRequirementModelWizard
{

    /**
     * Basic Constructor
     * 
     */
    public MergeRequirementModelWizard()
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param theProjectName The name of the existing project
     * @param theProjectDescription The short description of the existing project
     */
    public MergeRequirementModelWizard(String theProjectName, String theProjectDescription)
    {
        this();
        existingRequirementModel = true;
        projectName = theProjectName;
        projectDescription = theProjectDescription;
    }

    /**
     * @see org.topcased.requirement.core.wizards.AbstractRequirementModelWizard#getOperation()
     */
    @Override
    protected AbstractRequirementModelOperation getOperation()
    {
        return new MergeRequirementModelOperation(page.getTargetModelFile(), page.getSourceModelFile(), page.getDestModelFile());
    }
}
