/*****************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
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
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.topcased.requirement.core.internal.RequirementCorePlugin;

/**
 * An helper class working around the Requirements naming's format preference page.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public final class NamingRequirementPreferenceHelper
{

    public static final String REQUIREMENT_NAMING_FORMAT = "namingFormatRequirement";

    public static final String REQUIREMENT_STEP_INDEX = "requirementStepIndex";

    public static final String REQUIREMENT_COUNTING_ALGORITHM = "requirementAlgorithm";

    private static final String DEFAULT_NAMING_FORMAT = "E_{project}_{hierarchical element}_{number}";

    private static final String DEFAULT_COUNTING_ALGORITHM = "Default Algorithm";

    private static final int DEFAULT_INDEX_STEP = 10;

    public static final List<String> KEY_WORDS = new ArrayList<String>();

    static IPreferenceStore store = RequirementCorePlugin.getDefault().getPreferenceStore();

    private NamingRequirementPreferenceHelper()
    {
        // avoid to instantiate this class
    }

    /**
     * Gets the default index step.
     * 
     * @return the default index step
     */
    public static int getDefaultIndexStep()
    {
        return DEFAULT_INDEX_STEP;
    }

    /**
     * Gets the default naming format.
     * 
     * @return the default naming format
     */
    public static String getDefaultNamingFormat()
    {
        return DEFAULT_NAMING_FORMAT;
    }

    /**
     * Gets the default counting algorithm in charge of computing the next requirement index.
     * 
     * @return the default naming format
     */
    public static String getDefaultCountingAlgorithm()
    {
        return DEFAULT_COUNTING_ALGORITHM;
    }

    public static void addKeyWord(List<String> words)
    {
        for (String word : words)
        {
            if (!KEY_WORDS.contains(word))
            {
                KEY_WORDS.add(word);
            }
        }
    }

    /**
     * Get the requirement step stored in the PreferenceStore
     * 
     * @return the step if there is one or default step is there is none
     */
    public static int getRequirementStep()
    {
        try
        {
            int step = store.getInt(REQUIREMENT_STEP_INDEX);
            return step > 0 ? step : DEFAULT_INDEX_STEP;
        }
        catch (NumberFormatException e)
        {
            RequirementCorePlugin.log(e);
        }
        return DEFAULT_INDEX_STEP;
    }

    /**
     * Get the requirement counting algorithm name stored in the PreferenceStore
     * 
     * @return the name if there is one or default algorithm name is there is none
     */
    public static String getCurrentAlgorithm()
    {
        String nameStored = store.getString(REQUIREMENT_COUNTING_ALGORITHM);
        return !"".equals(nameStored) ? nameStored : DEFAULT_COUNTING_ALGORITHM;
    }
}
