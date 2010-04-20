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
package org.topcased.requirement.sam.actions;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.topcased.requirement.core.commands.CreateCurrentReqCommand;
import org.topcased.requirement.core.commands.CreateRequirementCommand;
import org.topcased.requirement.core.extensions.ISpecificDropAction;
import org.topcased.requirement.sam.internal.Messages;
import org.topcased.requirement.sam.dialogs.ChooseTargetDialog;
import org.topcased.sam.Flow;

/**
 * Handles specific processing on {@link Flow} when a drop operation is detected on it.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * @see {@link ISpecificDropAction}
 */
public class HandleDropOnFlow implements ISpecificDropAction
{

    /**
     * @see org.topcased.requirement.core.extensions.IHandleSpecificActions#createSpecificDropAction(org.eclipse.emf.ecore.EObject)
     */
    public Command createSpecificDropAction(Collection< ? > source, EObject target)
    {
        if (target instanceof Flow)
        {
            Flow flow = (Flow) target;
            
            // the dialog should be opened to ask the user if he wants to attach requirement to flow goup.
            Dialog dialog = new ChooseTargetDialog(Display.getCurrent().getActiveShell(), flow);

            if (dialog.open() == Dialog.OK)
            {
                CreateRequirementCommand dropCmd = new CreateCurrentReqCommand(Messages.getString("HandleDropOnFlow.0"));
                dropCmd.setRequirements(source);

                int result = dialog.getReturnCode();
                if (result == 1)
                {
                    // in case of a drop on a flow group
                    dropCmd.setTarget(flow.getGroup());
                    return dropCmd;
                }
                else
                {
                    // in case of a drop on a flow
                    dropCmd.setTarget(flow);
                    return dropCmd;
                }
            }
        }
        return UnexecutableCommand.INSTANCE;
    }
}
