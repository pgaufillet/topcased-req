/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import ttm.TtmPackage;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.topcased.requirement.RequirementFactory
 * @model kind="package"
 * @generated
 */
public interface RequirementPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "requirement";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://org.topcased.requirement/1.0";

    /**
     * The package namespace name.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "requirement";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    RequirementPackage eINSTANCE = org.topcased.requirement.impl.RequirementPackageImpl.init();

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.IdentifiedElementImpl <em>Identified Element</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.IdentifiedElementImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getIdentifiedElement()
     * @generated
     */
    int IDENTIFIED_ELEMENT = 8;

    /**
     * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFIED_ELEMENT__EANNOTATIONS = EcorePackage.EMODEL_ELEMENT__EANNOTATIONS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFIED_ELEMENT__IDENTIFIER = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Short Description</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int IDENTIFIED_ELEMENT__SHORT_DESCRIPTION = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Identified Element</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFIED_ELEMENT_FEATURE_COUNT = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.RequirementProjectImpl <em>Project</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.RequirementProjectImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getRequirementProject()
     * @generated
     */
    int REQUIREMENT_PROJECT = 0;

    /**
     * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT_PROJECT__EANNOTATIONS = IDENTIFIED_ELEMENT__EANNOTATIONS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT_PROJECT__IDENTIFIER = IDENTIFIED_ELEMENT__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Short Description</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int REQUIREMENT_PROJECT__SHORT_DESCRIPTION = IDENTIFIED_ELEMENT__SHORT_DESCRIPTION;

    /**
     * The feature id for the '<em><b>Hierarchical Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT = IDENTIFIED_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Attribute Configuration</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION = IDENTIFIED_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Chapter</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT_PROJECT__CHAPTER = IDENTIFIED_ELEMENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Upstream Model</b></em>' containment reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT_PROJECT__UPSTREAM_MODEL = IDENTIFIED_ELEMENT_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Project</em>' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT_PROJECT_FEATURE_COUNT = IDENTIFIED_ELEMENT_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.HierarchicalElementImpl <em>Hierarchical Element</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.HierarchicalElementImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getHierarchicalElement()
     * @generated
     */
    int HIERARCHICAL_ELEMENT = 1;

    /**
     * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int HIERARCHICAL_ELEMENT__EANNOTATIONS = IDENTIFIED_ELEMENT__EANNOTATIONS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HIERARCHICAL_ELEMENT__IDENTIFIER = IDENTIFIED_ELEMENT__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Short Description</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int HIERARCHICAL_ELEMENT__SHORT_DESCRIPTION = IDENTIFIED_ELEMENT__SHORT_DESCRIPTION;

    /**
     * The feature id for the '<em><b>Element</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HIERARCHICAL_ELEMENT__ELEMENT = IDENTIFIED_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Children</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int HIERARCHICAL_ELEMENT__CHILDREN = IDENTIFIED_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Parent</b></em>' container reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int HIERARCHICAL_ELEMENT__PARENT = IDENTIFIED_ELEMENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Requirement</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int HIERARCHICAL_ELEMENT__REQUIREMENT = IDENTIFIED_ELEMENT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Next Req Index</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HIERARCHICAL_ELEMENT__NEXT_REQ_INDEX = IDENTIFIED_ELEMENT_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Hierarchical Element</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int HIERARCHICAL_ELEMENT_FEATURE_COUNT = IDENTIFIED_ELEMENT_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.RequirementImpl <em>Requirement</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.RequirementImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getRequirement()
     * @generated
     */
    int REQUIREMENT = 18;

    /**
     * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT__EANNOTATIONS = IDENTIFIED_ELEMENT__EANNOTATIONS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT__IDENTIFIER = IDENTIFIED_ELEMENT__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Short Description</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int REQUIREMENT__SHORT_DESCRIPTION = IDENTIFIED_ELEMENT__SHORT_DESCRIPTION;

    /**
     * The feature id for the '<em><b>Attribute</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT__ATTRIBUTE = IDENTIFIED_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>External Resources</b></em>' attribute list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUIREMENT__EXTERNAL_RESOURCES = IDENTIFIED_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Requirement</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int REQUIREMENT_FEATURE_COUNT = IDENTIFIED_ELEMENT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.CurrentRequirementImpl <em>Current Requirement</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.CurrentRequirementImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getCurrentRequirement()
     * @generated
     */
    int CURRENT_REQUIREMENT = 2;

    /**
     * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int CURRENT_REQUIREMENT__EANNOTATIONS = REQUIREMENT__EANNOTATIONS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CURRENT_REQUIREMENT__IDENTIFIER = REQUIREMENT__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Short Description</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int CURRENT_REQUIREMENT__SHORT_DESCRIPTION = REQUIREMENT__SHORT_DESCRIPTION;

    /**
     * The feature id for the '<em><b>Attribute</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int CURRENT_REQUIREMENT__ATTRIBUTE = REQUIREMENT__ATTRIBUTE;

    /**
     * The feature id for the '<em><b>External Resources</b></em>' attribute list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int CURRENT_REQUIREMENT__EXTERNAL_RESOURCES = REQUIREMENT__EXTERNAL_RESOURCES;

    /**
     * The feature id for the '<em><b>Impacted</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CURRENT_REQUIREMENT__IMPACTED = REQUIREMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Current Requirement</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int CURRENT_REQUIREMENT_FEATURE_COUNT = REQUIREMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.AttributeImpl <em>Attribute</em>}' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.topcased.requirement.impl.AttributeImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getAttribute()
     * @generated
     */
    int ATTRIBUTE = 3;

    /**
     * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE__EANNOTATIONS = EcorePackage.EMODEL_ELEMENT__EANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE__NAME = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Attribute</em>' class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int ATTRIBUTE_FEATURE_COUNT = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.AttributeConfigurationImpl <em>Attribute Configuration</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.AttributeConfigurationImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getAttributeConfiguration()
     * @generated
     */
    int ATTRIBUTE_CONFIGURATION = 4;

    /**
     * The feature id for the '<em><b>List Attributes</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE_CONFIGURATION__LIST_ATTRIBUTES = 0;

    /**
     * The number of structural features of the '<em>Attribute Configuration</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE_CONFIGURATION_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.ConfiguratedAttributeImpl <em>Configurated Attribute</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.ConfiguratedAttributeImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getConfiguratedAttribute()
     * @generated
     */
    int CONFIGURATED_ATTRIBUTE = 5;

    /**
     * The feature id for the '<em><b>Default Value</b></em>' containment reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE = 0;

    /**
     * The feature id for the '<em><b>List Value</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int CONFIGURATED_ATTRIBUTE__LIST_VALUE = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONFIGURATED_ATTRIBUTE__NAME = 2;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONFIGURATED_ATTRIBUTE__TYPE = 3;

    /**
     * The number of structural features of the '<em>Configurated Attribute</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int CONFIGURATED_ATTRIBUTE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.AttributeValueImpl <em>Attribute Value</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.AttributeValueImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getAttributeValue()
     * @generated
     */
    int ATTRIBUTE_VALUE = 6;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE_VALUE__VALUE = 0;

    /**
     * The number of structural features of the '<em>Attribute Value</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE_VALUE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.DefaultAttributeValueImpl <em>Default Attribute Value</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.DefaultAttributeValueImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getDefaultAttributeValue()
     * @generated
     */
    int DEFAULT_ATTRIBUTE_VALUE = 7;

    /**
     * The feature id for the '<em><b>Value</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEFAULT_ATTRIBUTE_VALUE__VALUE = 0;

    /**
     * The number of structural features of the '<em>Default Attribute Value</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int DEFAULT_ATTRIBUTE_VALUE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.SpecialChapterImpl <em>Special Chapter</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.SpecialChapterImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getSpecialChapter()
     * @generated
     */
    int SPECIAL_CHAPTER = 9;

    /**
     * The feature id for the '<em><b>Hierarchical Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIAL_CHAPTER__HIERARCHICAL_ELEMENT = 0;

    /**
     * The feature id for the '<em><b>Requirement</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIAL_CHAPTER__REQUIREMENT = 1;

    /**
     * The number of structural features of the '<em>Special Chapter</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIAL_CHAPTER_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.TextAttributeImpl <em>Text Attribute</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.TextAttributeImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getTextAttribute()
     * @generated
     */
    int TEXT_ATTRIBUTE = 10;

    /**
     * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_ATTRIBUTE__EANNOTATIONS = ATTRIBUTE__EANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_ATTRIBUTE__NAME = ATTRIBUTE__NAME;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_ATTRIBUTE__VALUE = ATTRIBUTE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Text Attribute</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_ATTRIBUTE_FEATURE_COUNT = ATTRIBUTE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.ObjectAttributeImpl <em>Object Attribute</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.ObjectAttributeImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getObjectAttribute()
     * @generated
     */
    int OBJECT_ATTRIBUTE = 11;

    /**
     * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int OBJECT_ATTRIBUTE__EANNOTATIONS = ATTRIBUTE__EANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OBJECT_ATTRIBUTE__NAME = ATTRIBUTE__NAME;

    /**
     * The feature id for the '<em><b>Value</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OBJECT_ATTRIBUTE__VALUE = ATTRIBUTE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Object Attribute</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int OBJECT_ATTRIBUTE_FEATURE_COUNT = ATTRIBUTE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.UpstreamModelImpl <em>Upstream Model</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.UpstreamModelImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getUpstreamModel()
     * @generated
     */
    int UPSTREAM_MODEL = 12;

    /**
     * The feature id for the '<em><b>Activated Rules</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int UPSTREAM_MODEL__ACTIVATED_RULES = TtmPackage.PROJECT__ACTIVATED_RULES;

    /**
     * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int UPSTREAM_MODEL__ATTRIBUTES = TtmPackage.PROJECT__ATTRIBUTES;

    /**
     * The feature id for the '<em><b>Ident</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPSTREAM_MODEL__IDENT = TtmPackage.PROJECT__IDENT;

    /**
     * The feature id for the '<em><b>Short Description</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int UPSTREAM_MODEL__SHORT_DESCRIPTION = TtmPackage.PROJECT__SHORT_DESCRIPTION;

    /**
     * The feature id for the '<em><b>Texts</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int UPSTREAM_MODEL__TEXTS = TtmPackage.PROJECT__TEXTS;

    /**
     * The feature id for the '<em><b>Documents</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int UPSTREAM_MODEL__DOCUMENTS = TtmPackage.PROJECT__DOCUMENTS;

    /**
     * The feature id for the '<em><b>Rules</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int UPSTREAM_MODEL__RULES = TtmPackage.PROJECT__RULES;

    /**
     * The feature id for the '<em><b>Coverage Results</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPSTREAM_MODEL__COVERAGE_RESULTS = TtmPackage.PROJECT__COVERAGE_RESULTS;

    /**
     * The feature id for the '<em><b>Any Document Covered</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int UPSTREAM_MODEL__ANY_DOCUMENT_COVERED = TtmPackage.PROJECT__ANY_DOCUMENT_COVERED;

    /**
     * The number of structural features of the '<em>Upstream Model</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int UPSTREAM_MODEL_FEATURE_COUNT = TtmPackage.PROJECT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.AttributeLinkImpl <em>Attribute Link</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.AttributeLinkImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getAttributeLink()
     * @generated
     */
    int ATTRIBUTE_LINK = 13;

    /**
     * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE_LINK__EANNOTATIONS = OBJECT_ATTRIBUTE__EANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE_LINK__NAME = OBJECT_ATTRIBUTE__NAME;

    /**
     * The feature id for the '<em><b>Value</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE_LINK__VALUE = OBJECT_ATTRIBUTE__VALUE;

    /**
     * The feature id for the '<em><b>Partial</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE_LINK__PARTIAL = OBJECT_ATTRIBUTE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Attribute Link</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE_LINK_FEATURE_COUNT = OBJECT_ATTRIBUTE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.AttributeAllocateImpl <em>Attribute Allocate</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.AttributeAllocateImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getAttributeAllocate()
     * @generated
     */
    int ATTRIBUTE_ALLOCATE = 14;

    /**
     * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE_ALLOCATE__EANNOTATIONS = OBJECT_ATTRIBUTE__EANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE_ALLOCATE__NAME = OBJECT_ATTRIBUTE__NAME;

    /**
     * The feature id for the '<em><b>Value</b></em>' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE_ALLOCATE__VALUE = OBJECT_ATTRIBUTE__VALUE;

    /**
     * The number of structural features of the '<em>Attribute Allocate</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ATTRIBUTE_ALLOCATE_FEATURE_COUNT = OBJECT_ATTRIBUTE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.UntracedChapterImpl <em>Untraced Chapter</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.UntracedChapterImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getUntracedChapter()
     * @generated
     */
    int UNTRACED_CHAPTER = 15;

    /**
     * The feature id for the '<em><b>Hierarchical Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNTRACED_CHAPTER__HIERARCHICAL_ELEMENT = SPECIAL_CHAPTER__HIERARCHICAL_ELEMENT;

    /**
     * The feature id for the '<em><b>Requirement</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int UNTRACED_CHAPTER__REQUIREMENT = SPECIAL_CHAPTER__REQUIREMENT;

    /**
     * The number of structural features of the '<em>Untraced Chapter</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int UNTRACED_CHAPTER_FEATURE_COUNT = SPECIAL_CHAPTER_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.ProblemChapterImpl <em>Problem Chapter</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.ProblemChapterImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getProblemChapter()
     * @generated
     */
    int PROBLEM_CHAPTER = 16;

    /**
     * The feature id for the '<em><b>Hierarchical Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROBLEM_CHAPTER__HIERARCHICAL_ELEMENT = SPECIAL_CHAPTER__HIERARCHICAL_ELEMENT;

    /**
     * The feature id for the '<em><b>Requirement</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int PROBLEM_CHAPTER__REQUIREMENT = SPECIAL_CHAPTER__REQUIREMENT;

    /**
     * The number of structural features of the '<em>Problem Chapter</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int PROBLEM_CHAPTER_FEATURE_COUNT = SPECIAL_CHAPTER_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.TrashChapterImpl <em>Trash Chapter</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.TrashChapterImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getTrashChapter()
     * @generated
     */
    int TRASH_CHAPTER = 17;

    /**
     * The feature id for the '<em><b>Hierarchical Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRASH_CHAPTER__HIERARCHICAL_ELEMENT = SPECIAL_CHAPTER__HIERARCHICAL_ELEMENT;

    /**
     * The feature id for the '<em><b>Requirement</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TRASH_CHAPTER__REQUIREMENT = SPECIAL_CHAPTER__REQUIREMENT;

    /**
     * The number of structural features of the '<em>Trash Chapter</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int TRASH_CHAPTER_FEATURE_COUNT = SPECIAL_CHAPTER_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.topcased.requirement.impl.AnonymousRequirementImpl <em>Anonymous Requirement</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.topcased.requirement.impl.AnonymousRequirementImpl
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getAnonymousRequirement()
     * @generated
     */
    int ANONYMOUS_REQUIREMENT = 19;

    /**
     * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ANONYMOUS_REQUIREMENT__EANNOTATIONS = REQUIREMENT__EANNOTATIONS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANONYMOUS_REQUIREMENT__IDENTIFIER = REQUIREMENT__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Short Description</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     * @ordered
     */
    int ANONYMOUS_REQUIREMENT__SHORT_DESCRIPTION = REQUIREMENT__SHORT_DESCRIPTION;

    /**
     * The feature id for the '<em><b>Attribute</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ANONYMOUS_REQUIREMENT__ATTRIBUTE = REQUIREMENT__ATTRIBUTE;

    /**
     * The feature id for the '<em><b>External Resources</b></em>' attribute list.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ANONYMOUS_REQUIREMENT__EXTERNAL_RESOURCES = REQUIREMENT__EXTERNAL_RESOURCES;

    /**
     * The number of structural features of the '<em>Anonymous Requirement</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ANONYMOUS_REQUIREMENT_FEATURE_COUNT = REQUIREMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.topcased.requirement.AttributesType <em>Attributes Type</em>}' enum. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.topcased.requirement.AttributesType
     * @see org.topcased.requirement.impl.RequirementPackageImpl#getAttributesType()
     * @generated
     */
    int ATTRIBUTES_TYPE = 20;

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.RequirementProject <em>Project</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Project</em>'.
     * @see org.topcased.requirement.RequirementProject
     * @generated
     */
    EClass getRequirementProject();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.topcased.requirement.RequirementProject#getHierarchicalElement <em>Hierarchical Element</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Hierarchical Element</em>'.
     * @see org.topcased.requirement.RequirementProject#getHierarchicalElement()
     * @see #getRequirementProject()
     * @generated
     */
    EReference getRequirementProject_HierarchicalElement();

    /**
     * Returns the meta object for the containment reference '{@link org.topcased.requirement.RequirementProject#getAttributeConfiguration <em>Attribute Configuration</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Attribute Configuration</em>'.
     * @see org.topcased.requirement.RequirementProject#getAttributeConfiguration()
     * @see #getRequirementProject()
     * @generated
     */
    EReference getRequirementProject_AttributeConfiguration();

    /**
     * Returns the meta object for the containment reference list '{@link org.topcased.requirement.RequirementProject#getChapter <em>Chapter</em>}'.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @return the meta object for the containment reference list '<em>Chapter</em>'.
     * @see org.topcased.requirement.RequirementProject#getChapter()
     * @see #getRequirementProject()
     * @generated
     */
    EReference getRequirementProject_Chapter();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.topcased.requirement.RequirementProject#getUpstreamModel <em>Upstream Model</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Upstream Model</em>'.
     * @see org.topcased.requirement.RequirementProject#getUpstreamModel()
     * @see #getRequirementProject()
     * @generated
     */
    EReference getRequirementProject_UpstreamModel();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.HierarchicalElement <em>Hierarchical Element</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Hierarchical Element</em>'.
     * @see org.topcased.requirement.HierarchicalElement
     * @generated
     */
    EClass getHierarchicalElement();

    /**
     * Returns the meta object for the reference '{@link org.topcased.requirement.HierarchicalElement#getElement <em>Element</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Element</em>'.
     * @see org.topcased.requirement.HierarchicalElement#getElement()
     * @see #getHierarchicalElement()
     * @generated
     */
    EReference getHierarchicalElement_Element();

    /**
     * Returns the meta object for the containment reference list '{@link org.topcased.requirement.HierarchicalElement#getChildren <em>Children</em>}'.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @return the meta object for the containment reference list '<em>Children</em>'.
     * @see org.topcased.requirement.HierarchicalElement#getChildren()
     * @see #getHierarchicalElement()
     * @generated
     */
    EReference getHierarchicalElement_Children();

    /**
     * Returns the meta object for the container reference '{@link org.topcased.requirement.HierarchicalElement#getParent <em>Parent</em>}'.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @return the meta object for the container reference '<em>Parent</em>'.
     * @see org.topcased.requirement.HierarchicalElement#getParent()
     * @see #getHierarchicalElement()
     * @generated
     */
    EReference getHierarchicalElement_Parent();

    /**
     * Returns the meta object for the containment reference list '{@link org.topcased.requirement.HierarchicalElement#getRequirement <em>Requirement</em>}'.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Requirement</em>'.
     * @see org.topcased.requirement.HierarchicalElement#getRequirement()
     * @see #getHierarchicalElement()
     * @generated
     */
    EReference getHierarchicalElement_Requirement();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.requirement.HierarchicalElement#getNextReqIndex <em>Next Req Index</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Next Req Index</em>'.
     * @see org.topcased.requirement.HierarchicalElement#getNextReqIndex()
     * @see #getHierarchicalElement()
     * @generated
     */
    EAttribute getHierarchicalElement_NextReqIndex();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.CurrentRequirement <em>Current Requirement</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Current Requirement</em>'.
     * @see org.topcased.requirement.CurrentRequirement
     * @generated
     */
    EClass getCurrentRequirement();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.requirement.CurrentRequirement#isImpacted <em>Impacted</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Impacted</em>'.
     * @see org.topcased.requirement.CurrentRequirement#isImpacted()
     * @see #getCurrentRequirement()
     * @generated
     */
    EAttribute getCurrentRequirement_Impacted();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.Attribute <em>Attribute</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Attribute</em>'.
     * @see org.topcased.requirement.Attribute
     * @generated
     */
    EClass getAttribute();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.requirement.Attribute#getName <em>Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.topcased.requirement.Attribute#getName()
     * @see #getAttribute()
     * @generated
     */
    EAttribute getAttribute_Name();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.AttributeConfiguration <em>Attribute Configuration</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Attribute Configuration</em>'.
     * @see org.topcased.requirement.AttributeConfiguration
     * @generated
     */
    EClass getAttributeConfiguration();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.topcased.requirement.AttributeConfiguration#getListAttributes <em>List Attributes</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>List Attributes</em>'.
     * @see org.topcased.requirement.AttributeConfiguration#getListAttributes()
     * @see #getAttributeConfiguration()
     * @generated
     */
    EReference getAttributeConfiguration_ListAttributes();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.ConfiguratedAttribute <em>Configurated Attribute</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Configurated Attribute</em>'.
     * @see org.topcased.requirement.ConfiguratedAttribute
     * @generated
     */
    EClass getConfiguratedAttribute();

    /**
     * Returns the meta object for the containment reference '
     * {@link org.topcased.requirement.ConfiguratedAttribute#getDefaultValue <em>Default Value</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference '<em>Default Value</em>'.
     * @see org.topcased.requirement.ConfiguratedAttribute#getDefaultValue()
     * @see #getConfiguratedAttribute()
     * @generated
     */
    EReference getConfiguratedAttribute_DefaultValue();

    /**
     * Returns the meta object for the containment reference list '{@link org.topcased.requirement.ConfiguratedAttribute#getListValue <em>List Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>List Value</em>'.
     * @see org.topcased.requirement.ConfiguratedAttribute#getListValue()
     * @see #getConfiguratedAttribute()
     * @generated
     */
    EReference getConfiguratedAttribute_ListValue();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.requirement.ConfiguratedAttribute#getName <em>Name</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.topcased.requirement.ConfiguratedAttribute#getName()
     * @see #getConfiguratedAttribute()
     * @generated
     */
    EAttribute getConfiguratedAttribute_Name();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.requirement.ConfiguratedAttribute#getType <em>Type</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see org.topcased.requirement.ConfiguratedAttribute#getType()
     * @see #getConfiguratedAttribute()
     * @generated
     */
    EAttribute getConfiguratedAttribute_Type();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.AttributeValue <em>Attribute Value</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Attribute Value</em>'.
     * @see org.topcased.requirement.AttributeValue
     * @generated
     */
    EClass getAttributeValue();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.requirement.AttributeValue#getValue <em>Value</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.topcased.requirement.AttributeValue#getValue()
     * @see #getAttributeValue()
     * @generated
     */
    EAttribute getAttributeValue_Value();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.DefaultAttributeValue <em>Default Attribute Value</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Default Attribute Value</em>'.
     * @see org.topcased.requirement.DefaultAttributeValue
     * @generated
     */
    EClass getDefaultAttributeValue();

    /**
     * Returns the meta object for the reference '{@link org.topcased.requirement.DefaultAttributeValue#getValue <em>Value</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Value</em>'.
     * @see org.topcased.requirement.DefaultAttributeValue#getValue()
     * @see #getDefaultAttributeValue()
     * @generated
     */
    EReference getDefaultAttributeValue_Value();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.IdentifiedElement <em>Identified Element</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Identified Element</em>'.
     * @see org.topcased.requirement.IdentifiedElement
     * @generated
     */
    EClass getIdentifiedElement();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.requirement.IdentifiedElement#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Identifier</em>'.
     * @see org.topcased.requirement.IdentifiedElement#getIdentifier()
     * @see #getIdentifiedElement()
     * @generated
     */
    EAttribute getIdentifiedElement_Identifier();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.requirement.IdentifiedElement#getShortDescription <em>Short Description</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Short Description</em>'.
     * @see org.topcased.requirement.IdentifiedElement#getShortDescription()
     * @see #getIdentifiedElement()
     * @generated
     */
    EAttribute getIdentifiedElement_ShortDescription();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.SpecialChapter <em>Special Chapter</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Special Chapter</em>'.
     * @see org.topcased.requirement.SpecialChapter
     * @generated
     */
    EClass getSpecialChapter();

    /**
     * Returns the meta object for the containment reference list '
     * {@link org.topcased.requirement.SpecialChapter#getHierarchicalElement <em>Hierarchical Element</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Hierarchical Element</em>'.
     * @see org.topcased.requirement.SpecialChapter#getHierarchicalElement()
     * @see #getSpecialChapter()
     * @generated
     */
    EReference getSpecialChapter_HierarchicalElement();

    /**
     * Returns the meta object for the containment reference list '{@link org.topcased.requirement.SpecialChapter#getRequirement <em>Requirement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Requirement</em>'.
     * @see org.topcased.requirement.SpecialChapter#getRequirement()
     * @see #getSpecialChapter()
     * @generated
     */
    EReference getSpecialChapter_Requirement();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.TextAttribute <em>Text Attribute</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Text Attribute</em>'.
     * @see org.topcased.requirement.TextAttribute
     * @generated
     */
    EClass getTextAttribute();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.requirement.TextAttribute#getValue <em>Value</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.topcased.requirement.TextAttribute#getValue()
     * @see #getTextAttribute()
     * @generated
     */
    EAttribute getTextAttribute_Value();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.ObjectAttribute <em>Object Attribute</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Object Attribute</em>'.
     * @see org.topcased.requirement.ObjectAttribute
     * @generated
     */
    EClass getObjectAttribute();

    /**
     * Returns the meta object for the reference '{@link org.topcased.requirement.ObjectAttribute#getValue <em>Value</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Value</em>'.
     * @see org.topcased.requirement.ObjectAttribute#getValue()
     * @see #getObjectAttribute()
     * @generated
     */
    EReference getObjectAttribute_Value();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.UpstreamModel <em>Upstream Model</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Upstream Model</em>'.
     * @see org.topcased.requirement.UpstreamModel
     * @generated
     */
    EClass getUpstreamModel();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.AttributeLink <em>Attribute Link</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Attribute Link</em>'.
     * @see org.topcased.requirement.AttributeLink
     * @generated
     */
    EClass getAttributeLink();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.requirement.AttributeLink#getPartial <em>Partial</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Partial</em>'.
     * @see org.topcased.requirement.AttributeLink#getPartial()
     * @see #getAttributeLink()
     * @generated
     */
    EAttribute getAttributeLink_Partial();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.AttributeAllocate <em>Attribute Allocate</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Attribute Allocate</em>'.
     * @see org.topcased.requirement.AttributeAllocate
     * @generated
     */
    EClass getAttributeAllocate();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.UntracedChapter <em>Untraced Chapter</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Untraced Chapter</em>'.
     * @see org.topcased.requirement.UntracedChapter
     * @generated
     */
    EClass getUntracedChapter();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.ProblemChapter <em>Problem Chapter</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Problem Chapter</em>'.
     * @see org.topcased.requirement.ProblemChapter
     * @generated
     */
    EClass getProblemChapter();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.TrashChapter <em>Trash Chapter</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Trash Chapter</em>'.
     * @see org.topcased.requirement.TrashChapter
     * @generated
     */
    EClass getTrashChapter();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.Requirement <em>Requirement</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Requirement</em>'.
     * @see org.topcased.requirement.Requirement
     * @generated
     */
    EClass getRequirement();

    /**
     * Returns the meta object for the containment reference list '{@link org.topcased.requirement.Requirement#getAttribute <em>Attribute</em>}'.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @return the meta object for the containment reference list '<em>Attribute</em>'.
     * @see org.topcased.requirement.Requirement#getAttribute()
     * @see #getRequirement()
     * @generated
     */
    EReference getRequirement_Attribute();

    /**
     * Returns the meta object for the attribute list '{@link org.topcased.requirement.Requirement#getExternalResources <em>External Resources</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>External Resources</em>'.
     * @see org.topcased.requirement.Requirement#getExternalResources()
     * @see #getRequirement()
     * @generated
     */
    EAttribute getRequirement_ExternalResources();

    /**
     * Returns the meta object for class '{@link org.topcased.requirement.AnonymousRequirement <em>Anonymous Requirement</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Anonymous Requirement</em>'.
     * @see org.topcased.requirement.AnonymousRequirement
     * @generated
     */
    EClass getAnonymousRequirement();

    /**
     * Returns the meta object for enum '{@link org.topcased.requirement.AttributesType <em>Attributes Type</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for enum '<em>Attributes Type</em>'.
     * @see org.topcased.requirement.AttributesType
     * @generated
     */
    EEnum getAttributesType();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    RequirementFactory getRequirementFactory();

    /**
     * <!-- begin-user-doc --> Defines literals for the meta objects that represent
     * <ul>
     * <li>each class,</li>
     * <li>each feature of each class,</li>
     * <li>each enum,</li>
     * <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals
    {
        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.RequirementProjectImpl <em>Project</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.RequirementProjectImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getRequirementProject()
         * @generated
         */
        EClass REQUIREMENT_PROJECT = eINSTANCE.getRequirementProject();

        /**
         * The meta object literal for the '<em><b>Hierarchical Element</b></em>' containment reference list feature.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @generated
         */
        EReference REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT = eINSTANCE.getRequirementProject_HierarchicalElement();

        /**
         * The meta object literal for the '<em><b>Attribute Configuration</b></em>' containment reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION = eINSTANCE.getRequirementProject_AttributeConfiguration();

        /**
         * The meta object literal for the '<em><b>Chapter</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference REQUIREMENT_PROJECT__CHAPTER = eINSTANCE.getRequirementProject_Chapter();

        /**
         * The meta object literal for the '<em><b>Upstream Model</b></em>' containment reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference REQUIREMENT_PROJECT__UPSTREAM_MODEL = eINSTANCE.getRequirementProject_UpstreamModel();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.HierarchicalElementImpl <em>Hierarchical Element</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.HierarchicalElementImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getHierarchicalElement()
         * @generated
         */
        EClass HIERARCHICAL_ELEMENT = eINSTANCE.getHierarchicalElement();

        /**
         * The meta object literal for the '<em><b>Element</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference HIERARCHICAL_ELEMENT__ELEMENT = eINSTANCE.getHierarchicalElement_Element();

        /**
         * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference HIERARCHICAL_ELEMENT__CHILDREN = eINSTANCE.getHierarchicalElement_Children();

        /**
         * The meta object literal for the '<em><b>Parent</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference HIERARCHICAL_ELEMENT__PARENT = eINSTANCE.getHierarchicalElement_Parent();

        /**
         * The meta object literal for the '<em><b>Requirement</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference HIERARCHICAL_ELEMENT__REQUIREMENT = eINSTANCE.getHierarchicalElement_Requirement();

        /**
         * The meta object literal for the '<em><b>Next Req Index</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute HIERARCHICAL_ELEMENT__NEXT_REQ_INDEX = eINSTANCE.getHierarchicalElement_NextReqIndex();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.CurrentRequirementImpl <em>Current Requirement</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.CurrentRequirementImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getCurrentRequirement()
         * @generated
         */
        EClass CURRENT_REQUIREMENT = eINSTANCE.getCurrentRequirement();

        /**
         * The meta object literal for the '<em><b>Impacted</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute CURRENT_REQUIREMENT__IMPACTED = eINSTANCE.getCurrentRequirement_Impacted();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.AttributeImpl <em>Attribute</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.AttributeImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getAttribute()
         * @generated
         */
        EClass ATTRIBUTE = eINSTANCE.getAttribute();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute ATTRIBUTE__NAME = eINSTANCE.getAttribute_Name();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.AttributeConfigurationImpl <em>Attribute Configuration</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.AttributeConfigurationImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getAttributeConfiguration()
         * @generated
         */
        EClass ATTRIBUTE_CONFIGURATION = eINSTANCE.getAttributeConfiguration();

        /**
         * The meta object literal for the '<em><b>List Attributes</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference ATTRIBUTE_CONFIGURATION__LIST_ATTRIBUTES = eINSTANCE.getAttributeConfiguration_ListAttributes();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.ConfiguratedAttributeImpl <em>Configurated Attribute</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.ConfiguratedAttributeImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getConfiguratedAttribute()
         * @generated
         */
        EClass CONFIGURATED_ATTRIBUTE = eINSTANCE.getConfiguratedAttribute();

        /**
         * The meta object literal for the '<em><b>Default Value</b></em>' containment reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE = eINSTANCE.getConfiguratedAttribute_DefaultValue();

        /**
         * The meta object literal for the '<em><b>List Value</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference CONFIGURATED_ATTRIBUTE__LIST_VALUE = eINSTANCE.getConfiguratedAttribute_ListValue();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute CONFIGURATED_ATTRIBUTE__NAME = eINSTANCE.getConfiguratedAttribute_Name();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute CONFIGURATED_ATTRIBUTE__TYPE = eINSTANCE.getConfiguratedAttribute_Type();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.AttributeValueImpl <em>Attribute Value</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.AttributeValueImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getAttributeValue()
         * @generated
         */
        EClass ATTRIBUTE_VALUE = eINSTANCE.getAttributeValue();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute ATTRIBUTE_VALUE__VALUE = eINSTANCE.getAttributeValue_Value();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.DefaultAttributeValueImpl <em>Default Attribute Value</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.DefaultAttributeValueImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getDefaultAttributeValue()
         * @generated
         */
        EClass DEFAULT_ATTRIBUTE_VALUE = eINSTANCE.getDefaultAttributeValue();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference DEFAULT_ATTRIBUTE_VALUE__VALUE = eINSTANCE.getDefaultAttributeValue_Value();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.IdentifiedElementImpl <em>Identified Element</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.IdentifiedElementImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getIdentifiedElement()
         * @generated
         */
        EClass IDENTIFIED_ELEMENT = eINSTANCE.getIdentifiedElement();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute IDENTIFIED_ELEMENT__IDENTIFIER = eINSTANCE.getIdentifiedElement_Identifier();

        /**
         * The meta object literal for the '<em><b>Short Description</b></em>' attribute feature.
         * <!-- begin-user-doc
         * --> <!-- end-user-doc -->
         * @generated
         */
        EAttribute IDENTIFIED_ELEMENT__SHORT_DESCRIPTION = eINSTANCE.getIdentifiedElement_ShortDescription();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.SpecialChapterImpl <em>Special Chapter</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.SpecialChapterImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getSpecialChapter()
         * @generated
         */
        EClass SPECIAL_CHAPTER = eINSTANCE.getSpecialChapter();

        /**
         * The meta object literal for the '<em><b>Hierarchical Element</b></em>' containment reference list feature.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @generated
         */
        EReference SPECIAL_CHAPTER__HIERARCHICAL_ELEMENT = eINSTANCE.getSpecialChapter_HierarchicalElement();

        /**
         * The meta object literal for the '<em><b>Requirement</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference SPECIAL_CHAPTER__REQUIREMENT = eINSTANCE.getSpecialChapter_Requirement();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.TextAttributeImpl <em>Text Attribute</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.TextAttributeImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getTextAttribute()
         * @generated
         */
        EClass TEXT_ATTRIBUTE = eINSTANCE.getTextAttribute();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute TEXT_ATTRIBUTE__VALUE = eINSTANCE.getTextAttribute_Value();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.ObjectAttributeImpl <em>Object Attribute</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.ObjectAttributeImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getObjectAttribute()
         * @generated
         */
        EClass OBJECT_ATTRIBUTE = eINSTANCE.getObjectAttribute();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' reference feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EReference OBJECT_ATTRIBUTE__VALUE = eINSTANCE.getObjectAttribute_Value();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.UpstreamModelImpl <em>Upstream Model</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.UpstreamModelImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getUpstreamModel()
         * @generated
         */
        EClass UPSTREAM_MODEL = eINSTANCE.getUpstreamModel();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.AttributeLinkImpl <em>Attribute Link</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.AttributeLinkImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getAttributeLink()
         * @generated
         */
        EClass ATTRIBUTE_LINK = eINSTANCE.getAttributeLink();

        /**
         * The meta object literal for the '<em><b>Partial</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * @generated
         */
        EAttribute ATTRIBUTE_LINK__PARTIAL = eINSTANCE.getAttributeLink_Partial();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.AttributeAllocateImpl <em>Attribute Allocate</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.AttributeAllocateImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getAttributeAllocate()
         * @generated
         */
        EClass ATTRIBUTE_ALLOCATE = eINSTANCE.getAttributeAllocate();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.UntracedChapterImpl <em>Untraced Chapter</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.UntracedChapterImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getUntracedChapter()
         * @generated
         */
        EClass UNTRACED_CHAPTER = eINSTANCE.getUntracedChapter();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.ProblemChapterImpl <em>Problem Chapter</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.ProblemChapterImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getProblemChapter()
         * @generated
         */
        EClass PROBLEM_CHAPTER = eINSTANCE.getProblemChapter();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.TrashChapterImpl <em>Trash Chapter</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.TrashChapterImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getTrashChapter()
         * @generated
         */
        EClass TRASH_CHAPTER = eINSTANCE.getTrashChapter();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.RequirementImpl <em>Requirement</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.RequirementImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getRequirement()
         * @generated
         */
        EClass REQUIREMENT = eINSTANCE.getRequirement();

        /**
         * The meta object literal for the '<em><b>Attribute</b></em>' containment reference list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference REQUIREMENT__ATTRIBUTE = eINSTANCE.getRequirement_Attribute();

        /**
         * The meta object literal for the '<em><b>External Resources</b></em>' attribute list feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute REQUIREMENT__EXTERNAL_RESOURCES = eINSTANCE.getRequirement_ExternalResources();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.impl.AnonymousRequirementImpl <em>Anonymous Requirement</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.impl.AnonymousRequirementImpl
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getAnonymousRequirement()
         * @generated
         */
        EClass ANONYMOUS_REQUIREMENT = eINSTANCE.getAnonymousRequirement();

        /**
         * The meta object literal for the '{@link org.topcased.requirement.AttributesType <em>Attributes Type</em>}' enum.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see org.topcased.requirement.AttributesType
         * @see org.topcased.requirement.impl.RequirementPackageImpl#getAttributesType()
         * @generated
         */
        EEnum ATTRIBUTES_TYPE = eINSTANCE.getAttributesType();

    }

} // RequirementPackage
