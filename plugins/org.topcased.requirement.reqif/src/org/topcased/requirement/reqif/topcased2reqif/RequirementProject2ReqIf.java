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
package org.topcased.requirement.reqif.topcased2reqif;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.rmf.reqif10.DatatypeDefinitionBoolean;
import org.eclipse.rmf.reqif10.DatatypeDefinitionInteger;
import org.eclipse.rmf.reqif10.DatatypeDefinitionSimple;
import org.eclipse.rmf.reqif10.DatatypeDefinitionString;
import org.eclipse.rmf.reqif10.ReqIF;
import org.eclipse.rmf.reqif10.ReqIF10Factory;
import org.eclipse.rmf.reqif10.ReqIFContent;
import org.eclipse.rmf.reqif10.ReqIFHeader;
import org.eclipse.rmf.reqif10.SpecHierarchy;
import org.eclipse.rmf.reqif10.SpecObject;
import org.eclipse.rmf.reqif10.Specification;
import org.eclipse.rmf.reqif10.pror.configuration.Column;
import org.eclipse.rmf.reqif10.pror.configuration.ConfigurationFactory;
import org.eclipse.rmf.reqif10.pror.configuration.LabelConfiguration;
import org.eclipse.rmf.reqif10.pror.configuration.ProrGeneralConfiguration;
import org.eclipse.rmf.reqif10.pror.configuration.ProrSpecViewConfiguration;
import org.eclipse.rmf.reqif10.pror.configuration.ProrToolExtension;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.reqif.common.Transformer;
import org.topcased.requirement.reqif.utils.CreationManager;
import org.topcased.requirement.reqif.utils.Messages;

import ttm.Document;
import ttm.HierarchicalElement;
import ttm.Requirement;
import ttm.Section;

public class RequirementProject2ReqIf extends Transformer<ReqIF, RequirementProject> {

	private boolean proRExtension = true;

	public RequirementProject2ReqIf(boolean proRExtension) {
		this.proRExtension = proRExtension;
	}
	
	@Override
	public ReqIF transform(RequirementProject input, IProgressMonitor monitor) {
		
		ReqIF reqIf = ReqIF10Factory.eINSTANCE.createReqIF();
		
		ReqIFContent reqIFContent = ReqIF10Factory.eINSTANCE.createReqIFContent();
		reqIf.setCoreContent(reqIFContent);

		ReqIFHeader reqIfHeader = ReqIF10Factory.eINSTANCE.createReqIFHeader();
		reqIf.setTheHeader(reqIfHeader);

		reqIFContent.getDatatypes().addAll(createDataTypes(reqIFContent));
		
		UpstreamModel upstreamModel = input.getUpstreamModel();
		
		EList<Document> documents = upstreamModel.getDocuments();
		
		for (Document document : documents) {

			Specification specification = new Document2Specification().transform(document, monitor);
			reqIFContent.getSpecifications().add(specification);

			EList<HierarchicalElement> attributes = document.getChildren();

			for (HierarchicalElement hierarchicalElement : attributes) {
				if (hierarchicalElement instanceof Section) {
					Section section = (Section) hierarchicalElement;
					SpecHierarchy specHierarchy = new Section2SpecHierarchy().transform(section, monitor);
					specification.getChildren().add(specHierarchy);
				}
				else if (hierarchicalElement instanceof Requirement){
					Requirement requirement = (Requirement) hierarchicalElement;
					SpecHierarchy specHierarchy = new Requirement2SpecHierarchy().transform(requirement, monitor);
					specification.getChildren().add(specHierarchy);
				}
			}
			reqIFContent.getSpecObjects().addAll((Collection<? extends SpecObject>) CreationManager.INSTANCE.getSpecObject());
			CreationManager.INSTANCE.clearListSpecObject();
		}
		if (proRExtension) {
			reqIf.getToolExtensions().add(createViewConfiguration(reqIFContent.getSpecifications()));
		}
		reqIFContent.getSpecTypes().addAll(CreationManager.INSTANCE.getSpecTypes().values());
		CreationManager.INSTANCE.clearListSpecTypes();
		return reqIf;
	}

	private ProrToolExtension createViewConfiguration(EList<Specification> eList) {
		ProrToolExtension prorToolExtension = ConfigurationFactory.eINSTANCE.createProrToolExtension();
		for (Specification specification : eList) {
			
			ProrSpecViewConfiguration prorSpecViewConfiguration = ConfigurationFactory.eINSTANCE.createProrSpecViewConfiguration();
			prorSpecViewConfiguration.setSpecification(specification);
			prorToolExtension.getSpecViewConfigurations().add(prorSpecViewConfiguration);
			
			Column columnIdent = ConfigurationFactory.eINSTANCE.createColumn();
			columnIdent.setLabel(Messages.IDENT);
			prorSpecViewConfiguration.getColumns().add(columnIdent);
			
			Column columnDesc = ConfigurationFactory.eINSTANCE.createColumn();
			columnDesc.setLabel(Messages.DESCRIPTION);
			prorSpecViewConfiguration.getColumns().add(columnDesc);
			
			Column columnLeft = ConfigurationFactory.eINSTANCE.createColumn();
			columnLeft.setLabel(Messages.LEADHEADERCOLUMN);
			prorSpecViewConfiguration.setLeftHeaderColumn(columnLeft);
			
			
			ProrGeneralConfiguration prorGeneralConfiguration = ConfigurationFactory.eINSTANCE.createProrGeneralConfiguration();
			prorToolExtension.setGeneralConfiguration(prorGeneralConfiguration);
			
			LabelConfiguration labelConfiguration = ConfigurationFactory.eINSTANCE.createLabelConfiguration();
			labelConfiguration.getDefaultLabel().add(Messages.DESCRIPTION);
			prorGeneralConfiguration.setLabelConfiguration(labelConfiguration);
		}

		return prorToolExtension;
	}

	private List<DatatypeDefinitionSimple> createDataTypes(ReqIFContent reqIFContent) {
		DatatypeDefinitionBoolean dtdb = ReqIF10Factory.eINSTANCE.createDatatypeDefinitionBoolean();
		DatatypeDefinitionInteger dtdi = ReqIF10Factory.eINSTANCE.createDatatypeDefinitionInteger();
		DatatypeDefinitionString dtds = ReqIF10Factory.eINSTANCE.createDatatypeDefinitionString();
		List<DatatypeDefinitionSimple> result = Arrays.asList(dtdb,dtdi,dtds);
		CreationManager.INSTANCE.setDataTypes(result);
		return result;
	}
	
}
