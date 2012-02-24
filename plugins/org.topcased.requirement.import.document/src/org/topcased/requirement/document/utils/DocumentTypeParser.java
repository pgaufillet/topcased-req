/*****************************************************************************
 * Copyright (c) 2011 Atos.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Anass RADOUANI (Atos) anass.radouani@atos.net - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.document.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.core.runtime.Status;
import org.topcased.requirement.document.Activator;
import org.topcased.requirement.document.elements.AttributeRequirement;
import org.topcased.requirement.document.elements.Column;
import org.topcased.requirement.document.elements.Mapping;
import org.topcased.requirement.document.elements.RecognizedElement;
import org.topcased.requirement.document.elements.Regex;
import org.topcased.requirement.document.elements.Style;
import org.topcased.typesmodel.handler.IniManager;
import org.topcased.typesmodel.model.inittypes.DocumentType;
import org.topcased.typesmodel.model.inittypes.Type;
/**
 * Parse type Document to extract useful information for import requirement
 *
 */
public class DocumentTypeParser
{

    private DocumentType type;

    /**
     * Constructor
     * 
     * @param type the document type to parse
     */
    public DocumentTypeParser(DocumentType type)
    {
        this.type = type;
    }

    /**
     * Gets the end Text description
     * 
     * @return
     */
    public String getDescription()
    {
        if (type != null)
        {
            return type.getTextType();
        }
        return null;
    }

    /**
     * Gets the id
     * 
     * @param idType The id to get (use the Constants Class)
     * @return
     */
    public RecognizedElement getIdentification(String idType)
    {
        if (type == null || type.getId() == null)
        {
            return null;
        }
        RecognizedElement result = null;
        Type id = type.getId();

        if (Constants.REGEX_STYLE_TYPE.equals(idType))
        {

            if (id instanceof org.topcased.typesmodel.model.inittypes.Style)
            {
                if (((org.topcased.typesmodel.model.inittypes.Style) id).getExpression() != null)
                {
                    result = new Style(((org.topcased.typesmodel.model.inittypes.Style) id).getLabel(), ((org.topcased.typesmodel.model.inittypes.Style) id).getExpression());
                } 
                else
                {
                    result = new Style(((org.topcased.typesmodel.model.inittypes.Style) id).getLabel(),"");
                }
            }
            else if (id instanceof org.topcased.typesmodel.model.inittypes.Regex)
            {
                result = new Regex(((org.topcased.typesmodel.model.inittypes.Regex) id).getExpression());
            }
        } 
        else if (Constants.COLUMN_TYPE.equals(idType))
        {
            if (id instanceof org.topcased.typesmodel.model.inittypes.Column)
            {
                result = new Column(((org.topcased.typesmodel.model.inittypes.Column) id).getNumber(), ((org.topcased.typesmodel.model.inittypes.Column) id).getExpression());
            }
        }

        return result;
    }


    /**
     * Gets if its hierarchical or not
     * 
     * @return
     */
    public boolean getIsHiearachical()
    {
        if (type != null)
        {
            return type.isHierarchical();
        }
        return false;
    }

    /**
     * Gets the mapping
     * 
     * @param idType The id type (use the Constants Class)
     * @return
     */
    public Collection<Mapping> getMapping(String idType)
    {

        if (type == null)
        {
            return null;
        }

        Collection<Mapping> mapping = new ArrayList<Mapping>();
        
        if (Constants.REGEX_STYLE_TYPE.equals(idType))
        {

            List<org.topcased.typesmodel.model.inittypes.Style> styles = IniManager.getInstance().getStyles(type);
            List<org.topcased.typesmodel.model.inittypes.Regex> regex = IniManager.getInstance().getRegex(type);


            for (org.topcased.typesmodel.model.inittypes.Regex oneRegex : regex)
            {
                if (oneRegex.getExpression() != null && oneRegex.getName() != null)
                {
                    Regex newRegex = new Regex(oneRegex.getExpression());
                    AttributeRequirement regexAttribute = new AttributeRequirement(oneRegex.getName(), false,oneRegex.isIsText(), "Requirement");
                    mapping.add(new Mapping(newRegex, regexAttribute));
                }
                else
                {
                    Activator.getDefault().getLog().log(
                            new Status(Status.WARNING, Activator.PLUGIN_ID, "The regex " + oneRegex.getName() + " has been ignored because it doesn't contains a name or an expression"));
                }
            }

            for (org.topcased.typesmodel.model.inittypes.Style style : styles)
            {
                if (style.getName() != null && style.getLabel() != null)
                {
                    Style newStyle;
                    if (style.getExpression() != null)
                    {
                        newStyle = new Style(style.getLabel(), style.getExpression());
                    }
                    else
                    {
                        newStyle = new Style(style.getLabel(), "");
                    }
                    AttributeRequirement newAttribute = new AttributeRequirement(style.getName(), false, style.isIsText(), "Requirement");
                    mapping.add(new Mapping(newStyle, newAttribute));
                }
                else
                {
                    Activator.getDefault().getLog().log(
                            new Status(Status.WARNING, Activator.PLUGIN_ID, "The style " + style.getName() + " " + style.getName()
                                    + " has been ignored because it doesn't contains a name or an expression"));
                }
            }
        } 
        else if (Constants.COLUMN_TYPE.equals(idType))
        {
            List<org.topcased.typesmodel.model.inittypes.Column> columns = IniManager.getInstance().getColumns(type);
            for (org.topcased.typesmodel.model.inittypes.Column column : columns)
            {
                if (column.getExpression() != null && column.getName() != null)
                {
                    Column newColumn = new Column(column.getNumber(), column.getExpression());
                    AttributeRequirement newAttribute = new AttributeRequirement(column.getName(), false,column.isIsText(), "Requirement");
                    mapping.add(new Mapping(newColumn, newAttribute));
                    
                }
                else
                {
                    Activator.getDefault().getLog().log(
                            new Status(Status.WARNING, Activator.PLUGIN_ID, "Column with number " + column.getNumber() + " has been ignored because it doesn't contains a name or an expression"));
                }
            }
        }

        return mapping;
    }
}
