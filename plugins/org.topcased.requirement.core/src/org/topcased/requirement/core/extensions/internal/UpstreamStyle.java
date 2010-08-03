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
package org.topcased.requirement.core.extensions.internal;

import org.eclipse.swt.graphics.RGB;
import org.topcased.requirement.core.extensions.UpstreamStyleEvaluator;

import ttm.Requirement;

/**
 * This handles an style to display an upstream requirement.
 * 
 * @author vhemery
 */
public class UpstreamStyle
{
    /** the priority */
    private Priority priority;

    /** the name */
    private String name;

    /** the evaluator provided by extension */
    private UpstreamStyleEvaluator evaluator;

    /** whether style is bold */
    private Boolean bold = null;

    /** whether style is italic */
    private Boolean italic = null;

    /** the color of text */
    private RGB color = null;

    /** whether bold can be overridden */
    private Boolean overrideBold = null;

    /** whether italic can be overridden */
    private Boolean overrideItalic = null;

    /** whether color can be overridden */
    private Boolean overrideColor = null;

    /**
     * Initiate a new style with its default values.
     * 
     * @param stylePriority the priority of the style
     * @param styleName the name
     * @param styleEvaluator the style evaluator (contains default values)
     */
    public UpstreamStyle(Priority stylePriority, String styleName, UpstreamStyleEvaluator styleEvaluator)
    {
        priority = stylePriority;
        name = styleName;
        evaluator = styleEvaluator;
    }

    /**
     * Set whether style is bold (from preferences)
     * 
     * @param isBold true if bold
     */
    public void setBold(boolean isBold)
    {
        bold = isBold;
    }

    /**
     * Whether style is bold
     * 
     * @return true if bold
     */
    public Boolean getBold()
    {
        if (bold == null)
        {
            return evaluator.isStyleBoldByDefault();
        }
        return bold;
    }

    /**
     * Set whether bold can be overridden (from preferences)
     * 
     * @param isOverridable true if bold can be overridden by other styles
     */
    public void setOverrideBold(boolean isOverridable)
    {
        overrideBold = isOverridable;
    }

    /**
     * Whether bold can be overridden
     * 
     * @return true if bold can be overridden by other styles
     */
    public Boolean getOverrideBold()
    {
        if (overrideBold == null)
        {
            return evaluator.isBoldOverrideableByDefault();
        }
        return overrideBold;
    }

    /**
     * Set whether style is italic (from preferences)
     * 
     * @param isItalic true if italic
     */
    public void setItalic(boolean isItalic)
    {
        italic = isItalic;
    }

    /**
     * Whether style is italic
     * 
     * @return true if italic
     */
    public Boolean getItalic()
    {
        if (italic == null)
        {
            return evaluator.isStyleItalicByDefault();
        }
        return italic;
    }

    /**
     * Set whether italic can be overridden (from preferences)
     * 
     * @param isOverridable true if italic can be overridden by other styles
     */
    public void setOverrideItalic(boolean isOverridable)
    {
        overrideItalic = isOverridable;
    }

    /**
     * Whether italic can be overridden
     * 
     * @return true if italic can be overridden by other styles
     */
    public Boolean getOverrideItalic()
    {
        if (overrideItalic == null)
        {
            return evaluator.isItalicOverrideableByDefault();
        }
        return overrideItalic;
    }

    /**
     * Set the style's text color (from preferences)
     * 
     * @param textColor text color
     */
    public void setColor(RGB textColor)
    {
        color = textColor;
    }

    /**
     * Get the text color
     * 
     * @return color
     */
    public RGB getColor()
    {
        if (color == null)
        {
            return evaluator.getColorByDefault();
        }
        return color;
    }

    /**
     * Set whether color can be overridden (from preferences)
     * 
     * @param isOverridable true if color can be overridden by other styles
     */
    public void setOverrideColor(boolean isOverridable)
    {
        overrideColor = isOverridable;
    }

    /**
     * Whether color can be overridden
     * 
     * @return true if color can be overridden by other styles
     */
    public Boolean getOverrideColor()
    {
        if (overrideColor == null)
        {
            return evaluator.isColorOverrideableByDefault();
        }
        return overrideColor;
    }

    /**
     * Get the style's name
     * 
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the style's priority
     * 
     * @return priority text
     */
    public String getPriority()
    {
        return priority.name();
    }

    /**
     * Restore the style's default values (from extensions)
     */
    public void restoreDefaultValues()
    {
        bold = null;
        italic = null;
        color = null;
        overrideBold = null;
        overrideItalic = null;
        overrideColor = null;
    }

    /**
     * Check whether style is correct for a given requirement
     * 
     * @param requirement the requirement to test
     * @return true if style applies
     */
    public boolean appliesTo(Requirement requirement)
    {
        return evaluator.isStyleAppliedToRequirement(requirement);
    }

}
