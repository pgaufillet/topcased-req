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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.DeleteModelCommand;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.requirement.core.commands.RemoveRequirementCommand;

/**
 * This Class handle specific behaviour for requirements when a DeleteModelCommand is executed.
 * 
 * @author <a href="tristan.faure@atosorigin.com">Tristan FAURE</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class DeleteCommandResolver extends AdditionalCommand<DeleteModelCommand>
{

    private Map<DeleteModelCommand, CompoundCommand> commands;

    public DeleteCommandResolver()
    {
        this(DeleteModelCommand.class);
        commands = new HashMap<DeleteModelCommand, CompoundCommand>();
    }

    public DeleteCommandResolver(Class< ? super DeleteModelCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_execute(java.util.List)
     */
    @Override
    protected void pre_execute(List<DeleteModelCommand> deleteModelCommands)
    {

        for (DeleteModelCommand deleteModelCommand : deleteModelCommands)
        {
            Collection<EObject> eObjects = deleteModelCommand.getObjectsDeleting();
            CompoundCommand command = new CompoundCommand();
            for (EObject e : eObjects)
            {
                RemoveRequirementCommand com = new RemoveRequirementCommand(AdapterFactoryEditingDomain.getEditingDomainFor(e), e);
                command.add(new EMFtoGEFCommandWrapper(com));
            }
            command.execute();
            commands.put(deleteModelCommand, command);
        }

    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<DeleteModelCommand> deleteModelCommands)
    {
        for (DeleteModelCommand deleteModelCommand : deleteModelCommands)
        {
            CompoundCommand compound = commands.get(deleteModelCommand);
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
    protected void post_undo(List<DeleteModelCommand> deleteModelCommands)
    {
        for (ListIterator<DeleteModelCommand> i = deleteModelCommands.listIterator(deleteModelCommands.size()); i.hasPrevious();)
        {
            DeleteModelCommand deleteModelCommand = i.previous();
            CompoundCommand compound = commands.get(deleteModelCommand);
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
        List<Object> result = new LinkedList<Object>();

        // deals with DeleteModelContentCommand (specific behaviour)
        if (command instanceof CompoundCommand)
        {
            CompoundCommand compound = (CompoundCommand) command;
            List< ? > commands = compound.getCommands();
            for (Object o : commands)
            {
                // if we got a compound command with one or more deleteModelCommand, we add the command to the result
                if (o instanceof DeleteModelCommand)
                {
                    result.add((DeleteModelCommand) o);
                }
                else
                {
                    // same algorithm than CommandStack.getCommands
                    List<Object> tmp = CommandStack.getCommands((Command) o, clazz);
                    if (!(tmp.isEmpty()))
                    {
                        result.addAll(tmp);
                    }
                }
            }
        }
        else
        {
            // same algorithm than CommandStack.getCommands
            List<Object> tmp = CommandStack.getCommands(command, clazz);
            if (!(tmp.isEmpty()))
            {
                result.addAll(tmp);
            }
        }
        return result;
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