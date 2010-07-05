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
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * Handles move operations of hierarchical elements and current requirements when a drag and drop is performed from the
 * outline.<br>
 * 
 * Updated : 03 july 2009<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class MoveHierarchicalElementCommand extends CompoundCommand
{

    private EditingDomain domain;

    private Collection< ? > elements;

    private EObject container;

    private boolean targetCreated;

    private HierarchicalElement targetElt;

    private List<EObject> toSelect;

    /**
     * Constructor
     * 
     * @param newContainer The target container
     * @param children The collection of dragged/moved objects.
     */
    public MoveHierarchicalElementCommand(EObject newContainer, Collection< ? > children)
    {
        super(Messages.getString("MoveHierarchicalElementCommand.0")); //$NON-NLS-1$
        domain = TopcasedAdapterFactoryEditingDomain.getEditingDomainFor(newContainer);
        container = newContainer;
        elements = children;
        targetCreated = false;
        toSelect = new ArrayList<EObject>();
        initializeCommands();
    }

    /**
     * Initializes EMF Commands to handle consistency between the outline and the Current Requirement View.<br>
     * This method is intended to be overridden by clients.
     */
    protected void initializeCommands()
    {
        removeFromSource();
        addToTarget();
    }

    /**
     * Adds {@link RemoveCommand}s responsible for deleting the older {@link HierarchicalElement}.
     */
    private void removeFromSource()
    {
        for (Object element : elements)
        {
            HierarchicalElement sourceElt = RequirementUtils.getHierarchicalElementFor(element);
            if (sourceElt != null)
            {
                appendIfCanExecute(RemoveCommand.create(domain, sourceElt));
            }
        }
    }

    /**
     * Adds {@link AddCommand}s responsible for adding the new {@link HierarchicalElement}.
     */
    private void addToTarget()
    {
        for (Object element : elements)
        {
            HierarchicalElement sourceElt = RequirementUtils.getHierarchicalElementFor(element);
            if (sourceElt != null)
            {
                // adjustTarget(element);
                initializeTarget();
                appendIfCanExecute(AddCommand.create(domain, targetElt, RequirementPackage.eINSTANCE.getHierarchicalElement_Children(), sourceElt));
                toSelect.add(sourceElt);
            }
        }
    }

    /**
     * Initializes the target Hierarchical Element. This method is executed only one time per Command.
     */
    private void initializeTarget()
    {
        if (!targetCreated)
        {
            targetElt = RequirementUtils.getHierarchicalElementFor(container);
            if (targetElt == null)
            {
                targetElt = RequirementHelper.INSTANCE.getHierarchicalElement(container, this);
            }
            targetCreated = true;
        }
    }

    /**
     * Sets the focus on the moved elements presented on the current page.
     */
    private void setFocusOnSelection()
    {
        CurrentPage page = RequirementHelper.INSTANCE.getCurrentPage();
        page.setSelection(new StructuredSelection(toSelect));
    }

    /**
     * @see org.eclipse.emf.common.command.CompoundCommand#execute()
     */
    @Override
    public void execute()
    {
        super.execute();
        setFocusOnSelection();
    }

    /**
     * @see org.eclipse.emf.common.command.CompoundCommand#undo()
     */
    @Override
    public void undo()
    {
        super.undo();
        setFocusOnSelection();
    }

    /**
     * @see org.eclipse.emf.common.command.CompoundCommand#redo()
     */
    public void redo()
    {
        super.redo();
        setFocusOnSelection();
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
     */
    @Override
    public boolean canExecute()
    {
        boolean result = RequirementUtils.hasRequirementModel(TopcasedAdapterFactoryEditingDomain.getEditingDomainFor(container));
        return super.canExecute() && elements != null && !elements.isEmpty() && container != null && result;
    }
}
