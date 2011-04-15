/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.dialogs;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.IWorkspaceCommandStack;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.DefaultAttributeValue;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.common.commands.CompoundCommand;
import org.topcased.requirement.common.commands.EMFtoGEFCommandWrapper;
import org.topcased.requirement.common.commands.GEFtoEMFCommandWrapper;
import org.topcased.requirement.core.commands.AddAttributeCommand;
import org.topcased.requirement.core.commands.MoveAttributeCommand;
import org.topcased.requirement.core.commands.RemoveAttributeCommand;
import org.topcased.requirement.core.commands.RenameAttributeCommand;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementHelper;

/**
 * Manager for handling update of the attribute configuration.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 * 
 */
final class UpdateAttributeManager
{

    /** The shared editing domain */
    private EditingDomain editingDomain;

    /** The attribute configuration stored inside the model */
    private AttributeConfiguration localConf;

    /** Compound command containing all commands which will be executed when the dialog will be closed. */
    private CompoundCommand cmd;

    /** The number of commands which have been executed in the command stack (to eventually merge or undo them) */
    private int numberOfCommandsExecutedInStack;

    /**
     * Constructor
     * 
     * @param workspace
     * @param local
     */
    public UpdateAttributeManager(AttributeConfiguration local)
    {
        editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(local);
        localConf = local;
        cmd = new CompoundCommand(Messages.getString("UpdateAttributeManager.0")); //$NON-NLS-1$
        numberOfCommandsExecutedInStack = 0;
    }

