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
 *  Caroline Bourdeu d'Aguerre (ATOS ORIGIN INTEGRATION) caroline.bourdeudaguerre@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.generic.importrequirement.utils;

import org.eclipse.osgi.util.NLS;

/**
 * The Class Messages.
 */
public class Messages extends NLS
{

    /** The Constant BUNDLE_NAME. */
    private static final String BUNDLE_NAME = "org.topcased.requirement.generic.importrequirement.utils.messages"; //$NON-NLS-1$

    /** The Import requirement wizard page select document_ inpu t_ document. */
    public static String ImportRequirementWizardPageSelectDocument_INPUT_DOCUMENT;

    /** The Import requirement wizard page select document_ appl y_ stereotypes. */
    public static String ImportRequirementWizardPageSelectDocument_APPLY_STEREOTYPES;

    /** The Import requirement wizard page select document_ eannotation. */
    public static String ImportRequirementWizardPageSelectDocument_EANNOTATION;

    /** The Import requirement wizard page select document_ outpu t_ model. */
    public static String ImportRequirementWizardPageSelectDocument_OUTPUT_MODEL;

    /** The Import requirement wizard page select format_ fla t_ o r_ hierarchical. */
    public static String ImportRequirementWizardPageSelectFormat_FLAT_OR_HIERARCHICAL;

    /** The Import requirement wizard page select format_ identification. */
    public static String ImportRequirementWizardPageSelectFormat_IDENTIFICATION;

    /** The New attribute popup_ attribut e_ name. */
    public static String NewAttributePopup_ATTRIBUTE_NAME;

    /** The New attribute popup_ attribut e_ re f_ o r_ text. */
    public static String NewAttributePopup_ATTRIBUTE_REF_OR_TEXT;

    /** The New column popup_ column. */
    public static String NewColumnPopup_COLUMN;
    
    public static String SelectStereotypeDialog_HelpDForStereotypeSelection;

    /** The Popup regex dialog_ regex. */
    public static String PopupRegexDialog_REGEX;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    /**
     * Instantiates a new messages.
     */
    private Messages()
    {
    }
}
