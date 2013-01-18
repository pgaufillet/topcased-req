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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.topcased.requirement.reqif.common.Transformer;

public abstract class AbstractImportExportWizard extends Wizard implements
		IWorkbenchWizard {
	private String outputURI;
	private URI inputUri;



	@Override
	public final boolean performFinish() {
		try {
			getContainer().run(true, false, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					// open source
					Resource resource = loadResource();

					// get contents get (0) de la resource
					EList<EObject> contents = resource.getContents();
					if (contents.size() > 0) {
						EObject object = contents.get(0);

						//get Element number
						int elementNb = getElementNb(object);
						
						monitor.beginTask(getProgressMsg(), elementNb+1);
						
						// get transformer from page
						Transformer<?, EObject> t = getTransformer();

						// launch transformer
						EObject result = (EObject) t.transform(object, monitor);

						// target resource get contents add de result du
						// transformer

						ResourceSet resourceSet = resource.getResourceSet();
						Resource outputResource = resourceSet.createResource(URI.createURI(outputURI));
						outputResource.getContents().add(result);
						
						// save
						try {
							outputResource.save(Collections.EMPTY_MAP);
						} catch (IOException e) {
							e.printStackTrace();
						}
						monitor.worked(1);
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public ResourceSet getResourceSet(){
		return new ResourceSetImpl();
	}
	
	protected abstract int getElementNb(EObject object);
	
	public abstract Transformer<?, EObject> getTransformer();

	private Resource loadResource() {
		IWizardPage[] pages = getPages();
		if (pages.length > 0) {
			if (pages[0] instanceof AbstractImportExportPage) {
				AbstractImportExportPage page = (AbstractImportExportPage) pages[0];
				
				outputURI = page.getOutputURIText();
				
				String inputURIText = page.getInputURIText();
				URI inputURI = URI.createURI(inputURIText);
				return getResourceSet().getResource(inputURI, true);
			}
		}
		return null;
	}

	protected abstract String getProgressMsg();
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setNeedsProgressMonitor(true);
		if (selection instanceof TreeSelection && selection.getFirstElement() instanceof IFile) {
				this.inputUri = URI.createPlatformResourceURI(((IFile)selection.getFirstElement()).getFullPath().toString(),true);
				
		}
	}

	public URI getInputURI() {
		return inputUri;
	}

	
	
}
