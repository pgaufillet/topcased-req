/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.typesmodel.model.inittypes.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.topcased.typesmodel.model.inittypes.DocumentType;
import org.topcased.typesmodel.model.inittypes.InittypesFactory;
import org.topcased.typesmodel.model.inittypes.InittypesPackage;

/**
 * This is the item provider adapter for a {@link org.topcased.typesmodel.model.inittypes.DocumentType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class DocumentTypeItemProvider
    extends ItemProviderAdapter
    implements
        IEditingDomainItemProvider,
        IStructuredItemContentProvider,
        ITreeItemContentProvider,
        IItemLabelProvider,
        IItemPropertySource
{
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DocumentTypeItemProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
    }

    /**
     * This returns the property descriptors for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
    {
        if (itemPropertyDescriptors == null)
        {
            super.getPropertyDescriptors(object);

            addNamePropertyDescriptor(object);
            addHierarchicalPropertyDescriptor(object);
            addTextTypePropertyDescriptor(object);
            addDocumentPathPropertyDescriptor(object);
            addTextRegexPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Name feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNamePropertyDescriptor(Object object)
    {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_DocumentType_name_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_DocumentType_name_feature", "_UI_DocumentType_type"),
                 InittypesPackage.Literals.DOCUMENT_TYPE__NAME,
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Hierarchical feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addHierarchicalPropertyDescriptor(Object object)
    {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_DocumentType_hierarchical_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_DocumentType_hierarchical_feature", "_UI_DocumentType_type"),
                 InittypesPackage.Literals.DOCUMENT_TYPE__HIERARCHICAL,
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Text Type feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addTextTypePropertyDescriptor(Object object)
    {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_DocumentType_textType_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_DocumentType_textType_feature", "_UI_DocumentType_type"),
                 InittypesPackage.Literals.DOCUMENT_TYPE__TEXT_TYPE,
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Document Path feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addDocumentPathPropertyDescriptor(Object object)
    {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_DocumentType_documentPath_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_DocumentType_documentPath_feature", "_UI_DocumentType_type"),
                 InittypesPackage.Literals.DOCUMENT_TYPE__DOCUMENT_PATH,
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Text Regex feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addTextRegexPropertyDescriptor(Object object)
    {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_DocumentType_textRegex_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_DocumentType_textRegex_feature", "_UI_DocumentType_type"),
                 InittypesPackage.Literals.DOCUMENT_TYPE__TEXT_REGEX,
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
     * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
     * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
    {
        if (childrenFeatures == null)
        {
            super.getChildrenFeatures(object);
            childrenFeatures.add(InittypesPackage.Literals.DOCUMENT_TYPE__TYPES);
            childrenFeatures.add(InittypesPackage.Literals.DOCUMENT_TYPE__ID);
        }
        return childrenFeatures;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EStructuralFeature getChildFeature(Object object, Object child)
    {
        // Check the type of the specified child object and return the proper feature to use for
        // adding (see {@link AddCommand}) it as a child.

        return super.getChildFeature(object, child);
    }

    /**
     * This returns DocumentType.gif.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object getImage(Object object)
    {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/DocumentType"));
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public String getText(Object object)
    {
        DocumentType documentType= ((DocumentType)object) ;
        String label = documentType.getName();
        if ( label == null || label.length() == 0)
        {
            label = getString("_UI_DocumentType_type") + "No Name" ;
        }
        label += " (" + documentType.getDocumentPath() + ")" ;
        return label ;
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached
     * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void notifyChanged(Notification notification)
    {
        updateChildren(notification);

        switch (notification.getFeatureID(DocumentType.class))
        {
            case InittypesPackage.DOCUMENT_TYPE__NAME:
            case InittypesPackage.DOCUMENT_TYPE__HIERARCHICAL:
            case InittypesPackage.DOCUMENT_TYPE__TEXT_TYPE:
            case InittypesPackage.DOCUMENT_TYPE__DOCUMENT_PATH:
            case InittypesPackage.DOCUMENT_TYPE__TEXT_REGEX:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case InittypesPackage.DOCUMENT_TYPE__TYPES:
            case InittypesPackage.DOCUMENT_TYPE__ID:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
     * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
     * that can be created under this object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
    {
        super.collectNewChildDescriptors(newChildDescriptors, object);

        newChildDescriptors.add
            (createChildParameter
                (InittypesPackage.Literals.DOCUMENT_TYPE__TYPES,
                 InittypesFactory.eINSTANCE.createRegex()));

        newChildDescriptors.add
            (createChildParameter
                (InittypesPackage.Literals.DOCUMENT_TYPE__TYPES,
                 InittypesFactory.eINSTANCE.createColumn()));

        newChildDescriptors.add
            (createChildParameter
                (InittypesPackage.Literals.DOCUMENT_TYPE__TYPES,
                 InittypesFactory.eINSTANCE.createStyle()));

        newChildDescriptors.add
            (createChildParameter
                (InittypesPackage.Literals.DOCUMENT_TYPE__ID,
                 InittypesFactory.eINSTANCE.createRegex()));

        newChildDescriptors.add
            (createChildParameter
                (InittypesPackage.Literals.DOCUMENT_TYPE__ID,
                 InittypesFactory.eINSTANCE.createColumn()));

        newChildDescriptors.add
            (createChildParameter
                (InittypesPackage.Literals.DOCUMENT_TYPE__ID,
                 InittypesFactory.eINSTANCE.createStyle()));
    }

    /**
     * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection)
    {
        Object childFeature = feature;
        Object childObject = child;

        boolean qualify =
            childFeature == InittypesPackage.Literals.DOCUMENT_TYPE__TYPES ||
            childFeature == InittypesPackage.Literals.DOCUMENT_TYPE__ID;

        if (qualify)
        {
            return getString
                ("_UI_CreateChild_text2",
                 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
        }
        return super.getCreateChildText(owner, feature, child, selection);
    }

    /**
     * Return the resource locator for this item provider's resources.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator()
    {
        return InitTypesEditPlugin.INSTANCE;
    }

}
