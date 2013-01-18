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
import org.eclipse.rmf.reqif10.AttributeValue;
import org.eclipse.rmf.reqif10.SpecHierarchy;
import org.eclipse.rmf.reqif10.SpecObject;
import org.eclipse.rmf.reqif10.common.util.ReqIF10Util;
import org.topcased.requirement.reqif.common.Transformer;
import org.topcased.requirement.reqif.utils.CreationManager;

import ttm.Attribute;
import ttm.HierarchicalElement;
import ttm.Text;
import ttm.TtmFactory;

public class SpecHierarchy2HierarchicalElement extends Transformer<HierarchicalElement, SpecHierarchy> {


	@Override
	public HierarchicalElement transform(SpecHierarchy input, IProgressMonitor monitor) {
		HierarchicalElement element;
		SpecObject specObject = input.getObject();
		if ( specObject != null) {
			element = TtmFactory.eINSTANCE.createRequirement(); 
			element.setIdent(input.getLongName());
			EList<AttributeValue> values = specObject.getValues();
			for (AttributeValue attributeValue : values) {
				AttributeDefinition attributeDef = ReqIF10Util.getAttributeDefinition(attributeValue);
				if (CreationManager.INSTANCE.getDesriptionText().equals(attributeDef.getLongName())){
					Text text = TtmFactory.eINSTANCE.createText();
					text.setValue(ReqIF10Util.getTheValue(attributeValue).toString());
					element.getTexts().add(text);
				} else
				{
					Attribute att = TtmFactory.eINSTANCE.createAttribute();
					att.setName(attributeDef.getLongName());
					att.setValue(ReqIF10Util.getTheValue(attributeValue).toString());
					element.getAttributes().add(att);

				}
			}
		}
		else
		{
			element = TtmFactory.eINSTANCE.createSection();
			element.setIdent(input.getLongName());
		}
		monitor.worked(1);
		EList<SpecHierarchy> specHierarchies = input.getChildren();
		if (specHierarchies.size()>0) {
			for (SpecHierarchy specHierarchy : specHierarchies) {
				element.getChildren().add(transform(specHierarchy, monitor));
			}
		}
		return element;
	}
}
