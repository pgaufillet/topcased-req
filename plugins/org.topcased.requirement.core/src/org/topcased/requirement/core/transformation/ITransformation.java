/*****************************************************************************
 * Copyright (c) 2012 Atos.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthieu Boivineau - Initial API and implementation
 *    
 ******************************************************************************/
package org.topcased.requirement.core.transformation;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import ttm.Document;


public interface ITransformation{

    /**
     * Returns the transformation name
     * @return the name of the transformation
     */
    String getText();
    
    /**
     * Executes the transformation
     * @param inputModel the input model URL
     * @param set the resource set
     * @param docName the doc name (from the upstream documents)
     * @return the transformed model
     */
    Document transform(URI inputModel, ResourceSet set, String docName);
    
    List<Document> getDocuments(Resource inputModel);
    
    public interface Provider {
        Iterable<? extends ITransformation> getTransformations();
        Iterable<String> getExtensions();
    }

}
