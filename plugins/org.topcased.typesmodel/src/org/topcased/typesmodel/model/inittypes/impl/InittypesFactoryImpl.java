/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.topcased.typesmodel.model.inittypes.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class InittypesFactoryImpl extends EFactoryImpl implements InittypesFactory
{
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static InittypesFactory init()
    {
        try
        {
            InittypesFactory theInittypesFactory = (InittypesFactory)EPackage.Registry.INSTANCE.getEFactory("http://inittypes/1.0"); 
            if (theInittypesFactory != null)
            {
                return theInittypesFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new InittypesFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InittypesFactoryImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass)
    {
        switch (eClass.getClassifierID())
        {
            case InittypesPackage.TYPE_MODEL: return createTypeModel();
            case InittypesPackage.REGEX: return createRegex();
            case InittypesPackage.COLUMN: return createColumn();
            case InittypesPackage.DOCUMENT_TYPE: return createDocumentType();
            case InittypesPackage.STYLE: return createStyle();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TypeModel createTypeModel()
    {
        TypeModelImpl typeModel = new TypeModelImpl();
        return typeModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Regex createRegex()
    {
        RegexImpl regex = new RegexImpl();
        return regex;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Column createColumn()
    {
        ColumnImpl column = new ColumnImpl();
        return column;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DocumentType createDocumentType()
    {
        DocumentTypeImpl documentType = new DocumentTypeImpl();
        return documentType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Style createStyle()
    {
        StyleImpl style = new StyleImpl();
        return style;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InittypesPackage getInittypesPackage()
    {
        return (InittypesPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static InittypesPackage getPackage()
    {
        return InittypesPackage.eINSTANCE;
    }

} //InittypesFactoryImpl
