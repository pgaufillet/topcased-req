/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.utils;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

/**
 * This helper class is used to get the name of an EObject.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public class LabelHelper
{
    public static final LabelHelper INSTANCE = new LabelHelper();

    /**
     * Gets the name of the object using the appropriate label provider.
     * 
     * @param eObject The {@link EObject} for which the label must be obtained.
     * @return the object's name
     */
    public String getName(EObject eObject)
    {
        AdapterFactory adapterFactory = RequirementUtils.getAdapterFactory();
        if (eObject != null && adapterFactory != null)
        {
            // Get the provider from the adapterFactory
            IItemLabelProvider labelFeatureProvider = (IItemLabelProvider) adapterFactory.adapt(eObject, IItemLabelProvider.class);
            return labelFeatureProvider.getText(eObject);
        }
        return ""; //$NON-NLS-1$
    }

}
