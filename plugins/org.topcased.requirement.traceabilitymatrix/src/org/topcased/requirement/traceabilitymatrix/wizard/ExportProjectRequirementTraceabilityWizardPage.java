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
 *  Cyril MARCHIVE (Atos) cyril.marchive@atos.net 
 *
 *****************************************************************************/
package org.topcased.requirement.traceabilitymatrix.wizard;

import java.net.URL;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.topcased.requirement.traceabilitymatrix.Activator;

public class ExportProjectRequirementTraceabilityWizardPage extends WizardPage
{

    public Text textProject;

    public Text getTextProject()
    {
        return textProject;
    }

    public void setTextProject(Text textProject)
    {
        this.textProject = textProject;
    }

    public Text getTextOutput()
    {
        return textOutput;
    }

    public void setTextOutput(Text textOutput)
    {
        this.textOutput = textOutput;
    }

    public boolean isOpenFile()
    {
        return openFile;
    }

    public void setOpenFile(boolean openFile)
    {
        this.openFile = openFile;
    }

    public Text textOutput;

    public boolean openFile;

    private final IContainer project;

    public ExportProjectRequirementTraceabilityWizardPage(IContainer project)
    {
        super("Project requirement traceability export");
        setTitle("Project requirement traceability export");
        setDescription("Project requirement traceability exported to an excel file");
        // image
        final URL image = Platform.getBundle(Activator.PLUGIN_ID).getResource("icons/bricks.ico");
        setImageDescriptor(ImageDescriptor.createFromURL(image));
        this.project = project;

    }

    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        container.setLayout(new GridLayout(3, false));

        Label lblProject = new Label(container, SWT.NONE);
        lblProject.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblProject.setText("project :");

        textProject = new Text(container, SWT.BORDER);
        textProject.setEditable(false);
        textProject.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Button btnNewButton = new Button(container, SWT.NONE);
        btnNewButton.addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                ContainerSelectionDialog d = new ContainerSelectionDialog(getShell(), project, true, null);
                d.open();
                textProject.setText(d.getResult()[0].toString());
                getContainer().updateButtons();
            }
        });
        btnNewButton.setText("Browse...");

        Label lblOutputPath = new Label(container, SWT.NONE);
        lblOutputPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblOutputPath.setText("output path :");

        textOutput = new Text(container, SWT.BORDER);
        textOutput.setEditable(false);
        textOutput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Button btnBrowse = new Button(container, SWT.NONE);
        btnBrowse.addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                ContainerSelectionDialog d = new ContainerSelectionDialog(getShell(), project, true, null);
                d.open();
                textOutput.setText(d.getResult()[0].toString());
                getContainer().updateButtons();
            }
        });
        btnBrowse.setText("Browse...");

        Button btnOpenFileAfter = new Button(container, SWT.CHECK);
        btnOpenFileAfter.addSelectionListener(new SelectionListener()
        {

            public void widgetSelected(SelectionEvent e)
            {
                openFile = !openFile;

            }

            public void widgetDefaultSelected(SelectionEvent e)
            {
                openFile = !openFile;

            }
        });
        btnOpenFileAfter.setText("Open file after execution");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        if (project != null)
        {
            textProject.setText(project.getFullPath().toString());
            textOutput.setText(project.getFullPath().toString());

        }

    }

}
