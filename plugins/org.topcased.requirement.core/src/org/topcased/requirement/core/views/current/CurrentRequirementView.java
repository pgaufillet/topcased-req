/***********************************************************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe MERTZ (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views.current;

import org.eclipse.core.commands.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.topcased.modeler.documentation.IDocPage;
import org.topcased.modeler.editor.Modeler;
import org.topcased.requirement.core.documentation.current.CurrentDescPage;
import org.topcased.requirement.core.handlers.ICommandConstants;
import org.topcased.requirement.core.properties.RequirementPropertySheetPage;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.AbstractRequirementView;
import org.topcased.requirement.core.views.upstream.UpstreamRequirementView;

/**
 * Defines the current requirement view.<br>
 * 
 * Update : 16 April 2010<br>
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class CurrentRequirementView extends AbstractRequirementView implements ITabbedPropertySheetPageContributor
{
    public static final String VIEW_ID = "org.topcased.requirement.core.currentView"; //$NON-NLS-1$

    /** The current page */
    private CurrentPage currentPage;

    /** PropertySheet page */
    private RequirementPropertySheetPage mPropertySheetPage;

    /**
     * @see org.eclipse.ui.part.PageBookView#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {        
        super.createPartControl(parent);
        
        //activate the view context for key binding
        IContextService contextService = (IContextService) getSite().getService(IContextService.class);
        contextService.activateContext(VIEW_ID);
    }
    
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
        return "org.topcased.requirement.core.contributor"; //$NON-NLS-1$
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
        super.partActivated(part);

        if (getCurrentPage() instanceof CurrentPage)
        {
            currentPage = (CurrentPage) getCurrentPage();

            // When the view is first opened, pass the selection to the page
            if (bootstrapSelection != null)
            {
                //ATTENTION : here, there is no need to call the hookListener method: 
                //Now the selection listener is installed in the linkToUpstreamHandler
                //This listener is no more a default listener, it is installed/uninstalled by the user via a command
                bootstrapSelection = null;
                loadPage(part, currentPage);
            }
            else
            {
                RequirementHelper.INSTANCE.setCurrentPage(currentPage);
            }

            //Update the IsImpacted variable as often as possible!
            RequirementUtils.fireIsImpactedVariableChanged();
        }

        // We need to constantly set the value of the hasRequirement variable to synchronize toolbar actions
        // enablement with the requirement model state
        RequirementUtils.fireHasRequirementVariableChanged();
    }

    /**
     * @see org.eclipse.ui.part.PageBookView#partClosed(org.eclipse.ui.IWorkbenchPart)
     */
    @Override
    public void partClosed(IWorkbenchPart part)
    {
        super.partClosed(part);
        
        if (part instanceof Modeler)
        {
            // We need to constantly set the value of the hasRequirement variable to synchronize toolbar actions
            // enablement with the requirement model state
            RequirementUtils.fireHasRequirementVariableChanged();
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
            RequirementHelper.INSTANCE.setCurrentPage(thePage);
            thePage.refreshViewer(true);
        }
    }

    /**
     * @see org.eclipse.ui.part.PageBookView#dispose()
     */
    @Override
    public void dispose()
    {
        unhookListener();
        
        super.dispose();
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#hookListener()
     */
    @Override
    public void hookListener()
    {
        super.hookListener();

        // Get the commands who have a registered state
        ICommandService cs = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
        Command linkCmd = cs.getCommand(ICommandConstants.LINK_TO_UPSTREAM_ID);
        
        if (linkCmd.getState(RegistryToggleState.STATE_ID).getValue().equals(true))
        {
            if (((UpstreamRequirementView)UpstreamRequirementView.getInstance()) != null)
            {
                this.addSelectionChangedListener(((UpstreamRequirementView)UpstreamRequirementView.getInstance()));
            }
        }
    }
    
    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#unhookListener()
     */
    @Override
    public void unhookListener()
    {
        super.unhookListener();
        
        if (((UpstreamRequirementView)UpstreamRequirementView.getInstance()) != null)
        {
            this.removeSelectionChangedListener(((UpstreamRequirementView)UpstreamRequirementView.getInstance()));
        }
    }

}
