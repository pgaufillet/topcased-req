/***********************************************************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe MERTZ (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views.current;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.services.ISourceProviderService;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.topcased.modeler.documentation.IDocPage;
import org.topcased.requirement.core.documentation.current.CurrentDescPage;
import org.topcased.requirement.core.properties.RequirementPropertySheetPage;
import org.topcased.requirement.core.services.RequirementModelSourceProvider;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.AbstractRequirementView;
import org.topcased.requirement.core.views.upstream.UpstreamRequirementView;

/**
 * Defines the current requirement view.<br>
 * 
 * Update : 17 March 2009<br>
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class CurrentRequirementView extends AbstractRequirementView implements ITabbedPropertySheetPageContributor
{
    public static final String VIEW_ID = "org.topcased.requirement.core.currentView"; //$NON-NLS-1$

    /** The current page */
    private CurrentPage currentPage;

    /** PropertySheet page */
    private RequirementPropertySheetPage mPropertySheetPage;

    /**
     * @see org.eclipse.ui.part.PageBookView#getAdapter(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class key)
    {
        if (currentPage != null && key == IDocPage.class)
        {
            return new CurrentDescPage(currentPage.getEditingDomain().getCommandStack());
        }
        else if (key.equals(IPropertySheetPage.class))
        {
            if (mPropertySheetPage == null)
            {
                mPropertySheetPage = new RequirementPropertySheetPage(this);
            }
            return mPropertySheetPage;
        }
        else
        {
            return super.getAdapter(key);
        }
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#getEmptyPage()
     */
    @Override
    protected IPageBookViewPage getEmptyPage()
    {
        return new EmptyCurrentPage();
    }

    /**
     * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor#getContributorId()
     */
    public String getContributorId()
    {
        return "org.topcased.requirement.core.contributor";
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#getAdapterType()
     */
    @Override
    protected Class< ? > getAdapterType()
    {
        return ICurrentRequirementPage.class;
    }

    /**
     * Returns the instance of this view
     * 
     * @return The view instance
     */
    public static IViewPart getInstance()
    {
        if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null)
        {
            return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(CurrentRequirementView.VIEW_ID);
        }
        return null;
    }

    /**
     * @see org.eclipse.ui.part.PageBookView#partActivated(org.eclipse.ui.IWorkbenchPart)
     */
    public void partActivated(IWorkbenchPart part)
    {
        ISourceProviderService service = (ISourceProviderService)PlatformUI.getWorkbench().getService(ISourceProviderService.class);
        RequirementModelSourceProvider provider = (RequirementModelSourceProvider)service.getSourceProvider(RequirementModelSourceProvider.HAS_REQUIREMENT_MODEL);
        
        super.partActivated(part);

        if (getCurrentPage() instanceof CurrentPage)
        {
            currentPage = (CurrentPage) getCurrentPage();

            // When the view is first opened, pass the selection to the page
            if (bootstrapSelection != null)
            {
                bootstrapSelection = null;
                loadPage(part, currentPage);
                hookListener();
            }
            else
            {
                RequirementHelper.INSTANCE.setCurrentPage(currentPage);
            }
            
            if (RequirementUtils.getRequirementProject(currentPage.getEditingDomain()) == null)
            {
                provider.setHasRequirementState(false);  
            }
            else
            {                
                provider.setHasRequirementState(true);  
            }
        }
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#updatePage(org.eclipse.ui.part.Page)
     */
    protected void updatePage(IPage page)
    {
        CurrentPage thePage = (CurrentPage) page;
        Resource resource = RequirementUtils.getRequirementModel(thePage.getEditingDomain());
        if (resource != null && resource.isLoaded())
        {
            EObject model = resource.getContents().get(0);
            thePage.setModel(model);
            thePage.getViewer().setInput(model);
        }
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#hookListener()
     */
    public void hookListener()
    {
        IViewPart upstreamPart = UpstreamRequirementView.getInstance();
        if (upstreamPart instanceof UpstreamRequirementView)
        {
            addSelectionChangedListener((UpstreamRequirementView) upstreamPart);
        }
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#unhookListener()
     */
    public void unhookListener()
    {
        IViewPart upstreamPart = UpstreamRequirementView.getInstance();
        if (upstreamPart instanceof UpstreamRequirementView)
        {
            removeSelectionChangedListener((UpstreamRequirementView) upstreamPart);
        }
    }
}
