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
package org.topcased.requirement.document.elements;

import java.io.Serializable;
import java.util.Vector;

/**
 * The Class Column.
 */
public class Column extends AbstractRecognizedElement implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 405897653320656254L;

    /** The column. */
    private int column;

    /** The regex. */
    private String regex;

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.elements.RecognizedElement#getText()
     */
    public String getText()
    {
        String text = "Column: " + String.valueOf(column);
        if (!"".equals(regex))
        {
            text += " Regex: " + regex;
        }
        return text;
    }

    /**
     * Instantiates a new column.
     * 
     * @param column the column
     * @param regex the regex
     */
    public Column(int column, String regex)
    {
        this.column = column;
        this.regex = regex;
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

    /**
     * Gets the column.
     * 
     * @return the column
     */
    public int getColumn()
    {
        return column;
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
