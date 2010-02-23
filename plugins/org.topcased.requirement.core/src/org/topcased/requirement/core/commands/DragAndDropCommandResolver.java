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

package org.topcased.requirement.core.commands;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.gef.commands.Command;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;

/**
 *
 * This Class handle specific behaviour for requirements when a DragAndDropCommand is executed.
 * 
 * @author <a href="tristan.faure@atosorigin.com">Tristan FAURE</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class DragAndDropCommandResolver extends AdditionalCommand<DragAndDropCommand>
{

    private Map<DragAndDropCommand, EMFtoGEFCommandWrapper> command;

    public DragAndDropCommandResolver()
    {
        this(DragAndDropCommand.class);
        command = new HashMap<DragAndDropCommand, EMFtoGEFCommandWrapper>();
    }

    public DragAndDropCommandResolver(Class< ? super DragAndDropCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#post_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<DragAndDropCommand> dndCommands)
    {        
        for (DragAndDropCommand dndCommand : dndCommands)
        {           
            EMFtoGEFCommandWrapper cmd = new EMFtoGEFCommandWrapper(new MoveHierarchicalElementCommand((EObject) dndCommand.getOwner(), dndCommand.getCollection()));
            cmd.execute();
            command.put(dndCommand, cmd);
        }

    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<DragAndDropCommand> dndCommands)
    {
        for (DragAndDropCommand dndCommand : dndCommands)
        {
            EMFtoGEFCommandWrapper compound = command.get(dndCommand);
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
    protected void post_undo(List<DragAndDropCommand> dndCommands)
    {
        for (ListIterator<DragAndDropCommand> i = dndCommands.listIterator(dndCommands.size()); i.hasPrevious();)
        {
            DragAndDropCommand dndCommand = i.previous();
            EMFtoGEFCommandWrapper compound = command.get(dndCommand);
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
        return CommandStack.getCommands(command, clazz);
    }
}