/***********************************************************************************************************************
 * Copyright (c) 2012 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mathieu VELTEN (Atos) - initial API and implementation
 * 				 Matthieu BOIVINEAU (Atos) - composite modified according to the new deletion parameters model
 * 
 **********************************************************************************************************************/
package org.topcased.typesmodel.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.topcased.typesmodel.Messages;
import org.topcased.typesmodel.model.inittypes.DeletionParameters;
import org.topcased.typesmodel.model.inittypes.DeletionParemeter;
import org.topcased.typesmodel.model.inittypes.InittypesFactory;

/**
 * Composite of the deletion parameters. 
 */
public class DeletionParametersComposite {
	
	private Label lblIdRegex;
	private Text txtIdRegex;
	private Label lblDescriptionRegex;
	private Text txtDescriptionRegex;
	private Table tabAttributes;
	private Button btnAddAttribute;
	private Button btnEditAttribute;
	private Button btnRemoveAttribute;
	

	public DeletionParametersComposite(Composite composite, DeletionParameters deletionParameters) {
		composite.setLayout(new GridLayout(2, false));

		lblIdRegex = new Label(composite, SWT.NONE);
		lblIdRegex.setText(Messages.DeletionParametersComposite_LabelIdRegex);
		
		txtIdRegex = new Text(composite, SWT.BORDER);
		txtIdRegex.setText(Messages.DeletionParametersComposite_TestIdRegex);
		txtIdRegex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblDescriptionRegex = new Label(composite, SWT.NONE);
		lblDescriptionRegex.setText(Messages.DeletionParametersComposite_LabelDescriptionRegex);
		
		txtDescriptionRegex = new Text(composite, SWT.BORDER);
		txtDescriptionRegex.setText(Messages.DeletionParametersComposite_TextDescriptionRegex);
		txtDescriptionRegex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
		Group deletionGroup = new Group(composite, SWT.NONE);
        deletionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        deletionGroup.setText(Messages.DeletionParametersComposite_GroupAttributes);
        deletionGroup.setLayout(new GridLayout(2, false));

        // The table containing all the attributes (name & regex) 
        tabAttributes = new Table(deletionGroup, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        tabAttributes.setLinesVisible(true);
        tabAttributes.setHeaderVisible(true);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.heightHint = 150;
        tabAttributes.setLayoutData(data);
        String[] titles = { Messages.DeletionParametersComposite_ColumnAttributeName, Messages.DeletionParametersComposite_ColumnAttributeRegex};
        int i;
        for (i = 0; i < titles.length; i++) {
          TableColumn column = new TableColumn(tabAttributes, SWT.NONE);
          column.setText(titles[i]);
          tabAttributes.getColumn(i).pack();
        }
        // The column for the combo deletion/filters is added
        TableColumn column = new TableColumn(tabAttributes, SWT.NONE);
        column.setText(Messages.DeletionParametersComposite_ParameterTypeColumn);
        tabAttributes.getColumn(i).pack();
        
        Composite buttonGroup = new Composite(deletionGroup, SWT.NONE);
        buttonGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        buttonGroup.setLayout(new GridLayout(1, false));
        
        btnAddAttribute = new Button(buttonGroup, SWT.PUSH);
        btnAddAttribute.setText(Messages.DeletionParametersComposite_ButtonAdd);
        btnAddAttribute.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        btnAddAttribute.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				PopupAttributesDialog dialog = new PopupAttributesDialog(null);
                if (dialog.open() == Dialog.OK)
                {
                	TableItem item = new TableItem(tabAttributes, SWT.NONE);
                    item.setText (0, dialog.getAttributesName());
                    item.setText (1, dialog.getAttributesRegex());
                    item.setText (2, dialog.getAttributesType());
                }
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
        btnEditAttribute = new Button(buttonGroup, SWT.PUSH);
        btnEditAttribute.setText(Messages.DeletionParametersComposite_ButtonEdit);
        btnEditAttribute.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        btnEditAttribute.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				int selectedIndex = tabAttributes.getSelectionIndex();
				PopupAttributesDialog dialog = new PopupAttributesDialog(null,tabAttributes.getItem(selectedIndex).getText(0),tabAttributes.getItem(selectedIndex).getText(1), tabAttributes.getItem(selectedIndex).getText(2));
                if (dialog.open() == Dialog.OK)
                {
                	tabAttributes.getItem(selectedIndex).setText(0, dialog.getAttributesName());
                	tabAttributes.getItem(selectedIndex).setText(1, dialog.getAttributesRegex());
                	tabAttributes.getItem(selectedIndex).setText(2, dialog.getAttributesType());
                }
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
        btnRemoveAttribute = new Button(buttonGroup, SWT.PUSH);
        btnRemoveAttribute.setText(Messages.DeletionParametersComposite_ButtonRemove);
        btnRemoveAttribute.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        btnRemoveAttribute.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e){
				boolean result = MessageDialog.openConfirm(null, Messages.DeletionParametersComposite_DialogConfirmationTitle, Messages.DeletionParametersComposite_DialogConfirmationText);
				if (result){
					int[] indices = tabAttributes.getSelectionIndices();
					tabAttributes.remove(indices);
				}
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		});

        this.setDeletionParameters(deletionParameters);
	}
	
	/**
	 * Fills the display according to the given deletion parameters
	 * @param deletionParameters the deletion parameters to display
	 */
    public void setDeletionParameters(DeletionParameters deletionParameters) {
    	if(deletionParameters != null){
    		txtIdRegex.setText(deletionParameters.getRegexId());
    		txtDescriptionRegex.setText(deletionParameters.getRegexDescription());
    		if(deletionParameters.getRegexAttributes() != null){
    			for(DeletionParemeter deletionParameter:deletionParameters.getRegexAttributes()){
    				TableItem item = new TableItem(tabAttributes, SWT.NONE);
    				item.setText(0, deletionParameter.getNameAttribute());
    				item.setText(1, deletionParameter.getRegexAttribute());
    				item.setText(2, Messages.DeletionParametersComposite_Deletion);
    			}
    			for(DeletionParemeter filterParameter:deletionParameters.getFilterRegexAttributes()){
    				TableItem item = new TableItem(tabAttributes, SWT.NONE);
    				item.setText(0, filterParameter.getNameAttribute());
    				item.setText(1, filterParameter.getRegexAttribute());
    				item.setText(2, Messages.DeletionParametersComposite_Filter);
    			}
    		}
    	}
    }

    
    /**
     * Creates and returns deletion parameters filled according to the user inputs 
     * @return the created deletion parameter
     */
    public DeletionParameters getDeletionParameters() {
        DeletionParameters deleteParameters = InittypesFactory.eINSTANCE.createDeletionParameters();
        deleteParameters.setRegexId(txtIdRegex.getText());
        deleteParameters.setRegexDescription(txtDescriptionRegex.getText());
        TableItem[] tabItems = tabAttributes.getItems();
        for(int i=0;i<tabItems.length;i++){
        	DeletionParemeter param = InittypesFactory.eINSTANCE.createDeletionParemeter();
        	param.setNameAttribute(tabItems[i].getText(0));
        	param.setRegexAttribute(tabItems[i].getText(1));
        	if(Messages.DeletionParametersComposite_Filter.equals(tabItems[i].getText(2)))
        	{
        		deleteParameters.getFilterRegexAttributes().add(param);
        	}
        	else
        	{
        		deleteParameters.getRegexAttributes().add(param);
        	}
        }
        return deleteParameters;
    }
}
