/***********************************************************************************************************************
 * Copyright (c) 2012 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mathieu VELTEN (Atos) - initial API and implementation
 * 				 Matthieu BOIVINEAU (Atos) - dialog updated according to the new composite
 * 
 **********************************************************************************************************************/
package org.topcased.typesmodel.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.topcased.typesmodel.Messages;
import org.topcased.typesmodel.model.inittypes.DeletionParameters;


public class DeletionParametersDialog extends Dialog {

	private DeletionParametersComposite deletionParametersComposite;
	private DeletionParameters deletionParameters;

	public DeletionParametersDialog(Shell shell, DeletionParameters deletionParameters) {
		super(shell);
		this.deletionParameters = deletionParameters;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite)super.createDialogArea(parent);
		getShell().setText(Messages.DeletionParametersDialog_Title);
		deletionParametersComposite = new DeletionParametersComposite(composite, deletionParameters);
        
        return composite;
	}
	
	@Override
	protected void okPressed() {
		deletionParameters = deletionParametersComposite.getDeletionParameters();
		super.okPressed();
	}

    public DeletionParameters getDeletionParameters() {
        return deletionParameters;
    }
}
