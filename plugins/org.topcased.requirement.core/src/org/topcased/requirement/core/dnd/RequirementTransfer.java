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
package org.topcased.requirement.core.dnd;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.topcased.requirement.core.internal.Messages;

/**
 * The data transfer for drag and drop between upstream view and current view
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public final class RequirementTransfer extends LocalSelectionTransfer
{

    public static final String TYPE_NAME = Messages.getString("RequirementTransfer.0"); //$NON-NLS-1$

    private static RequirementTransfer tRANSFER = null;

    private static final int TYPE_ID = registerType(TYPE_NAME);

    private RequirementTransfer()
    {
        // Do nothing
    }

    /**
     * Returns the shared instance
     * 
     * @return the singleton
     */
    public static RequirementTransfer getInstance()
    {
        if (tRANSFER == null)
        {
            tRANSFER = new RequirementTransfer();
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
