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

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
import org.topcased.requirement.core.extensions.IRequirementIdentifierVariables;
import org.topcased.requirement.core.extensions.RequirementCountingAlgorithmManager;
import org.topcased.requirement.core.extensions.RequirementIdentifierVariablesManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;

/**
 * Manages the preference store for the Requirements naming's format
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class RequirementNamingPreferencePage extends AbstractTopcasedPreferencePage implements IWorkbenchPreferencePage
{
    private Text formatText;

    private Text stepText;

    private Table tableViewer;

    private StringFieldEditor namingFormat;

    private IntegerFieldEditor indexStep;

    private ComboFieldEditor algorithmUsed;

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
        mainGroup.setText(Messages.getString("RequirementNamingPreferencePage.0")); //$NON-NLS-1$

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
        namingFormat = new StringFieldEditor(RequirementNamingConstants.REQUIREMENT_NAMING_FORMAT, Messages.getString("RequirementNamingPreferencePage.6"), textComposite); //$NON-NLS-1$
        namingFormat.setPreferenceStore(getPreferenceStore());
        formatText = namingFormat.getTextControl(textComposite);
        formatText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

        // Select Label
        GridData selectLayoutData = new GridData(SWT.FILL, SWT.NONE, true, false);
        selectLayoutData.horizontalSpan = 2;
        final Label selectLabel = new Label(mainGroup, SWT.NONE);
        selectLabel.setText(Messages.getString("RequirementNamingPreferencePage.1")); //$NON-NLS-1$
        selectLabel.setLayoutData(selectLayoutData);

        // Key words Table
        tableViewer = new Table(mainGroup, SWT.BORDER);
        tableViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        fillTableWithVariables();

        // Add Button
        final GridData addDataLayout = new GridData(SWT.FILL, SWT.NONE, false, false);
        addDataLayout.widthHint = 75;

        final Button addButton = new Button(mainGroup, SWT.PUSH);
        addButton.setText(Messages.getString("RequirementNamingPreferencePage.2")); //$NON-NLS-1$
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

        // Index Step Field
        final GridData layoutData = new GridData(SWT.NONE, SWT.NONE, true, false);
        layoutData.widthHint = 25;

        indexStep = new IntegerFieldEditor(RequirementNamingConstants.REQUIREMENT_STEP_INDEX, Messages.getString("RequirementNamingPreferencePage.3"), stepComposite); //$NON-NLS-1$
        indexStep.setPreferenceStore(getPreferenceStore());
        stepText = indexStep.getTextControl(stepComposite);
        stepText.setTextLimit(4);
        stepText.addModifyListener(new StepTextModifyListener());
        formatText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
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

        // Algorithm combo
        int nbAlgo = RequirementCountingAlgorithmManager.getInstance().getAllAlgorithm().size();
        String[][] map = new String[nbAlgo][2];
        for (int i = 0; i < nbAlgo; i++)
        {
            map[i][0] = RequirementCountingAlgorithmManager.getInstance().getAllAlgorithm().toArray(new String[0])[i];
            map[i][1] = RequirementCountingAlgorithmManager.getInstance().getAllAlgorithm().toArray(new String[0])[i];
        }
        algorithmUsed = new ComboFieldEditor(RequirementNamingConstants.REQUIREMENT_COUNTING_ALGORITHM, Messages.getString("RequirementNamingPreferencePage.5"), map, algorithmComposite);//$NON-NLS-1$
        algorithmUsed.setPreferenceStore(getPreferenceStore());
        algorithmUsed.fillIntoGrid(algorithmComposite, 2);

        loadPreferences();

        return mainComposite;
    }

    /**
     * Loads the preferences
     */
    private void loadPreferences()
    {
        namingFormat.load();
        indexStep.load();
        algorithmUsed.load();
    }

    /**
     * Stores the preferences
     */
    private void storePreferences()
    {
        namingFormat.store();
        indexStep.store();
        algorithmUsed.store();
    }

    /**
     * Loads the default preferences
     */
    private void loadDefaultPreferences()
    {
        namingFormat.loadDefault();
        indexStep.loadDefault();
        algorithmUsed.loadDefault();
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk()
    {
        storePreferences();
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
    private void fillTableWithVariables()
    {
        for (IRequirementIdentifierVariables vars : RequirementIdentifierVariablesManager.getInstance().getIdentifierVariables())
        {
            for (String key : vars.getVariables())
            {
                TableItem item = new TableItem(tableViewer, SWT.NONE);
                item.setText(key);
            }
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
                setErrorMessage(Messages.getString("RequirementNamingPreferencePage.4")); //$NON-NLS-1$
                setValid(false);
            }
        }
    }
}
