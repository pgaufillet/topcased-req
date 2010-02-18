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

package org.topcased.requirement.core.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.requirement.core.actions.HierarchicalElementTransfer;

/**
 * This Class handle specific behaviour for requirements when a PasteFromClipboardCommand is executed.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class PasteCommandResolver extends AdditionalCommand<PasteFromClipboardCommand>
{

    private Map<PasteFromClipboardCommand, CompoundCommand> commands;

    public PasteCommandResolver()
    {
        this(PasteFromClipboardCommand.class);
        commands = new HashMap<PasteFromClipboardCommand, CompoundCommand>();
    }

    public PasteCommandResolver(Class< ? super PasteFromClipboardCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#post_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<PasteFromClipboardCommand> pasteCommands)
    {
        CompoundCommand command = new CompoundCommand();
        for (PasteFromClipboardCommand pasteCommand : pasteCommands)
        {

            Object target = pasteCommand.getOwner();

                Collection< ? > toDuplicate = HierarchicalElementTransfer.INSTANCE.getResult();
                if (!toDuplicate.isEmpty())
                {
                    PasteHierarchicalElementCommand com = new PasteHierarchicalElementCommand(AdapterFactoryEditingDomain.getEditingDomainFor(target),(EObject) target, toDuplicate);
                    command.add(new EMFtoGEFCommandWrapper(com));
                }         

                commands.put(pasteCommand, command);
        }
        command.execute();

    }
    
    
    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<PasteFromClipboardCommand> pasteCommands)
    {
        for (PasteFromClipboardCommand pasteCommand : pasteCommands)
        {
            CompoundCommand compound = commands.get(pasteCommand);
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
    protected void post_undo(List<PasteFromClipboardCommand> pasteCommands)
    {
        for (ListIterator<PasteFromClipboardCommand> i = pasteCommands.listIterator(pasteCommands.size()); i.hasPrevious();)
        {
            PasteFromClipboardCommand pasteCommand = i.previous();
            CompoundCommand compound = commands.get(pasteCommand);
            if (compound != null)
            {
                compound.undo();
            }
        }
    }
    

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#getSpecificCommands(org.eclipse.gef.commands.Command, java.lang.Class)
     */
    @Override
    protected List<Object> getSpecificCommands(Command command, Class< ? > clazz)
    {
        List<Object> result = new LinkedList<Object>();
        
        // deals with PasteFromClipboardCommand (specific behaviour)
        if (command instanceof EMFtoGEFCommandWrapper)
        {
            org.eclipse.emf.common.command.Command cmd = ((EMFtoGEFCommandWrapper) command).getEMFCommand();
            if (cmd instanceof PasteFromClipboardCommand)
            {
                if (((PasteFromClipboardCommand) cmd).getClass().equals(clazz))
                {
                    result.add((PasteFromClipboardCommand) cmd);
                }
            }
        }
        else
        {
         // same behavior as CommandStack.getCommands
            List<Object> tmp = CommandStack.getCommands(command, clazz);
            if (!(tmp.isEmpty()))
            {
                result.add(tmp);
            }
        }
        return result;
    }
}