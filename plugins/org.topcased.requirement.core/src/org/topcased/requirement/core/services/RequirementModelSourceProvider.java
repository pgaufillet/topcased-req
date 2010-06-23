/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/
package org.topcased.requirement.core.services;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;

/**
 * A Source provider to define commands activation. There is two variables : hasRequirementModel who will notify
 * commands when a requirement model has been linked or unlinked to the current page isImpacted who will notify commands
 * when there is no more impacted requirements in the current page requirement model
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class RequirementModelSourceProvider extends AbstractSourceProvider
{
    public final static String HAS_REQUIREMENT_MODEL = "requirementActionsEnablement.hasRequirementModel"; //$NON-NLS-1$

    public final static String IS_IMPACTED = "requirementActionsEnablement.isImpacted"; //$NON-NLS-1$
    
    public final static String IS_SECTION_ENABLED = "requirementActionsEnablement.isSectionEnabled"; //$NON-NLS-1$

    private static Map<String, Boolean> currentState;

    /**
     * Constructor
     */
    public RequirementModelSourceProvider()
    {
        currentState = new HashMap<String, Boolean>();
    }

    /**
     * @see org.eclipse.ui.ISourceProvider#getCurrentState()
     */
    public Map<String, Boolean> getCurrentState()
    {
        if (!currentState.containsKey(HAS_REQUIREMENT_MODEL))
        {
            currentState.put(HAS_REQUIREMENT_MODEL, true);
        }
        if (!currentState.containsKey(IS_IMPACTED))
        {
            currentState.put(IS_IMPACTED, true);
        }
        if (!currentState.containsKey(IS_SECTION_ENABLED))
        {
            currentState.put(IS_SECTION_ENABLED, true);
        }
        return currentState;

    }

    /**
     * @see org.eclipse.ui.ISourceProvider#getProvidedSourceNames()
     */
    public String[] getProvidedSourceNames()
    {
        return new String[] {HAS_REQUIREMENT_MODEL, IS_IMPACTED, IS_SECTION_ENABLED};
    }

    /**
     * @see org.eclipse.ui.ISourceProvider#dispose()
     */
    public void dispose()
    {
        // No need to implement this

    }

    /**
     * Provide way to notify the commands about a changing state of the hasRequirement variable
     * 
     * @param boolean enablement
     */
    public void setHasRequirementState(Boolean enablement)
    {
        fireSourceChanged(0, HAS_REQUIREMENT_MODEL, enablement);
    }

    /**
     * Provide way to notify the commands about a changing state of the isImpacted variable
     * 
     * @param boolean enablement
     */
    public void setIsImpactedState(Boolean enablement)
    {
        fireSourceChanged(0, IS_IMPACTED, enablement);
    }
    
    /**
     * Provide way to notify the commands about a changing state of the isSectionEnabled variable
     * 
     * @param boolean enablement
     */
    public void setIsSectionEnabledState(Boolean enablement)
    {
        fireSourceChanged(0, IS_SECTION_ENABLED, enablement);
    }
}
