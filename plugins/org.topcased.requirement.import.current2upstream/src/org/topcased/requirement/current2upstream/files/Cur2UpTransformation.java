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
package org.topcased.requirement.current2upstream.files;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.transformation.ITransformation;

import ttm.Document;

public class Cur2UpTransformation implements ITransformation
{

    public String getText()
    {
        return Messages.Cur2UpTransformation_0;
    }

    public Document transform(URI inputModel, ResourceSet set, String docName)
    {
        Cur2Up runner;
        try{
            runner = new Cur2Up();
            Resource res = set.getResource(inputModel, true);
            UpstreamModel upstreamModel = runner.transform(res);
            if(upstreamModel != null){
                return upstreamModel.getDocuments().get(0);
            } else {
                return null;
            }
        } catch (IOException e){
            return null;
        }
    }
    
    public List<Document> getDocuments(Resource inputModel)
    {
        return new ArrayList<Document>();
    }
    
    /**
     * Provides the Current to Upstream transformation (".requirement" extension) 
     */
    public static class Cur2UpProvider implements Provider {

        public Iterable<? extends ITransformation> getTransformations()
        {
            return Collections.singleton(new Cur2UpTransformation());
        }

        public Iterable<String> getExtensions()
        {
            return Collections.singleton("requirement"); //$NON-NLS-1$
        }
        
    }

    

}
