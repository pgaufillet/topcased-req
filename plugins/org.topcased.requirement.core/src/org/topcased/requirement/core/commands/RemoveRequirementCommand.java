/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.commands;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.core.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * When a model element is deleted, the requirement model must be updated.<br>
 * This command handles expected modifications :
 * <ul>
 * <li>Put the Hierarchical Element and all its involved requirements to the Trash Chapter (DeleteCommand then
 * AddCommand)</li>
 * <li>From the requirement model, delete if necessary the corresponding hierarchical elements.</li>
 * </ul>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 * 
 */
public class RemoveRequirementCommand extends CompoundCommand
{
    /** The hierarchical element to delete */
    private HierarchicalElement toDelete;

    /** The editing domain to use */
    private EditingDomain editingDomain;

    /**
     * Constructor
     * 
     * @param deleted Represents the model object which is going to be deleted
     */
    public RemoveRequirementCommand(EditingDomain domain, EObject deleted)
    {
        super(Messages.getString("RemoveRequirementCommand.0")); //$NON-NLS-1$
        editingDomain = domain;
        toDelete = RequirementUtils.getHierarchicalElementFor(deleted);
        initializeCommands();
    }

    /**
     * Initializes the commands contained in this compound command
     */
    protected void initializeCommands()
    {
        if (toDelete != null)
        {
            // 1) The HierarchicalElement is removed from the model
            appendIfCanExecute(RemoveCommand.create(editingDomain, toDelete));

            // 2) Then, hierarchical element and its requirements are added to the trash chapter.
            SpecialChapter trash = RequirementUtils.getTrashChapter(editingDomain);
            Command addTrashCmd = AddCommand.create(editingDomain, trash, RequirementPackage.eINSTANCE.getSpecialChapter_HierarchicalElement(), toDelete);
            appendIfCanExecute(addTrashCmd);
        }
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
     */
    @Override
    public boolean canExecute()
    {
        return super.canExecute() && toDelete != null && editingDomain != null;
    }
}
