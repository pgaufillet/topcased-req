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
package org.topcased.requirement.generic.actions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.topcased.sam.requirement.Attribute;
import org.topcased.sam.requirement.AttributeConfiguration;
import org.topcased.sam.requirement.AttributeLink;
import org.topcased.sam.requirement.AttributeValue;
import org.topcased.sam.requirement.ConfiguratedAttribute;
import org.topcased.sam.requirement.CurrentRequirement;
import org.topcased.sam.requirement.HierarchicalElement;
import org.topcased.sam.requirement.IdentifiedElement;
import org.topcased.sam.requirement.ProblemChapter;
import org.topcased.sam.requirement.RequirementProject;
import org.topcased.sam.requirement.TrashChapter;
import org.topcased.sam.requirement.UntracedChapter;
import org.topcased.sam.requirement.UpstreamModel;
import org.topcased.ttm.Requirement;

/**
 * The filter for the current requirement view.
 * 
 * @author christophe.mertz@c-s.fr
 * @author tristan.faure@atosorigin.com
 */
public class CurrentRequirementFilter extends ViewerFilter
{
    private static final String LINK_TO = "#Link_to";
    
	private static CurrentRequirementFilter instance = new CurrentRequirementFilter();

    public static CurrentRequirementFilter getInstance()
    {
        return instance;
    }

    private CurrentRequirementFilter()
    {

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
                for (org.topcased.sam.requirement.Requirement req : elt.getRequirement())
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
    private Boolean filter(org.topcased.sam.requirement.Requirement requirement)
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
	                display |= LINK_TO.equals(link.getName()) && searchedRequirement.equals(link.getValue());     
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
        for (org.topcased.sam.requirement.Requirement currentReq : element.getRequirement())
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