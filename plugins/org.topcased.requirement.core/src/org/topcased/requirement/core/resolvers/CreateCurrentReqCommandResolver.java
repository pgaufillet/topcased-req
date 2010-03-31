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

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.gef.commands.Command;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.core.commands.CreateCurrentReqCommand;
import org.topcased.requirement.core.utils.RequirementHelper;

/**
 * This Class handle specific behaviour for requirements when a CreateCurrentReqCommand is executed.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class CreateCurrentReqCommandResolver extends AdditionalCommand<CreateCurrentReqCommand>
{

    private Map<CreateCurrentReqCommand, CompoundCommand> mapCommand;

    public CreateCurrentReqCommandResolver()
    {
        this(CreateCurrentReqCommand.class);
        mapCommand = new HashMap<CreateCurrentReqCommand, CompoundCommand>();
    }

    public CreateCurrentReqCommandResolver(Class< ? super CreateCurrentReqCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#post_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<CreateCurrentReqCommand> dndCommands)
    {
        CompoundCommand compound = new CompoundCommand();

        for (CreateCurrentReqCommand createCommand : dndCommands)
        {
            if (!(createCommand.getGlobalCmd() instanceof UnexecutableCommand))
            {
                for (Object req : createCommand.getRequirements())
                {
                    if (req instanceof CurrentRequirement)
                    {
                        // Handle case of current view requirements drag'n'drop
                        org.topcased.requirement.CurrentRequirement requirement = (org.topcased.requirement.CurrentRequirement) req;
                        compound.appendIfCanExecute(RequirementHelper.INSTANCE.renameRequirement(requirement));
                    }
                }
                compound.execute();
                mapCommand.put(createCommand, compound);
            }
        }

    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<CreateCurrentReqCommand> dndCommands)
    {
        for (CreateCurrentReqCommand dndCommand : dndCommands)
        {
            CompoundCommand compound = mapCommand.get(dndCommand);
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
    protected void post_undo(List<CreateCurrentReqCommand> dndCommands)
    {
        for (ListIterator<CreateCurrentReqCommand> i = dndCommands.listIterator(dndCommands.size()); i.hasPrevious();)
        {
            CreateCurrentReqCommand dndCommand = i.previous();
            CompoundCommand compound = mapCommand.get(dndCommand);
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