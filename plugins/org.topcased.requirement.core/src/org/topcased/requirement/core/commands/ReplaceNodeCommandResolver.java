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
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) - Initial API and implementation
 *  Maxime AUDRAIN (CS) - API changes
  *****************************************************************************/

package org.topcased.requirement.core.commands;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.modeler.commands.ReplaceNodeContainerCommand;

/**
 * This Class handle specific behaviour for requirements when a ReplaceNodeContainerCommand is executed.
 * 
 * @author <a href="tristan.faure@atosorigin.com">Tristan FAURE</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class ReplaceNodeCommandResolver extends AdditionalCommand<ReplaceNodeContainerCommand>
{

    private Map<ReplaceNodeContainerCommand, EMFtoGEFCommandWrapper> commands;

    public ReplaceNodeCommandResolver()
    {
        this(ReplaceNodeContainerCommand.class);
        commands = new HashMap<ReplaceNodeContainerCommand, EMFtoGEFCommandWrapper>();
    }

    public ReplaceNodeCommandResolver(Class< ? super ReplaceNodeContainerCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#pre_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<ReplaceNodeContainerCommand> replaceCommands)
    {

        for (ReplaceNodeContainerCommand cmd : replaceCommands)
        {
            EObject child = cmd.getChild();
            EObject host = cmd.getHost();
            EMFtoGEFCommandWrapper commandWrapper = new EMFtoGEFCommandWrapper(new MoveHierarchicalElementCommand(host, Collections.singleton(child)));
            commandWrapper.execute();
            commands.put(cmd, commandWrapper);
        }

    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<ReplaceNodeContainerCommand> replaceCommands)
    {
        for (ReplaceNodeContainerCommand replaceCommand : replaceCommands)
        {
            EMFtoGEFCommandWrapper wrap = commands.get(replaceCommand);
            if (wrap != null)
            {
                wrap.redo();
            }
        }            
    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#post_undo(java.util.List)
     */
    @Override
    protected void post_undo(List<ReplaceNodeContainerCommand> replaceCommands)
    {
        for (ListIterator<ReplaceNodeContainerCommand> i = replaceCommands.listIterator(replaceCommands.size()); i.hasPrevious();)
        {
            ReplaceNodeContainerCommand replaceCommand = i.previous();
            EMFtoGEFCommandWrapper wrap = commands.get(replaceCommand);
            if (wrap != null)
            {
                wrap.undo();
            }
        }
    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#getSpecificCommands(org.eclipse.gef.commands.Command,
     *      java.lang.Class)
     */
    @Override
    protected List<Object> getSpecificCommands(Command command, Class< ? > clazz)
    {
        return CommandStack.getCommands(command, clazz);
    }
}