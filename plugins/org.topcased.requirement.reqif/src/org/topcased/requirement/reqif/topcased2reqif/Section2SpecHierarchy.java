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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.rmf.reqif10.ReqIF10Factory;
import org.eclipse.rmf.reqif10.SpecHierarchy;
import org.topcased.requirement.reqif.common.Transformer;

import ttm.HierarchicalElement;
import ttm.Requirement;
import ttm.Section;

public class Section2SpecHierarchy extends Transformer<SpecHierarchy, Section> {

	@Override
	public SpecHierarchy transform(Section  input, IProgressMonitor monitor) {
		
		SpecHierarchy specHierarchy = ReqIF10Factory.eINSTANCE.createSpecHierarchy();
		specHierarchy.setLongName(input.getIdent());
		
		EList<HierarchicalElement> children = input.getChildren();
		for (HierarchicalElement hierarchicalElement : children) {
			if (hierarchicalElement instanceof Section)
			{
				Section section = (Section) hierarchicalElement;
				monitor.worked(1);
				SpecHierarchy specHierarchyChild = new Section2SpecHierarchy().transform(section, monitor);
				specHierarchy.getChildren().add(specHierarchyChild);
			} 
			else if (hierarchicalElement instanceof Requirement)
			{
				Requirement requirement = (Requirement) hierarchicalElement;
				monitor.worked(1);
				SpecHierarchy specHierarchyChild = new Requirement2SpecHierarchy().transform(requirement, monitor);
				specHierarchy.getChildren().add(specHierarchyChild);
			}
			
		}
		
		return specHierarchy;
	}

}
