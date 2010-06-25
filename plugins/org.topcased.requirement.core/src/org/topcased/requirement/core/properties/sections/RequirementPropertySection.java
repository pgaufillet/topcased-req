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
package org.topcased.requirement.core.properties.sections;

import org.eclipse.core.commands.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.handlers.ICommandConstants;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.providers.RequirementPropertyTableContentProvider;
import org.topcased.requirement.core.providers.RequirementPropertyTableLabelProvider;
import org.topcased.requirement.core.providers.RequirementPropertyTreeContentProvider;
import org.topcased.requirement.core.providers.RequirementPropertyTreeLabelProvider;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.tabbedproperties.utils.ObjectAdapter;

/**
 * Section that displays a single table or a tree containing associated upstream and current requirements.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe MERTZ</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class RequirementPropertySection extends AbstractPropertySection
{
    /**
     * Could be a table viewer or a tree viewer containing the upstream and current requirements.
     */
    private ColumnViewer currentViewer;
    
    
    /**
     * The parent composite of the currentViewer
     */
    private Composite parentCompo;
    
    /**
     * @see org.eclipse.ui.part.Page#dispose()
     */
    @Override
    public void dispose()
    {
        unhookListeners();
        super.dispose();
    }

    /**
     * Enable to dispose the current viewer
     */
    public void disposeViewer()
    {
        unhookListeners();
        parentCompo.getChildren()[0].dispose();
    }

    /**
     * Adds a listener to listen to the model changes
     */
    protected void hookListeners()
    {
        RequirementUtils.getAdapterFactory().addListener(modelListener);
    }

    /**
     * Removes listener listening to model changes.
     */
    protected void unhookListeners()
    {
        RequirementUtils.getAdapterFactory().removeListener(modelListener);
    }
    
    /**
     * @see org.eclipse.ui.views.properties.tabbed.ISection#createControls(org.eclipse.swt.widgets.Composite,
     *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
     */
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage)
    {
        super.createControls(parent, aTabbedPropertySheetPage);
        parent.setLayout(new FillLayout());
        parentCompo = parent;     

        // Get the commands who have a registered state
        ICommandService cs = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
        Command tableCmd = cs.getCommand(ICommandConstants.TABLE_ID);
        Command treeCmd = cs.getCommand(ICommandConstants.TREE_ID);
        
        //This is only for the first launch of the property section. Every
        //changes of viewers are made in the commands handlers
        if (tableCmd.getState(RegistryToggleState.STATE_ID).getValue().equals(true))
        {
            createTable(parent);
        }
        else if (treeCmd.getState(RegistryToggleState.STATE_ID).getValue().equals(true))
        {
            createTree(parent);
        }
        else
        {
            //default case when no button is already persisted
            tableCmd.getState(RegistryToggleState.STATE_ID).setValue(true);
            treeCmd.getState(RegistryToggleState.STATE_ID).setValue(false);
            createTable(parent);
        }
    }

    
    /**
     * Create the table in the property section
     * 
     * @param parent the parent composite
     */
    public void createTable(Composite parent)
    {           
        
        final Table requirementTable = getWidgetFactory().createTable(parent, SWT.MULTI | SWT.BORDER);
        requirementTable.setLayout(new FillLayout());
        requirementTable.setHeaderVisible(true);
        requirementTable.setLinesVisible(true);
                
        currentViewer = new TableViewer(requirementTable);
        
        final TableViewerColumn upstreamCol = new TableViewerColumn((TableViewer) currentViewer, SWT.FILL);
        upstreamCol.getColumn().setText(Messages.getString("RequirementPropertySection.0")); //$NON-NLS-1$
        upstreamCol.getColumn().setWidth(300);

        final TableViewerColumn currentCol = new TableViewerColumn((TableViewer) currentViewer, SWT.FILL);
        currentCol.getColumn().setText(Messages.getString("RequirementPropertySection.1")); //$NON-NLS-1$
        currentCol.getColumn().setWidth(500);     
        
        hookListeners();
    }
    
    /**
     * Create the tree in the property section
     * 
     * @param parent the parent composite
     */
    public void createTree(Composite parent)
    {          
        parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
        final Tree tree = getWidgetFactory().createTree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        tree.setLayout(new FillLayout());

        currentViewer = new TreeViewer(tree, SWT.FILL);
       
        hookListeners();
    }
    
    /**
     * @see org.eclipse.ui.views.properties.tabbed.ISection#setInput(org.eclipse.ui.IWorkbenchPart,
     *      org.eclipse.jface.viewers.ISelection)
     */
    public void setInput(IWorkbenchPart part, ISelection selection)
    {
        super.setInput(part, selection);
        if (selection instanceof IStructuredSelection)
        {
            Object ssel = ((IStructuredSelection) selection).getFirstElement();
            EObject eObject = ObjectAdapter.adaptObject(ssel);
              
            EditingDomain domain = TopcasedAdapterFactoryEditingDomain.getEditingDomainFor(eObject);
            Resource resource = RequirementUtils.getRequirementModel(domain);
            if (resource != null)
            {
                if (currentViewer instanceof TableViewer)
                {
                    currentViewer.setLabelProvider(new RequirementPropertyTableLabelProvider(RequirementUtils.getAdapterFactory()));
                    currentViewer.setContentProvider(new RequirementPropertyTableContentProvider(RequirementUtils.getAdapterFactory()));
                }
                else
                {
                    currentViewer.setLabelProvider(new RequirementPropertyTreeLabelProvider(RequirementUtils.getAdapterFactory()));
                    currentViewer.setContentProvider(new RequirementPropertyTreeContentProvider(RequirementUtils.getAdapterFactory()));
                }
                HierarchicalElement hierarchicalElement = RequirementUtils.getHierarchicalElementFor(eObject);
                if (hierarchicalElement != null)
                {  
                    currentViewer.setInput(hierarchicalElement.getRequirement());
                }
                else
                {           
                    currentViewer.setInput(null);
                }
            }
        }        
    }

    /**
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#aboutToBeShown()
     */
    @Override
    public void aboutToBeShown()
    {
        super.aboutToBeShown();
        
        RequirementUtils.fireIsSectionEnabledVariableChanged(true);
    }
    
    /**
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#aboutToBeHidden()
     */
    @Override
    public void aboutToBeHidden()
    {
        super.aboutToBeHidden();
        
        RequirementUtils.fireIsSectionEnabledVariableChanged(false);
    }
    
    /**
     * Listener to refresh the viewer when the model is modified from the current requirement view
     */
    private INotifyChangedListener modelListener = new INotifyChangedListener()
    {
        public void notifyChanged(Notification msg)
        {
            if (!currentViewer.getControl().isDisposed())
            {
                refresh();
            }
        }
    };

    /**
     * Refreshes the viewer
     * 
     * @param updateLabel : true if the label is update
     */
    private final void refreshViewer(final boolean updateLabel)
    {
        if (Display.getCurrent() != Display.getDefault())
        {
            syncRefreshViewer(updateLabel);
        }
        else
        {
            currentViewer.refresh(updateLabel);         
        }
    }

    /**
     * Refreshes the viewer
     * 
     * @param updateLabel : true if the label is update
     */
    private void syncRefreshViewer(final boolean updateLabel)
    {            
        currentViewer.getControl().getDisplay().syncExec(new Runnable()
        {
            public void run()
            {
                currentViewer.refresh(updateLabel);
            }
        });
    }

    /**
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#refresh()
     */
    public void refresh()
    {
        refreshViewer(true);
    }

    /**
     * All available space must be used.
     * 
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#shouldUseExtraSpace()
     */
    public boolean shouldUseExtraSpace()
    {
        return true;
    }

    /**
     * @return the current viewer
     */
    public ColumnViewer getViewer()
    {
        return currentViewer;
    }    

    /**
     * @return the parent composite
     */
    public Composite getParentCompo()
    {
        return parentCompo;
    }
}
