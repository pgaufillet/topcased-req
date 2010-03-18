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

import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.Messages;

/**
 * Manages the preference store for the current requirement
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public class CurrentPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
    private static final String NEW_ATTRIBUT = "attribute"; //$NON-NLS-1$

    private static final String NEW_VALUE = "value"; //$NON-NLS-1$

    private IPreferenceStore preferenceStore = RequirementCorePlugin.getDefault().getPreferenceStore();

    private Text valueTextAttribute;

    private Text valueTextValue;

    private Table attributesTable;

    private Table valuesTable;

    private Button removeAttributeButton;

    private Button addButtonValue;

    private Button removeValueButton;

    private Button setDefaultButton;

    private Group valueGroup;

    private Button txtBtn;

    private Button objBtn;

    private Button allBtn;

    private Button linBtn;

    private String currentKey;

    private Map<String, Vector<String>> currDefaultAttributes;

    @Override
    protected Control createContents(Composite parent)
    {
        final Composite mainComposite = new Composite(parent, SWT.NONE);
        final GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        mainComposite.setLayout(layout);

        createContentsAttributes(mainComposite);
        createContentsValues(mainComposite);

        initValues();

        return mainComposite;
    }

    private void createContentsAttributes(Composite parent)
    {

        final Group attributeGroup = new Group(parent, SWT.NONE);
        attributeGroup.setText(Messages.getString("CurrentPreferencePage.2")); //$NON-NLS-1$
        GridData attributeGroupGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        attributeGroup.setLayoutData(attributeGroupGridData);
        attributeGroup.setLayout(new GridLayout(2, false));

        attributesTable = new Table(attributeGroup, SWT.BORDER);
        attributesTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        attributesTable.addSelectionListener(new SelectionTableListener());

        final Composite buttonsComposite = new Composite(attributeGroup, SWT.NONE);
        final GridLayout buttonsCompoLayout = new GridLayout();
        buttonsCompoLayout.marginHeight = 0;
        buttonsCompoLayout.marginWidth = 0;
        buttonsComposite.setLayout(buttonsCompoLayout);
        buttonsComposite.setLayoutData(new GridData(SWT.NONE, SWT.TOP, false, true));

        final Button addButton = new Button(buttonsComposite, SWT.PUSH);
        final GridLayout addLayout = new GridLayout();
        addLayout.marginHeight = 0;
        addLayout.marginWidth = 0;
        final GridData addDataLayout = new GridData(SWT.FILL, SWT.FILL, true, true);
        addDataLayout.widthHint = 75;
        addButton.setLayoutData(addDataLayout);
        addButton.setText(Messages.getString("CurrentPreferencePage.3")); //$NON-NLS-1$
        addButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                TableItem item = new TableItem(attributesTable, SWT.NONE);
                item.setText(CurrentPreferencePage.NEW_ATTRIBUT);
                unSetAttributeType();
                txtBtn.setSelection(true);
            }
        });

        removeAttributeButton = new Button(buttonsComposite, SWT.PUSH);
        final GridLayout removeLayout = new GridLayout();
        removeLayout.marginHeight = 0;
        removeLayout.marginWidth = 0;
        final GridData removeDataLayout = new GridData(SWT.FILL, SWT.FILL, true, true);
        removeDataLayout.widthHint = 75;
        removeAttributeButton.setLayoutData(removeDataLayout);
        removeAttributeButton.setText(Messages.getString("CurrentPreferencePage.4")); //$NON-NLS-1$
        removeAttributeButton.setEnabled(false);
        removeAttributeButton.addSelectionListener(new RemoveAttributeListener());

        /*
         * Attribute type
         */
        Group radioGroup = new Group(buttonsComposite, SWT.NONE);
        radioGroup.setText(Messages.getString("CurrentPreferencePage.5")); //$NON-NLS-1$
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        radioGroup.setLayoutData(gridData);
        radioGroup.setLayout(new GridLayout());
        txtBtn = createRadioButton(radioGroup, Messages.getString("CurrentPreferencePage.6")); //$NON-NLS-1$
        objBtn = createRadioButton(radioGroup, Messages.getString("CurrentPreferencePage.7")); //$NON-NLS-1$
        allBtn = createRadioButton(radioGroup, Messages.getString("CurrentPreferencePage.8")); //$NON-NLS-1$
        linBtn = createRadioButton(radioGroup, Messages.getString("CurrentPreferencePage.9")); //$NON-NLS-1$

        txtBtn.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(final SelectionEvent event)
            {
                if (txtBtn.getSelection())
                {
                    setEnableDefaultValue(true);
                    Vector<String> listValue = currDefaultAttributes.get(currentKey);

                    /*
                     * Delete the attribute
                     */
                    currDefaultAttributes.remove(currentKey);

                    currentKey = attributesTable.getSelection()[0].getText() + CurrentPreferenceHelper.STRING_IS_TEXT;
                    currDefaultAttributes.put(currentKey, listValue);
                }
            }
        });
        objBtn.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(final SelectionEvent event)
            {
                if (objBtn.getSelection())
                {
                    setEnableDefaultValue(false);
                    Vector<String> listValue = currDefaultAttributes.get(currentKey);

                    /*
                     * Delete the attribute with all the possible type
                     */
                    currDefaultAttributes.remove(currentKey);

                    currentKey = attributesTable.getSelection()[0].getText() + CurrentPreferenceHelper.STRING_IS_REFERENCE;
                    currDefaultAttributes.put(currentKey, listValue);
                }
            }
        });
        allBtn.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(final SelectionEvent event)
            {
                if (allBtn.getSelection())
                {
                    setEnableDefaultValue(false);
                    Vector<String> listValue = currDefaultAttributes.get(currentKey);

                    /*
                     * Delete the attribute with all the possible type
                     */
                    currDefaultAttributes.remove(currentKey);

                    currentKey = attributesTable.getSelection()[0].getText() + CurrentPreferenceHelper.STRING_IS_ALLOCATE;
                    currDefaultAttributes.put(currentKey, listValue);
                }
            }
        });
        linBtn.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(final SelectionEvent event)
            {
                if (linBtn.getSelection())
                {
                    setEnableDefaultValue(false);
                    Vector<String> listValue = currDefaultAttributes.get(currentKey);

                    /*
                     * Delete the attribute with all the possible type
                     */
                    currDefaultAttributes.remove(currentKey);

                    currentKey = attributesTable.getSelection()[0].getText() + CurrentPreferenceHelper.STRING_IS_LINK;
                    currDefaultAttributes.put(currentKey, listValue);
                }
            }
        });

        Composite valueComposite = new Composite(attributeGroup, SWT.NONE);
        final GridLayout valueLayout = new GridLayout(2, false);
        valueLayout.marginHeight = 0;
        valueLayout.marginWidth = 0;

        final GridData valueLayoutData = new GridData(SWT.FILL, SWT.NONE, true, false);
        valueLayoutData.horizontalSpan = 1;
        valueComposite.setLayoutData(valueLayoutData);
        valueComposite.setLayout(valueLayout);

        final Label valueLabel = new Label(valueComposite, SWT.NONE);
        valueLabel.setText(Messages.getString("CurrentPreferencePage.10")); //$NON-NLS-1$

        valueTextAttribute = new Text(valueComposite, SWT.BORDER);
        valueTextAttribute.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        valueTextAttribute.setEnabled(false);
        valueTextAttribute.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                if (!currDefaultAttributes.containsKey(valueTextAttribute.getText()))
                {
                    valueTextAttribute.setText(valueTextAttribute.getText().trim());
                    if (valueTextAttribute.getText().trim().length() == 0)
                    {
                        return;
                    }

                    Vector<String> listValue = currDefaultAttributes.get(currentKey);
                    currDefaultAttributes.remove(currentKey);

                    String key = valueTextAttribute.getText();
                    if (CurrentPreferenceHelper.isText(currentKey))
                    {
                        currentKey = key + CurrentPreferenceHelper.STRING_IS_TEXT;
                    }
                    else if (CurrentPreferenceHelper.isReference(currentKey))
                    {
                        currentKey = key + CurrentPreferenceHelper.STRING_IS_REFERENCE;
                    }
                    else if (CurrentPreferenceHelper.isLink(currentKey))
                    {
                        currentKey = key + CurrentPreferenceHelper.STRING_IS_LINK;
                    }
                    else if (CurrentPreferenceHelper.isAllocate(currentKey))
                    {
                        currentKey = key + CurrentPreferenceHelper.STRING_IS_ALLOCATE;
                    }
                    currDefaultAttributes.put(currentKey, listValue);

                    attributesTable.getSelection()[0].setText(valueTextAttribute.getText());
                }
            }
        });
    }

    private Button createRadioButton(final Composite composite, final String text)
    {
        Button result = new Button(composite, SWT.RADIO);
        result.setText(text);
        result.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        return result;
    }

    private void createContentsValues(Composite parent)
    {
        valueGroup = new Group(parent, SWT.NONE);
        GridData valueGroupGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        valueGroup.setText(Messages.getString("CurrentPreferencePage.11")); //$NON-NLS-1$
        valueGroupGridData.horizontalSpan = 1;
        valueGroup.setLayoutData(valueGroupGridData);
        valueGroup.setLayout(new GridLayout(2, false));
        valuesTable = new Table(valueGroup, SWT.BORDER);
        valuesTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        valuesTable.addSelectionListener(new AddValueListener());

        final Composite buttonsComposite = new Composite(valueGroup, SWT.NONE);
        final GridLayout buttonsCompoLayout = new GridLayout();
        buttonsCompoLayout.marginHeight = 0;
        buttonsCompoLayout.marginWidth = 0;
        buttonsComposite.setLayout(buttonsCompoLayout);
        buttonsComposite.setLayoutData(new GridData(SWT.NONE, SWT.TOP, false, true));

        addButtonValue = new Button(buttonsComposite, SWT.PUSH);
        final GridLayout addLayout = new GridLayout();
        addLayout.marginHeight = 0;
        addLayout.marginWidth = 0;
        final GridData addDataLayout = new GridData(SWT.FILL, SWT.FILL, true, true);
        addDataLayout.widthHint = 75;
        addButtonValue.setLayoutData(addDataLayout);
        addButtonValue.setText(Messages.getString("CurrentPreferencePage.12")); //$NON-NLS-1$
        addButtonValue.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                TableItem item = new TableItem(valuesTable, SWT.NONE);
                item.setText(CurrentPreferencePage.NEW_VALUE);

                // update the current attribute's configuration
                Vector<String> listValue = new Vector<String>();
                for (int i = 0; i < valuesTable.getItemCount(); i++)
                {
                    listValue.add(valuesTable.getItem(i).getText());
                }

                currDefaultAttributes.put(currentKey, listValue);
            }
        });

        removeValueButton = new Button(buttonsComposite, SWT.PUSH);
        final GridLayout removeLayout = new GridLayout();
        removeLayout.marginHeight = 0;
        removeLayout.marginWidth = 0;
        final GridData removeDataLayout = new GridData(SWT.FILL, SWT.FILL, true, true);
        removeDataLayout.widthHint = 75;
        removeValueButton.setLayoutData(removeDataLayout);
        removeValueButton.setText(Messages.getString("CurrentPreferencePage.13")); //$NON-NLS-1$
        removeValueButton.setEnabled(false);
        removeValueButton.addSelectionListener(new RemoveValueListener());

        setDefaultButton = new Button(buttonsComposite, SWT.PUSH);
        final GridLayout setLayout = new GridLayout();
        setLayout.marginHeight = 0;
        setLayout.marginWidth = 0;
        final GridData setDataLayout = new GridData(SWT.FILL, SWT.FILL, true, true);
        setDataLayout.widthHint = 75;
        setDefaultButton.setLayoutData(setDataLayout);
        setDefaultButton.setEnabled(false);
        setDefaultButton.addSelectionListener(new SetDefaultValueListener());
        setDefaultButton.setText(Messages.getString("CurrentPreferencePage.14")); //$NON-NLS-1$

        final Composite valueComposite = new Composite(valueGroup, SWT.NONE);
        final GridLayout valueLayout = new GridLayout(2, false);
        valueLayout.marginHeight = 0;
        valueLayout.marginWidth = 0;

        final GridData valueLayoutData = new GridData(SWT.FILL, SWT.NONE, true, false);
        valueLayoutData.horizontalSpan = 1;
        valueComposite.setLayoutData(valueLayoutData);
        valueComposite.setLayout(valueLayout);

        final Label valueLabel = new Label(valueComposite, SWT.NONE);
        valueLabel.setText(Messages.getString("CurrentPreferencePage.15")); //$NON-NLS-1$

        valueTextValue = new Text(valueComposite, SWT.BORDER);
        valueTextValue.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        valueTextValue.setEnabled(false);
        valueTextValue.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                valueTextValue.setText(valueTextValue.getText().trim());
                if (valueTextValue.getText().trim().length() == 0)
                {
                    return;
                }
                valuesTable.getSelection()[0].setText(valueTextValue.getText());

                Vector<String> listValue = new Vector<String>();
                for (int i = 0; i < valuesTable.getItemCount(); i++)
                {
                    listValue.add(valuesTable.getItem(i).getText());
                }

                currDefaultAttributes.put(currentKey, listValue);
            }
        });
    }

    public void init(IWorkbench workbench)
    {
        // TODO Auto-generated method stub
    }

    /**
     * Get all value in the preferenceStore and display it in the preferencePage
     */
    private void initValues()
    {
        // Load the attributes
        String attributeLocations = preferenceStore.getString(CurrentPreferenceHelper.CURRENT_ATTRIBUTES_STORE);
        currDefaultAttributes = CurrentPreferenceHelper.deserialize(attributeLocations);

        Set<Entry<String, Vector<String>>> entrySet = currDefaultAttributes.entrySet();
        // update the table
        for (Entry<String, Vector<String>> anEntry : entrySet)
        {
            TableItem item = new TableItem(attributesTable, SWT.NONE);
            item.setText(CurrentPreferenceHelper.getLabelAttribute(anEntry.getKey()));
        }

        setEnableDefaultValue(false);
        setEnableAttributeButton(false);
    }

    @Override
    public boolean performOk()
    {
        preferenceStore.setValue(CurrentPreferenceHelper.CURRENT_ATTRIBUTES_STORE, CurrentPreferenceHelper.serialize(currDefaultAttributes));

        return super.performOk();
    }

    @Override
    protected void performApply()
    {
        super.performApply();
    }

    @Override
    protected void performDefaults()
    {
        /*
         * Default attributes
         */
        currDefaultAttributes = CurrentPreferenceHelper.getDefaultValues();
        preferenceStore.setValue(CurrentPreferenceHelper.CURRENT_ATTRIBUTES_STORE, CurrentPreferenceHelper.serialize(currDefaultAttributes));

        attributesTable.removeAll();
        valuesTable.removeAll();

        Set<Entry<String, Vector<String>>> entrySet = currDefaultAttributes.entrySet();
        // update the table
        for (Entry<String, Vector<String>> anEntry : entrySet)
        {
            TableItem item = new TableItem(attributesTable, SWT.NONE);
            item.setText(CurrentPreferenceHelper.getLabelAttribute(anEntry.getKey()));
        }

        valueTextAttribute.setEnabled(false);
        valueTextAttribute.setText(""); //$NON-NLS-1$
        removeAttributeButton.setEnabled(false);

        valueTextValue.setEnabled(false);
        valueTextValue.setText(""); //$NON-NLS-1$
        removeValueButton.setEnabled(false);
        setDefaultButton.setEnabled(false);

        setEnableDefaultValue(false);
        setEnableAttributeButton(false);

        super.performDefaults();
    }

    private void setEnableDefaultValue(Boolean state)
    {
        valueGroup.setEnabled(state);
        valuesTable.setEnabled(state);
        valueTextValue.setEnabled(state);
        addButtonValue.setEnabled(state);
    }

    private void setEnableAttributeButton(Boolean state)
    {
        txtBtn.setEnabled(state);
        objBtn.setEnabled(state);
        allBtn.setEnabled(state);
        linBtn.setEnabled(state);
    }

    private void unSetAttributeType()
    {
        txtBtn.setSelection(false);
        objBtn.setSelection(false);
        allBtn.setSelection(false);
        linBtn.setSelection(false);
    }

    private class SetDefaultValueListener extends SelectionAdapter
    {
        public void widgetSelected(SelectionEvent e)
        {
            // search if a previous default value exists
            for (int i = 0; i < valuesTable.getItemCount(); i++)
            {
                if (CurrentPreferenceHelper.isDefaultValue(valuesTable.getItem(i).getText()))
                {
                    int pos = valuesTable.getItem(i).getText().indexOf(CurrentPreferenceHelper.STRING_DEFAULT_VALUE);
                    String newVal = (String) valuesTable.getItem(i).getText().subSequence(0, pos);
                    valuesTable.getItem(i).setText(newVal);
                }
            }

            String defaultValue = valuesTable.getSelection()[0].getText() + CurrentPreferenceHelper.STRING_DEFAULT_VALUE;
            valuesTable.getSelection()[0].setText(defaultValue);

            // update the current attribute's configuration
            Vector<String> listValue = new Vector<String>();
            for (int i = 0; i < valuesTable.getItemCount(); i++)
            {
                listValue.add(valuesTable.getItem(i).getText());
            }
            currDefaultAttributes.put(currentKey, listValue);
        }
    }

    private class RemoveAttributeListener extends SelectionAdapter
    {
        public void widgetSelected(SelectionEvent e)
        {
            if (attributesTable.getSelectionCount() > 0)
            {
                currDefaultAttributes.remove(currentKey);

                // save the selection index
                int selectionIndex = attributesTable.getSelectionIndex();

                // remove the selected item
                attributesTable.remove(selectionIndex);

                // select an item in the table
                if (attributesTable.getItemCount() > 0)
                {
                    if (selectionIndex >= attributesTable.getItemCount())
                    {
                        selectionIndex = attributesTable.getItemCount() - 1;
                    }
                    attributesTable.select(selectionIndex);
                    updateAttributeType(attributesTable.getSelection()[0].getText());

                    /*
                     * Update list of values
                     */
                    valuesTable.removeAll();
                    Vector<String> listValue = currDefaultAttributes.get(currentKey);
                    for (String val : listValue)
                    {
                        TableItem itemValue = new TableItem(valuesTable, SWT.NONE);
                        itemValue.setText(val);
                    }
                }
                else
                {
                    valueTextAttribute.setEnabled(false);
                    valueTextAttribute.setText(""); //$NON-NLS-1$
                    removeAttributeButton.setEnabled(false);
                }
                if (attributesTable.getItemCount() == 0)
                {
                    setEnableAttributeButton(false);
                }

            }
        }
    }

    private class SelectionTableListener extends SelectionAdapter
    {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
            TableItem item = (TableItem) e.item;
            if (attributesTable.getSelectionCount() == 0 || item == null)
            {
                valueTextAttribute.setText(""); //$NON-NLS-1$
                valueTextAttribute.setEnabled(false);
                removeAttributeButton.setEnabled(false);
                setEnableDefaultValue(false);
                setEnableAttributeButton(false);
                unSetAttributeType();
                currentKey = ""; //$NON-NLS-1$
            }
            else
            {
                valueTextAttribute.setEnabled(true);
                removeAttributeButton.setEnabled(true);
                if (!CurrentPreferencePage.NEW_ATTRIBUT.equals(item.getText()))
                {
                    valueTextAttribute.setText(item.getText());
                }
                else
                {
                    valueTextAttribute.setText(""); //$NON-NLS-1$
                }

                /*
                 * Attribute type
                 */
                updateAttributeType(item.getText());

                /*
                 * Update list of values
                 */
                valuesTable.removeAll();
                Vector<String> listValue = currDefaultAttributes.get(currentKey);
                if (listValue != null)
                {
                    for (String val : listValue)
                    {
                        TableItem itemValue = new TableItem(valuesTable, SWT.NONE);
                        itemValue.setText(val);
                    }
                }
            }
        }
    }

    private class AddValueListener extends SelectionAdapter
    {

        @Override
        public void widgetDefaultSelected(SelectionEvent e)
        {
            // TODO Auto-generated method stub
            super.widgetDefaultSelected(e);
        }

        @Override
        public void widgetSelected(SelectionEvent e)
        {
            TableItem item = (TableItem) e.item;
            if (valuesTable.getSelectionCount() == 0 || item == null)
            {
                valueTextValue.setText(""); //$NON-NLS-1$
                valueTextValue.setEnabled(false);
                removeValueButton.setEnabled(false);
                setDefaultButton.setEnabled(false);
            }
            else
            {
                valueTextValue.setEnabled(true);
                removeValueButton.setEnabled(true);
                setDefaultButton.setEnabled(true);
                if (!CurrentPreferencePage.NEW_VALUE.equals(item.getText()))
                {
                    valueTextValue.setText(item.getText());
                }
                else
                {
                    valueTextValue.setText(""); //$NON-NLS-1$
                }
            }
        }
    }

    private class RemoveValueListener extends SelectionAdapter
    {
        public void widgetSelected(SelectionEvent e)
        {
            if (valuesTable.getSelectionCount() > 0)
            {
                // save the selection index
                int selectionIndex = valuesTable.getSelectionIndex();

                // remove the selected item
                valuesTable.remove(selectionIndex);

                // select an item in the table
                if (selectionIndex < valuesTable.getItemCount())
                {
                    valuesTable.select(selectionIndex);
                }
                else if (valuesTable.getItemCount() > 0)
                {
                    valuesTable.select(valuesTable.getItemCount() - 1);
                }
                else
                {
                    valueTextValue.setEnabled(false);
                    valueTextValue.setText(""); //$NON-NLS-1$
                    removeValueButton.setEnabled(false);
                    setDefaultButton.setEnabled(false);
                }

                // update the current attribute's configuration
                Vector<String> listValue = new Vector<String>();
                for (int i = 0; i < valuesTable.getItemCount(); i++)
                {
                    listValue.add(valuesTable.getItem(i).getText());
                }
                currDefaultAttributes.put(currentKey, listValue);
            }
        }
    }

    private void updateAttributeType(String key)
    {
        unSetAttributeType();
        setEnableDefaultValue(false);
        setEnableAttributeButton(true);

        if (!CurrentPreferencePage.NEW_ATTRIBUT.equals(key))
        {
            if (currDefaultAttributes.containsKey(key + CurrentPreferenceHelper.STRING_IS_TEXT))
            {
                txtBtn.setSelection(true);
                setEnableDefaultValue(true);
                currentKey = key + CurrentPreferenceHelper.STRING_IS_TEXT;
            }
            if (currDefaultAttributes.containsKey(key + CurrentPreferenceHelper.STRING_IS_LINK))
            {
                linBtn.setSelection(true);
                currentKey = key + CurrentPreferenceHelper.STRING_IS_LINK;
            }
            if (currDefaultAttributes.containsKey(key + CurrentPreferenceHelper.STRING_IS_ALLOCATE))
            {
                allBtn.setSelection(true);
                currentKey = key + CurrentPreferenceHelper.STRING_IS_ALLOCATE;
            }
            if (currDefaultAttributes.containsKey(key + CurrentPreferenceHelper.STRING_IS_REFERENCE))
            {
                objBtn.setSelection(true);
                currentKey = key + CurrentPreferenceHelper.STRING_IS_REFERENCE;
            }
        }
        else
        {
            txtBtn.setSelection(true);
            setEnableDefaultValue(true);
            currentKey = key + CurrentPreferenceHelper.STRING_IS_TEXT;
        }
    }

}
