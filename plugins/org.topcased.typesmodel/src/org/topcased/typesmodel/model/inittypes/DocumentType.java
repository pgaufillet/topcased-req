/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DocumentType#getTypes <em>Types</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DocumentType#getId <em>Id</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DocumentType#getName <em>Name</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DocumentType#isHierarchical <em>Hierarchical</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DocumentType#getTextType <em>Text Type</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.DocumentType#getDocumentPath <em>Document Path</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDocumentType()
 * @model
 * @generated
 */
public interface DocumentType extends EObject
{
    /**
     * Returns the value of the '<em><b>Types</b></em>' containment reference list.
     * The list contents are of type {@link org.topcased.typesmodel.model.inittypes.Type}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Types</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Types</em>' containment reference list.
     * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDocumentType_Types()
     * @model containment="true"
     * @generated
     */
    EList<Type> getTypes();

    /**
     * Returns the value of the '<em><b>Id</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Id</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Id</em>' containment reference.
     * @see #setId(Type)
     * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDocumentType_Id()
     * @model containment="true"
     * @generated
     */
    Type getId();

    /**
     * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.DocumentType#getId <em>Id</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Id</em>' containment reference.
     * @see #getId()
     * @generated
     */
    void setId(Type value);

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDocumentType_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.DocumentType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Hierarchical</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Hierarchical</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Hierarchical</em>' attribute.
     * @see #setHierarchical(boolean)
     * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDocumentType_Hierarchical()
     * @model
     * @generated
     */
    boolean isHierarchical();

    /**
     * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.DocumentType#isHierarchical <em>Hierarchical</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Hierarchical</em>' attribute.
     * @see #isHierarchical()
     * @generated
     */
    void setHierarchical(boolean value);

    /**
     * Returns the value of the '<em><b>Text Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Text Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Text Type</em>' attribute.
     * @see #setTextType(String)
     * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDocumentType_TextType()
     * @model
     * @generated
     */
    String getTextType();

    /**
     * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.DocumentType#getTextType <em>Text Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Text Type</em>' attribute.
     * @see #getTextType()
     * @generated
     */
    void setTextType(String value);

    /**
     * Returns the value of the '<em><b>Document Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Document Path</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Document Path</em>' attribute.
     * @see #setDocumentPath(String)
     * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getDocumentType_DocumentPath()
     * @model
     * @generated
     */
    String getDocumentPath();

    /**
     * Sets the value of the '{@link org.topcased.typesmodel.model.inittypes.DocumentType#getDocumentPath <em>Document Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Document Path</em>' attribute.
     * @see #getDocumentPath()
     * @generated
     */
    void setDocumentPath(String value);

} // DocumentType
