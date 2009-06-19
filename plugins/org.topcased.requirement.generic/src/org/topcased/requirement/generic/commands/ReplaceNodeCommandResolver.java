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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.ecore.EObject;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.modeler.commands.ReplaceNodeContainerCommand;
import org.topcased.sam.requirement.core.commands.MoveHierarchicalElementCommand;

/**
 * The Class ReplaceNodeCommandResolver.
 */
public class ReplaceNodeCommandResolver extends AdditionalCommand<ReplaceNodeContainerCommand>
{

    private HashMap<ReplaceNodeContainerCommand, EMFtoGEFCommandWrapper> map = new HashMap<ReplaceNodeContainerCommand, EMFtoGEFCommandWrapper>();

    public ReplaceNodeCommandResolver()
    {
        super(ReplaceNodeContainerCommand.class);
    }

    @Override
    protected void pre_execute(List<ReplaceNodeContainerCommand> commands)
    {
        for (ReplaceNodeContainerCommand command : commands)
        {
            EObject child = command.getChild();
            EObject host = command.getHost();
            EMFtoGEFCommandWrapper commandWrapper = new EMFtoGEFCommandWrapper(new MoveHierarchicalElementCommand(host, Collections.singleton(child)));
            commandWrapper.execute();
            map.put(command, commandWrapper);
        }
    }

    @Override
    protected void pre_redo(List<ReplaceNodeContainerCommand> commands)
    {
        for (ReplaceNodeContainerCommand command : commands)
        {
            EMFtoGEFCommandWrapper emfWrapper = map.get(command);
            if (emfWrapper != null)
            {
                emfWrapper.redo();
            }
        }
    }

    @Override
    protected void post_undo(List<ReplaceNodeContainerCommand> commands)
    {
        for (ListIterator<ReplaceNodeContainerCommand> i = commands.listIterator(commands.size()); i.hasPrevious();)
        {
            ReplaceNodeContainerCommand command = i.previous();
            EMFtoGEFCommandWrapper emfWrapper = map.get(command);
            if (emfWrapper != null)
            {
                emfWrapper.undo();
            }
        }
    }

}
