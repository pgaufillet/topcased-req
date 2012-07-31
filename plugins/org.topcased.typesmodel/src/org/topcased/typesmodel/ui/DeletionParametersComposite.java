/***********************************************************************************************************************
 * Copyright (c) 2012 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mathieu VELTEN (Atos) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.typesmodel.ui;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.topcased.typesmodel.Messages;
import org.topcased.typesmodel.handler.IniManagerRegistry;
import org.topcased.typesmodel.model.inittypes.DeletionParameters;
import org.topcased.typesmodel.model.inittypes.InittypesFactory;


public class DeletionParametersComposite {
	
    public static final String PREFERENCE_DELETION_MATCH_ID = "deletionMatchId_updateReq"; //$NON-NLS-1$
    
    public static final String PREFERENCE_DELETION_MATCH_DESCRIPTION = "deletionMatchDescription_updateReq"; //$NON-NLS-1$
    
    public static final String PREFERENCE_DELETION_ATTS_TO_MATCH = "deletionAttsToMatch_updateReq"; //$NON-NLS-1$
    
    public static final String PREFERENCE_DELETION_REGEX = "deletionRegex_updateReq"; //$NON-NLS-1$

	private Button btnMatchId;
	private Button btnMatchDescription;
	private Button btnAttributesToMatch;
	private Text txtAttributesToMatch;
	private Text txtRegex;
	private IPreferenceStore prefStore;
	
	public DeletionParametersComposite(Composite composite) {
		this(composite, null);
	}

	public DeletionParametersComposite(Composite composite, IPreferenceStore prefStore) {
		this.prefStore = prefStore;
		
		composite.setLayout(new GridLayout(2, false));

        Label lblToMatch = new Label(composite, SWT.NONE);
        lblToMatch.setText(Messages.lblToMatch);
        new Label(composite, SWT.NONE);
        
        btnMatchId = new Button(composite, SWT.CHECK);
        btnMatchId.setText(Messages.btnMatchId);
        new Label(composite, SWT.NONE);
        
        btnMatchDescription = new Button(composite, SWT.CHECK);
        btnMatchDescription.setText(Messages.btnMatchDescription);
        new Label(composite, SWT.NONE);
        
        btnAttributesToMatch = new Button(composite, SWT.CHECK);
        btnAttributesToMatch.setText(Messages.btnAttributesToMatch);
        
        txtAttributesToMatch = new Text(composite, SWT.BORDER);
        txtAttributesToMatch.setText(Messages.txtAttributesToMatch);
        txtAttributesToMatch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        btnAttributesToMatch.addSelectionListener(new SelectionListener()
        {

            public void widgetSelected(SelectionEvent e)
            {
                txtAttributesToMatch.setEnabled(btnAttributesToMatch.getSelection());
                if (btnAttributesToMatch.getSelection() && Messages.txtAttributesToMatch.equals(txtAttributesToMatch.getText())) {
                    txtAttributesToMatch.setText("");
                }
            }

            public void widgetDefaultSelected(SelectionEvent e){
                widgetSelected(e);
            }
        });

        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);

        Label lblRegex = new Label(composite, SWT.NONE);
        lblRegex.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblRegex.setText(Messages.lblRegex);
        
        txtRegex = new Text(composite, SWT.BORDER);
        txtRegex.setText(Messages.txtRegex);
        txtRegex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        loadPrefs();
	}
	
	private void loadPrefs() {
		if (prefStore != null) {
			boolean deletionMatchId = prefStore.getBoolean(PREFERENCE_DELETION_MATCH_ID);
	        boolean deletionMatchDescription = prefStore.getBoolean(PREFERENCE_DELETION_MATCH_DESCRIPTION);
	        
	        String deletionAttsToMatch = null;
	        if (!prefStore.isDefault(PREFERENCE_DELETION_ATTS_TO_MATCH)) {
	            deletionAttsToMatch = prefStore.getString(PREFERENCE_DELETION_ATTS_TO_MATCH);
	        }
	        String deletionRegex = prefStore.getString(PREFERENCE_DELETION_REGEX);
	        
	        setDeletionParameters(deletionMatchId, deletionMatchDescription, deletionAttsToMatch, deletionRegex);
		} else {
			setDeletionParameters(false, false, null, "");
		}
	}
	
    
    public void savePrefs() {
    	prefStore.setValue(PREFERENCE_DELETION_MATCH_ID, btnMatchId.getSelection());
    	prefStore.setValue(PREFERENCE_DELETION_MATCH_DESCRIPTION, btnMatchDescription.getSelection());
    	prefStore.setValue(PREFERENCE_DELETION_REGEX, txtRegex.getText());

        if (btnAttributesToMatch.getSelection() 
                && !org.topcased.typesmodel.Messages.txtAttributesToMatch.equals(txtAttributesToMatch.getText())
                && !"".equals(txtAttributesToMatch.getText())) {
        	prefStore.setValue(PREFERENCE_DELETION_ATTS_TO_MATCH, txtAttributesToMatch.getText());
        } else {
        	prefStore.setValue(PREFERENCE_DELETION_ATTS_TO_MATCH, "");
        }
    }
    
    public void setDeletionParameters(DeletionParameters deletionParameters) {
    	setDeletionParameters(deletionParameters.isMatchId(), deletionParameters.isMatchDescription(), IniManagerRegistry.serializeCommaSeparated(deletionParameters.getAttributesToMatch()), deletionParameters.getRegex());
    }

	public void setDeletionParameters(boolean matchId, boolean matchDescription, String attributesToMatch, String regex) {
        btnMatchId.setSelection(matchId);
        btnMatchDescription.setSelection(matchDescription);
        
        if (attributesToMatch == null || "".equals(attributesToMatch)) {
        	btnAttributesToMatch.setSelection(false);
        	txtAttributesToMatch.setEnabled(false);
        } else {
        	btnAttributesToMatch.setSelection(true);
        	txtAttributesToMatch.setEnabled(true);
        	txtAttributesToMatch.setText(attributesToMatch);
        }
        txtRegex.setText(regex);
	}

    public DeletionParameters getDeletionParameters() {
        DeletionParameters deleteParameters = InittypesFactory.eINSTANCE.createDeletionParameters();
        deleteParameters.setMatchId(btnMatchId.getSelection());
        deleteParameters.setMatchDescription(btnMatchDescription.getSelection());
        deleteParameters.setRegex(txtRegex.getText());
        
        String attributesToMatch = txtAttributesToMatch.getText();
        if (btnAttributesToMatch.getSelection()
        	&& !org.topcased.typesmodel.Messages.txtAttributesToMatch.equals(attributesToMatch)
        	&& !"".equals(attributesToMatch)) {
        	deleteParameters.getAttributesToMatch().addAll(IniManagerRegistry.parseCommaSeparated(attributesToMatch));
        }
        return deleteParameters;
    }
}
