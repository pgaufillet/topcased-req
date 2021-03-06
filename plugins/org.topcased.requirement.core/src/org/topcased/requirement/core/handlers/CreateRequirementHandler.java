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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.core.commands.CreateRequirementCommand;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Implements FR#2066 Execute a requirement creation in the current view from the modeler popup menu or from the current
 * view popup menu
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public abstract class CreateRequirementHandler extends AbstractHandler
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
            EditingDomain domain = services.getEditingDomain(editor);
            // Get the current selection
            List< ? > elements = ((List< ? >) ((EvaluationContext) event.getApplicationContext()).getDefaultVariable());
            for (Object obj : elements)
            {
                CreateRequirementCommand createCmd = getCreateCommand();
                if (obj instanceof EditPart)
                {
                    EObject modelElem = services.getEObject((EditPart) obj);
                    createCmd.setTarget(modelElem);
                }
                else if (obj instanceof Requirement)
                {
                    HierarchicalElement elt = ((HierarchicalElement) ((Requirement) obj).eContainer());
                    createCmd.setTarget(elt.getElement());
                }
                else if (obj instanceof HierarchicalElement)
                {
                    createCmd.setTarget(((HierarchicalElement) obj).getElement());
                }

                if (createCmd != null && createCmd.canExecute())
                {
                    domain.getCommandStack().execute(createCmd);
                }
            }
        }
        return null;
    }

    /**
     * @return the command who create a requirement (current or anonymous)
     */
    protected abstract CreateRequirementCommand getCreateCommand();

}
