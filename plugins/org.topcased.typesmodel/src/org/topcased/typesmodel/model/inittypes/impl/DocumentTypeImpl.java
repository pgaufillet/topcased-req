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
import org.topcased.typesmodel.model.inittypes.DocumentType;
import org.topcased.typesmodel.model.inittypes.InittypesPackage;
import org.topcased.typesmodel.model.inittypes.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DocumentTypeImpl#getTypes <em>Types</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DocumentTypeImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DocumentTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DocumentTypeImpl#isHierarchical <em>Hierarchical</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DocumentTypeImpl#getTextType <em>Text Type</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DocumentTypeImpl#getDocumentPath <em>Document Path</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DocumentTypeImpl#getTextRegex <em>Text Regex</em>}</li>
 *   <li>{@link org.topcased.typesmodel.model.inittypes.impl.DocumentTypeImpl#getDeletionParameters <em>Deletion Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentTypeImpl extends MinimalEObjectImpl.Container implements DocumentType
{
    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected DocumentTypeImpl()
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
		return InittypesPackage.Literals.DOCUMENT_TYPE;
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
    public EList<Type> getTypes()
    {
		return (EList<Type>)eGet(InittypesPackage.Literals.DOCUMENT_TYPE__TYPES, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Type getId()
    {
		return (Type)eGet(InittypesPackage.Literals.DOCUMENT_TYPE__ID, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setId(Type newId)
    {
		eSet(InittypesPackage.Literals.DOCUMENT_TYPE__ID, newId);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getName()
    {
		return (String)eGet(InittypesPackage.Literals.DOCUMENT_TYPE__NAME, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setName(String newName)
    {
		eSet(InittypesPackage.Literals.DOCUMENT_TYPE__NAME, newName);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isHierarchical()
    {
		return (Boolean)eGet(InittypesPackage.Literals.DOCUMENT_TYPE__HIERARCHICAL, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setHierarchical(boolean newHierarchical)
    {
		eSet(InittypesPackage.Literals.DOCUMENT_TYPE__HIERARCHICAL, newHierarchical);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTextType()
    {
		return (String)eGet(InittypesPackage.Literals.DOCUMENT_TYPE__TEXT_TYPE, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTextType(String newTextType)
    {
		eSet(InittypesPackage.Literals.DOCUMENT_TYPE__TEXT_TYPE, newTextType);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getDocumentPath()
    {
		return (String)eGet(InittypesPackage.Literals.DOCUMENT_TYPE__DOCUMENT_PATH, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDocumentPath(String newDocumentPath)
    {
		eSet(InittypesPackage.Literals.DOCUMENT_TYPE__DOCUMENT_PATH, newDocumentPath);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTextRegex()
    {
		return (String)eGet(InittypesPackage.Literals.DOCUMENT_TYPE__TEXT_REGEX, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTextRegex(String newTextRegex)
    {
		eSet(InittypesPackage.Literals.DOCUMENT_TYPE__TEXT_REGEX, newTextRegex);
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeletionParameters getDeletionParameters() {
		return (DeletionParameters)eGet(InittypesPackage.Literals.DOCUMENT_TYPE__DELETION_PARAMETERS, true);
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeletionParameters(DeletionParameters newDeletionParameters) {
		eSet(InittypesPackage.Literals.DOCUMENT_TYPE__DELETION_PARAMETERS, newDeletionParameters);
	}

} //DocumentTypeImpl
