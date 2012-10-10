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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.topcased.requirement.core.extensions.IImportDocument;
import org.topcased.requirement.core.extensions.ImportDocumentManager;
import org.topcased.requirement.core.transformation.ITransformation;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.typesmodel.handler.IniManager;
import org.topcased.typesmodel.model.inittypes.DocumentType;
import org.topcased.typesmodel.model.inittypes.provider.DocumentTypeItemProvider;

import ttm.Document;

public class TypeTransformation implements ITransformation
{

    private final DocumentType type;
    private final String docText;

    public TypeTransformation (DocumentType type)
    {
        this.type = type;
        this.docText = new DocumentTypeItemProvider(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE)).getText(type);
    }
    
    public String getText()
    {
        return docText;
    }

    public Document transform(URI inputModel, ResourceSet resourceSet, String docName)
    {
        IImportDocument importer = ImportDocumentManager.getInstance().getImporter();
        File file;
        try
        {
            file = File.createTempFile("updateRequirementTemp", ".requirement");
            importer.getDocument(getType(), inputModel, URI.createFileURI(file.getAbsolutePath()), new NullProgressMonitor());
            List<Document> docs = RequirementUtils.getUpstreamDocuments(resourceSet.getResource(URI.createFileURI(file.getAbsolutePath()), true));
            if (!docs.isEmpty()) {
                return docs.get(0);
            }
        }
        catch (IOException e)
        {
        }
        return null ;
    }

    public List<Document> getDocuments(Resource inputModel)
    {
        return new ArrayList<Document>();
    }
    
    public DocumentType getType(){
        return type;
    }

    /**
     * Provides the types transfomations (".docx", ".odt", ".csv", ".ods", ".xlsx" extensions)
     */
    public static class TypeProvider implements Provider 
    {

        public Iterable< ? extends ITransformation> getTransformations()
        {
            List<DocumentType> types = IniManager.getInstance().getAllTypes();
            List<TypeTransformation> typesT = new ArrayList<TypeTransformation>(types.size());
            for (DocumentType t : types)
            {
                typesT.add(new TypeTransformation(t));
            }
            return typesT;
        }
        
        public Iterable<String> getExtensions()
        {
            ArrayList<String> extList = new ArrayList<String>();
            extList.add("docx");
            extList.add("odt");
            extList.add("csv");
            extList.add("ods");
            extList.add("xlsx");
            return extList;
        }
        
    }

}
