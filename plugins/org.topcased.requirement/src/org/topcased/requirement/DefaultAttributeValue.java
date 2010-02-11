/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Default Attribute Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.requirement.DefaultAttributeValue#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.requirement.RequirementPackage#getDefaultAttributeValue()
 * @model
 * @generated
 */
public interface DefaultAttributeValue extends EObject
{
    /**
     * Returns the value of the '<em><b>Value</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' reference.
     * @see #setValue(AttributeValue)
     * @see org.topcased.requirement.RequirementPackage#getDefaultAttributeValue_Value()
     * @model required="true"
     * @generated
     */
    AttributeValue getValue();

    /**
     * Sets the value of the '{@link org.topcased.requirement.DefaultAttributeValue#getValue <em>Value</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' reference.
     * @see #getValue()
     * @generated
     */
    void setValue(AttributeValue value);

} // DefaultAttributeValue
