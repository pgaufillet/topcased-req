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
package org.topcased.requirement.generic;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.dnd.Transfer;

/**
 * The Class GenericTransfer.
 */
public class GenericTransfer extends LocalSelectionTransfer
{
    public static final String TYPE_NAME = "Upstream Requirement to Current Requirement view"; //$NON-NLS-1$

    private static GenericTransfer tRANSFER = null;

    private static final int TYPE_ID = registerType(TYPE_NAME);

    /**
     * Returns the shared instance
     * 
     * @return the singleton
     */
    public static Transfer getInstance()
    {
        if (tRANSFER == null)
        {
            tRANSFER = new GenericTransfer();
        }

        return tRANSFER;
    }

    protected int[] getTypeIds()
    {
        return new int[] {TYPE_ID};
    }

    protected String[] getTypeNames()
    {
        return new String[] {TYPE_NAME};
    }

}
