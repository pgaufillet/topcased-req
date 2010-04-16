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
 *    
 ******************************************************************************/
package org.topcased.requirement.core.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.IdentifiedElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.ProblemChapter;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.TrashChapter;
import org.topcased.requirement.UntracedChapter;
import ttm.Requirement;

/**
 * The filter for the current requirement view.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe MERTZ</a>
 */
public class CurrentRequirementFilter extends ViewerFilter implements IRequirementFilter
{

    private String searched;

    /**
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     *      java.lang.Object)
     */
    public boolean select(Viewer viewer, Object parentElement, Object element)
    {
        if (element instanceof RequirementProject || element instanceof IdentifiedElement || element instanceof AttributeConfiguration || element instanceof ConfiguratedAttribute
                || element instanceof AttributeValue || element instanceof CurrentRequirement || element instanceof Attribute || element instanceof TrashChapter || element instanceof ProblemChapter
                || element instanceof UntracedChapter)
        {
            if (element instanceof HierarchicalElement)
            {
                HierarchicalElement elt = (HierarchicalElement) element;
                if (elt.getChildren().isEmpty() && elt.getRequirement().isEmpty())
                {
                    return false;
                }
            }
            else if (searched != null && searched.length() > 0)
            {
                if (element instanceof HierarchicalElement)
                {
                    return filter((HierarchicalElement) element);
                }
                else if (element instanceof CurrentRequirement)
                {
                    return filter((CurrentRequirement) element);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Determine if the current requirement match the filter searched
     * 
     * @param currentRequirement
     * 
     * @return true if the current requirement match the filter searched
     */
    private Boolean filter(org.topcased.requirement.Requirement requirement)
    {
        Boolean display = false;

        // Filter the name of the requirement
        display = requirement.getIdentifier() != null ? requirement.getIdentifier().indexOf(searched) > -1 : false;

        if (!display)
        {
            display = requirement.getShortDescription() != null ? requirement.getShortDescription().indexOf(searched) > -1 : false;
        }

        if (!display)
        {
            for (Attribute attribute : requirement.getAttribute())
            {
                // Filter the text attribute
                if (!display && attribute instanceof TextAttribute)
                {
                    display = filter((TextAttribute) attribute);
                }
                // Filter the object attribute (ObjectAttribute, LinkAttribute, AllocateAttribute)
                else if (!display && attribute instanceof ObjectAttribute)
                {
                    display = filter((ObjectAttribute) attribute);
                }
            }
        }
        return display;
    }

    /**
     * Determine if at least one current requirement match the filter searched
     * 
     * @param element : a hierarchical element
     * 
     * @return true if the hierarchical shall be display
     */
    private Boolean filter(HierarchicalElement element)
    {
        Boolean display = false;
        for (org.topcased.requirement.Requirement currentReq : element.getRequirement())
        {
            if (!display)
            {
                display = filter(currentReq);
            }
        }
        if (!display)
        {
            for (HierarchicalElement children : element.getChildren())
            {
                if (!display)
                {
                    display = filter(children);
                }
            }
        }
        return display;
    }

    private Boolean filter(TextAttribute text)
    {
        Boolean display = false;

        if (text.getValue() != null)
        {
            if (text.getValue().indexOf(searched) > -1)
            {
                display = true;
            }
        }
        return display;
    }

    private Boolean filter(ObjectAttribute link)
    {
        Boolean display = false;

        if (link.getValue() != null)
        {
            if (link.getValue() instanceof Requirement)
            {
                Requirement upstreamReq = (Requirement) link.getValue();
                if (upstreamReq.getIdent().indexOf(searched) > -1)
                {
                    display = true;
                }
            }
            else if (link.getValue() instanceof org.topcased.requirement.Requirement)
            {
                org.topcased.requirement.Requirement currentReq = (org.topcased.requirement.Requirement) link.getValue();
                if (currentReq.getIdentifier().indexOf(searched) > -1)
                {
                    display = true;
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
        this.searched = request;
    }

}