/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.DefaultAttributeValue;
import org.topcased.requirement.RequirementPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Default Attribute Value</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.requirement.impl.DefaultAttributeValueImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DefaultAttributeValueImpl extends MinimalEObjectImpl.Container implements DefaultAttributeValue
{
    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected AttributeValue value;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected DefaultAttributeValueImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass()
    {
        return RequirementPackage.Literals.DEFAULT_ATTRIBUTE_VALUE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public AttributeValue getValue()
    {
        if (value != null && value.eIsProxy())
        {
            InternalEObject oldValue = (InternalEObject) value;
            value = (AttributeValue) eResolveProxy(oldValue);
            if (value != oldValue)
            {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, RequirementPackage.DEFAULT_ATTRIBUTE_VALUE__VALUE, oldValue, value));
            }
        }
        return value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public AttributeValue basicGetValue()
    {
        return value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setValue(AttributeValue newValue)
    {
        AttributeValue oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RequirementPackage.DEFAULT_ATTRIBUTE_VALUE__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType)
    {
        switch (featureID)
        {
            case RequirementPackage.DEFAULT_ATTRIBUTE_VALUE__VALUE:
                if (resolve)
                    return getValue();
                return basicGetValue();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue)
    {
        switch (featureID)
        {
            case RequirementPackage.DEFAULT_ATTRIBUTE_VALUE__VALUE:
                setValue((AttributeValue) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID)
    {
        switch (featureID)
        {
            case RequirementPackage.DEFAULT_ATTRIBUTE_VALUE__VALUE:
                setValue((AttributeValue) null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID)
    {
        switch (featureID)
        {
            case RequirementPackage.DEFAULT_ATTRIBUTE_VALUE__VALUE:
                return value != null;
        }
        return super.eIsSet(featureID);
    }

} // DefaultAttributeValueImpl
