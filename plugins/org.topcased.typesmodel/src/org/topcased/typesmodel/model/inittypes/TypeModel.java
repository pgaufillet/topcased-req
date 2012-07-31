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
 * A representation of the model object '<em><b>Type Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.TypeModel#getDocumentTypes <em>Document Types</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getTypeModel()
 * @model
 * @generated
 */
public interface TypeModel extends EObject
{
    /**
	 * Returns the value of the '<em><b>Document Types</b></em>' containment reference list.
	 * The list contents are of type {@link org.topcased.typesmodel.model.inittypes.DocumentType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Document Types</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Document Types</em>' containment reference list.
	 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage#getTypeModel_DocumentTypes()
	 * @model containment="true"
	 * @generated
	 */
    EList<DocumentType> getDocumentTypes();

} // TypeModel
