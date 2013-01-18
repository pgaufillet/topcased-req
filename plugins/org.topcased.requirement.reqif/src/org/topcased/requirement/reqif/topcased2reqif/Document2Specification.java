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
import org.eclipse.rmf.reqif10.ReqIF10Factory;
import org.eclipse.rmf.reqif10.Specification;
import org.topcased.requirement.reqif.common.Transformer;

import ttm.Document;

public class Document2Specification extends Transformer<Specification, Document> {

	@Override
	public Specification transform(Document input, IProgressMonitor monitor) {
		
		Specification specification = ReqIF10Factory.eINSTANCE.createSpecification();
		specification.setLongName(input.getIdent());
		

		return specification;
	}

}
