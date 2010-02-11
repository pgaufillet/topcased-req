/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.core.Messages;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * Action available from the Current View, allowing to move (up or down) a {@link Requirement} or a
 * {@link HierarchicalElement}.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class MoveAction extends RequirementAction
{
    /** give the orientation */
    private Boolean up;

    /**
     * Constructor
     * 
     * @param page A reference to the page proposing this action.
     * @param up Up or Down ?
     */
    public MoveAction(CurrentPage page, Boolean orientation)
    {
        super(page.getViewer(), page.getEditingDomain());
        up = orientation;
        if (up)
        {
            setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/up.gif")); //$NON-NLS-1$
            setText(Messages.getString("MoveAction.0")); //$NON-NLS-1$
        }
        else
        {
            setText(Messages.getString("MoveAction.1")); //$NON-NLS-1$
            setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/down.gif")); //$NON-NLS-1$
        }
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run()
    {
        if (checkSelection())
        {
            CompoundCommand compoundCmd = buildCommands();

            if (!compoundCmd.isEmpty() && compoundCmd.canExecute())
            {
                editingDomain.getCommandStack().execute(compoundCmd);
            }
        }
    }

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

}
