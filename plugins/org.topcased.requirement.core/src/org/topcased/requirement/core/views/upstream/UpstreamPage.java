/***********************************************************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe Mertz (CS) - initial API and implementation
 *               Maxime AUDRAIN (CS) - API Changes
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views.upstream;

import java.util.Collection;

import org.eclipse.core.commands.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.dnd.DragSourceUpstreamAdapter;
import org.topcased.requirement.core.dnd.RequirementTransfer;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.filters.CurrentViewFilterFromUpstreamSelection;
import org.topcased.requirement.core.filters.RequirementFilter;
import org.topcased.requirement.core.handlers.ICommandConstants;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.listeners.RequirementDoubleClickListener;
import org.topcased.requirement.core.listeners.UpstreamSelectionChangedListener;
import org.topcased.requirement.core.preferences.RequirementPreferenceConstants;
import org.topcased.requirement.core.preferences.UpstreamStylesPreferenceHelper;
import org.topcased.requirement.core.providers.UpstreamRequirementContentProvider;
import org.topcased.requirement.core.providers.UpstreamRequirementLabelProvider;
import org.topcased.requirement.core.utils.RequirementCoverageComputer;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.AbstractRequirementPage;
import org.topcased.requirement.core.views.AbstractRequirementView;
import org.topcased.requirement.core.views.SearchComposite;
import org.topcased.requirement.core.views.current.CurrentPage;

import ttm.Document;

/**
 * Represents the Page displayed into the upstream requirement view.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class UpstreamPage extends AbstractRequirementPage implements IUpstreamRequirementPage
{
    private IStructuredSelection currSelection;

    private UpstreamRequirementContentProvider ctPvd;

    static final String UPSTREAM_POPUP_ID = "org.topcased.requirement.core.views.upstream.popupMenu"; //$NON-NLS-1$

    /** The message entry for displaying coverage of upstream requirements in the status bar */
    private static final String UPSTREAM_PAGE_STATUS_MESSAGE = "UpstreamPage.2"; //$NON-NLS-1$

    /**
     * @see org.eclipse.ui.part.Page#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent)
    {
        final GridLayout mainLayout = new GridLayout();
        mainLayout.marginHeight = 0;
        mainLayout.marginWidth = 0;

        mainComposite = new Composite(parent, SWT.NONE);
        mainComposite.setLayout(mainLayout);

        viewer = new TreeViewer(mainComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        viewer.setUseHashlookup(true);
        viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        viewer.setContentProvider(ctPvd = new UpstreamRequirementContentProvider(RequirementUtils.getAdapterFactory(), getEditingDomain()));

        UpstreamRequirementLabelProvider labelProvider = new UpstreamRequirementLabelProvider(RequirementUtils.getAdapterFactory(getEditingDomain()));
        ILabelDecorator labelDecorator = RequirementCorePlugin.getDefault().getWorkbench().getDecoratorManager().getLabelDecorator();
        ILabelProvider fullLabelProvider = new DecoratingLabelProvider(labelProvider, labelDecorator);
        viewer.setLabelProvider(fullLabelProvider);
        viewer.addDoubleClickListener(new RequirementDoubleClickListener());
        viewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            public void selectionChanged(SelectionChangedEvent event)
            {
                if (!event.getSelection().isEmpty() && event.getSelection() instanceof IStructuredSelection)
                {
                    currSelection = (IStructuredSelection) event.getSelection();
                    handleStatusLine();
                }
            }
        });

        int dndOperations = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] {RequirementTransfer.getInstance()};
        viewer.addDragSupport(dndOperations, transfers, new DragSourceUpstreamAdapter(viewer));
        RequirementFilter upstreamRequirementFilter = new RequirementFilter(false, true);
        viewer.addFilter(upstreamRequirementFilter);

        // Text filter
        final SearchComposite findIt = new SearchComposite(mainComposite, SWT.NONE)
        {
            @Override
            protected void doAfterSearch()
            {
                viewer.refresh();
            }

            @Override
            protected void doAfterEmptySearch()
            {
                viewer.refresh();
            }
        };
        findIt.setFilter(upstreamRequirementFilter);

        hookContextMenu();
        hookListeners();

        getSite().setSelectionProvider(viewer);
    }

    /**
     * Defines the default popup menu. It only contains undo & redo gef actions. All others actions are defined via the
     * extension point org.eclipse.ui.menus
     */
    protected void hookContextMenu()
    {
        // Create menu
        MenuManager menuManager = new MenuManager();
        menuManager.setRemoveAllWhenShown(true);
        menuManager.addMenuListener(new IMenuListener()
        {
            public void menuAboutToShow(IMenuManager manager)
            {
                if (RequirementUtils.getCurrentEditor() != null)
                {
                    // add a first separator to surround undo & redo actions
                    Separator first = new Separator(firstPopupMenuSeparator);
                    first.setVisible(true);
                    manager.add(first);

                    // using gef undo stack action because emf undo/redo got label problems.
                    UndoAction undoAction = new UndoAction(RequirementUtils.getCurrentEditor().getSite().getPart());
                    undoAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
                    undoAction.update();
                    undoAction.setActionDefinitionId(ICommandConstants.UNDO_ID);
                    manager.add(undoAction);

                    // using gef redo stack actions because emf undo/redo got label problems.
                    RedoAction redoAction = new RedoAction(RequirementUtils.getCurrentEditor().getSite().getPart());
                    redoAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
                    redoAction.update();
                    redoAction.setActionDefinitionId(ICommandConstants.REDO_ID);
                    manager.add(redoAction);

                    // add a last separator to surround undo & redo actions
                    Separator last = new Separator(lastPopupMenuSeparator);
                    last.setVisible(true);
                    manager.add(last);
                }

            }
        });

        Menu menu = menuManager.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);

        // Register menu for extension.
        getSite().registerContextMenu(UPSTREAM_POPUP_ID, menuManager, viewer);
    }

    /**
     * Handles the display of the status line according to the selection done.
     */
    protected void handleStatusLine()
    {
        IActionBars actionBars = getSite().getActionBars();
        if (actionBars != null)
        {
            IStatusLineManager statusLineManager = actionBars.getStatusLineManager();
            if (statusLineManager != null)
            {
                StringBuffer message = new StringBuffer();
                if (currSelection.getFirstElement() instanceof Document)
                {
                    Document currentDoc = (Document) currSelection.getFirstElement();
                    // add message about document which requirement were extracted from
                    message.append(NLS.bind(Messages.getString("UpstreamPage.0"), currentDoc.getIdent())); //$NON-NLS-1$
                }
                // get elements for coverage metrics
                int numberOfRequirements = RequirementCoverageComputer.INSTANCE.getNumberOfRequirements();
                int numberOfCovered = RequirementCoverageComputer.INSTANCE.getNumberOfFullyCoveredUpstreamRequirements();
                String coveredRate = RequirementCoverageComputer.INSTANCE.getFullyCoveredUpstreamRate();
                int numberOfTraced = RequirementCoverageComputer.INSTANCE.getNumberOfCoveredWithPartialUpstreamRequirements();
                String tracedRate = RequirementCoverageComputer.INSTANCE.getCoveredWithPartialUpstreamRate();
                int numberOfNotCovered = RequirementCoverageComputer.INSTANCE.getNumberOfNotCoveredUpstreamRequirements();
                String notCoveredRate = RequirementCoverageComputer.INSTANCE.getNotCoveredUpstreamRate();
                // add message with coverage metrics
                Object[] params = new Object[] {numberOfRequirements, numberOfCovered, coveredRate, numberOfTraced, tracedRate, numberOfNotCovered, notCoveredRate};
                message.append(NLS.bind(Messages.getString(UPSTREAM_PAGE_STATUS_MESSAGE), params));

                statusLineManager.setMessage(message.toString());
            }
        }
    }

    /**
     * The Flat and hierarchical sorters (FlatCommand and HierarchicalCommand) need to get the content provider to apply
     * sorting changes
     * 
     * @return the UpstreamRequirementContentProvider
     */
    public UpstreamRequirementContentProvider getUpstreamRequirementContentProvider()
    {
        return ctPvd;
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementPage#hookListeners()
     */
    @Override
    protected void hookListeners()
    {
        super.hookListeners();

        RequirementUtils.getAdapterFactory().addListener(changeListener);
        AbstractRequirementView.getPreferenceStore().addPropertyChangeListener(prefListener);

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

        RequirementUtils.getAdapterFactory().removeListener(changeListener);
        AbstractRequirementView.getPreferenceStore().removePropertyChangeListener(prefListener);

        unhookUpstreamSelectionChangedListener();
    }

    /**
     * Hooks the upstream selection change listener if the command is checked and the two pages are active
     */
    public void hookUpstreamSelectionChangedListener()
    {
        // Get the commands who have a registered state
        ICommandService cs = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
        Command filterCmd = cs.getCommand(ICommandConstants.FILTER_CURRENT_REQ_ID);

        // Handle cases when the command is toggled but the associated action isn't performed
        if (filterCmd.getState(RegistryToggleState.STATE_ID).getValue().equals(true))
        {
            CurrentPage currentPage = RequirementHelper.INSTANCE.getCurrentPage();
            if (currentPage != null && upstreamListener == null)
            {
                upstreamListener = new UpstreamSelectionChangedListener(currentPage);
                viewer.addSelectionChangedListener(upstreamListener);
            }
        }
    }

    /**
     * Unhooks the upstream selection change listener when the page is disposed and reset the current page tree
     */
    public void unhookUpstreamSelectionChangedListener()
    {
        CurrentPage currentPage = RequirementHelper.INSTANCE.getCurrentPage();
        if (currentPage != null && upstreamListener != null)
        {
            viewer.removeSelectionChangedListener(upstreamListener);
            upstreamListener = null;

            // reset the tree otherwise the filter would be still active
            CurrentViewFilterFromUpstreamSelection.getInstance().setSearchedRequirement(null);
            if (!currentPage.getViewer().getControl().isDisposed())
            {
                currentPage.getViewer().refresh();
            }
        }
    }

    /**
     * Internal listener to refresh the upstream viewer when style preference is updated.
     */
    private IPropertyChangeListener prefListener = new IPropertyChangeListener()
    {
        /**
         * Update display if preference style has changed
         * 
         * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent event)
        {
            if (UpstreamStylesPreferenceHelper.UPSTREAM_STYLES_PREFERENCE.equals(event.getProperty()))
            {
                if (!viewer.getControl().isDisposed())
                {
                    viewer.refresh(true);
                }
            }
        }
    };

    /**
     * Internal listener to refresh the upstream viewer when modifications are done on the current page.
     */
    private INotifyChangedListener changeListener = new INotifyChangedListener()
    {
        /**
         * @see org.eclipse.emf.edit.provider.INotifyChangedListener#notifyChanged(org.eclipse.emf.common.notify.Notification)
         */
        public void notifyChanged(Notification msg)
        {
            if (!getControl().isDisposed())
            {
                if (msg.getEventType() == Notification.ADD)
                {
                    internalRefresh(msg.getNewValue());
                    // refresh decorator only if needed
                    if (msg.getNewValue() instanceof Requirement)
                    {
                        EObject hierElt = ((Requirement) msg.getNewValue()).eContainer();
                        if (hierElt instanceof HierarchicalElement)
                        {
                            refreshLinkedEditPartsDecorators((HierarchicalElement) hierElt);
                        }
                    }
                }
                else if (msg.getEventType() == Notification.REMOVE)
                {
                    internalRefresh(msg.getOldValue());
                    // refresh decorator only if needed
                    if (msg.getOldValue() instanceof CurrentRequirement && msg.getNotifier() instanceof HierarchicalElement)
                    {
                        refreshLinkedEditPartsDecorators((HierarchicalElement) msg.getNotifier());
                    }
                }
                else if (msg.getEventType() == Notification.SET)
                {
                    switch (msg.getFeatureID(AttributeLink.class))
                    {
                        case RequirementPackage.ATTRIBUTE_LINK__VALUE: {
                            internalRefresh(msg.getOldValue());
                            internalRefresh(msg.getNewValue());
                            // refresh decorator only if needed
                            if (msg.getNotifier() instanceof AttributeLink)
                            {
                                AttributeLink attLink = (AttributeLink) msg.getNotifier();
                                if (attLink.eContainer() != null && attLink.eContainer().eContainer() instanceof HierarchicalElement) {
                                    HierarchicalElement hierElt = (HierarchicalElement) attLink.eContainer().eContainer();
                                    refreshLinkedEditPartsDecorators(hierElt);
                                }
                            }
                            break;
                        }
                        case RequirementPackage.ATTRIBUTE_LINK__PARTIAL: {
                            internalRefresh(msg.getNotifier());
                            break;
                        }
                    }
                }
            }
        }

        private void internalRefresh(Object object)
        {
            if (object instanceof AttributeLink)
            {
                Object value = ((AttributeLink) object).getValue();
                internalRefresh(value); //TODO value == null -> invert add/set order and try again
            }
            else if (object instanceof ttm.Requirement)
            {
                viewer.update(object, null);
            }
            else if (object instanceof Requirement)
            {
                updateReferencedUpstream((Requirement) object);
            }
            else if (object instanceof HierarchicalElement)
            {
                Collection<Requirement> reqs = RequirementUtils.getCurrents((HierarchicalElement) object);
                for (Requirement aReq : reqs)
                {
                    updateReferencedUpstream(aReq);
                }
            }
        }

        private void updateReferencedUpstream(Requirement currReq)
        {
            for (Attribute attribute : currReq.getAttribute())
            {
                if (attribute instanceof AttributeLink)
                {
                    EObject value = ((AttributeLink) attribute).getValue();
                    if (value != null)
                    {
                        viewer.update(value, null);
                    }
                }
            }
        }

    };

    /**
     * Refresh requirement decorators of related edit parts
     * 
     * @param updatedHierachicalElement the hierarchical element which current requirements are modified
     */
    protected void refreshLinkedEditPartsDecorators(HierarchicalElement updatedHierachicalElement)
    {
        // check if necessary according to preference first
        if (RequirementCorePlugin.getDefault().getPreferenceStore().getBoolean(RequirementPreferenceConstants.DISPLAY_CURRENT_DECORATOR))
        {
            EObject linkedModelElt = updatedHierachicalElement.getElement();
            if (linkedModelElt != null)
            {
                IEditorPart editor = RequirementUtils.getCurrentEditor();
                IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
                if (editor != null && services != null)
                {
                    EditPartViewer graphViewer = services.getGraphicalViewer(editor);
                    for (Object part : graphViewer.getEditPartRegistry().values())
                    {
                        if (part instanceof EditPart)
                        {
                            EditPart editPart = (EditPart) part;
                            if (linkedModelElt.equals(services.getEObject(editPart)))
                            {
                                // notify the edit part to refresh decorators
                                editPart.addNotify();
                            }
                        }
                    }
                }
            }
        }
    }
}
