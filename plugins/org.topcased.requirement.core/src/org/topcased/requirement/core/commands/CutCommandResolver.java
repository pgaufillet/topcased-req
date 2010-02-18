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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CutToClipboardCommand;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.requirement.core.actions.HierarchicalElementTransfer;

/**
 * This Class handle specific behaviour for requirements when a CutToClipboardCommand is executed.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class CutCommandResolver extends AdditionalCommand<CutToClipboardCommand>
{

    private Map<CutToClipboardCommand, CompoundCommand> commands;

    public CutCommandResolver()
    {
        this(CutToClipboardCommand.class);
        commands = new HashMap<CutToClipboardCommand, CompoundCommand>();
    }

    public CutCommandResolver(Class< ? super CutToClipboardCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#post_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<CutToClipboardCommand> cutCommands)
    {
        CompoundCommand command = new CompoundCommand();
        HierarchicalElementTransfer.INSTANCE.clear();
        for (CutToClipboardCommand cutCommand : cutCommands)
        {
            Collection< ? > eObjects = cutCommand.getCommand().getResult();

            for (Object e : eObjects)
            {
                if (e instanceof EObject)
                {
                    RemoveHierarchicalElementCommand cmd = new RemoveHierarchicalElementCommand((EObject) e);
                    command.add(new EMFtoGEFCommandWrapper(cmd));
                    HierarchicalElementTransfer.INSTANCE.setResult(cmd.getResult());
                }
            }
            commands.put(cutCommand, command);
        }
        command.execute();
    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<CutToClipboardCommand> cutCommands)
    {
        for (CutToClipboardCommand cutCommand : cutCommands)
        {
            CompoundCommand compound = commands.get(cutCommand);
            if (compound != null)
            {
                compound.redo();
            }
        }
    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#post_undo(java.util.List)
     */
    @Override
    protected void post_undo(List<CutToClipboardCommand> cutCommands)
    {
        for (ListIterator<CutToClipboardCommand> i = cutCommands.listIterator(cutCommands.size()); i.hasPrevious();)
        {
            CutToClipboardCommand cutCommand = i.previous();
            CompoundCommand compound = commands.get(cutCommand);
            if (compound != null)
            {
                compound.undo();
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
        List<Object> result = new LinkedList<Object>();

        // deals with CutToClipboardCommand (specific behaviour)
        if (command instanceof CompoundCommand)
        {
            CompoundCommand compound = (CompoundCommand) command;
            List< ? > commands = compound.getCommands();
            for (Object o : commands)
            {

                if (o instanceof EMFtoGEFCommandWrapper)
                {
                    org.eclipse.emf.common.command.Command cmd = ((EMFtoGEFCommandWrapper) o).getEMFCommand();
                    if (cmd instanceof CutToClipboardCommand)
                    {
                        if (((CutToClipboardCommand) cmd).getClass().equals(clazz))
                        {
                            result.add((CutToClipboardCommand) cmd);
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
        else
        {
            // same algo than CommandStack.getCommands
            List<Object> tmp = CommandStack.getCommands(command, clazz);
            if (!(tmp.isEmpty()))
            {
                result.add(tmp);
            }
        }
        return result;

    }
}