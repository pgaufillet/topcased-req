/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;

/**
 * <!-- begin-user-doc --> The <b>Resource Factory</b> associated with the package. <!-- end-user-doc -->
 * 
 * @see org.topcased.requirement.util.RequirementResourceImpl
 * @generated
 */
public class RequirementResourceFactoryImpl extends ResourceFactoryImpl
{
    /**
     * Creates an instance of the resource factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public RequirementResourceFactoryImpl()
    {
        super();
    }

    /**
     * Creates an instance of the resource. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Resource createResource(URI uri)
    {
        Resource result = new RequirementResourceImpl(uri);
        return result;
    }

} // RequirementResourceFactoryImpl
