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

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.topcased.modeler.commands.ChangeLabelTextCommand;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;

/**
 * This Class handle specific behaviour for requirements when a ChangeLabelTextCommand is executed.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class RenameCommandResolver extends AdditionalCommand<ChangeLabelTextCommand>
{

    private Map<ChangeLabelTextCommand, CompoundCommand> commands;

    public RenameCommandResolver()
    {
        this(ChangeLabelTextCommand.class);
        commands = new HashMap<ChangeLabelTextCommand, CompoundCommand>();
    }

    public RenameCommandResolver(Class< ? super ChangeLabelTextCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#post_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<ChangeLabelTextCommand> renameCommands)
    {
        CompoundCommand command = new CompoundCommand();
        for (ChangeLabelTextCommand renameCommand : renameCommands)
        {
            if (!renameCommand.equals(UnexecutableCommand.INSTANCE))
            {
                command.add(new EMFtoGEFCommandWrapper(new RenameRequirementCommand(renameCommand.getEObject(), renameCommand.getOldName(), renameCommand.getName())));
                commands.put(renameCommand, command);
            }

        }
        command.execute();
    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<ChangeLabelTextCommand> renameCommands)
    {
        for (ChangeLabelTextCommand renameCommand : renameCommands)
        {
            CompoundCommand compound = commands.get(renameCommand);
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
    protected void post_undo(List<ChangeLabelTextCommand> renameCommands)
    {
        for (ListIterator<ChangeLabelTextCommand> i = renameCommands.listIterator(renameCommands.size()); i.hasPrevious();)
        {
            ChangeLabelTextCommand renameCommand = i.previous();
            CompoundCommand compound = commands.get(renameCommand);
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
        //nothing special to handle
        return CommandStack.getCommands(command, clazz);
    }
}