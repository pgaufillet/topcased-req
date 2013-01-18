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
package org.topcased.requirement.reqif.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.rmf.reqif10.AttributeDefinition;
import org.eclipse.rmf.reqif10.DatatypeDefinitionSimple;
import org.eclipse.rmf.reqif10.SpecObject;
import org.eclipse.rmf.reqif10.SpecType;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class CreationManager {
	
	public static CreationManager INSTANCE = new CreationManager();
	
	private Multimap<EObject, EObject> map = HashMultimap.create();
	
	private Collection<SpecObject> list = new LinkedList<SpecObject>();
	
	private Collection<DatatypeDefinitionSimple> dataTypes = new HashSet<DatatypeDefinitionSimple>();

	private Map<String, SpecType> specTypes = new HashMap<String, SpecType>();

	private Collection<AttributeDefinition> attDefs = new HashSet<AttributeDefinition>();
	
	private Collection<String> attributesDefinitionName = new HashSet<String>();

	private String desriptionText;
	
	public Collection<String> getAttributesDefinitionName() {
		return attributesDefinitionName;
	}

	public void addAttributesDefinitionName(
			String attributesDefinitionName) {
		this.attributesDefinitionName.add(attributesDefinitionName);
	}

	@SuppressWarnings("unchecked")
	public <T extends EObject> T create (EObject source, EClass eclass)
	{
		T eobject = (T) eclass.getEPackage().getEFactoryInstance().create(eclass);
		map.put(source, eobject);
		return eobject;
	}
	
	public void add(EObject key, EObject value)
	{
		map.put(key, value);
	}
	
	public void addSpecObject(SpecObject specObject) {
		list.add(specObject);
	}
	
	public Collection<SpecObject> getSpecObject(){
		return list;
	}
	
	public void clearListSpecObject()
	{
		list.clear();
	}
	
	public void setDataTypes(Collection<DatatypeDefinitionSimple> dataTypes)
	{
		this.dataTypes = dataTypes;
	}
	
	public void addDataType(DatatypeDefinitionSimple dataType)
	{
		this.dataTypes.add(dataType);
	}
	
	public Object getDataType(Class<?> t)
	{
		for (DatatypeDefinitionSimple dataType : dataTypes) {
				if (t.isAssignableFrom(dataType.getClass())) {
					return t.cast(dataType);
				}
		}
		return null;
	}
	
	public void setSpecTypes(Map<String, SpecType> specTypes)
	{
		this.specTypes = specTypes;
	}
	
	public void addSpecType(String key, SpecType specType){
		this.specTypes.put(key, specType);
	}
	
	public Map<String, SpecType> getSpecTypes()
	{
		return specTypes;
	}
	
	public void setAttributeDefinitions(Collection<AttributeDefinition> attDefs) {
		this.attDefs = attDefs;
	}
	
	public Collection<AttributeDefinition> getAttributeDefinitions(){
		return attDefs;
	}
	
	public void addAttributeDefinition(AttributeDefinition attDef) {
		attDefs.add(attDef);
	}

	public void clearListSpecTypes() {
		specTypes.clear();
		
	}

	public void setDescriptionText(String descriptionText) {
		this.desriptionText = descriptionText;
	}

	public String getDesriptionText() {
		return desriptionText;
	}
	
}
