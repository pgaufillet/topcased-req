/***********************************************************************************************************************
 * Copyright (c) 2012 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Laurent Devernay <a href="mailto:laurent.devernay@atos.net">laurent.devernay@atos.net</a>"
 * 
 **********************************************************************************************************************/

package org.topcased.requirement.core.filters;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.SpecialChapter;

/**
 * Filter to display only the requirements that have links to other requirements that were put in the
 * 
 * @author omelois
 * 
 */
public class ValidRequirementFilter extends ViewerFilter implements IValidRequirementFilter
{

    /**
     * Cache used to keep a trace of elements that have already been processed by the filter
     */
    private Map<Object, Boolean> cache = new HashMap<Object, Boolean>();

    /**
     * Method used to state whether an element should be displayed.
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element)
    {
        // Default behavior : returns false;
        boolean result = true;

        Boolean elementVisible = cache.get(element);
        boolean isElementVisible = (elementVisible != null && elementVisible.booleanValue());

        Boolean parentVisible = cache.get(parentElement);
        boolean isParentVisible = (parentVisible != null && parentVisible.booleanValue());

        // First test : has the element been cached
        if (elementVisible != null)
        {
            result = isElementVisible;
        }
        // ObjectAttributes are displayed when their parent is visible.
        else if (element instanceof ObjectAttribute)
        {
            result = isParentVisible;
            cache.put(element, result);
        }
        // Valid requirements requirement are visible.
        else if (element instanceof Requirement)
        {
            EObject req = (EObject) element;
            if (req instanceof CurrentRequirement){
                result = !((CurrentRequirement) req).isImpacted();
                cache.put(element, result);
            }
        }
        // Recursive calls when we're dealing with a hierarchical : only
        // visible if it has a visible descendant
        else if (element instanceof HierarchicalElement)
        {
            // This is the first time the hierarchical element gets processed.
            cache.put(element, false);
            // Checking subtrees of hierarchicals.
            for (HierarchicalElement hierarchical : ((HierarchicalElement) element).getChildren())
            {
                if (select(viewer, element, hierarchical))
                {
                    cache.put(element, true);
                    result = true;
                }
            }
            // Checking subtrees of requirements.
            for (Requirement req : ((HierarchicalElement) element).getRequirement())
            {
                if (select(viewer, element, req))
                {
                    cache.put(element, true);
                    result = true;
                }
            }
        }
        // Recursive calls when we're dealing with a special chapter : only
        // visible if it has a visible descendant
        else if (element instanceof SpecialChapter)
        {
            // This is the first time the hierarchical element gets processed.
            cache.put(element, false);
            // Checking subtrees of hierarchicals.
            for (HierarchicalElement hierarchical : ((SpecialChapter) element).getHierarchicalElement())
            {
                if (select(viewer, element, hierarchical))
                {
                    cache.put(element, true);
                    result = true;
                }
            }
            // Checking subtrees of requirements.
            for (Requirement req : ((SpecialChapter) element).getRequirement())
            {
                if (select(viewer, element, req))
                {
                    cache.put(element, true);
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Empties the cache.
     */
    public void emptyCache()
    {
        cache.clear();
    }

}
