package org.topcased.requirement.search.ui.providers;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;

import org.topcased.requirement.provider.RequirementItemProviderAdapterFactory;

public class AllElementsItemProviderAdapterFactory extends ComposedAdapterFactory

{
    public AllElementsItemProviderAdapterFactory()
    {
        super(new AdapterFactory[] {new EcoreItemProviderAdapterFactory(),

        new RequirementItemProviderAdapterFactory(),

        new ResourceItemProviderAdapterFactory()});
    }
}