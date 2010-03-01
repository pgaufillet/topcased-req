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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.emf.ecore.EObject;
import org.topcased.facilities.extensions.AbstractExtensionManager;
import org.topcased.requirement.core.RequirementCorePlugin;


/**
 * @author Maxime AUDRAIN (CS)
 *
 */
/**
 * @author maudrain
 *
 */
public class DropRestrictionManager extends AbstractExtensionManager
{

    /** constant representing the name of the extension point */
    private static final String DROP_RESTRICTION_EXTENSION_POINT = "dropRestriction"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the value given to the model element. */
    static final String ATT_VALUE = "value"; //$NON-NLS-1$

    /** the shared instance */
    private static DropRestrictionManager manager;

    public Set<Class< ? >> eobjects;

    /**
     * Private constructor
     */
    private DropRestrictionManager()
    {
        super(RequirementCorePlugin.getId() + "." + DROP_RESTRICTION_EXTENSION_POINT);
        eobjects = new HashSet<Class< ? >>();
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
            String elt = confElt.getAttribute(ATT_VALUE);
            try
            {
                eobjects.add(Class.forName(elt));
            }
            catch (ClassNotFoundException e)
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
            String elt = confElt.getAttribute(ATT_VALUE);
            try
            {
                eobjects.remove(Class.forName(elt));
            }
            catch (ClassNotFoundException e)
            {
                RequirementCorePlugin.log(e);
            }
        }

    }

    /**
     * Check if the eobject in parameter is an instance of the objects defined in the extension
     * 
     * @param EObject eobject
     * @return boolean allowed
     */
    public boolean isDropAllowed(EObject eobject)
    {
        boolean allowed = true;

        if (eobjects != null)
        {
            for (Class< ? > clazz : eobjects)
            {
                if (clazz.isInstance(eobject))
                {
                    allowed = false;
                }
            }
        }
        return allowed;
    }

}
