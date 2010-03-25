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
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.topcased.requirement.core.RequirementCorePlugin;

/**
 * An Helper working around the Requirements naming's format preference page.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public final class NamingRequirementPreferenceHelper
{

    public static final String NAMING_FORMAT_REQUIREMENT_STORE = "namingFormatRequirement";
    
    public static final String REQUIREMENT_STEP_INDEX = "requirementStepIndex";
    
    public static final String REQUIREMENT_ALGORITHM = "requirementAlgorithm";

    public static final String DEFAULT_FORMAT = "E_{project}_{hierarchical element}_{number}";
    
    public static final List<String> KEY_WORDS = new ArrayList<String>();
    
    static IPreferenceStore store = RequirementCorePlugin.getDefault().getPreferenceStore();
    
    private NamingRequirementPreferenceHelper()
    {

    }

    public static String getDefaultFormat()
    {
        return DEFAULT_FORMAT;
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
     * Set the requirement step to store in the PreferenceStore
     * 
     * @param the step if there is one or default step is there is none
     */
    public static void setRequirementStep(String step)
    {
        if (step.length() == 0)
        {
            step = "10";
        }
        store.putValue(REQUIREMENT_STEP_INDEX, step);
    }

    
    /**
     * Get the requirement step stored in the PreferenceStore
     * 
     * @return the step if there is one or default step is there is none
     */
    public static int getRequirementStep()
    {
        int result = 10;
        try
        {
            String step = store.getString(REQUIREMENT_STEP_INDEX);
            if (step.length() > 0)
            {
                result = Integer.parseInt(step);
            }
        }
        catch (NumberFormatException e)
        {
            RequirementCorePlugin.log(e);
        }
        return result;
    }
    
    /**
     * Set the requirement counting algorithm name to store in the PreferenceStore
     * 
     * @param the name if there is one or default algorithm name is there is none
     */
    public static void setCurrentAlgorithm(String name)
    {
        if (name.length() == 0)
        {
            name = "Default algorithm";
        }
        store.putValue(REQUIREMENT_ALGORITHM, name);
    }
    
    /**
     * Get the requirement counting algorithm name stored in the PreferenceStore
     * 
     * @return the name if there is one or default algorithm name is there is none
     */
    public static String getCurrentAlgorithm()
    {
        String name = "Default algorithm";
        String nameStored = store.getString(REQUIREMENT_ALGORITHM);
        if (nameStored.length() > 0)
        {
            name = nameStored;
        }
        return name;
    }
}
