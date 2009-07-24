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

import org.topcased.requirement.generic.importrequirement.utils.Constants;

import doc2modelMapping.AttributeInjection;
import doc2modelMapping.CompositionInjection;
import doc2modelMapping.Doc2modelMappingFactory;
import doc2modelMapping.ElementCreationInjection;
import doc2modelMapping.InjectionElement;
import doc2modelMapping.LinkedElement;
import doc2modelMapping.ReferenceInjection;
import doc2modelMapping.TextFormatter;

/**
 * The Class AttributeRequirement.
 */
public class AttributeRequirement extends Attribute
{

    /** This is for the serializable. */
    private static final long serialVersionUID = 7222453599466072771L;
    private boolean isText;

    /**
     * Instantiates a new attribute requirement.
     * 
     * @param name the name
     * @param isReference the is reference
     * @param source the source
     */
    public AttributeRequirement(String name, boolean isReference, String source)
    {
        this(name, isReference,false, source);
    }

    public AttributeRequirement(String name, boolean isReference, boolean isText, String source)
    {
        super(name, isReference, source);
        this.isText = isText ; 
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.elements.Attribute#getInjection(doc2modelMapping.LinkedElement)
     */
    @Override
    public InjectionElement getInjection(LinkedElement element)
    {
        // // Create the composition Injection
        CompositionInjection c = Doc2modelMappingFactory.eINSTANCE.createCompositionInjection();
        c.setNewInstanceForEachComposition(true);
        c.setSpecificNamespaceURI(Constants.METAMODEL_TRACEABILITY);
        c.setDependsWith((ElementCreationInjection) element.getInjection());
        if (isReference())
        {
            c.setAssociationName("navigationLinks");
            c.setOwningClass("NavigationLink");
            c.setName(this.getName());
            // create the reference injection
            ReferenceInjection a = Doc2modelMappingFactory.eINSTANCE.createReferenceInjection();
            a.setAttributeToFind("ident");
            a.setClassReferenced("Element");
            a.setIsStereotypeReference(false);
            a.setDependsWith(c);
            a.setReferenceAttribute("to");
            c.getStandardOptionalInjections().add(a);
        }
        else
        {
            AttributeInjection a = Doc2modelMappingFactory.eINSTANCE.createAttributeInjection();
            if (isText)
            {
                c.setOwningClass("Text");
                c.setAssociationName("texts");
            }
            else
            {
                c.setOwningClass("Attribute");
                c.setAssociationName("attributes");
                a.setInstanceAttribute("name");
            }
            c.setAttributeName("value");
            c.setName(this.getName());
            // Create the attribute injection
            a.setDependsWith(c);
            c.getStandardOptionalInjections().add(a);
            TextFormatter t = Doc2modelMappingFactory.eINSTANCE.createTextFormatter();
            t.setPattern(this.getOriginalName());
            a.setStringFormat(t);
        }

        // Create the Text Formatter
        return c;
    }

}
