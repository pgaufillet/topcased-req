/*****************************************************************************
 * Copyright (c) 2010 Rockwell Collins.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Vincent Hemery (Atos Origin) - Initial API and implementation
 *    
 ******************************************************************************/
package org.topcased.requirement.core.services;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.topcased.requirement.core.extensions.internal.UpstreamStyle;
import org.topcased.requirement.core.extensions.internal.UpstreamStylesManager;

import ttm.Requirement;

/**
 * This service recovers the names of styles which apply on a given upstream requirement.
 * 
 * @author vhemery
 */
public class ApplicableUpstreamStylesService
{
    /**
     * Get the list of styles which are applicable on an upstream requirement
     * 
     * @param req the requirement to recover applicable styles on
     * @return the list of styles names, ordered by decreasing priority
     */
    public static List<String> getStylesForRequirement(Requirement req)
    {
        List<String> result = new LinkedList<String>();
        LinkedHashMap<String, UpstreamStyle> existingStyles = UpstreamStylesManager.getInstance().getStylesFromExtensions();
        for (Entry<String, UpstreamStyle> entry : existingStyles.entrySet())
        {
            if (entry.getValue().appliesTo(req))
            {
                result.add(entry.getKey());
            }
        }
        return result;
    }
}
