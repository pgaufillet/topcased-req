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

import java.util.Vector;

/**
 * The Interface RecognizedElement.
 */
public interface RecognizedElement extends OwnerElement
{

    /**
     * Gets the text.
     * 
     * @return the text
     */
    String getText();

    /**
     * Gets the children.
     * 
     * @return the children
     */
    public Vector<RecognizedElement> getChildren();

    /**
     * Sets the parent.
     * 
     * @param recognizedTree the new parent
     */
    void setParent(OwnerElement recognizedTree);

    /**
     * Gets the parent.
     * 
     * @return the parent
     */
    OwnerElement getParent();

    /**
     * Checks if is selected.
     * 
     * @return true, if is selected
     */
    boolean isSelected();

    /**
     * Sets the selected.
     * 
     * @param selected the new selected
     */
    void setSelected(boolean selected);
}
