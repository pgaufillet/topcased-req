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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.views.current.CurrentPage;

public class AbstractMoveHandler extends AbstractHandler
{
    private IStructuredSelection selection;
    
    private CurrentPage page;
    
    protected boolean up;
    /**
     * Builds commands in iterating over the elements contained in the copy of the selection.
     * 
     * @param compoundCmd The compound command in which intermediate commands must be added.
     * @param selected The list of selected element
     */
    @SuppressWarnings("unchecked")
    private CompoundCommand buildCommands()
    {
        CompoundCommand cmd = new CompoundCommand();
        List<Object> selected = new ArrayList<Object>(selection.toList());

        if (!up)
        {
            // the selection must be inverted to be consistent with commands
            Collections.reverse(selected);
        }
        
        for (Object currObject : selected)
        {
            if (currObject instanceof Requirement)
            {
                cmd.setLabel(Messages.getString("MoveAction.2"));//$NON-NLS-1$
                cmd.appendIfCanExecute(RequirementHelper.INSTANCE.move((Requirement) currObject, up));
            }
            else if (currObject instanceof HierarchicalElement)
            {
                cmd.setLabel(Messages.getString("MoveAction.3"));//$NON-NLS-1$
                cmd.appendIfCanExecute(RequirementHelper.INSTANCE.move((HierarchicalElement) currObject, up));
            }
        }
        return cmd;
    }

    /**
     * Checks the content of the selection.<br>
     * First, we check that the selection is not empty. Then, we inspect the content of the selection : the selection
     * must contain only objects of the same type. Nothing will happen if the selection contains both Requirement and
     * Hierarchical Element.
     * 
     * @return <code>true</code> if the selection is O.K, <code>false</code> when the selection is K.O.
     */
    private boolean checkSelection()
    {
        if (selection != null && !selection.isEmpty())
        {
            Object previousInSelection = selection.getFirstElement();
            for (Object inSelection : selection.toList())
            {
                if (!previousInSelection.getClass().getSuperclass().equals(inSelection.getClass().getSuperclass()))
                {
                    return false;
                }
                previousInSelection = inSelection;
            }
            return true;
        }
        return false;
    }

    
    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        page = RequirementHelper.INSTANCE.getCurrentPage();
        //Gather the current selection
        if (page != null)
        {
            selection = (IStructuredSelection) page.getViewer().getSelection();
        }
        
        if (checkSelection())
        {
            CompoundCommand compoundCmd = buildCommands();

            if (!compoundCmd.isEmpty() && compoundCmd.canExecute())
            {
                Utils.getCurrentModeler().getEditingDomain().getCommandStack().execute(compoundCmd);
            }
        }
        return null;
    }

}