/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Olivier Mélois <a href="mailto:olivier.melois@atos.net">olivier.melois@atos.net</a>"
 * 
 **********************************************************************************************************************/

package org.topcased.requirement.core.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.topcased.requirement.TrashChapter;
import org.topcased.requirement.core.commands.TrashCurrentReqCommand;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This class handles the TrashCurrentReqCommand.
 * 
 * @author Olivier Mélois <a href="mailto:olivier.melois@atos.net">olivier.melois@atos.net</a>"
 * 
 */
public class TrashHandler extends RequirementAbstractEMFCommandHandler
{

    /**
     * Not used anyway, as the run() method is overridden.
     * 
     * @see org.topcased.requirement.core.handlers.RequirementAbstractEMFCommandHandler#getCommand()
     */
    @Override
    public Class< ? extends Command> getCommand()
    {
        return TrashCurrentReqCommand.class;
    }

    /**
     * Not used anyway, as the run() method is overridden.
     * 
     * @see org.topcased.requirement.core.handlers.RequirementAbstractEMFCommandHandler#getParam()
     */
    @Override
    public CommandParameter getParam()
    {
        if (((EvaluationContext) evt.getApplicationContext()).getDefaultVariable() instanceof List< ? >)
        {
            return new CommandParameter(null, null, ((List< ? >) ((EvaluationContext) evt.getApplicationContext()).getDefaultVariable()));
        }
        else
        {
            return null;
        }

    }

    /**
     * Creates a TrashCurrentReqCommand and executes it in the command stack.
     */
    @Override
    protected void run(final IProgressMonitor monitor)
    {
        monitor.beginTask(Messages.getString("TrashHandler.0"), 2); //$NON-NLS-1$
        monitor.worked(1);

        Command cmd = new TrashCurrentReqCommand(editingDomain, getSelectedEObjects());
        compoundCmd.appendIfCanExecute(cmd);

        if (!compoundCmd.isEmpty() && compoundCmd.canExecute())
        {
            // Execute it.
            editingDomain.getCommandStack().execute(compoundCmd);
        }

        monitor.worked(1);
        monitor.done();
    }

    /**
     * Gets a list of eObjects from the ISelection (selection made by the user)
     * 
     * @return
     */
    protected List<EObject> getSelectedEObjects()
    {
        List<EObject> eObjects = new ArrayList<EObject>();
        ISelection selection = null;

        // Get current selection
        selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

        // Get first element if the selection is an IStructuredSelection
        if (selection instanceof IStructuredSelection)
        {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            Iterator< ? > iter = structuredSelection.iterator();
            while (iter.hasNext())
            {
                Object current = iter.next();
                EObject toAdd = null;
                if (current instanceof IAdaptable)
                {
                    IAdaptable adap = (IAdaptable) current;
                    toAdd = (EObject) adap.getAdapter(EObject.class);
                }
                else if (current instanceof EObject)
                {
                    toAdd = (EObject) current;
                }
                if (toAdd != null)
                {
                    eObjects.add(toAdd);
                }
            }
        }

        return eObjects;
    }

    /**
     * Allows the execution only when no element from the selection is in the trash.
     */
    @Override
    public boolean isEnabled()
    {
        boolean result = super.isEnabled();
        TrashChapter trashChapter = RequirementUtils.getTrashChapter(editingDomain);
        for (EObject eObject : this.getSelectedEObjects()){
            result &= (eObject.eContainer() != trashChapter);
        }
        return result;
    }
}
