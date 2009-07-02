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

import org.topcased.requirement.generic.importrequirement.elements.Column;
import org.topcased.requirement.generic.importrequirement.elements.RecognizedElement;
import org.topcased.requirement.generic.importrequirement.elements.Regex;
import org.topcased.requirement.generic.importrequirement.elements.Style;

import doc2modelMapping.ColumnMatchElement;
import doc2modelMapping.Doc2modelMappingFactory;
import doc2modelMapping.DocStyle;
import doc2modelMapping.LinkedElement;
import doc2modelMapping.RegEx;

/**
 * The Class LinkedElementResolver.
 */
public class LinkedElementResolver
{

    /** The factory. */
    private static Doc2modelMappingFactory factory = Doc2modelMappingFactory.eINSTANCE;

    /**
     * Gets the linked element.
     * 
     * @param e the e
     * 
     * @return the linked element
     */
    public static LinkedElement getLinkedElement(RecognizedElement e)
    {
        if (e instanceof Regex)
        {
            Regex regex = (Regex) e;
            RegEx createRegEx = factory.createRegEx();
            createRegEx.setRegExToMatch(regex.getRegex());
            return createRegEx;
        }
        else if (e instanceof Column)
        {
            Column col = (Column) e;
            ColumnMatchElement colMe = factory.createColumnMatchElement();
            colMe.setNumColumn(col.getColumn());
            colMe.setRegExToMatch(col.getRegex());
            return colMe;
        }
        else if (e instanceof Style)
        {
            Style s = (Style) e;
            DocStyle style = factory.createDocStyle();
            style.setStyleName(s.getStyle());
            return style;
        }
        return null;
    }
}
