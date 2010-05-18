/***********************************************************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.commands;

import org.topcased.requirement.core.commands.CreateRequirementCommand;
import org.topcased.requirement.core.utils.RequirementHelper;

/**
 * Command creating one or more current requirements either by drag'n'drop or directly thanks to a Workbench action.<br>
 * 
 * Creation : 11 March 2009<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.5.0
 */
public class CreateCurrentReqCommand extends CreateRequirementCommand
{

    /**
     * Constructor
     * 
     * @param title The title of the command
     */
    public CreateCurrentReqCommand(String title)
    {
        super(title);
    }

    /**
     * @see org.eclipse.emf.common.command.Command#execute()
     */
    public void execute()
    {
        globalCmd = RequirementHelper.INSTANCE.createCurrentRequirements(getRequirements(), getTarget());
        
    }
}
