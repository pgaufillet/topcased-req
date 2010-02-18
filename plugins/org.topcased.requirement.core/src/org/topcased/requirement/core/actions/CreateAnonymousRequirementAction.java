/*****************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
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

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.Messages;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementHelper;

/**
 * Action accessible from a hierarchical element or a requirement.<br>
 * It allows to create a new {@link AnonymousRequirement}.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class CreateAnonymousRequirementAction extends AbstractCreateRequirementAction
{
    /**
     * Constructor
     * 
     * @param selection The selection done
     */
    public CreateAnonymousRequirementAction(IStructuredSelection selection)
    {
        super(selection);
        setText(Messages.getString("CreateAnonymousRequirementAction.0")); //$NON-NLS-1$
        setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/anonymous.gif")); //$NON-NLS-1$
    }

    /**
     * @see org.topcased.requirement.core.actions.AbstractCreateRequirementAction#getCommand()
     */
    @Override
    public Command getCommand()
    {
        HierarchicalElement hierarchicalElt = (HierarchicalElement) getSelectedElement();
        CompoundCommand cmd = new CompoundCommand(Messages.getString("CreateAnonymousRequirementAction.0"));
        cmd.append(RequirementHelper.INSTANCE.createAnonymousRequirement(hierarchicalElt.getElement()));
        return cmd.unwrap();
    }
}