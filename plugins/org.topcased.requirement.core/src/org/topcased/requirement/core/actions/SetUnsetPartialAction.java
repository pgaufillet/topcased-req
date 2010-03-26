/***********************************************************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe Mertz (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.actions;

import java.util.Iterator;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * The action to set/unset partial status from an {@link AttributeLink}.<br>
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class SetUnsetPartialAction extends RequirementAction
{

    /**
     * Constructor
     * 
     * @param selection The selection done
     * @param page The current page
     */
    public SetUnsetPartialAction(IStructuredSelection selection, CurrentPage page)
    {
        super(selection, page.getViewer(), page.getEditingDomain());
        setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/partial.gif")); //$NON-NLS-1$
        setText(Messages.getString("SetUnsetPartialAction.0")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run()
    {
        CompoundCommand compoundCmd = new CompoundCommand(Messages.getString("SetUnsetPartialAction.1")); //$NON-NLS-1$

        for (Iterator< ? > iter = selection.iterator(); iter.hasNext();)
        {
            AttributeLink attr = (AttributeLink) iter.next();
            Boolean value = !attr.getPartial();
            SetCommand setCmd = new SetCommand(editingDomain, attr, RequirementPackage.eINSTANCE.getAttributeLink_Partial(), value);
            compoundCmd.appendIfCanExecute(setCmd);
        }

        if (compoundCmd != null && compoundCmd.canExecute())
        {
            editingDomain.getCommandStack().execute(compoundCmd);
        }
    }
}
