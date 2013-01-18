/*****************************************************************************
 * Copyright (c) 2012 Atos
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Anass RADOUANI (Atos) anass.radouani@atos.net - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.reqif.ui.reqif2topcased;

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.topcased.requirement.reqif.Activator;
import org.topcased.requirement.reqif.ui.common.AbstractImportExportPage;
import org.topcased.requirement.reqif.utils.Messages;

public class ImportRequirementWizardPage extends AbstractImportExportPage {


	private String descriptionText = ""; //$NON-NLS-1$
	
	private Text descriptionName;
	
	private static String DESC_PREFERENCE = "desc_preference";
	
	public String getDescriptionText() {
		return descriptionText;
	}

	protected ImportRequirementWizardPage(URI inputURI) {
		super(Messages.IMPORT_REQIF, Messages.REQIF2TOPCASED_REQ, Messages.IMPORT_REQIF2TOPCASED, inputURI);
	}

	@Override
	protected String getOutputErrorMsg() {
		return Messages.TOPCASED_FILE_ERROR;
	}
	
	@Override
	protected String getInputErrorMsg() {
		return Messages.REQIF_FILE_ERROR;
	}

	@Override
	protected String getImportLabel() {
		return Messages.REQIF_FILE;
	}

	@Override
	protected String getSaveAsLabel() {
		return Messages.TOPCASED_REF_FILE_ERROR;
	}

	@Override
	protected String getInputFileExtension() {
		return Messages.REQIF_EXT;
	}

	@Override
	protected String getOutputFileExtension() {
		return Messages.REQ_EXT;
	}

	@Override
	protected void createAdditionalContent(FormToolkit formToolkit,
			Composite composite) {
		
		Label textLabel = new Label(composite, SWT.NONE);
		textLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(textLabel, true, true);
		textLabel.setText(Messages.DESC);
		
		descriptionName = new Text(composite, SWT.BORDER);
		descriptionName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		formToolkit.adapt(descriptionName, true, true);
		descriptionName.setText(descriptionText);
		initDesc();
		
		descriptionName.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent e) {
				descriptionText = descriptionName.getText();
				Activator.getDefault().getPreferenceStore().putValue(DESC_PREFERENCE, descriptionText);
			}
		});
	}
	
	private void initDesc(){
		if (Activator.getDefault().getPreferenceStore().contains(DESC_PREFERENCE)) {
			descriptionText = Activator.getDefault().getPreferenceStore().getString(DESC_PREFERENCE);
			descriptionName.setText(descriptionText);
			
		}
	}
	
}