    /**
     * Handles <b>Add</b> operations.<br />
     * The selection is done from the workspace configuration and is moved to the local attribute configuration.
     * 
     * @param element The element to add to the current configuration.
     */
    public void handleAdd(final ConfiguratedAttribute element)
    {
        IRunnableWithProgress runnable = new IRunnableWithProgress()
        {
            /**
             * Add the attribute
             */
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
            {
                monitor.setTaskName(Messages.getString("UpdateAttributeManager.4")); //$NON-NLS-1$
                monitor.beginTask(Messages.getString("UpdateAttributeManager.4"), 2); //$NON-NLS-1$
                monitor.worked(1);
                CompoundCommand cmdToComplete = getCommandToComplete();
                Command addAttCmd = AddCommand.create(editingDomain, localConf, RequirementPackage.eINSTANCE.getAttributeConfiguration_ListAttributes(), EcoreUtil.copy(element));
                if (cmdToComplete.addAndExecute(new EMFtoGEFCommandWrapper(addAttCmd)))
                {
                    cmdToComplete.addAndExecute(new EMFtoGEFCommandWrapper(new AddAttributeCommand(editingDomain, element)));
                }
                monitor.worked(1);
                monitor.done();
            }
        };
        runnable = RequirementHelper.INSTANCE.encapsulateEMFRunnable(runnable, Messages.getString("UpdateAttributeManager.4")); //$NON-NLS-1$
        try
        {
            // commands are executed in an independent thread.
            new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, runnable);
            completeCommandIfTransactional(runnable);
        }
        catch (InterruptedException e)
        {
            RequirementCorePlugin.log(Messages.getString("UpdateAttributeManager.5"), IStatus.ERROR, e); //$NON-NLS-1$
        }
        catch (InvocationTargetException e)
        {
            RequirementCorePlugin.log(Messages.getString("UpdateAttributeManager.5"), IStatus.ERROR, e); //$NON-NLS-1$
        }
    }

    /**
     * Handles <b>Remove</b> operations.<br />
     * The selection is done from the local configuration displayed to screen.
     * 
     * @param element The configured element to remove
     */
    public void handleRemove(final ConfiguratedAttribute element)
    {
        IRunnableWithProgress runnable = new IRunnableWithProgress()
        {
            /**
             * Remove the attribute
             */
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
            {
                monitor.setTaskName(Messages.getString("UpdateAttributeManager.6")); //$NON-NLS-1$
                monitor.beginTask(Messages.getString("UpdateAttributeManager.6"), 2); //$NON-NLS-1$
                monitor.worked(1);
                CompoundCommand cmdToComplete = getCommandToComplete();
                Command removeAttCmd = RemoveCommand.create(editingDomain, localConf, RequirementPackage.eINSTANCE.getAttributeConfiguration_ListAttributes(), element);
                if (cmdToComplete.addAndExecute(new EMFtoGEFCommandWrapper(removeAttCmd)))
                {
                    cmdToComplete.addAndExecute(new EMFtoGEFCommandWrapper(new RemoveAttributeCommand(editingDomain, element)));
                }
                monitor.worked(1);
                monitor.done();
            }
        };
        runnable = RequirementHelper.INSTANCE.encapsulateEMFRunnable(runnable, "UpdateAttributeManager.6"); //$NON-NLS-1$
        try
        {
            // commands are executed in an independent thread.
            new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, runnable);
            completeCommandIfTransactional(runnable);
        }
        catch (InterruptedException e)
        {
            RequirementCorePlugin.log(Messages.getString("UpdateAttributeManager.7"), IStatus.ERROR, e); //$NON-NLS-1$
        }
        catch (InvocationTargetException e)
        {
            RequirementCorePlugin.log(Messages.getString("UpdateAttributeManager.7"), IStatus.ERROR, e); //$NON-NLS-1$
        }
    }

    /**
     * Handles <b>Move</b> operations.<br />
     * The selection is done from the local configuration displayed to screen.
     * 
     * @param element The configured element to move
     * @param up Boolean indicating if the chosen direction is <b>up</b> or <b>down</b>
     */
    public void handleMove(final ConfiguratedAttribute element, final boolean up)
    {
        IRunnableWithProgress runnable = new IRunnableWithProgress()
        {
            /**
             * Move the attribute
             */
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
            {
                monitor.setTaskName(Messages.getString("UpdateAttributeManager.8")); //$NON-NLS-1$
                monitor.beginTask(Messages.getString("UpdateAttributeManager.8"), 2); //$NON-NLS-1$
                monitor.worked(1);
                CompoundCommand cmdToComplete = getCommandToComplete();
                int index = localConf.getListAttributes().indexOf(element);
                Command moveAttCmd = UnexecutableCommand.INSTANCE;
                if (up)
                {
                    if (index > 0)
                    {
                        moveAttCmd = MoveCommand.create(editingDomain, localConf, RequirementPackage.eINSTANCE.getAttributeConfiguration_ListAttributes(), element, --index);
                    }
                }
                else
                {
                    if (index < localConf.getListAttributes().size())
                    {
                        moveAttCmd = MoveCommand.create(editingDomain, localConf, RequirementPackage.eINSTANCE.getAttributeConfiguration_ListAttributes(), element, ++index);
                    }
                }
                if (cmdToComplete.addAndExecute(new EMFtoGEFCommandWrapper(moveAttCmd)))
                {
                    cmdToComplete.addAndExecute(new EMFtoGEFCommandWrapper(new MoveAttributeCommand(editingDomain, element, index)));
                }
                monitor.worked(1);
                monitor.done();
            }
        };
        runnable = RequirementHelper.INSTANCE.encapsulateEMFRunnable(runnable, Messages.getString("UpdateAttributeManager.8")); //$NON-NLS-1$
        try
        {
            // commands are executed in an independent thread.
            new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, runnable);
            completeCommandIfTransactional(runnable);
        }
        catch (InterruptedException e)
        {
            RequirementCorePlugin.log(Messages.getString("UpdateAttributeManager.9"), IStatus.ERROR, e); //$NON-NLS-1$
        }
        catch (InvocationTargetException e)
        {
            RequirementCorePlugin.log(Messages.getString("UpdateAttributeManager.9"), IStatus.ERROR, e); //$NON-NLS-1$
        }
    }

    /**
     * Handles <b>Replace</b> operations.<br />
     * 
     * @param leftElement The left configured element
     * @param rightElement The right configured element.
     */
    public void handleReplace(final ConfiguratedAttribute leftElement, final ConfiguratedAttribute rightElement)
    {
        IRunnableWithProgress runnable = new IRunnableWithProgress()
        {
            /**
             * Replace the attribute
             */
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
            {
                monitor.setTaskName(Messages.getString("UpdateAttributeManager.10")); //$NON-NLS-1$
                monitor.beginTask(Messages.getString("UpdateAttributeManager.10"), 4); //$NON-NLS-1$
                monitor.worked(1);
                CompoundCommand cmdToComplete = getCommandToComplete();

                // first, older attributes are removed from the target
                Command removeAttCmd = RemoveCommand.create(editingDomain, rightElement, RequirementPackage.eINSTANCE.getConfiguratedAttribute_ListValue(), rightElement.getListValue());
                cmdToComplete.addAndExecute(new EMFtoGEFCommandWrapper(removeAttCmd));

                // then, the new ones are copied and added to the target.
                monitor.worked(1);
                Collection<AttributeValue> attToCopy = EcoreUtil.copyAll(leftElement.getListValue());
                Command addAttCmd = AddCommand.create(editingDomain, rightElement, RequirementPackage.eINSTANCE.getConfiguratedAttribute_ListValue(), attToCopy);
                cmdToComplete.addAndExecute(new EMFtoGEFCommandWrapper(addAttCmd));

                // after we can handle the default attribute value.
                monitor.worked(1);
                EObject defaultAtt = null;
                DefaultAttributeValue defaultvalue = leftElement.getDefaultValue();
                if (defaultvalue != null)
                {
                    int defaultIndex = leftElement.getListValue().indexOf(defaultvalue.getValue());

                    defaultAtt = EcoreUtil.copy(defaultvalue);
                    if (defaultIndex >= 0)
                    {
                        ((DefaultAttributeValue) defaultAtt).setValue(attToCopy.toArray(new AttributeValue[0])[defaultIndex]);
                    }
                }

                // the default attribute value is set.
                monitor.worked(1);
                Command setDefaultCmd = SetCommand.create(editingDomain, rightElement, RequirementPackage.eINSTANCE.getConfiguratedAttribute_DefaultValue(), defaultAtt);
                cmdToComplete.addAndExecute(new EMFtoGEFCommandWrapper(setDefaultCmd));
                monitor.done();
            }
        };
        runnable = RequirementHelper.INSTANCE.encapsulateEMFRunnable(runnable, Messages.getString("UpdateAttributeManager.10")); //$NON-NLS-1$
        try
        {
            // commands are executed in an independent thread.
            new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, runnable);
            completeCommandIfTransactional(runnable);
        }
        catch (InterruptedException e)
        {
            RequirementCorePlugin.log(Messages.getString("UpdateAttributeManager.11"), IStatus.ERROR, e); //$NON-NLS-1$
        }
        catch (InvocationTargetException e)
        {
            RequirementCorePlugin.log(Messages.getString("UpdateAttributeManager.11"), IStatus.ERROR, e); //$NON-NLS-1$
        }
    }

    /**
     * Handles <b>Rename</b> operations.<br />
     * 
     * @param leftElement The left configured element
     * @param rightElement The right configured element.
     */
    public void handleRename(final ConfiguratedAttribute leftElement, final ConfiguratedAttribute rightElement)
    {
        IRunnableWithProgress runnable = new IRunnableWithProgress()
        {
            /**
             * Rename the attribute
             */
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
            {
                monitor.setTaskName(Messages.getString("UpdateAttributeManager.12")); //$NON-NLS-1$
                monitor.beginTask(Messages.getString("UpdateAttributeManager.12"), 2); //$NON-NLS-1$
                monitor.worked(1);
                CompoundCommand cmdToComplete = getCommandToComplete();

                // keep trace of the former name
                String oldName = rightElement.getName();
                // the new name is set
                Command renameAttCmd = SetCommand.create(editingDomain, rightElement, RequirementPackage.eINSTANCE.getConfiguratedAttribute_Name(), leftElement.getName());
                if (cmdToComplete.addAndExecute(new EMFtoGEFCommandWrapper(renameAttCmd)))
                {
                    cmdToComplete.addAndExecute(new EMFtoGEFCommandWrapper(new RenameAttributeCommand(editingDomain, oldName, rightElement.getName())));
                }

                monitor.worked(1);
                monitor.done();
            }
        };
        runnable = RequirementHelper.INSTANCE.encapsulateEMFRunnable(runnable, Messages.getString("UpdateAttributeManager.12")); //$NON-NLS-1$
        try
        {
            // commands are executed in an independent thread.
            new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, runnable);
            completeCommandIfTransactional(runnable);
        }
        catch (InterruptedException e)
        {
            RequirementCorePlugin.log(Messages.getString("UpdateAttributeManager.13"), IStatus.ERROR, e); //$NON-NLS-1$
        }
        catch (InvocationTargetException e)
        {
            RequirementCorePlugin.log(Messages.getString("UpdateAttributeManager.13"), IStatus.ERROR, e); //$NON-NLS-1$
        }
    }

    /**
     * Complete the global command in case we use a transactional context and it has not already been completed.
     * 
     * @param runnable the executed runnable
     */
    private void completeCommandIfTransactional(IRunnableWithProgress runnable)
    {
        if (runnable instanceof Command && editingDomain instanceof TransactionalEditingDomain)
        {
            cmd.addExecuted(new EMFtoGEFCommandWrapper((Command) runnable));
            numberOfCommandsExecutedInStack++;
        }
    }

    /**
     * Get the command which shall be completed in the runnable executions
     * 
     * @return command to complete in runnables
     */
    protected CompoundCommand getCommandToComplete()
    {
        // either complete the global command or use another one if we use a transactional context.
        if (editingDomain instanceof TransactionalEditingDomain)
        {
            return new CompoundCommand(Messages.getString("UpdateAttributeManager.0")); //$NON-NLS-1$
        }
        return cmd;
    }

    /**
     * Executes the commands contained in the command stack in a different thread.
     */
    public void execute()
    {
        if (cmd.getCommands().size() > 0)
        {
            try
            {
                if (editingDomain instanceof TransactionalEditingDomain)
                {
                    // TODO ensure the command stack is not polluted by merging all transactions in the command stack
                    // CommandStack stack = editingDomain.getCommandStack();
                }
                else
                {
                    // commands are executed in an independent thread.
                    new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, new WorkspaceModifyOperation()
                    {
                        @Override
                        protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException
                        {
                            monitor.setTaskName(Messages.getString("UpdateAttributeManager.1")); //$NON-NLS-1$
                            monitor.beginTask(Messages.getString("UpdateAttributeManager.2"), 2); //$NON-NLS-1$
                            monitor.worked(1);
                            editingDomain.getCommandStack().execute(getCommand());
                            numberOfCommandsExecutedInStack++;
                            monitor.worked(1);
                            monitor.done();
                        }
                    });
                }
            }
            catch (InvocationTargetException e)
            {
                RequirementCorePlugin.log(e);

            }
            catch (InterruptedException e)
            {
                RequirementCorePlugin.log(e);
            }
        }
    }

    /**
     * Undoes command that have been already executed.
     */
    public void undoExecuted()
    {
        if (cmd.getCommands().size() > 0)
        {
            try
            {
                if (editingDomain instanceof TransactionalEditingDomain)
                {
                    for (int i = 0; i < numberOfCommandsExecutedInStack; i++)
                    {
                        editingDomain.getCommandStack().undo();
                    }
                    // we should remove these commands from the command stack
                    CommandStack stack = editingDomain.getCommandStack();
                    if (stack instanceof IWorkspaceCommandStack)
                    {
                        IOperationHistory history = ((IWorkspaceCommandStack) stack).getOperationHistory();
                        IUndoableOperation[] operations = history.getRedoHistory(((IWorkspaceCommandStack) stack).getDefaultUndoContext());
                        for (int i = 0; i < numberOfCommandsExecutedInStack && i < operations.length; i++)
                        {
                            history.replaceOperation(operations[i], new IUndoableOperation[] {});
                        }
                    }
                    numberOfCommandsExecutedInStack = 0;
                }
                else
                {
                    // commands are executed in an independent thread.
                    new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, new WorkspaceModifyOperation()
                    {
                        @Override
                        protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException
                        {
                            monitor.setTaskName(Messages.getString("UpdateAttributeManager.3")); //$NON-NLS-1$
                            monitor.beginTask(Messages.getString("UpdateAttributeManager.3"), 2); //$NON-NLS-1$
                            monitor.worked(1);
                            cmd.undoExecuted();
                            monitor.worked(1);
                            monitor.done();
                        }
                    });
                }
            }
            catch (InvocationTargetException e)
            {
                RequirementCorePlugin.log(e);

            }
            catch (InterruptedException e)
            {
                RequirementCorePlugin.log(e);
            }
        }
    }

    /**
     * Gets the command.
     * 
     * @return The compound command containing basic executed commands.
     */
    public Command getCommand()
    {
        return new GEFtoEMFCommandWrapper(cmd);
    }

}
