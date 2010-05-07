/*****************************************************************************
 * Copyright (c) 2009 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  bmarcona (Atos Origin) benjamin.marconato@atosorigin.com - Initial API and implementation
 *  nsamson (Atos Origin) nicolas.samson@atosorigin.com - Adaptation for Ifplug export plugin
 *
  *****************************************************************************/
package org.topcased.requirement.traceabilitymatrix.wizard;

import java.io.File;
import java.net.URI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;

/**
 * This listener can be used to define a browse button associated to a text field.
 * It can do a file browse or a folder browse. Files can be filtered by extensions.
 * 
 * After user selection, text field is updated and its listeners are notified of the modification.
 */
public class BrowseButtonMouseListener extends MouseAdapter {

	public enum TYPE {
		FILE, FOLDER
	}
	
	// browse type
	private TYPE type;
	// filtered extensions (used for FILE type)
	private String[] extensions;
	// associated text field
	private Text text;
	// current workbench
	private IWorkbench workbench;
	
	/**
	 * Constructor
	 * 
	 * @param type browse type
	 * @param extensions filtered extensions for FILE type (can be null, even for FILE)
	 * @param text associated text field
	 * @param workbench current workbench
	 */
	public BrowseButtonMouseListener(TYPE type, String[] extensions, Text text, IWorkbench workbench) {
		this.type = type;
		this.extensions = extensions;
		this.text = text;
		this.workbench = workbench;
	}
	
	/**
	 * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
		switch (type) {
		case FILE :
			// search for a file
			FileDialog fileDialog = new FileDialog(workbench.getDisplay().getActiveShell());
			fileDialog.setFilterExtensions(extensions);
			// default value
			try {
				File defaultFile = new File(new URI(text.getText()));
				if (defaultFile.isFile()) {
					fileDialog.setFileName(defaultFile.getAbsolutePath());
				}
			} catch (Exception ex) {
				// do nothing => no initial value
			}
			// selected value
			String selectedFile = fileDialog.open();
			if (selectedFile != null) {
				text.setText(new File(selectedFile).toURI().toString());
				text.notifyListeners(SWT.Modify, new Event());
			}
			break;
		case FOLDER :
			// search for a folder
			DirectoryDialog directoryDialog = new DirectoryDialog(workbench.getDisplay().getActiveShell());
			// default value
			try {
				File defaultDirectory = new File(new URI(text.getText()));
				if (defaultDirectory.isDirectory()) {
					directoryDialog.setFilterPath(defaultDirectory.getAbsolutePath());
				}
			} catch (Exception ex) {
				// do nothing => no initial value
			}
			// selected value
			String selectedDirectory = directoryDialog.open();
			if (selectedDirectory != null) {
				text.setText(new File(selectedDirectory).toURI().toString());
				text.notifyListeners(SWT.Modify, new Event());
			}
			break;
		}
	}
	
}
