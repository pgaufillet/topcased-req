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

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Main class of the wizard.
 */
public class ExportWizard extends Wizard implements IExportWizard {

	// wizard page
	private ExportWizardPage mainPage;

	/**
	 * Constructor.
	 */
	public ExportWizard() {
	}

	/**
	 * Overrides addPages.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		addPage(mainPage);
	}

	/**
	 * Overrides canFinish.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		if (mainPage != null) {
			return mainPage.isPageComplete();
		}
		return false;
	}

	/**
	 * Overrides init.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(final IWorkbench workbench,
			final IStructuredSelection selection) {
		// general information
		setWindowTitle("Requirement traceability export");

		// user selected a file => considered as the model
		String defaultModel = null;
		if (selection.getFirstElement() instanceof IFile) {
			final IFile file = (IFile) selection.getFirstElement();
			if ("requirement".equals(file.getFileExtension())) {
				defaultModel = file.getLocationURI().toString();
			}
		}

		mainPage = new ExportWizardPage("ExportWizardPage", defaultModel, null,
				workbench);
	}

	/**
	 * Overrides performFinish.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		if (mainPage.validate()) {
			// do the export !
			mainPage.performProcess();
			return true;
		}
		return false;
	}

}
