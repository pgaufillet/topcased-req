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
package org.topcased.requirement.core.views.upstream.styles;

import org.topcased.requirement.core.extensions.UpstreamStyleEvaluator;
import org.topcased.requirement.core.utils.RequirementUtils;

import ttm.Requirement;

public class LinkedUpstreamRequirementStyle extends UpstreamStyleEvaluator
{

    /**
     * Check if "Linked" style shall apply
     * 
     * @return true if requirement is linked with a current
     */
    @Override
    public boolean isStyleAppliedToRequirement(Requirement requirement)
    {
        if (RequirementUtils.isLinked(requirement))
        {
            return true;
        }
        return false;
    }

    /**
     * Style is bold
     * 
     * @return true
     */
    @Override
    public boolean isStyleBoldByDefault()
    {
        return true;
    }

    /**
     * Style is italic
     * 
     * @return true
     */
    @Override
    public boolean isStyleItalicByDefault()
    {
        return true;
    }
}
