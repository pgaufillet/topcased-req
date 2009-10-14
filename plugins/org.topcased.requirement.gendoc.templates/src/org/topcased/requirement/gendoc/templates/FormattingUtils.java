/*****************************************************************************
 * Copyright (c) 2009 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  eperico (Atos Origin) emilien.perico@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.gendoc.templates;

import java.util.Arrays;

import org.eclipse.emf.ecore.EObject;
import org.topcased.sam.requirement.Attribute;

/**
 * Util class to manage and format attributes and values of current requirements
 * 
 * @author eperico
 */
public class FormattingUtils
{
    /** tab characters for spaces */
    private static String tabChars = "&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;";
    
    /** to know if current "Link To" is the first attribute link. */
    private boolean isFirstAttributeLink = true;
    
    /**
     * Split text on lines, insert tab if any, apply specified style
     * 
     * @param text the text
     * @param attributeStyle the attribute style
     * 
     * @return string with needed tags to insert it in an entry tag (cell of a table)
     */
    public String insertFormattedAttribute(String text, String attributeStyle)
    {
        String result = "";
        String[] split = formatText(text, true);
        for (String line : Arrays.asList(split))
        {
            result += "<para><phrase><markup role=\"" + attributeStyle + "\"><![CDATA[" + line + "]]></markup></phrase></para>";
        }
        return result;
    }
    
    /**
     * Format text, inserting tab characters and splitting specified text on line separators
     * 
     * @param text the text
     * @param wrappedInCDATA the wrapped in cdata
     * 
     * @return the splitted text
     */
    private String[] formatText(String text, boolean wrappedInCDATA)
    {
        // manage tabs
        String insertTab = "";
        if (wrappedInCDATA)
        {
            insertTab = "]]>" + tabChars + "<![CDATA[";
        }
        else
        {
            insertTab = tabChars;
        }
        String textWithTab = text.replaceAll("\\t", insertTab);

        // manage new lines
        String newline = System.getProperty("line.separator");
        if (newline == null || newline.length() == 0)
        {
            newline = "\\n";
        }
        return textWithTab.split(newline);
    }
    
    /**
     * Clean the attribute name to have the right formatter
     * 
     * @param eObject the e object
     * @param name the old name
     * 
     * @return the formatted name
     */
    public String getFormattedName(EObject eObject, String name)
    {
        return name.replaceFirst("#", "").replaceAll("_", "&#160;").replaceAll(" ", "&#160;");
    }
    
    /**
     * Gets the style name for the specified name.
     * 
     * @param eObject the current eObject
     * @param name the name
     * 
     * @return the style name for the name
     */
    public String getStyleNameFromName(EObject eObject, String name)
    {
        String formattedName = name.replaceFirst("#", "").replaceAll("_", " ");
        formattedName = formattedName.replaceAll("[^1-z]", "");
        return formattedName;
    }
    
    /**
     * Gets the tabCharacter for spaces
     * 
     * @param currentEObject the current eObject
     * 
     * @return the tab char
     */
    public String getTabChar(EObject currentEObject)
    {
        return tabChars;
    }
    
    /**
     * Checks if is first attribute link.
     * 
     * @param attribute the attribute
     * 
     * @return true, if is first attribute link
     */
    public boolean isFirstAttributeLink(Attribute attribute)
    {
        return isFirstAttributeLink;
    }

    /**
     * Sets the first attribute link.
     * 
     * @param eObject the e object
     * @param newBooleanValue the new boolean value
     */
    public void setFirstAttributeLink(EObject eObject, boolean newBooleanValue)
    {
        isFirstAttributeLink = newBooleanValue;
    }

}
