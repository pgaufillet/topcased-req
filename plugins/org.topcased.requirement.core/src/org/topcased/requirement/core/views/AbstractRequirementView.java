/***********************************************************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe MERTZ (CS) - initial API and implementation
 *               Maxime AUDRAIN (CS) - API Changes
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views;

import org.eclipse.core.commands.AbstractHandlerWithState;
import org.eclipse.core.commands.Command;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.part.IContributedContentsView;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.topcased.modeler.editor.Modeler;
import org.topcased.requirement.core.dnd.RequirementDropListener;
import org.topcased.requirement.core.extensions.DefaultAttachmentPolicy;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.handlers.ICommandIds;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.views.current.CurrentPage;
import org.topcased.requirement.core.views.upstream.UpstreamPage;

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

    /** flag indicating if the drop adpater is installed or not */
    public static boolean dropListenerInstalled = false;

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
        pageRecord.page.dispose();
        pageRecord.dispose();
        dropListenerInstalled = false;
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
                if (policy != null && policy.getLinkedTargetModel(modeler.getEditingDomain().getResourceSet()) != null)
                {
                    initializePage(modeler, page);
                }
                else if (DefaultAttachmentPolicy.getInstance().getLinkedTargetModel(modeler.getEditingDomain().getResourceSet()) != null)
                {
                    initializePage(modeler, page);
                }
            }
        }
    }

    protected void initializePage(Modeler modeler, IPage page)
    {
        if (dropListenerInstalled == false)
        {
            dropListenerInstalled = true;
            modeler.getGraphicalViewer().addDropTargetListener(new RequirementDropListener(modeler.getGraphicalViewer()));
        }
        
        updatePage(page);
        
        restoreCommandsPreferencesFromLastSession(page);
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

    /**
     * Gets the preference store according to the resource loaded into the modeler.
     * 
     * @return the local (scoped to the IProject) or global preference store.
     */
    public static IPreferenceStore getPreferenceStore()
    {
        IProject project = Modeler.getCurrentIFile().getProject();
        if (project != null)
        {
            Preferences root = Platform.getPreferencesService().getRootNode();
            try
            {
                if (root.node(ProjectScope.SCOPE).node(project.getName()).nodeExists(RequirementCorePlugin.getId()))
                {
                    return new ScopedPreferenceStore(new ProjectScope(project), RequirementCorePlugin.getId());
                }
            }
            catch (BackingStoreException e)
            {
                RequirementCorePlugin.log(e);
            }
        }
        return RequirementCorePlugin.getDefault().getPreferenceStore();
    }
    
    /**
     * Provide state action restoring from previous session. 
     * Check the saved state of commands and launch the corresponding action
     * 
     * @param page the page where there is commands
     */
    private void restoreCommandsPreferencesFromLastSession(IPage page)
    {
        //Get the commands who have a registered state
        ICommandService cs = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
        Command linkCmd = cs.getCommand(ICommandIds.LINK_WITH_EDITOR_ID);
        Command sortCmd = cs.getCommand(ICommandIds.SORT_ID);
        Command flatCmd = cs.getCommand(ICommandIds.FLAT_ID);
        Command hierarchicalCmd = cs.getCommand(ICommandIds.HIERARCHICAL_ID);
        
        //Handle cases when the command is toggled but the associated action isn't performed
        if (linkCmd.getState(RegistryToggleState.STATE_ID).getValue().equals(true))
        {
            if (((AbstractHandlerWithState)linkCmd.getHandler()) != null && page instanceof CurrentPage)
            {       
                ((AbstractHandlerWithState)linkCmd.getHandler()).handleStateChange(linkCmd.getState(RegistryToggleState.STATE_ID), false);
            }
        }

      //Handle cases when the command is toggled but the associated action isn't performed
        if (((AbstractHandlerWithState)sortCmd.getHandler()) != null && page instanceof UpstreamPage)
        { 
            ((AbstractHandlerWithState)sortCmd.getHandler()).handleStateChange(sortCmd.getState(RegistryToggleState.STATE_ID), false);
        }
        
        //Handle cases when the command is toggled but the associated action isn't performed
        if (flatCmd.getState(RegistryToggleState.STATE_ID).getValue().equals(true))
        {
            if (((AbstractHandlerWithState)flatCmd.getHandler()) != null && page instanceof UpstreamPage)
            {       
                ((AbstractHandlerWithState)flatCmd.getHandler()).handleStateChange(flatCmd.getState(RegistryToggleState.STATE_ID), false);
            }
        }
        else if (hierarchicalCmd.getState(RegistryToggleState.STATE_ID).getValue().equals(true))
        {
            if (((AbstractHandlerWithState)hierarchicalCmd.getHandler()) != null && page instanceof UpstreamPage)
            {       
                ((AbstractHandlerWithState)hierarchicalCmd.getHandler()).handleStateChange(hierarchicalCmd.getState(RegistryToggleState.STATE_ID), false);
            }
        }
    }
}