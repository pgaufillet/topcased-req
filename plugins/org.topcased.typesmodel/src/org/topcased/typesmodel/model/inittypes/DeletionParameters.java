/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deletion Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#getRegexDescription <em>Regex Description</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#getRegexId <em>Regex Id</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#getRegexAttributes <em>Regex Attributes</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#getFilterRegexAttributes <em>Filter Regex Attributes</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#isIsAnd <em>Is And</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParameters()
 * @model
 * @generated
 */
public interface DeletionParameters extends EObject {
	/**
	 * Returns the value of the '<em><b>Regex Description</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Regex Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Regex Description</em>' attribute.
	 * @see #setRegexDescription(String)
	 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParameters_RegexDescription()
	 * @model default=""
	 * @generated
	 */
	String getRegexDescription();

	/**
	 * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#getRegexDescription <em>Regex Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Regex Description</em>' attribute.
	 * @see #getRegexDescription()
	 * @generated
	 */
	void setRegexDescription(String value);

	/**
	 * Returns the value of the '<em><b>Regex Id</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Regex Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Regex Id</em>' attribute.
	 * @see #setRegexId(String)
	 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParameters_RegexId()
	 * @model default=""
	 * @generated
	 */
	String getRegexId();

	/**
	 * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#getRegexId <em>Regex Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Regex Id</em>' attribute.
	 * @see #getRegexId()
	 * @generated
	 */
	void setRegexId(String value);

	/**
	 * Returns the value of the '<em><b>Regex Attributes</b></em>' containment reference list.
	 * The list contents are of type {@link org.topcased.typesmodel.model.inittypes.DeletionParemeter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Regex Attributes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Regex Attributes</em>' containment reference list.
	 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParameters_RegexAttributes()
	 * @model containment="true"
	 * @generated
	 */
	EList<DeletionParemeter> getRegexAttributes();

	/**
	 * Returns the value of the '<em><b>Filter Regex Attributes</b></em>' reference list.
	 * The list contents are of type {@link org.topcased.typesmodel.model.inittypes.DeletionParemeter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Filter Regex Attributes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Filter Regex Attributes</em>' reference list.
	 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParameters_FilterRegexAttributes()
	 * @model
	 * @generated
	 */
	EList<DeletionParemeter> getFilterRegexAttributes();

	/**
	 * Returns the value of the '<em><b>Is And</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is And</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is And</em>' attribute.
	 * @see #setIsAnd(boolean)
	 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParameters_IsAnd()
	 * @model
	 * @generated
	 */
	boolean isIsAnd();

	/**
	 * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#isIsAnd <em>Is And</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is And</em>' attribute.
	 * @see #isIsAnd()
	 * @generated
	 */
	void setIsAnd(boolean value);

} // DeletionParameters
