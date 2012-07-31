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

import org.topcased.typesmodel.model.inittypes.DocumentType;
import org.topcased.typesmodel.model.inittypes.InittypesPackage;
import org.topcased.typesmodel.model.inittypes.TypeModel;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Type Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.TypeModelImpl#getDocumentTypes <em>Document Types</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TypeModelImpl extends MinimalEObjectImpl.Container implements TypeModel
{
    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected TypeModelImpl()
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
		return InittypesPackage.Literals.TYPE_MODEL;
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
    @SuppressWarnings("unchecked")
    public EList<DocumentType> getDocumentTypes()
    {
		return (EList<DocumentType>)eGet(InittypesPackage.Literals.TYPE_MODEL__DOCUMENT_TYPES, true);
	}

} //TypeModelImpl
