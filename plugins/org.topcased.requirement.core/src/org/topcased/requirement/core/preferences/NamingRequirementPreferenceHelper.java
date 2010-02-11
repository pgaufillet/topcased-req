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

/**
 * An Helper working around the Requirements naming's format preference page.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public final class NamingRequirementPreferenceHelper
{

    public static final String NAMING_FORMAT_REQUIREMENT_STORE = "namingFormatRequirement";

    public static final String NUMBER_REQUIREMENT_STORE = "numberDocumentRequirement";

    public static final String DEFAULT_FORMAT = "E_{project}_{hierarchical element}_{number}";

    public static final int PROJECT = 0;

    public static final int HIERARCHICAL_ELEMENT = 1;

    public static final int UPSTREAM_IDENT = 2;

    public static final int NUMBER = 3;

    public static final String[] KEY_WORDS = {"{project}", "{hierarchical element}", "{upstream ident}", "{number}"};

    private NamingRequirementPreferenceHelper()
    {

    }

    public static String getDefaultFormat()
    {
        return DEFAULT_FORMAT;
    }

    public static Boolean getDefaultNumber()
    {
        return false;
    }

}
