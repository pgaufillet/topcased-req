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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.CopyToClipboardCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.actions.HierarchicalElementTransfer;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This Class handle specific behaviour for requirements when a CopyToClipboardCommand is executed.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class CopyCommandResolver extends AdditionalCommand<CopyToClipboardCommand>
{

    private Map<CopyToClipboardCommand, CompoundCommand> commands;

    public CopyCommandResolver()
    {
        this(CopyToClipboardCommand.class);
        commands = new HashMap<CopyToClipboardCommand, CompoundCommand>();
    }

    public CopyCommandResolver(Class< ? super CopyToClipboardCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#post_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<CopyToClipboardCommand> copyCommands)
    {
        HierarchicalElementTransfer.INSTANCE.clear();
        for (CopyToClipboardCommand copyCommand : copyCommands)
        {
            List<EObject> hierarchicalElt = new ArrayList<EObject>();
            List< ? > inSelection = (List< ? >) copyCommand.getSourceObjects();

            for (int i = 0; i < inSelection.size(); i++)
            {
                HierarchicalElement elt = RequirementUtils.getHierarchicalElementFor(inSelection.get(i));
                if (elt != null)
                {
                    EObject copy = EcoreUtil.copy(elt);
                    Object inClipboard = AdapterFactoryEditingDomain.getEditingDomainFor(inSelection.get(i)).getClipboard().toArray()[i];
                    copy.eSet(RequirementPackage.eINSTANCE.getHierarchicalElement_Element(), inClipboard);
                    hierarchicalElt.add(copy);
                }
            }
            HierarchicalElementTransfer.INSTANCE.setResult(hierarchicalElt);

        }

    }

    /**
     * @see org.topcased.requirement.core.commands.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<CopyToClipboardCommand> copyCommands)
    {
        for (CopyToClipboardCommand copyCommand : copyCommands)
        {
            CompoundCommand compound = commands.get(copyCommand);
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
    protected void post_undo(List<CopyToClipboardCommand> copyCommands)
    {
        for (ListIterator<CopyToClipboardCommand> i = copyCommands.listIterator(copyCommands.size()); i.hasPrevious();)
        {
            CopyToClipboardCommand copyCommand = i.previous();
            CompoundCommand compound = commands.get(copyCommand);
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
        
        // deals with CopyToClipboardCommand (specific behaviour)
        if (command instanceof EMFtoGEFCommandWrapper)
        {
            org.eclipse.emf.common.command.Command cmd = ((EMFtoGEFCommandWrapper) command).getEMFCommand();
            if (cmd instanceof CopyToClipboardCommand)
            {
                if (((CopyToClipboardCommand) cmd).getClass().equals(clazz))
                {
                    result.add((CopyToClipboardCommand) cmd);
                }
            }
        }
        else
        {
            // same algo than CommandStack.getCommands
            List<Object> tmp = CommandStack.getCommands(command, clazz);
            if (!(tmp.isEmpty()))
            {
                result.add(tmp);
            }
        }
        return result;

    }
}
