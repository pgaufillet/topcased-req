/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *      Matthieu Boivineau (Atos) <matthieu.boivineau@atos.net>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.wizards.MergeRequirementModelWizardPage;

/**
 * This class contributes to the <code>org.eclipse.core.runtime.preferences</code> extension point.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
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
        IPreferenceStore store = RequirementCorePlugin.getDefault().getPreferenceStore();

        store.setDefault(RequirementPreferenceConstants.DELETE_MODEL_WITHOUT_CONFIRM, false);

        if (store.getString(CurrentPreferenceHelper.CURRENT_ATTRIBUTES_STORE).length() == 0)
        {
            store.setValue(CurrentPreferenceHelper.CURRENT_ATTRIBUTES_STORE, CurrentPreferenceHelper.serialize(CurrentPreferenceHelper.getDefaultValues()));
        }

        if (store.getString(UpstreamPreferenceHelper.UPSTREAM_ATTRIBUTES_STORE).length() == 0)
        {
            store.setValue(UpstreamPreferenceHelper.UPSTREAM_ATTRIBUTES_STORE, UpstreamPreferenceHelper.serialize(UpstreamPreferenceHelper.getDefaultValues()));
        }

        store.setDefault(RequirementNamingConstants.REQUIREMENT_MINIMUM_DIGITS, RequirementNamingConstants.DEFAULT_MINIMUM_DIGITS);
        store.setDefault(RequirementNamingConstants.REQUIREMENT_NAMING_FORMAT, RequirementNamingConstants.DEFAULT_NAMING_FORMAT);
        store.setDefault(RequirementNamingConstants.REQUIREMENT_STEP_INDEX, RequirementNamingConstants.DEFAULT_INDEX_STEP);
        store.setDefault(RequirementNamingConstants.REQUIREMENT_COUNTING_ALGORITHM, RequirementNamingConstants.DEFAULT_COUNTING_ALGORITHM);
        store.setDefault(RequirementPreferenceConstants.DISPLAY_CURRENT_DECORATOR, false);
        store.setDefault(RequirementPreferenceConstants.IMPORT_REQUIREMENT_WITHOUT_DIALOG, false);
        store.setDefault(MergeRequirementModelWizardPage.PREFERENCE_FOR_PERFORM_IMPACT_ANALYSIS, true);
    }
}
