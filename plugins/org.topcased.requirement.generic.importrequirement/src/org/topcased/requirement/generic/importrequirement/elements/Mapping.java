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

/**
 * The Class Mapping.
 */
public class Mapping implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1344194888404235047L;

    /** The element. */
    private RecognizedElement element;

    /** The attribute. */
    private Attribute attribute;

    /**
     * Instantiates a new mapping.
     * 
     * @param element the element
     * @param attribute the attribute
     */
    public Mapping(RecognizedElement element, Attribute attribute)
    {
        super();
        this.element = element;
        this.attribute = attribute;
    }

    /**
     * Gets the element.
     * 
     * @return the element
     */
    public RecognizedElement getElement()
    {
        return element;
    }

    /**
     * Gets the attribute.
     * 
     * @return the attribute
     */
    public Attribute getAttribute()
    {
        return attribute;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return attribute.getName() + ": " + element.getText();
    }

}
