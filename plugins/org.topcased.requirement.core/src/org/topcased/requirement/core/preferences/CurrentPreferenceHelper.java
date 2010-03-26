/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.preferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;

import org.eclipse.jface.preference.IPreferenceStore;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.AttributesType;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.DefaultAttributeValue;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * An Helper working around the preference page.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public final class CurrentPreferenceHelper
{
    public static final String STRING_DEFAULT_VALUE = "(default)";

    public static final String STRING_IS_ALLOCATE = "(allocate)";

    public static final String STRING_IS_TEXT = "(text)";

    public static final String STRING_IS_REFERENCE = "(reference)";

    public static final String STRING_IS_LINK = "(link)";

    public static final String PREFERENCE_DELIMITER_ATTRIBUTE = "||";

    public static final String PREFERENCE_DELIMITER_VALUE = ";;";

    public static final String CURRENT_ATTRIBUTES_STORE = "defaultCurrentAttributes";

    public static final String[] DEFAULT_ATTRIBUTES = {

    "#Maturity" + STRING_IS_TEXT,

    "#Allocate" + STRING_IS_ALLOCATE,

    "#Link_to" + STRING_IS_LINK,

    "#Covered_by" + STRING_IS_TEXT,

    "#Justify" + STRING_IS_TEXT

    };

    private static final String[][] DEFAULT_VALUES = { {"TBD" + STRING_DEFAULT_VALUE, "TBC"}, {}, {}, {}, {}};

    private static IPreferenceStore preferenceStorePlugIn = RequirementCorePlugin.getDefault().getPreferenceStore();

    /**
     * Constructor
     * 
     */
    private CurrentPreferenceHelper()
    {
        // prevent instanciation
    }

    public static Boolean isDefaultValue(String value)
    {
        return value.endsWith(STRING_DEFAULT_VALUE);
    }

    public static Boolean isAllocate(String value)
    {
        return value.endsWith(STRING_IS_ALLOCATE);
    }

    public static Boolean isLink(String value)
    {
        return value.endsWith(STRING_IS_LINK);
    }

    public static Boolean isText(String value)
    {
        return value.endsWith(STRING_IS_TEXT);
    }

    public static Boolean isReference(String value)
    {
        return value.endsWith(STRING_IS_REFERENCE);
    }

    public static String getDefaultAttributeValue(String value)
    {
        int posDefault = value.indexOf(CurrentPreferenceHelper.STRING_DEFAULT_VALUE);
        return (String) value.subSequence(0, posDefault > 0 ? posDefault : (value.length()));
    }

    /**
     * Gets the requirement configuration defined at the workspace level.
     * 
     * @return an model objetc ok kind {@link AttributeConfiguration}.
     */
    public static AttributeConfiguration getConfigurationInWorkspace()
    {
        AttributeConfiguration attrConfiuration = RequirementFactory.eINSTANCE.createAttributeConfiguration();
        String attributeLocations = preferenceStorePlugIn.getString(CurrentPreferenceHelper.CURRENT_ATTRIBUTES_STORE);
        Map<String, Vector<String>> currDefaultAttributes = deserialize(attributeLocations);
        Set<Entry<String, Vector<String>>> entrySet = currDefaultAttributes.entrySet();

        // Update the table
        for (Entry<String, Vector<String>> anEntry : entrySet)
        {
            // Add an attribute
            ConfiguratedAttribute attribute = RequirementFactory.eINSTANCE.createConfiguratedAttribute();
            attribute.setName(getLabelAttribute(anEntry.getKey()));

            if (isText(anEntry.getKey()))
            {
                attribute.setType(AttributesType.TEXT);
            }
            else if (isAllocate(anEntry.getKey()))
            {
                attribute.setType(AttributesType.ALLOCATE);
            }
            else if (isLink(anEntry.getKey()))
            {
                attribute.setType(AttributesType.LINK);
            }
            else if (isReference(anEntry.getKey()))
            {
                attribute.setType(AttributesType.OBJECT);
            }

            if (anEntry.getValue().size() > 0)
            {
                DefaultAttributeValue defValue = RequirementFactory.eINSTANCE.createDefaultAttributeValue();

                // Add an attribute value
                for (String value : anEntry.getValue())
                {
                    AttributeValue attValue = RequirementFactory.eINSTANCE.createAttributeValue();

                    if (isDefaultValue(value))
                    {
                        // value is the default value, remove the "(default)" string
                        attValue.setValue(getDefaultAttributeValue(value));
                        defValue.setValue(attValue);
                        attribute.setDefaultValue(defValue);
                    }
                    else
                    {
                        attValue.setValue(value);
                    }
                    attribute.getListValue().add(attValue);
                }
            }
            attrConfiuration.getListAttributes().add(attribute);
        }
        return attrConfiuration;
    }

    /**
     * Ensures serialization of preference page content.
     * 
     * @param elements The Map to serialize.
     * @return A string serialized
     */
    public static String serialize(Map<String, Vector<String>> elements)
    {
        StringBuffer buffer = new StringBuffer();
        Set<Entry<String, Vector<String>>> entrySet = elements.entrySet();
        for (Entry<String, Vector<String>> anEntry : entrySet)
        {
            buffer.append(anEntry.getKey());
            if (anEntry.getValue() != null)
            {
                for (String element : anEntry.getValue())
                {
                    buffer.append(PREFERENCE_DELIMITER_VALUE);
                    buffer.append(element);
                }
            }
            buffer.append(PREFERENCE_DELIMITER_ATTRIBUTE);
        }
        return buffer.toString();
    }

    public static Map<String, Vector<String>> deserialize(String preferenceValue)
    {
        Map<String, Vector<String>> elements = new TreeMap<String, Vector<String>>();

        StringTokenizer tokenizer = new StringTokenizer(preferenceValue, PREFERENCE_DELIMITER_ATTRIBUTE);
        int countTokenizer = tokenizer.countTokens();

        for (int i = 0; i < countTokenizer; i++)
        {
            StringTokenizer tokenizerValues = new StringTokenizer(tokenizer.nextToken(), PREFERENCE_DELIMITER_VALUE);
            int countTokenizerValues = tokenizerValues.countTokens();

            Vector<String> listValue = new Vector<String>();
            String key = tokenizerValues.nextToken();

            for (int j = 1; j < countTokenizerValues; j++)
            {
                listValue.add(tokenizerValues.nextToken());
            }

            elements.put(key, listValue);
        }
        return elements;
    }

    /**
     * Gets the default values
     * 
     * @return a Map containing all couples of default values.
     */
    public static Map<String, Vector<String>> getDefaultValues()
    {
        Map<String, Vector<String>> defaults = new TreeMap<String, Vector<String>>();
        int i = 0;
        for (String att : DEFAULT_ATTRIBUTES)
        {
            Vector<String> listValue = new Vector<String>();
            String[] str = DEFAULT_VALUES[i++];
            for (int j = 0; j < str.length; j++)
            {
                listValue.add(str[j]);
            }
            defaults.put(att, listValue);
        }
        return defaults;
    }

    public static String getLabelAttribute(String value)
    {
        String result = "";
        int pos;

        pos = value.indexOf(CurrentPreferenceHelper.STRING_IS_ALLOCATE);
        if (pos > 0)
        {
            result = (String) value.subSequence(0, pos > 0 ? pos : (value.length()));
        }
        else
        {
            pos = value.indexOf(CurrentPreferenceHelper.STRING_IS_LINK);
            if (pos > 0)
            {
                result = (String) value.subSequence(0, pos > 0 ? pos : (value.length()));
            }
            else
            {
                pos = value.indexOf(CurrentPreferenceHelper.STRING_IS_TEXT);
                if (pos > 0)
                {
                    result = (String) value.subSequence(0, pos > 0 ? pos : (value.length()));
                }
                else
                {
                    pos = value.indexOf(CurrentPreferenceHelper.STRING_IS_REFERENCE);
                    if (pos > 0)
                    {
                        result = (String) value.subSequence(0, pos > 0 ? pos : (value.length()));
                    }
                    else
                    {
                        result = value;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Extracts all textual values that can receive a text attribute.
     * 
     * @param text The text attribute
     * @return A collection of potential values
     */
    public static Collection<String> getTextualValues(TextAttribute text)
    {
        List<String> result = new ArrayList<String>();
        if (text != null)
        {
            AttributeConfiguration configuration = RequirementUtils.getAttributeConfiguration(text.eResource());
            for (ConfiguratedAttribute attribute : configuration.getListAttributes())
            {
                if (attribute.getName().equals(text.getName()) && attribute.getType().equals(AttributesType.TEXT))
                {
                    for (AttributeValue values : attribute.getListValue())
                    {
                        result.add(values.getValue());
                    }
                }
            }
        }
        return result;
    }

    public static Attribute getAttribute(Requirement requirement, String attName)
    {
        for (Attribute att : requirement.getAttribute())
        {
            if (att.getName().equals(attName))
            {
                return att;
            }
        }
        return null;

    }

}
