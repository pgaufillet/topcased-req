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
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#getRegex <em>Regex</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#getAttributesToMatch <em>Attributes To Match</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#isMatchDescription <em>Match Description</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#isMatchId <em>Match Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParameters()
 * @model
 * @generated
 */
public interface DeletionParameters extends EObject {
	/**
	 * Returns the value of the '<em><b>Regex</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Regex</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Regex</em>' attribute.
	 * @see #setRegex(String)
	 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParameters_Regex()
	 * @model
	 * @generated
	 */
	String getRegex();

	/**
	 * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#getRegex <em>Regex</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Regex</em>' attribute.
	 * @see #getRegex()
	 * @generated
	 */
	void setRegex(String value);

	/**
	 * Returns the value of the '<em><b>Attributes To Match</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attributes To Match</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attributes To Match</em>' attribute list.
	 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParameters_AttributesToMatch()
	 * @model
	 * @generated
	 */
	EList<String> getAttributesToMatch();

	/**
	 * Returns the value of the '<em><b>Match Description</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Match Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Match Description</em>' attribute.
	 * @see #setMatchDescription(boolean)
	 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParameters_MatchDescription()
	 * @model default="false"
	 * @generated
	 */
	boolean isMatchDescription();

	/**
	 * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#isMatchDescription <em>Match Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Match Description</em>' attribute.
	 * @see #isMatchDescription()
	 * @generated
	 */
	void setMatchDescription(boolean value);

	/**
	 * Returns the value of the '<em><b>Match Id</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Match Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Match Id</em>' attribute.
	 * @see #setMatchId(boolean)
	 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDeletionParameters_MatchId()
	 * @model default="false"
	 * @generated
	 */
	boolean isMatchId();

	/**
	 * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.DeletionParameters#isMatchId <em>Match Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Match Id</em>' attribute.
	 * @see #isMatchId()
	 * @generated
	 */
	void setMatchId(boolean value);

} // DeletionParameters
