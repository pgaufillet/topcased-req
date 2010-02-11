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

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.RequirementPackage;


/**
 * This is the item provider adapter for a {@link org.topcased.requirement.ObjectAttribute} object. <!-- begin-user-doc --> <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class ObjectAttributeItemProvider extends AttributeItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider,
        IItemPropertySource, IItemColorProvider
{
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public ObjectAttributeItemProvider(AdapterFactory adapterFactory)
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

            addValuePropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Value feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addValuePropertyDescriptor(Object object)
    {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
                getString("_UI_ObjectAttribute_value_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ObjectAttribute_value_feature", "_UI_ObjectAttribute_type"),
                RequirementPackage.Literals.OBJECT_ATTRIBUTE__VALUE, true, false, true, null, null, null));
    }

    /**
     * This returns ObjectAttribute.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object getImage(Object object)
    {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/ObjectAttribute"));
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public String getText(Object object)
    {
        return ((ObjectAttribute) object).getName();
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached children and by creating
     * a viewer notification, which it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated NOT
     * @custom
     */
    @Override
    public void notifyChanged(Notification notification)
    {
        updateChildren(notification);
        switch (notification.getFeatureID(ObjectAttribute.class))
        {
            case RequirementPackage.OBJECT_ATTRIBUTE__VALUE:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
    }

}
