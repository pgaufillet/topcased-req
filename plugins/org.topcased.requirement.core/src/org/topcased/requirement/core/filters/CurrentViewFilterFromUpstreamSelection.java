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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.IdentifiedElement;
import org.topcased.requirement.ProblemChapter;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.TrashChapter;
import org.topcased.requirement.UntracedChapter;
import org.topcased.requirement.UpstreamModel;

import ttm.Requirement;

/**
 * The filter for the current requirement view.
 * 
 * @author christophe.mertz@c-s.fr
 * @author tristan.faure@atosorigin.com
 */
public class CurrentViewFilterFromUpstreamSelection extends ViewerFilter
{
	private static CurrentViewFilterFromUpstreamSelection instance = new CurrentViewFilterFromUpstreamSelection();

    public static CurrentViewFilterFromUpstreamSelection getInstance()
    {
        return instance;
    }

    private CurrentViewFilterFromUpstreamSelection()
    {
        //avoid instantiation
    }

    private Requirement searchedRequirement;

    /**
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     *      java.lang.Object)
     */
    public boolean select(Viewer viewer, Object parentElement, Object element)
    {
        if (searchedRequirement == null)
        {
            return !(element instanceof UpstreamModel) ;
        }
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
                boolean result = false;
                for (HierarchicalElement children : elt.getChildren())
                {
                    result = result || filter(children);
                }
                for (org.topcased.requirement.Requirement req : elt.getRequirement())
                {
                    result = result || filter(req);
                }
                return result;
            }
            else if (searchedRequirement != null)
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

        if (searchedRequirement != null)
        {
	        // Filter the requirement itself (not its name)
	        for (Attribute a : requirement.getAttribute())
	        {
	            if (a instanceof AttributeLink)
	            {
	                AttributeLink link = (AttributeLink) a;
	                display |= searchedRequirement.equals(link.getValue());     
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

    public void setSearchedRequirement(Requirement current)
    {
        this.searchedRequirement = current;
    }

}