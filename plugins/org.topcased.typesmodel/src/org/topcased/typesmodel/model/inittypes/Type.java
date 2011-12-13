/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.Type#getName <em>Name</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.Type#isIsText <em>Is Text</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.Type#isIsReference <em>Is Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getType()
 * @model abstract="true"
 * @generated
 */
public interface Type extends EObject
{
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getType_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.Type#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Is Text</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Text</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Text</em>' attribute.
     * @see #setIsText(boolean)
     * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getType_IsText()
     * @model
     * @generated
     */
    boolean isIsText();

    /**
     * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.Type#isIsText <em>Is Text</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Text</em>' attribute.
     * @see #isIsText()
     * @generated
     */
    void setIsText(boolean value);

    /**
     * Returns the value of the '<em><b>Is Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Reference</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Reference</em>' attribute.
     * @see #setIsReference(boolean)
     * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getType_IsReference()
     * @model
     * @generated
     */
    boolean isIsReference();

    /**
     * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.Type#isIsReference <em>Is Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Reference</em>' attribute.
     * @see #isIsReference()
     * @generated
     */
    void setIsReference(boolean value);

} // Type
