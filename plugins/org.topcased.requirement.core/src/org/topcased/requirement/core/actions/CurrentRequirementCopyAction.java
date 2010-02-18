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

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CopyToClipboardCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.core.Messages;

/**
 * This class defines the EMF <b>copy</b> command for the Current Requirements.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class CurrentRequirementCopyAction extends RequirementAbstractEMFAction
{

    /**
     * Constructor
     * 
     * @param selection The current selection done
     * @param editingDomain The editing domain to use.
     */
    public CurrentRequirementCopyAction(IStructuredSelection selection, EditingDomain editingDomain)
    {
        super(Messages.getString("CurrentRequirementView.17"), selection, editingDomain); //$NON-NLS-1$
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
        setActionDefinitionId("org.topcased.requirement.core.copyModelObject"); //$NON-NLS-1$
        setToolTipText(Messages.getString("CurrentRequirementView.18")); //$NON-NLS-1$
    }

    /**
     * @see org.topcased.requirement.core.actions.RequirementAbstractEMFAction#initialize()
     */
    @Override
    public void initialize()
    {
        setCommand(CopyToClipboardCommand.class);
        setParam(new CommandParameter(null, null, getSelection().toList()));
    }

    /**
     * @see org.topcased.requirement.core.actions.RequirementAbstractEMFAction#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        if (super.isEnabled())
        {
            for (Object obj : getSelection().toList())
            {
                if (!(obj instanceof Requirement))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}