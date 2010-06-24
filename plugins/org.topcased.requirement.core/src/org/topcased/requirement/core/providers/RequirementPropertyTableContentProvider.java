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
package org.topcased.requirement.core.providers;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;

import ttm.Requirement;

/**
 * Content provider for Table content to display in the requirement property section
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class RequirementPropertyTableContentProvider extends AdapterFactoryContentProvider
{
    public RequirementPropertyTableContentProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getElements(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public Object[] getElements(Object object)
    {
        Collection<RequirementCRegistry> result = new LinkedHashSet<RequirementCRegistry>();
        for (org.topcased.requirement.Requirement requirement : (List<org.topcased.requirement.Requirement>) object)
        {
            // Selects only the current requirements not the anonymous requirements
            Boolean atLeastOne = false;
            if (requirement instanceof CurrentRequirement)
            {
                for (Attribute attribute : requirement.getAttribute())
                {
                    if (attribute instanceof AttributeLink && ((AttributeLink) attribute).getValue() instanceof Requirement)
                    {
                        Requirement upstream = (Requirement) ((AttributeLink) attribute).getValue();
                        result.add(new RequirementCRegistry(requirement, upstream));
                        atLeastOne = true;
                    }
                }

                // If any link attribute, add the requirement without attribute
                if (!atLeastOne)
                {
                    result.add(new RequirementCRegistry(requirement, null));
                }
            }
        }            
        return result.toArray();
    }
}
