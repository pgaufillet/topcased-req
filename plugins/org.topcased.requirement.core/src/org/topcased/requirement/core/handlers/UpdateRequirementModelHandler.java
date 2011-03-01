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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.services.RequirementModelSourceProvider;
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

                ISourceProviderService spc = (ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
                RequirementModelSourceProvider myPro = (RequirementModelSourceProvider) spc.getSourceProvider(RequirementModelSourceProvider.IS_IMPACTED);
                RequirementUtils.fireIsImpactedVariableChanged();
                
                if(!myPro.getCurrentState().get(RequirementModelSourceProvider.IS_IMPACTED)) {
                    Dialog msgDialog = new MessageDialog(Display.getCurrent().getActiveShell(), Messages.getString("UpdateMessageDialog.title"), null, Messages.getString("UpdateMessageDialog.message"), MessageDialog.WARNING, new String[] {IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL}, Window.OK);
                    if(msgDialog.open() == Window.OK) {
                        launchWizard(wizard);
                    }
                }
                else {
                    launchWizard(wizard);
                }
                
            }
        }
        RequirementUtils.fireIsImpactedVariableChanged();
        return null;
    }

    private void launchWizard(MergeRequirementModelWizard wizard)
    {
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
        // Wizard help may be set later
        wizardDialog.setHelpAvailable(false);
        wizardDialog.open();
    }
}
