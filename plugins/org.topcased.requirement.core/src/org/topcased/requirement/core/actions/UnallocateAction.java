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

import java.util.Iterator;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.topcased.requirement.AnonymousRequirement;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeAllocate;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.Messages;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * The action "Unallocate" for a hierarchical element or a current requirement
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class UnallocateAction extends RequirementAction
{
    /** A single EMF compound command */
    private CompoundCommand compoundCmd;

    /**
     * Constructor
     * 
     * @param selection The selection done
     * @param page The current page.
     * @param editingDomain The editing domain
     */
    public UnallocateAction(IStructuredSelection selection, CurrentPage page)
    {
        super(selection, page.getViewer(), page.getEditingDomain());
        setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/unallocate.gif")); //$NON-NLS-1$
        setText(Messages.getString("UnallocateAction.0")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run()
    {
        compoundCmd = new CompoundCommand(Messages.getString("UnallocateAction.0")); //$NON-NLS-1$

        for (Iterator< ? > iter = selection.iterator(); iter.hasNext();)
        {
            Object currObject = iter.next();
            if (currObject instanceof HierarchicalElement)
            {
                unallocate((HierarchicalElement) currObject);
            }
            else if (currObject instanceof CurrentRequirement || currObject instanceof AnonymousRequirement)
            {
                unallocate((Requirement) currObject);
            }
        }

        if (!compoundCmd.isEmpty() && compoundCmd.canExecute())
        {
            // Execute it.
            editingDomain.getCommandStack().execute(compoundCmd);
        }
        viewer.refresh();
    }

    /**
     * Unallocates a current requirement.
     * 
     * @param currReq A {@link CurrentRequirement}.
     */
    private void unallocate(Requirement currReq)
    {
        for (Attribute attribute : currReq.getAttribute())
        {
            if (attribute instanceof AttributeAllocate)
            {
                Command set = new SetCommand(editingDomain, attribute, RequirementPackage.eINSTANCE.getObjectAttribute_Value(), null);
                compoundCmd.appendIfCanExecute(set);
            }
        }
    }

    /**
     * Unallocates all requirements found inside a given Hierarchical Element.
     * 
     * @param element A {@link HierarchicalElement}.
     */
    private void unallocate(HierarchicalElement element)
    {
        for (Requirement currentReq : element.getRequirement())
        {
            unallocate(currentReq);
        }
        for (HierarchicalElement children : element.getChildren())
        {
            unallocate(children);
        }
    }
}
