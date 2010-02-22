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

package org.topcased.requirement.core.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.modeler.commands.ReplaceNodeContainerCommand;

/**
 * This Class handle specific behaviour for requirements when a ReplaceNodeContainerCommand is executed.
 * 
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
        CompoundCommand command = new CompoundCommand();

        if (replaceCommands.get(0) instanceof List<?>)
        {
            List<?> replaceList = (List< ? >) replaceCommands.get(0);
            for (Object replaceCommand : replaceList)
            {
                if (replaceCommand instanceof ReplaceNodeContainerCommand)
                {
                    ArrayList<EObject> eobjs = new ArrayList<EObject>();
                    ReplaceNodeContainerCommand replaceNode = ((ReplaceNodeContainerCommand) replaceCommand);
                    eobjs.add(replaceNode.getChild());
                    EMFtoGEFCommandWrapper cmd = new EMFtoGEFCommandWrapper(new MoveHierarchicalElementCommand(replaceNode.getHost(), eobjs)); 
                    cmd.execute();
                    commands.put(replaceNode, cmd);
                }
    
            }
        }
        command.execute();
    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#pre_redo(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void pre_redo(List<ReplaceNodeContainerCommand> replaceCommands)
    {
        if (replaceCommands.get(0) instanceof List<?>)
        {
            List<ReplaceNodeContainerCommand> replaceList = (List<ReplaceNodeContainerCommand>) replaceCommands.get(0);    

            for (ReplaceNodeContainerCommand replaceCommand : replaceList)
            {
                EMFtoGEFCommandWrapper compound = commands.get(replaceCommand);
                if (compound != null)
                {
                    compound.redo();
                }
            }
            
        }
    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#post_undo(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void post_undo(List<ReplaceNodeContainerCommand> replaceCommands)
    {
        if (replaceCommands.get(0) instanceof List<?>)
        {
            List<ReplaceNodeContainerCommand> replaceList = (List<ReplaceNodeContainerCommand>) replaceCommands.get(0);    

            for (ListIterator<ReplaceNodeContainerCommand> i = replaceList.listIterator(replaceList.size()); i.hasPrevious();)
            {
                ReplaceNodeContainerCommand replaceCommand = i.previous();
                EMFtoGEFCommandWrapper compound = commands.get(replaceCommand);
                if (compound != null)
                {
                    compound.undo();
                }
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
        List<Object> result = new ArrayList<Object>();

        // deals with ReplaceNodeContainerCommand (specific behaviour)
        if (command instanceof CompoundCommand)
        {
            CompoundCommand compound = (CompoundCommand) command;
            List< ? > commands = compound.getCommands();
            for (Object o : commands)
            {
                if (o instanceof ReplaceNodeContainerCommand)
                {
                    if (((ReplaceNodeContainerCommand) o).getClass().equals(clazz))
                    {
                        result.add((ReplaceNodeContainerCommand) o);
                    }
                }
                else
                {
                    // same algo than CommandStack.getCommands
                    List<Object> tmp = CommandStack.getCommands((Command) o, clazz);
                    if (!(tmp.isEmpty()))
                    {
                        result.add(tmp);
                    }
                }
            }
        }
        return result;

    }
}