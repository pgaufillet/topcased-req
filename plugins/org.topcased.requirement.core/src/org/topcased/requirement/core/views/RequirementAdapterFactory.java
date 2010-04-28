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

package org.topcased.requirement.core.views;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.topcased.modeler.di.model.Property;
import org.topcased.modeler.editor.Modeler;
import org.topcased.requirement.core.extensions.DefaultAttachmentPolicy;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentPage;
import org.topcased.requirement.core.views.current.ICurrentRequirementPage;
import org.topcased.requirement.core.views.upstream.IUpstreamRequirementPage;
import org.topcased.requirement.core.views.upstream.UpstreamPage;

/**
 * A factory for creating Adapter objects.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class RequirementAdapterFactory implements IAdapterFactory
{

    /**
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public Object getAdapter(Object adaptableObject, Class adapterType)
    {
        if (adaptableObject instanceof Modeler)
        {
            //Get the current modeler
            Modeler modeler = (Modeler) adaptableObject;
            
            //Get the policy for it
            IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(modeler.getEditingDomain());

            if (policy != null)
            {
                if (loadRequirementModelWithSpecifiedPolicy(modeler, policy))
                {
                    return getPage(adapterType);
                }
            }
            else if (loadRequirementModelWithDefaultPolicy(modeler))
            {
                return getPage(adapterType);
            }
        }
        return null;
    }
    
    /**
     * This method get the linked target model for the default policy and load the requirement model
     * 
     * @param modeler the current modeler
     * @return true if there is a target model linked to a requirement model
     */
    private boolean loadRequirementModelWithDefaultPolicy (Modeler modeler)
    {
        Property requirementProperty = DefaultAttachmentPolicy.getInstance().getProperty(modeler.getDiagrams());
        if (requirementProperty != null)
        {
            URI uri = URI.createURI(requirementProperty.getValue()).trimFragment().resolve(requirementProperty.eResource().getURI());
            RequirementUtils.loadRequirementModel(uri, modeler.getEditingDomain());
            return true;
        }
        return false;
    }

    /**
     * This method get the linked target model for a specified policy and load the requirement model
     * 
     * @param modeler the current modeler
     * @param policy the specified policy
     * @return true if there is a target model linked to a requirement model
     */
    private boolean loadRequirementModelWithSpecifiedPolicy (Modeler modeler, IModelAttachmentPolicy policy)
    {
        Resource targetModel = policy.getLinkedTargetModel(modeler.getEditingDomain().getResourceSet());
        if (targetModel != null)
        {
            RequirementUtils.loadRequirementModel(targetModel.getURI(), modeler.getEditingDomain());
            return true;
        }
        return false;
    }
    
    /**
     * Get the page from the adapter type
     * 
     * @param type the type to adapt
     * @return the abstract requirement page to load
     */
    private AbstractRequirementPage getPage (Class<?> type)
    {
        if (type == IUpstreamRequirementPage.class)
        {
            return new UpstreamPage();
        }
        else if (type == ICurrentRequirementPage.class)
        {
            return new CurrentPage();
        }
        return null;
    }
    
    /**
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
     */
    @SuppressWarnings("unchecked")
    public Class[] getAdapterList()
    {
        return new Class[] {ICurrentRequirementPage.class, IUpstreamRequirementPage.class};
    }

}
