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
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.topcased.requirement.traceabilitymatrix.Activator;
import org.topcased.requirement.traceabilitymatrix.generator.CSVFileGenerator;


/**
 * Job to schedule the export.
 */
public class ExportJob extends Job {

	// model to export (URI)
	private final String modelUri;
	// output directory for the export (URI)
	private final String outputUri;

	/**
	 * Constructor
	 * 
	 * @param modelUri
	 *            model to export (URI)
	 * @param outputUri
	 *            output directory for the export (URI)
	 */
	public ExportJob(final String modelUri, final String outputUri) {
		super("");
		final String[] tab = modelUri.split("/");
		setName("Requirement traceability export for " + tab[tab.length - 1]);
		this.modelUri = modelUri;
		this.outputUri = outputUri;
	}

	/**
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(final IProgressMonitor monitor) {
		try {
			// convert URI to absolute path
			final String modelPath = new File(new URI(modelUri)).getPath();
			final String outputPath = new File(new URI(outputUri)).getPath();

			// OAW generation
			final CSVFileGenerator generator = new CSVFileGenerator(outputPath,
					modelPath);
			generator.generate();
		} catch (final Exception e) {
			final Status lStatus = new Status(IStatus.ERROR,
					Activator.PLUGIN_ID,
					"Requirement traceability export failed.", e);
			Activator.getDefault().getLog().log(lStatus);
			return lStatus;
		}

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageDialog
						.openInformation(null, "Traceability export",
								"Requirement traceability has been exported successfully.");
			}
		});

		return new Status(IStatus.OK, Activator.PLUGIN_ID,
				"Requirement traceability has been exported.");
	}

}
