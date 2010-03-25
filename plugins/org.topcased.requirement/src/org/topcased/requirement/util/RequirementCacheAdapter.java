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

package org.topcased.requirement.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

/**
 * Defines a specific {@link ECrossReferenceAdapter} for the Requirement language.<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class RequirementCacheAdapter extends ECrossReferenceAdapter
{
    /**
     * Searches the adapter list of the given Notifier for a {@link ECrossReferenceAdapter}. If not found, returns null.
     * 
     * @param notifier the notifier to search
     * @return the CrossReferenceAdapter if found, otherwise null
     */
    public static RequirementCacheAdapter getExistingRequirementCacheAdapter(Notifier notifier)
    {
        if (notifier == null)
        {
            return null;
        }
        for (Adapter adapter : notifier.eAdapters())
        {
            if (adapter instanceof RequirementCacheAdapter)
            {
                return (RequirementCacheAdapter) adapter;
            }
        }
        return null;
    }

    /**
     * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#handleContainment(org.eclipse.emf.common.notify.Notification)
     */
    @Override
    protected void handleContainment(Notification notification)
    {
        // call to super
        super.handleContainment(notification);

        // don't know why but this case is not handled by default behavior
        switch (notification.getEventType())
        {
            case Notification.ADD:
                Object newValue = notification.getNewValue();
                if (newValue instanceof Notifier)
                {
                    setTarget((Notifier) notification.getNewValue());
                }
                break;
                
            case Notification.REMOVE:
                Object oldValue = notification.getOldValue();
                if (oldValue instanceof Notifier)
                {
                    unsetTarget((Notifier) notification.getOldValue());
                }
                break;
        }
    }

    /**
     * Disposes the different component.
     */
    public void dispose()
    {
        inverseCrossReferencer.clear();
        inverseCrossReferencer = null;
        unloadedEObjects.clear();
        unloadedEObjects = null;
        unloadedResources.clear();
        unloadedResources = null;
    }
}
