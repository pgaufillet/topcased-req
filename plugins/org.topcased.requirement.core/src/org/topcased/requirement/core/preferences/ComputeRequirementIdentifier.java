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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.preference.IPreferenceStore;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.extensions.IRequirementIdentifierVariables;
import org.topcased.requirement.core.extensions.RequirementIdentifierVariablesManager;
import org.topcased.requirement.core.views.AbstractRequirementView;

/**
 * This class provides the feature to compute the requirement identifier with the Requirements naming's format of the
 * project's property.<br>
 * 
 * Update : 12th may 2009.<br>
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class ComputeRequirementIdentifier
{
    public static final ComputeRequirementIdentifier INSTANCE = new ComputeRequirementIdentifier();

    // Parameters used to compute the full identifier:
    private EditingDomain editingDomain;

    private HierarchicalElement hierarchicalElement;

    private String upstreamIdentifier;

    private long requirementIndex;

    /**
     * Computes the identifier of the new requirement
     * 
     * @param domain The editing domain to use
     * @param hierarchicalElt : the hierarchical element
     * @param source : the identifier of requirement
     * @param nextIndex The next index to use to name the {@link CurrentRequirement}
     * @return the new composed identifier
     */
    public String computeIdentifier(EditingDomain domain, HierarchicalElement hierarchicalElt, String source, long nextIndex)
    {
        Assert.isNotNull(domain, "The editing domain should not be null");
        editingDomain = domain;
        hierarchicalElement = hierarchicalElt;
        upstreamIdentifier = source;
        requirementIndex = nextIndex;

        // Determine the number in the system
        return computeFullIdentifier();
    }

    /**
     * Computes the current requirement identifier including the {number} field
     * 
     * @param domain The editing domain to use
     * @param element The target hierarchical element
     * @param source The identifier of the source
     * 
     * @return the current requirement identifier
     */
    private String computeFullIdentifier()
    {
        Map<String, String> map = new HashMap<String, String>();

        // Variables added by extension point, map are initialized for this purpose
        for (IRequirementIdentifierVariables vars : RequirementIdentifierVariablesManager.getInstance().getIdentifierVariables())
        {
            map = vars.setValuesToVariables(editingDomain, map);
        }

        // Gets the current naming format from the preference store (scoped or global)
        IPreferenceStore store = AbstractRequirementView.getPreferenceStore();
        String currentFormat = store.getString(RequirementNamingConstants.REQUIREMENT_NAMING_FORMAT);

        // replace the variables by their values.
        for (String keyWord : map.keySet())
        {
            if (map.get(keyWord) != null)
            {
                currentFormat = currentFormat.replace(keyWord, map.get(keyWord));
            }
        }
        return currentFormat;
    }

    public HierarchicalElement getIdentifierHierarchicalElement()
    {
        return hierarchicalElement;
    }

    public String getIdentifierUpstreamIdent()
    {
        return upstreamIdentifier;
    }

    public long getIdentifierRequirementIndex()
    {
        return requirementIndex;
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
            IPreferenceStore store = AbstractRequirementView.getPreferenceStore();
            int step = store.getInt(RequirementNamingConstants.REQUIREMENT_STEP_INDEX);
            return step > 0 ? step : RequirementNamingConstants.DEFAULT_INDEX_STEP;
        }
        catch (NumberFormatException e)
        {
            RequirementCorePlugin.log(e);
        }
        return RequirementNamingConstants.DEFAULT_INDEX_STEP;
    }

    /**
     * Get the requirement counting algorithm name stored in the PreferenceStore
     * 
     * @return the name if there is one or default algorithm name is there is none
     */
    public static String getCurrentAlgorithm()
    {
        IPreferenceStore store = AbstractRequirementView.getPreferenceStore();
        String nameStored = store.getString(RequirementNamingConstants.REQUIREMENT_COUNTING_ALGORITHM);
        return !"".equals(nameStored) ? nameStored : RequirementNamingConstants.DEFAULT_COUNTING_ALGORITHM; //$NON-NLS-1$
    }

}
