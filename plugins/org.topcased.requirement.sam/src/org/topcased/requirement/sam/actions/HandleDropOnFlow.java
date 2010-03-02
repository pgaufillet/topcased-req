package org.topcased.requirement.sam.actions;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.topcased.requirement.core.Messages;
import org.topcased.requirement.core.commands.CreateCurrentReqCommand;
import org.topcased.requirement.core.commands.CreateRequirementCommand;
import org.topcased.requirement.core.extensions.ISpecificDropAction;
import org.topcased.requirement.sam.dialogs.ChooseTargetDialog;
import org.topcased.sam.Flow;

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
                Command dropCmd = new CreateCurrentReqCommand(Messages.getString("CreateCurrentRequirementAction.0"));
                ((CreateRequirementCommand) dropCmd).setRequirements(source);
                
                int result = dialog.getReturnCode();                
                if (result == 1)
                {
                    //in case of a drop on a flow group
                    ((CreateRequirementCommand) dropCmd).setTarget(flow.getGroup());
                    return dropCmd;
                }
                else
                {
                    //in case of a drop on a flow
                    ((CreateRequirementCommand) dropCmd).setTarget(flow);
                    return dropCmd;
                }
            }
            else
            {
                return UnexecutableCommand.INSTANCE;
            }
        }
        return UnexecutableCommand.INSTANCE;
    }
}
