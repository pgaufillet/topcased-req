/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Object Attribute</b></em>'. <!-- end-user-doc
 * -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.topcased.requirement.ObjectAttribute#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.topcased.requirement.RequirementPackage#getObjectAttribute()
 * @model
 * @generated
 */
public interface ObjectAttribute extends Attribute
{
    /**
     * Returns the value of the '<em><b>Value</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' reference isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' reference.
     * @see #setValue(EObject)
     * @see org.topcased.requirement.RequirementPackage#getObjectAttribute_Value()
     * @model
     * @generated
     */
    EObject getValue();

    /**
     * Sets the value of the '{@link org.topcased.requirement.ObjectAttribute#getValue <em>Value</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Value</em>' reference.
     * @see #getValue()
     * @generated
     */
    void setValue(EObject value);

} // ObjectAttribute
