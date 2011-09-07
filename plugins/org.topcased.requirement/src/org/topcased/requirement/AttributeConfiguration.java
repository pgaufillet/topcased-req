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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Attribute Configuration</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.requirement.AttributeConfiguration#getListAttributes <em>List Attributes</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.requirement.RequirementPackage#getAttributeConfiguration()
 * @model
 * @generated
 */
public interface AttributeConfiguration extends EObject
{
    /**
     * Returns the value of the '<em><b>List Attributes</b></em>' containment reference list.
     * The list contents are of type {@link org.topcased.requirement.ConfiguratedAttribute}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>List Attributes</em>' containment reference list isn't clear, there really should be
     * more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>List Attributes</em>' containment reference list.
     * @see org.topcased.requirement.RequirementPackage#getAttributeConfiguration_ListAttributes()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<ConfiguratedAttribute> getListAttributes();

} // AttributeConfiguration
