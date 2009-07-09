/*****************************************************************************
 * Copyright (c) 2009 ATOS ORIGIN INTEGRATION.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Amine Bouchikhi (ATOS ORIGIN INTEGRATION) amine.bouchikhi@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.generic.filter.popup.actions;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.topcased.requirement.generic.filter.ui.RequirementFilterWizard;

/**
 * The Class RequirementsFilter.
 */
public class RequirementsFilter implements IObjectActionDelegate
{

    /** The shell. */
    private Shell shell;

    /** The current selection. */
    private StructuredSelection currentSelection;

    /**
     * Constructor for Action1.
     */
    public RequirementsFilter()
    {
        super();
    }

    /**
     * Sets the active part.
     * 
     * @param action the action
     * @param targetPart the target part
     * 
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart)
    {
        shell = targetPart.getSite().getShell();
    }

    /**
     * Run.
     * 
     * @param action the action
     * 
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction action)
    {
        Collection<IPath> ipathes = new LinkedList<IPath>();
        if (currentSelection == null)
        {
            return;
        }
        for (Iterator< ? > i = currentSelection.iterator(); i.hasNext();)
        {
            Object tmp = i.next();
            if (tmp instanceof IFile)
            {
                IFile file = (IFile) tmp;
                ipathes.add(file.getFullPath());
            }
        }
        WizardDialog wd = new WizardDialog(shell, new RequirementFilterWizard(ipathes.toArray(new IPath[] {})));
        wd.open();
    }

    /**
     * Gets the requirements project.
     * 
     * @param requirementsPath the requirements path
     * 
     * @return the requirements project
     */
    public EObject getRequirementsProject(String requirementsPath)
    {
        EObject requirements = null;
        if (requirementsPath != null)
        {

            URI uri = URI.createURI(requirementsPath);

            ResourceSet set = new ResourceSetImpl();

            Resource requirementsResources = set.getResource(uri, true);

            requirements = requirementsResources.getContents().get(0);
        }

        return requirements;

    }

    /**
     * Selection changed.
     * 
     * @param action the action
     * @param selection the selection
     * 
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        if (selection instanceof StructuredSelection)
        {
            currentSelection = (StructuredSelection) selection;

        }
    }

}
