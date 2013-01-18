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
package org.topcased.requirement.reqif.reqif2topcased;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.rmf.reqif10.AttributeDefinition;
import org.eclipse.rmf.reqif10.ReqIF;
import org.eclipse.rmf.reqif10.ReqIFContent;
import org.eclipse.rmf.reqif10.SpecType;
import org.eclipse.rmf.reqif10.Specification;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributesType;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.reqif.common.Transformer;
import org.topcased.requirement.reqif.utils.CreationManager;

import ttm.Document;

public class ReqIf2RequirementProject extends Transformer<RequirementProject, ReqIF> {

	private String descriptionText;

	public ReqIf2RequirementProject(String descriptionText) {
		this.descriptionText = descriptionText;
		CreationManager.INSTANCE.setDescriptionText(descriptionText);
	}

	@Override
	public RequirementProject transform(ReqIF input, IProgressMonitor monitor) {
		RequirementProject reqProject = RequirementFactory.eINSTANCE.createRequirementProject();
		UpstreamModel upstreamModel = RequirementFactory.eINSTANCE.createUpstreamModel();
		ReqIFContent coreContent = input.getCoreContent();
		
		//AttributeConfiguration
		EList<SpecType> specTypes = coreContent.getSpecTypes();
		for (SpecType specType : specTypes) {
			EList<AttributeDefinition> specAttributes = specType.getSpecAttributes();
			for (AttributeDefinition attributeDefinition : specAttributes) {
				if (!attributeDefinition.getLongName().equals(descriptionText)) {
					CreationManager.INSTANCE.addAttributesDefinitionName(attributeDefinition.getLongName());
				}
			}
		}
		AttributeConfiguration attributeConfiguration = RequirementFactory.eINSTANCE.createAttributeConfiguration();
		for (String attDefName : CreationManager.INSTANCE.getAttributesDefinitionName()) {
			ConfiguratedAttribute configuratedAttribute = RequirementFactory.eINSTANCE.createConfiguratedAttribute();
			configuratedAttribute.setName(attDefName);
			configuratedAttribute.setType(AttributesType.TEXT);
			attributeConfiguration.getListAttributes().add(configuratedAttribute);
		}
		reqProject.setAttributeConfiguration(attributeConfiguration);
		
		EList<Specification> specifications = coreContent.getSpecifications();
		for (Specification specification : specifications) {
			Document document = new Specification2Document().transform(specification, monitor);
			upstreamModel.getDocuments().add(document);
		}
		reqProject.setUpstreamModel(upstreamModel);
		
		return reqProject;
	}

}
