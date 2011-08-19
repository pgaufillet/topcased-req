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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This class defines and run an EMF command.<br>
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
        Object trigger = event.getTrigger();
        if (trigger instanceof Event)
        {
            Widget srcWidget = ((Event) trigger).widget;
            if (srcWidget instanceof Text)
            {
                // command initiated from search widget, which should not trigger such commands
                return null;
            }
        }
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
        if (services != null)
        {
            evt = event;
            compoundCmd = new CompoundCommand();
            editingDomain = services.getEditingDomain(editor);
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
        monitor.beginTask(Messages.getString("RequirementAbstractEMFCommandHandler.0"), 2); //$NON-NLS-1$
        monitor.worked(1);

        Command cmd = editingDomain.createCommand(getCommand(), getParam());
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
     * @return param the parameter of the command
     */
    public abstract CommandParameter getParam();

    /**
     * @return command the command type
     */
    public abstract Class< ? extends Command> getCommand();

}
