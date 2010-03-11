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
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.Messages;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.utils.DefaultAttachmentPolicy;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This action sets to <code>null</code> the <b>Requirement</b> feature of a model.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class UnlinkRequirementModelAction extends Action
{

    private EditingDomain editingDomain;

    /**
     * Default constructor
     * 
     * @param domain The Editing Domain
     */
    public UnlinkRequirementModelAction(EditingDomain domain)
    {
        super("Unlink", IAction.AS_PUSH_BUTTON);
        setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/unlink.gif"));
        editingDomain = domain;
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run()
    {
        boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), Messages.getString("UnlinkRequirementModelAction.0"), //$NON-NLS-1$
        Messages.getString("UnlinkRequirementModelAction.1")); //$NON-NLS-1$
        
        if (result)
        {
            IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(editingDomain);
            
            if (policy != null)
            {
                policy.unlinkRequirementModel(policy.getLinkedTargetModel(editingDomain.getResourceSet()), RequirementUtils.getRequirementModel(editingDomain));         
            }
            else
            {
                DefaultAttachmentPolicy.getInstance().unlinkRequirementModel(Utils.getCurrentModeler().getResourceSet().getResources().get(0),RequirementUtils.getRequirementModel(editingDomain));
            }
            

            // the content of each page (Upstream & Current) is updated
            if (RequirementHelper.INSTANCE.getCurrentPage() != null)
            {
                RequirementHelper.INSTANCE.getCurrentPage().getViewer().setInput(null);
                RequirementHelper.INSTANCE.getCurrentPage().getViewer();
            }
            if (RequirementHelper.INSTANCE.getUpstreamPage() != null)
            {
                RequirementHelper.INSTANCE.getUpstreamPage().getViewer().setInput(null);
                RequirementHelper.INSTANCE.getUpstreamPage().refreshViewer(true);
            }
            
            RequirementCorePlugin.setCreateDropListener(true);
            setEnabled(isEnabled());
        }
    }

    /**
     * @see org.eclipse.jface.action.Action#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        return RequirementUtils.getRequirementModel(editingDomain) != null;
    }
}
