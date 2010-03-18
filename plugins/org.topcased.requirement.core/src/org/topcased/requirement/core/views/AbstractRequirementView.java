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
package org.topcased.requirement.core.views;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IContributedContentsView;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.topcased.modeler.editor.Modeler;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.dnd.RequirementDropListener;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.IRequirementIdentifierDefinition;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.extensions.RequirementIdentifierDefinitionManager;
import org.topcased.requirement.core.preferences.NamingRequirementPreferenceHelper;
import org.topcased.requirement.core.utils.DefaultAttachmentPolicy;
import org.topcased.requirement.core.utils.DefaultRequirementIdentifierDefinition;
import org.topcased.requirement.core.utils.RequirementUtils;


/**
 * Defines the abstract requirement view.<br>
 * 
 * Update : 17 March 2009<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe MERTZ</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public abstract class AbstractRequirementView extends PageBookView implements ISelectionProvider
{
    /** The initial selection when the view opens */
    protected ISelection bootstrapSelection;

    /**
     * Gets the default empty page for this view.
     * 
     * @return The empty page of the view
     */
    protected abstract IPageBookViewPage getEmptyPage();

    /**
     * Gets the class type to adapt.
     * 
     * @return the class
     */
    protected abstract Class< ? > getAdapterType();

    /**
     * Updates the page content
     * 
     * @param page The current page
     */
    protected void updatePage(IPage page)
    {
        // provide an empty implementation
    }
    
    /**
     * Installs the change listener when the view is created.
     */
    protected void hookListener()
    {
        // provide an empty implementation
    }
    
    /**
     * Removes the change listener when the view is disposed.
     */
    protected void unhookListener()
    {
        // provide an empty implementation
    }

    /**
     * @see org.eclipse.ui.part.PageBookView#getBootstrapPart()
     */
    protected IWorkbenchPart getBootstrapPart()
    {
        IWorkbenchPage page = getSite().getPage();
        if (page != null)
        {
            bootstrapSelection = page.getSelection();
            return page.getActivePart();
        }
        return null;
    }
    
    /**
     * @see org.eclipse.ui.part.PageBookView#createDefaultPage(org.eclipse.ui.part.PageBook)
     */
    public IPage createDefaultPage(PageBook book)
    {
        IPageBookViewPage defaultPage = getEmptyPage();
        initPage(defaultPage);
        defaultPage.createControl(book);
        return defaultPage;
    }

    /**
     * @see org.eclipse.ui.part.PageBookView#doCreatePage(org.eclipse.ui.IWorkbenchPart)
     */
    protected PageRec doCreatePage(IWorkbenchPart part)
    {
        AbstractRequirementPage page = (AbstractRequirementPage) part.getAdapter(getAdapterType());
        if (page != null && part != null && part instanceof IEditingDomainProvider)
        {
            page.setEditingDomain(((IEditingDomainProvider) part).getEditingDomain());
            initPage(page);
            page.createControl(getPageBook());
            getBootstrapPart();
            return new PageRec(part, page);
        }
        return null;
    }

    /**
     * @see org.eclipse.ui.part.PageBookView#partActivated(org.eclipse.ui.IWorkbenchPart)
     */
    @Override
    public void partActivated(IWorkbenchPart part)
    {
        IContributedContentsView view = (IContributedContentsView) part.getAdapter(IContributedContentsView.class);
        IWorkbenchPart source = null;
        if (view != null)
        {
            source = view.getContributingPart();
        }
        if (source != null)
        {
            super.partActivated(source);
        }
        else
        {
            super.partActivated(part);
        }
    }

    /**
     * @see org.eclipse.ui.part.PageBookView#doDestroyPage(org.eclipse.ui.IWorkbenchPart,
     *      org.eclipse.ui.part.PageBookView.PageRec)
     */
    protected void doDestroyPage(IWorkbenchPart part, PageRec pageRecord)
    {
        RequirementCorePlugin.setCreateDropListener(true);
        NamingRequirementPreferenceHelper.KEY_WORDS.clear();
        pageRecord.page.dispose();
        pageRecord.dispose();
       
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
     * @see org.eclipse.ui.part.PageBookView#isImportant(org.eclipse.ui.IWorkbenchPart)
     */
    public boolean isImportant(IWorkbenchPart part)
    {
        return part instanceof IEditorPart;
    }

    /**
     * Load the requirement model
     * 
     * @param IWorkbenchPart part
     * @param UpstreamPage page
     */
    protected void loadPage(IWorkbenchPart part, IPage page)
    {
        if (part instanceof Modeler)
        {
            Modeler modeler = (Modeler) part;
            // Bug 1970 : in case where the model is exported and contain no diagram. See
            if (modeler.getActiveDiagram() != null)
            {               
                IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(modeler.getEditingDomain());
                if (policy != null)
                {
                    Resource targetModel = policy.getLinkedTargetModel(modeler.getEditingDomain().getResourceSet());
                    if (targetModel != null)
                    {
                        RequirementUtils.loadRequirementModel(targetModel.getURI(), modeler.getEditingDomain());
                        if (RequirementCorePlugin.getCreateDropListener() == true)
                        {
                            RequirementCorePlugin.setCreateDropListener(false);
                            modeler.getGraphicalViewer().addDropTargetListener(new RequirementDropListener(modeler.getGraphicalViewer()));
                            IRequirementIdentifierDefinition definition = RequirementIdentifierDefinitionManager.getInstance().getIdentifierDefinition(modeler.getEditingDomain());
                            NamingRequirementPreferenceHelper.addKeyWord(DefaultRequirementIdentifierDefinition.getInstance().addPatterns());
                            if (definition != null)
                            {
                                NamingRequirementPreferenceHelper.addKeyWord(definition.addPatterns());
                            }
                        }
                        updatePage(page);
                    }
                }
                else
                {
                    if ( DefaultAttachmentPolicy.getInstance().getLinkedTargetModel(modeler.getEditingDomain().getResourceSet()) != null)
                    {
                        if (RequirementCorePlugin.getCreateDropListener() == true)
                        {
                            RequirementCorePlugin.setCreateDropListener(false);
                            modeler.getGraphicalViewer().addDropTargetListener(new RequirementDropListener(modeler.getGraphicalViewer()));
                            IRequirementIdentifierDefinition definition = RequirementIdentifierDefinitionManager.getInstance().getIdentifierDefinition(modeler.getEditingDomain());
                            NamingRequirementPreferenceHelper.addKeyWord(DefaultRequirementIdentifierDefinition.getInstance().addPatterns());
                            if (definition != null)
                            {
                                NamingRequirementPreferenceHelper.addKeyWord(definition.addPatterns());
                            }
                        }
                        updatePage(page);
                    }
                }
            }
        }
    }

    /**
     * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener)
    {
        getSelectionProvider().addSelectionChangedListener(listener);
    }

    /**
     * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
     */
    public ISelection getSelection()
    {
        return getSelectionProvider().getSelection();
    }

    /**
     * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    public void removeSelectionChangedListener(ISelectionChangedListener listener)
    {
        getSelectionProvider().removeSelectionChangedListener(listener);
    }

    /**
     * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
     */
    public void setSelection(ISelection selection)
    {
        getSelectionProvider().setSelection(selection);
    }
}