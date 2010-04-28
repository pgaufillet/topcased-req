/***********************************************************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe Mertz (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.decorators;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;
import ttm.Requirement;

/**
 * Defines a decorator for the upstream requirements whose are set as partial.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class UpstreamRequirementDecorator implements ILightweightLabelDecorator
{
    private static final ImageDescriptor ICON_PARTIAL;

    static
    {
        ICON_PARTIAL = RequirementCorePlugin.getImageDescriptor("icons/is_partial.gif"); //$NON-NLS-1$
    }

    public void decorate(Object element, IDecoration decoration)
    {
        if (element instanceof Requirement)
        {
            Requirement req = (Requirement) element;
            if (RequirementUtils.isPartial(req))
            {
                decoration.addOverlay(ICON_PARTIAL, IDecoration.BOTTOM_RIGHT);
            }
        }
    }

    /**
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    public void addListener(ILabelProviderListener listener)
    {
        // nothing to do
    }

    /**
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    public void removeListener(ILabelProviderListener listener)
    {
        // nothing to do
    }

    /**
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
     */
    public void dispose()
    {
        // nothing to do
    }

    /**
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
     */
    public boolean isLabelProperty(Object element, String property)
    {
        return false;
    }

}
