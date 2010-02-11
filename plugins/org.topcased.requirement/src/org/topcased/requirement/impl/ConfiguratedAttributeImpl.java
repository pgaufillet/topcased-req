/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.AttributesType;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.DefaultAttributeValue;
import org.topcased.requirement.RequirementPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Configurated Attribute</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.requirement.impl.ConfiguratedAttributeImpl#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link org.topcased.requirement.impl.ConfiguratedAttributeImpl#getListValue <em>List Value</em>}</li>
 *   <li>{@link org.topcased.requirement.impl.ConfiguratedAttributeImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.topcased.requirement.impl.ConfiguratedAttributeImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConfiguratedAttributeImpl extends EObjectImpl implements ConfiguratedAttribute
{
    /**
     * The cached value of the '{@link #getDefaultValue() <em>Default Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultValue()
     * @generated
     * @ordered
     */
    protected DefaultAttributeValue defaultValue;

    /**
     * The cached value of the '{@link #getListValue() <em>List Value</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getListValue()
     * @generated
     * @ordered
     */
    protected EList<AttributeValue> listValue;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final AttributesType TYPE_EDEFAULT = AttributesType.TEXT;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected AttributesType type = TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConfiguratedAttributeImpl()
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
        return RequirementPackage.Literals.CONFIGURATED_ATTRIBUTE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DefaultAttributeValue getDefaultValue()
    {
        return defaultValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDefaultValue(DefaultAttributeValue newDefaultValue, NotificationChain msgs)
    {
        DefaultAttributeValue oldDefaultValue = defaultValue;
        defaultValue = newDefaultValue;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RequirementPackage.CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE, oldDefaultValue, newDefaultValue);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefaultValue(DefaultAttributeValue newDefaultValue)
    {
        if (newDefaultValue != defaultValue)
        {
            NotificationChain msgs = null;
            if (defaultValue != null)
                msgs = ((InternalEObject) defaultValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RequirementPackage.CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE, null, msgs);
            if (newDefaultValue != null)
                msgs = ((InternalEObject) newDefaultValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RequirementPackage.CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE, null, msgs);
            msgs = basicSetDefaultValue(newDefaultValue, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RequirementPackage.CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE, newDefaultValue, newDefaultValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AttributeValue> getListValue()
    {
        if (listValue == null)
        {
            listValue = new EObjectContainmentEList<AttributeValue>(AttributeValue.class, this, RequirementPackage.CONFIGURATED_ATTRIBUTE__LIST_VALUE);
        }
        return listValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName()
    {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(String newName)
    {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RequirementPackage.CONFIGURATED_ATTRIBUTE__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AttributesType getType()
    {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(AttributesType newType)
    {
        AttributesType oldType = type;
        type = newType == null ? TYPE_EDEFAULT : newType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RequirementPackage.CONFIGURATED_ATTRIBUTE__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
    {
        switch (featureID)
        {
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE:
                return basicSetDefaultValue(null, msgs);
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__LIST_VALUE:
                return ((InternalEList< ? >) getListValue()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
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
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE:
                return getDefaultValue();
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__LIST_VALUE:
                return getListValue();
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__NAME:
                return getName();
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__TYPE:
                return getType();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue)
    {
        switch (featureID)
        {
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE:
                setDefaultValue((DefaultAttributeValue) newValue);
                return;
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__LIST_VALUE:
                getListValue().clear();
                getListValue().addAll((Collection< ? extends AttributeValue>) newValue);
                return;
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__NAME:
                setName((String) newValue);
                return;
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__TYPE:
                setType((AttributesType) newValue);
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
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE:
                setDefaultValue((DefaultAttributeValue) null);
                return;
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__LIST_VALUE:
                getListValue().clear();
                return;
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__TYPE:
                setType(TYPE_EDEFAULT);
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
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE:
                return defaultValue != null;
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__LIST_VALUE:
                return listValue != null && !listValue.isEmpty();
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__TYPE:
                return type != TYPE_EDEFAULT;
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
        result.append(" (name: ");
        result.append(name);
        result.append(", type: ");
        result.append(type);
        result.append(')');
        return result.toString();
    }

} //ConfiguratedAttributeImpl
