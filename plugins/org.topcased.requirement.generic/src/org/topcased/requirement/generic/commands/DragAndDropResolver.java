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
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.sam.requirement.core.commands.MoveHierarchicalElementCommand;

/**
 * The Class DragAndDropResolver.
 */
public class DragAndDropResolver extends AdditionalCommand<DragAndDropCommand>
{

    private HashMap<DragAndDropCommand, EMFtoGEFCommandWrapper> map = new HashMap<DragAndDropCommand, EMFtoGEFCommandWrapper>();

    public DragAndDropResolver()
    {
        super(DragAndDropCommand.class);
    }

    @Override
    protected void pre_execute(List<DragAndDropCommand> commands)
    {
        for (DragAndDropCommand command : commands)
        {
            Collection< ? > children = command.getCollection();
            Object parent = command.getOwner();
            if (parent instanceof EObject)
            {
                EObject owner = (EObject) parent;
                EMFtoGEFCommandWrapper hier = new EMFtoGEFCommandWrapper(new MoveHierarchicalElementCommand(owner, children));
                hier.execute();
                map.put(command, hier);
            }
        }
    }

    @Override
    protected void pre_redo(List<DragAndDropCommand> commands)
    {
        for (DragAndDropCommand command : commands)
        {
            EMFtoGEFCommandWrapper wrapper = map.get(command);
            if (wrapper != null)
            {
                wrapper.redo();
            }
        }
    }

    @Override
    protected void post_undo(List<DragAndDropCommand> commands)
    {
        for (ListIterator<DragAndDropCommand> i = commands.listIterator(commands.size()); i.hasPrevious();)
        {
            DragAndDropCommand command = i.previous();
            EMFtoGEFCommandWrapper wrapper = map.get(command);
            if (wrapper != null)
            {
                wrapper.undo();
            }
        }
    }

}
