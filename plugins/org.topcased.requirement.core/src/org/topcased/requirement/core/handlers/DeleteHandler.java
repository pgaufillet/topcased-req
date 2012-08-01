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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.core.commands.DeleteRequirementCommand;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This class defines the EMF <b>delete</b> command
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class DeleteHandler extends AbstractHandler
{
    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);

        if (services != null && ((EvaluationContext) event.getApplicationContext()).getDefaultVariable() instanceof List< ? >)
        {
            EditingDomain editingDomain = services.getEditingDomain(editor);
            // Get the current selection
            List< ? > elements = ((List< ? >) ((EvaluationContext) event.getApplicationContext()).getDefaultVariable());
            CompoundCommand compoundCmd = new CompoundCommand(Messages.getString("DeleteHandler.0")); //$NON-NLS-1$

            Command delete = new DeleteRequirementCommand(editingDomain, elements);
            compoundCmd.appendIfCanExecute(delete);

            if (!compoundCmd.isEmpty() && compoundCmd.canExecute())
            {
                editingDomain.getCommandStack().execute(compoundCmd);
            }
        }
        return null;
    }
}
