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
package org.topcased.requirement.core.transformation.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.transformation.ITransformation;
import org.topcased.requirement.core.utils.RequirementUtils;

import ttm.Document;

public class InPlaceTransformation implements ITransformation
{

    public InPlaceTransformation ()
    {
    }
    
    public String getText()
    {
        return Messages.getString("MergeRequirementModelWizardPage.inPlaceTransformation.text");
    }

    public Document transform(URI inputModel, ResourceSet resourceSet, String docName)
    {
        List<Document> docs = RequirementUtils.getUpstreamDocuments(resourceSet.getResource(inputModel, true));
        for (Document d : docs){
            if (d.getIdent().equals(docName)){
                return d;
            }
        }
        return null;
    }
    
    public List<Document> getDocuments(Resource inputModel)
    {
        return RequirementUtils.getUpstreamDocuments(inputModel);
    }
    
    /**
     * Provides the InPlace transformation (".requirement" extension)
     */
    public static class InPlaceProvider implements Provider 
    {

        public Iterable<? extends ITransformation> getTransformations()
        {
            return Collections.singleton(new InPlaceTransformation());
        }
             
        public Iterable<String> getExtensions()
        {
            return Collections.singleton("requirement");
        }
    }

}
