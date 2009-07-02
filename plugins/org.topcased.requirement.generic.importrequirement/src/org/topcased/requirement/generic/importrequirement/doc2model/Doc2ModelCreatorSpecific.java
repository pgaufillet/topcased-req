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
package org.topcased.requirement.generic.importrequirement.doc2model;

import org.topcased.requirement.generic.importrequirement.elements.Mapping;

import doc2modelMapping.Attribute;
import doc2modelMapping.Hierarchy;
import doc2modelMapping.InjectionElement;
import doc2modelMapping.LinkedElement;

/**
 * The Interface Doc2ModelCreatorSpecific.
 */
public interface Doc2ModelCreatorSpecific
{

    /**
     * Gets the injection model.
     * 
     * @return the injection model
     */
    public InjectionElement getInjectionModel();

    // public LinkedElement getIdentification(RecognizedElement identification);

    /**
     * Gets the injection element.
     * 
     * @param stereo the stereo
     * @param isFlat the is flat
     * @param isSpreadsheet 
     * 
     * @return the injection element
     */
    public InjectionElement getInjectionElement(String stereo, boolean isFlat, boolean isSpreadsheet);

    /**
     * Gets the column attribute.
     * 
     * @param m the m
     * @param linkedElement the linked element
     * 
     * @return the column attribute
     */
    public Attribute getColumnAttribute(Mapping m, LinkedElement linkedElement);

    /**
     * Gets the attribute.
     * 
     * @param m the m
     * @param linkedElement the linked element
     * 
     * @return the attribute
     */
    public Attribute getAttribute(Mapping m, LinkedElement linkedElement);

    /**
     * Gets the all hierachy.
     * 
     * @return the all hierachy
     */
    public Hierarchy getAllHierachy();

}
