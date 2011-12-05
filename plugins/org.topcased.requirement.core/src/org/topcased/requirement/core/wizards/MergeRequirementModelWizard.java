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

package org.topcased.requirement.core.wizards;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.wizards.operation.AbstractRequirementModelOperation;
import org.topcased.requirement.core.wizards.operation.MergeRequirementModelOperation;

import ttm.Document;

/**
 * 
 * Defines the wizard for updating Requirement Models.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class MergeRequirementModelWizard extends AbstractRequirementModelWizard
{

    /**
     * Basic Constructor
     * 
     */
    public MergeRequirementModelWizard()
    {
        super();
        toMerge = true;
    }

    /**
     * Constructor
     * 
     * @param theProjectName The name of the existing project
     * @param theProjectDescription The short description of the existing project
     */
    public MergeRequirementModelWizard(String theProjectName, String theProjectDescription)
    {
        this();
        existingRequirementModel = true;
        projectName = theProjectName;
        projectDescription = theProjectDescription;
    }

    /**
     * @throws InterruptedException 
     * @see org.topcased.requirement.core.wizards.AbstractRequirementModelWizard#getOperation()
     */
    @Override
    protected AbstractRequirementModelOperation getOperation() throws InterruptedException
    {
        String messages = null;
        Map<Document, Document> documentsToMerge = pageMerge.getDocumentsToMerge();
        for (Entry<Document, Document> doc : documentsToMerge.entrySet())
        {
            if (doc.getValue().eContents().isEmpty())
            {
                if (messages == null)
                {
                    messages = "\n"+doc.getKey().getIdent(); //$NON-NLS-1$
                } 
                else
                {
                    messages+="\n"+doc.getKey().getIdent(); //$NON-NLS-1$
                }
            }
        }
        
        if (messages != null)
        {
            MessageDialog dialog = new MessageDialog(getShell(), Messages.getString("EmptyRequirementTitle"), null, Messages.getString("EmptyRequirementMsg")+"\n" + messages+"\n\n"+Messages.getString("ContinueMsg"), MessageDialog.WARNING, new String[] {IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL}, Window.OK|Window.CANCEL);   //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
            if (dialog.open() == Window.CANCEL)
            {
                throw new InterruptedException();
            }
        }
        
        return new MergeRequirementModelOperation(pageMerge.getTargetModelFile(), pageMerge.getSourceModelFile(), pageMerge.getDestModelFile(),pageMerge.getDocumentsToMerge(), pageMerge.isPartialImport(), pageMerge.isImpactAnalysis());
    }
}
