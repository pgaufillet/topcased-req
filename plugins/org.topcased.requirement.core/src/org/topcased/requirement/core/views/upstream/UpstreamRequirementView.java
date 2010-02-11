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
package org.topcased.requirement.core.views.upstream;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.topcased.modeler.documentation.IDocPage;
import org.topcased.requirement.core.documentation.upstream.UpstreamDescPage;
import org.topcased.requirement.core.utils.RequirementCoverageComputer;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.AbstractRequirementView;
import org.topcased.requirement.core.views.current.CurrentRequirementView;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.RequirementProject;
import ttm.Requirement;

/**
 * Defines the upstream requirement view.<br>
 * 
 * Update : 17 March 2009<br>
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * 
 */
public class UpstreamRequirementView extends AbstractRequirementView implements ISelectionChangedListener
{
    public static final String VIEW_ID = "org.topcased.requirement.core.upstreamView"; //$NON-NLS-1$

    private UpstreamPage upstreamPage;

    /**
     * @see org.eclipse.ui.part.PageBookView#getAdapter(java.lang.Class)
     */
    @Override
    public Object getAdapter(Class type)
    {
        if (upstreamPage != null && type == IDocPage.class)
        {
            return new UpstreamDescPage(upstreamPage.getEditingDomain().getCommandStack());
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
            return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(UpstreamRequirementView.VIEW_ID);
        }
        return null;
    }

    /**
     * @see org.eclipse.ui.part.PageBookView#partActivated(org.eclipse.ui.IWorkbenchPart)
     */
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
                // reset the preference store
                RequirementHelper.INSTANCE.setUpstreamPage(upstreamPage);
            }
        }
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#hookListener()
     */
    public void hookListener()
    {
        IViewPart currentPart = getInstance();
        if (currentPart instanceof CurrentRequirementView)
        {
            ((CurrentRequirementView) currentPart).addSelectionChangedListener(this);
        }
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#unhookListener()
     */
    public void unhookListener()
    {
        IViewPart currentPart = getInstance();
        if (currentPart instanceof CurrentRequirementView)
        {
            ((CurrentRequirementView) currentPart).removeSelectionChangedListener(this);
        }
    }

    /**
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event)
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
                    setFocus();
                }
            }
        }
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementView#updatePage(org.eclipse.ui.part.IPage)
     */
    protected void updatePage(IPage page)
    {
        UpstreamPage thePage = (UpstreamPage) page;
        Resource resource = RequirementUtils.getRequirementModel(thePage.getEditingDomain());
        if (resource != null && resource.isLoaded())
        {
            RequirementProject project = (RequirementProject) RequirementUtils.getRoot(resource, RequirementProject.class);
            thePage.getViewer().setInput(project.getUpstreamModel());
            RequirementHelper.INSTANCE.setUpstreamPage(thePage);
            RequirementCoverageComputer.INSTANCE.refreshCoverageRateDisplay();
        }
    }
}