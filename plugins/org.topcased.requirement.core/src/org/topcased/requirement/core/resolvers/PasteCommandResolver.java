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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.StructuredSelection;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.commands.PasteHierarchicalElementCommand;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * This Class handle specific behavior for requirements when a PasteFromClipboardCommand is executed.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class PasteCommandResolver extends AdditionalCommand<PasteFromClipboardCommand>
{

    private Map<PasteFromClipboardCommand, EMFtoGEFCommandWrapper> commands;

    private Map<PasteFromClipboardCommand, org.eclipse.emf.common.command.Command> emfCommands;

    public PasteCommandResolver()
    {
        this(PasteFromClipboardCommand.class);
        commands = new HashMap<PasteFromClipboardCommand, EMFtoGEFCommandWrapper>();
        emfCommands = new HashMap<PasteFromClipboardCommand, org.eclipse.emf.common.command.Command>();
    }

    public PasteCommandResolver(Class< ? super PasteFromClipboardCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#post_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<PasteFromClipboardCommand> pasteCommands)
    {
        CompoundCommand compound = new CompoundCommand(Messages.getString("PasteCommandResolver.0")); //$NON-NLS-1$
        CurrentPage currentPage = RequirementHelper.INSTANCE.getCurrentPage();

        for (PasteFromClipboardCommand pasteCommand : pasteCommands)
        {
            Object target = pasteCommand.getOwner();
            Collection< ? > toDuplicate = HierarchicalElementTransfer.INSTANCE.getResult();
            Collection< ? > sources = pasteCommand.getAffectedObjects();

            // Handle cases of copy/cut then paste on the current requirement view
            if (target instanceof HierarchicalElement)
            {
                /*
                 * We disable automatic renaming the current requirement. The ident is final.
                 */
                // for (Object source : sources)
                // {
                // if(source instanceof CurrentRequirement)
                // {
                // // rename the current requirement
                // CurrentRequirement requirement = (CurrentRequirement) source;
                // compound.appendAndExecute(RequirementHelper.INSTANCE.renameRequirement(requirement));
                // }
                // }
                //
                // if (!compound.isEmpty() && compound.canExecute())
                // {
                // // Execute the renaming commands
                // compound.execute();
                // emfCommands.put(pasteCommand, compound);
                // }
            }
            // Handle cases of copy/cut then paste on the modeler
            else if (!toDuplicate.isEmpty())
            {
                EMFtoGEFCommandWrapper com = new EMFtoGEFCommandWrapper(new PasteHierarchicalElementCommand(AdapterFactoryEditingDomain.getEditingDomainFor(target), (EObject) target, toDuplicate));
                com.execute();
                commands.put(pasteCommand, com);
            }
        }

        if (currentPage != null && !compound.getAffectedObjects().isEmpty())
        {
            currentPage.setSelection(new StructuredSelection((List< ? >) compound.getAffectedObjects()));
        }
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<PasteFromClipboardCommand> pasteCommands)
    {
        for (PasteFromClipboardCommand pasteCommand : pasteCommands)
        {
            EMFtoGEFCommandWrapper compound = commands.get(pasteCommand);
            org.eclipse.emf.common.command.Command emfCommand = emfCommands.get(pasteCommand);
            if (compound != null)
            {
                compound.redo();
            }
            else if (emfCommand != null)
            {
                emfCommand.redo();
            }
        }
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#post_undo(java.util.List)
     */
    @Override
    protected void post_undo(List<PasteFromClipboardCommand> pasteCommands)
    {
        for (ListIterator<PasteFromClipboardCommand> i = pasteCommands.listIterator(pasteCommands.size()); i.hasPrevious();)
        {
            PasteFromClipboardCommand pasteCommand = i.previous();
            EMFtoGEFCommandWrapper compound = commands.get(pasteCommand);
            org.eclipse.emf.common.command.Command emfCommand = emfCommands.get(pasteCommand);
            if (compound != null)
            {
                compound.undo();
            }
            else if (emfCommand != null)
            {
                emfCommand.undo();
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
        List<Object> result = new ArrayList<Object>();

        // deals with PasteFromClipboardCommand (specific behaviour)
        if (command instanceof EMFtoGEFCommandWrapper)
        {
            org.eclipse.emf.common.command.Command emfCommand = ((EMFtoGEFCommandWrapper) command).getEMFCommand();

            if (emfCommand instanceof CompoundCommand)
            {
                List< ? > commands = ((CompoundCommand) emfCommand).getCommandList();
                for (Object o : commands)
                {
                    // if we got a compound command with one or more PasteFromClipboardCommand, we add the command to
                    // the
                    // result
                    if (o instanceof PasteFromClipboardCommand)
                    {
                        result.add((PasteFromClipboardCommand) o);
                    }
                }
            }
            else if (emfCommand instanceof PasteFromClipboardCommand)
            {
                result.add((PasteFromClipboardCommand) emfCommand);
            }
            else
            {
                // same algorithm than CommandStack.getCommands
                List<Object> tmp = CommandStack.getCommands(command, clazz);
                if (!(tmp.isEmpty()))
                {
                    result.add(tmp);
                }
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