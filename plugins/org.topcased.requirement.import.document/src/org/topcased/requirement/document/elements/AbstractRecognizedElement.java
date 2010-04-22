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

/**
 * The Class AbstractRecognizedElement.
 */
public abstract class AbstractRecognizedElement implements RecognizedElement
{

    /** The parent. */
    private OwnerElement parent = null;

    /** The selected. */
    private boolean selected = false;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.doc2model.requirement.elements.RecognizedElement#setParent(org.topcased.doc2model.requirement.elements
     * .OwnerElement)
     */
    public void setParent(OwnerElement p)
    {
        parent = p;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.elements.RecognizedElement#getParent()
     */
    public OwnerElement getParent()
    {
        return parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.elements.RecognizedElement#isSelected()
     */
    public boolean isSelected()
    {
        // TODO Auto-generated method stub
        return selected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.elements.RecognizedElement#setSelected(boolean)
     */
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

}
