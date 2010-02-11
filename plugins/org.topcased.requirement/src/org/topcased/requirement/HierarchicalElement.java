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
 * A representation of the model object '<em><b>Hierarchical Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.requirement.HierarchicalElement#getElement <em>Element</em>}</li>
 *   <li>{@link org.topcased.requirement.HierarchicalElement#getChildren <em>Children</em>}</li>
 *   <li>{@link org.topcased.requirement.HierarchicalElement#getParent <em>Parent</em>}</li>
 *   <li>{@link org.topcased.requirement.HierarchicalElement#getRequirement <em>Requirement</em>}</li>
 *   <li>{@link org.topcased.requirement.HierarchicalElement#getNextReqIndex <em>Next Req Index</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.requirement.RequirementPackage#getHierarchicalElement()
 * @model
 * @generated
 */
public interface HierarchicalElement extends IdentifiedElement
{
    /**
     * Returns the value of the '<em><b>Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Element</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element</em>' reference.
     * @see #setElement(EObject)
     * @see org.topcased.requirement.RequirementPackage#getHierarchicalElement_Element()
     * @model
     * @generated
     */
    EObject getElement();

    /**
     * Sets the value of the '{@link org.topcased.requirement.HierarchicalElement#getElement <em>Element</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Element</em>' reference.
     * @see #getElement()
     * @generated
     */
    void setElement(EObject value);

    /**
     * Returns the value of the '<em><b>Children</b></em>' containment reference list.
     * The list contents are of type {@link org.topcased.requirement.HierarchicalElement}.
     * It is bidirectional and its opposite is '{@link org.topcased.requirement.HierarchicalElement#getParent <em>Parent</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Children</em>' containment reference list.
     * @see org.topcased.requirement.RequirementPackage#getHierarchicalElement_Children()
     * @see org.topcased.requirement.HierarchicalElement#getParent
     * @model opposite="parent" containment="true"
     * @generated
     */
    EList<HierarchicalElement> getChildren();

    /**
     * Returns the value of the '<em><b>Parent</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link org.topcased.requirement.HierarchicalElement#getChildren <em>Children</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parent</em>' container reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parent</em>' container reference.
     * @see #setParent(HierarchicalElement)
     * @see org.topcased.requirement.RequirementPackage#getHierarchicalElement_Parent()
     * @see org.topcased.requirement.HierarchicalElement#getChildren
     * @model opposite="children" transient="false"
     * @generated
     */
    HierarchicalElement getParent();

    /**
     * Sets the value of the '{@link org.topcased.requirement.HierarchicalElement#getParent <em>Parent</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parent</em>' container reference.
     * @see #getParent()
     * @generated
     */
    void setParent(HierarchicalElement value);

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
     * @see org.topcased.requirement.RequirementPackage#getHierarchicalElement_Requirement()
     * @model containment="true"
     * @generated
     */
    EList<Requirement> getRequirement();

    /**
     * Returns the value of the '<em><b>Next Req Index</b></em>' attribute.
     * The default value is <code>"10"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Next Req Index</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Next Req Index</em>' attribute.
     * @see #setNextReqIndex(long)
     * @see org.topcased.requirement.RequirementPackage#getHierarchicalElement_NextReqIndex()
     * @model default="10" required="true"
     * @generated
     */
    long getNextReqIndex();

    /**
     * Sets the value of the '{@link org.topcased.requirement.HierarchicalElement#getNextReqIndex <em>Next Req Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Next Req Index</em>' attribute.
     * @see #getNextReqIndex()
     * @generated
     */
    void setNextReqIndex(long value);

} // HierarchicalElement
