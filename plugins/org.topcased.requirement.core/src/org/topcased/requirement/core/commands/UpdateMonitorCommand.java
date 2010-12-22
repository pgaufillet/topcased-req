/*****************************************************************************
 * Copyright (c) 2010 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Vincent Hemery (Atos Origin}) {vincent.hemery@atosorigin.com} - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.core.commands;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;

/**
 * This utility class allow to construct commands to update a progress monitor.
 * 
 * @author vhemery
 */
public class UpdateMonitorCommand
{
    /**
     * Get command to update the progress monitor with new tasks
     * 
     * @param monitor progress monitor to update
     * @param redoTask the new main task to start at beginning of redo (or null)
     * @param redoTaskAmount the amount of work in redoTask (or 0)
     * @param undoTask the new main task to start at beginning of undo (or null)
     * @param undoTaskAmount the amount of work in undoTask (or 0)
     * @return update monitor command
     * @see #getCommand(IProgressMonitor, String, int, int, String, String, int, int, String)
     */
    public static Command getCommand(final IProgressMonitor monitor, final String redoTask, final int redoTaskAmount, final String undoTask, final int undoTaskAmount)
    {
        return getCommand(monitor, redoTask, redoTaskAmount, 0, null, undoTask, undoTaskAmount, 0, null);
    }

    /**
     * Get command to update the progress monitor with work progress and subtasks
     * 
     * @param monitor progress monitor to update
     * @param redoWork the amount of work units completed at redo (or 0)
     * @param redoSubTask the new sub task to start at end of redo (or null)
     * @param undoWork the amount of work units completed at undo (or 0)
     * @param undoSubTask the new sub task to start at end of undo (or null)
     * @return update monitor command
     * @see #getCommand(IProgressMonitor, String, int, int, String, String, int, int, String)
     */
    public static Command getCommand(final IProgressMonitor monitor, final int redoWork, final String redoSubTask, final int undoWork, final String undoSubTask)
    {
        return getCommand(monitor, null, 0, redoWork, redoSubTask, null, 0, undoWork, undoSubTask);
    }

    /**
     * Get command to update the progress monitor with work progress
     * 
     * @param monitor progress monitor to update
     * @param redoWork the amount of work units completed at redo (or 0)
     * @param undoWork the amount of work units completed at undo (or 0)
     * @return update monitor command
     * @see #getCommand(IProgressMonitor, String, int, int, String, String, int, int, String)
     */
    public static Command getCommand(final IProgressMonitor monitor, final int redoWork, final int undoWork)
    {
        return getCommand(monitor, null, 0, redoWork, null, null, 0, undoWork, null);
    }

    /**
     * Get command to update the progress monitor with all information
     * 
     * @param monitor progress monitor to update
     * @param redoTask the new main task to start at beginning of redo (or null)
     * @param redoTaskAmount the amount of work in redoTask (or 0)
     * @param redoWork the amount of work units completed at redo (or 0)
     * @param redoSubTask the new sub task to start at end of redo (or null)
     * @param undoTask the new main task to start at beginning of undo (or null)
     * @param undoTaskAmount the amount of work in undoTask (or 0)
     * @param undoWork the amount of work units completed at undo (or 0)
     * @param undoSubTask the new sub task to start at end of undo (or null)
     * @return update monitor command
     */
    public static Command getCommand(final IProgressMonitor monitor, final String redoTask, final int redoTaskAmount, final int redoWork, final String redoSubTask, final String undoTask,
            final int undoTaskAmount, final int undoWork, final String undoSubTask)
    {
        return new CommandStub()
        {
            public void redo()
            {
                if (redoTask != null)
                {
                    monitor.beginTask(redoTask, redoTaskAmount);
                }
                if (redoWork > 0)
                {
                    monitor.worked(redoWork);
                }
                if (redoSubTask != null)
                {
                    monitor.subTask(redoSubTask);
                }
            }

            @Override
            public void undo()
            {
                if (undoTask != null)
                {
                    monitor.beginTask(undoTask, undoTaskAmount);
                }
                if (undoWork > 0)
                {
                    monitor.worked(undoWork);
                }
                if (undoSubTask != null)
                {
                    monitor.subTask(undoSubTask);
                }
            }
        };
    }
}
