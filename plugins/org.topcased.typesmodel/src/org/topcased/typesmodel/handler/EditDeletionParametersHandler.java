/***********************************************************************************************************************
 * Copyright (c) 2012 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mathieu VELTEN (Atos) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.typesmodel.handler;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;
import org.topcased.typesmodel.model.inittypes.DeletionParameters;
import org.topcased.typesmodel.model.inittypes.DocumentType;
import org.topcased.typesmodel.ui.ComboInputDialog;
import org.topcased.typesmodel.ui.DeletionParametersDialog;


public class EditDeletionParametersHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		if (sel instanceof StructuredSelection) {
			Object selectedElem = ((StructuredSelection)sel).getFirstElement();
			if (selectedElem instanceof IFile && "types".equals(((IFile)selectedElem).getFileExtension())) {
				IFile typesFile = (IFile)selectedElem;
				Map<String, DocumentType> documentTypes = IniManagerRegistry.parseTypesFile(typesFile);
				Set<String> documentTypeNames = documentTypes.keySet();

				if (!documentTypeNames.isEmpty()) {
					ComboInputDialog selectDocTypeDialog = new ComboInputDialog(Display.getDefault().getActiveShell(), "Document type to edit", "Please select the document type you want to edit :", documentTypeNames.iterator().next(), documentTypeNames.toArray(new String[documentTypeNames.size()]));
					int res = selectDocTypeDialog.open();
					if (res == Dialog.OK) {
						DocumentType selectedDocumentType = documentTypes.get(selectDocTypeDialog.getValue());
						if (selectedDocumentType != null) {
							DeletionParametersDialog deletionParametersDialog = new DeletionParametersDialog(Display.getDefault().getActiveShell(), selectedDocumentType.getDeletionParameters());
							res = deletionParametersDialog.open();
							if (res == Dialog.OK) {
								DeletionParameters deletionParameters = deletionParametersDialog.getDeletionParameters();
								selectedDocumentType.setDeletionParameters(deletionParameters);
								IniManagerRegistry.save(typesFile, documentTypes.values());
								
								try {
									typesFile.refreshLocal(IResource.DEPTH_ZERO, null);
								} catch (CoreException e) {}
							}
						}
					}
				}
			}
		}
		
		return null;
	}
}
