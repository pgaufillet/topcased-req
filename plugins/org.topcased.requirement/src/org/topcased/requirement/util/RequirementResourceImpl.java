/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.topcased.requirement.util.migration.RequirementMigrationHelper;


/**
 * <!-- begin-user-doc --> The <b>Resource </b> associated with the package. <!-- end-user-doc -->
 * 
 * @see org.topcased.sam.requirement.util.RequirementResourceFactoryImpl
 * @generated
 */
public class RequirementResourceImpl extends XMIResourceImpl
{
    
    private RequirementMigrationHelper myMigrationHelper;
    
    /**
     * Creates an instance of the resource. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param uri the URI of the new resource.
     * @generated
     */
    public RequirementResourceImpl(URI uri)
    {
        super(uri);
    }

    /**
     * @see org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl#useUUIDs()
     */
    @Override
    protected boolean useUUIDs()
    {
        return true;
    }
    
    /**
     * @see org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl#createXMLHelper()
     * @custom
     */
    @Override
    protected XMLHelper createXMLHelper()
    {
        myMigrationHelper = new RequirementMigrationHelper(this);
        return myMigrationHelper;
    }

} // RequirementResourceImpl
