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
 *  Caroline Bourdeu d'Aguerre (ATOS ORIGIN INTEGRATION) caroline.bourdeudaguerre@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.generic.importrequirement.elements;

import java.io.Serializable;
import java.util.Vector;

/**
 * The Class Style.
 */
public class Style extends AbstractRecognizedElement implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7561244253149960404L;

    /** The style. */
    private String style = "";

    /** The regex. */
    private String regex = "";

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.elements.RecognizedElement#getText()
     */
    public String getText()
    {
        String text = "Style: " + style;
        if (!"".equals(regex))
        {
            text += " Regex: " + regex;
        }
        return text;
    }

    /**
     * Instantiates a new style.
     * 
     * @param style the style
     * @param regex the regex
     */
    public Style(String style, String regex)
    {
        this.style = style;
        this.regex = regex;
    }

    /**
     * Gets the style.
     * 
     * @return the style
     */
    public String getStyle()
    {
        return style;
    }

    /**
     * Gets the regex.
     * 
     * @return the regex
     */
    public String getRegex()
    {
        return regex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.elements.RecognizedElement#getChildren()
     */
    public Vector<RecognizedElement> getChildren()
    {
        return null;
    }

}
