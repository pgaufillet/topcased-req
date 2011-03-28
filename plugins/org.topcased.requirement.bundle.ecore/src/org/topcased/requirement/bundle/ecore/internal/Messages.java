/*****************************************************************************
 * Copyright (c) 2010 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Vincent Hemery (Atos Origin}) {vincent.hemery@atosorigin.com} - Initial API and implementation
 *	David Ribeiro (Atos Origin}) {david.ribeirocampelo@atosorigin.com}
 *
 *****************************************************************************/
package org.topcased.requirement.bundle.ecore.internal;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The Class Messages.
 */
public class Messages
{

    /** The Constant BUNDLE_NAME. */
    private static final String BUNDLE_NAME = "org.topcased.requirement.bundle.ecore.internal.messages"; //$NON-NLS-1$

    /** The Constant RESOURCE_BUNDLE. */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Instantiates a new messages.
     */
    private Messages()
    {
    }

    /**
     * Gets the string.
     * 
     * @param key the key
     * @return the string
     */
    public static String getString(String key)
    {
        try
        {
            return RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }
}
