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

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.UpstreamModel;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Project</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.requirement.impl.RequirementProjectImpl#getHierarchicalElement <em>Hierarchical Element</em>}</li>
 *   <li>{@link org.topcased.requirement.impl.RequirementProjectImpl#getAttributeConfiguration <em>Attribute Configuration</em>}</li>
 *   <li>{@link org.topcased.requirement.impl.RequirementProjectImpl#getChapter <em>Chapter</em>}</li>
 *   <li>{@link org.topcased.requirement.impl.RequirementProjectImpl#getUpstreamModel <em>Upstream Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RequirementProjectImpl extends IdentifiedElementImpl implements RequirementProject
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
     * The cached value of the '{@link #getAttributeConfiguration() <em>Attribute Configuration</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getAttributeConfiguration()
     * @generated
     * @ordered
     */
    protected AttributeConfiguration attributeConfiguration;

    /**
     * The cached value of the '{@link #getChapter() <em>Chapter</em>}' containment reference list.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getChapter()
     * @generated
     * @ordered
     */
    protected EList<SpecialChapter> chapter;

    /**
     * The cached value of the '{@link #getUpstreamModel() <em>Upstream Model</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getUpstreamModel()
     * @generated
     * @ordered
     */
    protected UpstreamModel upstreamModel;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected RequirementProjectImpl()
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
        return RequirementPackage.Literals.REQUIREMENT_PROJECT;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<HierarchicalElement> getHierarchicalElement()
    {
        if (hierarchicalElement == null)
        {
            hierarchicalElement = new EObjectContainmentEList.Resolving<HierarchicalElement>(HierarchicalElement.class, this, RequirementPackage.REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT);
        }
        return hierarchicalElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public AttributeConfiguration getAttributeConfiguration()
    {
        if (attributeConfiguration != null && attributeConfiguration.eIsProxy())
        {
            InternalEObject oldAttributeConfiguration = (InternalEObject) attributeConfiguration;
            attributeConfiguration = (AttributeConfiguration) eResolveProxy(oldAttributeConfiguration);
            if (attributeConfiguration != oldAttributeConfiguration)
            {
                InternalEObject newAttributeConfiguration = (InternalEObject) attributeConfiguration;
                NotificationChain msgs = oldAttributeConfiguration.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RequirementPackage.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION, null, null);
                if (newAttributeConfiguration.eInternalContainer() == null)
                {
                    msgs = newAttributeConfiguration.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RequirementPackage.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION, null, msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, RequirementPackage.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION, oldAttributeConfiguration, attributeConfiguration));
            }
        }
        return attributeConfiguration;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public AttributeConfiguration basicGetAttributeConfiguration()
    {
        return attributeConfiguration;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAttributeConfiguration(AttributeConfiguration newAttributeConfiguration, NotificationChain msgs)
    {
        AttributeConfiguration oldAttributeConfiguration = attributeConfiguration;
        attributeConfiguration = newAttributeConfiguration;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RequirementPackage.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION, oldAttributeConfiguration,
                    newAttributeConfiguration);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setAttributeConfiguration(AttributeConfiguration newAttributeConfiguration)
    {
        if (newAttributeConfiguration != attributeConfiguration)
        {
            NotificationChain msgs = null;
            if (attributeConfiguration != null)
                msgs = ((InternalEObject) attributeConfiguration).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RequirementPackage.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION, null, msgs);
            if (newAttributeConfiguration != null)
                msgs = ((InternalEObject) newAttributeConfiguration).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RequirementPackage.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION, null, msgs);
            msgs = basicSetAttributeConfiguration(newAttributeConfiguration, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RequirementPackage.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION, newAttributeConfiguration, newAttributeConfiguration));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<SpecialChapter> getChapter()
    {
        if (chapter == null)
        {
            chapter = new EObjectContainmentEList.Resolving<SpecialChapter>(SpecialChapter.class, this, RequirementPackage.REQUIREMENT_PROJECT__CHAPTER);
        }
        return chapter;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public UpstreamModel getUpstreamModel()
    {
        if (upstreamModel != null && upstreamModel.eIsProxy())
        {
            InternalEObject oldUpstreamModel = (InternalEObject) upstreamModel;
            upstreamModel = (UpstreamModel) eResolveProxy(oldUpstreamModel);
            if (upstreamModel != oldUpstreamModel)
            {
                InternalEObject newUpstreamModel = (InternalEObject) upstreamModel;
                NotificationChain msgs = oldUpstreamModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RequirementPackage.REQUIREMENT_PROJECT__UPSTREAM_MODEL, null, null);
                if (newUpstreamModel.eInternalContainer() == null)
                {
                    msgs = newUpstreamModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RequirementPackage.REQUIREMENT_PROJECT__UPSTREAM_MODEL, null, msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, RequirementPackage.REQUIREMENT_PROJECT__UPSTREAM_MODEL, oldUpstreamModel, upstreamModel));
            }
        }
        return upstreamModel;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public UpstreamModel basicGetUpstreamModel()
    {
        return upstreamModel;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUpstreamModel(UpstreamModel newUpstreamModel, NotificationChain msgs)
    {
        UpstreamModel oldUpstreamModel = upstreamModel;
        upstreamModel = newUpstreamModel;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RequirementPackage.REQUIREMENT_PROJECT__UPSTREAM_MODEL, oldUpstreamModel, newUpstreamModel);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setUpstreamModel(UpstreamModel newUpstreamModel)
    {
        if (newUpstreamModel != upstreamModel)
        {
            NotificationChain msgs = null;
            if (upstreamModel != null)
                msgs = ((InternalEObject) upstreamModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RequirementPackage.REQUIREMENT_PROJECT__UPSTREAM_MODEL, null, msgs);
            if (newUpstreamModel != null)
                msgs = ((InternalEObject) newUpstreamModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RequirementPackage.REQUIREMENT_PROJECT__UPSTREAM_MODEL, null, msgs);
            msgs = basicSetUpstreamModel(newUpstreamModel, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RequirementPackage.REQUIREMENT_PROJECT__UPSTREAM_MODEL, newUpstreamModel, newUpstreamModel));
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
            case RequirementPackage.REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT:
                return ((InternalEList< ? >) getHierarchicalElement()).basicRemove(otherEnd, msgs);
            case RequirementPackage.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION:
                return basicSetAttributeConfiguration(null, msgs);
            case RequirementPackage.REQUIREMENT_PROJECT__CHAPTER:
                return ((InternalEList< ? >) getChapter()).basicRemove(otherEnd, msgs);
            case RequirementPackage.REQUIREMENT_PROJECT__UPSTREAM_MODEL:
                return basicSetUpstreamModel(null, msgs);
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
            case RequirementPackage.REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT:
                return getHierarchicalElement();
            case RequirementPackage.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION:
                if (resolve)
                    return getAttributeConfiguration();
                return basicGetAttributeConfiguration();
            case RequirementPackage.REQUIREMENT_PROJECT__CHAPTER:
                return getChapter();
            case RequirementPackage.REQUIREMENT_PROJECT__UPSTREAM_MODEL:
                if (resolve)
                    return getUpstreamModel();
                return basicGetUpstreamModel();
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
            case RequirementPackage.REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT:
                getHierarchicalElement().clear();
                getHierarchicalElement().addAll((Collection< ? extends HierarchicalElement>) newValue);
                return;
            case RequirementPackage.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION:
                setAttributeConfiguration((AttributeConfiguration) newValue);
                return;
            case RequirementPackage.REQUIREMENT_PROJECT__CHAPTER:
                getChapter().clear();
                getChapter().addAll((Collection< ? extends SpecialChapter>) newValue);
                return;
            case RequirementPackage.REQUIREMENT_PROJECT__UPSTREAM_MODEL:
                setUpstreamModel((UpstreamModel) newValue);
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
            case RequirementPackage.REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT:
                getHierarchicalElement().clear();
                return;
            case RequirementPackage.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION:
                setAttributeConfiguration((AttributeConfiguration) null);
                return;
            case RequirementPackage.REQUIREMENT_PROJECT__CHAPTER:
                getChapter().clear();
                return;
            case RequirementPackage.REQUIREMENT_PROJECT__UPSTREAM_MODEL:
                setUpstreamModel((UpstreamModel) null);
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
            case RequirementPackage.REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT:
                return hierarchicalElement != null && !hierarchicalElement.isEmpty();
            case RequirementPackage.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION:
                return attributeConfiguration != null;
            case RequirementPackage.REQUIREMENT_PROJECT__CHAPTER:
                return chapter != null && !chapter.isEmpty();
            case RequirementPackage.REQUIREMENT_PROJECT__UPSTREAM_MODEL:
                return upstreamModel != null;
        }
        return super.eIsSet(featureID);
    }

} // RequirementProjectImpl
