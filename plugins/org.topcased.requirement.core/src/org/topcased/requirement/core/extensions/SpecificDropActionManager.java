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
import org.topcased.facilities.extensions.AbstractExtensionManager;
import org.topcased.requirement.core.internal.RequirementCorePlugin;

/**
 * Define the manager of the extension point "specificDropAction" who enable to create action for an element when a drag
 * and drop occurs on it
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class SpecificDropActionManager extends AbstractExtensionManager
{

    /** constant representing the name of the extension point */
    private static final String SPECIFIC_DROP_ACTION_EXTENSION_POINT = "specificDropAction"; //$NON-NLS-1$

    /** the shared instance */
    private static SpecificDropActionManager manager;

    /** The set of registered actions */
    private Set<SpecificDropActionDescriptor> actions;

    /**
     * Private constructor
     */
    private SpecificDropActionManager()
    {
        super(RequirementCorePlugin.getId() + "." + SPECIFIC_DROP_ACTION_EXTENSION_POINT); //$NON-NLS-1$
        actions = new HashSet<SpecificDropActionDescriptor>();
        readRegistry();
    }

    /**
     * Gets the shared instance.
     * 
     * @return the specific actions manager
     */
    public static SpecificDropActionManager getInstance()
    {
        if (manager == null)
        {
            manager = new SpecificDropActionManager();
        }
        return manager;
    }

    /**
     * @see org.topcased.facilities.extensions.AbstractExtensionManager#addExtension(org.eclipse.core.runtime.IExtension)
     */
    protected void addExtension(IExtension extension)
    {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (IConfigurationElement confElt : elements)
        {
            if (SpecificDropActionDescriptor.TAG_MODEL.equals(confElt.getName()))
            {
                actions.add(new SpecificDropActionDescriptor(confElt));
            }
        }
    }

    /**
     * @see org.topcased.facilities.extensions.AbstractExtensionManager#removeExtension(org.eclipse.core.runtime.IExtension)
     */
    protected void removeExtension(IExtension extension)
    {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (IConfigurationElement confElt : elements)
        {
            if (SpecificDropActionDescriptor.TAG_MODEL.equals(confElt.getName()))
            {
                String id = confElt.getAttribute(SpecificDropActionDescriptor.ATT_URI);
                SpecificDropActionDescriptor descriptor = find(id);
                actions.remove(descriptor);
            }
        }
    }

    /**
     * Finds the descriptor by uri
     * 
     * @param uri the searched uri
     * @return the specific action descriptor or <code>null</code> if not found
     */
    public SpecificDropActionDescriptor find(String uri)
    {
        for (SpecificDropActionDescriptor descriptor : actions)
        {
            if (uri.equals(descriptor.getUri()))
            {
                return descriptor;
            }
        }
        return null;
    }

    /**
     * Gets the set of action descriptors
     * 
     * @return The set of all registered descriptors
     */
    public Set<SpecificDropActionDescriptor> getDropActionDescriptors()
    {
        return actions;
    }
}
