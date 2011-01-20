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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.topcased.requirement.core.documentation.current.CurrentDescPage;
import org.topcased.requirement.core.documentation.current.PapyrusCurrentDescPage;
import org.topcased.requirement.core.handlers.ICommandConstants;
import org.topcased.requirement.core.properties.RequirementPropertySheetPage;
import org.topcased.requirement.core.utils.RequirementCoverageComputer;
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

        // activate the view context for key binding
        IContextService contextService = (IContextService) getSite().getService(IContextService.class);
        contextService.activateContext(VIEW_ID);
    }

    /**
     * @see org.eclipse.ui.part.PageBookView#getAdapter(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class key)
    {
        if (currentPage != null && key.equals(org.topcased.modeler.documentation.IDocPage.class))
        {
            return new CurrentDescPage();
        }
        else if (currentPage != null && key.equals(org.eclipse.papyrus.documentation.view.IDocPage.class))
        {
            return new PapyrusCurrentDescPage();
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
            IViewReference ref = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findViewReference(CurrentRequirementView.VIEW_ID);
            if (ref != null)
            {
                // We need to get the view without restoring it (prevent from the recursive view creation warning)
                return ref.getView(false);
            }
        }
        return null;
    }

    /**
     * @see org.eclipse.ui.part.PageBookView#partActivated(org.eclipse.ui.IWorkbenchPart)
     */
    @Override
    public void partActivated(IWorkbenchPart part)
    {
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

            // Update the IsImpacted variable as often as possible!
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

        if (part instanceof IEditorPart)
        {
            // We need to constantly set the value of the hasRequirement variable to synchronize toolbar actions
            // enablement with the requirement model state
            RequirementUtils.fireHasRequirementVariableChanged();
        }
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#updatePage(org.eclipse.ui.part.Page)
     */
    @Override
    protected void updatePage(IPage page)
    {
        CurrentPage thePage = (CurrentPage) page;
        if (RequirementUtils.hasRequirementModel(thePage.getEditingDomain()))
        {
            EObject model = RequirementUtils.getRequirementProject(thePage.getEditingDomain());
            thePage.setModel(model);
            thePage.getViewer().setInput(model);
            RequirementHelper.INSTANCE.setCurrentPage(thePage);
            RequirementCoverageComputer.INSTANCE.refreshNumberOfCurrentRequirementsDisplay();
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
            if ((UpstreamRequirementView) UpstreamRequirementView.getInstance() != null && selectionListener == null)
            {
                selectionListener = (UpstreamRequirementView) UpstreamRequirementView.getInstance();
                addSelectionChangedListener(selectionListener);
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

        if ((UpstreamRequirementView) UpstreamRequirementView.getInstance() != null && selectionListener != null)
        {
            removeSelectionChangedListener(selectionListener);
            selectionListener = null;
        }
    }

}
