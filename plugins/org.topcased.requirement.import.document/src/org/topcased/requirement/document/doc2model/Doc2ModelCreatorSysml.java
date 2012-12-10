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
package org.topcased.requirement.document.doc2model;

import java.util.Collection;

import org.eclipse.uml2.uml.Stereotype;
import org.topcased.requirement.document.utils.Constants;

import doc2modelMapping.Doc2modelMappingFactory;
import doc2modelMapping.InjectionElement;
import doc2modelMapping.NewClassInjection;

/**
 * The Class Doc2ModelCreatorSysml.
 */
public class Doc2ModelCreatorSysml extends Doc2ModelCreatorUml
{

    /**
     * Instantiates a new doc2 model creator sysml.
     * 
     * @param stereotype the stereotype
     */
    public Doc2ModelCreatorSysml(Stereotype stereotype)
    {
        super(stereotype);
    }
    
    /**
     * Instantiates a new doc2 model creator sysml.
     * 
     * @param stereotypes the stereotypes collection
     */
    public Doc2ModelCreatorSysml(Collection<Stereotype> stereotypes)
    {
        super(stereotypes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.doc2model.requirement.doc2model.Doc2ModelCreatorUml#specificInject(doc2modelMapping.InjectionElement
     * )
     */
    @Override
    protected void specificInject(InjectionElement injection)
    {
        if (injection instanceof NewClassInjection)
        {
            NewClassInjection newClass = (NewClassInjection) injection;
            newClass.setSpecificNamespaceURI(Doc2ModelCreator.metaModels.get(Constants.UML_EXTENSION));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.doc2model.Doc2ModelCreatorUml#getInjectionElement(java.lang.String,
     * boolean)
     */
    public InjectionElement getInjectionElement(String stereo, boolean isFlat, boolean isSpreadsheet)
    {

        // Create class injection
        NewClassInjection injection = Doc2modelMappingFactory.eINSTANCE.createNewClassInjection();
        injection.setInstanceTarget("Requirement");
        injection.setAttributeForValue("name");
        injection.setAssociationOwning("packagedElement");
        injection.setOwningClass("Package");
        injection.setBrowseTopContainersForOwner(true);

        // Add stereotype
        if (stereo != null && stereo.length() > 0)
        {
            injection.setStereotypeToApply(stereo);
        }

        return injection;
    }

}
