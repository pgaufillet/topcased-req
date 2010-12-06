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
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.RequirementPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Attribute Link</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.requirement.impl.AttributeLinkImpl#getPartial <em>Partial</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AttributeLinkImpl extends ObjectAttributeImpl implements AttributeLink
{
    /**
     * The default value of the '{@link #getPartial() <em>Partial</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPartial()
     * @generated
     * @ordered
     */
    protected static final Boolean PARTIAL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPartial() <em>Partial</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPartial()
     * @generated
     * @ordered
     */
    protected Boolean partial = PARTIAL_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AttributeLinkImpl()
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
        return RequirementPackage.Literals.ATTRIBUTE_LINK;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Boolean getPartial()
    {
        return partial;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPartial(Boolean newPartial)
    {
        Boolean oldPartial = partial;
        partial = newPartial;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RequirementPackage.ATTRIBUTE_LINK__PARTIAL, oldPartial, partial));
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
            case RequirementPackage.ATTRIBUTE_LINK__PARTIAL:
                return getPartial();
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
            case RequirementPackage.ATTRIBUTE_LINK__PARTIAL:
                setPartial((Boolean) newValue);
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
            case RequirementPackage.ATTRIBUTE_LINK__PARTIAL:
                setPartial(PARTIAL_EDEFAULT);
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
            case RequirementPackage.ATTRIBUTE_LINK__PARTIAL:
                return PARTIAL_EDEFAULT == null ? partial != null : !PARTIAL_EDEFAULT.equals(partial);
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
        result.append(" (partial: "); //$NON-NLS-1$
        result.append(partial);
        result.append(')');
        return result.toString();
    }

} //AttributeLinkImpl
