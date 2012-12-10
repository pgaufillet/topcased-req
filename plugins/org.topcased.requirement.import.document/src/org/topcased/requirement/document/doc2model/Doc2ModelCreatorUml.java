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
import java.util.HashMap;

import org.eclipse.uml2.uml.Stereotype;
import org.topcased.requirement.document.elements.Column;
import org.topcased.requirement.document.elements.Mapping;
import org.topcased.requirement.document.elements.RecognizedElement;
import org.topcased.requirement.document.elements.RecognizedTree;
import org.topcased.requirement.document.elements.Regex;
import org.topcased.requirement.document.elements.Style;

import doc2modelMapping.Attribute;
import doc2modelMapping.ColumnMatchAttribute;
import doc2modelMapping.Doc2modelMappingFactory;
import doc2modelMapping.Hierarchy;
import doc2modelMapping.InjectionElement;
import doc2modelMapping.LinkedElement;
import doc2modelMapping.NewClassInjection;
import doc2modelMapping.RegExAttribute;
import doc2modelMapping.StyleAttribute;
import doc2modelMapping.TextFormatter;

/**
 * The Class Doc2ModelCreatorUml.
 */
public class Doc2ModelCreatorUml implements Doc2ModelCreatorSpecific
{

    /** The stereotype. */
    Stereotype stereotype;

    /** The parent regex. */
    HashMap<RecognizedElement, doc2modelMapping.RegEx> parentRegex = new HashMap<RecognizedElement, doc2modelMapping.RegEx>();

    /** The stereotypes collection. */
    Collection<Stereotype> stereotypes;

    /**
     * Instantiates a new doc2 model creator uml.
     * 
     * @param s the stereotype
     */
    public Doc2ModelCreatorUml(Stereotype s)
    {
        stereotype = s;
    }
    
    /**
     * Instantiates a new doc2 model creator uml.
     * 
     * @param s the stereotypes Collection
     */
    public Doc2ModelCreatorUml(Collection<Stereotype> s)
    {
        stereotypes = s;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.doc2model.Doc2ModelCreatorSpecific#getInjectionModel()
     */
    public InjectionElement getInjectionModel()
    {
        NewClassInjection injection = Doc2modelMappingFactory.eINSTANCE.createNewClassInjection();
        injection.setInstanceTarget("Model");
        injection.setAttributeForValue("name");

        TextFormatter createTextFormatter = Doc2modelMappingFactory.eINSTANCE.createTextFormatter();
        createTextFormatter.setPattern("[document_name]");
        injection.setStringFormat(createTextFormatter);
        specificInject(injection);
        return injection;
    }

    /**
     * Specific inject.
     * 
     * @param injection the injection
     */
    protected void specificInject(InjectionElement injection)
    {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.doc2model.Doc2ModelCreatorSpecific#getInjectionElement(java.lang.String,
     * boolean)
     */
    public InjectionElement getInjectionElement(String stereo, boolean isFlat,boolean isSpreadsheet)
    {
        // Create class injection
        NewClassInjection injection = Doc2modelMappingFactory.eINSTANCE.createNewClassInjection();
        injection.setInstanceTarget("Class");
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.doc2model.requirement.doc2model.Doc2ModelCreatorSpecific#getAttribute(org.topcased.doc2model.requirement
     * .elements.Mapping, doc2modelMapping.LinkedElement)
     */
    public Attribute getAttribute(Mapping m, LinkedElement element)
    {

        if (m.getElement() instanceof Style)
        {
            Style s = (Style) m.getElement();
            // create the style attribute
            StyleAttribute att = Doc2modelMappingFactory.eINSTANCE.createStyleAttribute();
            att.setAttributeValue(s.getStyle());

            // Add the injection
            att.setInjection(m.getAttribute().getInjection(element));

            return att;
        }
        else if (m.getElement() instanceof Regex && m.getElement().getParent() instanceof RecognizedTree)
        {
            Regex r = (Regex) m.getElement();
            // create the regex attribute
            RegExAttribute att = Doc2modelMappingFactory.eINSTANCE.createRegExAttribute();
            att.setAttributeValue(r.getRegex());

            // Add the injection
            att.setInjection(m.getAttribute().getInjection(element));
            return att;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.doc2model.requirement.doc2model.Doc2ModelCreatorSpecific#getColumnAttribute(org.topcased.doc2model
     * .requirement.elements.Mapping, doc2modelMapping.LinkedElement)
     */
    public Attribute getColumnAttribute(Mapping m, LinkedElement element)
    {

        // Create Column match attribute
        ColumnMatchAttribute colMatch = Doc2modelMappingFactory.eINSTANCE.createColumnMatchAttribute();
        if (m.getElement() instanceof Column)
        {
            Column col = (Column) m.getElement();
            colMatch.setNumColumn(col.getColumn());
            colMatch.setRegExToMatch(col.getRegex());
        }

        // Create Attribute Injection
        colMatch.setInjection(m.getAttribute().getInjection(element));
        return colMatch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.doc2model.Doc2ModelCreatorSpecific#getAllHierachy()
     */
    public Hierarchy getAllHierachy()
    {
        // Create Hierarchy
        Hierarchy h = Doc2modelMappingFactory.eINSTANCE.createHierarchy();
        h.getStyleValue().add("Heading 1");
        h.getStyleValue().add("heading 1");
        h.getStyleValue().add("Titre 1");

        // Add class injection package
        NewClassInjection injection = Doc2modelMappingFactory.eINSTANCE.createNewClassInjection();
        injection.setAssociationOwning("packagedElement");
        injection.setAttributeForValue("name");
        injection.setBrowseTopContainersForOwner(false);
        injection.setInstanceTarget("Package");
        injection.setOwningClass("Package");
        h.setInjection(injection);

        // Add sub hierarchy
        getAllHierachy(2, h);

        return h;
    }

    /**
     * Gets the all hierachy.
     * 
     * @param level the level
     * @param hierarchy the hierarchy
     * 
     * @return the all hierachy
     */
    public void getAllHierachy(int level, Hierarchy hierarchy)
    {
        if (level <= 10)
        {
            // Create Hierarchy
            Hierarchy h = Doc2modelMappingFactory.eINSTANCE.createHierarchy();
            h.getStyleValue().add("Heading " + level);
            h.getStyleValue().add("heading " + level);
            h.getStyleValue().add("Titre " + level);

            // Add class injection package
            NewClassInjection injection = Doc2modelMappingFactory.eINSTANCE.createNewClassInjection();
            injection.setAssociationOwning("packagedElement");
            injection.setAttributeForValue("name");
            injection.setBrowseTopContainersForOwner(false);
            injection.setInstanceTarget("Package");
            injection.setOwningClass("Package");
            h.setInjection(injection);

            // Add the subHiearchy
            hierarchy.setSubHierarchy(h);

            // Call recursively
            level++;
            getAllHierachy(level, h);
        }
    }

}
