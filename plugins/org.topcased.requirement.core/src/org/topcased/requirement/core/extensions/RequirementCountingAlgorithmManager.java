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
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.topcased.facilities.extensions.AbstractExtensionManager;
import org.topcased.requirement.core.internal.RequirementCorePlugin;

/**
 * Define the manager of the extension point "requirementCountingAlgorithm" who provide the way to change the count
 * algorithm of current requirements
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class RequirementCountingAlgorithmManager extends AbstractExtensionManager
{

    /** constant representing the name of the extension point */
    private static final String REQUIREMENT_COUNTING_ALGORITHM_EXTENSION_POINT = "requirementCountingAlgorithm"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the value given to the name. */
    static final String ATT_NAME = "name"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the class. */
    static final String ATT_CLASS = "class"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the description. */
    static final String ATT_DESCRIPTION = "description"; //$NON-NLS-1$

    /** the shared instance */
    private static RequirementCountingAlgorithmManager manager;

    /** Map of a name of the algorithm and the associated class */
    public Map<String, IRequirementCountingAlgorithm> mapClass;

    /** Map of a name of the algorithm and the associated description */
    public Map<String, String> mapName;

    /**
     * Private constructor
     */
    private RequirementCountingAlgorithmManager()
    {
        super(RequirementCorePlugin.getId() + "." + REQUIREMENT_COUNTING_ALGORITHM_EXTENSION_POINT); //$NON-NLS-1$
        mapClass = new HashMap<String, IRequirementCountingAlgorithm>();
        mapName = new HashMap<String, String>();
        readRegistry();
    }

    /**
     * Gets the shared instance.
     * 
     * @return the requirement identifier definition manager
     */
    public static RequirementCountingAlgorithmManager getInstance()
    {
        if (manager == null)
        {
            manager = new RequirementCountingAlgorithmManager();
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
            String name = confElt.getAttribute(ATT_NAME);
            String description = confElt.getAttribute(ATT_DESCRIPTION);
            mapName.put(name, description);
            IRequirementCountingAlgorithm algorithm;
            try
            {
                algorithm = (IRequirementCountingAlgorithm) confElt.createExecutableExtension(ATT_CLASS);
                mapClass.put(name, algorithm);
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
            String elt = confElt.getAttribute(ATT_NAME);
            mapClass.remove(elt);
            mapName.remove(elt);
        }

    }

    /**
     * This method return the requirement counting algorithm for a given name
     * 
     * @param name the algorithm name
     * @return the counting algorithm
     */
    public IRequirementCountingAlgorithm getCountingAlgorithm(String name)
    {
        if (mapClass.containsKey(name))
        {
            return mapClass.get(name);
        }
        return null;
    }

    /**
     * This method return all the requirement counting algorithm name
     * 
     * @return the counting algorithms
     */
    public Set<String> getAllAlgorithm()
    {
        return mapClass.keySet();
    }

    /**
     * This method return the requirement counting algorithm description for a given algorithm name
     * 
     * @return the counting algorithm description
     */
    public String getAlgorithmDescription(String name)
    {
        if (mapName.containsKey(name))
        {
            return mapName.get(name);
        }
        return null;
    }
}
