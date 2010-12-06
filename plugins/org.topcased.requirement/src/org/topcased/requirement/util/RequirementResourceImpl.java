/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement.util;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.topcased.requirement.util.migration.RequirementMigrationHelper;


/**
 * <!-- begin-user-doc --> The <b>Resource </b> associated with the package. <!-- end-user-doc -->
 * 
 * @see org.topcased.requirement.util.RequirementResourceFactoryImpl
 * @generated
 */
public class RequirementResourceImpl extends XMIResourceImpl implements RequirementResource
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
     * According to the resource statement, the cache adapter is respectively set or unset.
     * 
     * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#setLoaded(boolean)
     * @generated NOT
     */
    @Override
    protected Notification setLoaded(boolean isLoaded)
    {
        Notification notification = super.setLoaded(isLoaded);
        if (isLoaded)
        {
            setCacheAdapter();
        }
        else
        {
            unsetCacheAdpater();
        }
        return notification;
    }

    /**
     * Sets the Requirement cache adapter on the Requirement resource when considered as 'loaded'.
     * 
     * @generated NOT
     * @custom
     */
    private void setCacheAdapter()
    {
        RequirementCacheAdapter cacheAdapter = RequirementCacheAdapter.getExistingRequirementCacheAdapter(getResourceSet());
        if (cacheAdapter == null)
        {
            getResourceSet().eAdapters().add(new RequirementCacheAdapter());
        }
    }

    /**
     * Unsets the Requirement cache adapter once the Requirement resource is marked as 'unloaded'.
     * 
     * @generated NOT
     * @custom
     */
    private void unsetCacheAdpater()
    {
        RequirementCacheAdapter cacheAdapter = RequirementCacheAdapter.getExistingRequirementCacheAdapter(getResourceSet());
        if (cacheAdapter != null)
        {
            getResourceSet().eAdapters().remove(cacheAdapter);
            cacheAdapter.dispose();
        }
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
