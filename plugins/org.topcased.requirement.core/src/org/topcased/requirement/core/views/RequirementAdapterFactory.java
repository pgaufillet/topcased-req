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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;
import org.topcased.modeler.di.model.Property;
import org.topcased.modeler.editor.Modeler;
import org.topcased.requirement.core.extensions.DefaultAttachmentPolicy;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.services.RequirementModelSourceProvider;
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

    @SuppressWarnings("unchecked")
    public Object getAdapter(Object adaptableObject, Class adapterType)
    {
        ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
        RequirementModelSourceProvider provider = (RequirementModelSourceProvider) service.getSourceProvider(RequirementModelSourceProvider.HAS_REQUIREMENT_MODEL);

        if (adapterType == IUpstreamRequirementPage.class && adaptableObject instanceof Modeler)
        {
            Modeler modeler = (Modeler) adaptableObject;

            IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(modeler.getEditingDomain());
            if (policy != null)
            {
                Resource targetModel = policy.getLinkedTargetModel(modeler.getEditingDomain().getResourceSet());
                if (targetModel != null)
                {
                    RequirementUtils.loadRequirementModel(targetModel.getURI(), modeler.getEditingDomain());
                    return new UpstreamPage();
                }
            }
            else
            {
                if (DefaultAttachmentPolicy.getInstance().getLinkedTargetModel(modeler.getEditingDomain().getResourceSet()) != null)
                {
                    Property requirementProperty = DefaultAttachmentPolicy.getInstance().getProperty(modeler.getActiveDiagram());
                    if (requirementProperty != null)
                    {
                        URI uri = URI.createURI(requirementProperty.getValue()).trimFragment().resolve(requirementProperty.eResource().getURI());
                        RequirementUtils.loadRequirementModel(uri, modeler.getEditingDomain());
                        return new UpstreamPage();
                    }
                }

            }
        }
        else if (adapterType == ICurrentRequirementPage.class && adaptableObject instanceof Modeler)
        {
            Modeler modeler = (Modeler) adaptableObject;

            IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(modeler.getEditingDomain());
            if (policy != null)
            {
                Resource targetModel = policy.getLinkedTargetModel(modeler.getEditingDomain().getResourceSet());
                if (targetModel != null)
                {
                    RequirementUtils.loadRequirementModel(targetModel.getURI(), modeler.getEditingDomain());
                    return new CurrentPage();
                }
            }
            else
            {
                if (DefaultAttachmentPolicy.getInstance().getLinkedTargetModel(modeler.getEditingDomain().getResourceSet()) != null)
                {
                    Property requirementProperty = DefaultAttachmentPolicy.getInstance().getProperty(modeler.getActiveDiagram());
                    if (requirementProperty != null)
                    {
                        URI uri = URI.createURI(requirementProperty.getValue()).trimFragment().resolve(requirementProperty.eResource().getURI());
                        RequirementUtils.loadRequirementModel(uri, modeler.getEditingDomain());
                        return new CurrentPage();
                    }
                }
            }
        }
        // Notify commands that the hasRequirement variable has changed
        provider.setHasRequirementState(false);
        return null;
    }

    @SuppressWarnings("unchecked")
    public Class[] getAdapterList()
    {
        return new Class[] {ICurrentRequirementPage.class, IUpstreamRequirementPage.class};
    }

}
