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
package org.topcased.requirement.core.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.topcased.facilities.resources.SharedImageHelper;
import org.topcased.requirement.core.wizards.operation.AbstractModelCreationOperation;
import org.topcased.requirement.core.wizards.operation.EmptyRequirementModelCreationOperation;
import org.topcased.requirement.core.wizards.operation.RequirementModelCreationOperation;

/**
 * 
 * Defines the wizard for creating/updating Requirement Models.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class NewRequirementModel extends Wizard implements INewWizard
{
    private IStructuredSelection selection;

    private RequirementWizardPage page;

    private boolean existingRequirementModel = false;

    private String projectName;

    private String projectDescription;

    /**
     * Constructor
     */
    public NewRequirementModel()
    {
        super();
        setDefaultPageImageDescriptor(SharedImageHelper.getTopcasedDialogImageDescriptor());
    }

    /**
     * Constructor
     * 
     * @param theProjectName The name of the existing project
     * @param theProjectDescription The short description of the existing project
     */
    public NewRequirementModel(String theProjectName, String theProjectDescription)
    {
        this();
        existingRequirementModel = true;
        projectName = theProjectName;
        projectDescription = theProjectDescription;
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages()
    {
        page = new RequirementWizardPage(selection);
        addPage(page);
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#createPageControls(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPageControls(Composite pageContainer)
    {
        super.createPageControls(pageContainer);
        if (existingRequirementModel)
        {
            // updates fields with values given in the constructor
            page.setProjectFd(projectName != null ? projectName : ""); //$NON-NLS-1$
            page.setProjectDescrFd(projectDescription != null ? projectDescription : ""); //$NON-NLS-1$
            page.getButton().setEnabled(false);
        }
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish()
    {
        Boolean result = false;
        if (page.isPageComplete())
        {
            projectName = page.getProjectFd();
            projectDescription = page.getProjectDescrFd();
            result = createModelFile();
        }
        return result;
    }

    /**
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     *      org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench pWorkbench, IStructuredSelection pSelection)
    {
        this.selection = pSelection;
    }

    /**
     * Creation of the requirement model
     * 
     * @return
     */
    private boolean createModelFile()
    {
        try
        {
            AbstractModelCreationOperation operation = null;
            if (page.getEmptySource())
            {
                // Do the work within an operation.
                operation = new EmptyRequirementModelCreationOperation(page.getModelFile(), page.getDestModelFile());
            }
            else
            {
                // Do the work within an operation.
                operation = new RequirementModelCreationOperation(page.getModelFile(), page.getSourceModelFile(), page.getDestModelFile());
            }
            operation.setProjectInformations(projectName, projectDescription);
            new ProgressMonitorDialog(getShell()).run(false, false, operation);
            return true;
        }
        catch (InvocationTargetException exception)
        {
            return false;
        }
        catch (InterruptedException exception)
        {
            return false;
        }
    }
}
