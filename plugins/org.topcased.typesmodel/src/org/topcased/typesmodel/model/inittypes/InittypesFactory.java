/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage
 * @generated
 */
public interface InittypesFactory extends EFactory
{
    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    InittypesFactory eINSTANCE = org.topcased.typesmodel.model.inittypes.impl.InittypesFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>Type Model</em>'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return a new object of class '<em>Type Model</em>'.
	 * @generated
	 */
    TypeModel createTypeModel();

    /**
	 * Returns a new object of class '<em>Regex</em>'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return a new object of class '<em>Regex</em>'.
	 * @generated
	 */
    Regex createRegex();

    /**
	 * Returns a new object of class '<em>Column</em>'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return a new object of class '<em>Column</em>'.
	 * @generated
	 */
    Column createColumn();

    /**
	 * Returns a new object of class '<em>Document Type</em>'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return a new object of class '<em>Document Type</em>'.
	 * @generated
	 */
    DocumentType createDocumentType();

    /**
	 * Returns a new object of class '<em>Style</em>'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return a new object of class '<em>Style</em>'.
	 * @generated
	 */
    Style createStyle();

    /**
	 * Returns a new object of class '<em>Deletion Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Deletion Parameters</em>'.
	 * @generated
	 */
	DeletionParameters createDeletionParameters();

				/**
	 * Returns a new object of class '<em>Deletion Paremeter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Deletion Paremeter</em>'.
	 * @generated
	 */
	DeletionParemeter createDeletionParemeter();

				/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    InittypesPackage getInittypesPackage();

} //InittypesFactory
