/*****************************************************************************
 * Copyright (c) 2009 ATOS ORIGIN INTEGRATION.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) tristan.faure@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.generic.commands;

import java.util.List;

import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.requirement.generic.Activator;

/**
 * The Class AdditionalCommand.
 */
public abstract class AdditionalCommand<T> implements CommandStackEventListener
{
    private Class< ? super T> theClass = null;

    public AdditionalCommand(Class< ? super T> clazz)
    {
        theClass = clazz;
    }

    final public void stackChanged(CommandStackEvent event)
    {
        if (!Activator.isCurrentEditorSamEditor() && Activator.currentEditorHasRequirements())
        {
            Object o = CommandStack.getCommands(event.getCommand(), theClass);
            if (o instanceof List< ? > && !((List< ? >) o).isEmpty())
            {
                List<T> commands = (List<T>) o;
                switch (event.getDetail())
                {
                    case CommandStack.PRE_UNDO:
                        pre_undo(commands);
                        break;
                    case CommandStack.PRE_REDO:
                        pre_redo(commands);
                        break;
                    case CommandStack.PRE_EXECUTE:
                        pre_execute(commands);
                        break;
                    case CommandStack.POST_UNDO:
                        post_undo(commands);
                        break;
                    case CommandStack.POST_REDO:
                        post_redo(commands);
                        break;
                    case CommandStack.POST_EXECUTE:
                        post_execute(commands);
                        break;
                    default:
                }
            }
        }
    }

    protected void post_undo(List<T> command)
    {
        // override this method if you want to add behavior
    }

    protected void post_redo(List<T> command)
    {
        // override this method if you want to add behavior
    }

    protected void post_execute(List<T> command)
    {
        // override this method if you want to add behavior
    }

    protected void pre_undo(List<T> command)
    {
        // override this method if you want to add behavior
    }

    protected void pre_redo(List<T> command)
    {
        // override this method if you want to add behavior
    }

    protected void pre_execute(List<T> command)
    {
        // override this method if you want to add behavior
    }
}
