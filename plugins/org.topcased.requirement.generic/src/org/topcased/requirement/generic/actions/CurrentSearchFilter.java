/*****************************************************************************
 * Copyright (c) 2009 ATOS ORIGIN INTEGRATION.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) tristan.faure@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
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
import org.topcased.sam.requirement.TextAttribute;
import org.topcased.sam.requirement.TrashChapter;
import org.topcased.sam.requirement.UntracedChapter;
import org.topcased.ttm.Requirement;

/**
 * The Class CurrentSearchFilter.
 */
public class CurrentSearchFilter extends ViewerFilter
{

    private static CurrentSearchFilter instance = new CurrentSearchFilter();

    private String searched;

    @Override
    /**
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     *      java.lang.Object)
     */
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
    private Boolean filter(org.topcased.sam.requirement.Requirement requirement)
    {
        Boolean display = false;
        if (searched == null)
        {
            return true;
        }
        if (requirement == null)
        {
            return false;
        }
        display |= requirement.getIdentifier() != null ? requirement.getIdentifier().contains(searched) : false;
        display |= requirement.getShortDescription() != null ? requirement.getShortDescription().contains(searched) : false;

        // Filter the name of the requirement
        for (Attribute a : requirement.getAttribute())
        {
            if (a instanceof AttributeLink)
            {
                AttributeLink link = (AttributeLink) a;
                EObject value = link.getValue();
                if (value instanceof Requirement)
                {
                    Requirement req = (Requirement) value;
                    if (req.getIdent() != null && searched != null)
                    {
                        display |= "#Link_to".equals(link.getName()) && req.getIdent().indexOf(searched) > -1;
                    }
                }
            }
            // Filter the text attribute
            else if (!display && a instanceof TextAttribute)
            {
                display |= filter((TextAttribute) a);
            }
            else if (!display && a instanceof AttributeLink)
            {
                display |= filter((AttributeLink) a);
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

    private Boolean filter(AttributeLink link)
    {
        Boolean display = false;

        if (link.getValue() != null && ((Requirement) link.getValue()).getIdent() != null)
        {
            if (((Requirement) link.getValue()).getIdent().indexOf(searched) > -1)
            {
                display = true;
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

    public static CurrentSearchFilter getInstance()
    {
        return instance;
    }

    private CurrentSearchFilter()
    {
    }

    public void setSearched(String text)
    {
        this.searched = text;
    }

}
