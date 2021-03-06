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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.wizards.NewRequirementModelWizard;

/**
 * Handler to link requirement models to the current modeler. Careful: this execution cannot merge requirement models,
 * only create new models
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class LinkRequirementModelHandler extends AbstractHandler
{

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        NewRequirementModelWizard wizard = null;
        IEditorPart part = HandlerUtil.getActiveEditor(event);
        IEditorServices services = RequirementUtils.getSpecificServices(part);
        if (services != null)
        {
            Resource requirementResource = services.getRequirementsResource(part);

            if (requirementResource != null && !requirementResource.getContents().isEmpty())
            {
                RequirementProject requirementProject = (RequirementProject) requirementResource.getContents().get(0);
                // Create a new requirement wizard with already attached requirement model informations
                wizard = new NewRequirementModelWizard(requirementProject.getIdentifier(), requirementProject.getShortDescription());
            }
            else
            {
                // Create a new requirement wizard with nothing to display in the dialog
                wizard = new NewRequirementModelWizard();
            }

            StructuredSelection selection = null;
            if (part.getEditorInput() instanceof IFileEditorInput)
            {
                IFile currentFile = ((IFileEditorInput) part.getEditorInput()).getFile();
                selection = new StructuredSelection(currentFile);
            }
            // if (Modeler.getCurrentIFile() != null)
            // {
            // selection = new StructuredSelection(Modeler.getCurrentIFile());
            // }
            wizard.init(PlatformUI.getWorkbench(), selection);

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
        return null;
    }

}
