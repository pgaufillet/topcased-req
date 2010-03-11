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
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.facilities.extensions.AbstractExtensionManager;
import org.topcased.requirement.core.RequirementCorePlugin;

/**
 * Define the manager of the extension point "modelAttachmentPolicy" who provide the way to link, unlink, update a
 * metamodel to a requirement model
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class ModelAttachmentPolicyManager extends AbstractExtensionManager
{

    /** constant representing the name of the extension point */
    private static final String MODEL_ATTACHMENT_POLICY_EXTENSION_POINT = "modelAttachmentPolicy"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the value given to the uri. */
    static final String ATT_URI = "uri"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the class. */
    static final String ATT_CLASS = "class"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the value given to the file extension. */
    static final String ATT_ENABLE_FOR = "enableFor"; //$NON-NLS-1$

    /** the shared instance */
    private static ModelAttachmentPolicyManager manager;

    /** Map of an uri of the metamodel and the attachement policy for it */
    public Map<String, IModelAttachmentPolicy> mapClass;

    /** Map of an uri of the metamodel and the file extension for it */
    public Map<String, String> mapExtension;

    /**
     * Private constructor
     */
    private ModelAttachmentPolicyManager()
    {
        super(RequirementCorePlugin.getId() + "." + MODEL_ATTACHMENT_POLICY_EXTENSION_POINT);
        mapClass = new HashMap<String, IModelAttachmentPolicy>();
        mapExtension = new HashMap<String, String>();
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
            String model = confElt.getAttribute(ATT_URI);
            IModelAttachmentPolicy policy;
            try
            {
                policy = (IModelAttachmentPolicy) confElt.createExecutableExtension(ATT_CLASS);
                mapClass.put(model, policy);
                mapExtension.put(model, confElt.getAttribute(ATT_ENABLE_FOR));
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
            String elt = confElt.getAttribute(ATT_URI);
            mapClass.remove(elt);
            mapExtension.remove(elt);
        }

    }

    /**
     * This method return the model attachement policy for a given metamodel uri
     * 
     * @param uri the metamodel uri
     * @return the model policy
     */
    public IModelAttachmentPolicy getModelPolicy(String uri)
    {
        if (mapClass.containsKey(uri))
        {
            return mapClass.get(uri);
        }
        return null;
    }

    /**
     * This method return the model attachement policy for a given editingDomain
     * 
     * @param the editing domain
     * @return the model policy
     */
    public IModelAttachmentPolicy getModelPolicy(EditingDomain editingDomain)
    {
        IModelAttachmentPolicy policy = null;
        EList<Resource> resources = editingDomain.getResourceSet().getResources();
        for (Resource resource : resources)
        {
            EList<EObject> roots = resource.getContents();
            for (EObject root : roots)
            {
                String uri = EcoreUtil.getURI(root.eClass().getEPackage()).trimFragment().toString();
                IModelAttachmentPolicy tempPolicy = getModelPolicy(uri);
                if (tempPolicy != null)
                {
                    policy = tempPolicy;
                }
            }
        }
        return policy;
    }

    /**
     * This method return the file extension for a given uri
     * 
     * @param uri the metamodel uri
     * @return the file extension
     */
    public String getFileExtension(String uri)
    {
        if (mapExtension.containsKey(uri))
        {
            return mapExtension.get(uri);
        }
        return null;

    }

    /**
     * This method return the file extension for a given editing domain
     * 
     * @param the editing domain
     * @return the file extension
     */
    public String getFileExtension(EditingDomain editingDomain)
    {
        String fileExtension = null;
        for (Resource resource : editingDomain.getResourceSet().getResources())
        {
            for (EObject root : resource.getContents())
            {
                String uri = EcoreUtil.getURI(root.eClass().getEPackage()).trimFragment().toString();
                String tempFileExtension = getFileExtension(uri);
                if (tempFileExtension != null)
                {
                    fileExtension = tempFileExtension;
                }
            }
        }
        return fileExtension;
    }

    /**
     * This method return true if the extension in parameter is in the hashmap
     * 
     * @param String the extension
     * @return boolean
     */
    public boolean isEnableFor(String fileExtension)
    {
        if (mapExtension.containsValue(fileExtension))
        {
            return true;
        }
        return false;

    }

}
