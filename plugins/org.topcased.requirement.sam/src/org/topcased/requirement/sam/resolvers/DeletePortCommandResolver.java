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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain;
import org.topcased.modeler.sam.commands.DeletePortCommand;
import org.topcased.requirement.core.commands.RemoveHierarchicalElementCommand;
import org.topcased.requirement.core.resolvers.AdditionalCommand;
import org.topcased.sam.Flow;

/**
 * This Class handle specific behaviour for requirements when a DeletePortCommand is executed.
 *
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a> * 
 */
public class DeletePortCommandResolver extends AdditionalCommand<DeletePortCommand>
{

    private Map<DeletePortCommand, EMFtoGEFCommandWrapper> commands;

    public DeletePortCommandResolver()
    {
        this(DeletePortCommand.class);
        commands = new HashMap<DeletePortCommand, EMFtoGEFCommandWrapper>();
    }

    public DeletePortCommandResolver(Class< ? super DeletePortCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_execute(java.util.List)
     */
    @Override
    protected void pre_execute(List<DeletePortCommand> deleteCommands)
    {
        Collection<EObject> selected = new ArrayList<EObject>();
        for (DeletePortCommand deleteCommand : deleteCommands)
        {
            if (deleteCommand.getRemovedFlow() != null)
            {
                selected.add(deleteCommand.getRemovedFlow());
                EList<Flow> flows = deleteCommand.getRemovedFlow().getGroup().getFlows();
                if (flows.size() < 2)
                {
                    selected.add(deleteCommand.getRemovedFlowGroup());
                }
                EMFtoGEFCommandWrapper deleteCmd = new EMFtoGEFCommandWrapper(new RemoveHierarchicalElementCommand( TopcasedAdapterFactoryEditingDomain.getEditingDomainFor(deleteCommand.getRemovedFlow()), selected));
                deleteCmd.execute();
                commands.put(deleteCommand, deleteCmd);
            }
        }

    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<DeletePortCommand> deleteCommands)
    {
        for (DeletePortCommand deleteCommand : deleteCommands)
        {
            EMFtoGEFCommandWrapper wrap = commands.get(deleteCommand);
            if (wrap != null)
            {
                wrap.redo();
            }
        }            
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#post_undo(java.util.List)
     */
    @Override
    protected void post_undo(List<DeletePortCommand> deleteCommands)
    {
        for (ListIterator<DeletePortCommand> i = deleteCommands.listIterator(deleteCommands.size()); i.hasPrevious();)
        {
            DeletePortCommand deleteCommand = i.previous();
            EMFtoGEFCommandWrapper wrap = commands.get(deleteCommand);
            if (wrap != null)
            {
                wrap.undo();
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