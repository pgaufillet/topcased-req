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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ttm.Attribute;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class TypeManager {
	public static TypeManager INSTANCE = new TypeManager () ;

	Multimap<String, Attribute> allNUplet = HashMultimap.create();
	
	Set<String> allTypes = new HashSet<String>();
	
	public void addAll(String id, List<Attribute> attribues) {
		allNUplet.putAll(id, attribues);
	}
	
	public void add(String id, Attribute attribute)
	{
		allNUplet.put(id, attribute);
	}
	
	public boolean contains(String id)
	{
		return allNUplet.containsKey(id);
	}
}
