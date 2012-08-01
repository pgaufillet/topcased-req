/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.topcased.requirement.util.RequirementAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers. The adapters generated by this
 * factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}. The adapters
 * also support Eclipse property sheets. Note that most of the adapters are shared among multiple instances. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class RequirementItemProviderAdapterFactory extends RequirementAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable
{
    /**
     * This keeps track of the root adapter factory that delegates to this adapter factory. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    protected ComposedAdapterFactory parentAdapterFactory;

    /**
     * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    protected IChangeNotifier changeNotifier = new ChangeNotifier();

    /**
     * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected Collection<Object> supportedTypes = new ArrayList<Object>();

    /**
     * This constructs an instance. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public RequirementItemProviderAdapterFactory()
    {
        supportedTypes.add(IEditingDomainItemProvider.class);
        supportedTypes.add(IStructuredItemContentProvider.class);
        supportedTypes.add(ITreeItemContentProvider.class);
        supportedTypes.add(IItemLabelProvider.class);
        supportedTypes.add(IItemPropertySource.class);
        supportedTypes.add(IItemColorProvider.class);
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.RequirementProject} instances.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected RequirementProjectItemProvider requirementProjectItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.RequirementProject}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createRequirementProjectAdapter()
    {
        if (requirementProjectItemProvider == null)
        {
            requirementProjectItemProvider = new RequirementProjectItemProvider(this);
        }

        return requirementProjectItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.HierarchicalElement} instances.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected HierarchicalElementItemProvider hierarchicalElementItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.HierarchicalElement}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createHierarchicalElementAdapter()
    {
        if (hierarchicalElementItemProvider == null)
        {
            hierarchicalElementItemProvider = new HierarchicalElementItemProvider(this);
        }

        return hierarchicalElementItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.CurrentRequirement} instances.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected CurrentRequirementItemProvider currentRequirementItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.CurrentRequirement}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createCurrentRequirementAdapter()
    {
        if (currentRequirementItemProvider == null)
        {
            currentRequirementItemProvider = new CurrentRequirementItemProvider(this);
        }

        return currentRequirementItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.AttributeConfiguration}
     * instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected AttributeConfigurationItemProvider attributeConfigurationItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.AttributeConfiguration}. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createAttributeConfigurationAdapter()
    {
        if (attributeConfigurationItemProvider == null)
        {
            attributeConfigurationItemProvider = new AttributeConfigurationItemProvider(this);
        }

        return attributeConfigurationItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.ConfiguratedAttribute}
     * instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected ConfiguratedAttributeItemProvider configuratedAttributeItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.ConfiguratedAttribute}. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createConfiguratedAttributeAdapter()
    {
        if (configuratedAttributeItemProvider == null)
        {
            configuratedAttributeItemProvider = new ConfiguratedAttributeItemProvider(this);
        }

        return configuratedAttributeItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.AttributeValue} instances. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected AttributeValueItemProvider attributeValueItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.AttributeValue}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createAttributeValueAdapter()
    {
        if (attributeValueItemProvider == null)
        {
            attributeValueItemProvider = new AttributeValueItemProvider(this);
        }

        return attributeValueItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.DefaultAttributeValue}
     * instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected DefaultAttributeValueItemProvider defaultAttributeValueItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.DefaultAttributeValue}. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createDefaultAttributeValueAdapter()
    {
        if (defaultAttributeValueItemProvider == null)
        {
            defaultAttributeValueItemProvider = new DefaultAttributeValueItemProvider(this);
        }

        return defaultAttributeValueItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.TextAttribute} instances. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected TextAttributeItemProvider textAttributeItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.TextAttribute}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createTextAttributeAdapter()
    {
        if (textAttributeItemProvider == null)
        {
            textAttributeItemProvider = new TextAttributeItemProvider(this);
        }

        return textAttributeItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.ObjectAttribute} instances. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected ObjectAttributeItemProvider objectAttributeItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.ObjectAttribute}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createObjectAttributeAdapter()
    {
        if (objectAttributeItemProvider == null)
        {
            objectAttributeItemProvider = new ObjectAttributeItemProvider(this);
        }

        return objectAttributeItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.UpstreamModel} instances. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected UpstreamModelItemProvider upstreamModelItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.UpstreamModel}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createUpstreamModelAdapter()
    {
        if (upstreamModelItemProvider == null)
        {
            upstreamModelItemProvider = new UpstreamModelItemProvider(this);
        }

        return upstreamModelItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.AttributeLink} instances. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected AttributeLinkItemProvider attributeLinkItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.AttributeLink}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createAttributeLinkAdapter()
    {
        if (attributeLinkItemProvider == null)
        {
            attributeLinkItemProvider = new AttributeLinkItemProvider(this);
        }

        return attributeLinkItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.AttributeAllocate} instances.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected AttributeAllocateItemProvider attributeAllocateItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.AttributeAllocate}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createAttributeAllocateAdapter()
    {
        if (attributeAllocateItemProvider == null)
        {
            attributeAllocateItemProvider = new AttributeAllocateItemProvider(this);
        }

        return attributeAllocateItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.UntracedChapter} instances. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected UntracedChapterItemProvider untracedChapterItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.UntracedChapter}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createUntracedChapterAdapter()
    {
        if (untracedChapterItemProvider == null)
        {
            untracedChapterItemProvider = new UntracedChapterItemProvider(this);
        }

        return untracedChapterItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.ProblemChapter} instances. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected ProblemChapterItemProvider problemChapterItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.ProblemChapter}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createProblemChapterAdapter()
    {
        if (problemChapterItemProvider == null)
        {
            problemChapterItemProvider = new ProblemChapterItemProvider(this);
        }

        return problemChapterItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.TrashChapter} instances. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected TrashChapterItemProvider trashChapterItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.TrashChapter}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createTrashChapterAdapter()
    {
        if (trashChapterItemProvider == null)
        {
            trashChapterItemProvider = new TrashChapterItemProvider(this);
        }

        return trashChapterItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.AnonymousRequirement} instances.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected AnonymousRequirementItemProvider anonymousRequirementItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.AnonymousRequirement}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createAnonymousRequirementAdapter()
    {
        if (anonymousRequirementItemProvider == null)
        {
            anonymousRequirementItemProvider = new AnonymousRequirementItemProvider(this);
        }

        return anonymousRequirementItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all {@link org.topcased.requirement.DeletedChapter} instances. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected DeletedChapterItemProvider deletedChapterItemProvider;

    /**
     * This creates an adapter for a {@link org.topcased.requirement.DeletedChapter}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createDeletedChapterAdapter()
    {
        if (deletedChapterItemProvider == null)
        {
            deletedChapterItemProvider = new DeletedChapterItemProvider(this);
        }

        return deletedChapterItemProvider;
    }

    /**
     * This returns the root adapter factory that contains this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public ComposeableAdapterFactory getRootAdapterFactory()
    {
        return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
    }

    /**
     * This sets the composed adapter factory that contains this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory)
    {
        this.parentAdapterFactory = parentAdapterFactory;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object type)
    {
        return supportedTypes.contains(type) || super.isFactoryForType(type);
    }

    /**
     * This implementation substitutes the factory itself as the key for the adapter. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter adapt(Notifier notifier, Object type)
    {
        return super.adapt(notifier, this);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object adapt(Object object, Object type)
    {
        if (isFactoryForType(type))
        {
            Object adapter = super.adapt(object, type);
            if (!(type instanceof Class< ? >) || (((Class< ? >) type).isInstance(adapter)))
            {
                return adapter;
            }
        }

        return null;
    }

    /**
     * This adds a listener. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void addListener(INotifyChangedListener notifyChangedListener)
    {
        changeNotifier.addListener(notifyChangedListener);
    }

    /**
     * This removes a listener. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void removeListener(INotifyChangedListener notifyChangedListener)
    {
        changeNotifier.removeListener(notifyChangedListener);
    }

    /**
     * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public void fireNotifyChanged(Notification notification)
    {
        changeNotifier.fireNotifyChanged(notification);

        if (parentAdapterFactory != null)
        {
            parentAdapterFactory.fireNotifyChanged(notification);
        }
    }

    /**
     * This disposes all of the item providers created by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void dispose()
    {
        if (requirementProjectItemProvider != null)
            requirementProjectItemProvider.dispose();
        if (hierarchicalElementItemProvider != null)
            hierarchicalElementItemProvider.dispose();
        if (currentRequirementItemProvider != null)
            currentRequirementItemProvider.dispose();
        if (attributeConfigurationItemProvider != null)
            attributeConfigurationItemProvider.dispose();
        if (configuratedAttributeItemProvider != null)
            configuratedAttributeItemProvider.dispose();
        if (attributeValueItemProvider != null)
            attributeValueItemProvider.dispose();
        if (defaultAttributeValueItemProvider != null)
            defaultAttributeValueItemProvider.dispose();
        if (textAttributeItemProvider != null)
            textAttributeItemProvider.dispose();
        if (objectAttributeItemProvider != null)
            objectAttributeItemProvider.dispose();
        if (upstreamModelItemProvider != null)
            upstreamModelItemProvider.dispose();
        if (attributeLinkItemProvider != null)
            attributeLinkItemProvider.dispose();
        if (attributeAllocateItemProvider != null)
            attributeAllocateItemProvider.dispose();
        if (untracedChapterItemProvider != null)
            untracedChapterItemProvider.dispose();
        if (problemChapterItemProvider != null)
            problemChapterItemProvider.dispose();
        if (trashChapterItemProvider != null)
            trashChapterItemProvider.dispose();
        if (anonymousRequirementItemProvider != null)
            anonymousRequirementItemProvider.dispose();
        if (deletedChapterItemProvider != null)
            deletedChapterItemProvider.dispose();
    }

}
