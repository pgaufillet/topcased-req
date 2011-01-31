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
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
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
    @SuppressWarnings("rawtypes")
    public Object getAdapter(Object adaptableObject, Class adapterType)
    {
        if (adaptableObject instanceof IEditorPart)
        {
            // Get the current editor
            IEditorPart editor = (IEditorPart) adaptableObject;
            IEditorServices services = RequirementUtils.getSpecificServices(editor);

            // Get the policy for it
            if (services != null)
            {
                EditingDomain domain = services.getEditingDomain(editor);
                if (domain != null)
                {
                    IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(domain);

                    if (policy != null)
                    {
                        if (loadRequirementModelWithSpecifiedPolicy(editor, policy))
                        {
                            return getPage(adapterType);
                        }
                    }
                    else
                    {
                        String extension = domain.getResourceSet().getResources().get(0).getURI().fileExtension();
                        String msg = NLS.bind(Messages.getString("ModelAttachmentPolicyManager.0"), extension);
                        RequirementCorePlugin.log(msg, Status.ERROR, null);//$NON-NLS-1$
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * This method get the linked target model for a specified policy and load the requirement model
     * 
     * @param editor the current editor
     * @param policy the specified policy
     * @return true if there is a target model linked to a requirement model
     */
    private boolean loadRequirementModelWithSpecifiedPolicy(IEditorPart editor, IModelAttachmentPolicy policy)
    {
        IEditorServices services = RequirementUtils.getSpecificServices(editor);
        if (services != null)
        {
            EditingDomain domain = services.getEditingDomain(editor);
            RequirementProject project = policy.getRequirementProjectFromTargetMainResource(domain.getResourceSet().getResources().get(0));
            if (project != null)
            {
                URI uri = project.eResource().getURI();
                RequirementUtils.loadRequirementModel(uri, domain);
                return true;
            }
        }
        return false;
    }

    /**
     * Get the page from the adapter type
     * 
     * @param type the type to adapt
     * @return the abstract requirement page to load
     */
    private AbstractRequirementPage getPage(Class< ? > type)
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
    @SuppressWarnings("rawtypes")
    public Class[] getAdapterList()
    {
        return new Class[] {ICurrentRequirementPage.class, IUpstreamRequirementPage.class};
    }

}
