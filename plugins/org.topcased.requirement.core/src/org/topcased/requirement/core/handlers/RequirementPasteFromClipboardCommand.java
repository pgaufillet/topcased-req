/*****************************************************************************
 * Copyright (c) 2012 Atos.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Anass RADOUANI (Atos) anass.radouani@atos.net - Initial API and implementation
 *
  *****************************************************************************/

package org.topcased.requirement.core.handlers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementHelper;

public class RequirementPasteFromClipboardCommand extends PasteFromClipboardCommand
{
    /** Store all the commands for the redo and undo     */
    private Map<PasteFromClipboardCommand, Command> commands;

    public RequirementPasteFromClipboardCommand(EditingDomain domain, Object owner, Object feature, int index)
    {
        super(domain, owner, feature, index);
        commands = new HashMap<PasteFromClipboardCommand, Command>();
    }
    
    @Override
    public void doExecute()
    {
        super.doExecute();
        
        Collection< ? > sources = getAffectedObjects();
        Object target = getOwner();
        if (target instanceof HierarchicalElement)
        {
            for (Object source : sources)
            {
                if (source instanceof CurrentRequirement)
                {
                    // rename the current requirement
                    CurrentRequirement requirement = (CurrentRequirement) source;
                    CompoundCommand compound = new CompoundCommand(Messages.getString("RequirementPasteFromClipboardCommand.0")); //$NON-NLS-1$
                    compound.append(RequirementHelper.INSTANCE.renameRequirement(requirement));
                    if (!compound.isEmpty() && compound.canExecute())
                    {
                        // Execute the renaming commands
                        compound.execute();
                        commands.put(this, compound);
                    }
                }
            }

        }
    }
    
    @Override
    public void doRedo()
    {
        super.doRedo();
        Command compound = commands.get(this);
        if (compound != null)
        {
            compound.redo();
        }
    }
    
    @Override
    public void doUndo()
    {
        super.doUndo();
        Command compound = commands.get(this);
        if (compound != null)
        {
            compound.undo();
        }
    }
    
    
}
