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

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.SpecialChapter;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Special Chapter</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.topcased.requirement.impl.SpecialChapterImpl#getHierarchicalElement <em>Hierarchical Element</em>}</li>
 * <li>{@link org.topcased.requirement.impl.SpecialChapterImpl#getRequirement <em>Requirement</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class SpecialChapterImpl extends MinimalEObjectImpl.Container implements SpecialChapter
{
    /**
     * The cached value of the '{@link #getHierarchicalElement() <em>Hierarchical Element</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getHierarchicalElement()
     * @generated
     * @ordered
     */
    protected EList<HierarchicalElement> hierarchicalElement;

    /**
     * The cached value of the '{@link #getRequirement() <em>Requirement</em>}' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getRequirement()
     * @generated
     * @ordered
     */
    protected EList<Requirement> requirement;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected SpecialChapterImpl()
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
        return RequirementPackage.Literals.SPECIAL_CHAPTER;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<HierarchicalElement> getHierarchicalElement()
    {
        if (hierarchicalElement == null)
        {
            hierarchicalElement = new EObjectContainmentEList.Resolving<HierarchicalElement>(HierarchicalElement.class, this, RequirementPackage.SPECIAL_CHAPTER__HIERARCHICAL_ELEMENT);
        }
        return hierarchicalElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<Requirement> getRequirement()
    {
        if (requirement == null)
        {
            requirement = new EObjectContainmentEList.Resolving<Requirement>(Requirement.class, this, RequirementPackage.SPECIAL_CHAPTER__REQUIREMENT);
        }
        return requirement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
    {
        switch (featureID)
        {
            case RequirementPackage.SPECIAL_CHAPTER__HIERARCHICAL_ELEMENT:
                return ((InternalEList< ? >) getHierarchicalElement()).basicRemove(otherEnd, msgs);
            case RequirementPackage.SPECIAL_CHAPTER__REQUIREMENT:
                return ((InternalEList< ? >) getRequirement()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
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
            case RequirementPackage.SPECIAL_CHAPTER__HIERARCHICAL_ELEMENT:
                return getHierarchicalElement();
            case RequirementPackage.SPECIAL_CHAPTER__REQUIREMENT:
                return getRequirement();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue)
    {
        switch (featureID)
        {
            case RequirementPackage.SPECIAL_CHAPTER__HIERARCHICAL_ELEMENT:
                getHierarchicalElement().clear();
                getHierarchicalElement().addAll((Collection< ? extends HierarchicalElement>) newValue);
                return;
            case RequirementPackage.SPECIAL_CHAPTER__REQUIREMENT:
                getRequirement().clear();
                getRequirement().addAll((Collection< ? extends Requirement>) newValue);
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
            case RequirementPackage.SPECIAL_CHAPTER__HIERARCHICAL_ELEMENT:
                getHierarchicalElement().clear();
                return;
            case RequirementPackage.SPECIAL_CHAPTER__REQUIREMENT:
                getRequirement().clear();
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
            case RequirementPackage.SPECIAL_CHAPTER__HIERARCHICAL_ELEMENT:
                return hierarchicalElement != null && !hierarchicalElement.isEmpty();
            case RequirementPackage.SPECIAL_CHAPTER__REQUIREMENT:
                return requirement != null && !requirement.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} // SpecialChapterImpl
