/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract class of
 * the model. <!-- end-user-doc -->
 * @see org.topcased.requirement.RequirementPackage
 * @generated
 */
public interface RequirementFactory extends EFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    RequirementFactory eINSTANCE = org.topcased.requirement.impl.RequirementFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Project</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Project</em>'.
     * @generated
     */
    RequirementProject createRequirementProject();

    /**
     * Returns a new object of class '<em>Hierarchical Element</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Hierarchical Element</em>'.
     * @generated
     */
    HierarchicalElement createHierarchicalElement();

    /**
     * Returns a new object of class '<em>Current Requirement</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Current Requirement</em>'.
     * @generated
     */
    CurrentRequirement createCurrentRequirement();

    /**
     * Returns a new object of class '<em>Attribute Configuration</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Attribute Configuration</em>'.
     * @generated
     */
    AttributeConfiguration createAttributeConfiguration();

    /**
     * Returns a new object of class '<em>Configurated Attribute</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Configurated Attribute</em>'.
     * @generated
     */
    ConfiguratedAttribute createConfiguratedAttribute();

    /**
     * Returns a new object of class '<em>Attribute Value</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Attribute Value</em>'.
     * @generated
     */
    AttributeValue createAttributeValue();

    /**
     * Returns a new object of class '<em>Default Attribute Value</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Default Attribute Value</em>'.
     * @generated
     */
    DefaultAttributeValue createDefaultAttributeValue();

    /**
     * Returns a new object of class '<em>Text Attribute</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Text Attribute</em>'.
     * @generated
     */
    TextAttribute createTextAttribute();

    /**
     * Returns a new object of class '<em>Object Attribute</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Object Attribute</em>'.
     * @generated
     */
    ObjectAttribute createObjectAttribute();

    /**
     * Returns a new object of class '<em>Upstream Model</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Upstream Model</em>'.
     * @generated
     */
    UpstreamModel createUpstreamModel();

    /**
     * Returns a new object of class '<em>Attribute Link</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Attribute Link</em>'.
     * @generated
     */
    AttributeLink createAttributeLink();

    /**
     * Returns a new object of class '<em>Attribute Allocate</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Attribute Allocate</em>'.
     * @generated
     */
    AttributeAllocate createAttributeAllocate();

    /**
     * Returns a new object of class '<em>Untraced Chapter</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Untraced Chapter</em>'.
     * @generated
     */
    UntracedChapter createUntracedChapter();

    /**
     * Returns a new object of class '<em>Problem Chapter</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Problem Chapter</em>'.
     * @generated
     */
    ProblemChapter createProblemChapter();

    /**
     * Returns a new object of class '<em>Trash Chapter</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Trash Chapter</em>'.
     * @generated
     */
    TrashChapter createTrashChapter();

    /**
     * Returns a new object of class '<em>Anonymous Requirement</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return a new object of class '<em>Anonymous Requirement</em>'.
     * @generated
     */
    AnonymousRequirement createAnonymousRequirement();

    /**
     * Returns a new object of class '<em>Deleted Chapter</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Deleted Chapter</em>'.
     * @generated
     */
    DeletedChapter createDeletedChapter();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    RequirementPackage getRequirementPackage();

} // RequirementFactory
