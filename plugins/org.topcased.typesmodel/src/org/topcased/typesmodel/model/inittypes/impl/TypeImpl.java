/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.topcased.typesmodel.model.inittypes.InittypesPackage;
import org.topcased.typesmodel.model.inittypes.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.TypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.TypeImpl#isIsText <em>Is Text</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.TypeImpl#isIsReference <em>Is Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class TypeImpl extends MinimalEObjectImpl.Container implements Type
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TypeImpl()
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
        return InittypesPackage.Literals.TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected int eStaticFeatureCount()
    {
        return 0;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName()
    {
        return (String)eGet(InittypesPackage.Literals.TYPE__NAME, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(String newName)
    {
        eSet(InittypesPackage.Literals.TYPE__NAME, newName);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsText()
    {
        return (Boolean)eGet(InittypesPackage.Literals.TYPE__IS_TEXT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsText(boolean newIsText)
    {
        eSet(InittypesPackage.Literals.TYPE__IS_TEXT, newIsText);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsReference()
    {
        return (Boolean)eGet(InittypesPackage.Literals.TYPE__IS_REFERENCE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsReference(boolean newIsReference)
    {
        eSet(InittypesPackage.Literals.TYPE__IS_REFERENCE, newIsReference);
    }

} //TypeImpl
