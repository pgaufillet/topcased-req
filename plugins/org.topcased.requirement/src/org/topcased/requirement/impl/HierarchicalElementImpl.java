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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Hierarchical Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.requirement.impl.HierarchicalElementImpl#getElement <em>Element</em>}</li>
 *   <li>{@link org.topcased.requirement.impl.HierarchicalElementImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link org.topcased.requirement.impl.HierarchicalElementImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link org.topcased.requirement.impl.HierarchicalElementImpl#getRequirement <em>Requirement</em>}</li>
 *   <li>{@link org.topcased.requirement.impl.HierarchicalElementImpl#getNextReqIndex <em>Next Req Index</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HierarchicalElementImpl extends IdentifiedElementImpl implements HierarchicalElement
{
    /**
     * The cached value of the '{@link #getElement() <em>Element</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElement()
     * @generated
     * @ordered
     */
    protected EObject element;

    /**
     * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getChildren()
     * @generated
     * @ordered
     */
    protected EList<HierarchicalElement> children;

    /**
     * The cached value of the '{@link #getRequirement() <em>Requirement</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRequirement()
     * @generated
     * @ordered
     */
    protected EList<Requirement> requirement;

    /**
     * The default value of the '{@link #getNextReqIndex() <em>Next Req Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNextReqIndex()
     * @generated
     * @ordered
     */
    protected static final long NEXT_REQ_INDEX_EDEFAULT = 10L;

    /**
     * The cached value of the '{@link #getNextReqIndex() <em>Next Req Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNextReqIndex()
     * @generated
     * @ordered
     */
    protected long nextReqIndex = NEXT_REQ_INDEX_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected HierarchicalElementImpl()
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
        return RequirementPackage.Literals.HIERARCHICAL_ELEMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getElement()
    {
        if (element != null && element.eIsProxy())
        {
            InternalEObject oldElement = (InternalEObject) element;
            element = eResolveProxy(oldElement);
            if (element != oldElement)
            {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, RequirementPackage.HIERARCHICAL_ELEMENT__ELEMENT, oldElement, element));
            }
        }
        return element;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject basicGetElement()
    {
        return element;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setElement(EObject newElement)
    {
        EObject oldElement = element;
        element = newElement;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RequirementPackage.HIERARCHICAL_ELEMENT__ELEMENT, oldElement, element));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<HierarchicalElement> getChildren()
    {
        if (children == null)
        {
            children = new EObjectContainmentWithInverseEList<HierarchicalElement>(HierarchicalElement.class, this, RequirementPackage.HIERARCHICAL_ELEMENT__CHILDREN,
                    RequirementPackage.HIERARCHICAL_ELEMENT__PARENT);
        }
        return children;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public HierarchicalElement getParent()
    {
        if (eContainerFeatureID() != RequirementPackage.HIERARCHICAL_ELEMENT__PARENT)
            return null;
        return (HierarchicalElement) eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetParent(HierarchicalElement newParent, NotificationChain msgs)
    {
        msgs = eBasicSetContainer((InternalEObject) newParent, RequirementPackage.HIERARCHICAL_ELEMENT__PARENT, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setParent(HierarchicalElement newParent)
    {
        if (newParent != eInternalContainer() || (eContainerFeatureID() != RequirementPackage.HIERARCHICAL_ELEMENT__PARENT && newParent != null))
        {
            if (EcoreUtil.isAncestor(this, newParent))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString()); //$NON-NLS-1$
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newParent != null)
                msgs = ((InternalEObject) newParent).eInverseAdd(this, RequirementPackage.HIERARCHICAL_ELEMENT__CHILDREN, HierarchicalElement.class, msgs);
            msgs = basicSetParent(newParent, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RequirementPackage.HIERARCHICAL_ELEMENT__PARENT, newParent, newParent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Requirement> getRequirement()
    {
        if (requirement == null)
        {
            requirement = new EObjectContainmentEList<Requirement>(Requirement.class, this, RequirementPackage.HIERARCHICAL_ELEMENT__REQUIREMENT);
        }
        return requirement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public long getNextReqIndex()
    {
        return nextReqIndex;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNextReqIndex(long newNextReqIndex)
    {
        long oldNextReqIndex = nextReqIndex;
        nextReqIndex = newNextReqIndex;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RequirementPackage.HIERARCHICAL_ELEMENT__NEXT_REQ_INDEX, oldNextReqIndex, nextReqIndex));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
    {
        switch (featureID)
        {
            case RequirementPackage.HIERARCHICAL_ELEMENT__CHILDREN:
                return ((InternalEList<InternalEObject>) (InternalEList< ? >) getChildren()).basicAdd(otherEnd, msgs);
            case RequirementPackage.HIERARCHICAL_ELEMENT__PARENT:
                if (eInternalContainer() != null)
                    msgs = eBasicRemoveFromContainer(msgs);
                return basicSetParent((HierarchicalElement) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
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
            case RequirementPackage.HIERARCHICAL_ELEMENT__CHILDREN:
                return ((InternalEList< ? >) getChildren()).basicRemove(otherEnd, msgs);
            case RequirementPackage.HIERARCHICAL_ELEMENT__PARENT:
                return basicSetParent(null, msgs);
            case RequirementPackage.HIERARCHICAL_ELEMENT__REQUIREMENT:
                return ((InternalEList< ? >) getRequirement()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs)
    {
        switch (eContainerFeatureID())
        {
            case RequirementPackage.HIERARCHICAL_ELEMENT__PARENT:
                return eInternalContainer().eInverseRemove(this, RequirementPackage.HIERARCHICAL_ELEMENT__CHILDREN, HierarchicalElement.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
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
            case RequirementPackage.HIERARCHICAL_ELEMENT__ELEMENT:
                if (resolve)
                    return getElement();
                return basicGetElement();
            case RequirementPackage.HIERARCHICAL_ELEMENT__CHILDREN:
                return getChildren();
            case RequirementPackage.HIERARCHICAL_ELEMENT__PARENT:
                return getParent();
            case RequirementPackage.HIERARCHICAL_ELEMENT__REQUIREMENT:
                return getRequirement();
            case RequirementPackage.HIERARCHICAL_ELEMENT__NEXT_REQ_INDEX:
                return getNextReqIndex();
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
            case RequirementPackage.HIERARCHICAL_ELEMENT__ELEMENT:
                setElement((EObject) newValue);
                return;
            case RequirementPackage.HIERARCHICAL_ELEMENT__CHILDREN:
                getChildren().clear();
                getChildren().addAll((Collection< ? extends HierarchicalElement>) newValue);
                return;
            case RequirementPackage.HIERARCHICAL_ELEMENT__PARENT:
                setParent((HierarchicalElement) newValue);
                return;
            case RequirementPackage.HIERARCHICAL_ELEMENT__REQUIREMENT:
                getRequirement().clear();
                getRequirement().addAll((Collection< ? extends Requirement>) newValue);
                return;
            case RequirementPackage.HIERARCHICAL_ELEMENT__NEXT_REQ_INDEX:
                setNextReqIndex((Long) newValue);
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
            case RequirementPackage.HIERARCHICAL_ELEMENT__ELEMENT:
                setElement((EObject) null);
                return;
            case RequirementPackage.HIERARCHICAL_ELEMENT__CHILDREN:
                getChildren().clear();
                return;
            case RequirementPackage.HIERARCHICAL_ELEMENT__PARENT:
                setParent((HierarchicalElement) null);
                return;
            case RequirementPackage.HIERARCHICAL_ELEMENT__REQUIREMENT:
                getRequirement().clear();
                return;
            case RequirementPackage.HIERARCHICAL_ELEMENT__NEXT_REQ_INDEX:
                setNextReqIndex(NEXT_REQ_INDEX_EDEFAULT);
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
            case RequirementPackage.HIERARCHICAL_ELEMENT__ELEMENT:
                return element != null;
            case RequirementPackage.HIERARCHICAL_ELEMENT__CHILDREN:
                return children != null && !children.isEmpty();
            case RequirementPackage.HIERARCHICAL_ELEMENT__PARENT:
                return getParent() != null;
            case RequirementPackage.HIERARCHICAL_ELEMENT__REQUIREMENT:
                return requirement != null && !requirement.isEmpty();
            case RequirementPackage.HIERARCHICAL_ELEMENT__NEXT_REQ_INDEX:
                return nextReqIndex != NEXT_REQ_INDEX_EDEFAULT;
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
        result.append(" (nextReqIndex: "); //$NON-NLS-1$
        result.append(nextReqIndex);
        result.append(')');
        return result.toString();
    }

} //HierarchicalElementImpl
