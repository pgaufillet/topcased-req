/*****************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
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

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
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
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class RequirementNamingPreferencePage extends AbstractTopcasedPreferencePage
{
    private Text formatText;

    private Text stepText;

    private Text descriptionText;

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

        // create the top group
        createNamingGroup(mainComposite);

        // create the bottom group
        createCountingGroup(mainComposite);

        loadPreferences();

        return mainComposite;
    }

    /**
     * Creates the naming group (top part).
     * 
     * @param parent The composite parent
     */
    private void createNamingGroup(Composite parent)
    {
        // Naming Group
        final Group mainGroup = new Group(parent, SWT.NONE);
        mainGroup.setLayout(new GridLayout(2, false));
        mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        mainGroup.setText(Messages.getString("RequirementNamingPreferencePage.group.naming")); //$NON-NLS-1$

        // Composite Text format
        final Composite textComposite = new Composite(mainGroup, SWT.NONE);
        final GridLayout textCompoLayout = new GridLayout(2, false);
        textCompoLayout.marginHeight = 0;
        textCompoLayout.marginWidth = 0;
        textComposite.setLayout(textCompoLayout);
        textComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));

        // Text format
        namingFormat = new StringFieldEditor(RequirementNamingConstants.REQUIREMENT_NAMING_FORMAT, Messages.getString("RequirementNamingPreferencePage.6"), textComposite); //$NON-NLS-1$
        namingFormat.setPreferenceStore(getPreferenceStore());
        formatText = namingFormat.getTextControl(textComposite);
        formatText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

        // Select Label
        final Label selectLabel = new Label(mainGroup, SWT.NONE);
        selectLabel.setText(Messages.getString("RequirementNamingPreferencePage.1")); //$NON-NLS-1$
        selectLabel.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));

        // Key words Table
        tableViewer = new Table(mainGroup, SWT.BORDER);
        tableViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        fillTableWithVariables();

        // Add Button
        final GridData addDataLayout = new GridData(SWT.FILL, SWT.NONE, false, false);
        addDataLayout.widthHint = 75;

        final Button addButton = new Button(mainGroup, SWT.PUSH);
        addButton.setText(Messages.getString("RequirementNamingPreferencePage.2")); //$NON-NLS-1$
        addButton.setLayoutData(addDataLayout);
        addButton.addSelectionListener(new AddButtonSelectionListener());
    }

    /**
     * Creates the counting group (bottom part).
     * 
     * @param parent The composite parent
     */
    private void createCountingGroup(Composite parent)
    {
        // Naming Group
        final Group countingGroup = new Group(parent, SWT.NONE);
        countingGroup.setLayout(new GridLayout());
        countingGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        countingGroup.setText(Messages.getString("RequirementNamingPreferencePage.group.naming")); //$NON-NLS-1$

        // Index Step Composite
        final Composite stepComposite = new Composite(countingGroup, SWT.NONE);
        final GridLayout stepCompoLayout = new GridLayout(2, false);
        stepCompoLayout.marginHeight = 0;
        stepCompoLayout.marginWidth = 0;
        stepComposite.setLayout(stepCompoLayout);
        stepComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

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
        final Composite algorithmComposite = new Composite(countingGroup, SWT.NONE);
        final GridLayout algorithmCompoLayout = new GridLayout(2, false);
        algorithmCompoLayout.marginHeight = 0;
        algorithmCompoLayout.marginWidth = 0;
        algorithmComposite.setLayout(algorithmCompoLayout);
        algorithmComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

        // Algorithm combo
        int nbAlgo = RequirementCountingAlgorithmManager.getInstance().getAllAlgorithm().size();
        String[][] map = new String[nbAlgo][2];
        for (int i = 0; i < nbAlgo; i++)
        {
            map[i][0] = RequirementCountingAlgorithmManager.getInstance().getAllAlgorithm().toArray(new String[0])[i];
            map[i][1] = RequirementCountingAlgorithmManager.getInstance().getAllAlgorithm().toArray(new String[0])[i];
        }
        algorithmUsed = new ComboFieldEditor(RequirementNamingConstants.REQUIREMENT_COUNTING_ALGORITHM, Messages.getString("RequirementNamingPreferencePage.5"), map, algorithmComposite);//$NON-NLS-1$
        algorithmUsed.setPropertyChangeListener(new ComboPropertyChangeListener());
        algorithmUsed.setPreferenceStore(getPreferenceStore());
        algorithmUsed.fillIntoGrid(algorithmComposite, 2);

        // description Group
        final Group descriptionGroup = new Group(countingGroup, SWT.NONE);
        descriptionGroup.setLayout(new FillLayout());
        descriptionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        descriptionGroup.setText(Messages.getString("RequirementNamingPreferencePage.7")); //$NON-NLS-1$

        // description Text
        descriptionText = new Text(descriptionGroup, SWT.READ_ONLY | SWT.WRAP);
        descriptionText.setEnabled(false);
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
    @Override
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
     * Internal listener for adding variable inside the Naming Format text field.
     */
    private class AddButtonSelectionListener extends SelectionAdapter
    {
        @Override
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
     * Internal listener to control value fill into the Step Index text field
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

    /**
     * Internal listener for updating description according to the algorithm chosen in the combo.
     */
    private class ComboPropertyChangeListener implements IPropertyChangeListener
    {

        /**
         * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent event)
        {
            String description = RequirementCountingAlgorithmManager.getInstance().getAlgorithmDescription(event.getNewValue().toString());
            if (description != null)
            {
                descriptionText.setText(description);
            }
            else
            {
                descriptionText.setText(""); //$NON-NLS-1$
            }
        }
    }
}
