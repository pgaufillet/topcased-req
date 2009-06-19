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
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) tristan.faure@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.generic.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The Class NamingSettingsPage.
 */
public class NamingSettingsPage extends PreferencePage implements IWorkbenchPreferencePage
{

    private Composite parentComposite;

    private Text textForStep;

    private Button choiceForNewAlgo;

    public NamingSettingsPage()
    {
    }

    public NamingSettingsPage(String title)
    {
        super(title);
    }

    public NamingSettingsPage(String title, ImageDescriptor image)
    {
        super(title, image);
    }

    @Override
    protected Control createContents(Composite parent)
    {
        GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        parentComposite = new Composite(parent, SWT.NONE);
        parentComposite.setLayout(new GridLayout());

        Group group = new Group(parentComposite, SWT.NONE);
        group.setText("Configure naming settings for current requirements");
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(layoutData);

        Label label = new Label(group, SWT.NONE);
        label.setText("use new requirement counting system");
        choiceForNewAlgo = new Button(group, SWT.CHECK);
        choiceForNewAlgo.setLayoutData(layoutData);

        Label label2 = new Label(group, SWT.NONE);
        label2.setText("enter the value for incremental number");
        textForStep = new Text(group, SWT.BORDER);
        textForStep.setLayoutData(layoutData);
        setValues();
        return parentComposite;
    }

    private void setValues()
    {
        choiceForNewAlgo.setSelection(PreferencesHelper.getNewAlgo());
        textForStep.setText(String.valueOf(PreferencesHelper.getStep()));
    }

    public void init(IWorkbench workbench)
    {

    }

    @Override
    protected void performApply()
    {
        super.performApply();
    }

    @Override
    public boolean performOk()
    {
        PreferencesHelper.setNewAlgo(choiceForNewAlgo.getSelection());
        PreferencesHelper.setStep(textForStep.getText());
        return super.performOk();
    }

    @Override
    protected void performDefaults()
    {
        setDefaultValues();
    }

    private void setDefaultValues()
    {
        choiceForNewAlgo.setSelection(true);
        textForStep.setText("10");
    }

}
