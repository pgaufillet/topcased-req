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

package org.topcased.requirement.bundle.topcased.resolvers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.gef.commands.Command;
import org.topcased.requirement.common.commands.EMFtoGEFCommandWrapper;
import org.topcased.requirement.core.commands.MoveHierarchicalElementCommand;

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

    private Map<DragAndDropCommand, CompoundCommand> mapCommand;

    public DragAndDropCommandResolver()
    {
        this(DragAndDropCommand.class);
        mapCommand = new HashMap<DragAndDropCommand, CompoundCommand>();
    }

    public DragAndDropCommandResolver(Class< ? super DragAndDropCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#post_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<DragAndDropCommand> dndCommands)
    {
        CompoundCommand compound = new CompoundCommand();

        for (DragAndDropCommand dndCommand : dndCommands)
        {
            MoveHierarchicalElementCommand cmd = new MoveHierarchicalElementCommand((EObject) dndCommand.getOwner(), dndCommand.getCollection());
            compound.appendIfCanExecute(cmd);
            /*
             * We disable automatic renaming the current requirement. The ident is final.
             */
            // for (Object currSrc : dndCommand.getCollection())
            // {
            // if (currSrc instanceof CurrentRequirement)
            // {
            // // Handle case of current view requirements drag'n'drop
            // org.topcased.requirement.CurrentRequirement requirement = (org.topcased.requirement.CurrentRequirement)
            // currSrc;
            // compound.appendAndExecute(RequirementHelper.INSTANCE.renameRequirement(requirement));
            // }
            // }
            compound.execute();
            mapCommand.put(dndCommand, compound);
        }

    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<DragAndDropCommand> dndCommands)
    {
        for (DragAndDropCommand dndCommand : dndCommands)
        {
            CompoundCommand compound = mapCommand.get(dndCommand);
            if (compound != null)
            {
                compound.redo();
            }
        }
    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#post_undo(java.util.List)
     */
    @Override
    protected void post_undo(List<DragAndDropCommand> dndCommands)
    {
        for (ListIterator<DragAndDropCommand> i = dndCommands.listIterator(dndCommands.size()); i.hasPrevious();)
        {
            DragAndDropCommand dndCommand = i.previous();
            CompoundCommand compound = mapCommand.get(dndCommand);
            if (compound != null)
            {
                compound.undo();
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
        List<Object> result = new ArrayList<Object>();

        // deals with DragAndDropCommand (specific behaviour)
        org.eclipse.emf.common.command.Command emfCommand = null;
        if (command instanceof org.topcased.modeler.commands.EMFtoGEFCommandWrapper)
        {
            emfCommand = ((org.topcased.modeler.commands.EMFtoGEFCommandWrapper) command).getEMFCommand();
        }
        else if (command instanceof EMFtoGEFCommandWrapper)
        {
            emfCommand = ((EMFtoGEFCommandWrapper) command).getEMFCommand();
        }
        if (emfCommand instanceof CompoundCommand)
        {
            List< ? > commands = ((CompoundCommand) emfCommand).getCommandList();
            for (Object o : commands)
            {
                // if we got a compound command with one or more DragAndDropCommand, we add the command to the
                // result
                if (o instanceof DragAndDropCommand)
                {
                    result.add((DragAndDropCommand) o);
                }
            }
        }
        else if (emfCommand instanceof DragAndDropCommand)
        {
            result.add((DragAndDropCommand) emfCommand);
        }
        else
        {
            // same algorithm than CommandStack.getCommands
            List<Object> tmp = getCommands(command, clazz, false);
            if (!(tmp.isEmpty()))
            {
                result.add(tmp);
            }
        }
        return result;
    }

    @Override
    public void dipose()
    {
        super.dipose();
        if (mapCommand != null)
        {
            mapCommand.clear();
        }
    }

}