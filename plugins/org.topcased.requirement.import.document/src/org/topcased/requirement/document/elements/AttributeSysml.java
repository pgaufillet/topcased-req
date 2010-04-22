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

import doc2modelMapping.AttributeInjection;
import doc2modelMapping.DependantInjection;
import doc2modelMapping.Doc2modelMappingFactory;
import doc2modelMapping.ElementCreationInjection;
import doc2modelMapping.InjectionElement;
import doc2modelMapping.LinkedElement;
import doc2modelMapping.ReferenceInjection;

/**
 * The Class AttributeSysml.
 */
public class AttributeSysml extends Attribute
{

    /** This is for the serializable. */
    private static final long serialVersionUID = 1216462491737957804L;

    /** The property name. */
    private String propertyName;

    /** The property type. */
    private String propertyType;

    /**
     * Instantiates a new attribute sysml.
     * 
     * @param name the name
     * @param isReference the is reference
     * @param source the source
     */
    public AttributeSysml(String name, boolean isReference, String source)
    {
        super(name, isReference, source);
    }

    /**
     * Instantiates a new attribute sysml.
     * 
     * @param name the name
     * @param ref the ref
     * @param profileName the profile name
     * @param pname the pname
     * @param type the type
     */
    public AttributeSysml(String name, boolean ref, String profileName, String pname, String type)
    {
        this(name, ref, profileName);
        this.propertyName = pname;
        this.propertyType = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.elements.Attribute#getInjection(doc2modelMapping.LinkedElement)
     */
    @Override
    public InjectionElement getInjection(LinkedElement element)
    {
        DependantInjection result = null;
        if (isReference() && propertyName != null && propertyType != null)
        {
            ReferenceInjection injection = Doc2modelMappingFactory.eINSTANCE.createReferenceInjection();
            injection.setAttributeToFind("name");
            injection.setIsStereotypeReference(false);
            injection.setReferenceAttribute(propertyName);
            injection.setClassReferenced(propertyType);
            result = injection;
        }
        else
        {
            AttributeInjection attributeInjection = Doc2modelMappingFactory.eINSTANCE.createAttributeInjection();
            if (!"Requirement".equals(this.getSource()))
            {
                attributeInjection.setStereotypeAttribute(this.getOriginalName());
            }
            else
            {
                attributeInjection.setInstanceAttribute(this.getOriginalName());
            }
            result = attributeInjection;
        }
        result.setDependsWith((ElementCreationInjection) element.getInjection());
        return result;
    }

}
