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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;

/**
 * This class defines and run an EMF command.<br>
 * The execution sequence is so defined :
 * <ul>
 * <li>preAction()</li>
 * <li>run()</li>
 * <li>postAction()</li>
 * <li>endAction()</li>
 * </ul>
 * 
 * @author christophe.mertz@c-s.fr
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public abstract class RequirementAbstractEMFCommandHandler extends AbstractHandler
{
    private CompoundCommand compoundCmd;
        
    protected EditingDomain editingDomain;
    
    protected ExecutionEvent evt;
        
    
    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        evt = event;
        compoundCmd = new CompoundCommand();
        editingDomain = Utils.getCurrentModeler().getEditingDomain();
        ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
        try
        {
            progressDialog.run(false, false, new IRunnableWithProgress()
            {
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                    RequirementAbstractEMFCommandHandler.this.run(monitor);
                }
            });
        }
        catch (InvocationTargetException ite)
        {
            RequirementCorePlugin.log(ite);
        }
        catch (InterruptedException ie)
        {
            RequirementCorePlugin.log(ie);
        }
        return null;
    }
    
    /**
     * Executes the command
     * 
     * @param monitor the progress monitor
     */
    protected void run(final IProgressMonitor monitor)
    {
        monitor.beginTask(Messages.getString("RequirementAbstractEMFCommandHandler.0"), 5); //$NON-NLS-1$
        monitor.worked(1);

        monitor.setTaskName(Messages.getString("RequirementAbstractEMFCommandHandler.1")); //$NON-NLS-1$
        preAction(compoundCmd);
        monitor.worked(1);

        Command cmd = editingDomain.createCommand(getCommand(), getParam());
        compoundCmd.appendIfCanExecute(cmd);

        monitor.setTaskName(Messages.getString("RequirementAbstractEMFCommandHandler.2")); //$NON-NLS-1$
        postAction(compoundCmd);
        monitor.worked(1);

        monitor.setTaskName(Messages.getString("RequirementAbstractEMFCommandHandler.3")); //$NON-NLS-1$
        if (!compoundCmd.isEmpty() && compoundCmd.canExecute())
        {
            // Execute it.
            editingDomain.getCommandStack().execute(compoundCmd);
        }
        monitor.worked(1);

        endAction(compoundCmd);
        monitor.worked(1);
        monitor.done();
    }
    
    /**
     * Method call before the execution of the EMF command.<br>
     * This method is intended to be sub-classed.
     * 
     * @param compoundCommand The compound command in which EMF commands must be stacked.
     */
    public void preAction(CompoundCommand compoundCommand)
    {
        // empty implementation
    }

    /**
     * Method call after the execution of the EMF command.<br>
     * This method is intended to be sub-classed.
     * 
     * @param compoundCommand The compound command in which EMF commands must be stacked.
     */
    public void postAction(CompoundCommand compoundCommand)
    {
        // empty implementation
    }

    /**
     * Method call after the execution of the EMF commands.<br>
     * This method is intended to be sub-classed.
     * 
     * @param compoundCommand The compound command in which EMF commands must be stacked.
     */
    public void endAction(CompoundCommand compoundCommand)
    {
        // empty implementation
    }

    /**
     * @return param the parameter of the command
     */
    public abstract CommandParameter getParam();

    /**
     * @return command the command type
     */
    public abstract Class< ? extends Command> getCommand();
  
}
