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

package org.topcased.requirement.util.migration;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIHelperImpl;
import org.topcased.requirement.RequirementPackage;

public class RequirementMigrationHelper extends XMIHelperImpl
{

    /** Map of features to change */
    private Map<String, EStructuralFeature> myRenamedFeature = new HashMap<String, EStructuralFeature>();

    /**
     * This creates an instance of the helper
     * 
     * @param resource
     */
    public RequirementMigrationHelper(XMLResource resource)
    {
        super(resource);

        // Register the renamed Feature
        registerRenamedFeature("samElement", RequirementPackage.eINSTANCE.getHierarchicalElement_Element()); //$NON-NLS-1$

    }

    /**
     * @see org.eclipse.emf.ecore.xmi.impl.XMLHelperImpl#getFeature(org.eclipse.emf.ecore.EClass, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public EStructuralFeature getFeature(EClass clazz, String namespaceURI, String featureName)
    {
        EStructuralFeature renamedType = getRenameFeature(featureName);
        if (renamedType != null)
        {
            return renamedType;
        }
        return super.getFeature(clazz, namespaceURI, featureName);
    }

    /**
     * Register a renamed feature
     * 
     * @param oldFeature The name of the type which has been renamed
     * @param newFeature The reference which replaces this feature
     */
    public void registerRenamedFeature(String oldFeature, EStructuralFeature newFeature)
    {
        myRenamedFeature.put(oldFeature, newFeature);
    }

    /**
     * Gets the reference, if exists, for this feature
     * 
     * @param featureName The name of the type
     * @return The corresponding reference or null
     */
    public EStructuralFeature getRenameFeature(String featureName)
    {
        return myRenamedFeature.get(featureName);
    }
}
