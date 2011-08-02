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
 *  Caroline Bourdeu d'Aguerre (ATOS ORIGIN INTEGRATION) caroline.bourdeudaguerre@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.merge.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;
import org.topcased.requirement.merge.process.Merge;

public class MergeRequirementWizard extends Wizard
{

    private MergeRequirementWizardPage page;

    public MergeRequirementWizard()
    {
        init(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection());
    }

    @Override
    public boolean performFinish()
    {
        boolean ok = true;
        try
        {
            getContainer().run(false, false, new IRunnableWithProgress()
            {
                public void run(IProgressMonitor monitor)
                {
                    Merge m = new Merge(page.getInputs(), page.getOutputValue());
                    m.process(monitor);
                }
            });
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
            ok = false;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            ok = false;
        }
        return ok;

    }

    public void init(ISelection selection1)
    {
        setWindowTitle("Merge Requirement Wizard");
        setNeedsProgressMonitor(true);
        if (selection1 instanceof IStructuredSelection)
        {
            IStructuredSelection selection = (IStructuredSelection) selection1;
            List<String> inputs = new LinkedList<String>();
            for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();)
            {
                Object o = iterator.next();
                IFile f = null ;
                if (o instanceof IFile)
                {
                	f = (IFile) o;
                }
                else
                {
                	Object adapter = Platform.getAdapterManager().getAdapter(o, IFile.class);
					if (adapter != null)
                	{
                		f  = (IFile) adapter ;
                	}
                }
                if (f.getLocation().getFileExtension().toLowerCase().endsWith("di"))
                {
                	inputs.add(f.getLocationURI().toString());
                }
            }
            page = new MergeRequirementWizardPage("Match Document", inputs);
            // Create page one
            addPage(page);
        }

    }

    public boolean canFinish()
    {
        return page.isPageComplete();
    }

}
