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
package org.topcased.requirement.core.extensions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.ui.IEditorPart;
import org.topcased.facilities.extensions.AbstractExtensionManager;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.extensions.internal.Priority;

/**
 * Manager of the extension point used to declare the editors supporting the attachment of requirements.
 * 
 * @author vhemery
 */
public class SupportingEditorsManager extends AbstractExtensionManager
{
    /** The extension point id */
    private static final String EXTENSION_POINT_ID = "org.topcased.requirement.core.supportingEditors";

    /** The key attribute in extensions */
    private static final String ATT_KEY = "key";

    /** The editor part attribute in extensions */
    private static final String ATT_EDITOR_PART = "editorPart";

    /** The services attribute in extensions */
    private static final String ATT_SERVICES = "services";

    /** The priority attribute in extensions */
    private static final String ATT_PRIORITY = "priority";

    /** A cache containing the keys for already encountered editor part types */
    private Map<Class< ? extends IEditorPart>, String> keysCache = new HashMap<Class< ? extends IEditorPart>, String>();

    /**
     * Class holding the singleton
     */
    private static class SingletonHolder
    {
        /** The singleton instance */
        private static SupportingEditorsManager singleton = new SupportingEditorsManager();
    }

    /** The map with keys, by corresponding class names of editor parts */
    private Map<String, String> editorPartTypesAndKey = new HashMap<String, String>();

    /** The map with services classes, by corresponding key */
    private Map<String, IEditorServices> editorServices = new HashMap<String, IEditorServices>();

    /** The map with priorities, by corresponding key */
    private Map<String, Priority> priorities = new HashMap<String, Priority>();

    /**
     * Private constructor
     */
    private SupportingEditorsManager()
    {
        super(EXTENSION_POINT_ID);
        readRegistry();
    }

    /**
     * Add the editor defined by this extension
     * 
     * @param extension extension defining editor and services
     * @see org.topcased.facilities.extensions.AbstractExtensionManager#addExtension(org.eclipse.core.runtime.IExtension)
     */
    protected void addExtension(IExtension extension)
    {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (IConfigurationElement confElt : elements)
        {
            String key = confElt.getAttribute(ATT_KEY);
            String editorPartClassName = confElt.getAttribute(ATT_EDITOR_PART);
            String priority = confElt.getAttribute(ATT_PRIORITY);
            try
            {
                Object services = confElt.createExecutableExtension(ATT_SERVICES);
                if (services instanceof IEditorServices && key != null && editorPartClassName != null)
                {
                    if (editorPartTypesAndKey.containsKey(editorPartClassName))
                    {
                        // check priorities and keep only the highest one
                        String conflictingKey = editorPartTypesAndKey.get(editorPartClassName);
                        if (Priority.getValue(priority).compareTo(priorities.get(conflictingKey)) < 0)
                        {
                            // new one has higher priority, remove old one which will never be used
                            editorServices.remove(conflictingKey);
                            priorities.remove(conflictingKey);
                        }
                        else
                        {
                            // existing one has higher priority
                            continue;
                        }
                    }
                    editorPartTypesAndKey.put(editorPartClassName, key);
                    editorServices.put(key, (IEditorServices) services);
                    priorities.put(key, Priority.getValue(priority));
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
            String key = confElt.getAttribute(ATT_KEY);
            if (key != null)
            {
                for (Entry<String, String> entry : editorPartTypesAndKey.entrySet())
                {
                    if (key.equals(entry.getValue()))
                    {
                        editorPartTypesAndKey.remove(entry.getKey());
                    }
                }
                editorServices.remove(key);
                priorities.remove(key);
            }
        }
    }

    /**
     * Get the key which shall be used to get services with the given editor part.
     * 
     * @param editor the editor part.
     * @return the String key or null.
     */
    public String getKey(IEditorPart editor)
    {
        if (editor == null)
        {
            return null;
        }
        Class< ? extends IEditorPart> editorClass = editor.getClass();
        // try to find cached value
        if (keysCache.containsKey(editorClass))
        {
            return keysCache.get(editorClass);
        }
        // get parent classes and interfaces of editor
        Set<Class< ? >> parentClasses = new HashSet<Class< ? >>();
        // first traverse class hierarchy
        Class< ? > clazz = editorClass;
        while (clazz != null)
        {
            parentClasses.add(clazz);
            clazz = clazz.getSuperclass();
        }
        // now traverse interface hierarchy for each class
        Class< ? >[] classHierarchy = (Class< ? >[]) parentClasses.toArray(new Class< ? >[parentClasses.size()]);
        for (Class< ? > hierarClass : classHierarchy)
        {
            Class< ? >[] interfaces = hierarClass.getInterfaces();
            parentClasses.addAll(Arrays.asList(interfaces));
        }

        // get compatible keys
        List<String> keys = new ArrayList<String>();
        for (Class< ? > parentClass : parentClasses)
        {
            if (editorPartTypesAndKey.containsKey(parentClass.getCanonicalName()))
            {
                keys.add(editorPartTypesAndKey.get(parentClass.getCanonicalName()));
            }
        }

        // find key with highest priority
        Priority highestPriorityFound = null;
        String bestKey = null;
        for (String key : keys)
        {
            Priority priority = priorities.get(key);
            if (highestPriorityFound == null || priority.compareTo(highestPriorityFound) < 0)
            {
                highestPriorityFound = priority;
                bestKey = key;
            }
        }
        // add value to cache
        keysCache.put(editorClass, bestKey);
        return bestKey;
    }

    /**
     * Get the editor service corresponding to an editor part
     * 
     * @param editor the editor part.
     * @return corresponding editor service or null
     */
    public IEditorServices getServices(IEditorPart editor)
    {
        return getServices(getKey(editor));
    }

    /**
     * Get the editor service corresponding to a key
     * 
     * @param key string key
     * @return corresponding editor service or null
     */
    public IEditorServices getServices(String key)
    {
        return editorServices.get(key);
    }

    /**
     * Get the singleton instance
     * 
     * @return the only instance
     */
    public static SupportingEditorsManager getInstance()
    {
        return SingletonHolder.singleton;
    }
}
