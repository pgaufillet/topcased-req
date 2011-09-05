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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class MergeRequirementWizardPage extends WizardPage
{

    private ScrolledForm form;

    private Composite section;

    private FormToolkit toolkit;

    // private List<String> inputs;

    private String outputValue = "";

    private Map<String, Boolean> inputs;

    /**
     * Instantiates a new merge requirement wizard page.
     * 
     * @param pageName the page name
     * @param files the files
     */
    protected MergeRequirementWizardPage(String pageName, List<String> files)
    {
        super(pageName);
        inputs = new HashMap<String, Boolean>();
        for (String file : files)
        {
            inputs.put(file, true);
        }
    }

    public void createControl(Composite parent)
    {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new FillLayout());
        // setImageDescriptor(workbench.getSharedImages().getImageDescriptor(Wizard.DEFAULT_IMAGE));
        this.setDescription("This wizard merge all the requirement models associated to the selected diagrams");
        toolkit = new FormToolkit(composite.getDisplay());
        form = toolkit.createScrolledForm(composite);
        // create the base form
        form.setText("Requirement Merge");
        toolkit.decorateFormHeading(form.getForm());
        FillLayout layout = new FillLayout();
        layout.spacing = 5;
        layout.marginHeight = 5;
        layout.marginWidth = 5;
        form.getBody().setLayout(layout);
        createSection();
        setControl(composite);

    }

    /**
     * Creates the section.
     */
    private void createSection()
    {
        section = createSection(form, "Select models", 3);
        createRowForInputs(section);
        createRowForOutput(section);
    }

    private void createRowForInputs(Composite compo)
    {
        Set<String> keys = inputs.keySet();
        Iterator<String> it = keys.iterator();

        // add header
        Label labelHeader = toolkit.createLabel(compo, "", SWT.FILL | SWT.NONE);
        GridData dataHeader = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        labelHeader.setLayoutData(dataHeader);
        toolkit.createLabel(compo, "is main model ", SWT.FILL | SWT.NONE);

        // for each inputs
        while (it.hasNext())
        {
            String file = it.next();
            Label label = toolkit.createLabel(compo, file, SWT.FILL | SWT.NONE);
            GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
            label.setLayoutData(data);
            Button b = toolkit.createButton(compo, "", SWT.CHECK);
            GridData dataButton = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
            b.setLayoutData(dataButton);
            b.setToolTipText("Tick if the model is not a sub model obtain with a control action");
            b.addSelectionListener(new MySelectionListener(file));
        }
    }

    private void createRowForOutput(Composite compo)
    {
        toolkit.createLabel(compo, "Output Requirement File");
        final Text output = toolkit.createText(compo, outputValue, SWT.BORDER);
        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        output.setLayoutData(data);
        output.addModifyListener(new ModifyListener()
        {

            public void modifyText(ModifyEvent e)
            {
                outputValue = output.getText();
                getWizard().getContainer().updateMessage();
                getWizard().getContainer().updateButtons();
            }
        });
        Button b = toolkit.createButton(compo, "Browse", SWT.PUSH);
        GridData dataButton = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        b.setLayoutData(dataButton);
        b.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                ResourceDialog dialog = new ResourceDialog(getShell(), "Select output model", SWT.SAVE | SWT.SINGLE);
                if (dialog.open() == ResourceDialog.OK)
                {
                    output.setText(dialog.getURIText());
                }
            }
        });
    }

    private Composite createSection(ScrolledForm mform, String title, int numColumns)
    {
        Section section = toolkit.createSection(mform.getBody(), Section.TITLE_BAR | Section.EXPANDED);
        section.setText(title);
        Composite client = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 1;
        layout.marginHeight = 1;
        layout.numColumns = numColumns;
        layout.makeColumnsEqualWidth = false;
        client.setLayout(layout);
        section.setClient(client);
        return client;
    }

    @Override
    public boolean isPageComplete()
    {
        boolean result = true;
        StringBuffer error = new StringBuffer("");

        if (outputValue.length() == 0 || !outputValue.endsWith(".requirement"))
        {
            result = false;
            error.append("select an output requirement model (.requirement)\n");
        }

        if (result)
        {
            setErrorMessage(null);
        }
        else
        {
            setErrorMessage(error.toString());
        }
        return result;

    }

    public String getOutputValue()
    {
        return outputValue;
    }

    public Map<String, Boolean> getInputs()
    {
        return inputs;
    }

    public class MySelectionListener implements SelectionListener
    {

        private final String file;

        public MySelectionListener(String file)
        {
            this.file = file;
        }

        public void widgetSelected(SelectionEvent e)
        {
            // the boolean is invert
            inputs.put(file, !((Button) e.getSource()).getSelection());
        }

        public void widgetDefaultSelected(SelectionEvent e)
        {
        }
    }
}
