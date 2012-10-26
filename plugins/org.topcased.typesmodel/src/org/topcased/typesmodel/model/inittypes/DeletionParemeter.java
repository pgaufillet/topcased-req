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
 * A representation of the model object '<em><b>Deletion Paremeter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DeletionParemeter#getNameAttribute <em>Name Attribute</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DeletionParemeter#getRegexAttribute <em>Regex Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParemeter()
 * @model
 * @generated
 */
public interface DeletionParemeter extends EObject {
	/**
	 * Returns the value of the '<em><b>Name Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name Attribute</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name Attribute</em>' attribute.
	 * @see #setNameAttribute(String)
	 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParemeter_NameAttribute()
	 * @model
	 * @generated
	 */
	String getNameAttribute();

	/**
	 * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.DeletionParemeter#getNameAttribute <em>Name Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name Attribute</em>' attribute.
	 * @see #getNameAttribute()
	 * @generated
	 */
	void setNameAttribute(String value);

	/**
	 * Returns the value of the '<em><b>Regex Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Regex Attribute</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Regex Attribute</em>' attribute.
	 * @see #setRegexAttribute(String)
	 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParemeter_RegexAttribute()
	 * @model
	 * @generated
	 */
	String getRegexAttribute();

	/**
	 * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.DeletionParemeter#getRegexAttribute <em>Regex Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Regex Attribute</em>' attribute.
	 * @see #getRegexAttribute()
	 * @generated
	 */
	void setRegexAttribute(String value);

} // DeletionParemeter
