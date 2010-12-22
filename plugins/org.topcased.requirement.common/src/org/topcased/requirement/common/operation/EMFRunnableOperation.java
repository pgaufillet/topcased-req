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
package org.topcased.requirement.common.operation;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.topcased.requirement.common.Activator;

/**
 * An EMF operation which can be run with a progress monitor. This can be used when a transactional context is imposed.
 * 
 * @author vhemery
 */
public abstract class EMFRunnableOperation extends AbstractEMFOperation implements IRunnableWithProgress, Command
{
    /** The progress monitor for executions */
    private IProgressMonitor progressMonitor;

    /**
     * Construct a new operation
     * 
     * @param domain transactional editing domain
     * @param label operation label
     */
    public EMFRunnableOperation(TransactionalEditingDomain domain, String label)
    {
        super(domain, label);
    }

    /**
     * Construct a new operation
     * 
     * @param domain transactional editing domain
     * @param label operation label
     * @param options options map for the transaction or null
     */
    public EMFRunnableOperation(TransactionalEditingDomain domain, String label, Map< ? , ? > options)
    {
        super(domain, label, options);
    }

    /**
     * Execute the transaction.
     * 
     * @param monitor progress monitor
     */
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
    {
        progressMonitor = monitor;
        getEditingDomain().getCommandStack().execute(this);
    }

    /**
     * Performs the command
     */
    public void execute()
    {
        try
        {
            execute(progressMonitor, null);
        }
        catch (ExecutionException e)
        {
            Activator.log(e);
        }
    }

    /**
     * Undo the command
     */
    public void undo()
    {
        try
        {
            undo(progressMonitor, null);
        }
        catch (ExecutionException e)
        {
            Activator.log(e);
        }
    }

    /**
     * Redo the command
     */
    public void redo()
    {
        try
        {
            redo(progressMonitor, null);
        }
        catch (ExecutionException e)
        {
            Activator.log(e);
        }
    }

    /**
     * Get the result of the command
     * 
     * @return null
     */
    public Collection< ? > getResult()
    {
        return null;
    }

    /**
     * Get objects affected by the command
     * 
     * @return null
     */
    public Collection< ? > getAffectedObjects()
    {
        return null;
    }

    /**
     * Get the command description
     * 
     * @return label
     */
    public String getDescription()
    {
        return getLabel();
    }

    /**
     * This operation is not supported
     * 
     * @throws UnsupportedOperationException
     */
    public Command chain(Command command)
    {
        throw new UnsupportedOperationException();
    }

}
