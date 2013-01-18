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
package org.topcased.requirement.reqif.ui.reqif2topcased;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.rmf.reqif10.ReqIF;
import org.eclipse.rmf.reqif10.ReqIFContent;
import org.eclipse.rmf.reqif10.SpecHierarchy;
import org.eclipse.rmf.reqif10.Specification;
import org.eclipse.rmf.serialization.ReqIFResourceSetImpl;
import org.topcased.requirement.reqif.common.Transformer;
import org.topcased.requirement.reqif.reqif2topcased.ReqIf2RequirementProject;
import org.topcased.requirement.reqif.ui.common.AbstractImportExportWizard;
import org.topcased.requirement.reqif.utils.Messages;

public class ImportRequirementWizard extends AbstractImportExportWizard{

	
	public ImportRequirementWizard() {
		setWindowTitle(Messages.ImportRequirementWizard_REQIF_TOPCASED);
	}

	@Override
	public void addPages() {
		addPage(new ImportRequirementWizardPage(getInputURI()));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Transformer getTransformer() {
		return new ReqIf2RequirementProject(((ImportRequirementWizardPage)getPages()[0]).getDescriptionText());
	}

	@Override
	public ResourceSet getResourceSet() {
		return new ReqIFResourceSetImpl();
	}

	@Override
	protected int getElementNb(EObject object) {
		int result = 0;
		if (object instanceof ReqIF) {
			ReqIF reqIf = (ReqIF) object;
			ReqIFContent coreContent = reqIf.getCoreContent();
			EList<Specification> specifications = coreContent.getSpecifications();
			for (Specification specification : specifications) {
				EList<SpecHierarchy> specHierarchies = specification.getChildren();
				result += getAllChildrenNb(specHierarchies);
			}
		}
		return result;
	}
	
	private int getAllChildrenNb(Collection<SpecHierarchy> specHierarchies) {
		int result = 0;
		for (SpecHierarchy specHierarchy : specHierarchies) {
			result += getAllChildrenNb(specHierarchy.getChildren());
		}
		return result + specHierarchies.size();
	}

	@Override
	protected String getProgressMsg() {
		return "Import ReqIF Requirement";
	}
}
