/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Column</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.Column#getNumber <em>Number</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getColumn()
 * @model
 * @generated
 */
public interface Column extends Regex
{

    /**
     * Returns the value of the '<em><b>Number</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Number</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Number</em>' attribute.
     * @see #setNumber(int)
     * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getColumn_Number()
     * @model
     * @generated
     */
    int getNumber();

    /**
     * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.Column#getNumber <em>Number</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Number</em>' attribute.
     * @see #getNumber()
     * @generated
     */
    void setNumber(int value);
} // Column
