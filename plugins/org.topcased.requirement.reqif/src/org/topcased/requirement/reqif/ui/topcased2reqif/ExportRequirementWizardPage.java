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
package org.topcased.requirement.reqif.ui.topcased2reqif;

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.topcased.requirement.reqif.ui.common.AbstractImportExportPage;
import org.topcased.requirement.reqif.utils.Messages;

public class ExportRequirementWizardPage extends AbstractImportExportPage {


	protected boolean proRExtension = true;

	public boolean isProRExtension() {
		return proRExtension;
	}

	protected ExportRequirementWizardPage(URI inputURI) {
		super(Messages.EXPORT_UPSTREAM, Messages.TOPCASED2REQIF, Messages.EXPORT_UPSTREAM2REQIF, inputURI);
	}

	@Override
	protected String getInputErrorMsg() {
		return Messages.TOPCASED_FILE_ERROR;
	}

	@Override
	protected String getOutputErrorMsg() {
		return Messages.REQIF_FILE_ERROR;
	}
	
	@Override
	protected String getImportLabel() {
		return Messages.TOPCASED_INPUT_FILE;
	}

	@Override
	protected String getSaveAsLabel() {
		return Messages.REQIF_OUTPUT_FILE;
	}

	@Override
	protected String getOutputFileExtension() {
		return Messages.REQIF_EXT;
	}

	@Override
	protected String getInputFileExtension() {
		return Messages.REQ_EXT;
	}

	@Override
	protected void createAdditionalContent(FormToolkit formToolkit, Composite composite) {
		final Button proRCheckButton = new Button(composite, SWT.CHECK);
		proRCheckButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		formToolkit.adapt(proRCheckButton, true, true);
		proRCheckButton.setText(Messages.GEN_PROR_EXT);
		proRCheckButton.setSelection(proRExtension);
		proRCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				proRExtension = proRCheckButton.getSelection();
			}
		});
	}

	
	
}
