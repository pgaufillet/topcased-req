/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;

/**
 * Default provider for the current requirement view.<br>
 * Manages the order between requirements and hierarchical elements presented into the view.<br>
 * Actually, it's better to present first the requirements then the eventual hierarchical elements.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class CurrentRequirementContentProvider extends AdapterFactoryContentProvider
{

    /**
     * Constructor
     * 
     * @param adapterFactory the default adapter factory
     */
    public CurrentRequirementContentProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getChildren(java.lang.Object)
     */
    @Override
    public Object[] getChildren(Object object)
    {
        if (object instanceof HierarchicalElement)
        {
            EList<Requirement> requirements = ((HierarchicalElement) object).getRequirement();
            EList<HierarchicalElement> children = ((HierarchicalElement) object).getChildren();
            List<Object> toReturn = new ArrayList<Object>();
            toReturn.addAll(requirements);
            toReturn.addAll(children);
            return toReturn.toArray();
        }
        return super.getChildren(object);
    }
}
