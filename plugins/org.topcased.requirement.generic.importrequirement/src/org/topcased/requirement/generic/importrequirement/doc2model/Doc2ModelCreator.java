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

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Stereotype;
import org.topcased.requirement.generic.importrequirement.elements.Attribute;
import org.topcased.requirement.generic.importrequirement.elements.Mapping;
import org.topcased.requirement.generic.importrequirement.elements.OwnerElement;
import org.topcased.requirement.generic.importrequirement.elements.RecognizedElement;
import org.topcased.requirement.generic.importrequirement.elements.RecognizedTree;
import org.topcased.requirement.generic.importrequirement.elements.Regex;
import org.topcased.requirement.generic.importrequirement.elements.Style;
import org.topcased.requirement.generic.importrequirement.utils.Constants;

import doc2modelMapping.DependantInjection;
import doc2modelMapping.Doc2modelMappingFactory;
import doc2modelMapping.InjectionElement;
import doc2modelMapping.LinkedElement;
import doc2modelMapping.RegExAttribute;
import doc2modelMapping.StyleAttribute;
import doc2modelMapping.TextFormatter;
import doc2modelMapping.doc2model;

/**
 * The Class Doc2ModelCreator.
 */
public class Doc2ModelCreator
{

    /** The list mapping. */
    private Collection<Mapping> listMapping;

    /** The model type. */
    private String modelType;

    /** The is spreadsheet. */
    private boolean isSpreadsheet;

    /** The profile. */
    private String profile;

    /** The stereotype. */
    private Stereotype stereotype;

    /** The is hierarchical. */
    private boolean isHierarchical;

    /** The identification. */
    private RecognizedElement identification;

    /** The creator specific. */
    private Doc2ModelCreatorSpecific creatorSpecific;

    /** The Constant metaModels. */
    static final HashMap<String, String> metaModels = new HashMap<String, String>();

    private String pathForDebug;
    static
    {
        metaModels.put(Constants.UML_EXTENSION, "http://www.eclipse.org/uml2/2.1.0/UML");
        metaModels.put(Constants.SYSML_EXTENSION, "http://www.topcased.org/2.0/sysML");
        metaModels.put(Constants.REQUIREMENT_EXTENSION, "http://org.topcased.requirement/1.0");
    }
    
    /**
     * Instantiates a new doc2 model creator.
     * 
     * @param listMapping the list mapping
     * @param modelType the model type
     * @param inputType the input type
     * @param profile the profile
     * @param stereotype the stereotype
     * @param isHierarchical the is hierarchical
     * @param identification the identification
     */
    public Doc2ModelCreator(Collection<Mapping> listMapping, String modelType, boolean inputType, String profile, Stereotype stereotype, boolean isHierarchical, RecognizedElement identification)
    {
        this(listMapping,modelType,inputType,profile,stereotype,isHierarchical,identification,null);
    }
    
    /**
     * Instantiates a new doc2 model creator.
     * 
     * @param listMapping the list mapping
     * @param modelType the model type
     * @param inputType the input type
     * @param profile the profile
     * @param stereotype the stereotype
     * @param isHierarchical the is hierarchical
     * @param identification the identification
     * @param pathFordebug the path folder to store the doc2model mapping
     */
    public Doc2ModelCreator(Collection<Mapping> listMapping, String modelType, boolean inputType, String profile, Stereotype stereotype, boolean isHierarchical, RecognizedElement identification,String pathFordebug)
    {
        super();
        this.listMapping = listMapping;
        this.modelType = modelType;
        this.isSpreadsheet = inputType;
        this.profile = profile;
        this.stereotype = stereotype;
        this.isHierarchical = isHierarchical;
        this.identification = identification;
        this.pathForDebug = pathFordebug;

        if (Constants.UML_EXTENSION.equals(modelType))
        {
            creatorSpecific = new Doc2ModelCreatorUml(stereotype);
        }
        else if (Constants.SYSML_EXTENSION.equals(modelType))
        {
            creatorSpecific = new Doc2ModelCreatorSysml(stereotype);
        }
        else
        {
            creatorSpecific = new Doc2ModelCreatorRequirement();
        }
    }

