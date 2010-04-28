/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.filters;

import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.preferences.UpstreamPreferenceHelper;
import ttm.Attribute;
import ttm.HierarchicalElement;
import ttm.Requirement;
import ttm.Text;

/**
 * 
 * The filter for the upstream requirement view
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class UpstreamRequirementFilter extends ViewerFilter implements IRequirementFilter
{

    private String searched;

    private IPreferenceStore preferenceStore = RequirementCorePlugin.getDefault().getPreferenceStore();

    private Set<String> defaultAttributes;

    /**
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     *      java.lang.Object)
     */
    public boolean select(Viewer viewer, Object parentElement, Object element)
    {
        if (element instanceof Text)
        {
            return true;
        }
        else if (element instanceof HierarchicalElement)
        {
            if (searched != null && searched.length() > 0)
            {
                return filter((HierarchicalElement) element);
            }
            return true;
        }
        else if (element instanceof Attribute)
        {
            String attributeLocations = preferenceStore.getString(UpstreamPreferenceHelper.UPSTREAM_ATTRIBUTES_STORE);
            defaultAttributes = UpstreamPreferenceHelper.deserialize(attributeLocations);
            return defaultAttributes.contains(((Attribute) element).getName());
        }

        return false;
    }

    private Boolean filter(HierarchicalElement hierarchicalElement)
    {
        Boolean display = false;

        if (!display)
        {
            if (hierarchicalElement instanceof Requirement)
            {
                display = filter((Requirement) hierarchicalElement);
            }

            for (HierarchicalElement children : hierarchicalElement.getChildren())
            {
                if (!display)
                {
                    display = filter(children);
                }
            }
        }

        return display;
    }

    private Boolean filter(Requirement requirement)
    {
        Boolean display = false;

        if (requirement == null)
        {
            return display;
        }

        // Filter the name of the requirement
        display = requirement.getIdent().indexOf(searched) > -1;

        if (!display)
        {
            for (Attribute attribute : requirement.getAttributes())
            {
                if (!display)
                {
                    display = attribute.getName().indexOf(searched) > -1;
                }
                if (!display)
                {
                    display = attribute.getValue().indexOf(searched) > -1;
                }
            }
        }

        return display;
    }

    /**
     * @see org.topcased.requirement.core.filters.IRequirementFilter#setSearched(java.lang.String)
     */
    public void setSearched(String request)
    {
        searched = request;
    }

}