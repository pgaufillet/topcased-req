/***********************************************************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe MERTZ (CS) - initial API and implementation,
 * Vincent Hemery [(Atos Origin)] [vincent.hemery@atosorigin.com] - updating of CurrentSelectionChangeListener
 * Maxime AUDRAIN (CS) - API Changes
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views.current;

import java.util.Iterator;

import org.eclipse.core.commands.Command;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.markers.MarkerItem;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.dnd.DragSourceCurrentAdapter;
import org.topcased.requirement.core.dnd.DropTargetCurrentAdapter;
import org.topcased.requirement.core.dnd.RequirementTransfer;
import org.topcased.requirement.core.filters.CurrentRequirementFilter;
import org.topcased.requirement.core.filters.CurrentViewSingletonFilter;
import org.topcased.requirement.core.handlers.ICommandConstants;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.listeners.RequirementDoubleClickListener;
import org.topcased.requirement.core.listeners.UpstreamSelectionChangedListener;
import org.topcased.requirement.core.providers.CurrentRequirementContentProvider;
import org.topcased.requirement.core.providers.CurrentRequirementLabelProvider;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.AbstractRequirementPage;
import org.topcased.requirement.core.views.SearchComposite;
import org.topcased.requirement.core.views.upstream.UpstreamPage;
import org.topcased.requirement.core.views.upstream.UpstreamRequirementView;

/**
 * This class creates the page to edit a requirement model in the upstream requirement view
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class CurrentPage extends AbstractRequirementPage implements ICurrentRequirementPage
{
    private EObject model;

    private IStructuredSelection selection;

    private MenuManager menuManager;
    
    static final String CURRENT_POPUP_ID = "org.topcased.requirement.core.views.current.popupMenu"; //$NON-NLS-1$
    
    /**
     * FIXME : find a better way to adapt the focus when an element is deleted
     * 
     * This class manages the change selection in the current requirement view
     * 
     * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
     * 
     */
    private class CurrentSelectionChangeListener implements ISelectionChangedListener
    {
        private HierarchicalElement hierarchicalElementToFocusAfterRequirementDeletion = null;

        private HierarchicalElement hierarchicalElementToFocusAfterHierarchicalElementDeletion = null;

        private Requirement previouslySelectedRequirement = null;

        private HierarchicalElement previouslySelectedHierarchicalElement = null;

        /**
         * Re-direct selection if necessary and update selection actions.
         * 
         * @param event the selection change event
         */
        public void selectionChanged(SelectionChangedEvent event)
        {
            if (handleAutomaticReselection(event))
            {
                // Attributes will be reseted in new selection change.
                return;
            }
            // Reset attributes and handle new selection.
            previouslySelectedRequirement = null;
            previouslySelectedHierarchicalElement = null;
            hierarchicalElementToFocusAfterRequirementDeletion = null;
            hierarchicalElementToFocusAfterHierarchicalElementDeletion = null;

            if (!event.getSelection().isEmpty() && event.getSelection() instanceof IStructuredSelection)
            {
                selection = (IStructuredSelection) event.getSelection();
                if (selection.getFirstElement() instanceof Requirement)
                {
                    // If we select a requirement we save its container to be able to focus it if the requirement is
                    // deleted
                    previouslySelectedRequirement = (Requirement) selection.getFirstElement();
                    if (previouslySelectedRequirement.eContainer() != null && previouslySelectedRequirement.eContainer() instanceof HierarchicalElement)
                    {
                        hierarchicalElementToFocusAfterRequirementDeletion = (HierarchicalElement) previouslySelectedRequirement.eContainer();
                    }
                }
                else if (selection.getFirstElement() instanceof HierarchicalElement)
                {
                    // If we select a hierarchical element we save its container to be able to focus it if this
                    // hierarchical element is deleted
                    previouslySelectedHierarchicalElement = (HierarchicalElement) selection.getFirstElement();
                    if (previouslySelectedHierarchicalElement.eContainer() != null && !(previouslySelectedHierarchicalElement.eContainer() instanceof RequirementProject)
                            && (previouslySelectedHierarchicalElement.eContainer() instanceof HierarchicalElement))
                    {
                        hierarchicalElementToFocusAfterHierarchicalElementDeletion = (HierarchicalElement) previouslySelectedHierarchicalElement.eContainer();
                    }
                }
            }
        }

        /**
         * Automatically reselect a new element if necessary
         * 
         * @param event the initial selection event
         * @return true if a new selection has been set.
         */
        private boolean handleAutomaticReselection(SelectionChangedEvent event)
        {
            // Handle case when selected requirement has been deleted
            boolean previousRequirementDeleted = previouslySelectedRequirement != null && !stillExistInModel(previouslySelectedRequirement);
            // Handle case when selected hierarchical element has been deleted
            boolean previousHierarchicalElementDeleted = previouslySelectedHierarchicalElement != null && !stillExistInModel(previouslySelectedHierarchicalElement);

            // Is the parent hierarchical element available?
            boolean parentToFocusAvailableAfterRequirementDeletion = hierarchicalElementToFocusAfterRequirementDeletion != null
                    && stillExistInModel(hierarchicalElementToFocusAfterRequirementDeletion);
            boolean parentToFocusAvailableAfterHierarchicalElementDeletion = hierarchicalElementToFocusAfterHierarchicalElementDeletion != null
                    && stillExistInModel(hierarchicalElementToFocusAfterHierarchicalElementDeletion);

            if (event.getSelection().isEmpty())
            {
                if (previousRequirementDeleted && parentToFocusAvailableAfterRequirementDeletion)
                {
                    if (!(hierarchicalElementToFocusAfterRequirementDeletion.getRequirement().isEmpty()))
                    {
                        // When a requirement is deleted from this page, we set focus on its container.
                        CurrentPage.this.getViewer().setSelection(new StructuredSelection(hierarchicalElementToFocusAfterRequirementDeletion));
                        // New selection will not produce another re-selection since it is not empty (Infinite loop
                        // avoided).
                        return true;
                    }
                }
                else if (previousHierarchicalElementDeleted && parentToFocusAvailableAfterHierarchicalElementDeletion)
                {
                    CurrentPage.this.getViewer().setSelection(new StructuredSelection(hierarchicalElementToFocusAfterHierarchicalElementDeletion));
                    return true;
                }
            }
            return false;
        }

        /**
         * Check if object exists in the model
         * 
         * @param eObjectToCheck the object
         * @return true if object is present in the model
         */
        private boolean stillExistInModel(EObject eObjectToCheck)
        {
            TreeIterator<EObject> allContents = CurrentPage.this.getModel().eAllContents();
            while (allContents.hasNext())
            {
                EObject object = (EObject) allContents.next();
                if (object.equals(eObjectToCheck))
                {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * This class create the default menu of the current requirement view
     * 
     * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
     * 
     */
    private class CurrentMenuManager implements IMenuListener
    {
        public void menuAboutToShow(IMenuManager manager)
        {
            if (model != null)
            {
                //add a first separator to surround undo & redo actions
                Separator first = new Separator("firstSeparator"); //$NON-NLS-1$
                first.setVisible(true);
                manager.add(first);
                
                UndoAction undoAction = new UndoAction(editingDomain);
                undoAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
                undoAction.setActionDefinitionId("org.topcased.requirement.core.undo"); //$NON-NLS-1$
                manager.add(undoAction);

                RedoAction redoAction = new RedoAction(editingDomain);
                redoAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
                undoAction.setActionDefinitionId("org.topcased.requirement.core.redo"); //$NON-NLS-1$
                manager.add(redoAction);
                
                //add a last separator to surround undo & redo actions
                Separator last = new Separator("lastSeparator"); //$NON-NLS-1$
                last.setVisible(true);
                manager.add(last);
            }
        }
    }

    /**
     * @see org.eclipse.ui.part.Page#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        final GridLayout mainLayout = new GridLayout();
        mainLayout.marginHeight = 0;
        mainLayout.marginWidth = 0;

        mainComposite = new Composite(parent, SWT.NONE);
        mainComposite.setLayout(mainLayout);

        viewer = new TreeViewer(mainComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        viewer.setContentProvider(new CurrentRequirementContentProvider(RequirementUtils.getAdapterFactory()));
        CurrentRequirementLabelProvider labelProvider = new CurrentRequirementLabelProvider(RequirementUtils.getAdapterFactory());
        ILabelDecorator labelDecorator = RequirementCorePlugin.getDefault().getWorkbench().getDecoratorManager().getLabelDecorator();
        ILabelProvider fullLabelProvider = new DecoratingLabelProvider(labelProvider, labelDecorator);
        viewer.setLabelProvider(fullLabelProvider);
        viewer.addDoubleClickListener(new RequirementDoubleClickListener());
        viewer.addSelectionChangedListener(new CurrentSelectionChangeListener());

        final CurrentRequirementFilter currentFilter = new CurrentRequirementFilter();
        viewer.setFilters(new ViewerFilter[] {currentFilter});

        int dndOperations = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] {RequirementTransfer.getInstance()};
        viewer.addDragSupport(dndOperations, transfers, new DragSourceCurrentAdapter(editingDomain, viewer));
        viewer.addDropSupport(dndOperations, transfers, new DropTargetCurrentAdapter(editingDomain, viewer));
        viewer.setUseHashlookup(true);

        // Text filter
        final SearchComposite findIt = new SearchComposite(mainComposite, SWT.NONE);
        findIt.setFilter(viewer, currentFilter);

        hookKeyListeners();
        hookContextMenu();
        hookListeners();

        getSite().setSelectionProvider(viewer);

    }
    
    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementPage#hookListeners()
     */
    @Override
    protected void hookListeners()
    {
        super.hookListeners();
        
        // Allow to receive only selection change coming From Property View
        getSite().getPage().addSelectionListener(IPageLayout.ID_PROBLEM_VIEW, this);
        
        hookUpstreamSelectionChangedListener();
    }
    
    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementPage#unhookListeners()
     */
    @Override
    protected void unhookListeners()
    {
        super.unhookListeners();
        
        // Fix [#3087] remove the listener set on the Problem view
        getSite().getPage().removeSelectionListener(IPageLayout.ID_PROBLEM_VIEW, this);
        
        unhookUpstreamSelectionChangedListener();
    }
    
    
    /**
     * hook the upstream selection change listener if the command is checked and the two pages are active
     */
    public void hookUpstreamSelectionChangedListener()
    {
        // Get the commands who have a registered state
        ICommandService cs = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
        Command filterCmd = cs.getCommand(ICommandConstants.FILTER_CURRENT_REQ_ID);
        
        // Handle cases when the command is toggled but the associated action isn't performed
        if (filterCmd.getState(RegistryToggleState.STATE_ID).getValue().equals(true))
        {
            if (((UpstreamRequirementView)UpstreamRequirementView.getInstance()) != null)
            {
                IPage upstreamPage = ((UpstreamRequirementView)UpstreamRequirementView.getInstance()).getCurrentPage();
                if (upstreamPage instanceof UpstreamPage && upstreamListener == null)
                {
                    upstreamListener = new UpstreamSelectionChangedListener(this);
                    ((UpstreamPage) upstreamPage).getViewer().addSelectionChangedListener(upstreamListener);
                }
            }
        }
    }
    

    /**
     * unhook the upstream selection change listener when the page is disposed and reset the current page tree
     */
    public void unhookUpstreamSelectionChangedListener()
    {
        if (((UpstreamRequirementView)UpstreamRequirementView.getInstance()) != null)
        {
            IPage upstreamPage = ((UpstreamRequirementView)UpstreamRequirementView.getInstance()).getCurrentPage();
            if (upstreamPage instanceof UpstreamPage && upstreamListener != null)
            {
                ((UpstreamPage) upstreamPage).getViewer().removeSelectionChangedListener(upstreamListener);
                upstreamListener = null;
                
                //reset the tree otherwise the filter would be still active
                CurrentViewSingletonFilter.getInstance().setSearchedRequirement(null);
                if (!this.getViewer().getControl().isDisposed())
                {
                    this.getViewer().refresh();
                }
            }
        }
    }
    /**
     * Defines the default content of the popup menu.
     */
    private void hookContextMenu()
    {
        //Create menu
        menuManager = new MenuManager();
        menuManager.setRemoveAllWhenShown(true);
        menuManager.addMenuListener(new CurrentMenuManager());

        Menu menu = menuManager.createContextMenu(viewer.getControl());
        viewer.getTree().setMenu(menu);
        
        // Register menu for extension.
        getSite().registerContextMenu(CURRENT_POPUP_ID, menuManager, viewer);
    }

    /**
     * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart,
     *      org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IWorkbenchPart part, ISelection theSelection)
    {
        if (theSelection != null && !theSelection.isEmpty() && theSelection instanceof StructuredSelection)
        {
            Object object = ((StructuredSelection) theSelection).getFirstElement();
            if (object instanceof MarkerItem)
            {
                try
                {
                    IMarker marker = ((MarkerItem) object).getMarker();
                    goToEObject(marker);
                }
                catch (CoreException e)
                {
                    RequirementCorePlugin.log(e);
                }
            }
        }
    }

    /**
     * Select an EObject in the TreeViewer
     * 
     * @param marker The marker related to a current requirement.
     * @throws CoreException If something failed.
     */
    private void goToEObject(IMarker marker) throws CoreException
    {
        if (marker != null)
        {
            int severity = (Integer) marker.getAttribute(IMarker.SEVERITY, 0);
            if (EValidator.MARKER.equals(marker.getType()) && IMarker.SEVERITY_WARNING == severity)
            {
                String emfURI = (String) marker.getAttribute(EValidator.URI_ATTRIBUTE);
                if (emfURI != null)
                {
                    URI uri = URI.createURI(emfURI);
                    EObject toSelect = editingDomain.getResourceSet().getEObject(uri, false);
                    if (toSelect != null)
                    {
                        viewer.setSelection(new StructuredSelection(toSelect));
                    }
                }
            }
        }
    }

    /**
     * FIXME : handle key with an extension point
     * 
     * @see org.topcased.requirement.core.views.AbstractRequirementPage#executeCodeForKey(org.eclipse.jface.viewers.ISelection)
     */
    @Override
    protected void executeCodeForKey(ISelection theSelection)
    {
        Boolean process = false;
        if (theSelection instanceof IStructuredSelection)
        {
            process = true;
            Iterator< ? > it = ((IStructuredSelection) theSelection).iterator();
            while (it.hasNext())
            {
                Object o = it.next();
                if (!(o instanceof Requirement) && !(o instanceof HierarchicalElement) && !(o instanceof AttributeLink))
                {
                    process = false;
                }
            }
        }
        if (process)
        {
//            IAction action = new CurrentRequirementDeleteAction((IStructuredSelection) selection, editingDomain);
//            if (action.isEnabled())
//            {
//                action.run();
//            }
        }
    }

    /**
     * The setter of the Model
     * 
     * @param model
     */
    public void setModel(EObject model)
    {
        this.model = model;
    }

    /**
     * The getter of the Model
     * 
     * @return model
     */
    public EObject getModel()
    {
        return model;
    }
}
