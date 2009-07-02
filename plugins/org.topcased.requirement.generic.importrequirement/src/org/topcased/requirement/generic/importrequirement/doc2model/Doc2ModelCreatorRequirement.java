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
import org.topcased.requirement.generic.importrequirement.elements.Mapping;
import org.topcased.requirement.generic.importrequirement.elements.Regex;
import org.topcased.requirement.generic.importrequirement.elements.Style;
import org.topcased.requirement.generic.importrequirement.utils.Constants;

import doc2modelMapping.Attribute;
import doc2modelMapping.ColumnMatchAttribute;
import doc2modelMapping.CompositionInjection;
import doc2modelMapping.Doc2modelMappingFactory;
import doc2modelMapping.Hierarchy;
import doc2modelMapping.InjectionElement;
import doc2modelMapping.LinkedElement;
import doc2modelMapping.NewClassInjection;
import doc2modelMapping.TextFormatter;

/**
 * The Class Doc2ModelCreatorRequirement.
 */
public class Doc2ModelCreatorRequirement implements Doc2ModelCreatorSpecific
{

    /** The doc injection. */
    NewClassInjection docInjection;

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.doc2model.Doc2ModelCreatorSpecific#getInjectionModel()
     */
    public InjectionElement getInjectionModel()
    {

        // Create project injection
        NewClassInjection injection = Doc2modelMappingFactory.eINSTANCE.createNewClassInjection();
        injection.setInstanceTarget("RequirementProject");
        injection.setAttributeForValue("identifier");
        injection.setSpecificNamespaceURI(Constants.METAMODEL_TRACEABILITY);

        // Add text formatter date
        TextFormatter createTextFormatter = Doc2modelMappingFactory.eINSTANCE.createTextFormatter();
        createTextFormatter.setPattern("project generated at [date]");
        injection.setStringFormat(createTextFormatter);

        // Add upstreamModel injection
        NewClassInjection upstreamInjection = Doc2modelMappingFactory.eINSTANCE.createNewClassInjection();
        upstreamInjection.setInstanceTarget("UpstreamModel");
        upstreamInjection.setAttributeForValue("ident");
        upstreamInjection.setAssociationOwning("upstreamModel");
        upstreamInjection.setBrowseTopContainersForOwner(true);
        upstreamInjection.setOwningClass("RequirementProject");
        upstreamInjection.setSpecificNamespaceURI(Constants.METAMODEL_TRACEABILITY);
        injection.getStandardOptionalInjections().add(upstreamInjection);

        // Add text formatter upstreamModel
        TextFormatter upstreamTextFormatter = Doc2modelMappingFactory.eINSTANCE.createTextFormatter();
        upstreamTextFormatter.setPattern("UpstreamModel");
        upstreamInjection.setStringFormat(upstreamTextFormatter);

        // Add document injection
        docInjection = Doc2modelMappingFactory.eINSTANCE.createNewClassInjection();
        docInjection.setInstanceTarget("Document");
        docInjection.setAttributeForValue("ident");
        docInjection.setAssociationOwning("documents");
        docInjection.setBrowseTopContainersForOwner(true);
        docInjection.setName("document");
        docInjection.setOwningClass("UpstreamModel");
        docInjection.setSpecificNamespaceURI(Constants.METAMODEL_TRACEABILITY);
        upstreamInjection.getStandardOptionalInjections().add(docInjection);

        // Add text formatter document name
        TextFormatter docTextFormatter = Doc2modelMappingFactory.eINSTANCE.createTextFormatter();
        docTextFormatter.setPattern("[document_name]");
        docInjection.setStringFormat(docTextFormatter);

        return injection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.doc2model.Doc2ModelCreatorSpecific#getInjectionElement(java.lang.String,
     * boolean)
     */
    public InjectionElement getInjectionElement(String stereo, boolean isFlat)
    {
        // Create composition injection requirement
        InjectionElement injectionE = null;
        if (isFlat)
        {
            CompositionInjection injection = Doc2modelMappingFactory.eINSTANCE.createCompositionInjection();
            injectionE = injection;
            injection.setAssociationName("children");
            injection.setAttributeName("ident");
            injection.setDependsWith(docInjection);
            injection.setName("requirement");
            injection.setNewInstanceForEachComposition(true);
            injection.setOwningClass("Requirement");
            injection.setSpecificNamespaceURI(Constants.METAMODEL_TRACEABILITY);
        }
        else
        {
            NewClassInjection injection = Doc2modelMappingFactory.eINSTANCE.createNewClassInjection();
            injectionE = injection;
            injection.setAssociationOwning("children");
            injection.setAttributeForValue("ident");
            injection.setInstanceTarget("Requirement");
            injection.setSpecificNamespaceURI(Constants.METAMODEL_TRACEABILITY);
            injection.setName("requirement");
            injection.setBrowseTopContainersForOwner(true);
            injection.setOwningClass("HierarchicalElement");
        }
        return injectionE;
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
        Attribute att = null;
        Doc2modelMappingFactory factory = Doc2modelMappingFactory.eINSTANCE;
        if (m.getElement() instanceof Style)
        {
            Style style = (Style) m.getElement();
            att = factory.createStyleAttribute();
            att.setAttributeValue(style.getStyle());
        }
        else if (m.getElement() instanceof Regex)
        {
            att = factory.createRegExAttribute();
            Regex regex = (Regex) m.getElement();
            att.setAttributeValue(regex.getRegex());
        }
        if (att != null)
        {
            att.setInjection(m.getAttribute().getInjection(element));
        }
        return att;
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
        ColumnMatchAttribute attribute = Doc2modelMappingFactory.eINSTANCE.createColumnMatchAttribute();
        if (m.getElement() instanceof Column)
        {
            Column col = (Column) m.getElement();
            attribute.setNumColumn(col.getColumn());
            attribute.setRegExToMatch(col.getRegex());
            attribute.setInjection(m.getAttribute().getInjection(element));
        }
        return attribute;
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
        h.getStyleValue().add("titre 1");

        // Add class injection package
        CompositionInjection injection = Doc2modelMappingFactory.eINSTANCE.createCompositionInjection();
        injection.setAssociationName("children");
        injection.setAttributeName("ident");
        injection.setNewInstanceForEachComposition(true);
        injection.setOwningClass("Section");
        injection.setDependsWith(docInjection);
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
            h.getStyleValue().add("titre " + level);

            // Add class injection package
            NewClassInjection injection = Doc2modelMappingFactory.eINSTANCE.createNewClassInjection();
            injection.setAssociationOwning("children");
            injection.setAttributeForValue("ident");
            injection.setBrowseTopContainersForOwner(false);
            injection.setInstanceTarget("Section");
            injection.setOwningClass("Section");
            h.setInjection(injection);

            // Add the subHiearchy
            hierarchy.setSubHierarchy(h);

            // Call recursively
            level++;
            getAllHierachy(level, h);
        }
    }

}
