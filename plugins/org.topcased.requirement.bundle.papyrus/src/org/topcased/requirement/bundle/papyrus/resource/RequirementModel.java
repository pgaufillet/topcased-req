/*****************************************************************************
 * Copyright (c) 2010 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Vincent Hemery (Atos Origin}) {vincent.hemery@atosorigin.com} - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.bundle.papyrus.resource;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.papyrus.resource.AbstractBaseModel;
import org.topcased.requirement.util.RequirementResource;

/**
 * The requirement model is the resource containing the requirement project for a given Papyrus diagram.
 * 
 * @author vhemery
 */
public class RequirementModel extends AbstractBaseModel
{
    /** Papyrus requirement identifier */
    public static String REQ_MODEL_ID = "org.topcased.requirement.bundle.papyrus.resource.RequirementModel";

    /**
     * Get the requirement identifier.
     * 
     * @return requirement identifier
     */
    public String getIdentifier()
    {
        return REQ_MODEL_ID;
    }

    /**
     * Get the file extension used by the resource.
     * 
     * @return requirement extension
     */
    protected String getModelFileExtension()
    {
        return RequirementResource.FILE_EXTENSION;
    }

    /**
     * Load the model by using the provided full path as a hint for the resource URI. In this implementation, simply add
     * the model extension.
     * 
     * @param fullPathWithoutExtension full path without the file extension
     */
    public void loadModel(IPath fullPathWithoutExtension)
    {
        URI uri = getPlatformURI(fullPathWithoutExtension.addFileExtension(getModelFileExtension()));
        if (exists(uri))
        {
            super.loadModel(fullPathWithoutExtension);
        }
    }

    /**
     * Returns <b>true</b> only if the URI represents a file and if this file exists.
     * 
     * @param uri uri of file to check existence
     * @see org.eclipse.ui.IEditorInput#exists()
     */
    private boolean exists(URI uri)
    {
        if (uri.isFile())
        {
            return new File(uri.toFileString()).exists();
        }
        else if (EMFPlugin.IS_RESOURCES_BUNDLE_AVAILABLE && uri.isPlatformResource())
        {
            return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(uri.toPlatformString(true))).exists();
        }
        else
        {
            return false;
        }
    }

    /**
     * Creation of the model is optional. When requirements are not used, we will not create a model for each diagram.
     * Hence, this method does nothing.
     * 
     * @see org.eclipse.papyrus.resource.IModel#createModel(org.eclipse.core.runtime.IPath)
     * 
     * @param fullPath the full resource path without the file extension
     */
    public void createModel(IPath fullPath)
    {
        // do nothing
    }

    /**
     * Save the model in its repository.
     * 
     * @throws IOException
     * @see org.eclipse.papyrus.resource.IModel#saveModel()
     * 
     */
    public void saveModel() throws IOException
    {
        if (resourceIsSet())
        {
            super.saveModel();
        }
    }

    /**
     * Change the path under which the model should be save. Do not save it now ! *
     * 
     * @see org.eclipse.papyrus.resource.IModel#changeModelPath(org.eclipse.core.runtime.IPath)
     * @param fullPath the full resource path without the file extension
     */
    public void changeModelPath(IPath fullPath)
    {
        if (resourceIsSet())
        {
            super.changeModelPath(fullPath);
        }
    }

}
