/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/

package org.topcased.requirement.core.extensions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.topcased.facilities.extensions.AbstractExtensionManager;
import org.topcased.requirement.core.internal.RequirementCorePlugin;

/**
 * Define the manager of the extension point "requirementTransformation" who provide the way to transform models into
 * requirement model.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class RequirementTransformationManager extends AbstractExtensionManager
{

    /** constant representing the name of the extension point */
    private static final String REQUIREMENT_TRANSFORMATION_EXTENSION_POINT = "requirementTransformation"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the class. */
    static final String ATT_CLASS = "class"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the value given to the file extension. */
    static final String ATT_FILE_EXTENSIONS = "extensions"; //$NON-NLS-1$

    /** the shared instance */
    private static RequirementTransformationManager manager;

    /** Map of a file extension and the transformation for it */
    public Map<String, IRequirementTransformation> mapClass;

    /**
     * Private constructor
     */
    private RequirementTransformationManager()
    {
        super(RequirementCorePlugin.getId() + "." + REQUIREMENT_TRANSFORMATION_EXTENSION_POINT); //$NON-NLS-1$
        mapClass = new HashMap<String, IRequirementTransformation>();
        readRegistry();
    }

    /**
     * Gets the shared instance.
     * 
     * @return the requirement transformation manager
     */
    public static RequirementTransformationManager getInstance()
    {
        if (manager == null)
        {
            manager = new RequirementTransformationManager();
        }
        return manager;
    }

    /**
     * @see org.topcased.facilities.extensions.AbstractExtensionManager#addExtension(org.eclipse.core.runtime.IExtension)
     */
    @Override
    protected void addExtension(IExtension extension)
    {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (IConfigurationElement confElt : elements)
        {
            try
            {
                String fileExtensions = confElt.getAttribute(ATT_FILE_EXTENSIONS);
                IRequirementTransformation transformation = (IRequirementTransformation) confElt.createExecutableExtension(ATT_CLASS);
                String[] ext = fileExtensions.split("[\\s]*,[\\s]*"); //$NON-NLS-1$
                for (String str : ext)
                {
                    mapClass.put(str, transformation);
                }
            }
            catch (CoreException e)
            {
                RequirementCorePlugin.log(e);
            }

        }

    }

    /**
     * @see org.topcased.facilities.extensions.AbstractExtensionManager#removeExtension(org.eclipse.core.runtime.IExtension)
     */
    @Override
    protected void removeExtension(IExtension extension)
    {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (IConfigurationElement confElt : elements)
        {
            String elts = confElt.getAttribute(ATT_FILE_EXTENSIONS);
            String[] ext = elts.split("[\\s]*,[\\s]*"); //$NON-NLS-1$
            for (String str : ext)
            {
                mapClass.remove(str);
            }
        }

    }

    /**
     * This method return the requirement transformation for a given fileExtension
     * 
     * @param the file extension
     * @return the requirement transformation
     */
    public IRequirementTransformation getRequirementTransformation(String fileExtension)
    {
        if (mapClass.containsKey(fileExtension))
        {
            return mapClass.get(fileExtension);
        }
        return null;
    }

    /**
     * This method return true if the file extension in parameter is in the map
     * 
     * @param String the file extension
     * @return boolean
     */
    public boolean isEnableFor(String fileExtension)
    {
        if (mapClass.containsKey(fileExtension))
        {
            return true;
        }
        return false;

    }
}
