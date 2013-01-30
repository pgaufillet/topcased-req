/*****************************************************************************
 * Copyright (c) 2008, 2010 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *      Maxime AUDRAIN (CS) : API Changes
 *    
 ******************************************************************************/
package org.topcased.requirement.core.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.topcased.facilities.resources.SharedImageHelper;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.wizards.operation.AbstractRequirementModelOperation;
import org.topcased.requirement.core.wizards.operation.EmptyRequirementModelOperation;

/**
 * 
 * Defines the wizard for creating/updating Requirement Models.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public abstract class AbstractRequirementModelWizard extends Wizard implements INewWizard
{
    /**
     * selection which wizard have been initialized with. This may contain IFiles or Resources
     */
    protected IStructuredSelection selection;

    protected boolean existingRequirementModel = false;

    protected MergeRequirementModelWizardPage pageMerge;

    protected String projectName;

    protected String projectDescription;

    protected boolean toMerge = false;

    protected RequirementWizardPage page;

    /**
     * Constructor
     * 
     * @param theProjectName The name of the existing project
     */
    public AbstractRequirementModelWizard()
    {
        super();
        setDefaultPageImageDescriptor(SharedImageHelper.getTopcasedDialogImageDescriptor());
        setHelpAvailable(false);
        setWindowTitle(Messages.getString("RequirementManager.shell.title")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages()
    {
        Resource alreadyAttachedRequirementResource = null;
        IPath alreadyAttachedRequirementPath = null;
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = RequirementUtils.getSpecificServices(editor);
        if (services != null)
        {
            alreadyAttachedRequirementResource = RequirementUtils.getRequirementModel(services.getEditingDomain(editor));
            if (alreadyAttachedRequirementResource != null)
            {
                IFile requirementFile = RequirementUtils.getFile(alreadyAttachedRequirementResource);
                alreadyAttachedRequirementPath = requirementFile.getFullPath();
            }
        }
        if (toMerge)
        {
            pageMerge = new MergeRequirementModelWizardPage(selection, alreadyAttachedRequirementResource);
            addPage(pageMerge);
        }
        else
        {
            page = new RequirementWizardPage(selection, alreadyAttachedRequirementPath, toMerge);
            addPage(page);
        }
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
            if (page != null)
            {
                // updates fields with values given in the constructor
                page.setProjectFd(projectName != null ? projectName : ""); //$NON-NLS-1$
                page.setProjectDescrFd(projectDescription != null ? projectDescription : ""); //$NON-NLS-1$
                page.getButton().setEnabled(false);
            }
            if (pageMerge != null)
            {
                // updates fields with values given in the constructor
                pageMerge.setProjectFd(projectName != null ? projectName : ""); //$NON-NLS-1$
                pageMerge.setProjectDescrFd(projectDescription != null ? projectDescription : ""); //$NON-NLS-1$
            }
        }
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish()
    {
        Boolean result = false;
        if (page != null && page.isPageComplete())
        {
            projectName = page.getProjectFd();
            projectDescription = page.getProjectDescrFd();
            result = createModelFile();
        }
        if (pageMerge != null && pageMerge.isPageComplete())
        {
            // Save is partial and perform analysis preferences
            pageMerge.savePrefs();
            projectName = pageMerge.getProjectFd();
            projectDescription = pageMerge.getProjectDescrFd();
            result = createModelFile();
        }
        return result;
    }

    /**
     * Initialize the wizard
     * 
     * @param pWorkbench the workbench
     * @param pSelection the selected model file. This should contain IFile objects.
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
    protected boolean createModelFile()
    {
        try
        {
            AbstractRequirementModelOperation operation = null;
            if (page != null && page.getEmptySource())
            {
                // Do the work within an operation.
                operation = new EmptyRequirementModelOperation(page.getTargetModelFile(), page.getDestModelFile());
            }
            else
            {
                // Do the work within an operation.
                operation = getOperation();
            }
            operation.setProjectInformations(projectName, projectDescription);
            new ProgressMonitorDialog(getShell()).run(false, false, operation);
            return true;
        }
        catch (InvocationTargetException exception)
        {
            exception.printStackTrace();
            return false;
        }
        catch (InterruptedException exception)
        {
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Get the operation to perform (new requirement model or merge requirement model This method is intended to be
     * implemented.
     * 
     * @return the AbstractRequirementModelOperation to perform
     * @throws InterruptedException 
     */
    protected abstract AbstractRequirementModelOperation getOperation() throws InterruptedException;
}
