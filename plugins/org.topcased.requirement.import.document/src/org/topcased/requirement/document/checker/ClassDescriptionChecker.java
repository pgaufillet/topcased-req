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
 *  Anass RADOUANI (Atos) anass.radouani@atos.net - Refactoring the initial API implementation
 *
 *****************************************************************************/
package org.topcased.requirement.document.checker;

import java.util.Collection;
import java.util.regex.Matcher;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.topcased.doc2model.parser.Activator;

public class ClassDescriptionChecker extends AbstractDescriptionChecker
{

    protected void addDescription()
    {
        EObject eobject = process.getLatestCreatedElement();
        if (eobject instanceof org.eclipse.uml2.uml.Class && descriptionAttribute != null)
        {
            Class clazz = (org.eclipse.uml2.uml.Class) eobject;
            addDescription2Attribute(eobject, getLastDescription(clazz.getName()));
        }
    }

    protected void bufferDescription(String string)
    {
        if (pattern.matcher(string).matches())
        {
            EObject eo = process.getLatestCreatedElement();
            if (eo instanceof org.eclipse.uml2.uml.Class && descriptionAttribute != null)
            {
                addDescription2Attribute(eo, buffer.toString());
                buffer = null;
            }
            flagIdent = false;
        }
        else if (string.length() > 0 && buffer != null)
        {
            buffer.append(string);
            buffer.append("\n");
        }
    }
    
    protected void addDescription2Attribute(EObject eo, String text)
    {
        Class clazz = (Class) eo;
        Stereotype stereotype = clazz.getAppliedStereotype(descriptionAttribute.getSource());
        if (stereotype != null)
        {
            
            Property theProperty = stereotype.getAttribute(descriptionAttribute.getProperName(), null);
            if (theProperty != null)
            {
                if (theProperty.isMultivalued())
                {
                    Collection elements = (Collection) clazz.getValue(stereotype, descriptionAttribute.getProperName());
                    elements.add(getCorrespondingValue4UMLType(theProperty, text));
                }
                else
                {
                    Object value = clazz.getValue(stereotype, descriptionAttribute.getProperName());
                    
                    if (value instanceof String && !((String) value).equals(text))
                    {
                        text = ((String)value).concat("\n" + text);
                    }
                    clazz.setValue(stereotype, descriptionAttribute.getProperName(), text);
                }
            }
            else
            {
                Activator.log(new Exception(String.format("the element typed %s doesn't have attributes named %s", clazz.getClass().getName(), descriptionAttribute.getProperName())));
            }
        }
        else
        {
            Activator.log(new Exception(String.format("the element typed %s can't have stereotype attribute %s it doesn't have stereotype applied", clazz.getClass().getName(), text)));
        }
    }
    
    protected String getLastDescription(String ident)
    {
        if (idDescription.containsKey(ident))
        {
            return idDescription.get(ident);
        }
        else
        {
            Matcher desc = regDescription.matcher(ident);
            if (desc.matches())
            {
                try
                {
                    if (desc.groupCount() == 0)
                    {
                        lastDescription = desc.group(0);
                    }
                    else
                    {
                        lastDescription = desc.group(1);
                    }
                    idDescription.put(ident, lastDescription);
                    return lastDescription;
                }
                catch (Exception e) {
                }
            }
        }
        
        return null;
    }
    
    /**
     * Gets the corresponding value for EMF type.
     * 
     * @param attr the attr
     * @param value the value
     * 
     * @return the corresponding value4 type
     */
    protected Object getCorrespondingValue4UMLType(Property attr, String value)
    {
        if (attr.getType() != null)
        {
            String javaInstanceTypeName = attr.getType().getName();
            try
            {
                if (attr.getType() instanceof Enumeration)
                {
                    for (EnumerationLiteral l : ((Enumeration) attr.getType()).getOwnedLiterals())
                    {
                        if (value.equals(l.getName()))
                        {
                            return l ;
                        }
                    }
                }
                else if ("Boolean".equals(javaInstanceTypeName))
                {
                    if ("false".equals(value.toLowerCase()) || "true".equals(value.toLowerCase()))
                    {
                        return Boolean.valueOf(value);
                    }
                    else
                    {
                        Activator.log(new Exception(String.format("The value %s can't be assigned in Boolean, false is setted by default", value)));
                        return false;
                    }
                }
                else if ("Integer".equals(javaInstanceTypeName))
                {
                    return Integer.valueOf(value);
                }
                else
                {
                    return value;
                }
            }
            catch (NumberFormatException e)
            {
                Activator.log(new Exception(String.format("The value %s can't be assigned to this type : %s", value, javaInstanceTypeName)));
            }
        }
        return null;
    }

}
