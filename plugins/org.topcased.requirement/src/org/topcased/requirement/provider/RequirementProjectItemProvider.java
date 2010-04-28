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

import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.RequirementProject;


/**
 * This is the item provider adapter for a {@link org.topcased.requirement.RequirementProject} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class RequirementProjectItemProvider extends IdentifiedElementItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider,
        IItemPropertySource, IItemColorProvider
{
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequirementProjectItemProvider(AdapterFactory adapterFactory)
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

        }
        return itemPropertyDescriptors;
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
    public Collection< ? extends EStructuralFeature> getChildrenFeatures(Object object)
    {
        if (childrenFeatures == null)
        {
            super.getChildrenFeatures(object);
            childrenFeatures.add(RequirementPackage.Literals.REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT);
            childrenFeatures.add(RequirementPackage.Literals.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION);
            childrenFeatures.add(RequirementPackage.Literals.REQUIREMENT_PROJECT__CHAPTER);
            childrenFeatures.add(RequirementPackage.Literals.REQUIREMENT_PROJECT__UPSTREAM_MODEL);
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
     * This returns RequirementProject.gif.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object getImage(Object object)
    {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/RequirementProject"));
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object)
    {
        String label = ((RequirementProject) object).getIdentifier();
        return label == null || label.length() == 0 ? getString("_UI_RequirementProject_type") : getString("_UI_RequirementProject_type") + " " + label;
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

        switch (notification.getFeatureID(RequirementProject.class))
        {
            case RequirementPackage.REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT:
            case RequirementPackage.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION:
            case RequirementPackage.REQUIREMENT_PROJECT__CHAPTER:
            case RequirementPackage.REQUIREMENT_PROJECT__UPSTREAM_MODEL:
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

        newChildDescriptors.add(createChildParameter(RequirementPackage.Literals.REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT, RequirementFactory.eINSTANCE.createHierarchicalElement()));

        newChildDescriptors.add(createChildParameter(RequirementPackage.Literals.REQUIREMENT_PROJECT__ATTRIBUTE_CONFIGURATION, RequirementFactory.eINSTANCE.createAttributeConfiguration()));

        newChildDescriptors.add(createChildParameter(RequirementPackage.Literals.REQUIREMENT_PROJECT__CHAPTER, RequirementFactory.eINSTANCE.createUntracedChapter()));

        newChildDescriptors.add(createChildParameter(RequirementPackage.Literals.REQUIREMENT_PROJECT__CHAPTER, RequirementFactory.eINSTANCE.createProblemChapter()));

        newChildDescriptors.add(createChildParameter(RequirementPackage.Literals.REQUIREMENT_PROJECT__CHAPTER, RequirementFactory.eINSTANCE.createTrashChapter()));

        newChildDescriptors.add(createChildParameter(RequirementPackage.Literals.REQUIREMENT_PROJECT__UPSTREAM_MODEL, RequirementFactory.eINSTANCE.createUpstreamModel()));
    }

}
