/***********************************************************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe MERTZ (CS) - initial API and implementation
 *               Matthieu BOIVINEAU (AtoS) - documentation management updated
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views.upstream;

import org.eclipse.core.commands.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.documentation.upstream.UpstreamDescPage;
import org.topcased.requirement.core.handlers.ICommandConstants;
import org.topcased.requirement.core.utils.RequirementCoverageComputer;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.AbstractRequirementView;
import org.topcased.requirement.core.views.current.CurrentRequirementView;

import ttm.Requirement;

/**
 * Defines the upstream requirement view.<br>
 * 
 * Update : 16 April 2010<br>
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class UpstreamRequirementView extends AbstractRequirementView implements ISelectionChangedListener
{
    public static final String VIEW_ID = "org.topcased.requirement.core.upstreamView"; //$NON-NLS-1$

    private UpstreamPage upstreamPage;

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
    public Object getAdapter(Class type)
    {
        if (upstreamPage != null && type.equals(org.topcased.modeler.documentation.IDocPage.class))
        {
            return new UpstreamDescPage();
        }
        return super.getAdapter(type);
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#getEmptyPage()
     */
    @Override
    protected IPageBookViewPage getEmptyPage()
    {
        return new EmptyUpstreamPage();
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#getAdapterType()
     */
    @Override
    protected Class< ? > getAdapterType()
    {
        return IUpstreamRequirementPage.class;
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
            IViewReference ref = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findViewReference(UpstreamRequirementView.VIEW_ID);
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

        if (getCurrentPage() instanceof UpstreamPage)
        {
            upstreamPage = (UpstreamPage) getCurrentPage();

            // When the view is first opened, pass the selection to the page
            if (bootstrapSelection != null)
            {
                bootstrapSelection = null;
                loadPage(part, upstreamPage);
                hookListener();
            }
            else
            {
                RequirementHelper.INSTANCE.setUpstreamPage(upstreamPage);
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
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event)
    {
        if (UpstreamRequirementView.getInstance() != null)
        {
            ISelection selection = event.getSelection();
            if (selection instanceof IStructuredSelection)
            {
                IStructuredSelection structuredSelection = (IStructuredSelection) selection;
                Object element = structuredSelection.getFirstElement();
                if (element instanceof ObjectAttribute)
                {
                    EObject value = ((ObjectAttribute) element).getValue();
                    if (value != null && value instanceof Requirement)
                    {
                        getSelectionProvider().setSelection(new StructuredSelection(value));
                        // the view takes the focus
                        setFocus();
                    }
                }
            }
        }
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#updatePage(org.eclipse.ui.part.IPage)
     */
    @Override
    protected void updatePage(IPage page)
    {
        UpstreamPage thePage = (UpstreamPage) page;
        if (RequirementUtils.hasRequirementModel(thePage.getEditingDomain()))
        {
            RequirementProject project = RequirementUtils.getRequirementProject(thePage.getEditingDomain());
            thePage.getViewer().setInput(project.getUpstreamModel());
            RequirementHelper.INSTANCE.setUpstreamPage(thePage);
            RequirementCoverageComputer.INSTANCE.refreshCoverageRateDisplay();
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
            if ((CurrentRequirementView) CurrentRequirementView.getInstance() != null && selectionListener == null)
            {
                selectionListener = this;
                ((CurrentRequirementView) CurrentRequirementView.getInstance()).addSelectionChangedListener(selectionListener);
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

        if ((CurrentRequirementView) CurrentRequirementView.getInstance() != null && selectionListener != null)
        {
            ((CurrentRequirementView) CurrentRequirementView.getInstance()).removeSelectionChangedListener(selectionListener);
            selectionListener = null;
        }
    }
}