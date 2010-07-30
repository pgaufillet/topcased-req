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

import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;

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
     * Handles a cross reference change by adding and removing the adapter as appropriate.
     * 
     * Copied from ECrossReferenceAdapter and add the resolving of proxy eobjects before
     * adding them to the cross reference map.
     */
    @Override
    protected void handleCrossReference(EReference reference, Notification notification)
    {
        switch (notification.getEventType())
        {
            case Notification.RESOLVE:
            case Notification.SET:
            case Notification.UNSET: {
                EObject notifier = (EObject) notification.getNotifier();
                EReference feature = (EReference) notification.getFeature();
                if (!feature.isMany() || notification.getPosition() != Notification.NO_INDEX)
                {
                    EObject oldValue = (EObject) notification.getOldValue();
                    if (oldValue != null)
                    {
                        inverseCrossReferencer.remove(notifier, feature, oldValue);
                    }
                    EObject newValue = (EObject) notification.getNewValue();
                    if (newValue != null)
                    {
                        // if the element in cross referencer is a proxy we resolve it. We do not want to keep proxies
                        // in the map
                        newValue = resolve(notifier, newValue);
                        inverseCrossReferencer.add(notifier, feature, newValue);
                    }
                }
                break;
            }
            case Notification.ADD: {
                EObject newValue = (EObject) notification.getNewValue();
                if (newValue != null)
                {
                    // if the element in cross referencer is a proxy we resolve it. We do not want to keep proxies in
                    // the map
                    Object notifier = notification.getNotifier();
                    if (notifier instanceof EObject)
                    {
                        newValue = resolve((EObject) notifier, newValue);
                    }
                    inverseCrossReferencer.add((EObject) notification.getNotifier(), (EReference) notification.getFeature(), newValue);
                }
                break;
            }
            case Notification.ADD_MANY: {
                EObject notifier = (EObject) notification.getNotifier();
                EReference feature = (EReference) notification.getFeature();
                for (Object newValue : (Collection< ? >) notification.getNewValue())
                {
                    if (newValue instanceof EObject)
                    {
                        newValue = resolve(notifier, (EObject) newValue);
                    }
                    inverseCrossReferencer.add(notifier, feature, (EObject) newValue);
                }
                break;
            }
            case Notification.REMOVE: {
                EObject oldValue = (EObject) notification.getOldValue();
                if (oldValue != null)
                {
                    inverseCrossReferencer.remove((EObject) notification.getNotifier(), (EReference) notification.getFeature(), oldValue);
                }
                break;
            }
            case Notification.REMOVE_MANY: {
                EObject notifier = (EObject) notification.getNotifier();
                EReference feature = (EReference) notification.getFeature();
                for (Object oldValue : (Collection< ? >) notification.getOldValue())
                {
                    inverseCrossReferencer.remove(notifier, feature, (EObject) oldValue);
                }
                break;
            }
        }
    }

    /**
     * Resolve the toResolve if necesary
     * 
     * @param notifier
     * @param toResolve
     * @return
     */
    protected EObject resolve(EObject notifier, EObject toResolve)
    {
        Resource eResource = notifier.eResource();
        ResourceSet resourceSet = null;
        if (eResource == null)
        {
            eResource = toResolve.eResource();
        }
        if (eResource != null)
        {
            resourceSet = eResource.getResourceSet();
        }
        if (toResolve.eIsProxy() && eResource != null && resourceSet != null)
        {
            toResolve = EcoreUtil.resolve(toResolve, resourceSet);
        }
        return toResolve;
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
