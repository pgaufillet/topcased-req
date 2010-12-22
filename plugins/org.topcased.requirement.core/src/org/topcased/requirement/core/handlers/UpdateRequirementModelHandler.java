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
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.wizards.MergeRequirementModelWizard;

/**
 * Handler to deals with the update action in the upstream view
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class UpdateRequirementModelHandler extends AbstractHandler
{
    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        Resource targetModel = null;
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = RequirementUtils.getSpecificServices(editor);
        if (editor != null && services != null)
        {
            EditingDomain domain = services.getEditingDomain(editor);
            // Get the policy and the linked target model
            IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(domain);
            if (policy != null)
            {
                targetModel = policy.getLinkedTargetModel(domain.getResourceSet());
            }
            else
            {
                String extension = domain.getResourceSet().getResources().get(0).getURI().fileExtension();
                String msg = NLS.bind(Messages.getString("ModelAttachmentPolicyManager.0"), extension);
                RequirementCorePlugin.log(msg, Status.ERROR, null);//$NON-NLS-1$
            }
            if (targetModel != null)
            {
                // creation of the merge wizard
                Resource requirement = RequirementUtils.getRequirementModel(domain);
                RequirementProject requirementProject = (RequirementProject) requirement.getContents().get(0);
                MergeRequirementModelWizard wizard = new MergeRequirementModelWizard(requirementProject.getIdentifier(), requirementProject.getShortDescription());

                IFile targetFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(targetModel.getURI().toPlatformString(true)));
                wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(targetFile));

                // launch the wizard allowing to perform the operations
                WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard)
                {
                    protected void configureShell(Shell newShell)
                    {
                        super.configureShell(newShell);
                        newShell.setMinimumSize(530, 580);
                        newShell.setSize(530, 580);
                    }
                };
                wizardDialog.open();
            }
        }
        RequirementUtils.fireIsImpactedVariableChanged();
        return null;
    }
}
