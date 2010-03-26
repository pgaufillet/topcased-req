/***********************************************************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe MERTZ (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.preferences;

import java.util.LinkedHashSet;
import java.util.Set;

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
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;

/**
 * Manage preferences related to Upstream View settings.<br>
 * The user can define upstream requirement attributes he wants to display/to show into the <b>Upstream View</b>.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class UpstreamPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
    private static final String NEW_ATTRIBUTE = "New Attribute"; //$NON-NLS-1$

    private IPreferenceStore preferenceStore = RequirementCorePlugin.getDefault().getPreferenceStore();

    private Text valueText;

    private Table table;

    private Button removeButton;

    private Set<String> defaultAttributes;

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent)
    {
        final Composite mainComposite = new Composite(parent, SWT.NONE);
        final GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        mainComposite.setLayout(layout);

        final Group attributeGroup = new Group(mainComposite, SWT.NONE);
        attributeGroup.setText(Messages.getString("UpstreamPreferencePage.1")); //$NON-NLS-1$
        GridData attributeGroupGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        attributeGroup.setLayoutData(attributeGroupGridData);
        attributeGroup.setLayout(new GridLayout(2, false));

        final Label infoLabel = new Label(attributeGroup, SWT.NONE);
        GridData layoutData = new GridData(SWT.FILL, SWT.NONE, true, false);
        layoutData.horizontalSpan = 2;
        infoLabel.setLayoutData(layoutData);
        infoLabel.setText(Messages.getString("UpstreamPreferencePage.0")); //$NON-NLS-1$

        table = new Table(attributeGroup, SWT.BORDER);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        table.addSelectionListener(new AddAttributeListener());

        final Composite buttonsComposite = new Composite(attributeGroup, SWT.NONE);
        final GridLayout buttonsCompoLayout = new GridLayout();
        buttonsCompoLayout.marginHeight = 0;
        buttonsCompoLayout.marginWidth = 0;
        buttonsComposite.setLayout(buttonsCompoLayout);
        buttonsComposite.setLayoutData(new GridData(SWT.NONE, SWT.TOP, false, true));

        final Button addButton = new Button(buttonsComposite, SWT.PUSH);
        addButton.setToolTipText(Messages.getString("UpstreamPreferencePage.3"));
        addButton.setImage(RequirementCorePlugin.getImageDescriptor(ISharedImages.IMG_OBJ_ADD).createImage());
        addButton.addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(UpstreamPreferencePage.NEW_ATTRIBUTE);
                // set the focus on the new item created
                table.setSelection(item);
                valueText.setText("");
            }
        });

        removeButton = new Button(buttonsComposite, SWT.PUSH);
        removeButton.setToolTipText(Messages.getString("UpstreamPreferencePage.4"));
        removeButton.setImage(RequirementCorePlugin.getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE).createImage());
        removeButton.setEnabled(false);
        removeButton.addSelectionListener(new RemoveAttributeListener());

        final GridLayout valueLayout = new GridLayout(2, false);
        valueLayout.marginHeight = 0;
        valueLayout.marginWidth = 0;

        final Composite valueComposite = new Composite(attributeGroup, SWT.NONE);
        valueComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        valueComposite.setLayout(valueLayout);

        final Label valueLabel = new Label(valueComposite, SWT.NONE);
        valueLabel.setText(Messages.getString("UpstreamPreferencePage.5")); //$NON-NLS-1$

        valueText = new Text(valueComposite, SWT.BORDER);
        valueText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        valueText.setEnabled(false);
        valueText.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                valueText.setText(valueText.getText().trim());
                if (valueText.getText().trim().length() == 0)
                {
                    return;
                }
                TableItem item = table.getItem(table.getSelectionIndex());
                item.setText(valueText.getText());
            }
        });

        initValues();

        return mainComposite;
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench)
    {
        // Do nothing
    }

    /**
     * Get all value in the preferenceStore and display it in the preferencePage
     */
    private void initValues()
    {
        // Save the old value
        String attributeLocations = preferenceStore.getString(UpstreamPreferenceHelper.UPSTREAM_ATTRIBUTES_STORE);
        defaultAttributes = UpstreamPreferenceHelper.deserialize(attributeLocations);

        // load the attribute locations
        for (String attribute : defaultAttributes)
        {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(attribute);
        }
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk()
    {
        // Get new setting values
        Set<String> newAttributes = new LinkedHashSet<String>(8);
        for (int i = 0; i < table.getItemCount(); i++)
        {
            String attribute = table.getItem(i).getText();
            // don't save the "attribute"
            if (!UpstreamPreferencePage.NEW_ATTRIBUTE.equals(attribute))
            {
                newAttributes.add(attribute);
            }
        }

        // save preferences
        preferenceStore.setValue(UpstreamPreferenceHelper.UPSTREAM_ATTRIBUTES_STORE, UpstreamPreferenceHelper.serialize(newAttributes));
        return super.performOk();
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults()
    {
        Set<String> defaultAttributes = UpstreamPreferenceHelper.getDefaultValues();
        preferenceStore.setValue(UpstreamPreferenceHelper.UPSTREAM_ATTRIBUTES_STORE, UpstreamPreferenceHelper.serialize(defaultAttributes));

        table.removeAll();
        for (String attribute : defaultAttributes)
        {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(attribute);
        }

        valueText.setEnabled(false);
        valueText.setText(""); //$NON-NLS-1$
        removeButton.setEnabled(false);
        super.performDefaults();
    }

    /**
     * @author christophe.mertz@c-s.fr
     */
    private class AddAttributeListener extends SelectionAdapter
    {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
            TableItem item = (TableItem) e.item;
            if (table.getSelectionCount() == 0 || item == null)
            {
                valueText.setEnabled(false);
                removeButton.setEnabled(false);
                valueText.setText(""); //$NON-NLS-1$
            }
            else
            {
                valueText.setEnabled(true);
                removeButton.setEnabled(true);
                displayValue(item);
            }
        }
    }

    /**
     * @author christophe.mertz@c-s.fr
     */
    private class RemoveAttributeListener extends SelectionAdapter
    {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
            if (table.getSelectionCount() > 0)
            {
                // save the selection index
                int selectionIndex = table.getSelectionIndex();

                // remove the selected item
                table.remove(selectionIndex);

                // select an item in the table
                if (table.getItemCount() == 0)
                {
                    valueText.setEnabled(false);
                    removeButton.setEnabled(false);
                    valueText.setText(""); //$NON-NLS-1$
                }
                else
                {
                    if (selectionIndex >= table.getItemCount())
                    {
                        selectionIndex = table.getItemCount() - 1;
                    }
                    table.select(selectionIndex);
                    TableItem previousItem = table.getItem(selectionIndex);
                    displayValue(previousItem);
                }
            }
        }
    }

    /**
     * According to the item selected in the table, the text field receives the item text except if it is a new
     * attribute.
     * 
     * @param anItem a {@link TableItem}
     */
    private void displayValue(TableItem anItem)
    {
        if (!UpstreamPreferencePage.NEW_ATTRIBUTE.equals(anItem.getText()))
        {
            valueText.setText(anItem.getText());
        }
        else
        {
            valueText.setText(""); //$NON-NLS-1$
        }
    }
}
