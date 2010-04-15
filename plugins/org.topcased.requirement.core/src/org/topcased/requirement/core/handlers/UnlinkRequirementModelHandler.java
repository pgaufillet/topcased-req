/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/
package org.topcased.requirement.core.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.dialogs.UnlinkDialog;
import org.topcased.requirement.core.extensions.DefaultAttachmentPolicy;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Handler to deal with the unlink action in the upstream view
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class UnlinkRequirementModelHandler extends AbstractHandler
{
    /** result of the unlink dialog **/
    private int dialogResult;

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        boolean deleteRequirementModel = false;
        Modeler modeler = Utils.getCurrentModeler();

        if (modeler != null)
        {
            // launch the unlink dialog
            UnlinkDialog dialog = new UnlinkDialog(Display.getCurrent().getActiveShell(), Messages.getString("UnlinkRequirementModelAction.0"), Messages.getString("UnlinkRequirementModelAction.1"));//$NON-NLS-1$
            int dialogResult = dialog.open();

            // If "delete file" is checked
            if (dialogResult == 2)
            {
                deleteRequirementModel = true;
            }

            // In OK cases
            if (dialogResult == 0 || dialogResult == 2)
            {
                IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(modeler.getEditingDomain());

                if (policy != null)
                {
                    policy.unlinkRequirementModel(policy.getLinkedTargetModel(modeler.getEditingDomain().getResourceSet()), RequirementUtils.getRequirementModel(modeler.getEditingDomain()),
                            deleteRequirementModel);
                }
                else
                {
                    DefaultAttachmentPolicy.getInstance().unlinkRequirementModel(Utils.getCurrentModeler().getResourceSet().getResources().get(0),
                            RequirementUtils.getRequirementModel(modeler.getEditingDomain()), deleteRequirementModel);
                }

                // the content of each page (Upstream & Current) is updated
                if (RequirementHelper.INSTANCE.getCurrentPage() != null)
                {
                    RequirementHelper.INSTANCE.getCurrentPage().getViewer().setInput(null);
                    RequirementHelper.INSTANCE.getCurrentPage().refreshViewer(true);
                }
                if (RequirementHelper.INSTANCE.getUpstreamPage() != null)
                {
                    RequirementHelper.INSTANCE.getUpstreamPage().getViewer().setInput(null);
                    RequirementHelper.INSTANCE.getUpstreamPage().refreshViewer(true);
                }
            }
        }
        return null;
    }

    /**
     * @return dialogResult
     */
    public int getDialogResult()
    {
        return dialogResult;
    }
}
