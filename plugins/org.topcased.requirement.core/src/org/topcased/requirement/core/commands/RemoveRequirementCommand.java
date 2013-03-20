/***********************************************************************************************************************
 * Copyright (c) 2010 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 * Anass RADOUANI (Atos) anass.radouani@atos.net - add constructor to specify the hierarchical element to delete 
 *      when we are in the semantic element post deletion
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.core.internal.Messages;
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
    /** List of selected objects to remove */
    private Collection<EObject> selected;

    /** The editing domain to use */
    private EditingDomain editingDomain;

    /** Hierarchical Element to remove */
    private HierarchicalElement hElement;

    /**
     * Constructor
     * 
     * @param deleted Represents the model object which is going to be deleted
     */
    public RemoveRequirementCommand(EditingDomain domain, EObject deleted)
    {
        super(Messages.getString("RemoveRequirementCommand.0")); //$NON-NLS-1$
        editingDomain = domain;
        selected = new ArrayList<EObject>();
        if (deleted != null)
        {
            selected.add(deleted);
        }
        initializeCommands();
    }

    /**
     * Constructor
     * 
     * @param domain The editing domain to use
     * @param deleted Represents the model object which is going to be deleted
     * @param hElement Represents the Hierarchical Element that will be added to the liste of element to delete
     */
    public RemoveRequirementCommand(EditingDomain domain, EObject deleted, HierarchicalElement hElement)
    {
        super(Messages.getString("RemoveRequirementCommand.0")); //$NON-NLS-1$
        editingDomain = domain;
        selected = new ArrayList<EObject>();
        if (deleted != null)
        {
            selected.add(deleted);
        }
        this.hElement = hElement;
        initializeCommands();
    }
    
    /**
     * Constructor
     * 
     * @param objects A collection of model objects for which the matching {@link HierarchicalElement} must be removed.
     */
    public RemoveRequirementCommand(EditingDomain domain, Collection<EObject> deleted)
    {
        super(Messages.getString("RemoveHierarchicalElementCommand.0")); //$NON-NLS-1$
        editingDomain = domain;
        selected = deleted;
        initializeCommands();
    }

    /**
     * Initializes the commands contained in this compound command
     */
    protected void initializeCommands()
    {
        List<EObject> toRemove = new ArrayList<EObject>();
        
        for (Iterator<EObject> it = selected.iterator(); it.hasNext();)
        {
            HierarchicalElement elt = RequirementUtils.getHierarchicalElementFor(it.next());
            if (elt != null && !RequirementUtils.istrashChapterChild(elt))
            {
                toRemove.add(elt);
            }
        }

        if (hElement != null && !toRemove.contains(hElement))
        {
            toRemove.add(hElement);
        }
        
        if (toRemove.isEmpty())
        {
            return;
        }
        
        // 1) The HierarchicalElement(s) is/are removed from the model
        appendIfCanExecute(RemoveCommand.create(editingDomain, toRemove));

        // 2) Then, hierarchical element and its requirements are added to the trash chapter.
        SpecialChapter trash = RequirementUtils.getTrashChapter(editingDomain);
        appendIfCanExecute(AddCommand.create(editingDomain, trash, RequirementPackage.eINSTANCE.getSpecialChapter_HierarchicalElement(), toRemove));
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
     */
    @Override
    public boolean canExecute()
    {
        return super.canExecute() && !selected.isEmpty() && editingDomain != null;
    }
}
