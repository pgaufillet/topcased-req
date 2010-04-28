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
import org.topcased.modeler.sam.systemdiagram.commands.internal.ChangeGraphEdgeCommand;
import org.topcased.requirement.core.resolvers.AdditionalCommand;
import org.topcased.requirement.sam.commands.ChangeHierarchicalElementCommand;

/**
 * This Class handle specific behaviour for requirements when a ChangeGraphEdgeCommand is executed.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class ChangeGraphEdgeCommandResolver extends AdditionalCommand<ChangeGraphEdgeCommand>
{

    private Map<ChangeGraphEdgeCommand, EMFtoGEFCommandWrapper> commands;

    public ChangeGraphEdgeCommandResolver()
    {
        this(ChangeGraphEdgeCommand.class);
        commands = new HashMap<ChangeGraphEdgeCommand, EMFtoGEFCommandWrapper>();
    }

    public ChangeGraphEdgeCommandResolver(Class< ? super ChangeGraphEdgeCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<ChangeGraphEdgeCommand> changeCommands)
    {   
        for (ChangeGraphEdgeCommand changeCommand : changeCommands)
        {
                // Change Requirement model
                EMFtoGEFCommandWrapper command =new EMFtoGEFCommandWrapper( new ChangeHierarchicalElementCommand(changeCommand.getTransformation()));
                command.execute();
                commands.put(changeCommand, command);
        }
        
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<ChangeGraphEdgeCommand> changeCommands)
    {
        for (ChangeGraphEdgeCommand changeCommand : changeCommands)
        {
            EMFtoGEFCommandWrapper compound = commands.get(changeCommand);
            if (compound != null)
            {
                compound.redo();
            }
        }
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#post_undo(java.util.List)
     */
    @Override
    protected void post_undo(List<ChangeGraphEdgeCommand> changeCommands)
    {
        for (ListIterator<ChangeGraphEdgeCommand> i = changeCommands.listIterator(changeCommands.size()); i.hasPrevious();)
        {
            ChangeGraphEdgeCommand changeCommand = i.previous();
            EMFtoGEFCommandWrapper compound = commands.get(changeCommand);
            if (compound != null)
            {
                compound.undo();
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