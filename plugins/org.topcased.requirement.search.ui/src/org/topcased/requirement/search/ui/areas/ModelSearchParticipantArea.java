package org.topcased.requirement.search.ui.areas;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.search.ui.areas.AbstractModelSearchParticipantArea;
import org.eclipse.emf.search.ui.pages.AbstractModelSearchPage;
import org.eclipse.emf.search.ui.providers.AbstractMetaModelParticipantsItemProvider;
import org.eclipse.emf.search.utils.ModelSearchImagesUtil;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.RequirementPlugin;
import org.topcased.requirement.provider.RequirementItemProviderAdapterFactory;
import org.topcased.requirement.search.ui.Activator;
import org.topcased.requirement.search.ui.providers.MetaModelParticipantsItemProvider;


public final class ModelSearchParticipantArea extends AbstractModelSearchParticipantArea
{

    private String nsURI;

    public ModelSearchParticipantArea(Composite parent, AbstractModelSearchPage page, int style)
    {
        super(parent, page, style);
        createContent();
    }

    public ModelSearchParticipantArea(Composite parent, AbstractModelSearchPage page, int style, String nsURI)
    {
        super(parent, page, style);
        this.nsURI = nsURI;
        createContent();
    }

    @Override
    protected boolean getDefaultParticpantsControlEnabling()
    {
        return false;
    }

    @Override
    public List<AdapterFactory> getMetaElementComposeableAdapterFactories(String nsURI)
    {
        return getMetaElementComposeableAdapterFactories();
    }

    @Override
    public List<AdapterFactory> getMetaElementComposeableAdapterFactories()
    {
        List<AdapterFactory> composeableAdapterFactories = new ArrayList<AdapterFactory>();
        composeableAdapterFactories.add(new EcoreItemProviderAdapterFactory());

        if (RequirementPackage.eNS_URI.equals(nsURI))
        {
            composeableAdapterFactories.add(new RequirementItemProviderAdapterFactory());
        }

        if (nsURI == null || "".equals(nsURI))
        {

            composeableAdapterFactories.add(new RequirementItemProviderAdapterFactory());

        }
        composeableAdapterFactories.add(new ResourceItemProviderAdapterFactory());
        return composeableAdapterFactories;
    }

    @Override
    public AbstractMetaModelParticipantsItemProvider getMetaModelParticipantsItemProvider()
    {
        return new MetaModelParticipantsItemProvider(getTargetMetaModelIDs());
    }

    @Override
    protected Collection<EPackage> getTargetModelPackages()
    {

        if (RequirementPackage.eNS_URI.equals(nsURI))
        {
            return Arrays.asList(new EPackage[] {RequirementPackage.eINSTANCE});
        }

        return Arrays.asList(new EPackage[] {

        RequirementPackage.eINSTANCE

        });
    }

    public Collection<String> getTargetMetaModelIDs()
    {

        if (RequirementPackage.eNS_URI.equals(nsURI))
        {
            return Arrays.asList(new String[] {nsURI});
        }

        return Arrays.asList(new String[] {

        RequirementPackage.eNS_URI

        });
    }

    @Override
    protected String getTargetModelElementText(Object object)
    {
        return object instanceof ENamedElement ? ((ENamedElement) object).getName() : null;
    }

    @Override
    protected Image getTargetModelElementImage(Object object)
    {
        try
        {
            if (object instanceof ENamedElement)
            {
                String imagePath = "/icons/full/obj16/" + computeElementImageName(((ENamedElement) object).getName()) + ".gif"; //$NON-NLS-1$  //$NON-NLS-2$
                URL url = FileLocator.find(RequirementPlugin.getPlugin().getBundle(), new Path(imagePath), null);
                if (url != null)
                {
                    return ModelSearchImagesUtil.getImage(url);
                }
            }
        }
        catch (Throwable t)
        {
            Activator.getDefault().getLog().log(
                    new Status(Status.ERROR, Activator.PLUGIN_ID,
                            "Error while attempmting to retrieve image from edit" + RequirementPlugin.getPlugin().getBundle() + " bundle")); //$NON-NLS-1$  //$NON-NLS-2$
        }
        return null;
    }

    private String computeElementImageName(String name)
    {
        return name;
    }
}
