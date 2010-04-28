/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/

package org.topcased.requirement.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.topcased.requirement.AttributesType;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.RequirementPlugin;


/**
 * This is the item provider adapter for a {@link org.topcased.requirement.ConfiguratedAttribute} object. <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class ConfiguratedAttributeItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider,
        IItemPropertySource, IItemColorProvider
{
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public ConfiguratedAttributeItemProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
    }

    /**
     * This returns the property descriptors for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
    {
        if (itemPropertyDescriptors == null)
        {
            super.getPropertyDescriptors(object);

            addNamePropertyDescriptor(object);
            addTypePropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Name feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addNamePropertyDescriptor(Object object)
    {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
                getString("_UI_ConfiguratedAttribute_name_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ConfiguratedAttribute_name_feature", "_UI_ConfiguratedAttribute_type"),
                RequirementPackage.Literals.CONFIGURATED_ATTRIBUTE__NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
     * This adds a property descriptor for the Type feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addTypePropertyDescriptor(Object object)
    {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
                getString("_UI_ConfiguratedAttribute_type_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ConfiguratedAttribute_type_feature", "_UI_ConfiguratedAttribute_type"),
                RequirementPackage.Literals.CONFIGURATED_ATTRIBUTE__TYPE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
     * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
     * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
     * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Collection< ? extends EStructuralFeature> getChildrenFeatures(Object object)
    {
        if (childrenFeatures == null)
        {
            super.getChildrenFeatures(object);
            childrenFeatures.add(RequirementPackage.Literals.CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE);
            childrenFeatures.add(RequirementPackage.Literals.CONFIGURATED_ATTRIBUTE__LIST_VALUE);
        }
        return childrenFeatures;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
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
     * This returns ConfiguratedAttribute.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public Object getImage(Object object)
    {
        switch (((ConfiguratedAttribute) object).getType().getValue())
        {
            case AttributesType.ALLOCATE_VALUE:
                return overlayImage(object, getResourceLocator().getImage("full/obj16/AttributeAllocate.gif"));
            case AttributesType.LINK_VALUE:
                return overlayImage(object, getResourceLocator().getImage("full/obj16/AttributeLink.gif"));
            case AttributesType.TEXT_VALUE:
                return overlayImage(object, getResourceLocator().getImage("full/obj16/TextAttribute.gif"));
            case AttributesType.OBJECT_VALUE:
                return overlayImage(object, getResourceLocator().getImage("full/obj16/ObjectAttribute.gif"));
            default:
                break;
        }
        return overlayImage(object, getResourceLocator().getImage("full/obj16/ConfiguratedAttribute"));
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public String getText(Object object)
    {
        String name = ((ConfiguratedAttribute) object).getName();
        String type = ((ConfiguratedAttribute) object).getType().getLiteral();
        return name.concat(" : ").concat(type);
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached children and by creating
     * a viewer notification, which it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     */
    @Override
    public void notifyChanged(Notification notification)
    {
        updateChildren(notification);

        switch (notification.getFeatureID(ConfiguratedAttribute.class))
        {
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__NAME:
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__TYPE:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE:
            case RequirementPackage.CONFIGURATED_ATTRIBUTE__LIST_VALUE:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
     * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children that can be created
     * under this object. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
    {
        super.collectNewChildDescriptors(newChildDescriptors, object);

        newChildDescriptors.add(createChildParameter(RequirementPackage.Literals.CONFIGURATED_ATTRIBUTE__DEFAULT_VALUE, RequirementFactory.eINSTANCE.createDefaultAttributeValue()));

        newChildDescriptors.add(createChildParameter(RequirementPackage.Literals.CONFIGURATED_ATTRIBUTE__LIST_VALUE, RequirementFactory.eINSTANCE.createAttributeValue()));
    }

    /**
     * Return the resource locator for this item provider's resources. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public ResourceLocator getResourceLocator()
    {
        return RequirementPlugin.INSTANCE;
    }

}
