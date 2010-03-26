/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.actions;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.core.dialogs.UpdateAttributeConfigurationDialog;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * This action allows to update attribute configuration of a requirement model.<br>
 * It has for effect to launch the update attribute configuration dialog.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class UpdateAttributeConfigurationAction extends RequirementAction
{

    /**
     * Constructor
     * 
     * @param selection The current selection done
     * @param page The page
     */
    public UpdateAttributeConfigurationAction(IStructuredSelection selection, CurrentPage page)
    {
        super(selection, page.getEditingDomain());
        setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/update.gif")); //$NON-NLS-1$
        setText(Messages.getString("CurrentRequirementView.28")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run()
    {
        new UpdateAttributeConfigurationDialog(selection, Display.getCurrent().getActiveShell()).open();
    }

    /**
     * @see org.eclipse.jface.action.Action#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        Resource requirementModel = RequirementUtils.getRequirementModel(editingDomain);
        if (requirementModel != null)
        {
            Collection<EObject> allRequirement = RequirementUtils.getAllObjects(requirementModel, CurrentRequirement.class);
            for (EObject aReq : allRequirement)
            {
                if (aReq instanceof CurrentRequirement && ((CurrentRequirement) aReq).isImpacted())
                {
                    return false;
                }
            }
        }
        return super.isEnabled();
    }
}
