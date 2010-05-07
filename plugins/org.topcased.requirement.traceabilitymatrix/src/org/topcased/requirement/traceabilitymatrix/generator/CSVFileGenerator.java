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
package org.topcased.requirement.traceabilitymatrix.generator;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.mwe.core.WorkflowRunner;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.issues.IssuesImpl;
import org.topcased.requirement.traceabilitymatrix.Activator;


/**
 * Launch an Modeling Workflow Engine.
 */
public class CSVFileGenerator {

	private static final String WORKFLOW_PATH = "org/topcased/requirement/traceabilitymatrix/generator/workflow/workflow.mwe";
	// generation source model
	private final String modelPath;

	// generation output folder
	private final String outputFolder;

	/**
	 * Constructor
	 * 
	 * @param outputFolder
	 *            generation output folder
	 * @param modelPath
	 *            generation source model
	 */
	public CSVFileGenerator(final String outputFolder, final String modelPath) {
		this.outputFolder = outputFolder;
		this.modelPath = modelPath;
	}

	/**
	 * Launch the OpenArchitectureWare workflow
	 */
	public void generate() throws Exception {
		// get the workflow
		final String workflowFile = Platform.getBundle(Activator.PLUGIN_ID)
				.getResource(WORKFLOW_PATH).getPath();

		// launch the workflow
		final WorkflowRunner wf = new WorkflowRunner();
		final boolean ok1 = wf.prepare(workflowFile, null, getProperties());
		final Issues issues = new IssuesImpl();
		final boolean ok = wf.executeWorkflow(null, issues);
		if (!ok) {
			String lError = ".";
			if (issues.hasErrors()) {
				lError = ": " + issues.getErrors()[0].getMessage();
			}
			throw new Exception("Error while exporting requirement traceability"
					+ lError);

		}

		// refresh the workspace
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		root.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
	}

	/**
	 * Generate a file name like "<root> (<index>).xls" or "<root>.xls" if
	 * index=0
	 * 
	 * @param root
	 *            see method description
	 * @param index
	 *            see method description
	 * @return see method description
	 */
	private String generateIfpugExportFileName(final String root,
			final int index) {
		if (index == 0) {
			return root + ".xls";
		} else {
			return root + " (" + index + ").xls";
		}
	}

	/**
	 * Gets the properties.
	 * 
	 * @return the properties
	 */
	private Map<String, String> getProperties() {
		final Map<String, String> properties = new HashMap<String, String>();
		properties.put("target", outputFolder);
		properties.put("model", "file:/" + modelPath.replaceAll("\\\\", "/"));

		// ////////////////////
		// compute fileName //
		// ////////////////////

		final File outputFolderFile = new File(outputFolder);
		final File modelPathFile = new File(modelPath);

		final String root = modelPathFile.getName().substring(0,
				modelPathFile.getName().length() - 12);
		int index = 0;
		boolean indexOk = false;

		// getting existing ifpug exports
		final List<String> existingFiles = Arrays.asList(outputFolderFile
				.list());

		// computing the index
		while (!indexOk) {
			if (existingFiles
					.contains(generateIfpugExportFileName(root, index))) {
				index++;
			} else {
				indexOk = true;
			}
		}

		properties.put("fileName", generateIfpugExportFileName(root, index));

		return properties;
	}

}
