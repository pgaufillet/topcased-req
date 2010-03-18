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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.topcased.requirement.core.RequirementCorePlugin;

/**
 * This class contributes to the <code>org.eclipse.core.runtime.preferences</code> extension point.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public class RequirementPreferenceInitializer extends AbstractPreferenceInitializer
{
    /**
     * Initializes a preference store with default preference values for the Requirement plug-in.
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences()
    {
        IPreferenceStore preferenceStore = RequirementCorePlugin.getDefault().getPreferenceStore();

        if (preferenceStore.getString(CurrentPreferenceHelper.CURRENT_ATTRIBUTES_STORE).length() == 0)
        {
            preferenceStore.setValue(CurrentPreferenceHelper.CURRENT_ATTRIBUTES_STORE, CurrentPreferenceHelper.serialize(CurrentPreferenceHelper.getDefaultValues()));
        }

        if (preferenceStore.getString(UpstreamPreferenceHelper.UPSTREAM_ATTRIBUTES_STORE).length() == 0)
        {
            preferenceStore.setValue(UpstreamPreferenceHelper.UPSTREAM_ATTRIBUTES_STORE, UpstreamPreferenceHelper.serialize(UpstreamPreferenceHelper.getDefaultValues()));
        }

        preferenceStore.setDefault(NamingRequirementPreferenceHelper.NAMING_FORMAT_REQUIREMENT_STORE, NamingRequirementPreferenceHelper.getDefaultFormat());

    }
}
