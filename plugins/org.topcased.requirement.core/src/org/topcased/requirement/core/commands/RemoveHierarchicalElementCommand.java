/***********************************************************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Handles remove operations of a hierarchical element and its requirements when the cut action of the modeler's contextual menu is used.<br>
 * 
 * Update : 06 may 2009<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class RemoveHierarchicalElementCommand extends CompoundCommand
{
    /** The editing domain to use to perform this operation */
    private EditingDomain editingDomain;

    /** List of selected objects to remove */
    private Collection<EObject> selected;

    /**
     * Constructor
     * 
     * @param objects A collection of model objects for which the matching {@link HierarchicalElement} must be removed.
     */
    public RemoveHierarchicalElementCommand(EditingDomain domain, Collection<EObject> objects)
    {
        super(Messages.getString("RemoveHierarchicalElementCommand.0")); //$NON-NLS-1$
        editingDomain = domain;
        selected = objects;
        initializeCommands();
    }

    /**
     * Constructor
     * 
     * @param obj A single model object for which the matching {@link HierarchicalElement} must be removed.
     */
    public RemoveHierarchicalElementCommand(EObject obj)
    {
        super(Messages.getString("RemoveHierarchicalElementCommand.0")); //$NON-NLS-1$
        editingDomain = TopcasedAdapterFactoryEditingDomain.getEditingDomainFor(obj);
        selected = new ArrayList<EObject>();
        selected.add(obj);
        initializeCommands();
    }

    /**
     * Initializes EMF Commands to handle consistency between the outline and the Current Requirement View.<br>
     * This method is intended to be overridden by clients.
     */
    protected void initializeCommands()
    {
        List<EObject> toRemove = new ArrayList<EObject>();
        for (Iterator<EObject> it = selected.iterator(); it.hasNext();)
        {
            HierarchicalElement elt = RequirementUtils.getHierarchicalElementFor(it.next());
            if (elt != null)
            {
                toRemove.add(elt);
            }
        }
        appendIfCanExecute(RemoveCommand.create(editingDomain, toRemove));
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
