/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes.impl;

import org.eclipse.emf.ecore.EClass;

import org.topcased.typesmodel.model.inittypes.InittypesPackage;
import org.topcased.typesmodel.model.inittypes.Regex;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Regex</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.RegexImpl#getExpression <em>Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RegexImpl extends TypeImpl implements Regex
{
    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected RegexImpl()
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
		return InittypesPackage.Literals.REGEX;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getExpression()
    {
		return (String)eGet(InittypesPackage.Literals.REGEX__EXPRESSION, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setExpression(String newExpression)
    {
		eSet(InittypesPackage.Literals.REGEX__EXPRESSION, newExpression);
	}

} //RegexImpl
