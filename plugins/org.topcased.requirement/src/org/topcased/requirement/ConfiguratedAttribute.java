/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Configurated Attribute</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.requirement.ConfiguratedAttribute#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link org.topcased.requirement.ConfiguratedAttribute#getListValue <em>List Value</em>}</li>
 *   <li>{@link org.topcased.requirement.ConfiguratedAttribute#getName <em>Name</em>}</li>
 *   <li>{@link org.topcased.requirement.ConfiguratedAttribute#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.requirement.RequirementPackage#getConfiguratedAttribute()
 * @model
 * @generated
 */
public interface ConfiguratedAttribute extends EObject
{
    /**
     * Returns the value of the '<em><b>Default Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Default Value</em>' containment reference isn't clear, there really should be more of
     * a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Default Value</em>' containment reference.
     * @see #setDefaultValue(DefaultAttributeValue)
     * @see org.topcased.requirement.RequirementPackage#getConfiguratedAttribute_DefaultValue()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    DefaultAttributeValue getDefaultValue();

    /**
     * Sets the value of the '{@link org.topcased.requirement.ConfiguratedAttribute#getDefaultValue <em>Default Value</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Value</em>' containment reference.
     * @see #getDefaultValue()
     * @generated
     */
    void setDefaultValue(DefaultAttributeValue value);

    /**
     * Returns the value of the '<em><b>List Value</b></em>' containment reference list.
     * The list contents are of type {@link org.topcased.requirement.AttributeValue}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>List Value</em>' containment reference list isn't clear, there really should be more
     * of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>List Value</em>' containment reference list.
     * @see org.topcased.requirement.RequirementPackage#getConfiguratedAttribute_ListValue()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<AttributeValue> getListValue();

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.topcased.requirement.RequirementPackage#getConfiguratedAttribute_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.topcased.requirement.ConfiguratedAttribute#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * The literals are from the enumeration {@link org.topcased.requirement.AttributesType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see org.topcased.requirement.AttributesType
     * @see #setType(AttributesType)
     * @see org.topcased.requirement.RequirementPackage#getConfiguratedAttribute_Type()
     * @model
     * @generated
     */
    AttributesType getType();

    /**
     * Sets the value of the '{@link org.topcased.requirement.ConfiguratedAttribute#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see org.topcased.requirement.AttributesType
     * @see #getType()
     * @generated
     */
    void setType(AttributesType value);

} // ConfiguratedAttribute
