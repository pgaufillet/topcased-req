/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.topcased.typesmodel.model.inittypes.InittypesFactory
 * @model kind="package"
 * @generated
 */
public interface InittypesPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "inittypes";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://inittypes/1.0";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "inittypes";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    InittypesPackage eINSTANCE = org.topcased.typesmodel.model.inittypes.impl.InittypesPackageImpl.init();

    /**
     * The meta object id for the '{@link org.topcased.typesmodel.model.inittypes.impl.TypeModelImpl <em>Type Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.topcased.typesmodel.model.inittypes.impl.TypeModelImpl
     * @see org.topcased.typesmodel.model.inittypes.impl.InittypesPackageImpl#getTypeModel()
     * @generated
     */
    int TYPE_MODEL = 0;

    /**
     * The feature id for the '<em><b>Document Types</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_MODEL__DOCUMENT_TYPES = 0;

    /**
     * The number of structural features of the '<em>Type Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_MODEL_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.topcased.typesmodel.model.inittypes.impl.TypeImpl <em>Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.topcased.typesmodel.model.inittypes.impl.TypeImpl
     * @see org.topcased.typesmodel.model.inittypes.impl.InittypesPackageImpl#getType()
     * @generated
     */
    int TYPE = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE__NAME = 0;

    /**
     * The feature id for the '<em><b>Is Text</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE__IS_TEXT = 1;

    /**
     * The feature id for the '<em><b>Is Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE__IS_REFERENCE = 2;

    /**
     * The number of structural features of the '<em>Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link org.topcased.typesmodel.model.inittypes.impl.RegexImpl <em>Regex</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.topcased.typesmodel.model.inittypes.impl.RegexImpl
     * @see org.topcased.typesmodel.model.inittypes.impl.InittypesPackageImpl#getRegex()
     * @generated
     */
    int REGEX = 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEX__NAME = TYPE__NAME;

    /**
     * The feature id for the '<em><b>Is Text</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEX__IS_TEXT = TYPE__IS_TEXT;

    /**
     * The feature id for the '<em><b>Is Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEX__IS_REFERENCE = TYPE__IS_REFERENCE;

    /**
     * The feature id for the '<em><b>Expression</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEX__EXPRESSION = TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Regex</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEX_FEATURE_COUNT = TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.topcased.typesmodel.model.inittypes.impl.ColumnImpl <em>Column</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.topcased.typesmodel.model.inittypes.impl.ColumnImpl
     * @see org.topcased.typesmodel.model.inittypes.impl.InittypesPackageImpl#getColumn()
     * @generated
     */
    int COLUMN = 3;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COLUMN__NAME = REGEX__NAME;

    /**
     * The feature id for the '<em><b>Is Text</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COLUMN__IS_TEXT = REGEX__IS_TEXT;

    /**
     * The feature id for the '<em><b>Is Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COLUMN__IS_REFERENCE = REGEX__IS_REFERENCE;

    /**
     * The feature id for the '<em><b>Expression</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COLUMN__EXPRESSION = REGEX__EXPRESSION;

    /**
     * The feature id for the '<em><b>Number</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COLUMN__NUMBER = REGEX_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Column</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COLUMN_FEATURE_COUNT = REGEX_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.topcased.typesmodel.model.inittypes.impl.DocumentTypeImpl <em>Document Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.topcased.typesmodel.model.inittypes.impl.DocumentTypeImpl
     * @see org.topcased.typesmodel.model.inittypes.impl.InittypesPackageImpl#getDocumentType()
     * @generated
     */
    int DOCUMENT_TYPE = 4;

    /**
     * The feature id for the '<em><b>Types</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_TYPE__TYPES = 0;

    /**
     * The feature id for the '<em><b>Id</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_TYPE__ID = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_TYPE__NAME = 2;

    /**
     * The feature id for the '<em><b>Hierarchical</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_TYPE__HIERARCHICAL = 3;

    /**
     * The feature id for the '<em><b>Text Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_TYPE__TEXT_TYPE = 4;

    /**
     * The feature id for the '<em><b>Document Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_TYPE__DOCUMENT_PATH = 5;

    /**
     * The feature id for the '<em><b>Text Regex</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_TYPE__TEXT_REGEX = 6;

    /**
     * The number of structural features of the '<em>Document Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_TYPE_FEATURE_COUNT = 7;

    /**
     * The meta object id for the '{@link org.topcased.typesmodel.model.inittypes.impl.StyleImpl <em>Style</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.topcased.typesmodel.model.inittypes.impl.StyleImpl
     * @see org.topcased.typesmodel.model.inittypes.impl.InittypesPackageImpl#getStyle()
     * @generated
     */
    int STYLE = 5;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE__NAME = REGEX__NAME;

    /**
     * The feature id for the '<em><b>Is Text</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE__IS_TEXT = REGEX__IS_TEXT;

    /**
     * The feature id for the '<em><b>Is Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE__IS_REFERENCE = REGEX__IS_REFERENCE;

    /**
     * The feature id for the '<em><b>Expression</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE__EXPRESSION = REGEX__EXPRESSION;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE__LABEL = REGEX_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Style</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE_FEATURE_COUNT = REGEX_FEATURE_COUNT + 1;


    /**
     * Returns the meta object for class '{@link org.topcased.typesmodel.model.inittypes.TypeModel <em>Type Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Type Model</em>'.
     * @see org.topcased.typesmodel.model.inittypes.TypeModel
     * @generated
     */
    EClass getTypeModel();

    /**
     * Returns the meta object for the containment reference list '{@link org.topcased.typesmodel.model.inittypes.TypeModel#getDocumentTypes <em>Document Types</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Document Types</em>'.
     * @see org.topcased.typesmodel.model.inittypes.TypeModel#getDocumentTypes()
     * @see #getTypeModel()
     * @generated
     */
    EReference getTypeModel_DocumentTypes();

    /**
     * Returns the meta object for class '{@link org.topcased.typesmodel.model.inittypes.Type <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Type</em>'.
     * @see org.topcased.typesmodel.model.inittypes.Type
     * @generated
     */
    EClass getType();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.typesmodel.model.inittypes.Type#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.topcased.typesmodel.model.inittypes.Type#getName()
     * @see #getType()
     * @generated
     */
    EAttribute getType_Name();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.typesmodel.model.inittypes.Type#isIsText <em>Is Text</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Is Text</em>'.
     * @see org.topcased.typesmodel.model.inittypes.Type#isIsText()
     * @see #getType()
     * @generated
     */
    EAttribute getType_IsText();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.typesmodel.model.inittypes.Type#isIsReference <em>Is Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Is Reference</em>'.
     * @see org.topcased.typesmodel.model.inittypes.Type#isIsReference()
     * @see #getType()
     * @generated
     */
    EAttribute getType_IsReference();

    /**
     * Returns the meta object for class '{@link org.topcased.typesmodel.model.inittypes.Regex <em>Regex</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Regex</em>'.
     * @see org.topcased.typesmodel.model.inittypes.Regex
     * @generated
     */
    EClass getRegex();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.typesmodel.model.inittypes.Regex#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Expression</em>'.
     * @see org.topcased.typesmodel.model.inittypes.Regex#getExpression()
     * @see #getRegex()
     * @generated
     */
    EAttribute getRegex_Expression();

    /**
     * Returns the meta object for class '{@link org.topcased.typesmodel.model.inittypes.Column <em>Column</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Column</em>'.
     * @see org.topcased.typesmodel.model.inittypes.Column
     * @generated
     */
    EClass getColumn();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.typesmodel.model.inittypes.Column#getNumber <em>Number</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Number</em>'.
     * @see org.topcased.typesmodel.model.inittypes.Column#getNumber()
     * @see #getColumn()
     * @generated
     */
    EAttribute getColumn_Number();

    /**
     * Returns the meta object for class '{@link org.topcased.typesmodel.model.inittypes.DocumentType <em>Document Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Document Type</em>'.
     * @see org.topcased.typesmodel.model.inittypes.DocumentType
     * @generated
     */
    EClass getDocumentType();

    /**
     * Returns the meta object for the containment reference list '{@link org.topcased.typesmodel.model.inittypes.DocumentType#getTypes <em>Types</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Types</em>'.
     * @see org.topcased.typesmodel.model.inittypes.DocumentType#getTypes()
     * @see #getDocumentType()
     * @generated
     */
    EReference getDocumentType_Types();

    /**
     * Returns the meta object for the containment reference '{@link org.topcased.typesmodel.model.inittypes.DocumentType#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Id</em>'.
     * @see org.topcased.typesmodel.model.inittypes.DocumentType#getId()
     * @see #getDocumentType()
     * @generated
     */
    EReference getDocumentType_Id();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.typesmodel.model.inittypes.DocumentType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.topcased.typesmodel.model.inittypes.DocumentType#getName()
     * @see #getDocumentType()
     * @generated
     */
    EAttribute getDocumentType_Name();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.typesmodel.model.inittypes.DocumentType#isHierarchical <em>Hierarchical</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Hierarchical</em>'.
     * @see org.topcased.typesmodel.model.inittypes.DocumentType#isHierarchical()
     * @see #getDocumentType()
     * @generated
     */
    EAttribute getDocumentType_Hierarchical();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.typesmodel.model.inittypes.DocumentType#getTextType <em>Text Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Text Type</em>'.
     * @see org.topcased.typesmodel.model.inittypes.DocumentType#getTextType()
     * @see #getDocumentType()
     * @generated
     */
    EAttribute getDocumentType_TextType();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.typesmodel.model.inittypes.DocumentType#getDocumentPath <em>Document Path</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Document Path</em>'.
     * @see org.topcased.typesmodel.model.inittypes.DocumentType#getDocumentPath()
     * @see #getDocumentType()
     * @generated
     */
    EAttribute getDocumentType_DocumentPath();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.typesmodel.model.inittypes.DocumentType#getTextRegex <em>Text Regex</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Text Regex</em>'.
     * @see org.topcased.typesmodel.model.inittypes.DocumentType#getTextRegex()
     * @see #getDocumentType()
     * @generated
     */
    EAttribute getDocumentType_TextRegex();

    /**
     * Returns the meta object for class '{@link org.topcased.typesmodel.model.inittypes.Style <em>Style</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Style</em>'.
     * @see org.topcased.typesmodel.model.inittypes.Style
     * @generated
     */
    EClass getStyle();

    /**
     * Returns the meta object for the attribute '{@link org.topcased.typesmodel.model.inittypes.Style#getLabel <em>Label</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.topcased.typesmodel.model.inittypes.Style#getLabel()
     * @see #getStyle()
     * @generated
     */
    EAttribute getStyle_Label();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    InittypesFactory getInittypesFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals
    {
        /**
         * The meta object literal for the '{@link org.topcased.typesmodel.model.inittypes.impl.TypeModelImpl <em>Type Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.topcased.typesmodel.model.inittypes.impl.TypeModelImpl
         * @see org.topcased.typesmodel.model.inittypes.impl.InittypesPackageImpl#getTypeModel()
         * @generated
         */
        EClass TYPE_MODEL = eINSTANCE.getTypeModel();

        /**
         * The meta object literal for the '<em><b>Document Types</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TYPE_MODEL__DOCUMENT_TYPES = eINSTANCE.getTypeModel_DocumentTypes();

        /**
         * The meta object literal for the '{@link org.topcased.typesmodel.model.inittypes.impl.TypeImpl <em>Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.topcased.typesmodel.model.inittypes.impl.TypeImpl
         * @see org.topcased.typesmodel.model.inittypes.impl.InittypesPackageImpl#getType()
         * @generated
         */
        EClass TYPE = eINSTANCE.getType();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TYPE__NAME = eINSTANCE.getType_Name();

        /**
         * The meta object literal for the '<em><b>Is Text</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TYPE__IS_TEXT = eINSTANCE.getType_IsText();

        /**
         * The meta object literal for the '<em><b>Is Reference</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TYPE__IS_REFERENCE = eINSTANCE.getType_IsReference();

        /**
         * The meta object literal for the '{@link org.topcased.typesmodel.model.inittypes.impl.RegexImpl <em>Regex</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.topcased.typesmodel.model.inittypes.impl.RegexImpl
         * @see org.topcased.typesmodel.model.inittypes.impl.InittypesPackageImpl#getRegex()
         * @generated
         */
        EClass REGEX = eINSTANCE.getRegex();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REGEX__EXPRESSION = eINSTANCE.getRegex_Expression();

        /**
         * The meta object literal for the '{@link org.topcased.typesmodel.model.inittypes.impl.ColumnImpl <em>Column</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.topcased.typesmodel.model.inittypes.impl.ColumnImpl
         * @see org.topcased.typesmodel.model.inittypes.impl.InittypesPackageImpl#getColumn()
         * @generated
         */
        EClass COLUMN = eINSTANCE.getColumn();

        /**
         * The meta object literal for the '<em><b>Number</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COLUMN__NUMBER = eINSTANCE.getColumn_Number();

        /**
         * The meta object literal for the '{@link org.topcased.typesmodel.model.inittypes.impl.DocumentTypeImpl <em>Document Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.topcased.typesmodel.model.inittypes.impl.DocumentTypeImpl
         * @see org.topcased.typesmodel.model.inittypes.impl.InittypesPackageImpl#getDocumentType()
         * @generated
         */
        EClass DOCUMENT_TYPE = eINSTANCE.getDocumentType();

        /**
         * The meta object literal for the '<em><b>Types</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_TYPE__TYPES = eINSTANCE.getDocumentType_Types();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_TYPE__ID = eINSTANCE.getDocumentType_Id();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_TYPE__NAME = eINSTANCE.getDocumentType_Name();

        /**
         * The meta object literal for the '<em><b>Hierarchical</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_TYPE__HIERARCHICAL = eINSTANCE.getDocumentType_Hierarchical();

        /**
         * The meta object literal for the '<em><b>Text Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_TYPE__TEXT_TYPE = eINSTANCE.getDocumentType_TextType();

        /**
         * The meta object literal for the '<em><b>Document Path</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_TYPE__DOCUMENT_PATH = eINSTANCE.getDocumentType_DocumentPath();

        /**
         * The meta object literal for the '<em><b>Text Regex</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_TYPE__TEXT_REGEX = eINSTANCE.getDocumentType_TextRegex();

        /**
         * The meta object literal for the '{@link org.topcased.typesmodel.model.inittypes.impl.StyleImpl <em>Style</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.topcased.typesmodel.model.inittypes.impl.StyleImpl
         * @see org.topcased.typesmodel.model.inittypes.impl.InittypesPackageImpl#getStyle()
         * @generated
         */
        EClass STYLE = eINSTANCE.getStyle();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute STYLE__LABEL = eINSTANCE.getStyle_Label();

    }

} //InittypesPackage
