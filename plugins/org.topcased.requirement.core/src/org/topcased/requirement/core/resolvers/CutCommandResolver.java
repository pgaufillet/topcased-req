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

package org.topcased.requirement.core.resolvers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CutToClipboardCommand;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain;
import org.topcased.requirement.core.commands.RemoveHierarchicalElementCommand;

/**
 * This Class handle specific behaviour for requirements when a CutToClipboardCommand is executed.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class CutCommandResolver extends AdditionalCommand<CutToClipboardCommand>
{

    private Map<CutToClipboardCommand, CompoundCommand> commands;

    public CutCommandResolver()
    {
        this(CutToClipboardCommand.class);
        commands = new HashMap<CutToClipboardCommand, CompoundCommand>();
    }

    public CutCommandResolver(Class< ? super CutToClipboardCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_execute(java.util.List)
     */
    @Override
    protected void pre_execute(List<CutToClipboardCommand> cutCommands)
    {
        CompoundCommand command = new CompoundCommand();
        HierarchicalElementTransfer.INSTANCE.clear();
        for (CutToClipboardCommand cutCommand : cutCommands)
        {
            Collection< ? > eObjects = cutCommand.getCommand().getResult();

            for (Object e : eObjects)
            {
                TopcasedAdapterFactoryEditingDomain.getEditingDomainFor(e);
                if (e instanceof EObject)
                {
                    RemoveHierarchicalElementCommand cmd = new RemoveHierarchicalElementCommand((EObject) e);
                    command.add(new EMFtoGEFCommandWrapper(cmd));
                    HierarchicalElementTransfer.INSTANCE.setResult(cmd.getResult());
                }
            }
            commands.put(cutCommand, command);
            command.execute();
        }

    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<CutToClipboardCommand> cutCommands)
    {
        for (CutToClipboardCommand cutCommand : cutCommands)
        {
            CompoundCommand compound = commands.get(cutCommand);
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
    protected void post_undo(List<CutToClipboardCommand> cutCommands)
    {
        for (ListIterator<CutToClipboardCommand> i = cutCommands.listIterator(cutCommands.size()); i.hasPrevious();)
        {
            CutToClipboardCommand cutCommand = i.previous();
            CompoundCommand compound = commands.get(cutCommand);
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