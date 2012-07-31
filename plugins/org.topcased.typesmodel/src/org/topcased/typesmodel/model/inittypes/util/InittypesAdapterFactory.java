/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.topcased.typesmodel.model.inittypes.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.topcased.typesmodel.model.inittypes.InittypesPackage
 * @generated
 */
public class InittypesAdapterFactory extends AdapterFactoryImpl
{
    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected static InittypesPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public InittypesAdapterFactory()
    {
		if (modelPackage == null) {
			modelPackage = InittypesPackage.eINSTANCE;
		}
	}

    /**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
    @Override
    public boolean isFactoryForType(Object object)
    {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

    /**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected InittypesSwitch<Adapter> modelSwitch =
        new InittypesSwitch<Adapter>() {
			@Override
			public Adapter caseTypeModel(TypeModel object) {
				return createTypeModelAdapter();
			}
			@Override
			public Adapter caseType(Type object) {
				return createTypeAdapter();
			}
			@Override
			public Adapter caseRegex(Regex object) {
				return createRegexAdapter();
			}
			@Override
			public Adapter caseColumn(Column object) {
				return createColumnAdapter();
			}
			@Override
			public Adapter caseDocumentType(DocumentType object) {
				return createDocumentTypeAdapter();
			}
			@Override
			public Adapter caseStyle(Style object) {
				return createStyleAdapter();
			}
			@Override
			public Adapter caseDeletionParameters(DeletionParameters object) {
				return createDeletionParametersAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

    /**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
    @Override
    public Adapter createAdapter(Notifier target)
    {
		return modelSwitch.doSwitch((EObject)target);
	}


    /**
	 * Creates a new adapter for an object of class '{@link org.topcased.typesmodel.model.inittypes.TypeModel <em>Type Model</em>}'.
	 * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.topcased.typesmodel.model.inittypes.TypeModel
	 * @generated
	 */
    public Adapter createTypeModelAdapter()
    {
		return null;
	}

    /**
	 * Creates a new adapter for an object of class '{@link org.topcased.typesmodel.model.inittypes.Type <em>Type</em>}'.
	 * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.topcased.typesmodel.model.inittypes.Type
	 * @generated
	 */
    public Adapter createTypeAdapter()
    {
		return null;
	}

    /**
	 * Creates a new adapter for an object of class '{@link org.topcased.typesmodel.model.inittypes.Regex <em>Regex</em>}'.
	 * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.topcased.typesmodel.model.inittypes.Regex
	 * @generated
	 */
    public Adapter createRegexAdapter()
    {
		return null;
	}

    /**
	 * Creates a new adapter for an object of class '{@link org.topcased.typesmodel.model.inittypes.Column <em>Column</em>}'.
	 * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.topcased.typesmodel.model.inittypes.Column
	 * @generated
	 */
    public Adapter createColumnAdapter()
    {
		return null;
	}

    /**
	 * Creates a new adapter for an object of class '{@link org.topcased.typesmodel.model.inittypes.DocumentType <em>Document Type</em>}'.
	 * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.topcased.typesmodel.model.inittypes.DocumentType
	 * @generated
	 */
    public Adapter createDocumentTypeAdapter()
    {
		return null;
	}

    /**
	 * Creates a new adapter for an object of class '{@link org.topcased.typesmodel.model.inittypes.Style <em>Style</em>}'.
	 * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.topcased.typesmodel.model.inittypes.Style
	 * @generated
	 */
    public Adapter createStyleAdapter()
    {
		return null;
	}

    /**
	 * Creates a new adapter for an object of class '{@link org.topcased.typesmodel.model.inittypes.DeletionParameters <em>Deletion Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.topcased.typesmodel.model.inittypes.DeletionParameters
	 * @generated
	 */
	public Adapter createDeletionParametersAdapter() {
		return null;
	}

				/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
    public Adapter createEObjectAdapter()
    {
		return null;
	}

} //InittypesAdapterFactory
