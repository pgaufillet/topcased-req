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
package org.topcased.requirement.bundle.topcased.resolvers;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.common.commands.EMFtoGEFCommandWrapper;
import org.topcased.requirement.common.commands.IDisposableCommandStackEventListener;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This class provide entry point to commandstacklistener events
 * 
 * @author <a href="tristan.faure@atosorigin.com">Tristan FAURE</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public abstract class AdditionalCommand<T> implements IDisposableCommandStackEventListener
{
    private Class< ? super T> theClass = null;

    private IEditorPart editor;

    public AdditionalCommand(Class< ? super T> clazz)
    {
        theClass = clazz;
    }

    @SuppressWarnings("unchecked")
    final public void stackChanged(CommandStackEvent event)
    {
        editor = RequirementUtils.getCurrentEditor();
        if (editor != null)
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
     * Gets the list of specific commands when the parameter command and the parameter clazz are equals This is the same
     * behaviour as CommandStack.getCommands(command, clazz) but specific to the type of <T>
     * 
     * @param command
     * @param clazz
     * @return List<Object>
     */
    protected List<Object> getSpecificCommands(Command command, Class< ? > clazz)
    {
        return getCommands(command, clazz, false);
    }

    /**
     * Return the command of class given in parameter
     * 
     * @param command
     * @param clazz
     * @return
     */
    public static List<Object> getCommands(Command command, Class< ? > clazz, boolean instanceOK)
    {
        List<Object> result = new LinkedList<Object>();
        if (clazz == null)
        {
            return result;
        }
        if (clazz.equals(command.getClass()) || (instanceOK && clazz.isAssignableFrom(command.getClass())))
        {
            result.add(command);
        }
        else if (command instanceof org.topcased.modeler.commands.EMFtoGEFCommandWrapper)
        {
            org.eclipse.emf.common.command.Command emfCmd = ((org.topcased.modeler.commands.EMFtoGEFCommandWrapper) command).getEMFCommand();
            if (emfCmd.getClass().equals(clazz) || (instanceOK && clazz.isAssignableFrom(emfCmd.getClass())))
            {
                result.add(emfCmd);
            }
        }
        else if (command instanceof EMFtoGEFCommandWrapper)
        {
            org.eclipse.emf.common.command.Command emfCmd = ((EMFtoGEFCommandWrapper) command).getEMFCommand();
            if (emfCmd.getClass().equals(clazz) || (instanceOK && clazz.isAssignableFrom(emfCmd.getClass())))
            {
                result.add(emfCmd);
            }
        }
        else if (command instanceof CompoundCommand)
        {
            CompoundCommand compound = (CompoundCommand) command;
            List< ? > commands = compound.getCommands();
            for (Object o : commands)
            {
                if (o instanceof Command)
                {
                    List<Object> tmp = getCommands((Command) o, clazz, instanceOK);
                    if (!(tmp.isEmpty()))
                    {
                        result.addAll(tmp);
                    }
                }
            }
        }
        return result;
    }

    public void dipose()
    {
        // override this method if you want to add behavior
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

    /**
     * Get the editing domain of the editor
     * 
     * @return editing domain or null if not found
     */
    protected EditingDomain getEditorEditingDomain()
    {
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(getEditor());
        if (services != null)
        {
            return services.getEditingDomain(getEditor());
        }
        return null;
    }

    protected IEditorPart getEditor()
    {
        return editor;
    }
}
