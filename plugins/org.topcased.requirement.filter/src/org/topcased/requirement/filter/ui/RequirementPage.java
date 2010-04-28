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
 *  Amine Bouchikhi (ATOS ORIGIN INTEGRATION) amine.bouchikhi@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.filter.ui;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.topcased.requirement.document.utils.Serializer;
import org.topcased.requirement.filter.Activator;

/**
 * The Class RequirementPage.
 */
public class RequirementPage extends WizardPage
{

    /** The Constant PREFERENCE_FILTER. */
    private static final String PREFERENCE_FILTER_ATTRIBUTES = "preferenceFilterRequirement_attributes";

    private static final String PREFERENCE_FILTER_REGEX = "preferenceFilterRequirement_regex";

    private static final String PREFERENCE_NAME_REGEX = "preferenceNameRequirement_regex";

    /** The top composite element. */
    private Composite top;

    /** The OR button. */
    private Button OR;

    /** The AND button. */
    private Button AND;

    /** The table of regular expressions. */
    private Table table;

    private Composite nameComp;

    private Text tName;

    /**
     * Instantiates a new requirement page.
     * 
     * @param pageName the page name
     */
    public RequirementPage(String pageName)
    {
        super(pageName);
        setMessage("Filter requirements with regular expressions." + "\nPlease enter the attribute name to filter and add the regular expression to match it.");
        setDescription("Filter requirements with regular expressions.");
        setTitle("Requirement Filtering");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets .Composite)
     */
    public void createControl(Composite parent)
    {
        top = new Composite(parent, SWT.NONE);

        // create elements

        top.setLayout(new GridLayout(2, false));

        nameComp = new Composite(top, SWT.NONE);
        nameComp.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
        nameComp.setLayout(new GridLayout(4, false));

        Label lName = new Label(nameComp, SWT.NONE);
        lName.setText("Filter on requirement name :");
        lName.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false, 1, 1));
        tName = new Text(nameComp, SWT.BORDER);
        tName.setEditable(false);
        tName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        Button bAddName = new Button(nameComp, SWT.NONE);
        bAddName.setText("add");
        bAddName.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false, 1, 1));
        bAddName.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {

                NameRegexDialog regexDial = new NameRegexDialog(top.getShell());
                if (regexDial.open() == Dialog.OK)
                {
                    tName.setText(regexDial.getUserRegex());
                }
                getWizard().getContainer().updateButtons();
                getWizard().getContainer().updateMessage();
            }
        });
        ;

        Button bDelName = new Button(nameComp, SWT.NONE);
        bDelName.setText("del");
        bDelName.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false, 1, 1));
        bDelName.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                tName.setText("");
                getWizard().getContainer().updateButtons();
                getWizard().getContainer().updateMessage();
            }
        });
        ;
        table = new Table(top, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setLinesVisible(false);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.heightHint = 200;
        data.horizontalSpan = 2;
        String[] titles = {"Attributes", "Regex"};
        for (int i = 0; i < titles.length; i++)
        {
            TableColumn column = new TableColumn(table, SWT.FILL);
            column.setText(titles[i]);
            data = new GridData(SWT.FILL, SWT.FILL, true, true);

            data.horizontalSpan = 1;
            column.setData(data);
        }
        top.addControlListener(new ControlAdapter()
        {
            public void controlResized(ControlEvent e)
            {
                Rectangle area = top.getClientArea();
                Point preferredSize = table.computeSize(SWT.NONE, SWT.NONE);
                int width = area.width - 2 * table.getBorderWidth();
                if (preferredSize.y > area.height + table.getHeaderHeight())
                {
                    // Subtract the scrollbar width from the total column width
                    // if a vertical scrollbar will be required
                    Point vBarSize = table.getVerticalBar().getSize();
                    width -= vBarSize.x;
                }
                Point oldSize = table.getSize();
                if (oldSize.x > area.width)
                {
                    // table is getting smaller so make the columns
                    // smaller first and then resize the table to
                    // match the client area width
                    table.getColumn(0).setWidth(width / 3);
                    table.getColumn(1).setWidth(table.getColumn(0).getWidth());
                    table.setSize(area.width, area.height);
                }
                else
                {
                    // table is getting bigger so make the table
                    // bigger first and then make the columns wider
                    // to match the client area width
                    table.setSize(area.width, area.height);
                    table.getColumn(0).setWidth(width / 3);
                    table.getColumn(1).setWidth(table.getColumn(0).getWidth());
                }
            }
        });
        final TableEditor editor = new TableEditor(table);
        // The editor must have the same size as the cell and must
        // not be any smaller than 50 pixels.
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
        // // editing the second column
        // final int EDITABLECOLUMN = 0;

        table.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {

                SelectionEvent event = (SelectionEvent) e;
                // Clean up any previous editor control
                Control oldEditor = editor.getEditor();
                if (oldEditor != null)
                    oldEditor.dispose();

                // Identify the selected row
                TableItem item = (TableItem) event.item;
                if (item == null)
                    return;

            }
        });

        Button addRegex = new Button(top, SWT.NONE);
        addRegex.setText("add");
        addRegex.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
        addRegex.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {

                RegexPopupDialog regexDial = new RegexPopupDialog(top.getShell());
                if (regexDial.open() == Dialog.OK)
                {
                    TableItem newRow = new TableItem(table, SWT.NONE);
                    newRow.setText(1, regexDial.getUserRegex());
                    newRow.setText(0, regexDial.getUserAttrName());
                    table.setSelection(newRow);
                }
                getWizard().getContainer().updateButtons();
                getWizard().getContainer().updateMessage();
            }
        });
        ;

        Button delRegex = new Button(top, SWT.NONE);
        delRegex.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
        delRegex.setText("del");
        delRegex.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                table.remove(table.getSelectionIndices());
                getWizard().getContainer().updateButtons();
                getWizard().getContainer().updateMessage();

            }
        });
        Composite compoORAND = new Composite(top, SWT.NONE);
        compoORAND.setLayout(new GridLayout(2, true));
        OR = new Button(compoORAND, SWT.RADIO);
        OR.setText("At least one regular expression must be matched");
        OR.setSelection(true);
        AND = new Button(compoORAND, SWT.RADIO);
        AND.setText("All regular expressions must be matched");
        initFromPreference(table);

        Label warning = new Label(top, SWT.NONE);
        warning.setText("\nWarning : At the end of filter process, your file shall be overridded.");
        warning.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 2, 1));
        top.pack();
        getWizard().getContainer().updateButtons();
        getWizard().getContainer().updateMessage();

        setControl(top);
    }

    /**
     * Inits the from preference.
     * 
     * @param table the table
     */
    @SuppressWarnings("unchecked")
    private void initFromPreference(Table table)
    {
        List<String> attributes = (List<String>) getFromPreference(PREFERENCE_FILTER_ATTRIBUTES);
        List<String> regexes = (List<String>) getFromPreference(PREFERENCE_FILTER_REGEX);
        Object nameFromPreference = getFromPreference(PREFERENCE_NAME_REGEX);
        if (nameFromPreference != null)
        {
            tName.setText((String) nameFromPreference);
        }
        if (attributes != null && regexes != null)
        {
            for (int i = 0; i < attributes.size(); i++)
            {
                String tmp = attributes.get(i);
                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(0, tmp);
                item.setText(1, regexes.get(i));
            }
        }

    }

    /**
     * @param pref
     * @return the preferences
     */
    private Object getFromPreference(String pref)
    {
        String s = Activator.getDefault().getPreferenceStore().getString(pref);
        Serializer<Object> ser = new Serializer<Object>();
        Object value = ser.unSerialize(s);
        return value;
    }

    /**
     * Save preferences.
     * 
     * @param map the map
     */
    public void savePreferences(List<String> attributes, List<String> regexes, String nameRegex)
    {
        Serializer<Object> ser = new Serializer<Object>();
        Activator.getDefault().getPreferenceStore().putValue(PREFERENCE_FILTER_ATTRIBUTES, ser.serialize(attributes));
        Activator.getDefault().getPreferenceStore().putValue(PREFERENCE_FILTER_REGEX, ser.serialize(regexes));
        Activator.getDefault().getPreferenceStore().putValue(PREFERENCE_NAME_REGEX, ser.serialize(nameRegex));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
     */
    @Override
    public boolean canFlipToNextPage()
    {
        boolean result = true;
        int numberOfReal = 0;
        for (TableItem t : table.getItems())
        {
            result = result && (!(t.getText(0).length() > 0) || t.getText(1).length() > 0);
            if (t.getText(0).length() > 0 && t.getText(1).length() > 0)
            {
                numberOfReal++;
            }
        }
        return (result && numberOfReal > 0) || (tName.getText() != "");
    }

    /**
     * Checks if is aND selected.
     * 
     * @return true, if is aND selected
     */
    public boolean isANDSelected()
    {
        return AND.getSelection();
    }

    public String getNameRegex()
    {
        return tName.getText();
    }

    public List<String> getRegexes()
    {
        List<String> result = new LinkedList<String>();
        for (TableItem t : table.getItems())
        {
            if (t.getText(0).length() > 0 && t.getText(1).length() > 0)
            {
                result.add(t.getText(1));
            }
        }
        return result;
    }

    public List<String> getAttributes()
    {
        List<String> result = new LinkedList<String>();
        for (TableItem t : table.getItems())
        {
            if (t.getText(0).length() > 0 && t.getText(1).length() > 0)
            {
                result.add(t.getText(0));
            }
        }
        return result;
    }

}
