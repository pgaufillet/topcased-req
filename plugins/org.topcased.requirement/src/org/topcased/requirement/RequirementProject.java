/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.requirement.RequirementProject#getHierarchicalElement <em>Hierarchical Element</em>}</li>
 *   <li>{@link org.topcased.requirement.RequirementProject#getAttributeConfiguration <em>Attribute Configuration</em>}</li>
 *   <li>{@link org.topcased.requirement.RequirementProject#getChapter <em>Chapter</em>}</li>
 *   <li>{@link org.topcased.requirement.RequirementProject#getUpstreamModel <em>Upstream Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.requirement.RequirementPackage#getRequirementProject()
 * @model
 * @generated
 */
public interface RequirementProject extends IdentifiedElement
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
     * @see org.topcased.requirement.RequirementPackage#getRequirementProject_HierarchicalElement()
     * @model containment="true"
     * @generated
     */
    EList<HierarchicalElement> getHierarchicalElement();

    /**
     * Returns the value of the '<em><b>Attribute Configuration</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Attribute Configuration</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Attribute Configuration</em>' containment reference.
     * @see #setAttributeConfiguration(AttributeConfiguration)
     * @see org.topcased.requirement.RequirementPackage#getRequirementProject_AttributeConfiguration()
     * @model containment="true"
     * @generated
     */
    AttributeConfiguration getAttributeConfiguration();

    /**
     * Sets the value of the '{@link org.topcased.requirement.RequirementProject#getAttributeConfiguration <em>Attribute Configuration</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Attribute Configuration</em>' containment reference.
     * @see #getAttributeConfiguration()
     * @generated
     */
    void setAttributeConfiguration(AttributeConfiguration value);

    /**
     * Returns the value of the '<em><b>Chapter</b></em>' containment reference list.
     * The list contents are of type {@link org.topcased.requirement.SpecialChapter}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Chapter</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Chapter</em>' containment reference list.
     * @see org.topcased.requirement.RequirementPackage#getRequirementProject_Chapter()
     * @model containment="true" lower="3" upper="3"
     * @generated
     */
    EList<SpecialChapter> getChapter();

    /**
     * Returns the value of the '<em><b>Upstream Model</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Upstream Model</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Upstream Model</em>' containment reference.
     * @see #setUpstreamModel(UpstreamModel)
     * @see org.topcased.requirement.RequirementPackage#getRequirementProject_UpstreamModel()
     * @model containment="true" required="true"
     * @generated
     */
    UpstreamModel getUpstreamModel();

    /**
     * Sets the value of the '{@link org.topcased.requirement.RequirementProject#getUpstreamModel <em>Upstream Model</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Upstream Model</em>' containment reference.
     * @see #getUpstreamModel()
     * @generated
     */
    void setUpstreamModel(UpstreamModel value);

} // RequirementProject
