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
 *    
 ******************************************************************************/
package org.topcased.requirement.core.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.topcased.facilities.preferences.AbstractTopcasedPreferencePage;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.Messages;

/**
 * Manages the preference store for the Requirements naming's format
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public class NamingRequirementPreferencePage extends AbstractTopcasedPreferencePage implements IWorkbenchPreferencePage
{
    private Text formatText;

    private CheckboxTableViewer tableViewer;

    private StringFieldEditor formatRequirement;

    private BooleanFieldEditor checkNumberDocument;

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent)
    {
        // Main Composite
        final Composite mainComposite = new Composite(parent, SWT.NONE);
        final GridLayout mainLayout = new GridLayout();
        mainLayout.marginHeight = 0;
        mainLayout.marginWidth = 0;
        mainComposite.setLayout(mainLayout);

        // Main Group
        final Group mainGroup = new Group(mainComposite, SWT.NONE);
        GridData mainGroupGridData = new GridData(SWT.FILL, SWT.NONE, true, true);
        mainGroup.setLayoutData(mainGroupGridData);
        mainGroup.setLayout(new GridLayout(2, false));
        mainGroup.setText(Messages.getString("NamingRequirementPreferencePage.0")); //$NON-NLS-1$

        // Composite format
        final Composite formatComposite = new Composite(mainGroup, SWT.NONE);
        final GridLayout formatLayout = new GridLayout(2, false);
        formatLayout.marginHeight = 0;
        formatLayout.marginWidth = 0;
        final GridData formatLayoutData = new GridData(SWT.FILL, SWT.NONE, true, false);
        formatLayoutData.horizontalSpan = 2;
        formatComposite.setLayoutData(formatLayoutData);
        formatComposite.setLayout(formatLayout);

        // Text format
        formatRequirement = new StringFieldEditor(NamingRequirementPreferenceHelper.NAMING_FORMAT_REQUIREMENT_STORE, "", formatComposite); //$NON-NLS-1$
        formatRequirement.setPreferenceStore(getPreferenceStore());
        formatText = formatRequirement.getTextControl(formatComposite);

        // Select Label
        GridData selectLayoutData = new GridData(SWT.FILL, SWT.NONE, true, true);
        selectLayoutData.horizontalSpan = 2;
        final Label selectLabel = new Label(mainGroup, SWT.NONE);
        selectLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
        selectLabel.setText(Messages.getString("NamingRequirementPreferencePage.2")); //$NON-NLS-1$
        selectLabel.setLayoutData(selectLayoutData);

        // Key words Table
        tableViewer = CheckboxTableViewer.newCheckList(mainGroup, SWT.BORDER);
        tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, true));
        tableViewer.setContentProvider(new KeyWordContentProvider());
        tableViewer.setInput(NamingRequirementPreferenceHelper.KEY_WORDS);

        // Composite Button
        final Composite buttonsComposite = new Composite(mainGroup, SWT.NONE);
        final GridLayout buttonsCompoLayout = new GridLayout();
        buttonsCompoLayout.marginHeight = 0;
        buttonsCompoLayout.marginWidth = 0;
        buttonsComposite.setLayout(buttonsCompoLayout);
        buttonsComposite.setLayoutData(new GridData(SWT.NONE, SWT.TOP, false, true));

        // Add Button
        final Button addButton = new Button(buttonsComposite, SWT.PUSH);
        final GridData addDataLayout = new GridData(SWT.FILL, SWT.FILL, true, true);
        addDataLayout.widthHint = 75;
        addButton.setLayoutData(addDataLayout);
        addButton.setText(Messages.getString("NamingRequirementPreferencePage.3")); //$NON-NLS-1$
        addButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                String toAdd = ""; //$NON-NLS-1$
                Object[] selections = tableViewer.getCheckedElements();
                for (int i = 0; i < selections.length; i++)
                {
                    toAdd += selections[i];
                }
                String currValue = formatText.getText();
                int pos = formatText.getCaretPosition();
                formatText.setText(currValue.substring(0, pos) + toAdd + currValue.substring(pos, currValue.length()));
            }
        });

        // Composite Check
        final Composite checkComposite = new Composite(mainGroup, SWT.NONE);
        final GridLayout checkCompoLayout = new GridLayout();
        checkCompoLayout.marginHeight = 0;
        checkCompoLayout.marginWidth = 0;
        checkComposite.setLayout(checkCompoLayout);
        checkComposite.setLayoutData(new GridData(SWT.NONE, SWT.TOP, false, true));

        checkNumberDocument = new BooleanFieldEditor(NamingRequirementPreferenceHelper.NUMBER_REQUIREMENT_STORE, Messages.getString("NamingRequirementPreferencePage.5"), checkComposite); //$NON-NLS-1$
        checkNumberDocument.setPreferenceStore(getPreferenceStore());

        loadPreferences();

        tableViewer.refresh(true);

        return mainComposite;
    }

    /**
     * Loads the preferences
     */
    private void loadPreferences()
    {
        formatRequirement.load();
        checkNumberDocument.load();
    }

    /**
     * Stores the preferences
     */
    private void storePreferences()
    {
        formatRequirement.store();
        checkNumberDocument.store();
    }

    /**
     * Loads the default preferences
     */
    private void loadDefaultPreferences()
    {
        formatRequirement.loadDefault();
        checkNumberDocument.loadDefault();
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
        // Nothing to do
    }

    /**
     * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
     */
    private class KeyWordContentProvider implements IStructuredContentProvider
    {
        public Object[] getElements(Object inputElement)
        {
            return (Object[]) inputElement;
        }

        public void dispose()
        {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
        {
        }
    }

}
