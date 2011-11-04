/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views.current.model.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.views.current.model.CurrentRequirementReference;
import org.topcased.requirement.core.views.current.model.CurrentRequirementReferenceContainer;
/**
 * {@link EContentAdapter} which is a listener of addition of new current requirement reference
 * 
 * @author "Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * @since 5.1
 */
public class ModelSetReferencesChangeListener extends EContentAdapter
{
    /**
     * {@link CurrentRequirementReference} to add new current requirement reference
     */
    private CurrentRequirementReferenceContainer referencesContainer;
    /**
     * {@link CurrentRequirementReference} to add new current requirement reference
     */
    public ModelSetReferencesChangeListener(CurrentRequirementReferenceContainer referencesContainer)
    {
        super();
        this.referencesContainer = referencesContainer;
    }
    /**
     * Add new reference to the {@link CurrentRequirementReferenceContainer} and ask for a refresh in the view
     */
    @Override
    public void notifyChanged(Notification notification)
    {
        boolean update = false;
        boolean noMoreEmpty = referencesContainer.getReferences().isEmpty();
        Object newValue = notification.getNewValue();
        if (notification.getEventType() == Notification.ADD || notification.getEventType() == Notification.ADD_MANY)
        {
            update = referencesContainer.addReference(newValue);
            noMoreEmpty &= !referencesContainer.getReferences().isEmpty();
        }
        
        if (update){
            Collection<RequirementProject> newReq = null;
            if (newValue instanceof RequirementProject){
                newReq = Collections.singleton((RequirementProject)newValue);
            } else if (newValue instanceof Collection<?>){
                if(!((Collection<?>)newValue).isEmpty() && ((Collection<?>)newValue).iterator().next() instanceof RequirementProject){
                    @SuppressWarnings("unchecked")
                    Collection<RequirementProject> newCollection = (Collection<RequirementProject>)newValue;
                    newReq = new ArrayList<RequirementProject>(newCollection);
                }
            }
            if(newReq != null){     
                Object tmp = referencesContainer;
                if (noMoreEmpty)
                {
                    // reference container adapted to EObject return the root of requirement resource
                    tmp = referencesContainer.getAdapter(EObject.class);
                }
                ViewerNotification newNot = new ViewerNotification(notification,tmp);
                referencesContainer.getNotifier().notifyChanged(newNot);
                /* For all requirement project which has been added - Should be very small*/
                for (RequirementProject req : newReq){
                    CurrentRequirementReference ref = referencesContainer.getReferences().get(req.eResource().getURI().trimFragment());
                    if (ref != null){
                        for (Object ob : ref.getRefenredBy()){                            
                            ViewerNotification newNotAux = new ViewerNotification(notification,ob);
                            referencesContainer.getNotifier().notifyChanged(newNotAux);
                        }
                    }
                }
            }
        }
        super.notifyChanged(notification);
    }

}
