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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This class defines and run an EMF command.<br>
 * The execution sequence is so defined :
 * <ul>
 * <li>initialize()</li>
 * <li>run()</li>
 * <li>postAction()</li>
 * </ul>
 * 
 * @author christophe.mertz@c-s.fr
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public abstract class RequirementAbstractEMFAction extends Action
{
    private CommandParameter param;

    private Class< ? extends Command> command;

    private IStructuredSelection selection;

    private EditingDomain editingDomain;

    private CompoundCommand compoundCmd;

    /**
     * Constructor
     * 
     * @param title The action title
     * @param sel The current selection done.
     * @param ed The editing domain
     */
    public RequirementAbstractEMFAction(String title, IStructuredSelection sel, EditingDomain ed)
    {
        super(title);
        selection = sel;
        editingDomain = ed;
        compoundCmd = new CompoundCommand(title);
    }

    /**
     * Abstract method call before the execution of the EMF command
     */
    public abstract void initialize();

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
     */
    public void endAction(CompoundCommand compoundCommand)
    {
        // empty implementation
    }

    /**
     * @see org.eclipse.jface.action.Action#isEnabled() <bR>
     * FIXME : Provide a better solution for handling update of requirement models.
     */
    @Override
    public boolean isEnabled()
    {
        Resource requirementModel = RequirementUtils.getRequirementModel(editingDomain);
        if (requirementModel != null)
        {
            for (Requirement aReq : RequirementUtils.getAllCurrents(requirementModel))
            {
                if (aReq instanceof CurrentRequirement && ((CurrentRequirement) aReq).isImpacted())
                {
                    return false;
                }
            }
        }
        return super.isEnabled();
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run()
    {
        ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
        try
        {
            progressDialog.run(true, false, new IRunnableWithProgress()
            {
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                    RequirementAbstractEMFAction.this.run(monitor);
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
    }

    /**
     * Executes the delete command
     * 
     * @param monitor the progress monitor
     */
    protected void run(final IProgressMonitor monitor)
    {
        monitor.beginTask(Messages.getString("RequirementAbstractEMFAction.0"), 5); //$NON-NLS-1$
        monitor.setTaskName(Messages.getString("RequirementAbstractEMFAction.1")); //$NON-NLS-1$
        initialize();
        monitor.worked(1);

        if (command != null)
        {
            monitor.setTaskName(Messages.getString("RequirementAbstractEMFAction.2")); //$NON-NLS-1$
            preAction(compoundCmd);
            monitor.worked(1);
           
            compoundCmd.appendIfCanExecute(editingDomain.createCommand(getCommand(), getParam()));

            monitor.setTaskName(Messages.getString("RequirementAbstractEMFAction.3")); //$NON-NLS-1$
            postAction(compoundCmd);
            monitor.worked(1);

            monitor.setTaskName(Messages.getString("RequirementAbstractEMFAction.4")); //$NON-NLS-1$
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
    }

    /**
     * Getter of the current selection
     * 
     * @return The current selection
     */
    public IStructuredSelection getSelection()
    {
        return selection;
    }

    /**
     * Getter of the editing domain
     * 
     * @return The editing domain
     */
    public EditingDomain getEditingDomain()
    {
        return editingDomain;
    }

    public CommandParameter getParam()
    {
        return param;
    }

    public void setParam(CommandParameter param)
    {
        this.param = param;
    }

    public Class< ? extends Command> getCommand()
    {
        return command;
    }

    public void setCommand(Class< ? extends Command> command)
    {
        this.command = command;
    }

}