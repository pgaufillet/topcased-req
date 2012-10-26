/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.topcased.typesmodel.model.inittypes.DeletionParameters;
import org.topcased.typesmodel.model.inittypes.DeletionParemeter;
import org.topcased.typesmodel.model.inittypes.InittypesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deletion Parameters</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DeletionParametersImpl#getRegexDescription <em>Regex Description</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DeletionParametersImpl#getRegexId <em>Regex Id</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DeletionParametersImpl#getRegexAttributes <em>Regex Attributes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DeletionParametersImpl extends MinimalEObjectImpl.Container implements DeletionParameters {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeletionParametersImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InittypesPackage.Literals.DELETION_PARAMETERS;
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
	public String getRegexDescription() {
		return (String)eGet(InittypesPackage.Literals.DELETION_PARAMETERS__REGEX_DESCRIPTION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRegexDescription(String newRegexDescription) {
		eSet(InittypesPackage.Literals.DELETION_PARAMETERS__REGEX_DESCRIPTION, newRegexDescription);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRegexId() {
		return (String)eGet(InittypesPackage.Literals.DELETION_PARAMETERS__REGEX_ID, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRegexId(String newRegexId) {
		eSet(InittypesPackage.Literals.DELETION_PARAMETERS__REGEX_ID, newRegexId);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<DeletionParemeter> getRegexAttributes() {
		return (EList<DeletionParemeter>)eGet(InittypesPackage.Literals.DELETION_PARAMETERS__REGEX_ATTRIBUTES, true);
	}

} //DeletionParametersImpl
