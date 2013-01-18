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

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.rmf.reqif10.AttributeDefinition;
import org.eclipse.rmf.reqif10.AttributeDefinitionString;
import org.eclipse.rmf.reqif10.AttributeValueString;
import org.eclipse.rmf.reqif10.DatatypeDefinitionString;
import org.eclipse.rmf.reqif10.ReqIF10Factory;
import org.eclipse.rmf.reqif10.SpecHierarchy;
import org.eclipse.rmf.reqif10.SpecObject;
import org.eclipse.rmf.reqif10.SpecObjectType;
import org.topcased.requirement.reqif.common.Transformer;
import org.topcased.requirement.reqif.utils.CreationManager;
import org.topcased.requirement.reqif.utils.Messages;

import ttm.Attribute;
import ttm.Requirement;
import ttm.Text;

public class Requirement2SpecHierarchy extends Transformer<SpecHierarchy, Requirement> {

	@Override
	public SpecHierarchy transform(Requirement input, IProgressMonitor monitor) {

		SpecHierarchy specHierarchy = ReqIF10Factory.eINSTANCE.createSpecHierarchy();
		specHierarchy.setLongName(input.getIdent());
		
		SpecObject specObject = ReqIF10Factory.eINSTANCE.createSpecObject();
		specHierarchy.setObject(specObject);
		
		ArrayList<String> names = new ArrayList<String>();

		EList<Attribute> attributes = input.getAttributes();
		for (Attribute attribute : attributes) {
			names.add(attribute.getName());
		}
		Collections.sort(names);

		if (input.getTexts().size()>0) {
			names.add(Messages.DESCRIPTION);
		}
		names.add(Messages.IDENT);
		
		String key = names.toString();
		SpecObjectType specObjectType;
		if (CreationManager.INSTANCE.getSpecTypes().containsKey(key)) {
			specObjectType = (SpecObjectType) CreationManager.INSTANCE.getSpecTypes().get(key);
		}
		else
		{
			specObjectType = ReqIF10Factory.eINSTANCE.createSpecObjectType();
			for (String name : names) {
				AttributeDefinitionString attributeDef = ReqIF10Factory.eINSTANCE.createAttributeDefinitionString();
				attributeDef.setLongName(name);
				attributeDef.setType((DatatypeDefinitionString) CreationManager.INSTANCE.getDataType(DatatypeDefinitionString.class));
				specObjectType.getSpecAttributes().add(attributeDef);
			}
			
			CreationManager.INSTANCE.addSpecType(key, specObjectType);
		}

		
		EList<AttributeDefinition> specAttributes = specObjectType.getSpecAttributes();
		
		AttributeDefinition[] tabAttributes = specAttributes.toArray(new AttributeDefinition[specAttributes.size()]);
		
		for (int i = 0; i < attributes.size(); i++) {
			
			AttributeValueString avs = ReqIF10Factory.eINSTANCE.createAttributeValueString();
			avs.setDefinition((AttributeDefinitionString) tabAttributes[i]);
			avs.setTheValue(attributes.get(i).getValue());
			specObject.getValues().add(avs);
		}
		
		if (input.getTexts().size()>0) {
			AttributeValueString avs = ReqIF10Factory.eINSTANCE.createAttributeValueString();
			avs.setDefinition((AttributeDefinitionString) tabAttributes[tabAttributes.length -2]);
			String text = getText(input);
			avs.setTheValue(text);
			specObject.getValues().add(avs);
		}
		
		AttributeValueString avs = ReqIF10Factory.eINSTANCE.createAttributeValueString();
		avs.setDefinition((AttributeDefinitionString) tabAttributes[tabAttributes.length -1]);
		avs.setTheValue(input.getIdent());
		specObject.getValues().add(avs);

		
		CreationManager.INSTANCE.addSpecObject(specObject);
		monitor.worked(1);
		
		return specHierarchy;

	}

	private String getText(Requirement input) {
		String result = ""; //$NON-NLS-1$
		EList<Text> texts = input.getTexts();
		for (Text text : texts) {
			result+=text.getValue()+ "\n"; //$NON-NLS-1$
		}
		return result;
	}

}
