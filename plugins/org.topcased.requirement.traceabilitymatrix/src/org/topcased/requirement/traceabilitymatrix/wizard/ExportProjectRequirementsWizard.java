/*****************************************************************************
 * Copyright (c) 2012 Atos.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Laurent DEVERNAY (Atos) laurent.devernay@tos.net - 
 *
 *****************************************************************************/
package org.topcased.requirement.traceabilitymatrix.wizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.topcased.requirement.traceabilitymatrix.handlers.ExportProjectRequirementTraceability;

public class ExportProjectRequirementsWizard extends Wizard implements IExportWizard
{

    private IContainer container;

    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        if (selection.getFirstElement() instanceof IContainer)
        {
            container = (IContainer) selection.getFirstElement();
        }
        setNeedsProgressMonitor(true);
        setWindowTitle("Project requirement traceability export");
    }

    @Override
    public boolean canFinish()
    {
        ExportProjectRequirementTraceabilityWizardPage page = (ExportProjectRequirementTraceabilityWizardPage) getContainer().getCurrentPage();
        return !"".equals(page.getTextOutput().getText()) && !"".equals(page.getTextProject().getText());
    }

    @Override
    public void addPages()
    {
        super.addPages();
        addPage(new ExportProjectRequirementTraceabilityWizardPage(container));
    }

    @Override
    public boolean performFinish()
    {
        ExportProjectRequirementTraceabilityWizardPage page = (ExportProjectRequirementTraceabilityWizardPage) getContainer().getCurrentPage();
        final String output = page.getTextOutput().getText();
        final String project = page.getTextProject().getText();
        final Boolean open = page.isOpenFile();
        Job j = new Job("Export traceability : " + project)
        {
            @Override
            protected IStatus run(IProgressMonitor arg0)
            {
                if (!"".equals(output) && !"".equals(project))
                {
                    new ExportProjectRequirementTraceability(project, output, open).run();
                }
                return Status.OK_STATUS;
            }
        };
        j.setUser(true);
        j.schedule();
        return true;
    }

}
