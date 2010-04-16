/*****************************************************************************
 * Copyright (c) 2009,2010 ATOS ORIGIN INTEGRATION.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) - Initial API and implementation
 *  Maxime AUDRAIN (CS) - API changes
 *
  *****************************************************************************/
package org.topcased.requirement.core.resolvers;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;

/**
 * This class provide entry point to commandstacklistener events
 * 
 * @author <a href="tristan.faure@atosorigin.com">Tristan FAURE</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public abstract class AdditionalCommand<T> implements CommandStackEventListener
{
    private Class< ? super T> theClass = null;
    
    private Modeler modeler;

    public AdditionalCommand(Class< ? super T> clazz)
    {
        theClass = clazz;
    }

    
    @SuppressWarnings("unchecked")
    final public void stackChanged(CommandStackEvent event)
    {
        modeler = Utils.getCurrentModeler();
        if (modeler != null)
        {
            Object o = getSpecificCommands(event.getCommand(), theClass);
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

    /**
     * get the list of specific commands when the parameter command and the parameter clazz are equals
     * This is the same behaviour as CommandStack.getCommands(command, clazz) but specific to the type of <T>
     * 
     * @param command
     * @param clazz
     * @return List<Object>
     */
    protected abstract List<Object> getSpecificCommands(Command command, Class< ? > clazz);


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
    
    protected Modeler getModeler()
    {
        return modeler;
    }
}
