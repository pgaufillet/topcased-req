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
 *  Nicolas SAMSON (ATOS ORIGIN INTEGRATION) nicolas.samson@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.traceabilitymatrix.wizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.topcased.requirement.traceabilitymatrix.Activator;


/**
 * Main page of the wizard.
 */
public class ExportWizardPage extends WizardPage {

	private final Color LIGHT_RED = new Color(Display.getCurrent(), 255, 128,
			128);
	// model value
	private String model;
	private Text modelText;
	// output value
	private String output;
	private Text outputText;

	private final Color WHITE = new Color(Display.getCurrent(), 255, 255, 255);
	// current workbench
	private final IWorkbench workbench;

	/**
	 * Constructor.
	 * 
	 * @param pageName
	 *            page name
	 * @param defaultModel
	 *            default model to display (can be null)
	 * @param defaultOutput
	 *            default output to display (can be null)
	 * @param workbench
	 *            current workbench
	 */
	public ExportWizardPage(final String pageName, final String defaultModel,
			final String defaultOutput, final IWorkbench workbench) {
		super(pageName);
		final IPreferenceStore preferences = Activator.getDefault()
				.getPreferenceStore();
		if (defaultModel != null) {
			model = defaultModel;
		} else {
			model = preferences.getString("modelField");
		}
		if (defaultOutput != null) {
			output = defaultOutput;
		} else {
			output = preferences.getString("outputField");
		}
		this.workbench = workbench;
	}

	/**
	 * Overrides createControl.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(final Composite parent) {
		// general information
		setTitle("Requirement traceability export");
		setDescription("Requirement traceability exported to an excel file..");
		// image
		final URL image = Platform.getBundle(Activator.PLUGIN_ID).getResource(
				"icons/bricks.ico");
		setImageDescriptor(ImageDescriptor.createFromURL(image));

		// create top component
		final Composite top = new Composite(parent, SWT.NONE);
		top.setLayout(new GridLayout(3, false));

		// create fields
		final Label modelLabel = new Label(top, SWT.NONE);
		modelText = new Text(top, SWT.BORDER);
		final Button modelButton = new Button(top, SWT.NONE);
		final Label outputLabel = new Label(top, SWT.NONE);
		outputText = new Text(top, SWT.BORDER);
		final Button outputButton = new Button(top, SWT.NONE);

		// layout data
		modelLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false));
		outputLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false));
		modelText
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		outputText
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		modelButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		outputButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));

		// settings
		modelLabel.setText("Requirements file: ");
		outputLabel.setText("Output path: ");
		if (model != null) {
			modelText.setText(model);
		}
		if (output != null) {
			outputText.setText(output);
		}
		modelButton.setText("Browse...");
		outputButton.setText("Browse...");

		// listeners
		modelText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				model = ((Text) e.getSource()).getText();
				modelText.setBackground(WHITE);
				setErrorMessage(null);
				getWizard().getContainer().updateButtons();
			}
		});
		outputText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				output = ((Text) e.getSource()).getText();
				outputText.setBackground(WHITE);
				setErrorMessage(null);
				getWizard().getContainer().updateButtons();
			}
		});
		modelButton.addMouseListener(new BrowseButtonMouseListener(
				BrowseButtonMouseListener.TYPE.FILE,
				new String[] { "*.requirement" }, modelText, workbench));
		outputButton.addMouseListener(new BrowseButtonMouseListener(
				BrowseButtonMouseListener.TYPE.FOLDER, null, outputText,
				workbench));

		// display !
		setControl(top);
	}

	/**
	 * Overrides isPageComplete.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete() {
		// missing fields
		if (model == null || model.length() == 0) {
			modelText.setBackground(LIGHT_RED);
			return false;
		}
		if (output == null || output.length() == 0) {
			outputText.setBackground(LIGHT_RED);
			return false;
		}
		// extensions
		if (!model.endsWith(".requirement")) {
			modelText.setBackground(LIGHT_RED);
			return false;
		}
		return true;
	}

	/**
	 * Schedule the export job.
	 */
	public void performProcess() {
		// save the fields
		final IPreferenceStore preferences = Activator.getDefault()
				.getPreferenceStore();
		preferences.setValue("modelField", model);
		preferences.setValue("outputField", output);
		// schedule the export
		final ExportJob job = new ExportJob(model, output);
		job.setUser(true);
		job.schedule();
	}

	/**
	 * Validate if model file and output directory exist .
	 * 
	 * @return true if ok
	 */
	public boolean validate() {
		// model file exists and is a file ?
		try {
			final File modelFile = new File(new URI(model));
			if (!modelFile.exists() || !modelFile.isFile()) {
				throw new FileNotFoundException();
			}
		} catch (final Exception e) {
			modelText.setBackground(LIGHT_RED);
			setErrorMessage("Requirement file does not exist.");
			return false;
		}
		// output directory exists and is a directory ?
		try {
			final File outputDirectory = new File(new URI(output));
			if (!outputDirectory.exists() || !outputDirectory.isDirectory()) {
				throw new FileNotFoundException();
			}
		} catch (final Exception e) {
			outputText.setBackground(LIGHT_RED);
			setErrorMessage("Output path does not exist.");
			return false;
		}
		return true;
	}

}
