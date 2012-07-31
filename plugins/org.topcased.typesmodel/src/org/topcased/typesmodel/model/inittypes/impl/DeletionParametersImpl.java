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
import org.topcased.typesmodel.model.inittypes.InittypesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deletion Parameters</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DeletionParametersImpl#getRegex <em>Regex</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DeletionParametersImpl#getAttributesToMatch <em>Attributes To Match</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DeletionParametersImpl#isMatchDescription <em>Match Description</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DeletionParametersImpl#isMatchId <em>Match Id</em>}</li>
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
	public String getRegex() {
		return (String)eGet(InittypesPackage.Literals.DELETION_PARAMETERS__REGEX, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRegex(String newRegex) {
		eSet(InittypesPackage.Literals.DELETION_PARAMETERS__REGEX, newRegex);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<String> getAttributesToMatch() {
		return (EList<String>)eGet(InittypesPackage.Literals.DELETION_PARAMETERS__ATTRIBUTES_TO_MATCH, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isMatchDescription() {
		return (Boolean)eGet(InittypesPackage.Literals.DELETION_PARAMETERS__MATCH_DESCRIPTION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMatchDescription(boolean newMatchDescription) {
		eSet(InittypesPackage.Literals.DELETION_PARAMETERS__MATCH_DESCRIPTION, newMatchDescription);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isMatchId() {
		return (Boolean)eGet(InittypesPackage.Literals.DELETION_PARAMETERS__MATCH_ID, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMatchId(boolean newMatchId) {
		eSet(InittypesPackage.Literals.DELETION_PARAMETERS__MATCH_ID, newMatchId);
	}

} //DeletionParametersImpl
