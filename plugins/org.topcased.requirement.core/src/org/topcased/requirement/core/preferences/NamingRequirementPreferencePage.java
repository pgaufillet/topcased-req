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
 *      Maxime AUDRAIN (CS) <maxime.audrain@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.preferences;

import java.util.List;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.topcased.facilities.preferences.AbstractTopcasedPreferencePage;
import org.topcased.requirement.core.Messages;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.extensions.RequirementCountingAlgorithmManager;

/**
 * Manages the preference store for the Requirements naming's format
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class NamingRequirementPreferencePage extends AbstractTopcasedPreferencePage implements IWorkbenchPreferencePage
{
    private Text formatText;

    private Text stepText;

    private Table tableViewer;

    private StringFieldEditor formatRequirement;

    private Combo algorithmCombo;

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent)
    {
        // Main Composite
        final Composite mainComposite = new Composite(parent, SWT.NONE);
        final GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        mainComposite.setLayout(layout);

        // Main Group
        final Group mainGroup = new Group(mainComposite, SWT.NONE);
        mainGroup.setLayout(new GridLayout(2, false));
        mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        mainGroup.setText(Messages.getString("NamingRequirementPreferencePage.0")); //$NON-NLS-1$

        // Composite Text format
        final Composite textComposite = new Composite(mainGroup, SWT.NONE);
        final GridLayout textCompoLayout = new GridLayout(2, false);
        textCompoLayout.marginHeight = 0;
        textCompoLayout.marginWidth = 0;
        textComposite.setLayout(textCompoLayout);
        final GridData textLayoutData = new GridData(SWT.FILL, SWT.NONE, true, false);
        textLayoutData.horizontalSpan = 2;
        textComposite.setLayoutData(textLayoutData);

        // Text format
        formatRequirement = new StringFieldEditor(NamingRequirementPreferenceHelper.NAMING_FORMAT_REQUIREMENT_STORE, "", textComposite); //$NON-NLS-1$
        formatRequirement.setPreferenceStore(getPreferenceStore());
        formatRequirement.setLabelText(Messages.getString("NamingRequirementPreferencePage.6")); //$NON-NLS-1$
        formatText = formatRequirement.getTextControl(textComposite);
        formatText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

        // Select Label
        GridData selectLayoutData = new GridData(SWT.FILL, SWT.NONE, true, false);
        selectLayoutData.horizontalSpan = 2;
        final Label selectLabel = new Label(mainGroup, SWT.NONE);
        selectLabel.setText(Messages.getString("NamingRequirementPreferencePage.1")); //$NON-NLS-1$
        selectLabel.setLayoutData(selectLayoutData);

        // Key words Table
        tableViewer = new Table(mainGroup, SWT.BORDER);
        setTableData(NamingRequirementPreferenceHelper.KEY_WORDS, tableViewer);
        tableViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        // Add Button
        final GridData addDataLayout = new GridData(SWT.FILL, SWT.NONE, false, false);
        addDataLayout.widthHint = 75;

        final Button addButton = new Button(mainGroup, SWT.PUSH);
        addButton.setText(Messages.getString("NamingRequirementPreferencePage.2")); //$NON-NLS-1$
        addButton.setLayoutData(addDataLayout);
        addButton.addSelectionListener(new AddButtonSelectionListener());

        // Index Step Composite
        final Composite stepComposite = new Composite(mainGroup, SWT.NONE);
        final GridLayout stepCompoLayout = new GridLayout(2, false);
        final GridData stepLayoutData = new GridData(SWT.FILL, SWT.NONE, true, false);
        stepLayoutData.horizontalSpan = 2;
        stepCompoLayout.marginHeight = 0;
        stepCompoLayout.marginWidth = 0;
        stepComposite.setLayout(stepCompoLayout);
        stepComposite.setLayoutData(stepLayoutData);

        // Index Step label
        Label stepLabel = new Label(stepComposite, SWT.NONE);
        stepLabel.setText(Messages.getString("NamingRequirementPreferencePage.3")); //$NON-NLS-1$

        // Index Step Field
        GridData layoutData = new GridData(SWT.NONE, SWT.NONE, true, false);
        stepText = new Text(stepComposite, SWT.BORDER);
        stepText.setTextLimit(4);
        layoutData.widthHint = 25;
        stepText.addModifyListener(new StepTextModifyListener());
        stepText.setLayoutData(layoutData);

        // Algorithm Composite
        final Composite algorithmComposite = new Composite(mainGroup, SWT.NONE);
        final GridLayout algorithmCompoLayout = new GridLayout(2, false);
        final GridData gd = new GridData(SWT.FILL, SWT.NONE, true, false);
        gd.horizontalSpan = 2;
        algorithmCompoLayout.marginHeight = 0;
        algorithmCompoLayout.marginWidth = 0;
        algorithmComposite.setLayout(algorithmCompoLayout);
        algorithmComposite.setLayoutData(gd);

        // Algorithm label
        Label algoritmLabel = new Label(algorithmComposite, SWT.NONE);
        algoritmLabel.setText(Messages.getString("NamingRequirementPreferencePage.5")); //$NON-NLS-1$

        // Algorithm combo
        algorithmCombo = new Combo(algorithmComposite, SWT.NULL);
        algorithmCombo.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        for (String key : RequirementCountingAlgorithmManager.getInstance().getAllAlgorithm())
        {
            algorithmCombo.add(key);
        }

        loadPreferences();

        return mainComposite;
    }

    /**
     * Loads the preferences
     */
    private void loadPreferences()
    {
        formatRequirement.load();
        stepText.setText(String.valueOf(NamingRequirementPreferenceHelper.getRequirementStep()));
        algorithmCombo.select(0);
    }

    /**
     * Stores the preferences
     */
    private void storePreferences()
    {
        formatRequirement.store();
    }

    /**
     * Loads the default preferences
     */
    private void loadDefaultPreferences()
    {
        formatRequirement.loadDefault();
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk()
    {
        storePreferences();
        NamingRequirementPreferenceHelper.setRequirementStep(stepText.getText());
        NamingRequirementPreferenceHelper.setCurrentAlgorithm(algorithmCombo.getItem(algorithmCombo.getSelectionIndex()));
        return super.performOk();
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults()
    {
        loadDefaultPreferences();
        super.performDefaults();
    }

    /**
     * @see org.topcased.facilities.preferences.AbstractTopcasedPreferencePage#getBundleId()
     */
    protected String getBundleId()
    {
        return RequirementCorePlugin.getId();
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench)
    {

    }

    /**
     * Set the contents of the table
     * 
     * @param data list of strings
     * @param table the parent table
     */
    private void setTableData(List<String> data, Table table)
    {
        for (String element : data)
        {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(element);
        }
    }

    /**
     * Listener for the Add Button
     * 
     */
    private class AddButtonSelectionListener extends SelectionAdapter
    {
        public void widgetSelected(SelectionEvent e)
        {
            String toAdd = ""; //$NON-NLS-1$
            TableItem[] selections = tableViewer.getSelection();
            for (int i = 0; i < selections.length; i++)
            {
                toAdd += selections[i].getText();
            }
            String currValue = formatText.getText();
            int pos = formatText.getCaretPosition();
            formatText.setText(currValue.substring(0, pos) + toAdd + currValue.substring(pos, currValue.length()));
        }
    }

    /**
     * Listener for the Step Text field
     * 
     */
    private class StepTextModifyListener implements ModifyListener
    {
        public void modifyText(ModifyEvent e)
        {
            try
            {
                Integer.parseInt("".equals(stepText.getText()) ? "0" : stepText.getText()); //$NON-NLS-1$ //$NON-NLS-2$
                setErrorMessage(null);
                setValid(true);
            }
            catch (NumberFormatException e1)
            {
                setErrorMessage(Messages.getString("NamingRequirementPreferencePage.4")); //$NON-NLS-1$
                setValid(false);
            }
        }
    }
}
