/***********************************************************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe MERTZ (CS) - initial API and implementation,
 * Vincent Hemery [(Atos Origin)] [vincent.hemery@atosorigin.com] - updating of CurrentSelectionChangeListener
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views.current;

import java.util.Iterator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.markers.MarkerItem;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.actions.AddAttributeAction;
import org.topcased.requirement.core.actions.CreateAnonymousRequirementAction;
import org.topcased.requirement.core.actions.CreateCurrentRequirementAction;
import org.topcased.requirement.core.actions.CurrentRequirementCopyAction;
import org.topcased.requirement.core.actions.CurrentRequirementCutAction;
import org.topcased.requirement.core.actions.CurrentRequirementDeleteAction;
import org.topcased.requirement.core.actions.CurrentRequirementPasteAction;
import org.topcased.requirement.core.actions.LinkWithEditorAction;
import org.topcased.requirement.core.actions.LoadResourceAction;
import org.topcased.requirement.core.actions.MoveAction;
import org.topcased.requirement.core.actions.RequirementAbstractEMFAction;
import org.topcased.requirement.core.actions.SetAsValidAction;
import org.topcased.requirement.core.actions.SetMarkerAction;
import org.topcased.requirement.core.actions.SetUnsetPartialAction;
import org.topcased.requirement.core.actions.UnallocateAction;
import org.topcased.requirement.core.actions.UpdateAttributeAction;
import org.topcased.requirement.core.actions.UpdateAttributeConfigurationAction;
import org.topcased.requirement.core.dnd.DragSourceCurrentAdapter;
import org.topcased.requirement.core.dnd.DropTargetCurrentAdapter;
import org.topcased.requirement.core.dnd.RequirementTransfer;
import org.topcased.requirement.core.filters.CurrentRequirementFilter;
import org.topcased.requirement.core.listeners.RequirementDoubleClickListener;
import org.topcased.requirement.core.providers.CurrentRequirementContentProvider;
import org.topcased.requirement.core.providers.CurrentRequirementLabelProvider;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.AbstractRequirementPage;
import org.topcased.requirement.core.views.AddRequirementMarker;
import org.topcased.requirement.core.views.SearchComposite;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.core.Messages;

/**
 * This class creates the page to edit a requirement model in the upstream requirement view
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class CurrentPage extends AbstractRequirementPage implements ICurrentRequirementPage
{
    private EObject model;

    private IStructuredSelection selection;

    private MenuManager submenuManager;

    private MenuManager createChildMenuManager;

    private MoveAction upAction;

    private MoveAction downAction;

    private IAction linkTo;

    /**
     * This class manages the change selection in the current requirement view
     * 
     * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
     * 
     */
    private class CurrentSelectionChangeListener implements ISelectionChangedListener
    {
        private HierarchicalElement hierarchicalElementToFocusAfterRequirementDeletion = null;

        private Requirement previouslySelectedRequirement = null;

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
            hierarchicalElementToFocusAfterRequirementDeletion = null;
            if (!event.getSelection().isEmpty() && event.getSelection() instanceof IStructuredSelection)
            {
                selection = (IStructuredSelection) event.getSelection();
                upAction.setSelection(selection);
                downAction.setSelection(selection);
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
            boolean parentToFocusAvailable = hierarchicalElementToFocusAfterRequirementDeletion != null && stillExistInModel(hierarchicalElementToFocusAfterRequirementDeletion);
            if (event.getSelection().isEmpty() && previousRequirementDeleted && parentToFocusAvailable)
            {
                if (!(hierarchicalElementToFocusAfterRequirementDeletion.getRequirement().isEmpty()))
                {
                // When a requirement is deleted from this page, we set focus on its container.
                CurrentPage.this.getViewer().setSelection(new StructuredSelection(hierarchicalElementToFocusAfterRequirementDeletion));
                // New selection will not produce another re-selection since it is not empty (Infinite loop avoided).
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
     * This class create the menu of the current requirement view
     * 
     * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
     * 
     */
    private class CurrentMenuManager implements IMenuListener
    {
        public void menuAboutToShow(IMenuManager manager)
        {
            if (model != null)
            {
                createSubMenuMarker(manager);

                final RequirementAbstractEMFAction copyAction = new CurrentRequirementCopyAction(selection, editingDomain);
                copyAction.setEnabled(copyAction.isEnabled());
                manager.add(copyAction);

                final RequirementAbstractEMFAction cutAction = new CurrentRequirementCutAction(selection, editingDomain);
                cutAction.setEnabled(cutAction.isEnabled());
                manager.add(cutAction);

                final RequirementAbstractEMFAction deleteAction = new CurrentRequirementDeleteAction(selection, editingDomain);
                deleteAction.setEnabled(deleteAction.isEnabled());
                manager.add(deleteAction);

                final RequirementAbstractEMFAction pasteAction = new CurrentRequirementPasteAction(selection, editingDomain);
                pasteAction.setEnabled(pasteAction.isEnabled());
                manager.add(pasteAction);

                manager.add(new Separator("separator_0")); //$NON-NLS-1$

                if (toDisplay(selection, Requirement.class))
                {
                    manager.add(new Separator("separator_1")); //$NON-NLS-1$
                    manager.add(new SetAsValidAction(selection, editingDomain));
                }

                if (toDisplay(selection, HierarchicalElement.class))
                {
                    manager.add(new Separator("separator_1")); //$NON-NLS-1$
                }

                if (toDisplay(selection, HierarchicalElement.class) || toDisplay(selection, CurrentRequirement.class))
                {
                    manager.add(new CreateCurrentRequirementAction(selection));
                    manager.add(new CreateAnonymousRequirementAction(selection));
                    manager.appendToGroup("SET_MARKER", submenuManager); //$NON-NLS-1$
                    manager.add(new Separator("separator_1")); //$NON-NLS-1$
                    manager.add(new UpdateAttributeAction(selection, CurrentPage.this));
                    manager.add(new UnallocateAction(selection, CurrentPage.this));
                }

                if (toDisplay(selection, ObjectAttribute.class))
                {
                    manager.add(new AddAttributeAction(selection, CurrentPage.this));
                }

                if (toDisplay(selection, AttributeLink.class))
                {
                    manager.add(new Separator("separator_1")); //$NON-NLS-1$
                    manager.add(new SetUnsetPartialAction(selection, CurrentPage.this));
                }

                if (toDisplay(selection, AttributeConfiguration.class))
                {
                    manager.add(new Separator("separator_2")); //$NON-NLS-1$
                    manager.add(new UpdateAttributeConfigurationAction(selection, CurrentPage.this));
                }

                manager.add(new Separator("separator_2")); //$NON-NLS-1$
                manager.add(new LoadResourceAction(editingDomain));
                manager.add(new Separator("separator_2")); //$NON-NLS-1$

                UndoAction undoAction = new UndoAction(editingDomain);
                undoAction.setActionDefinitionId("org.topcased.requirement.core.undoAction"); //$NON-NLS-1$
                undoAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
                manager.add(undoAction);

                RedoAction redoAction = new RedoAction(editingDomain);
                redoAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
                redoAction.setActionDefinitionId("org.topcased.requirement.core.redoAction"); //$NON-NLS-1$
                manager.add(redoAction);

                // final IHandlerService service = (IHandlerService) getSite().getService(IHandlerService.class);
                // service.activateHandler(copyAction.getActionDefinitionId(), new ActionHandler(copyAction));
                // service.activateHandler(cutAction.getActionDefinitionId(), new ActionHandler(cutAction));
                // service.activateHandler(pasteAction.getActionDefinitionId(), new ActionHandler(pasteAction));
                // service.activateHandler(redoAction.getActionDefinitionId(), new ActionHandler(redoAction));
                // service.activateHandler(undoAction.getActionDefinitionId(), new ActionHandler(undoAction));
            }

        }
    }

    /**
     * @see org.eclipse.ui.part.Page#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        mainComposite = new Composite(parent, SWT.NONE);
        mainComposite.setLayout(new GridLayout());

        int styleTree = SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI;
        Tree tree = new Tree(mainComposite, styleTree);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        viewer = new TreeViewer(tree);
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

        createToolBarActions();

        getSite().setSelectionProvider(viewer);

        // Allow to receive only selection change coming From Property View
        getSite().getPage().addSelectionListener(IPageLayout.ID_PROBLEM_VIEW, this);

    }

    /**
     * Creates the tool bar for the requirement view
     */
    private void createToolBarActions()
    {
        IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
        tbm.add(new Separator());

        linkTo = new LinkWithEditorAction(this);
        tbm.add(linkTo);

        upAction = new MoveAction(this, true);
        tbm.add(upAction);

        downAction = new MoveAction(this, false);
        tbm.add(downAction);
    }

    /**
     * Defines the content of the popup menu.
     */
    private void hookContextMenu()
    {
        createChildMenuManager = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        createChildMenuManager.setRemoveAllWhenShown(true);
        createChildMenuManager.addMenuListener(new CurrentMenuManager());

        Menu menu = createChildMenuManager.createContextMenu(viewer.getControl());
        viewer.getTree().setMenu(menu);
    }

    /**
     * Creates a sub menu.
     * 
     * @param manager The menu manager to use.
     */
    private void createSubMenuMarker(IMenuManager manager)
    {
        manager.add(new Separator("SET_MARKER")); //$NON-NLS-1$

        submenuManager = new MenuManager(Messages.getString("CurrentPage.3")); //$NON-NLS-1$
        IAction setStartMarker = new SetMarkerAction(selection, this, AddRequirementMarker.eINSTANCE.sTART);
        setStartMarker.setText(Messages.getString("CurrentPage.4")); //$NON-NLS-1$
        IAction setEndMarker = new SetMarkerAction(selection, this, AddRequirementMarker.eINSTANCE.eND);
        setEndMarker.setText(Messages.getString("CurrentPage.5")); //$NON-NLS-1$
        IAction setPosMarker = new SetMarkerAction(selection, this, AddRequirementMarker.eINSTANCE.pOS);
        setPosMarker.setText(Messages.getString("CurrentPage.6")); //$NON-NLS-1$
        submenuManager.add(setStartMarker);
        submenuManager.add(setEndMarker);
        submenuManager.add(setPosMarker);
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
            IAction action = new CurrentRequirementDeleteAction((IStructuredSelection) selection, editingDomain);
            if (action.isEnabled())
            {
                action.run();
            }
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