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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.topcased.facilities.extensions.AbstractExtensionManager;
import org.topcased.requirement.core.internal.RequirementCorePlugin;

/**
 * Define the manager of the extension point "requirementIdentifierDefinition" who provide
 * the way to change the count algorithm of current requirements and add key words to current requirements identifier
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class RequirementIdentifierVariablesManager extends AbstractExtensionManager
{

    /** constant representing the name of the extension point */
    private static final String REQUIREMENT_IDENTIFIER_VARIABLES_EXTENSION_POINT = "requirementIdentifierVariables"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the value given to the id. */
    static final String ATT_ID = "id"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the class. */
    static final String ATT_CLASS = "class"; //$NON-NLS-1$

    /** the shared instance */
    private static RequirementIdentifierVariablesManager manager;

    /** Map of the id and the identifier variables for it */
    public Map<String, IRequirementIdentifierVariables> mapClass;

    /**
     * Private constructor
     */
    private RequirementIdentifierVariablesManager()
    {
        super(RequirementCorePlugin.getId() + "." + REQUIREMENT_IDENTIFIER_VARIABLES_EXTENSION_POINT);
        mapClass = new HashMap<String, IRequirementIdentifierVariables>();
        readRegistry();
    }

    /**
     * Gets the shared instance.
     * 
     * @return the requirement identifier variables manager
     */
    public static RequirementIdentifierVariablesManager getInstance()
    {
        if (manager == null)
        {
            manager = new RequirementIdentifierVariablesManager();
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
            String model = confElt.getAttribute(ATT_ID);
            IRequirementIdentifierVariables definition;
            try
            {
                definition = (IRequirementIdentifierVariables) confElt.createExecutableExtension(ATT_CLASS);
                mapClass.put(model, definition);
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
            String elt = confElt.getAttribute(ATT_ID);
            mapClass.remove(elt);
        }

    }
    
    /**
     * This method return all the requirement identifier variables
     * 
     * @return the identifier variables
     */
    public Collection<IRequirementIdentifierVariables> getIdentifierVariables()
    {
        return mapClass.values();
    }

}
