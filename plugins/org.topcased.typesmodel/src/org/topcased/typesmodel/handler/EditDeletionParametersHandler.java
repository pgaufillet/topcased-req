/***********************************************************************************************************************
 * Copyright (c) 2012 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mathieu VELTEN (Atos) - initial API and implementation
 * 			     Matthieu BOIVINEAU (Atos) - modification of the deletion parameters edition dialog, now mabaged in the IniManagerRegistry 
 * 
 **********************************************************************************************************************/
package org.topcased.typesmodel.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;


public class EditDeletionParametersHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		if (sel instanceof StructuredSelection) {
			Object selectedElem = ((StructuredSelection)sel).getFirstElement();
			if (selectedElem instanceof IFile && "types".equals(((IFile)selectedElem).getFileExtension())) {
				IFile typesFile = (IFile)selectedElem;
				IniManagerRegistry.openDeletionParametersEditionDialog(typesFile, true);
				
			}
		}
		
		return null;
	}
}
