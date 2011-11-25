/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Olivier Mélois <a href="mailto:olivier.melois@atos.net">olivier.melois@atos.net</a>"
 * 
 **********************************************************************************************************************/

package org.topcased.requirement.core.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Command allowing to put a CurrentRequirement in a trashChapter.
 * 
 * @author Olivier Mélois <a href="mailto:olivier.melois@atos.net">olivier.melois@atos.net</a>"
 * 
 */
public class TrashCurrentReqCommand extends CompoundCommand
{
    private Collection<EObject> selected;

    private EditingDomain editingDomain;

    public TrashCurrentReqCommand(EditingDomain domain, EObject trash)
    {
        super("TrashCurrentReqCommand");
        editingDomain = domain;
        selected = new ArrayList<EObject>();
        if (trash != null)
        {
            selected.add(trash);
        }
        initializeCommands();
    }

    /**
     * Constructor
     * 
     * @param objects A collection of model objects for which the matching {@link HierarchicalElement} must be removed.
     */
    public TrashCurrentReqCommand(EditingDomain domain, Collection<EObject> trash)
    {
        super(Messages.getString("TrashCurrentReqCommand"));
        editingDomain = domain;
        selected = trash;
        initializeCommands();
    }

    /**
     * Initializes the commands contained in this compound command
     */
    protected void initializeCommands()
    {
        // The trash is the list of items that have to be added to the trash chapter.
        List<EObject> trash = new ArrayList<EObject>();
        for (EObject eObject : selected)
        {
            if (eObject instanceof Requirement)
            {
                trash.add(eObject);
            }
        }

        if (!trash.isEmpty())
        {
            // Removing the requirements from the model.
            appendIfCanExecute(RemoveCommand.create(editingDomain, trash));

            // Adding the requirements to the trash chapter.
            SpecialChapter trashChapter = RequirementUtils.getTrashChapter(editingDomain);
            appendIfCanExecute(AddCommand.create(editingDomain, trashChapter, RequirementPackage.eINSTANCE.getSpecialChapter_Requirement(), trash));
        }

    }

    @Override
    public boolean canExecute()
    {
        return super.canExecute() && !selected.isEmpty() && editingDomain != null;
    }
}
