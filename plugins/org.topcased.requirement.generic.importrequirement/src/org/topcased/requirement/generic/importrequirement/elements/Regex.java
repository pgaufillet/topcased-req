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
 * The Class Regex.
 */
public class Regex extends AbstractRecognizedElement implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4717494982663601132L;

    /** The value. */
    private String value;

    /** The results. */
    Vector<RecognizedElement> results = null;

    /**
     * Instantiates a new regex.
     * 
     * @param regex the regex
     */
    public Regex(String regex)
    {
        super();
        this.value = regex;
    }

    /**
     * Instantiates a new regex.
     * 
     * @param regex the regex
     * @param parent the parent
     */
    public Regex(String regex, RecognizedElement parent)
    {
        this.value = regex;
        this.setParent(parent);
    }

    /**
     * Gets the regex.
     * 
     * @return the regex
     */
    public String getRegex()
    {
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.elements.RecognizedElement#getText()
     */
    public String getText()
    {
        return "regex: " + value;
    }

    /**
     * Return as child in an array all the group (*) found in the value.
     * 
     * @return the children
     */
    public Vector<RecognizedElement> getChildren()
    {
        if (results == null)
        {
            results = new Vector<RecognizedElement>();
            for (int i = 0; i < value.length(); i++)
            {
                if ('(' == value.charAt(i))
                {
                    String tmp = "";
                    char aChar;
                    do
                    {
                        aChar = value.charAt(i);
                        i++;
                        tmp += aChar;
                    }
                    while (i < value.length() && value.charAt(i) != ')');
                    if (value.charAt(i) == ')')
                    {
                        tmp += value.charAt(i);
                        i--;
                        results.add(new Regex(tmp, this));
                    }
                }

            }
        }
        if (results.size() == 1)
        {
            results = null;
        }
        return results;
    }

}
