/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.RequirementPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Attribute Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.requirement.impl.AttributeConfigurationImpl#getListAttributes <em>List Attributes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AttributeConfigurationImpl extends EObjectImpl implements AttributeConfiguration
{
    /**
     * The cached value of the '{@link #getListAttributes() <em>List Attributes</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getListAttributes()
     * @generated
     * @ordered
     */
    protected EList<ConfiguratedAttribute> listAttributes;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AttributeConfigurationImpl()
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
        return RequirementPackage.Literals.ATTRIBUTE_CONFIGURATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ConfiguratedAttribute> getListAttributes()
    {
        if (listAttributes == null)
        {
            listAttributes = new EObjectContainmentEList<ConfiguratedAttribute>(ConfiguratedAttribute.class, this, RequirementPackage.ATTRIBUTE_CONFIGURATION__LIST_ATTRIBUTES);
        }
        return listAttributes;
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
            case RequirementPackage.ATTRIBUTE_CONFIGURATION__LIST_ATTRIBUTES:
                return ((InternalEList< ? >) getListAttributes()).basicRemove(otherEnd, msgs);
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
            case RequirementPackage.ATTRIBUTE_CONFIGURATION__LIST_ATTRIBUTES:
                return getListAttributes();
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
            case RequirementPackage.ATTRIBUTE_CONFIGURATION__LIST_ATTRIBUTES:
                getListAttributes().clear();
                getListAttributes().addAll((Collection< ? extends ConfiguratedAttribute>) newValue);
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
            case RequirementPackage.ATTRIBUTE_CONFIGURATION__LIST_ATTRIBUTES:
                getListAttributes().clear();
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
            case RequirementPackage.ATTRIBUTE_CONFIGURATION__LIST_ATTRIBUTES:
                return listAttributes != null && !listAttributes.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //AttributeConfigurationImpl
