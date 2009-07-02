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

import doc2modelMapping.InjectionElement;
import doc2modelMapping.LinkedElement;
import doc2modelMapping.Doc2modelMappingFactory;
import doc2modelMapping.NewClassInjection;
import doc2modelMapping.ReferenceInjection;
import doc2modelMapping.TextFormatter;

/**
 * The Class AttributeSysmlReference.
 */
public class AttributeSysmlReference extends Attribute
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -9047646164220456656L;

    /** The class to create. */
    private final String classToCreate;

    /**
     * Instantiates a new attribute sysml reference.
     * 
     * @param name the name
     * @param isReference the is reference
     * @param source the source
     * @param classToCreate the class to create
     */
    public AttributeSysmlReference(String name, boolean isReference, String source, String classToCreate)
    {
        super(name, true, source);
        this.classToCreate = classToCreate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.elements.Attribute#getInjection(doc2modelMapping.LinkedElement)
     */

    @Override
    public InjectionElement getInjection(LinkedElement element)
    {
        NewClassInjection result = Doc2modelMappingFactory.eINSTANCE.createNewClassInjection();
        result.setInstanceTarget(classToCreate);
        result.setSpecificNamespaceURI(Constants.METAMODEL_UML);
        result.setAssociationOwning("packagedElement");
        result.setBrowseTopContainersForOwner(true);
        result.setOwningClass("Package");
        ReferenceInjection ref1 = Doc2modelMappingFactory.eINSTANCE.createReferenceInjection();
        ref1.setAttributeToFind("name");
        ref1.setClassReferenced("Requirement");
        ref1.setDependsWith(result);
        ref1.setIsStereotypeReference(false);
        ref1.setReferenceAttribute("supplier");
        result.getStandardOptionalInjections().add(ref1);
        ReferenceInjection ref2 = Doc2modelMappingFactory.eINSTANCE.createReferenceInjection();
        ref2.setAttributeToFind("name");
        ref2.setClassReferenced("Requirement");
        ref2.setDependsWith(result);
        ref2.setIsStereotypeReference(false);
        ref2.setReferenceAttribute("client");
        TextFormatter formatter = Doc2modelMappingFactory.eINSTANCE.createTextFormatter();
        formatter.setPattern("[currentElement]");
        ref2.setStringFormat(formatter);
        result.getStandardOptionalInjections().add(ref2);
        return result;
    }

}
