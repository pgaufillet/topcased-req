/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Current Requirement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.requirement.CurrentRequirement#isImpacted <em>Impacted</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.requirement.RequirementPackage#getCurrentRequirement()
 * @model
 * @generated
 */
public interface CurrentRequirement extends Requirement
{
    /**
     * Returns the value of the '<em><b>Impacted</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Impacted</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Impacted</em>' attribute.
     * @see #setImpacted(boolean)
     * @see org.topcased.requirement.RequirementPackage#getCurrentRequirement_Impacted()
     * @model default="false"
     * @generated
     */
    boolean isImpacted();

    /**
     * Sets the value of the '{@link org.topcased.requirement.CurrentRequirement#isImpacted <em>Impacted</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Impacted</em>' attribute.
     * @see #isImpacted()
     * @generated
     */
    void setImpacted(boolean value);

} // CurrentRequirement