    /**
     * Creates the doc2 model.
     * 
     * @return the doc2model
     */
    public doc2model createDoc2Model()
    {
        if (creatorSpecific == null)
        {
            // throw exception
            return null;
        }
        // Create the doc2model
        doc2model doc = Doc2modelMappingFactory.eINSTANCE.createdoc2model();

        // create a resource
        Resource r = null ;
        if (pathForDebug != null && pathForDebug.length() > 0)
        {
            pathForDebug = pathForDebug.replace("\\", "/");
            if (!pathForDebug.endsWith("/"))
            {
                pathForDebug += "/";
            }
            ResourceSet set = new ResourceSetImpl();
            r =
                set.createResource(URI.createFileURI(pathForDebug + "debug.doc2modelmapping"));
            r.getContents().add(doc);
        }

        // Set the Extension (sysml, uml or requirement)
        doc.setExtension(modelType);

        // Set the metamodel
        doc.setMetamodelURI(metaModels.get(modelType));

        // Set the profile
        if (!Constants.REQUIREMENT_EXTENSION.equals(modelType) && profile != null)
        {
            doc.setProfileURI(profile);
        }

        // Add the injection for the model
        doc.setInjection(creatorSpecific.getInjectionModel());

        // Add the link element to recognize
        LinkedElement linkedElement = LinkedElementResolver.getLinkedElement(identification);
        if (linkedElement != null)
        {
            doc.getLinks().add(linkedElement);
            // Add the class element injection
            String source = null;
            if (stereotype != null)
            {
                source = stereotype.getName();
            }
            InjectionElement injectionElement = creatorSpecific.getInjectionElement(source, !isHierarchical);
            linkedElement.setInjection(injectionElement);
            if (identification instanceof Style)
            {
                Style s = (Style) identification;
                if (s.getRegex() != null && s.getRegex().length() > 0)
                {
                    InjectionElement tmp = injectionElement ;
                    applytextFormatter(tmp,s.getRegex());
                    
                }
            }
        }

        // Add the hierarchy
        if (!isSpreadsheet && isHierarchical)
        {
            doc.setHierarchy(creatorSpecific.getAllHierachy());
        }

        // Add all attributes
        getAllAttributes(linkedElement);

        // Save the Resource
        if (r != null && pathForDebug != null)
        {
            try
            {
                r.save(Collections.EMPTY_MAP);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return doc;
    }

    private void applytextFormatter(InjectionElement tmp,String regex)
    {
        TextFormatter formatter = Doc2modelMappingFactory.eINSTANCE.createTextFormatter();
        formatter.setRegexForConditionOrGroup(regex);
        if (tmp.getStringFormat() == null)
        {
            tmp.setStringFormat(formatter);        
        }
        for (InjectionElement i : tmp.getStandardOptionalInjections())
        {
            applytextFormatter(i,regex);
        }
    }

    /**
     * Gets the all attributes.
     * 
     * @param linkedElement the linked element
     * 
     * @return the all attributes
     */
    private void getAllAttributes(LinkedElement linkedElement)
    {
        if (isSpreadsheet)
        {
            for (Iterator<Mapping> it = listMapping.iterator(); it.hasNext();)
            {
                Mapping m = it.next();
                doc2modelMapping.Attribute attribute = null;
                attribute = creatorSpecific.getColumnAttribute(m, linkedElement);
                if (attribute != null)
                {
                    linkedElement.getAttributes().add(attribute);
                }
            }
        }
        else
        {
            List<OwnerElement> parents = new LinkedList<OwnerElement>();
            // Create new ordered list
            for (Iterator<Mapping> it = listMapping.iterator(); it.hasNext();)
            {
                Mapping m = it.next();
                doc2modelMapping.Attribute attribute = null;
                // if it is a simple regex or style
                if (m.getElement() instanceof Style || (m.getElement() instanceof Regex && m.getElement().getParent() instanceof RecognizedTree))
                {
                    // Manage it
                    attribute = creatorSpecific.getAttribute(m, linkedElement);
                    if (attribute != null)
                    {
                        if (attribute instanceof StyleAttribute && m.getElement() instanceof Style)
                        {
                            Style s = (Style) m.getElement();
                            StyleAttribute styleAtt = (StyleAttribute) attribute;
                            InjectionElement inj = styleAtt.getInjection();
                            if (inj != null)
                            {
                                if (s.getRegex() != null && s.getRegex().length() > 0)
                                {
                                    InjectionElement tmp = inj ;
                                    applytextFormatter(tmp, s.getRegex());
                                }
                            }
                        }
                        linkedElement.getAttributes().add(attribute);
                    }
                }
                else
                {
                    // if the parent has already been added
                    if (m.getElement().getParent() != null && !parents.contains(m.getElement().getParent()))
                    {
                        // Add to the parent
                        parents.add(m.getElement().getParent());
                    }
                }
            }
            // Manage Complex Regex
            manageComplexRegex(linkedElement, parents);
        }
    }

    /**
     * Manage complex regex.
     * 
     * @param linkedElement the linked element
     * @param parents the parents
     */
    private void manageComplexRegex(LinkedElement linkedElement, List<OwnerElement> parents)
    {
        for (Iterator<OwnerElement> it = parents.iterator(); it.hasNext();)
        {
            OwnerElement parent = it.next();
            if (parent instanceof Regex)
            {
                Regex parentRegex = (Regex) parent;

                // create the regex attribute
                RegExAttribute att = Doc2modelMappingFactory.eINSTANCE.createRegExAttribute();
                att.setAttributeValue(parentRegex.getRegex());

                // Add child injection
                boolean first = true;
                for (RecognizedElement element : parentRegex.getChildren())
                {
                    Attribute a = getCorrespondingAttribute(element);
                    if (first)
                    {
                        if (a != null)
                        {
                            att.setInjection(a.getInjection(linkedElement));
                        }
                        first = false;
                    }
                    else
                    {
                        if (a != null)
                        {
                            att.getSecondaryInjectionsForGroups().add((DependantInjection) a.getInjection(linkedElement));
                        }
                    }
                    if (att != null)
                    {
                        linkedElement.getAttributes().add(att);
                    }
                }
            }
        }
    }

    /**
     * Gets the corresponding attribute.
     * 
     * @param element the element
     * 
     * @return the corresponding attribute
     */
    private Attribute getCorrespondingAttribute(RecognizedElement element)
    {
        for (Mapping m : listMapping)
        {
            if (m.getElement() == element)
            {
                return m.getAttribute();
            }
        }
        return null;
    }
}
