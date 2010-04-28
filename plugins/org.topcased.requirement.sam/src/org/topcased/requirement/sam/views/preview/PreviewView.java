/*****************************************************************************
 * Copyright (c) 2008,2009 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Quentin Glineur (Obeo) <quentin.glineur@obeo.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.sam.views.preview;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.topcased.modeler.documentation.IDocPage;
import org.topcased.requirement.core.documentation.current.CurrentDescPage;
import org.topcased.requirement.core.views.AbstractRequirementView;
import org.topcased.requirement.core.views.current.CurrentPage;
import org.topcased.requirement.core.views.current.CurrentRequirementView;

/**
 * Defines the Document View.
 * 
 * @author <a href="mailto:quentin.glineur@obeo.fr">Quentin Glineur</a>
 * 
 */
public class PreviewView extends AbstractRequirementView implements ISelectionChangedListener
{
    public static final String VIEW_ID = "org.topcased.requirement.sam.preview"; //$NON-NLS-1$

    /** The current page */
    private PreviewPage previewPage;

    private CurrentRequirementView currentRequirementView;

    private EditingDomain editingDomain;

    /**
     * @see org.eclipse.ui.part.PageBookView#getAdapter(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class key)
    {
        if (previewPage != null && key == IDocPage.class)
        {
            return new CurrentDescPage(editingDomain.getCommandStack());
        }
        return super.getAdapter(key);
    }

    /**
     * @see org.topcased.sam.requirement.core.views.AbstractRequirementView#getAdapterType()
     */
    @Override
    protected Class< ? > getAdapterType()
    {
        return PreviewPage.class;
    }

    /**
     * @see org.topcased.sam.requirement.core.views.AbstractRequirementView#getEmptyPage()
     */
    @Override
    protected IPageBookViewPage getEmptyPage()
    {
        return new EmptyPreviewPage();
    }

    /**
     * @see org.topcased.sam.requirement.core.views.AbstractRequirementView#hookListener()
     */
    @Override
    protected void hookListener()
    {
        if (getCurrentRequirementView() != null)
        {
            getCurrentRequirementView().addSelectionChangedListener(this);
        }
        addSelectionChangedListener(this);
    }

    /**
     * @see org.topcased.sam.requirement.core.views.AbstractRequirementView#unhookListener()
     */
    @Override
    protected void unhookListener()
    {
        if (getCurrentRequirementView() != null)
        {
            getCurrentRequirementView().removeSelectionChangedListener(this);
        }
        removeSelectionChangedListener(this);        
    }

    /**
     * @see org.eclipse.ui.part.PageBookView#doCreatePage(org.eclipse.ui.IWorkbenchPart)
     */
    @Override
    protected PageRec doCreatePage(IWorkbenchPart part)
    {
        IPreviewPage page = (IPreviewPage) part.getAdapter(IPreviewPage.class);
        if (page != null && part != null && part instanceof IEditingDomainProvider)
        {
            editingDomain = ((IEditingDomainProvider) part).getEditingDomain();
            initPage(page);
            page.createControl(getPageBook());
            getBootstrapPart();
            return new PageRec(part, page);
        }
        return null;
    }

    /**
     * @see org.topcased.sam.requirement.core.views.AbstractRequirementView#partActivated(org.eclipse.ui.IWorkbenchPart)
     */
    @Override
    public void partActivated(IWorkbenchPart part)
    {
        super.partActivated(part);

        if (getCurrentPage() instanceof PreviewPage)
        {
            previewPage = (PreviewPage) getCurrentPage();

            if (bootstrapSelection != null)
            {
                bootstrapSelection = null;
                hookListener();
            }
        }
    }

    /**
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event)
    {
        Object source = event.getSource();
        IPage currentRequirementViewCurrentPage = getCurrentRequirementView().getCurrentPage();
        if (currentRequirementViewCurrentPage instanceof CurrentPage)
        {
            CurrentPage currentPage = (CurrentPage) currentRequirementViewCurrentPage;
            if (source != currentPage.getViewer())
            {
                ISelection selection = event.getSelection();
                if (selection instanceof IStructuredSelection)
                {
                    IStructuredSelection structuredSelection = (IStructuredSelection) selection;
                    Object element = structuredSelection.getFirstElement();
                    if (element instanceof EObject)
                    {
                        getCurrentRequirementView().setSelection(structuredSelection);
                    }
                }
            }
        }
        getSelectionProvider().setSelection(event.getSelection());
    }

    /**
     * Gets the reference of the current requirement view.
     * 
     * @return The Current Requirement View
     */
    private CurrentRequirementView getCurrentRequirementView()
    {
        if (currentRequirementView == null)
        {
            currentRequirementView = (CurrentRequirementView) CurrentRequirementView.getInstance();
        }
        return currentRequirementView;
    }
}
