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

/**
 * This handles a combination of styles to display an upstream requirement.
 * 
 * @author vhemery
 */
public class UpstreamCombinedStyle
{
    /** whether style is bold */
    private Boolean bold = null;

    /** whether style is italic */
    private Boolean italic = null;

    /** the color of text */
    private RGB color = null;

    /**
     * Initiate a new style with its final values.
     * 
     * @param isBold whether style is bold
     * @param isItalic whether style is italic
     * @param textColor the color of text
     */
    public UpstreamCombinedStyle(boolean isBold, boolean isItalic, RGB textColor)
    {
        bold = isBold;
        italic = isItalic;
        color = textColor;
    }

    /**
     * Whether style is bold
     * 
     * @return true if bold
     */
    public Boolean getBold()
    {
        return bold;
    }

    /**
     * Whether style is italic
     * 
     * @return true if italic
     */
    public Boolean getItalic()
    {
        return italic;
    }

    /**
     * Get the text color
     * 
     * @return color
     */
    public RGB getColor()
    {
        return color;
    }

}
