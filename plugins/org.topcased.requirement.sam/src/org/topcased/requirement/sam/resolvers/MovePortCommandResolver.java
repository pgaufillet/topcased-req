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

package org.topcased.requirement.sam.resolvers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.commands.Command;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain;
import org.topcased.modeler.sam.commands.MovePortCommand;
import org.topcased.requirement.core.commands.RemoveHierarchicalElementCommand;
import org.topcased.requirement.core.resolvers.AdditionalCommand;

/**
 * This Class handle specific behaviour for requirements when a MovePortCommand is executed.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class MovePortCommandResolver extends AdditionalCommand<MovePortCommand>
{

    private Map<MovePortCommand, EMFtoGEFCommandWrapper> commands;

    public MovePortCommandResolver()
    {
        this(MovePortCommand.class);
        commands = new HashMap<MovePortCommand, EMFtoGEFCommandWrapper>();
    }

    public MovePortCommandResolver(Class< ? super MovePortCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<MovePortCommand> moveCommands)
    {   
        Collection<EObject> selected = new ArrayList<EObject>();
        for (MovePortCommand moveCommand : moveCommands)
        {
                //The flow has already been deleted so we can't retreive his editing domain, that s why we call the modeler.
                EditingDomain ed = (EditingDomain) super.getModeler().getEditingDomain();
                if (ed instanceof TopcasedAdapterFactoryEditingDomain)
                {
                    selected.add(moveCommand.getdeletedFlow());
                    EMFtoGEFCommandWrapper deleteCmd = new EMFtoGEFCommandWrapper(new RemoveHierarchicalElementCommand(ed, selected));
                    deleteCmd.execute();
                    commands.put(moveCommand, deleteCmd);
                }
        }
        
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<MovePortCommand> moveCommands)
    {
        for (MovePortCommand moveCommand : moveCommands)
        {
            EMFtoGEFCommandWrapper compound = commands.get(moveCommand);
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
    protected void post_undo(List<MovePortCommand> moveCommands)
    {
        for (ListIterator<MovePortCommand> i = moveCommands.listIterator(moveCommands.size()); i.hasPrevious();)
        {
            MovePortCommand moveCommand = i.previous();
            EMFtoGEFCommandWrapper compound = commands.get(moveCommand);
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