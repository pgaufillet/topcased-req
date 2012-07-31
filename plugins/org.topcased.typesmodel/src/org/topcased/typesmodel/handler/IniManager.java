/*****************************************************************************
 * Copyright (c) 2011 Atos.
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
package org.topcased.typesmodel.handler;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.topcased.typesmodel.model.inittypes.Column;
import org.topcased.typesmodel.model.inittypes.DocumentType;
import org.topcased.typesmodel.model.inittypes.InittypesFactory;
import org.topcased.typesmodel.model.inittypes.InittypesPackage;
import org.topcased.typesmodel.model.inittypes.Regex;
import org.topcased.typesmodel.model.inittypes.Style;
import org.topcased.typesmodel.model.inittypes.Type;
import org.topcased.typesmodel.model.inittypes.TypeModel;
/**
 * Manager singleton
 */
public class IniManager
{
    // IniBuilder
    // IniRegistry

    IniManagerRegistry registry = new IniManagerRegistry();

    /**
     * get the ini manager
     * @return Ini manager
     */
    public static IniManager getInstance()
    {
        return SingletonHolder.INSTANCE;
    }

    private IniManager()
    {
        try
        {
            ResourcesPlugin.getWorkspace().getRoot().accept(registry);
            ResourcesPlugin.getWorkspace().addResourceChangeListener(new IniResourceChangeListener(registry));
        }
        catch (CoreException e)
        {
        }
    }

    public TypeModel getModel ()
    {
        return registry.getTypeModel();
    }
    
    public List<DocumentType> getAllTypes ()
    {
        return registry.getTypeModel().getDocumentTypes();
    }
    
    private static class SingletonHolder
    {
        private static final IniManager INSTANCE = new IniManager();
    }

    public List<Column> getColumns(DocumentType documentType)
    {
        return registry.getElementsByType(documentType, InittypesPackage.Literals.COLUMN);
    }

    public List<Style> getStyles(DocumentType documentType)
    {
        return registry.getElementsByType(documentType, InittypesPackage.Literals.STYLE);
    }
    
    public List<Regex> getRegex(DocumentType documentType)
    {
        return registry.getElementsByType(documentType, InittypesPackage.Literals.REGEX);
    }

    public void save(IFile typesFile, DocumentType documentType)
    {
        IniManagerRegistry.save(typesFile, documentType);
        
    }
    
    public void save(IFile typesFile, Collection<Type> types, Type id, boolean hierarchical, String endText, String descriptionRegex) {

    	DocumentType documentType = InittypesFactory.eINSTANCE.createDocumentType();
    	documentType.getTypes().addAll(types);
    	documentType.setId(id);
    	documentType.setHierarchical(hierarchical);
    	documentType.setTextType(endText);
    	documentType.setTextRegex(descriptionRegex);
    	
    	String name = typesFile.getName().replace(".types", "");
    	documentType.setName(name);
    	
    	save(typesFile, documentType);
    }
    
}
