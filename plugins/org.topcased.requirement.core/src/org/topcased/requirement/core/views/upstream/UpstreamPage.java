/***********************************************************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe Mertz (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views.upstream;

import java.util.Iterator;

import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.actions.RequirementAbstractEMFAction;
import org.topcased.requirement.core.actions.UnlinkRequirementModelAction;
import org.topcased.requirement.core.actions.UpdateRequirementModelAction;
import org.topcased.requirement.core.actions.UpstreamRequirementDeleteAction;
import org.topcased.requirement.core.dnd.DragSourceUpstreamAdapter;
import org.topcased.requirement.core.dnd.RequirementTransfer;
import org.topcased.requirement.core.filters.UpstreamRequirementFilter;
import org.topcased.requirement.core.listeners.RequirementDoubleClickListener;
import org.topcased.requirement.core.providers.UpstreamRequirementContentProvider;
import org.topcased.requirement.core.providers.UpstreamRequirementLabelProvider;
import org.topcased.requirement.core.utils.RequirementCoverageComputer;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.AbstractRequirementPage;
import org.topcased.requirement.core.views.SearchComposite;
import org.topcased.requirement.core.Messages;
import ttm.Document;

/**
 * Represents the Page displayed into the upstream requirement view.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class UpstreamPage extends AbstractRequirementPage implements IUpstreamRequirementPage
{
    private IStructuredSelection currSelection;

    private IAction flatHierarchy;

    private IAction treeHierarchy;

    private UpstreamRequirementContentProvider ctPvd;

    static
    {
        IPreferenceStore ps = RequirementCorePlugin.getDefault().getPreferenceStore();
        ps.setDefault(SHOW_TREE_HIERARCHY_PREF, DEFAULT_SHOW_TREE_HIERARCHY);
        ps.setDefault(SHOW_FLAT_HIERARCHY_PREF, DEFAULT_SHOW_FLAT_HIERARCHY);
        ps.setDefault(SORT_UPSTREAM_ALPHABETICALLY_PREF, DEFAULT_SORT_UPSTREAM_ALPHABETICALLY);
    }

    /**
     * @see org.eclipse.ui.part.Page#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent)
    {
        mainComposite = new Composite(parent, SWT.NONE);
        mainComposite.setLayout(new GridLayout());

        viewer = new TreeViewer(mainComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        viewer.setContentProvider(ctPvd = new UpstreamRequirementContentProvider(RequirementUtils.getAdapterFactory()));

        UpstreamRequirementLabelProvider labelProvider = new UpstreamRequirementLabelProvider(RequirementUtils.getAdapterFactory());
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
        UpstreamRequirementFilter upstreamRequirementFilter = new UpstreamRequirementFilter();
        viewer.addFilter(upstreamRequirementFilter);

        // Text filter
        final SearchComposite findIt = new SearchComposite(mainComposite, SWT.NONE);
        findIt.setFilter(viewer, upstreamRequirementFilter);

        hookContextMenu();
        hookKeyListeners();
        hookListeners();

        createToolBarActions();

        getSite().setSelectionProvider(viewer);
        this.refreshViewer(true);
    }

    /**
     * Defines the popup menu
     */
    protected void hookContextMenu()
    {
        MenuManager createChildMenuManager = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        createChildMenuManager.setRemoveAllWhenShown(true);
        createChildMenuManager.addMenuListener(new IMenuListener()
        {
            public void menuAboutToShow(IMenuManager manager)
            {
                if (toDisplay(currSelection, Document.class))
                {
                    RequirementAbstractEMFAction deleteAction = new UpstreamRequirementDeleteAction(currSelection, editingDomain);
                    deleteAction.setEnabled(deleteAction.isEnabled());
                    manager.add(deleteAction);
                }

                manager.add(new Separator("separator_2")); //$NON-NLS-1$
                ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();

                RedoAction redoAction = new RedoAction(editingDomain);
                redoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
                manager.add(redoAction);

                UndoAction undoAction = new UndoAction(editingDomain);
                undoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
                manager.add(undoAction);
            }
        });

        Menu menu = createChildMenuManager.createContextMenu(viewer.getControl());
        viewer.getTree().setMenu(menu);
    }

    /**
     * Create the tool bar for the requirement view
     */
    protected void createToolBarActions()
    {
        IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
        final IPreferenceStore ps = RequirementCorePlugin.getDefault().getPreferenceStore();

        final IAction updateAction = new UpdateRequirementModelAction(editingDomain);
        tbm.add(updateAction);

        final IAction unlinkAction = new UnlinkRequirementModelAction(editingDomain);
        unlinkAction.addPropertyChangeListener(new IPropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent event)
            {
                if ("enabled".equals(event.getProperty())) //$NON-NLS-1$
                {
                    updateAction.setEnabled((Boolean) event.getNewValue());
                }
            }
        });
        tbm.add(unlinkAction);
        
        tbm.add(new Separator());
        
        // sorter action keeps status between two sessions
        IAction sorter = new Action("Sort", IAction.AS_CHECK_BOX) //$NON-NLS-1$ 
        {

            @Override
            public void run()
            {
                applySorter(isChecked());
                ps.setValue(SORT_UPSTREAM_ALPHABETICALLY_PREF, isChecked());
            }
        };
        sorter.setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/alphabetic_sorter.gif")); //$NON-NLS-1$
        sorter.setChecked(ps.getBoolean(SORT_UPSTREAM_ALPHABETICALLY_PREF));
        tbm.add(sorter);

        tbm.add(new Separator());

        // flat hierarchy action keeps status between two sessions
        flatHierarchy = new Action("Flat", IAction.AS_RADIO_BUTTON) //$NON-NLS-1$
        {
            @Override
            public void run()
            {
                treeHierarchy.setChecked(!isChecked());
                applyRepresentation(true);
                ps.setValue(SHOW_FLAT_HIERARCHY_PREF, isChecked());
                ps.setValue(SHOW_TREE_HIERARCHY_PREF, !isChecked());
            }
        };
        flatHierarchy.setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/flat-mode.gif")); //$NON-NLS-1$
        flatHierarchy.setChecked(ps.getBoolean(SHOW_FLAT_HIERARCHY_PREF));
        tbm.add(flatHierarchy);

        // tree hierarchy action keeps status between two sessions
        treeHierarchy = new Action("Hierarchical", IAction.AS_RADIO_BUTTON) //$NON-NLS-1$
        {
            @Override
            public void run()
            {
                flatHierarchy.setChecked(!isChecked());
                applyRepresentation(false);
                ps.setValue(SHOW_TREE_HIERARCHY_PREF, isChecked());
                ps.setValue(SHOW_FLAT_HIERARCHY_PREF, !isChecked());
            }
        };
        treeHierarchy.setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/tree-mode.gif")); //$NON-NLS-1$
        treeHierarchy.setChecked(ps.getBoolean(SHOW_TREE_HIERARCHY_PREF));
        tbm.add(treeHierarchy);

        // initialize the sorter
        applySorter(sorter.isChecked());

        // initialize the kind of representation
        applyRepresentation(ps.getBoolean(SHOW_FLAT_HIERARCHY_PREF));
    }

    /**
     * Applies an eventual sorter on the tree viewer.
     * 
     * @param state The activation status of the sorter
     */
    private void applySorter(boolean state)
    {
        viewer.setSorter(state ? new ViewerSorter() : null);
    }

    /**
     * Applies the right representation of the tree contents
     * 
     * @param isFlat should we use the flat representation or the tree one ?
     */
    private void applyRepresentation(boolean isFlat)
    {
        ctPvd.setIsFlat(isFlat);
        viewer.refresh();
    }

    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementPage#executeCodeForKey(org.eclipse.jface.viewers.ISelection)
     */
    @Override
    protected void executeCodeForKey(ISelection selection)
    {
        Boolean process = false;
        if (selection instanceof IStructuredSelection)
        {
            process = true;
            Iterator< ? > it = ((IStructuredSelection) selection).iterator();
            while (it.hasNext())
            {
                Object o = it.next();
                if (!(o instanceof ttm.Requirement || o instanceof Document))
                {
                    process = false;
                }
            }
        }
        if (process)
        {
            IAction action = new UpstreamRequirementDeleteAction(currSelection, editingDomain);
            if (action.isEnabled())
            {
                action.run();
            }
        }
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
                    message.append(String.format(Messages.getString("UpstreamPage.0"), currentDoc.getFilename())); //$NON-NLS-1$
                }
                // get elements for coverage rate
                int numberOfRequirements = RequirementCoverageComputer.INSTANCE.getNumberOfRequirements();
                int coveredRequirements = RequirementCoverageComputer.INSTANCE.getNumberOfCoveredRequirements();
                String coverageRate = RequirementCoverageComputer.INSTANCE.getCoverageRate();
                // add message with coverage rate
                message.append(String.format(Messages.getString("UpstreamPage.1"), coveredRequirements, numberOfRequirements, coverageRate)); //$NON-NLS-1$

                statusLineManager.setMessage(message.toString());
            }
        }
    }
}
