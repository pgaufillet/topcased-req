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

import org.eclipse.emf.common.command.AbstractCommand;

/**
 * An advanced command stub which enables undo and redo.
 * 
 * @author vhemery
 */
public abstract class CommandStub extends AbstractCommand
{

    /**
     * Performs the command activity required for the effect. This implementation simply invokes {@link #redo()}.
     */
    public void execute()
    {
        redo();
    }

    /**
     * Undo the command
     */
    @Override
    public void undo()
    {
        // Do nothing by default.
    }

    /**
     * @see AbstractCommand#prepare()
     */
    @Override
    protected boolean prepare()
    {
        return true;
    }

}
