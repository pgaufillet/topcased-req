/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Special Chapter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.requirement.SpecialChapter#getHierarchicalElement <em>Hierarchical Element</em>}</li>
 *   <li>{@link org.topcased.requirement.SpecialChapter#getRequirement <em>Requirement</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.requirement.RequirementPackage#getSpecialChapter()
 * @model abstract="true"
 * @generated
 */
public interface SpecialChapter extends EObject
{
    /**
     * Returns the value of the '<em><b>Hierarchical Element</b></em>' containment reference list.
     * The list contents are of type {@link org.topcased.requirement.HierarchicalElement}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Hierarchical Element</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Hierarchical Element</em>' containment reference list.
     * @see org.topcased.requirement.RequirementPackage#getSpecialChapter_HierarchicalElement()
     * @model containment="true"
     * @generated
     */
    EList<HierarchicalElement> getHierarchicalElement();

    /**
     * Returns the value of the '<em><b>Requirement</b></em>' containment reference list.
     * The list contents are of type {@link org.topcased.requirement.Requirement}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Requirement</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Requirement</em>' containment reference list.
     * @see org.topcased.requirement.RequirementPackage#getSpecialChapter_Requirement()
     * @model containment="true"
     * @generated
     */
    EList<Requirement> getRequirement();

} // SpecialChapter
