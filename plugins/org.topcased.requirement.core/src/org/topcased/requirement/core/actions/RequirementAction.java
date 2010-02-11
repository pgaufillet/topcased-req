/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.actions;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;

/**
 * A class to manage the action in the two requirement views.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public abstract class RequirementAction extends Action
{
    protected Viewer viewer;

    protected IStructuredSelection selection;

    protected EditingDomain editingDomain;

    /**
     * Constructor
     * 
     * @param theSelection The current selection
     * @param viewer The viewer
     * @param ed The editing domain
     */
    public RequirementAction(IStructuredSelection theSelection, Viewer viewer, EditingDomain ed)
    {
        this(viewer, ed);
        selection = theSelection;
    }

    /**
     * Constructor
     * 
     * @param theSelection The current selection
     * @param ed The editing domain
     */
    public RequirementAction(IStructuredSelection theSelection, EditingDomain ed)
    {
        this(theSelection, null, ed);
    }

    /**
     * Constructor
     * 
     * @param theViewer The tree viewer
     * @param ed The editing domain
     */
    public RequirementAction(Viewer theViewer, EditingDomain ed)
    {
        super();
        viewer = theViewer;
        editingDomain = ed;
    }

    /**
     * Sets a selection
     * 
     * @param newSelection A new selection done
     */
    public void setSelection(IStructuredSelection newSelection)
    {
        selection = newSelection;
    }
}