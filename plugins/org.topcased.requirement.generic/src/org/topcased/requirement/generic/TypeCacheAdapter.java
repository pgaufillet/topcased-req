/***********************************************************************
 * Copyright (c) 2009 Obeo
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 **********************************************************************/
package org.topcased.requirement.generic;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

/**
 * This adapter that maintains an index of reachable objects of type. It provides an optimize way to improve
 * "ItemPropertyDescriptor.getReachableObjectsOfType" operation performance.
 */
public class TypeCacheAdapter extends ECrossReferenceAdapter
{

    Map<EClassifier, Collection<EObject>> cache = Collections.synchronizedMap(new HashMap<EClassifier, Collection<EObject>>());

    public TypeCacheAdapter()
    {
        super();
    }

    protected void handleContainment(Notification notification)
    {
        super.handleContainment(notification);
        Object notifier = notification.getNotifier();
        // synchronize cache on ADD and REMOVE notifications
        if (notifier instanceof EObject)
        {
            switch (notification.getEventType())
            {
                case Notification.ADD: {
                    Object newValue = notification.getNewValue();
                    if (newValue != null && (newValue instanceof EObject))
                    {
                        addObjectInCache((EObject) newValue);
                    }
                    break;
                }
                case Notification.ADD_MANY: {
                    for (Object newValue : (Collection< ? >) notification.getNewValue())
                    {
                        if (newValue != null && (newValue instanceof EObject))
                        {
                            addObjectInCache((EObject) newValue);
                        }
                    }
                    break;
                }
                case Notification.REMOVE: {
                    Object oldValue = notification.getOldValue();
                    if (oldValue != null && (oldValue instanceof EObject))
                    {
                        removeObjectFromCache((EObject) oldValue);
                    }
                    break;
                }
                case Notification.REMOVE_MANY: {
                    for (Object oldValue : (Collection< ? >) notification.getOldValue())
                    {
                        if (oldValue != null && (oldValue instanceof EObject))
                        {
                            removeObjectFromCache((EObject) oldValue);
                        }
                    }
                    break;
                }
            }
        }
    }

    private void addObjectInCache(EObject newObj)
    {
        EClass eClass = newObj.eClass();
        putObjectInCache(eClass, newObj);
        for (EClass eSuperClass : eClass.getEAllSuperTypes())
        {
            putObjectInCache(eSuperClass, newObj);
        }
    }

    private void putObjectInCache(EClassifier eClassifier, EObject newObj)
    {
        if (cache.containsKey(eClassifier))
        {
            Collection<EObject> listOfClassifiers = cache.get(eClassifier);
            listOfClassifiers.add(newObj);
            cache.put(eClassifier, listOfClassifiers);
        }
    }

    private void removeObjectFromCache(EObject newObj)
    {
        EClass eClass = newObj.eClass();
        removeObjectFromCache(eClass, newObj);
        for (EClass eSuperClass : eClass.getEAllSuperTypes())
        {
            removeObjectFromCache(eSuperClass, newObj);
        }
    }

    private void removeObjectFromCache(EClassifier eClassifier, EObject newObj)
    {
        if (cache.containsKey(eClassifier))
        {
            Collection<EObject> listOfClassifiers = cache.get(eClassifier);
            listOfClassifiers.remove(newObj);
            cache.put(eClassifier, listOfClassifiers);
        }
    }

    /**
     * Handles a cross reference change by adding and removing the adapter as appropriate.
     */
    protected void handleCrossReference(EReference reference, Notification notification)
    {
        // do nothing
    }

    /**
     * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
     */
    @Override
    public boolean isAdapterForType(Object type)
    {
        return TypeCacheAdapter.class.equals(type);
    }

    public Collection<EObject> getReachableObjectsOfType(EClassifier type, ResourceSet r)
    {
        if (!cache.containsKey(type))
        {
            cache.put(type, iGetReachableObjectsOfType(type, r));
        }
        return cache.get(type);
    }

    private Collection<EObject> iGetReachableObjectsOfType(EClassifier type, ResourceSet r)
    {
        Collection<EObject> result = new LinkedList<EObject>();
        for (Resource res : r.getResources())
        {
            for (Iterator<EObject> i = res.getAllContents() ; i.hasNext() ;)
            {
                EObject tmp = i.next();
                if (tmp.eClass() == type)
                {
                    result.add(tmp);
                }
            }
        }
        return result ;
    }

    /**
     * Searches the adapter list of the given Notifier for a TypeCacheAdapter. If not found, returns null.
     * 
     * @param notifier the notifier to search
     * @return the TypeCacheAdapter if found, otherwise null
     */
    public static TypeCacheAdapter getExistingTypeCacheAdapter(Notifier notifier)
    {
        if (notifier == null)
        {
            return null;
        }
        for (Adapter adapter : notifier.eAdapters())
        {
            if (adapter instanceof TypeCacheAdapter)
            {
                return (TypeCacheAdapter) adapter;
            }
        }
        if (notifier instanceof EObject)
        {
            EObject object = (EObject) notifier;
            TypeCacheAdapter typeCacheAdapter = getExistingTypeCacheAdapter(object.eResource());
            if (typeCacheAdapter != null)
            {
                object.eAdapters().add(typeCacheAdapter);
                return typeCacheAdapter;
            }
        }
        return null;
    }

    public void dispose()
    {
        cache.clear();
        cache = null;
    }

}
