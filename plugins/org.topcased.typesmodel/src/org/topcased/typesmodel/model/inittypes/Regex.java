/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Regex</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.Regex#getExpression <em>Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getRegex()
 * @model
 * @generated
 */
public interface Regex extends Type
{
    /**
	 * Returns the value of the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Expression</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Expression</em>' attribute.
	 * @see #setExpression(String)
	 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getRegex_Expression()
	 * @model
	 * @generated
	 */
    String getExpression();

    /**
	 * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.Regex#getExpression <em>Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expression</em>' attribute.
	 * @see #getExpression()
	 * @generated
	 */
    void setExpression(String value);

} // Regex
