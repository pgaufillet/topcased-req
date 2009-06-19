/*****************************************************************************
 * Copyright (c) 2009 ATOS ORIGIN INTEGRATION.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) tristan.faure@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.generic.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.topcased.requirement.generic.Activator;

public class PreferencesHelper
{
    private static final String STEP_FOR_NAMING = "stepForNaming";

    private static final String NEW_ALGO = "useNewAlgo";

    static IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    public static void setStep(String value)
    {
        if (value.length() == 0)
        {
            value = "10";
        }
        store.putValue(STEP_FOR_NAMING, value);
    }

    public static int getStep()
    {
        int result = 10;
        try
        {
            String get = store.getString(STEP_FOR_NAMING);
            if (get.length() > 0)
            {
                result = Integer.parseInt(get);
            }
        }
        catch (NumberFormatException e)
        {
        }
        return result;
    }

    public static void setNewAlgo(boolean value)
    {
        store.putValue(NEW_ALGO, String.valueOf(value));
    }

    public static boolean getNewAlgo()
    {
        boolean result = true;
        String tmp = store.getString(NEW_ALGO);
        if (tmp.length() > 0)
        {
            result = Boolean.valueOf(tmp);
        }
        return result;
    }
}
