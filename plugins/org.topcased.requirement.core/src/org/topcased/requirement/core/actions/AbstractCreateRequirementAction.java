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
package org.topcased.requirement.core.actions;

import java.util.Iterator;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;

/**
 * Defines an abstract class for creating requirements in the Current Page.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public abstract class AbstractCreateRequirementAction extends Action
{

    private Object selected;

    private IStructuredSelection selection;

    /**
     * Constructor
     * 
     * @param selection The selection corresponding to the parent container.
     * @param page The current page.
     * @param title The action title
     */
    public AbstractCreateRequirementAction(IStructuredSelection selection)
    {
        this.selection = selection;
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run()
    {
        for (Iterator< ? > iter = selection.iterator(); iter.hasNext();)
        {
            Object currentObject = iter.next();
            selected = currentObject;
            if (currentObject instanceof Requirement)
            {
                selected = ((Requirement) currentObject).eContainer();
            }
            if (selected instanceof HierarchicalElement)
            {
                Command cmd = getCommand();
                if (cmd != null && cmd.canExecute())
                {
                    EditingDomain domain = TopcasedAdapterFactoryEditingDomain.getEditingDomainFor(selected);
                    domain.getCommandStack().execute(cmd);
                }
            }
        }
    }

    /**
     * Gets the selected element.
     */
    public Object getSelectedElement()
    {
        return selected;
    }

    /**
     * @see org.eclipse.jface.action.Action#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        boolean isEnabled = true;
        for (Iterator< ? > iter = selection.iterator(); iter.hasNext();)
        {
            Object currentObject = iter.next();
            if (currentObject instanceof Requirement)
            {
                Requirement requirement = (Requirement) currentObject;
                isEnabled &= requirement.eContainer() instanceof HierarchicalElement;
            }
        }
        return isEnabled;
    }

    /**
     * Abstract method for executing the creation command according to the kind of action.
     */
    public abstract Command getCommand();
}
