package org.topcased.requirement.core.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.core.commands.ManageDeletedChapterCommand;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.utils.RequirementUtils;

public abstract class DeleteCurrentRequirementsHandler extends AbstractHandler
{

    public DeleteCurrentRequirementsHandler()
    {
        super();
    }

    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
        if (services != null)
        {
            EditingDomain domain = services.getEditingDomain(editor);
            CommandStack stack = domain.getCommandStack();
            ManageDeletedChapterCommand cmd = new ManageDeletedChapterCommand(domain);
            if (cmd != null && cmd.canExecute())
            {
                stack.execute(cmd);
            }
        }
        return null;
    }

}