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
package org.topcased.requirement.core.extensions;

import org.eclipse.swt.graphics.RGB;

import ttm.Requirement;

/**
 * This abstract class shall provide evaluation of whether a specific style must be applied to display an upstream
 * requirement in the Upstream Requirement view.<br />
 * Developers shall extend it to define extensions for the org.topcased.requirement.core.upstreamstyles extension point.
 * Default style information may also be set in this class by overriding methods.
 * 
 * @author vhemery
 */
public abstract class UpstreamStyleEvaluator
{
    /**
     * Check whether the specific style shall apply to the requirement
     * 
     * @param requirement the requirement to test
     * @return true if this specific style must be applied
     */
    abstract public boolean isStyleAppliedToRequirement(Requirement requirement);

    /**
     * Check default bold (the user can update definitive value through preferences).
     * 
     * @return true if this style shall be bold by default
     */
    public boolean isStyleBoldByDefault()
    {
        return false;
    }

    /**
     * Check default italic (the user can update definitive value through preferences).
     * 
     * @return true if this style shall be italic by default
     */
    public boolean isStyleItalicByDefault()
    {
        return false;
    }

    /**
     * Check default color (the user can update definitive value through preferences).
     * 
     * @return the color which shall used by default
     */
    public RGB getColorByDefault()
    {
        return new RGB(0, 0, 0);
    }

    /**
     * Check whether bold value can be overridden by correct styles which do not have the priority (correspond to a
     * default value).
     * 
     * @return true if bold value correspond to a default value and can be overridden
     */
    public boolean isBoldOverrideableByDefault()
    {
        return false;
    }

    /**
     * Check whether italic value can be overridden by correct styles which do not have the priority (correspond to a
     * default value).
     * 
     * @return true if italic value correspond to a default value and can be overridden
     */
    public boolean isItalicOverrideableByDefault()
    {
        return false;
    }

    /**
     * Check whether color value can be overridden by correct styles which do not have the priority (correspond to a
     * default value).
     * 
     * @return true if color value correspond to a default value and can be overridden
     */
    public boolean isColorOverrideableByDefault()
    {
        return false;
    }
}
