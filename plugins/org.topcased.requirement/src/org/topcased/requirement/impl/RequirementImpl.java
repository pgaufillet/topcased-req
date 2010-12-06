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

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Requirement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.requirement.impl.RequirementImpl#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.topcased.requirement.impl.RequirementImpl#getExternalResources <em>External Resources</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class RequirementImpl extends IdentifiedElementImpl implements Requirement
{
    /**
     * The cached value of the '{@link #getAttribute() <em>Attribute</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAttribute()
     * @generated
     * @ordered
     */
    protected EList<Attribute> attribute;

    /**
     * The cached value of the '{@link #getExternalResources() <em>External Resources</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExternalResources()
     * @generated
     * @ordered
     */
    protected EList<String> externalResources;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RequirementImpl()
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
        return RequirementPackage.Literals.REQUIREMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Attribute> getAttribute()
    {
        if (attribute == null)
        {
            attribute = new EObjectContainmentEList<Attribute>(Attribute.class, this, RequirementPackage.REQUIREMENT__ATTRIBUTE);
        }
        return attribute;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getExternalResources()
    {
        if (externalResources == null)
        {
            externalResources = new EDataTypeUniqueEList<String>(String.class, this, RequirementPackage.REQUIREMENT__EXTERNAL_RESOURCES);
        }
        return externalResources;
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
            case RequirementPackage.REQUIREMENT__ATTRIBUTE:
                return ((InternalEList< ? >) getAttribute()).basicRemove(otherEnd, msgs);
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
            case RequirementPackage.REQUIREMENT__ATTRIBUTE:
                return getAttribute();
            case RequirementPackage.REQUIREMENT__EXTERNAL_RESOURCES:
                return getExternalResources();
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
            case RequirementPackage.REQUIREMENT__ATTRIBUTE:
                getAttribute().clear();
                getAttribute().addAll((Collection< ? extends Attribute>) newValue);
                return;
            case RequirementPackage.REQUIREMENT__EXTERNAL_RESOURCES:
                getExternalResources().clear();
                getExternalResources().addAll((Collection< ? extends String>) newValue);
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
            case RequirementPackage.REQUIREMENT__ATTRIBUTE:
                getAttribute().clear();
                return;
            case RequirementPackage.REQUIREMENT__EXTERNAL_RESOURCES:
                getExternalResources().clear();
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
            case RequirementPackage.REQUIREMENT__ATTRIBUTE:
                return attribute != null && !attribute.isEmpty();
            case RequirementPackage.REQUIREMENT__EXTERNAL_RESOURCES:
                return externalResources != null && !externalResources.isEmpty();
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
        result.append(" (externalResources: "); //$NON-NLS-1$
        result.append(externalResources);
        result.append(')');
        return result.toString();
    }

} //RequirementImpl
