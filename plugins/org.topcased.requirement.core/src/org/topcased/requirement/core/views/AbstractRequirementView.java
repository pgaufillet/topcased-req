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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IContributedContentsView;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
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

    protected static ISelectionChangedListener selectionListener = null;

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
        IEditingDomainProvider provider;
        if (part instanceof IEditingDomainProvider)
        {
            provider = (IEditingDomainProvider) part;
        }
        else if (part.getAdapter(IEditingDomainProvider.class) instanceof IEditingDomainProvider)
        {
            provider = (IEditingDomainProvider) part.getAdapter(IEditingDomainProvider.class);
        }
        else
        {
            provider = (IEditingDomainProvider) RequirementUtils.getCurrentEditor().getAdapter(IEditingDomainProvider.class);
        }
        if (page != null && part != null && provider != null)
        {
            page.setEditingDomain(provider.getEditingDomain());
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
        if (part instanceof IEditorPart)
        {
            IEditorServices services = SupportingEditorsManager.getInstance().getServices((IEditorPart) part);
            if (services != null)
            {
                EditingDomain domain = services.getEditingDomain((IEditorPart) part);
                if (domain != null)
                {
                    IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(domain);
                    if (policy != null && policy.getLinkedTargetModel(domain.getResourceSet()) != null)
                    {
                        updatePage(page);
                    }
                    else
                    {
                        String extension = domain.getResourceSet().getResources().get(0).getURI().fileExtension();
                        String msg = NLS.bind(Messages.getString("ModelAttachmentPolicyManager.0"), extension);
                        RequirementCorePlugin.log(msg, Status.ERROR, null);//$NON-NLS-1$
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

    /**
     * Gets the preference store according to the resource loaded into the modeler.
     * 
     * @return the local (scoped to the IProject) or global preference store.
     */
    public static IPreferenceStore getPreferenceStore()
    {
        if (RequirementUtils.getCurrentEditor() != null)
        {
            IEditorInput input = RequirementUtils.getCurrentEditor().getEditorInput();
            IFile file = null;
            if (input instanceof FileEditorInput)
            {
                file = ((FileEditorInput) input).getFile();
            }
            if (file != null)
            {
                IProject project = file.getProject();
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
            }
        }
        return RequirementCorePlugin.getDefault().getPreferenceStore();
    }

    /**
     * @see org.eclipse.ui.part.PageBookView#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {
        super.createPartControl(parent);
        // initialize content from current editor
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        if (editor != null)
        {
            partActivated(editor);
        }
    }
}