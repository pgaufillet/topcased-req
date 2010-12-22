/*****************************************************************************
 * Copyright (c) 2010 ATOS ORIGIN INTEGRATION.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Mathieu Velten (ATOS ORIGIN INTEGRATION) mathieu.velten@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/

package org.topcased.requirement.bundle.topcased.resolvers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.ReplaceNodeContainerCommand;
import org.topcased.requirement.common.commands.EMFtoGEFCommandWrapper;
import org.topcased.requirement.core.commands.MoveHierarchicalElementCommand;

/**
 * This Class handle specific behaviour for requirements when a ReplaceNodeContainerCommand is executed.
 * 
 */
public class ReplaceNodeContainerCommandResolver extends AdditionalCommand<ReplaceNodeContainerCommand>
{

    private Map<ReplaceNodeContainerCommand, Command> commands;

    public ReplaceNodeContainerCommandResolver()
    {
        this(ReplaceNodeContainerCommand.class);
        commands = new HashMap<ReplaceNodeContainerCommand, Command>();
    }

    public ReplaceNodeContainerCommandResolver(Class< ? super ReplaceNodeContainerCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#pre_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<ReplaceNodeContainerCommand> moveCommands)
    {

        for (ReplaceNodeContainerCommand cmd : moveCommands)
        {
            ArrayList<EObject> children = new ArrayList<EObject>();
            children.add(cmd.getChild());
            EMFtoGEFCommandWrapper commandWrapper = new EMFtoGEFCommandWrapper(new MoveHierarchicalElementCommand(cmd.getHost(), children));
            if (commandWrapper.canExecute())
            {
                commandWrapper.execute();
                commands.put(cmd, commandWrapper);
            }
        }

    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<ReplaceNodeContainerCommand> moveCommands)
    {
        for (ReplaceNodeContainerCommand moveCommand : moveCommands)
        {
            Command wrap = commands.get(moveCommand);
            if (wrap != null)
            {
                wrap.redo();
            }
        }
    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#post_undo(java.util.List)
     */
    @Override
    protected void post_undo(List<ReplaceNodeContainerCommand> moveCommands)
    {
        for (ListIterator<ReplaceNodeContainerCommand> i = moveCommands.listIterator(moveCommands.size()); i.hasPrevious();)
        {
            ReplaceNodeContainerCommand moveCommand = i.previous();
            Command wrap = commands.get(moveCommand);
            if (wrap != null)
            {
                wrap.undo();
            }
        }
    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#getSpecificCommands(org.eclipse.gef.commands.Command,
     *      java.lang.Class)
     */
    @Override
    protected List<Object> getSpecificCommands(Command command, Class< ? > clazz)
    {
        return CommandStack.getCommands(command, clazz);
    }

    @Override
    public void dipose()
    {
        super.dipose();
        if (commands != null)
        {
            commands.clear();
        }
    }

}