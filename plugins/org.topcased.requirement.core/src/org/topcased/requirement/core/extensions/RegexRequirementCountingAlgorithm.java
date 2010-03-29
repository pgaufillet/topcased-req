/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 *                Tristan FAURE (ATOS ORIGIN INTEGRATION) - Regex methods
 * 
 *****************************************************************************/
package org.topcased.requirement.core.extensions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.preferences.NamingRequirementPreferenceHelper;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;

public class RegexRequirementCountingAlgorithm implements IRequirementCountingAlgorithm
{
    /** The next index initialize for the first current requirement creation when the step is at zero*/
    private static long nextIndex = NamingRequirementPreferenceHelper.getRequirementStep();
    
    /** The Step saved every time it change for synchronizme with the preference page*/
    private static long step = NamingRequirementPreferenceHelper.getRequirementStep();
    
    /**
     * @see org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm#getCurrentIndex(org.topcased.requirement.Requirement)
     */
    public long getCurrentIndex(Requirement currentRequirement)
    {
        long currentStep = NamingRequirementPreferenceHelper.getRequirementStep();
        if (currentStep != step)
        {
            nextIndex = nextIndex - step + currentStep; 
            step = currentStep;
        }
        return nextIndex;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm#increaseIndexWhenCreateRequirement(org.topcased.requirement.Requirement, long)
     */
    public void increaseIndexWhenCreateRequirement(Requirement createdRequirement, long index)
    {
        nextIndex = getMaxPlusOne();
    }
    
    /**
     * Gets the max. compute the max using the format of the requirement
     * 
     * @param editingDomainFor the editing domain for
     * @param hier the hier
     * 
     * @return the max
     */
    private static long getMaxPlusOne()
    {
        long max = 0;
        long result = 0;
        HierarchicalElement root = RequirementHelper.INSTANCE.getHierarchicalElementRoot();

        for (Requirement req : RequirementUtils.getAllCurrents(root.eResource()))
        {
            int value = getNumberOfCurrent(req);
            if (value > max)
            {
                max = value;
            }
        }
        result = max + NamingRequirementPreferenceHelper.getRequirementStep();
        return result;
    }
    
    /**
     * @param current the current requirement
     * @return the number of the current
     */
    private static int getNumberOfCurrent(Requirement current)
    {
        int value = -1;
        if (current.getIdentifier() != null && current.getIdentifier().length() > 0)
        {
            String format =RequirementCorePlugin.getDefault().getPreferenceStore().getString(NamingRequirementPreferenceHelper.NAMING_FORMAT_REQUIREMENT_STORE);
            String regex = format.replace("{number}", "(\\d*)");
            regex = regex.replaceAll("\\{[^\\{]*\\}", "\\\\w*");
            Pattern pat = Pattern.compile(regex);
            Matcher m = pat.matcher(current.getIdentifier());
            if (m.matches())
            {
                if (m.groupCount() > 0)
                {
                    String number = m.group(1);
                    value = Integer.valueOf(number);
                }
            }
        }
        return value;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm#setFirstIndex(org.topcased.requirement.Requirement)
     */
    public void setFirstIndex(Requirement firstCreatedRequirement)
    {
        nextIndex = NamingRequirementPreferenceHelper.getRequirementStep();        
    }

}
