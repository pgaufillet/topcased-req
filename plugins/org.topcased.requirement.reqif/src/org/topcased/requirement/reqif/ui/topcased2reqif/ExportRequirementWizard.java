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
package org.topcased.requirement.reqif.ui.topcased2reqif;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.reqif.common.Transformer;
import org.topcased.requirement.reqif.topcased2reqif.RequirementProject2ReqIf;
import org.topcased.requirement.reqif.ui.common.AbstractImportExportWizard;
import org.topcased.requirement.reqif.utils.Messages;

import ttm.Document;
import ttm.HierarchicalElement;

public class ExportRequirementWizard extends AbstractImportExportWizard{

	public ExportRequirementWizard() {
		setWindowTitle(Messages.EXPORT_UPSTREAM);
	}

	@Override
	public void addPages() {
		addPage(new ExportRequirementWizardPage(getInputURI()));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Transformer getTransformer() {
		return new RequirementProject2ReqIf(((ExportRequirementWizardPage)getPages()[0]).isProRExtension());
	}

	@Override
	protected int getElementNb(EObject object) {
		int result = 0;
		if (object instanceof RequirementProject) {
			RequirementProject reqProject = (RequirementProject) object;
			EList<Document> documents = reqProject.getUpstreamModel().getDocuments();
			for (Document document : documents) {
				EList<HierarchicalElement> hierarchicalElements = document.getChildren();
				result += getAllChildrenNb(hierarchicalElements);
			}
		}
		return result;
	}

	private int getAllChildrenNb(Collection<HierarchicalElement> hierarchicalElements) {
		int result = 0;
		for (HierarchicalElement hierarchicalElement : hierarchicalElements) {
			result += getAllChildrenNb(hierarchicalElement.getChildren());
		}
		return result + hierarchicalElements.size();
	}

	@Override
	protected String getProgressMsg() {
		
		return "Export Upstream Requirement";
	}
}
