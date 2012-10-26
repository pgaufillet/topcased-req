/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.topcased.typesmodel.model.inittypes.DeletionParemeter;
import org.topcased.typesmodel.model.inittypes.InittypesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deletion Paremeter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DeletionParemeterImpl#getNameAttribute <em>Name Attribute</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DeletionParemeterImpl#getRegexAttribute <em>Regex Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DeletionParemeterImpl extends MinimalEObjectImpl.Container implements DeletionParemeter {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeletionParemeterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InittypesPackage.Literals.DELETION_PAREMETER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected int eStaticFeatureCount() {
		return 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNameAttribute() {
		return (String)eGet(InittypesPackage.Literals.DELETION_PAREMETER__NAME_ATTRIBUTE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNameAttribute(String newNameAttribute) {
		eSet(InittypesPackage.Literals.DELETION_PAREMETER__NAME_ATTRIBUTE, newNameAttribute);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRegexAttribute() {
		return (String)eGet(InittypesPackage.Literals.DELETION_PAREMETER__REGEX_ATTRIBUTE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRegexAttribute(String newRegexAttribute) {
		eSet(InittypesPackage.Literals.DELETION_PAREMETER__REGEX_ATTRIBUTE, newRegexAttribute);
	}

} //DeletionParemeterImpl
