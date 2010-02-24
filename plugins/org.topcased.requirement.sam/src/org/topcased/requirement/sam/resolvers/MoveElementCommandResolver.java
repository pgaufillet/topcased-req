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

package org.topcased.requirement.sam.resolvers;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.modeler.sam.commands.MoveElementCommand;
import org.topcased.requirement.core.commands.MoveHierarchicalElementCommand;
import org.topcased.requirement.core.resolvers.AdditionalCommand;

/**
 * This Class handle specific behaviour for requirements when a MoveElementCommand is executed.
 *
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a> * 
 */
public class MoveElementCommandResolver extends AdditionalCommand<MoveElementCommand>
{

    private Map<MoveElementCommand, EMFtoGEFCommandWrapper> commands;

    public MoveElementCommandResolver()
    {
        this(MoveElementCommand.class);
        commands = new HashMap<MoveElementCommand, EMFtoGEFCommandWrapper>();
    }

    public MoveElementCommandResolver(Class< ? super MoveElementCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<MoveElementCommand> moveCommands)
    {

        for (MoveElementCommand cmd : moveCommands)
        {            
            EMFtoGEFCommandWrapper commandWrapper = new EMFtoGEFCommandWrapper(new MoveHierarchicalElementCommand(cmd.getNewContainer(), cmd.getDroppedChildren()));
            commandWrapper.execute();
            commands.put(cmd, commandWrapper);
        }

    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<MoveElementCommand> moveCommands)
    {
        for (MoveElementCommand moveCommand : moveCommands)
        {
            EMFtoGEFCommandWrapper wrap = commands.get(moveCommand);
            if (wrap != null)
            {
                wrap.redo();
            }
        }            
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#post_undo(java.util.List)
     */
    @Override
    protected void post_undo(List<MoveElementCommand> moveCommands)
    {
        for (ListIterator<MoveElementCommand> i = moveCommands.listIterator(moveCommands.size()); i.hasPrevious();)
        {
            MoveElementCommand moveCommand = i.previous();
            EMFtoGEFCommandWrapper wrap = commands.get(moveCommand);
            if (wrap != null)
            {
                wrap.undo();
            }
        }
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#getSpecificCommands(org.eclipse.gef.commands.Command,
     *      java.lang.Class)
     */
    @Override
    protected List<Object> getSpecificCommands(Command command, Class< ? > clazz)
    {
        return CommandStack.getCommands(command, clazz);
    }
}