/*****************************************************************************
 * Copyright (c) 2010 Rockwell Collins.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Vincent Hemery (Atos Origin) - Initial API and implementation
 *    
 ******************************************************************************/
package org.topcased.requirement.core.extensions.internal;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.topcased.facilities.extensions.AbstractExtensionManager;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.extensions.UpstreamStyleEvaluator;

public class UpstreamStylesManager extends AbstractExtensionManager
{
    /** The extension point id */
    private static final String EXTENSION_POINT_ID = "org.topcased.requirement.core.upstreamstyles";

    /** The name attribute in extensions */
    private static final String ATT_NAME = "name";

    /** The class attribute in extensions */
    private static final String ATT_CLASS = "class";

    /** The priority attribute in extensions */
    private static final String ATT_PRIORITY = "priority";

    /** The singleton instance */
    private static UpstreamStylesManager instance = null;

    /** The map with styles evaluator and their name and priority */
    private Map<Priority, Map<String, UpstreamStyleEvaluator>> definedUpstreamStylesByPriority;

    /**
     * Private constructor
     */
    private UpstreamStylesManager()
    {
        super(EXTENSION_POINT_ID);
        definedUpstreamStylesByPriority = new HashMap<Priority, Map<String, UpstreamStyleEvaluator>>();
        for (Priority priority : Priority.values())
        {
            definedUpstreamStylesByPriority.put(priority, new HashMap<String, UpstreamStyleEvaluator>());
        }
        readRegistry();
    }

    /**
     * Add the upstream styles defined by this extension
     * 
     * @param extension extension defining upstream requirement styles
     * @see org.topcased.facilities.extensions.AbstractExtensionManager#addExtension(org.eclipse.core.runtime.IExtension)
     */
    protected void addExtension(IExtension extension)
    {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (IConfigurationElement confElt : elements)
        {
            String priority = confElt.getAttribute(ATT_PRIORITY);
            String name = confElt.getAttribute(ATT_NAME);
            try
            {
                Object evaluator = confElt.createExecutableExtension(ATT_CLASS);
                if (evaluator instanceof UpstreamStyleEvaluator && name != null)
                {
                    Map<String, UpstreamStyleEvaluator> stylesMap = definedUpstreamStylesByPriority.get(Priority.getValue(priority));
                    stylesMap.put(name, (UpstreamStyleEvaluator) evaluator);
                }
            }
            catch (CoreException e1)
            {
                RequirementCorePlugin.log(e1);
            }
        }
    }

    /**
     * Remove the upstream styles defined by this extension
     * 
     * @param extension extension defining upstream requirement styles
     * @see org.topcased.facilities.extensions.AbstractExtensionManager#removeExtension(org.eclipse.core.runtime.IExtension)
     */
    protected void removeExtension(IExtension extension)
    {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (IConfigurationElement confElt : elements)
        {
            String priority = confElt.getAttribute(ATT_PRIORITY);
            String name = confElt.getAttribute(ATT_NAME);
            if (name != null)
            {
                Map<String, UpstreamStyleEvaluator> stylesMap = definedUpstreamStylesByPriority.get(Priority.getValue(priority));
                stylesMap.remove(name);
            }
        }
    }

    /**
     * Get the upstream styles loaded from extensions only (with default values)
     * 
     * @return upstream styles map (by name) with default values, ordered by decreasing priority
     */
    public LinkedHashMap<String, UpstreamStyle> getStylesFromExtensions()
    {
        LinkedHashMap<String, UpstreamStyle> res = new LinkedHashMap<String, UpstreamStyle>();
        for (Priority priority : Priority.values())
        {
            Map<String, UpstreamStyleEvaluator> stylesMap = definedUpstreamStylesByPriority.get(priority);
            for (Entry<String, UpstreamStyleEvaluator> entry : stylesMap.entrySet())
            {
                UpstreamStyle style = new UpstreamStyle(priority, entry.getKey(), entry.getValue());
                res.put(entry.getKey(), style);
            }
        }
        return res;
    }

    /**
     * Get the singleton instance
     * 
     * @return the only instance
     */
    public static UpstreamStylesManager getInstance()
    {
        if (instance == null)
        {
            // initialize singleton
            instance = new UpstreamStylesManager();
        }
        return instance;
    }
}
