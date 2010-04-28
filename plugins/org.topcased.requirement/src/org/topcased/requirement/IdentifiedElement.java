/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement;

import org.eclipse.emf.ecore.EModelElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Identified Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.requirement.IdentifiedElement#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link org.topcased.requirement.IdentifiedElement#getShortDescription <em>Short Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.requirement.RequirementPackage#getIdentifiedElement()
 * @model abstract="true"
 * @generated
 */
public interface IdentifiedElement extends EModelElement
{
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Identifier</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Identifier</em>' attribute.
     * @see #setIdentifier(String)
     * @see org.topcased.requirement.RequirementPackage#getIdentifiedElement_Identifier()
     * @model id="true"
     * @generated
     */
    String getIdentifier();

    /**
     * Sets the value of the '{@link org.topcased.requirement.IdentifiedElement#getIdentifier <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' attribute.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(String value);

    /**
     * Returns the value of the '<em><b>Short Description</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Short Description</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Short Description</em>' attribute.
     * @see #setShortDescription(String)
     * @see org.topcased.requirement.RequirementPackage#getIdentifiedElement_ShortDescription()
     * @model
     * @generated
     */
    String getShortDescription();

    /**
     * Sets the value of the '{@link org.topcased.requirement.IdentifiedElement#getShortDescription <em>Short Description</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Short Description</em>' attribute.
     * @see #getShortDescription()
     * @generated
     */
    void setShortDescription(String value);

} // IdentifiedElement
