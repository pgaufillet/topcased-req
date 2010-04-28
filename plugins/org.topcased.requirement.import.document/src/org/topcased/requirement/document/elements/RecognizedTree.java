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

import java.util.Collection;
import java.util.LinkedList;

/**
 * The Class RecognizedTree.
 */
public class RecognizedTree implements OwnerElement
{

    /** The elements. */
    private Collection<RecognizedElement> elements = new LinkedList<RecognizedElement>();

    /**
     * Adds the.
     * 
     * @param e the e
     */
    public void add(RecognizedElement e)
    {
        elements.add(e);
        e.setParent(this);
    }

    /**
     * Gets the children.
     * 
     * @return the children
     */
    public Collection<RecognizedElement> getChildren()
    {
        return elements;
    }
}
