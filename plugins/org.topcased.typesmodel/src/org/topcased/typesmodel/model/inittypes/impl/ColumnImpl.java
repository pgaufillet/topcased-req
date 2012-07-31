/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes.impl;

import org.eclipse.emf.ecore.EClass;

import org.topcased.typesmodel.model.inittypes.Column;
import org.topcased.typesmodel.model.inittypes.InittypesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Column</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.ColumnImpl#getNumber <em>Number</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ColumnImpl extends RegexImpl implements Column
{
    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected ColumnImpl()
    {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass()
    {
		return InittypesPackage.Literals.COLUMN;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public int getNumber()
    {
		return (Integer)eGet(InittypesPackage.Literals.COLUMN__NUMBER, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNumber(int newNumber)
    {
		eSet(InittypesPackage.Literals.COLUMN__NUMBER, newNumber);
	}

} //ColumnImpl
