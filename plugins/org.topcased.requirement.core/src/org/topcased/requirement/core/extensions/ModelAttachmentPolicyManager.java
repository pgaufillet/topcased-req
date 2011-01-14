/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * Vincent HEMERY (Atos Origin) - extend framework to use regular expressions for extensions
 * 
 *****************************************************************************/
package org.topcased.requirement.core.extensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.facilities.extensions.AbstractExtensionManager;
import org.topcased.requirement.core.internal.RequirementCorePlugin;

/**
 * Defines the manager of the extension point "modelAttachmentPolicy" which provides the way to link, unlink, update a
 * requirement model to a semantic model.<br>
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class ModelAttachmentPolicyManager extends AbstractExtensionManager
{

    /** constant representing the name of the extension point */
    private static final String MODEL_ATTACHMENT_POLICY_EXTENSION_POINT = "modelAttachmentPolicy"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the value given to the file extension. */
    static final String ATT_EXTENSION = "extension"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the class. */
    static final String ATT_CLASS = "class"; //$NON-NLS-1$

    /** the shared instance */
    private static ModelAttachmentPolicyManager manager;

    /** Map of the graphical model file extension patterns and the attachment policy for it */
    public Map<String, IModelAttachmentPolicy> mapClass;

    /** A cache with the extensions which have already been handled and the matching regular expression to use for it. */
    public Map<String, String> knownExtensions;

    /**
     * Private constructor
     */
    private ModelAttachmentPolicyManager()
    {
        super(RequirementCorePlugin.getId() + "." + MODEL_ATTACHMENT_POLICY_EXTENSION_POINT); //$NON-NLS-1$
        mapClass = new HashMap<String, IModelAttachmentPolicy>();
        knownExtensions = new HashMap<String, String>();
        readRegistry();
    }

    /**
     * Gets the shared instance.
     * 
     * @return the drop restriction manager
     */
    public static ModelAttachmentPolicyManager getInstance()
    {
        if (manager == null)
        {
            manager = new ModelAttachmentPolicyManager();
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
                String model = confElt.getAttribute(ATT_EXTENSION);
                IModelAttachmentPolicy policy = (IModelAttachmentPolicy) confElt.createExecutableExtension(ATT_CLASS);
                mapClass.put(model, policy);
            }
            catch (CoreException e)
            {
                RequirementCorePlugin.log(e);
            }
        }
        // empty extensions cache to recompute with new extension patterns next time
        knownExtensions.clear();
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
            // remove attachment policy with pattern
            String elt = confElt.getAttribute(ATT_EXTENSION);
            mapClass.remove(elt);
            // remove matching extensions from cache
            List<Entry<String, String>> entries = new ArrayList<Entry<String, String>>(knownExtensions.entrySet());
            for (Entry<String, String> entry : entries)
            {
                // check matching values and remove them
                if (elt.equals(entry.getValue()))
                {
                    knownExtensions.remove(entry.getKey());
                }
            }
        }
    }

    /**
     * This method return the model attachment policy for a given file extension
     * 
     * @param fileExtension the graphical model file extension
     * @return the model policy
     */
    public IModelAttachmentPolicy getModelPolicy(String fileExtension)
    {
        if (fileExtension != null)
        {
            // check known extensions cache
            completeCacheForExtension(fileExtension);

            String correspondingPattern = knownExtensions.get(fileExtension);
            if (correspondingPattern != null)
            {
                if (mapClass.containsKey(correspondingPattern))
                {
                    return mapClass.get(correspondingPattern);
                }
            }
        }
        return null;
    }

    /**
     * Complete the cache with the pattern corresponding for the file extension
     * 
     * @param fileExtension extension to find a matching pattern for
     */
    private void completeCacheForExtension(String fileExtension)
    {
        if (!knownExtensions.containsKey(fileExtension))
        {
            // take more specific pattern in account : remove key words and keep result for the longest one
            int patternSpecificity = 0;
            for (String extPattern : mapClass.keySet())
            {
                // remove traditional key words, to know the number of other specified characters
                String patternReplaced = extPattern.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\.", "").replaceAll("\\*", "").replaceAll("\\+", "");
                int extPatternSpecificity = patternReplaced.length();
                if (fileExtension.matches(extPattern) && extPatternSpecificity > patternSpecificity)
                {
                    // store corresponding pattern in cache and keep searching for a more specific pattern
                    knownExtensions.put(fileExtension, extPattern);
                    patternSpecificity = extPatternSpecificity;
                }
            }
            if (!knownExtensions.containsKey(fileExtension))
            {
                // none has been found, none exist
                knownExtensions.put(fileExtension, null);
            }
        }
    }

    /**
     * This method return the model attachment policy for a given editing domain
     * 
     * @param the editing domain
     * @return the model policy
     */
    public IModelAttachmentPolicy getModelPolicy(EditingDomain editingDomain)
    {
        return getModelPolicy(editingDomain.getResourceSet());
    }

    /**
     * This method return the model attachment policy for a given resource set
     * 
     * @param the resource set
     * @return the model policy
     */
    public IModelAttachmentPolicy getModelPolicy(ResourceSet set)
    {
        IModelAttachmentPolicy policy = null;
        for (Resource resource : set.getResources())
        {
            String fileExtension = resource.getURI().fileExtension();
            IModelAttachmentPolicy tempPolicy = getModelPolicy(fileExtension);
            if (tempPolicy != null)
            {
                policy = tempPolicy;
            }
        }
        return policy;
    }

    /**
     * This method return true if a policy has been registered for the extension in parameter
     * 
     * @param String the extension
     * @return boolean true if a policy exists
     */
    public boolean isEnableFor(String fileExtension)
    {
        // check known extensions cache
        completeCacheForExtension(fileExtension);

        String correspondingPattern = knownExtensions.get(fileExtension);
        if (correspondingPattern != null)
        {
            if (mapClass.containsKey(correspondingPattern))
            {
                return true;
            }
        }
        return false;
    }
}
