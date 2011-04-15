/*****************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *  	Mathieu Velten (Atos Origin) <mathieu.velten@atosorigin.com>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.filters;

import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.preferences.UpstreamPreferenceHelper;

import ttm.Document;
import ttm.TtmPackage;

/**
 * a parametrable viewer filter for requirements.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe MERTZ</a>
 */
public class RequirementFilter extends ViewerFilter implements IRequirementFilter
{

    private String searched;

    private boolean displayCurrent;

    private boolean displayUpstream;

    private IPreferenceStore preferenceStore = RequirementCorePlugin.getDefault().getPreferenceStore();

    private boolean caseSensitive = false;

    public RequirementFilter(boolean displayCurrent, boolean displayUpstream)
    {
        this.displayCurrent = displayCurrent;
        this.displayUpstream = displayUpstream;
    }

    /**
     * @see org.topcased.requirement.core.filters.IRequirementFilter#setSearched(java.lang.String)
     */
    public void setSearched(String request)
    {
        this.searched = request;
    }

    /**
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     *      java.lang.Object)
     */
    public boolean select(Viewer viewer, Object parentElement, Object element)
    {
        // special case : display UpstreamModel from current meta-model if upstream needs to be displayed
        if (element instanceof UpstreamModel)
        {
            if (displayUpstream)
            {
                return filter((UpstreamModel) element);
            }
            else
            {
                return false;
            }
        }

        // check package to filter upstream and current according to display* parameters
        if (element instanceof EObject)
        {
            EPackage pack = ((EObject) element).eClass().getEPackage();

            if (!displayUpstream && pack.equals(TtmPackage.eINSTANCE))
            {
                return false;
            }
            if (!displayCurrent && pack.equals(RequirementPackage.eINSTANCE))
            {
                return false;
            }
        }

        // don't filter more if no current search
        if (searched != null && searched.length() != 0)
        {
            if (element instanceof ttm.HierarchicalElement)
            {
                return filter((ttm.HierarchicalElement) element);
            }
            else if (element instanceof org.topcased.requirement.HierarchicalElement)
            {
                return filter((org.topcased.requirement.HierarchicalElement) element);
            }
            else if (element instanceof CurrentRequirement)
            {
                return filter((CurrentRequirement) element);
            }
            else if (element instanceof SpecialChapter)
            {
                // hide chapters without matching elements on search
                return filter((SpecialChapter) element);
            }
            else if (element instanceof AttributeConfiguration)
            {
                // hide configuration on search
                return false;
            }
        }

        // Attributes need to be filtered even when the search is empty
        if (element instanceof ttm.Attribute)
        {
            return filter((ttm.Attribute) element);
        }
        else if (element instanceof org.topcased.requirement.Attribute)
        {
            return filter((org.topcased.requirement.Attribute) element);
        }

        return true;
    }

    private boolean filterString(String s)
    {
        if (searched == null || searched.length() == 0)
        {
            return true;
        }

        if (s == null)
        {
            return false;
        }
        else
        {
            String sCase = s;
            String searchedCase = searched;
            if (!caseSensitive)
            {
                sCase = sCase.toLowerCase();
                searchedCase = searchedCase.toLowerCase();
            }
            if (sCase.contains(searchedCase))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    private boolean filter(UpstreamModel upstreamModel)
    {

        for (Document doc : upstreamModel.getDocuments())
        {
            if (filter((ttm.HierarchicalElement) doc))
            {
                return true;
            }
        }
        return false;
    }

    private boolean filter(org.topcased.requirement.Attribute attribute)
    {
        return true;
    }

    private boolean filter(SpecialChapter specialChapter)
    {

        for (org.topcased.requirement.HierarchicalElement he : specialChapter.getHierarchicalElement())
        {
            if (filter(he))
            {
                return true;
            }
        }

        for (org.topcased.requirement.Requirement r : specialChapter.getRequirement())
        {
            if (filter(r))
            {
                return true;
            }
        }

        return false;
    }

    private boolean filter(org.topcased.requirement.Requirement requirement)
    {

        // Filter the name of the requirement
        if (filterString(requirement.getIdentifier()))
        {
            return true;
        }

        if (filterString(requirement.getShortDescription()))
        {
            return true;
        }

        for (org.topcased.requirement.Attribute attribute : requirement.getAttribute())
        {
            // Filter the text attribute
            if (attribute instanceof TextAttribute)
            {
                if (filter((TextAttribute) attribute))
                {
                    return true;
                }
            }
            // Filter the object attribute (ObjectAttribute, LinkAttribute, AllocateAttribute)
            if (attribute instanceof ObjectAttribute)
            {
                if (filter((ObjectAttribute) attribute))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean filter(org.topcased.requirement.HierarchicalElement element)
    {

        for (org.topcased.requirement.Requirement currentReq : element.getRequirement())
        {
            if (filter(currentReq))
            {
                return true;
            }
        }

        for (org.topcased.requirement.HierarchicalElement children : element.getChildren())
        {
            if (filter(children))
            {
                return true;
            }
        }

        return false;
    }

    private boolean filter(TextAttribute text)
    {
        return filterString(text.getValue());
    }

    private boolean filter(ObjectAttribute link)
    {

        if (link.getValue() != null)
        {
            if (link.getValue() instanceof ttm.Requirement)
            {
                ttm.Requirement upstreamReq = (ttm.Requirement) link.getValue();
                if (filterString(upstreamReq.getIdent()))
                {
                    return true;
                }
            }
            else if (link.getValue() instanceof org.topcased.requirement.Requirement)
            {
                org.topcased.requirement.Requirement currentReq = (org.topcased.requirement.Requirement) link.getValue();
                if (filterString(currentReq.getIdentifier()))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean filter(ttm.Attribute attribute)
    {
        String attributeLocations = preferenceStore.getString(UpstreamPreferenceHelper.UPSTREAM_ATTRIBUTES_STORE);
        Set<String> defaultAttributes = UpstreamPreferenceHelper.deserialize(attributeLocations);
        return defaultAttributes.contains(attribute.getName());
    }

    private boolean filter(ttm.HierarchicalElement hierarchicalElement)
    {

        // Filter the name
        if (filterString(hierarchicalElement.getIdent()))
        {
            return true;
        }

        if (filterString(hierarchicalElement.getShortDescription()))
        {
            return true;
        }

        for (ttm.HierarchicalElement children : hierarchicalElement.getChildren())
        {
            if (filter(children))
            {
                return true;
            }
        }

        for (ttm.Attribute att : hierarchicalElement.getAttributes())
        {
            if (filterString(att.getValue()))
            {
                return true;
            }
        }

        return false;
    }

    public void setCaseSensitive(boolean selection)
    {
        this.caseSensitive = selection;
    }

}