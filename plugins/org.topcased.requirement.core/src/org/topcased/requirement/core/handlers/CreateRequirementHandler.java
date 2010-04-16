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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.topcased.modeler.edit.IModelElementEditPart;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.commands.CreateRequirementCommand;
import org.topcased.requirement.core.extensions.DropRestrictionManager;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Implements FR#2066
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public abstract class CreateRequirementHandler extends AbstractHandler
{
    /** boolean to check if the commands are enabled */
    private boolean isEnabled = false;

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {

        IEditorPart part = HandlerUtil.getActiveEditor(event);
        if (part instanceof Modeler)
        {
            Modeler modeler = (Modeler) part;

            // Get the content of the selection
            IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
            for (Object obj : selection.toList())
            {
                if (obj instanceof IModelElementEditPart)
                {
                    IModelElementEditPart modelElem = (IModelElementEditPart) obj;
                    CreateRequirementCommand createCmd = getCreateCommand();
                    createCmd.setTarget(modelElem.getEObject());
                    modeler.getEditingDomain().getCommandStack().execute(createCmd);
                }
            }
        }
        return null;
    }

    protected abstract CreateRequirementCommand getCreateCommand();

    /**
     * @see org.eclipse.core.commands.AbstractHandler#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        return isEnabled;
    }

    /**
     * set commands enabled when the current diagram has a link with requirements and the object selected is enable for
     * requirement creation
     * 
     * @see org.eclipse.core.commands.AbstractHandler#setEnabled(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public void setEnabled(Object evaluationContext)
    {
        String fileExtension = Modeler.getCurrentIFile().getFileExtension();

        // Check if the modeler is linked to requirements
        if (RequirementUtils.getRequirementModel(Utils.getCurrentModeler().getEditingDomain()) != null)
        {
            EvaluationContext eval = (EvaluationContext) evaluationContext;
            if (eval.getDefaultVariable() instanceof List< ? >)
            {
                List<Object> vars = (List<Object>) eval.getDefaultVariable();
                for (Object var : vars)
                {
                    if (var instanceof IModelElementEditPart)
                    {
                        // Check if the selected element has drop allowed for it
                        IModelElementEditPart editPart = (IModelElementEditPart) var;
                        isEnabled = DropRestrictionManager.getInstance().isDropAllowed(fileExtension, editPart.getEObject());
                        break;
                    }
                }
            }
        }
        else
        {
            isEnabled = false;
        }
    }
}
