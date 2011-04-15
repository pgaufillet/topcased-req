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
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.internal.Messages;

/**
 * The requirement preferences page.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public class RequirementPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
    /** The parent composite */
    private Composite parentComposite;

    /** the preference store */
    private IPreferenceStore preferenceStore = RequirementCorePlugin.getDefault().getPreferenceStore();

    /** the field to edit whether a deleting a model element with requirements need confirmation */
    private BooleanFieldEditor deleteModelElements;
    
    /** the field to edit whether a deleting a model element with requirements need confirmation */
    private BooleanFieldEditor displayCurrentReqDecorator;

    /**
     * Constructor
     */
    public RequirementPreferencePage()
    {
        super(Messages.getString("RequirementPreferencePage.0")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent)
    {
        parentComposite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        parentComposite.setLayout(layout);
        parentComposite.setFont(parent.getFont());

        Group group = new Group(parentComposite, SWT.SHADOW_ETCHED_OUT);
        group.setLayout(new GridLayout());
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText("Delete actions");

        Composite fieldsContainer = new Composite(group, SWT.NONE);
        fieldsContainer.setLayout(new GridLayout());
        fieldsContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        deleteModelElements = new BooleanFieldEditor(RequirementPreferenceConstants.DELETE_MODEL_WITHOUT_CONFIRM, "Do not ask for confirmation before deleting elements with current requirements.",
                fieldsContainer);
        deleteModelElements.setPreferenceStore(preferenceStore);

        Group group2 = new Group(parentComposite, SWT.SHADOW_ETCHED_OUT);
        group2.setLayout(new GridLayout());
        group2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group2.setText("Editor");
        
        Composite fieldsContainer2 = new Composite(group2, SWT.NONE);
        fieldsContainer2.setLayout(new GridLayout());
        fieldsContainer2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        displayCurrentReqDecorator = new BooleanFieldEditor(RequirementPreferenceConstants.DISPLAY_CURRENT_DECORATOR, "Display current requirements decorator",
                fieldsContainer2);
        displayCurrentReqDecorator.setPreferenceStore(preferenceStore);
        
        // load preferences to fields
        deleteModelElements.load();
        displayCurrentReqDecorator.load();

        return parentComposite;
    }

    /**
     * Restore the default values
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    protected void performDefaults()
    {
        deleteModelElements.loadDefault();
        displayCurrentReqDecorator.loadDefault();
        super.performDefaults();
    }

    /**
     * Store the values in the PreferenceStore
     * 
     * @see org.eclipse.jface.preference.IPreferencePage#performOk()
     */
    public boolean performOk()
    {
        deleteModelElements.store();
        displayCurrentReqDecorator.store();
        return super.performOk();
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench)
    {
        // Do nothind
    }

}
