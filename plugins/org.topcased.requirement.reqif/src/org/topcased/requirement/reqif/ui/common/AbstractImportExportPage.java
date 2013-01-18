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
package org.topcased.requirement.reqif.ui.common;

import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.topcased.requirement.reqif.utils.Messages;

public abstract class AbstractImportExportPage extends WizardPage {
	
	private URI inputURI;

	/**
	 * Create the wizard.
	 * @param inputUri 
	 */
	protected AbstractImportExportPage(String pageName, String title, String description, URI inputURI) {
		super(pageName);
		setTitle(title);
		setDescription(description);
		this.inputURI = inputURI;
	}
		

	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	
	private Text inputText;
	
	private Text outputText;
	
	private Button buttonInput;
	
	private Button buttonOutput;

	private Label inputLabel;

	private Label outputLabel;

	private Section section;

	private String inputURIText;
	
	private String outputURIText;
	
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ScrolledForm scrldfrmNewScrolledform = formToolkit.createScrolledForm(container);
		formToolkit.paintBordersFor(scrldfrmNewScrolledform);
		FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
		fillLayout.marginHeight = 5;
		fillLayout.marginWidth = 5;
		scrldfrmNewScrolledform.getBody().setLayout(fillLayout);
		
		section = formToolkit.createSection(scrldfrmNewScrolledform.getBody(), Section.TITLE_BAR);
		formToolkit.paintBordersFor(section);
		
		Composite composite = new Composite(section, SWT.NONE);
		
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		inputLabel = new Label(composite, SWT.NONE);
		inputLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		inputLabel.setText(getImportLabel());
		
		inputText = new Text(composite, SWT.BORDER);
		inputText.setEnabled(false);
		inputText.setEditable(false);
		inputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		
		buttonInput = formToolkit.createButton(composite, Messages.BROWSE, SWT.NONE);
		
		outputLabel = new Label(composite, SWT.NONE);
		outputLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		outputLabel.setText(getSaveAsLabel());
		
		outputText = new Text(composite, SWT.BORDER);
		outputText.setEnabled(false);
		outputText.setEditable(false);
		outputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		if (validInput(this.inputURI)) {
			initInput(inputURI);
			initOutput(inputURI.trimFileExtension().appendFileExtension(getOutputFileExtension()));
		}
		
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		
		buttonOutput = formToolkit.createButton(composite, Messages.BROWSE, SWT.NONE);
		
		createAdditionalContent(formToolkit, composite);
		
		hookListeners();
		
	}
	
	private boolean validInput(URI inputURI) {
		return inputURI != null && getInputFileExtension().equals(inputURI.fileExtension());
	}

	private void initOutput(URI output) {
		outputURIText = transformTextURI(output).toString();
		outputText.setText(outputURIText);
	}

	private void initInput(URI input) {
		inputURIText = input.toString();
		inputText.setText(inputURIText);
	}

	protected void createAdditionalContent(FormToolkit formToolkit, Composite composite)
	{
		
	}

	protected abstract String getImportLabel();

	protected abstract String getSaveAsLabel();

	private void hookListeners() {
		buttonInput.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResourceDialog resourceDialog = new ResourceDialog(getShell(), getImportLabel(), SWT.OPEN);
				if (resourceDialog.open() == ResourceDialog.OK)
                {
					if (resourceDialog.getURIs().size()>0 && validInput(resourceDialog.getURIs().get(0))) {
						initInput(resourceDialog.getURIs().get(0));
						initOutput(resourceDialog.getURIs().get(0).trimFileExtension().appendFileExtension(getOutputFileExtension()));
					}
                }
				getWizard().getContainer().updateButtons();
				getWizard().getContainer().updateMessage();
				getWizard().getContainer().updateTitleBar();

			}
		});
		
		buttonOutput.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResourceDialog resourceDialog = new ResourceDialog(getShell(), getSaveAsLabel(), SWT.SAVE);
				if (resourceDialog.open() == ResourceDialog.OK && resourceDialog.getURIs().size()>0 && resourceDialog.getURIs().get(0).segmentCount()>1 )
                {
					initOutput(resourceDialog.getURIs().get(0));
                }
				getWizard().getContainer().updateButtons();
				getWizard().getContainer().updateMessage();
				getWizard().getContainer().updateTitleBar();
			}
		});
	}
	
	protected URI transformTextURI(URI textURI){
		if (getOutputFileExtension().equals(textURI.fileExtension())) {
			return textURI;
		}
		return textURI.appendFileExtension(getOutputFileExtension());
	}
	
	protected abstract String getInputFileExtension();
	
	protected abstract String getOutputFileExtension();
	
	public String getInputURIText() {
		return inputURIText;
	}

	public String getOutputURIText() {
		return outputURIText;
	}
	
	@Override
	public boolean isPageComplete() {
		boolean result = true;
		StringBuffer error = new StringBuffer(""); //$NON-NLS-1$
		if (inputURIText == null || inputURIText.isEmpty() || inputURIText.endsWith("." + getOutputFileExtension())) {
			error.append(getInputErrorMsg());
			result = false;
		}
		
		if (outputURIText == null || outputURIText.isEmpty()) {
			error.append(getOutputErrorMsg());
			result = false;
		}
		if (result) {
			setErrorMessage(null);
		}else{
			setErrorMessage(error.toString());
		}
		
		return result;
	}
	
	protected abstract String getOutputErrorMsg();

	protected abstract String getInputErrorMsg();
	
}
