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

import org.eclipse.core.commands.Command;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
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
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.part.IPage;
import org.topcased.requirement.core.dnd.DragSourceUpstreamAdapter;
import org.topcased.requirement.core.dnd.RequirementTransfer;
import org.topcased.requirement.core.filters.CurrentViewFilterFromUpstreamSelection;
import org.topcased.requirement.core.filters.UpstreamRequirementFilter;
import org.topcased.requirement.core.handlers.ICommandConstants;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.listeners.RequirementDoubleClickListener;
import org.topcased.requirement.core.listeners.UpstreamSelectionChangedListener;
import org.topcased.requirement.core.providers.UpstreamRequirementContentProvider;
import org.topcased.requirement.core.providers.UpstreamRequirementLabelProvider;
import org.topcased.requirement.core.utils.RequirementCoverageComputer;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.AbstractRequirementPage;
import org.topcased.requirement.core.views.SearchComposite;
import org.topcased.requirement.core.views.current.CurrentPage;
import org.topcased.requirement.core.views.current.CurrentRequirementView;

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
        hookListeners();

        getSite().setSelectionProvider(viewer);
        refreshViewer(true);
    }

    /**
     * Defines the default popup menu. It only contains undo & redo actions. 
     * All others actions are defined via the extension point org.eclipse.ui.menus
     */
    protected void hookContextMenu()
    {
        
        //Create menu
        MenuManager menuManager = new MenuManager();
        menuManager.setRemoveAllWhenShown(true);
        menuManager.addMenuListener(new IMenuListener()
        {
            public void menuAboutToShow(IMenuManager manager)
            {
                ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
                
                //add a first separator to surround undo & redo actions
                Separator first = new Separator(firstPopupMenuSeparator);
                first.setVisible(true);
                manager.add(first);  
                
                UndoAction undoAction = new UndoAction(editingDomain);
                undoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
                undoAction.setActionDefinitionId(ICommandConstants.UNDO_ID);
                manager.add(undoAction);
                
                RedoAction redoAction = new RedoAction(editingDomain);
                redoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
                redoAction.setActionDefinitionId(ICommandConstants.REDO_ID);
                manager.add(redoAction);
                
                //add a last separator to surround undo & redo actions
                Separator last = new Separator(lastPopupMenuSeparator);
                last.setVisible(true);
                manager.add(last);

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
                    message.append(String.format(Messages.getString("UpstreamPage.0"), currentDoc.getIdent())); //$NON-NLS-1$
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
        
        hookUpstreamSelectionChangedListener();
    }
    
    /**
     * @see org.topcased.requirement.core.views.AbstractRequirementPage#unhookListeners()
     */
    @Override
    protected void unhookListeners()
    {
        super.unhookListeners();
        
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
            IPage currentPage = RequirementHelper.INSTANCE.getCurrentPage();
            if (currentPage instanceof CurrentPage && upstreamListener == null)
            {
                upstreamListener = new UpstreamSelectionChangedListener((CurrentPage) currentPage);
                this.getViewer().addSelectionChangedListener(upstreamListener);
            }
        }
    }
    

    /**
     * unhook the upstream selection change listener when the page is disposed and reset the current page tree
     */
    public void unhookUpstreamSelectionChangedListener()
    {
        if (((CurrentRequirementView)CurrentRequirementView.getInstance()) != null)
        {
            IPage currentPage = ((CurrentRequirementView)CurrentRequirementView.getInstance()).getCurrentPage();
            if (upstreamListener != null)
            {
                this.getViewer().removeSelectionChangedListener(upstreamListener);
                upstreamListener = null;
                
                if (currentPage instanceof CurrentPage)
                {
                    //reset the tree otherwise the filter would be still active
                    CurrentViewFilterFromUpstreamSelection.getInstance().setSearchedRequirement(null);
                    ((CurrentPage) currentPage).getViewer().refresh();
                }
            }
        }
    }
}
