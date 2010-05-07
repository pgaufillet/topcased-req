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

    /** Map of the graphical model file extension and the attachement policy for it */
    public Map<String, IModelAttachmentPolicy> mapClass;

    /**
     * Private constructor
     */
    private ModelAttachmentPolicyManager()
    {
        super(RequirementCorePlugin.getId() + "." + MODEL_ATTACHMENT_POLICY_EXTENSION_POINT); //$NON-NLS-1$
        mapClass = new HashMap<String, IModelAttachmentPolicy>();
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
            String elt = confElt.getAttribute(ATT_EXTENSION);
            mapClass.remove(elt);
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
        if (mapClass.containsKey(fileExtension))
        {
            return mapClass.get(fileExtension);
        }
        return null;
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
     * This method return true if the extension in parameter is in the hashmap
     * 
     * @param String the extension
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
