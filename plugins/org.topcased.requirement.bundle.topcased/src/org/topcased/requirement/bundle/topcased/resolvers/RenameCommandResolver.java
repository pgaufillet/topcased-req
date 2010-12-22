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

package org.topcased.requirement.bundle.topcased.resolvers;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.topcased.modeler.commands.ChangeLabelTextCommand;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.requirement.core.commands.RenameRequirementCommand;

/**
 * This Class handle specific behaviour for requirements when a ChangeLabelTextCommand is executed.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class RenameCommandResolver extends AdditionalCommand<ChangeLabelTextCommand>
{

    private Map<ChangeLabelTextCommand, AbstractCommand> commands;

    public RenameCommandResolver()
    {
        this(ChangeLabelTextCommand.class);
        commands = new HashMap<ChangeLabelTextCommand, AbstractCommand>();
    }

    public RenameCommandResolver(Class< ? super ChangeLabelTextCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#post_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<ChangeLabelTextCommand> renameCommands)
    {
        for (ChangeLabelTextCommand renameCommand : renameCommands)
        {
            if (!renameCommand.equals(UnexecutableCommand.INSTANCE))
            {
                AbstractCommand cmd = new RenameRequirementCommand(renameCommand.getEObject(), renameCommand.getOldName(), renameCommand.getName());
                if (cmd.canExecute())
                {
                    cmd.execute();
                    commands.put(renameCommand, cmd);
                }
            }
        }
    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<ChangeLabelTextCommand> renameCommands)
    {
        for (ChangeLabelTextCommand renameCommand : renameCommands)
        {
            AbstractCommand command = commands.get(renameCommand);
            if (command != null)
            {
                command.redo();
            }
        }
    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#post_undo(java.util.List)
     */
    @Override
    protected void post_undo(List<ChangeLabelTextCommand> renameCommands)
    {
        for (ListIterator<ChangeLabelTextCommand> i = renameCommands.listIterator(renameCommands.size()); i.hasPrevious();)
        {
            ChangeLabelTextCommand renameCommand = i.previous();
            AbstractCommand command = commands.get(renameCommand);
            if (command != null)
            {
                command.undo();
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
        // nothing special to handle
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