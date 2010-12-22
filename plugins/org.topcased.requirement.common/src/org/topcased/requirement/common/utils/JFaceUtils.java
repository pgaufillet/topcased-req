/*****************************************************************************
 * Copyright (c) 2010 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Vincent Hemery (Atos Origin}) {vincent.hemery@atosorigin.com} - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.common.utils;

import org.eclipse.jface.resource.DataFormatException;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

/**
 * A Utility class which provides methods for using JFace aspects
 * 
 * @author vhemery
 */
public class JFaceUtils
{

    /**
     * Get the Color corresponding to a string with a particular format. Return null if color cannot be resolved.
     * 
     * @param rgb a string representing the rgb. Format is "R,G,B" like 244,164,96 or 128,0,255
     * @return the color
     */
    public static Color getColor(String rgb)
    {
        try
        {
            RGB colorRGB = StringConverter.asRGB(rgb);
            if (colorRGB != null)
            {
                Color color = JFaceResources.getColorRegistry().get(StringConverter.asString(colorRGB));
                if (color == null)
                {
                    JFaceResources.getColorRegistry().put(StringConverter.asString(colorRGB), colorRGB);
                    color = JFaceResources.getColorRegistry().get(StringConverter.asString(colorRGB));
                }
                return color;
            }
        }
        catch (DataFormatException e)
        {
            // Just ignore
        }
        return null;
    }

    /**
     * Gets the Font with given styles.
     * 
     * @param fontData An existing font data
     * @param style One or several Font style(s)
     * @return Font
     */
    public static Font getFont(FontData fontData, int style)
    {
        fontData.setStyle(fontData.getStyle() | style);
        return getFont(fontData);
    }

    /**
     * Get the Font with given font data
     * 
     * @param fontData An existing font data
     * @return the font associated with the FontData
     */
    public static Font getFont(FontData fontData)
    {
        if (fontData != null)
        {
            FontData[] fontDataList = new FontData[] {fontData};
            String asString = StringConverter.asString(fontData);
            if (!JFaceResources.getFontRegistry().hasValueFor(asString))
            {
                JFaceResources.getFontRegistry().put(asString, fontDataList);
            }
            // the get on a font always return a font even if this font is not registered
            return JFaceResources.getFontRegistry().get(asString);
        }
        return null;
    }
}
