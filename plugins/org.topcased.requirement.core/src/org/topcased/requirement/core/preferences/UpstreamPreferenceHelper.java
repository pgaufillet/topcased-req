/***********************************************************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe MERTZ (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.preferences;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * An Helper working around the preference page.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public final class UpstreamPreferenceHelper
{
    public static final String PREFERENCE_DELIMITER = "||"; //$NON-NLS-1$

    public static final String UPSTREAM_ATTRIBUTES_STORE = "defaultUpstreamAttributes"; //$NON-NLS-1$

    public static final String[] DEFAULT_ATTRIBUTES = {"#Maturity", "#Allocate", "#Link_to", "#Covered_by", "#Justify"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    /**
     * Constructor
     */
    private UpstreamPreferenceHelper()
    {
        // prevent from instanciation
    }

    /**
     * Deserializes an array of string input format : "elem1 PREFERENCE_DELIMITER elem2 PREFERENCE_DELIMITER elem3"
     * 
     * @param preferenceValue The String to deserialize
     */
    public static Set<String> deserialize(String preferenceValue)
    {
        StringTokenizer tokenizer = new StringTokenizer(preferenceValue, PREFERENCE_DELIMITER);
        int tokenCount = tokenizer.countTokens();
        Set<String> elements = new LinkedHashSet<String>(8);
        for (int i = 0; i < tokenCount; i++)
        {
            elements.add(tokenizer.nextToken());
        }
        return elements;
    }

    /**
     * Serialises a Set of string output format : "elem1 PREFERENCE_DELIMITER elem2 PREFERENCE_DELIMITER elem3"
     * 
     * @param A Set of String representing the items to serialize into a single String
     * @return a serialized String containing all the values
     */
    public static String serialize(Set<String> elements)
    {
        StringBuffer buffer = new StringBuffer();
        for (String element : elements)
        {
            buffer.append(element);
            buffer.append(PREFERENCE_DELIMITER);
        }
        return buffer.toString();
    }

    /**
     * Gets default values
     * 
     * @return a Set ok keys
     */
    public static Set<String> getDefaultValues()
    {
        Set<String> defaults = new LinkedHashSet<String>(8);
        for (String element : DEFAULT_ATTRIBUTES)
        {
            defaults.add(element);
        }
        return defaults;
    }

}
