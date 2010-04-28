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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.topcased.facilities.extensions.AbstractExtensionManager;
import org.topcased.requirement.core.internal.RequirementCorePlugin;

/**
 * Define the manager of the extension point "dropRestriction" who can disable drop on modeler objects specified in the
 * extension
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class DropRestrictionManager extends AbstractExtensionManager
{

    /** constant representing the name of the extension point */
    private static final String DROP_RESTRICTION_EXTENSION_POINT = "dropRestriction"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the value given to the file extension. */
    static final String ATT_FILE_EXTENSION = "extension"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the value given to the model element. */
    static final String ATT_VALUE = "value"; //$NON-NLS-1$

    /** the shared instance */
    private static DropRestrictionManager manager;

    /** Map of a file extension and the metamodel object to restrict */
    public Map<String, Collection<Class< ? >>> map;

    /**
     * Private constructor
     */
    private DropRestrictionManager()
    {
        super(RequirementCorePlugin.getId() + "." + DROP_RESTRICTION_EXTENSION_POINT); //$NON-NLS-1$
        map = new HashMap<String, Collection<Class< ? >>>();
        readRegistry();
    }

    /**
     * Gets the shared instance.
     * 
     * @return the drop restriction manager
     */
    public static DropRestrictionManager getInstance()
    {
        if (manager == null)
        {
            manager = new DropRestrictionManager();
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
            String model = confElt.getAttribute(ATT_FILE_EXTENSION);
            IConfigurationElement[] childElements = confElt.getChildren();
            for (IConfigurationElement childElt : childElements)
            {
                String elt = childElt.getAttribute(ATT_VALUE);
                try
                {
                    Set<Class< ? >> values = null;
                    if (map.containsKey(model))
                    {
                        values = (Set<Class< ? >>) map.get(model);
                    }
                    else
                    {
                        values = new HashSet<Class< ? >>();
                        map.put(model, values);
                    }
                    values.add(Platform.getBundle(childElt.getContributor().getName()).loadClass(elt));

                }
                catch (ClassNotFoundException e)
                {
                    RequirementCorePlugin.log(e);
                }
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
            String elt = confElt.getAttribute(ATT_FILE_EXTENSION);
            map.remove(elt);
        }

    }

    /**
     * Check if the eobject in parameter is an instance of (at least one of) the objects defined in the extension
     * 
     * @param EObject eobject, the modeler object
     * @param fileExtension, the diagram file extension
     * @return boolean allowed or not
     */
    public boolean isDropAllowed(String fileExtension, EObject eobject)
    {
        if (map.containsKey(fileExtension))
        {
            for (Class< ? > clazz : map.get(fileExtension))
            {
                if (clazz.isInstance(eobject))
                {
                    return false;
                }
            }
        }
        return true;
    }

}
