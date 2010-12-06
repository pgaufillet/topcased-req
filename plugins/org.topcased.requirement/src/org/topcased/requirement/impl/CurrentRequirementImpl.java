/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.RequirementPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Current Requirement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.requirement.impl.CurrentRequirementImpl#isImpacted <em>Impacted</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CurrentRequirementImpl extends RequirementImpl implements CurrentRequirement
{
    /**
     * The default value of the '{@link #isImpacted() <em>Impacted</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isImpacted()
     * @generated
     * @ordered
     */
    protected static final boolean IMPACTED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isImpacted() <em>Impacted</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isImpacted()
     * @generated
     * @ordered
     */
    protected boolean impacted = IMPACTED_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CurrentRequirementImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass()
    {
        return RequirementPackage.Literals.CURRENT_REQUIREMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isImpacted()
    {
        return impacted;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setImpacted(boolean newImpacted)
    {
        boolean oldImpacted = impacted;
        impacted = newImpacted;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RequirementPackage.CURRENT_REQUIREMENT__IMPACTED, oldImpacted, impacted));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType)
    {
        switch (featureID)
        {
            case RequirementPackage.CURRENT_REQUIREMENT__IMPACTED:
                return isImpacted();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue)
    {
        switch (featureID)
        {
            case RequirementPackage.CURRENT_REQUIREMENT__IMPACTED:
                setImpacted((Boolean) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID)
    {
        switch (featureID)
        {
            case RequirementPackage.CURRENT_REQUIREMENT__IMPACTED:
                setImpacted(IMPACTED_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID)
    {
        switch (featureID)
        {
            case RequirementPackage.CURRENT_REQUIREMENT__IMPACTED:
                return impacted != IMPACTED_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString()
    {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (impacted: "); //$NON-NLS-1$
        result.append(impacted);
        result.append(')');
        return result.toString();
    }

} //CurrentRequirementImpl
