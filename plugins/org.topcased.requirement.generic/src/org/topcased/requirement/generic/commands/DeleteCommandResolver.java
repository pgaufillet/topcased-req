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
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) tristan.faure@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.generic.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.gef.commands.CompoundCommand;
import org.topcased.modeler.commands.DeleteModelCommand;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.sam.requirement.core.commands.RemoveRequirementCommand;

/**
 * The Class DeleteCommandResolver.
 */
public class DeleteCommandResolver extends AdditionalCommand<DeleteModelCommand>
{

    private HashMap<DeleteModelCommand, CompoundCommand> commands = new HashMap<DeleteModelCommand, CompoundCommand>();

    public DeleteCommandResolver()
    {
        this(DeleteModelCommand.class);
    }

    public DeleteCommandResolver(Class< ? super DeleteModelCommand> clazz)
    {
        super(clazz);
    }

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
}