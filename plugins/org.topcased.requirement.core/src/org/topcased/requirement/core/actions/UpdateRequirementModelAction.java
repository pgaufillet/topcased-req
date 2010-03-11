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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.utils.DefaultAttachmentPolicy;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.wizards.NewRequirementModel;

/**
 * This action allows to add/remove/update documents and/or requirements for a model.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class UpdateRequirementModelAction extends Action
{
    private EditingDomain editingDomain;

    /**
     * Default contructor
     */
    public UpdateRequirementModelAction(EditingDomain domain)
    {
        super("Update", IAction.AS_PUSH_BUTTON);
        setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/import.gif"));
        editingDomain = domain;
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run()
    {
        Resource targetModel = null;
        
        Resource requirement = RequirementUtils.getRequirementModel(editingDomain);
        RequirementProject requirementProject = (RequirementProject) requirement.getContents().get(0);
        NewRequirementModel wizard = new NewRequirementModel(requirementProject.getIdentifier(), requirementProject.getShortDescription());
        IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(editingDomain);
        if (policy != null)
        {
            targetModel = policy.getLinkedTargetModel(editingDomain.getResourceSet());
        }
        else
        {
            targetModel = DefaultAttachmentPolicy.getInstance().getLinkedTargetModel(editingDomain.getResourceSet());
        }
        if (targetModel != null)
        {
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

    /**
     * @see org.eclipse.jface.action.Action#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        Resource requirement = RequirementUtils.getRequirementModel(editingDomain);
        if (requirement != null)
        {
            Collection<EObject> allRequirement = RequirementUtils.getAllObjects(requirement, CurrentRequirement.class);
            // checks that all CurrentRequirement are marked as not impacted.
            for (EObject aReq : allRequirement)
            {
                if (aReq instanceof CurrentRequirement && ((CurrentRequirement) aReq).isImpacted())
                {
                    // action must be disabled.
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
