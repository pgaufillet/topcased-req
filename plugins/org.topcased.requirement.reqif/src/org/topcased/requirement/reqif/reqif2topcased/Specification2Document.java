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
import org.eclipse.rmf.reqif10.SpecHierarchy;
import org.eclipse.rmf.reqif10.Specification;
import org.topcased.requirement.reqif.common.Transformer;

import ttm.Document;
import ttm.HierarchicalElement;
import ttm.TtmFactory;

public class Specification2Document extends Transformer<Document, Specification> {

	@Override
	public Document transform(Specification input, IProgressMonitor monitor) {
		Document document = TtmFactory.eINSTANCE.createDocument();
		document.setIdent(input.getLongName());
		EList<SpecHierarchy> SpecHierarchies = input.getChildren();
		for (SpecHierarchy specHierarchy : SpecHierarchies) {
			HierarchicalElement hierarchicalElement = new SpecHierarchy2HierarchicalElement().transform(specHierarchy, monitor);
			document.getChildren().add(hierarchicalElement);
		}
		return document;
	}

}
