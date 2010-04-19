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
package org.topcased.requirement.core.handlers;

import java.util.List;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CutToClipboardCommand;

/**
 * This class defines the EMF <b>cut</b> command
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public class CutHandler extends RequirementAbstractEMFCommandHandler
{

    /**
     * @see org.topcased.requirement.core.handlers.RequirementAbstractEMFCommandHandler#getCommand()
     */
    @Override
    public Class< ? extends Command> getCommand()
    {
        return CutToClipboardCommand.class;
    }

    /**
     * @see org.topcased.requirement.core.handlers.RequirementAbstractEMFCommandHandler#getParam()
     */
    @Override
    public CommandParameter getParam()
    {
        if (((EvaluationContext)evt.getApplicationContext()).getDefaultVariable() instanceof List<?>)
        {
            return new CommandParameter(null, null, ((List<?>)((EvaluationContext)evt.getApplicationContext()).getDefaultVariable()));
        }
        else
        {
            return null;
        }
        
    }

}
