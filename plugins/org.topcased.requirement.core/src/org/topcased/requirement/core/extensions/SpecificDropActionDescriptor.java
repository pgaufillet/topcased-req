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
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.emf.ecore.EObject;
import org.topcased.requirement.core.RequirementCorePlugin;

/**
 * @author Maxime AUDRAIN (CS)
 * 
 */
public class SpecificDropActionDescriptor
{

    static final String TAG_MODEL = "model"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the value given to the metamodel. */
    static final String ATT_METAMODEL = "metamodel"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the value given to the class. */
    static final String ATT_CLASS = "class"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the value given to the model element. */
    static final String ATT_ELEMENT = "element"; //$NON-NLS-1$

    /** This descriptor's wrapped. */

    private String uri;

    /** Keeps a reference to the configuration elements that describes the Action. */
    private final IConfigurationElement element;

    public Map<Class< ? >, ISpecificDropAction> map;

    /**
     * Constructs a new descriptor from an IConfigurationElement.
     * 
     * @param configuration Configuration element coming from plugin.xml
     */
    public SpecificDropActionDescriptor(IConfigurationElement configuration)
    {
        element = configuration;
        map = new HashMap<Class< ? >, ISpecificDropAction>();
        load();

    }

    /**
     * Gets the metamodel uri
     * 
     * @return The URI
     */
    public String getUri()
    {
        return uri;
    }

    /**
     * Load all the attributes
     * 
     * @throws ClassNotFoundException
     * @throws InvalidRegistryObjectException
     */
    private void load()
    {
        uri = element.getAttribute(ATT_METAMODEL);
        IConfigurationElement[] childElements = element.getChildren();
        for (IConfigurationElement childElt : childElements)
        {
            Class< ? > clazz;
            try
            {
                clazz = Class.forName(childElt.getAttribute(ATT_ELEMENT));
                ISpecificDropAction act = (ISpecificDropAction) childElt.createExecutableExtension(ATT_CLASS);
    
                if (!map.containsKey(clazz))
                {
                    map.put(clazz, act);
                }
            }
            catch (ClassNotFoundException e)
            {
                RequirementCorePlugin.log(e);
            }
            catch (CoreException e)
            {
                RequirementCorePlugin.log(e);
            }
        }
    }

    /**
     * get the action for the given element
     * 
     * @param action
     * @return collection of elements
     */
    public ISpecificDropAction getActionFor(EObject eobject)
    {
        for (Class< ? > clazz : map.keySet())
        {
            if (clazz.isInstance(eobject))
            {
                return map.get(clazz);
            }
        }
        return null;
    }
}
