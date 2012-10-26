/***********************************************************************************************************************
 * Copyright (c) 2012 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Matthieu BOIVINEAU (Atos) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.typesmodel.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * This class manages a popup that enable the user to enter 2 strings (attributeName and attributeRegex)
 * in order to fill deletion parameters.
 */
public class PopupAttributesDialog extends Dialog {

	/** The sec. */
    protected Section sec;

    /** The form. */
    protected Form form;
	
    protected String attributeName;
    protected String attributeRegex;
    protected Text txtAttributeName;
    protected Text txtAttributeRegex;
    
	protected PopupAttributesDialog(Shell parentShell) {
		super(parentShell);
		this.attributeName = null;
		this.attributeRegex = null;
	}
	
	protected PopupAttributesDialog(Shell parentShell, String attributeName, String attributeRegex) {
		super(parentShell);
		this.attributeName = attributeName;
		this.attributeRegex = attributeRegex;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("New Regex"); //$NON-NLS-1$

        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(new FillLayout());
        
        FormToolkit toolkit = new FormToolkit(Display.getDefault());
        form = toolkit.createForm(composite);
        form.getBody().setLayout(new GridLayout());
        toolkit.decorateFormHeading(form);

        sec = toolkit.createSection(form.getBody(), Section.TITLE_BAR);
        sec.setText("Attribute"); //$NON-NLS-1$
        sec.setLayoutData(new GridData(SWT.FILL, SWT.Expand, true, false, 1, 1));
        Composite compo = toolkit.createComposite(sec);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        compo.setLayout(layout);
        sec.setClient(compo);
       
        Label lblAttributeName = new Label(compo, SWT.NONE);
        lblAttributeName.setText("Name:");
        
        txtAttributeName = new Text(compo, SWT.BORDER);
        if(attributeName == null)
        {
        	txtAttributeName.setText("");
        }
        else
        {
        	txtAttributeName.setText(attributeName);
        }
        txtAttributeName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        txtAttributeName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				attributeName = txtAttributeName.getText();
			}
		});
        
        Label lblAttributeDescription = new Label(compo, SWT.NONE);
        lblAttributeDescription.setText("Regex:");
        
        txtAttributeRegex = new Text(compo, SWT.BORDER);
        if(attributeRegex == null)
        {
        	txtAttributeRegex.setText("");
        }
        else
        {
        	txtAttributeRegex.setText(attributeRegex);
        }
        txtAttributeRegex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        txtAttributeRegex.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				attributeRegex = txtAttributeRegex.getText();
			}
		});
        
        
        return composite;
	}
	
	public String getAttributesName(){
		return attributeName;
	}
	
	public String getAttributesRegex(){
		return attributeRegex;
	}
}
