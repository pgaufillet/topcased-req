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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.topcased.facilities.util.EMFMarkerUtil;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.core.Messages;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementHelper;

/**
 * This action allows to set a Current Requirement as valid after an update operation.<br>
 * The warning is so removed from the Problem View and the decorator presented in the viewer is also deleted.<br>
 * The <b>impacted</b> feature of the Current Requirement(s) is/are switch to <code>false</code><br>
 * The feature <b>status</b> of the upstream requirement(s) is/are also modified.<br>
 * 
 * Update : 06 may 2009<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class SetAsValidAction extends RequirementAction
{

    /**
     * Constructor
     * 
     * @param theSelection The current selection done in the viewer
     * @param ed The editing domain to use
     */
    public SetAsValidAction(IStructuredSelection theSelection, EditingDomain ed)
    {
        super(theSelection, ed);
        setText(Messages.getString("SetAsValidAction.0")); //$NON-NLS-1$
        setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/checked.gif")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run()
    {
        CompoundCommand compoundCmd = new CompoundCommand("Revert as no-impacted"); //$NON-NLS-1$
        Iterator< ? > it = ((StructuredSelection) selection).iterator();
        while (it.hasNext())
        {
            try
            {
                // gets the concerned EObject
                EObject currentRequirement = (EObject) it.next();

                if (currentRequirement instanceof CurrentRequirement)
                {
                    CurrentRequirement req = (CurrentRequirement) currentRequirement;
                    for (Attribute anAttribute : req.getAttribute())
                    {
                        // Try to remove marker for each attribute.
                        EMFMarkerUtil.removeMarkerFor(anAttribute);
                    }
                }

                // then update the feature
                compoundCmd.appendIfCanExecute(RequirementHelper.INSTANCE.revertImpact(currentRequirement));
            }
            catch (CoreException e)
            {
                RequirementCorePlugin.log("Marker cannot be removed : ", IStatus.ERROR, e); //$NON-NLS-1$
            }
        }
        if (!compoundCmd.isEmpty() && compoundCmd.canExecute())
        {
            editingDomain.getCommandStack().execute(compoundCmd);
        }

    }

    /**
     * The selection must contain only impacted current requirements.
     * 
     * @see org.eclipse.jface.action.Action#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        // multiple selection is allowed
        if (selection != null && !selection.isEmpty())
        {
            boolean enable = true;
            for (Iterator< ? > it = selection.iterator(); it.hasNext();)
            {
                Object object = it.next();
                enable &= object instanceof CurrentRequirement && ((CurrentRequirement) object).isImpacted();
            }
            return enable;
        }
        return false;
    }
}
