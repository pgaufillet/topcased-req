/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.topcased.requirement.AnonymousRequirement;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeAllocate;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.AttributesType;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.DefaultAttributeValue;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.IdentifiedElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.ProblemChapter;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.TrashChapter;
import org.topcased.requirement.UntracedChapter;
import org.topcased.requirement.UpstreamModel;

import ttm.TtmPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RequirementPackageImpl extends EPackageImpl implements RequirementPackage
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass requirementProjectEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass hierarchicalElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass currentRequirementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass attributeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass attributeConfigurationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass configuratedAttributeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass attributeValueEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass defaultAttributeValueEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass identifiedElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass specialChapterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass textAttributeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass objectAttributeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass upstreamModelEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass attributeLinkEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass attributeAllocateEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass untracedChapterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass problemChapterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass trashChapterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass requirementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass anonymousRequirementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum attributesTypeEEnum = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.topcased.requirement.RequirementPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private RequirementPackageImpl()
    {
        super(eNS_URI, RequirementFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link RequirementPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static RequirementPackage init()
    {
        if (isInited)
            return (RequirementPackage) EPackage.Registry.INSTANCE.getEPackage(RequirementPackage.eNS_URI);

        // Obtain or create and register package
        RequirementPackageImpl theRequirementPackage = (RequirementPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof RequirementPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
                : new RequirementPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        EcorePackage.eINSTANCE.eClass();
        TtmPackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theRequirementPackage.createPackageContents();

        // Initialize created meta-data
        theRequirementPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theRequirementPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(RequirementPackage.eNS_URI, theRequirementPackage);
        return theRequirementPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRequirementProject()
    {
        return requirementProjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRequirementProject_HierarchicalElement()
    {
        return (EReference) requirementProjectEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRequirementProject_AttributeConfiguration()
    {
        return (EReference) requirementProjectEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRequirementProject_Chapter()
    {
        return (EReference) requirementProjectEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRequirementProject_UpstreamModel()
    {
        return (EReference) requirementProjectEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getHierarchicalElement()
    {
        return hierarchicalElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getHierarchicalElement_Element()
    {
        return (EReference) hierarchicalElementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getHierarchicalElement_Children()
    {
        return (EReference) hierarchicalElementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getHierarchicalElement_Parent()
    {
        return (EReference) hierarchicalElementEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getHierarchicalElement_Requirement()
    {
        return (EReference) hierarchicalElementEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHierarchicalElement_NextReqIndex()
    {
        return (EAttribute) hierarchicalElementEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCurrentRequirement()
    {
        return currentRequirementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurrentRequirement_Impacted()
    {
        return (EAttribute) currentRequirementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAttribute()
    {
        return attributeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAttribute_Name()
    {
        return (EAttribute) attributeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAttributeConfiguration()
    {
        return attributeConfigurationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAttributeConfiguration_ListAttributes()
    {
        return (EReference) attributeConfigurationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getConfiguratedAttribute()
    {
        return configuratedAttributeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConfiguratedAttribute_DefaultValue()
    {
        return (EReference) configuratedAttributeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConfiguratedAttribute_ListValue()
    {
        return (EReference) configuratedAttributeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConfiguratedAttribute_Name()
    {
        return (EAttribute) configuratedAttributeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConfiguratedAttribute_Type()
    {
        return (EAttribute) configuratedAttributeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAttributeValue()
    {
        return attributeValueEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAttributeValue_Value()
    {
        return (EAttribute) attributeValueEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDefaultAttributeValue()
    {
        return defaultAttributeValueEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDefaultAttributeValue_Value()
    {
        return (EReference) defaultAttributeValueEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getIdentifiedElement()
    {
        return identifiedElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIdentifiedElement_Identifier()
    {
        return (EAttribute) identifiedElementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIdentifiedElement_ShortDescription()
    {
        return (EAttribute) identifiedElementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSpecialChapter()
    {
        return specialChapterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSpecialChapter_HierarchicalElement()
    {
        return (EReference) specialChapterEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSpecialChapter_Requirement()
    {
        return (EReference) specialChapterEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTextAttribute()
    {
        return textAttributeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTextAttribute_Value()
    {
        return (EAttribute) textAttributeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getObjectAttribute()
    {
        return objectAttributeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getObjectAttribute_Value()
    {
        return (EReference) objectAttributeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUpstreamModel()
    {
        return upstreamModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAttributeLink()
    {
        return attributeLinkEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAttributeLink_Partial()
    {
        return (EAttribute) attributeLinkEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAttributeAllocate()
    {
        return attributeAllocateEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUntracedChapter()
    {
        return untracedChapterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getProblemChapter()
    {
        return problemChapterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTrashChapter()
    {
        return trashChapterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRequirement()
    {
        return requirementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRequirement_Attribute()
    {
        return (EReference) requirementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRequirement_ExternalResources()
    {
        return (EAttribute) requirementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAnonymousRequirement()
    {
        return anonymousRequirementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getAttributesType()
    {
        return attributesTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequirementFactory getRequirementFactory()
    {
        return (RequirementFactory) getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents()
    {
        if (isCreated)
            return;
        isCreated = true;

        // Create classes and their features
        requirementProjectEClass = createEClass(REQUIREMENT_PROJECT);
        createEReference(requirementProjectEClass, REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT);
        createEReference(requirementProjectEClass, REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION);
        createEReference(requirementProjectEClass, REQUIREMENT_PROJECT__CHAPTER);
        createEReference(requirementProjectEClass, REQUIREMENT_PROJECT__UPSTREAM_MODEL);

        hierarchicalElementEClass = createEClass(HIERARCHICAL_ELEMENT);
        createEReference(hierarchicalElementEClass, HIERARCHICAL_ELEMENT__ELEMENT);
        createEReference(hierarchicalElementEClass, HIERARCHICAL_ELEMENT__CHILDREN);
        createEReference(hierarchicalElementEClass, HIERARCHICAL_ELEMENT__PARENT);
        createEReference(hierarchicalElementEClass, HIERARCHICAL_ELEMENT__REQUIREMENT);
        createEAttribute(hierarchicalElementEClass, HIERARCHICAL_ELEMENT__NEXT_REQ_INDEX);

        currentRequirementEClass = createEClass(CURRENT_REQUIREMENT);
        createEAttribute(currentRequirementEClass, CURRENT_REQUIREMENT__IMPACTED);

        attributeEClass = createEClass(ATTRIBUTE);
        createEAttribute(attributeEClass, ATTRIBUTE__NAME);

        attributeConfigurationEClass = createEClass(ATTRIBUTE_CONFIGURATION);
        createEReference(attributeConfigurationEClass, ATTRIBUTE_CONFIGURATION__LIST_ATTRIBUTES);

        configuratedAttributeEClass = createEClass(CONFIGURATED_ATTRIBUTE);
        createEReference(configuratedAttributeEClass, CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE);
        createEReference(configuratedAttributeEClass, CONFIGURATED_ATTRIBUTE__LIST_VALUE);
        createEAttribute(configuratedAttributeEClass, CONFIGURATED_ATTRIBUTE__NAME);
        createEAttribute(configuratedAttributeEClass, CONFIGURATED_ATTRIBUTE__TYPE);

        attributeValueEClass = createEClass(ATTRIBUTE_VALUE);
        createEAttribute(attributeValueEClass, ATTRIBUTE_VALUE__VALUE);

        defaultAttributeValueEClass = createEClass(DEFAULT_ATTRIBUTE_VALUE);
        createEReference(defaultAttributeValueEClass, DEFAULT_ATTRIBUTE_VALUE__VALUE);

        identifiedElementEClass = createEClass(IDENTIFIED_ELEMENT);
        createEAttribute(identifiedElementEClass, IDENTIFIED_ELEMENT__IDENTIFIER);
        createEAttribute(identifiedElementEClass, IDENTIFIED_ELEMENT__SHORT_DESCRIPTION);

        specialChapterEClass = createEClass(SPECIAL_CHAPTER);
        createEReference(specialChapterEClass, SPECIAL_CHAPTER__HIERARCHICAL_ELEMENT);
        createEReference(specialChapterEClass, SPECIAL_CHAPTER__REQUIREMENT);

        textAttributeEClass = createEClass(TEXT_ATTRIBUTE);
        createEAttribute(textAttributeEClass, TEXT_ATTRIBUTE__VALUE);

        objectAttributeEClass = createEClass(OBJECT_ATTRIBUTE);
        createEReference(objectAttributeEClass, OBJECT_ATTRIBUTE__VALUE);

        upstreamModelEClass = createEClass(UPSTREAM_MODEL);

        attributeLinkEClass = createEClass(ATTRIBUTE_LINK);
        createEAttribute(attributeLinkEClass, ATTRIBUTE_LINK__PARTIAL);

        attributeAllocateEClass = createEClass(ATTRIBUTE_ALLOCATE);

        untracedChapterEClass = createEClass(UNTRACED_CHAPTER);

        problemChapterEClass = createEClass(PROBLEM_CHAPTER);

        trashChapterEClass = createEClass(TRASH_CHAPTER);

        requirementEClass = createEClass(REQUIREMENT);
        createEReference(requirementEClass, REQUIREMENT__ATTRIBUTE);
        createEAttribute(requirementEClass, REQUIREMENT__EXTERNAL_RESOURCES);

        anonymousRequirementEClass = createEClass(ANONYMOUS_REQUIREMENT);

        // Create enums
        attributesTypeEEnum = createEEnum(ATTRIBUTES_TYPE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents()
    {
        if (isInitialized)
            return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        EcorePackage theEcorePackage = (EcorePackage) EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
        TtmPackage theTtmPackage = (TtmPackage) EPackage.Registry.INSTANCE.getEPackage(TtmPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        requirementProjectEClass.getESuperTypes().add(this.getIdentifiedElement());
        hierarchicalElementEClass.getESuperTypes().add(this.getIdentifiedElement());
        currentRequirementEClass.getESuperTypes().add(this.getRequirement());
        attributeEClass.getESuperTypes().add(theEcorePackage.getEModelElement());
        identifiedElementEClass.getESuperTypes().add(theEcorePackage.getEModelElement());
        textAttributeEClass.getESuperTypes().add(this.getAttribute());
        objectAttributeEClass.getESuperTypes().add(this.getAttribute());
        upstreamModelEClass.getESuperTypes().add(theTtmPackage.getProject());
        attributeLinkEClass.getESuperTypes().add(this.getObjectAttribute());
        attributeAllocateEClass.getESuperTypes().add(this.getObjectAttribute());
        untracedChapterEClass.getESuperTypes().add(this.getSpecialChapter());
        problemChapterEClass.getESuperTypes().add(this.getSpecialChapter());
        trashChapterEClass.getESuperTypes().add(this.getSpecialChapter());
        requirementEClass.getESuperTypes().add(this.getIdentifiedElement());
        anonymousRequirementEClass.getESuperTypes().add(this.getRequirement());

        // Initialize classes and features; add operations and parameters
        initEClass(requirementProjectEClass, RequirementProject.class, "RequirementProject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRequirementProject_HierarchicalElement(), this.getHierarchicalElement(), null, "hierarchicalElement", null, 0, -1, RequirementProject.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRequirementProject_AttributeConfiguration(), this.getAttributeConfiguration(), null, "attributeConfiguration", null, 0, 1, RequirementProject.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRequirementProject_Chapter(), this.getSpecialChapter(), null, "chapter", null, 3, 3, RequirementProject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
                !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRequirementProject_UpstreamModel(), this.getUpstreamModel(), null, "upstreamModel", null, 1, 1, RequirementProject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(hierarchicalElementEClass, HierarchicalElement.class, "HierarchicalElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getHierarchicalElement_Element(), theEcorePackage.getEObject(), null, "element", null, 0, 1, HierarchicalElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getHierarchicalElement_Children(), this.getHierarchicalElement(), this.getHierarchicalElement_Parent(), "children", null, 0, -1, HierarchicalElement.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getHierarchicalElement_Parent(), this.getHierarchicalElement(), this.getHierarchicalElement_Children(), "parent", null, 0, 1, HierarchicalElement.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getHierarchicalElement_Requirement(), this.getRequirement(), null, "requirement", null, 0, -1, HierarchicalElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHierarchicalElement_NextReqIndex(), ecorePackage.getELong(), "nextReqIndex", "10", 1, 1, HierarchicalElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(currentRequirementEClass, CurrentRequirement.class, "CurrentRequirement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCurrentRequirement_Impacted(), ecorePackage.getEBoolean(), "impacted", "false", 0, 1, CurrentRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(attributeEClass, Attribute.class, "Attribute", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAttribute_Name(), ecorePackage.getEString(), "name", null, 0, 1, Attribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
                IS_ORDERED);

        initEClass(attributeConfigurationEClass, AttributeConfiguration.class, "AttributeConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAttributeConfiguration_ListAttributes(), this.getConfiguratedAttribute(), null, "listAttributes", null, 0, -1, AttributeConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(configuratedAttributeEClass, ConfiguratedAttribute.class, "ConfiguratedAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConfiguratedAttribute_DefaultValue(), this.getDefaultAttributeValue(), null, "defaultValue", null, 0, 1, ConfiguratedAttribute.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getConfiguratedAttribute_ListValue(), this.getAttributeValue(), null, "listValue", null, 0, -1, ConfiguratedAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConfiguratedAttribute_Name(), theEcorePackage.getEString(), "name", null, 0, 1, ConfiguratedAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConfiguratedAttribute_Type(), this.getAttributesType(), "type", null, 0, 1, ConfiguratedAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(attributeValueEClass, AttributeValue.class, "AttributeValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAttributeValue_Value(), theEcorePackage.getEString(), "value", null, 0, 1, AttributeValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(defaultAttributeValueEClass, DefaultAttributeValue.class, "DefaultAttributeValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDefaultAttributeValue_Value(), this.getAttributeValue(), null, "value", null, 1, 1, DefaultAttributeValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(identifiedElementEClass, IdentifiedElement.class, "IdentifiedElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getIdentifiedElement_Identifier(), theEcorePackage.getEString(), "identifier", null, 0, 1, IdentifiedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIdentifiedElement_ShortDescription(), theEcorePackage.getEString(), "shortDescription", null, 0, 1, IdentifiedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(specialChapterEClass, SpecialChapter.class, "SpecialChapter", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSpecialChapter_HierarchicalElement(), this.getHierarchicalElement(), null, "hierarchicalElement", null, 0, -1, SpecialChapter.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSpecialChapter_Requirement(), this.getRequirement(), null, "requirement", null, 0, -1, SpecialChapter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
                !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(textAttributeEClass, TextAttribute.class, "TextAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTextAttribute_Value(), theEcorePackage.getEString(), "value", null, 0, 1, TextAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);

        initEClass(objectAttributeEClass, ObjectAttribute.class, "ObjectAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getObjectAttribute_Value(), theEcorePackage.getEObject(), null, "value", null, 0, 1, ObjectAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(upstreamModelEClass, UpstreamModel.class, "UpstreamModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(attributeLinkEClass, AttributeLink.class, "AttributeLink", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAttributeLink_Partial(), theEcorePackage.getEBooleanObject(), "partial", null, 0, 1, AttributeLink.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(attributeAllocateEClass, AttributeAllocate.class, "AttributeAllocate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(untracedChapterEClass, UntracedChapter.class, "UntracedChapter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(problemChapterEClass, ProblemChapter.class, "ProblemChapter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(trashChapterEClass, TrashChapter.class, "TrashChapter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(requirementEClass, Requirement.class, "Requirement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRequirement_Attribute(), this.getAttribute(), null, "attribute", null, 0, -1, Requirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
                !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRequirement_ExternalResources(), theEcorePackage.getEString(), "externalResources", null, 0, -1, Requirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(anonymousRequirementEClass, AnonymousRequirement.class, "AnonymousRequirement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        // Initialize enums and add enum literals
        initEEnum(attributesTypeEEnum, AttributesType.class, "AttributesType");
        addEEnumLiteral(attributesTypeEEnum, AttributesType.TEXT);
        addEEnumLiteral(attributesTypeEEnum, AttributesType.OBJECT);
        addEEnumLiteral(attributesTypeEEnum, AttributesType.ALLOCATE);
        addEEnumLiteral(attributesTypeEEnum, AttributesType.LINK);

        // Create resource
        createResource(eNS_URI);
    }

} //RequirementPackageImpl
