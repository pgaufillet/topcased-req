/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
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
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementPackage;

/**
 * This is the item provider adapter for a {@link org.topcased.requirement.HierarchicalElement} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class HierarchicalElementItemProvider extends IdentifiedElementItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider,
        IItemPropertySource, IItemColorProvider
{
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public HierarchicalElementItemProvider(AdapterFactory adapterFactory)
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

            addElementPropertyDescriptor(object);
            addNextReqIndexPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Element feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addElementPropertyDescriptor(Object object)
    {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
                getString("_UI_HierarchicalElement_element_feature"), //$NON-NLS-1$
                getString("_UI_PropertyDescriptor_description", "_UI_HierarchicalElement_element_feature", "_UI_HierarchicalElement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                RequirementPackage.Literals.HIERARCHICAL_ELEMENT__ELEMENT, false, false, true, null, null, null));
    }

    /**
     * This adds a property descriptor for the Next Req Index feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addNextReqIndexPropertyDescriptor(Object object)
    {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
                getString("_UI_HierarchicalElement_nextReqIndex_feature"), //$NON-NLS-1$
                getString("_UI_PropertyDescriptor_description", "_UI_HierarchicalElement_nextReqIndex_feature", "_UI_HierarchicalElement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                RequirementPackage.Literals.HIERARCHICAL_ELEMENT__NEXT_REQ_INDEX, true, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null));
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
            childrenFeatures.add(RequirementPackage.Literals.HIERARCHICAL_ELEMENT__CHILDREN);
            childrenFeatures.add(RequirementPackage.Literals.HIERARCHICAL_ELEMENT__REQUIREMENT);
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
     * This returns HierarchicalElement.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object getImage(Object object)
    {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/HierarchicalElement")); //$NON-NLS-1$
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getText(Object object)
    {
        String label = ((HierarchicalElement) object).getIdentifier();
        return label == null || label.length() == 0 ? getString("_UI_HierarchicalElement_type") : //$NON-NLS-1$
                getString("_UI_HierarchicalElement_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
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

        switch (notification.getFeatureID(HierarchicalElement.class))
        {
            case RequirementPackage.HIERARCHICAL_ELEMENT__NEXT_REQ_INDEX:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case RequirementPackage.HIERARCHICAL_ELEMENT__CHILDREN:
            case RequirementPackage.HIERARCHICAL_ELEMENT__REQUIREMENT:
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

        newChildDescriptors.add(createChildParameter(RequirementPackage.Literals.HIERARCHICAL_ELEMENT__CHILDREN, RequirementFactory.eINSTANCE.createHierarchicalElement()));

        newChildDescriptors.add(createChildParameter(RequirementPackage.Literals.HIERARCHICAL_ELEMENT__REQUIREMENT, RequirementFactory.eINSTANCE.createCurrentRequirement()));

        newChildDescriptors.add(createChildParameter(RequirementPackage.Literals.HIERARCHICAL_ELEMENT__REQUIREMENT, RequirementFactory.eINSTANCE.createAnonymousRequirement()));
    }

}
