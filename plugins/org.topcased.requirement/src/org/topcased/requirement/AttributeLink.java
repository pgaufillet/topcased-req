/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attribute Link</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.requirement.AttributeLink#getPartial <em>Partial</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.requirement.RequirementPackage#getAttributeLink()
 * @model
 * @generated
 */
public interface AttributeLink extends ObjectAttribute
{
    /**
     * Returns the value of the '<em><b>Partial</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Partial</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Partial</em>' attribute.
     * @see #setPartial(Boolean)
     * @see org.topcased.requirement.RequirementPackage#getAttributeLink_Partial()
     * @model
     * @generated
     */
    Boolean getPartial();

    /**
     * Sets the value of the '{@link org.topcased.requirement.AttributeLink#getPartial <em>Partial</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Partial</em>' attribute.
     * @see #getPartial()
     * @generated
     */
    void setPartial(Boolean value);

} // AttributeLink
