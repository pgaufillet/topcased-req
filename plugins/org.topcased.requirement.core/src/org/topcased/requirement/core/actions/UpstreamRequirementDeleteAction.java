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
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.topcased.requirement.core.Messages;

/**
 * This class defines the EMF <b>delete</b> command for the Upstream Requirements.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class UpstreamRequirementDeleteAction extends RequirementAbstractEMFAction
{
    /**
     * Constructor
     * 
     * @param selection The current selection done
     * @param editingDomain The editing domain to use.
     */
    public UpstreamRequirementDeleteAction(IStructuredSelection selection, EditingDomain editingDomain)
    {
        super(Messages.getString("CurrentRequirementView.23"), selection, editingDomain); //$NON-NLS-1$
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
    }

    /**
     * @see org.topcased.requirement.core.actions.RequirementAbstractEMFAction#initialize()
     */
    @Override
    public void initialize()
    {
        setCommand(DeleteCommand.class);
        setParam(new CommandParameter(null, null, getSelection().toList()));
    }

}