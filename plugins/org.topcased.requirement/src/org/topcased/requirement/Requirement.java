/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Requirement</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.requirement.Requirement#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.topcased.requirement.Requirement#getExternalResources <em>External Resources</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.requirement.RequirementPackage#getRequirement()
 * @model abstract="true"
 * @generated
 */
public interface Requirement extends IdentifiedElement
{
    /**
     * Returns the value of the '<em><b>Attribute</b></em>' containment reference list.
     * The list contents are of type {@link org.topcased.requirement.Attribute}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Attribute</em>' containment reference list isn't clear, there really should be more of
     * a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Attribute</em>' containment reference list.
     * @see org.topcased.requirement.RequirementPackage#getRequirement_Attribute()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<Attribute> getAttribute();

    /**
     * Returns the value of the '<em><b>External Resources</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>External Resources</em>' attribute list isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>External Resources</em>' attribute list.
     * @see org.topcased.requirement.RequirementPackage#getRequirement_ExternalResources()
     * @model
     * @generated
     */
    EList<String> getExternalResources();

} // Requirement
