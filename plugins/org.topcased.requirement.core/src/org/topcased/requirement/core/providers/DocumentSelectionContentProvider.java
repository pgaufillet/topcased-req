/***********************************************************************************************************************
 * Copyright (c) 2010 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.providers;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.util.RequirementResource;

import ttm.Document;

/**
 * Content provider used for the checkbox tree viewer allowing the user to select {@link Document} to import into his
 * current requirement model.
 * 
 * Creation : 06 december 2010<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since TOPCASED 4.3.0
 */
public class DocumentSelectionContentProvider extends AdapterFactoryContentProvider
{
    public DocumentSelectionContentProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object object)
    {
        if (object instanceof ResourceSet)
        {
            Collection<Resource> candidates = new HashSet<Resource>();
            for (Resource rsc : ((ResourceSet) object).getResources())
            {
                // need to filter because other kind of resources have been loaded into the resource set.
                if (rsc instanceof RequirementResource)
                {
                    // we keep only RequirementResource as root elements
                    candidates.add(rsc);
                }
            }
            return candidates.toArray();
        }
        return super.getElements(object);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getChildren(java.lang.Object)
     */
    @Override
    public Object[] getChildren(Object object)
    {
        // the only possible children are the ttm:Document contained inside the requirement models.
        if (object instanceof RequirementResource)
        {
            return RequirementUtils.getUpstreamDocuments((RequirementResource) object).toArray();
        }
        return new Object[0];
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#hasChildren(java.lang.Object)
     */
    @Override
    public boolean hasChildren(Object object)
    {
        return object instanceof RequirementResource;
    }
}
