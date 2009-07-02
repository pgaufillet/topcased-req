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

import doc2modelMapping.InjectionElement;
import doc2modelMapping.LinkedElement;

/**
 * The Class Attribute.
 */
abstract public class Attribute implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7970190621036487952L;

    /** The name. */
    private String name;

    /** The is reference. */
    private boolean isReference;

    /** The source. */
    private String source;

    /**
     * Instantiates a new attribute.
     * 
     * @param name the name
     * @param isReference the is reference
     * @param source the source
     */
    public Attribute(String name, boolean isReference, String source)
    {
        this.name = name;
        this.isReference = isReference;
        this.source = source;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName()
    {
        String s = source + ": " + name;
        if (isReference)
        {
            s += " (reference)";
        }
        return s;
    }

    /**
     * Checks if is reference.
     * 
     * @return true, if is reference
     */
    public boolean isReference()
    {
        return isReference;
    }

    /**
     * Gets the original name.
     * 
     * @return the original name
     */
    public String getOriginalName()
    {
        return name;
    }

    /**
     * Gets the source.
     * 
     * @return the source
     */
    public String getSource()
    {
        return source;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Attribute)
        {
            Attribute a = (Attribute) obj;
            return this.getName() != null && this.getName().equals(a.getName()) && this.getOriginalName() != null && this.getOriginalName().equals(a.getOriginalName()) && this.getSource() != null
                    && this.getSource().equals(a.getSource());
        }
        return super.equals(obj);
    }

    /**
     * Gets the injection.
     * 
     * @param element the element
     * 
     * @return the injection
     */
    public abstract InjectionElement getInjection(LinkedElement element);
}
