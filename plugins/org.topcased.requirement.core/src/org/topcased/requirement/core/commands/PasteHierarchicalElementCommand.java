/***********************************************************************************************************************
 * Copyright (c) 2008-2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.commands;

import java.util.Collection;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * Handles paste operations of hierarchical elements when the paste action of the modeler's contextual menu is used.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 * 
 */
public class PasteHierarchicalElementCommand extends CompoundCommand
{

    private EditingDomain editingDomain;

    private EObject target;

    private HierarchicalElement targetElt;

    private Collection< ? > toCopy;

    public PasteHierarchicalElementCommand(EditingDomain domain, EObject target, Collection< ? > toCopy)
    {
        super(Messages.getString("PasteHierarchicalElementCommand.0")); //$NON-NLS-1$
        this.target = target;
        this.toCopy = toCopy;
        editingDomain = domain;
        initializeCommands();
    }

    /**
     * Initializes EMF Commands to handle consistency between the outline and the Current Requirement View.<br>
     * This method is intended to be overridden by clients.
     */
    protected void initializeCommands()
    {
        initializeTarget();
        appendIfCanExecute(AddCommand.create(editingDomain, targetElt, RequirementPackage.eINSTANCE.getHierarchicalElement_Children(), toCopy));
    }

    /**
     * Initializes the target Hierarchical Element. This method is executed only one time per Command.
     */
    private void initializeTarget()
    {
        targetElt = RequirementUtils.getHierarchicalElementFor(target);
        if (targetElt == null)
        {
            targetElt = RequirementHelper.INSTANCE.getHierarchicalElement(target, this);
        }
    }

    /**
     * Refreshes the current page viewer
     */
    private void refreshViewer()
    {
        CurrentPage page = RequirementHelper.INSTANCE.getCurrentPage();
        page.setSelection(new StructuredSelection(toCopy));
    }

    /**
     * @see org.eclipse.emf.common.command.CompoundCommand#execute()
     */
    @Override
    public void execute()
    {
        super.execute();
        refreshViewer();
    }

    /**
     * @see org.eclipse.emf.common.command.CompoundCommand#undo()
     */
    @Override
    public void undo()
    {
        super.undo();
        refreshViewer();
    }

    /**
     * @see org.eclipse.emf.common.command.CompoundCommand#redo()
     */
    public void redo()
    {
        super.redo();
        refreshViewer();
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
     */
    @Override
    public boolean canExecute()
    {
        return super.canExecute() && !toCopy.isEmpty() && target != null;
    }
}
