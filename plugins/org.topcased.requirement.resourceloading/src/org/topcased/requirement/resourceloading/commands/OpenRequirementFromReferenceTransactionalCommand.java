/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Olivier Mélois <a href="mailto:olivier.melois@atos.net">olivier.melois@atos.net</a>"
 * 
 **********************************************************************************************************************/

package org.topcased.requirement.resourceloading.commands;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.topcased.requirement.core.views.current.model.CurrentRequirementReference;
import org.topcased.requirement.resourceloading.views.QuickEditorView;

/**
 * Transactionnal command to open a Current Requirement from a selected reference.
 * 
 * @author omelois
 * 
 */
public class OpenRequirementFromReferenceTransactionalCommand extends AbstractTransactionalCommand
{

    /**
     * References to the requirements that has to be opened. (However, this command should be processed only when 1
     * reference is selected)
     */
    private List<CurrentRequirementReference> currentReferences;

    /**
     * Constructor
     * 
     * @param domain {@link TransactionalEditingDomain}
     * @param label Label of the command
     * @param affectedFiles
     * @param currentReferences of all {@link CurrentRequirementReference} to load
     */
    public OpenRequirementFromReferenceTransactionalCommand(TransactionalEditingDomain domain, String label, List< ? > affectedFiles, List<CurrentRequirementReference> currentReferences)
    {
        super(domain, label, affectedFiles);
        this.currentReferences = currentReferences;
    }

    @Override
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
    {
        // Opening the view
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        String idView = QuickEditorView.ID;
        CurrentRequirementReference currentRequirementReference = currentReferences.get(0);
        String requirementPath = currentRequirementReference.getUri().path();

        try
        {
            ViewPart viewPart = (ViewPart) page.showView(idView, requirementPath, IWorkbenchPage.VIEW_ACTIVATE);
            if (viewPart instanceof QuickEditorView)
            {
                QuickEditorView quick = (QuickEditorView) viewPart;
                quick.setInput(currentRequirementReference);
            }
            // TODO change view name.
            // page.findViewReference(idView, requirementPath).getView(false);
        }
        catch (PartInitException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return CommandResult.newOKCommandResult();
    }
}
