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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.handlers.ICommandConstants;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.tabbedproperties.utils.ObjectAdapter;

import ttm.Requirement;
import ttm.TtmFactory;

/**
 * Section that displays a single table or a tree containing associated upstream and current requirements.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe MERTZ</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
/**
 * @author maudrain
 *
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
     *  The list of current requirements associated with the selected diagram element
     */
    List<org.topcased.requirement.Requirement> listCurrent;

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
        parentCompo = parent;
        
        super.createControls(parent, aTabbedPropertySheetPage);
                
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
    }

    
    /**
     * Create the table in the property section
     * 
     * @param parent the parent composite
     */
    public void createTable(Composite parent)
    {
        
        Composite composite = getWidgetFactory().createFlatFormComposite(parent);
        composite.setLayout(new GridLayout());
        
        final Table requirementTable = getWidgetFactory().createTable(composite, SWT.MULTI | SWT.BORDER);
        requirementTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        requirementTable.setHeaderVisible(true);
        requirementTable.setLinesVisible(true);
        
        currentViewer = new TableViewer(requirementTable);
        
        final TableViewerColumn upstreamCol = new TableViewerColumn((TableViewer) currentViewer, SWT.FILL);
        upstreamCol.getColumn().setText(Messages.getString("RequirementPropertySection.0")); //$NON-NLS-1$
        upstreamCol.getColumn().setWidth(230);

        final TableViewerColumn currentCol = new TableViewerColumn((TableViewer) currentViewer, SWT.FILL);
        currentCol.getColumn().setText(Messages.getString("RequirementPropertySection.1")); //$NON-NLS-1$
        currentCol.getColumn().setWidth(230);
        
        hookListeners();
    }
    
    /**
     * Create the tree in the property section
     * 
     * @param parent the parent composite
     */
    public void createTree(Composite parent)
    {        
        Composite composite = getWidgetFactory().createFlatFormComposite(parent);
        composite.setLayout(new GridLayout());
        
        currentViewer = new TreeViewer(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        ((TreeViewer) currentViewer).getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); 
        
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
     * Label provider for Tree labels and icons display
     * 
     * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
     */
    private class RequirementPropertyTreeLabelProvider extends AdapterFactoryLabelProvider
    {

        /**
         * @param adapterFactory
         */
        public RequirementPropertyTreeLabelProvider(AdapterFactory adapterFactory)
        {
            super(adapterFactory);
        }
        
        /**
         * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getImage(java.lang.Object)
         */
        @Override
        public Image getImage(Object object)
        {
            if (object instanceof Requirement)
            {
                return super.getImage((Requirement)object);
            }
            else if (object instanceof org.topcased.requirement.Requirement)
            {
                return super.getImage((org.topcased.requirement.Requirement)object);
            }
            return super.getImage(object);
        }
        
        /**
         * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getText(java.lang.Object)
         */
        @Override
        public String getText(Object object)
        {
            if (object instanceof Requirement)
            {
                return ((Requirement)object).getIdent();
            }
            else if (object instanceof org.topcased.requirement.Requirement)
            {
                return ((org.topcased.requirement.Requirement)object).getIdentifier();
            }
            return super.getText(object);
        }
        
    }
    
    /**
     * Content provider for Tree content to display.
     * 
     * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
     */
    private class RequirementPropertyTreeContentProvider extends AdapterFactoryContentProvider
    {
        /**
         * @param adapterFactory
         */
        public RequirementPropertyTreeContentProvider(AdapterFactory adapterFactory)
        {
            super(adapterFactory);
        }
        
        /**
         * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getElements(java.lang.Object)
         */
        @SuppressWarnings("unchecked")
        @Override
        public Object[] getElements(Object object)
        {
            listCurrent = (List<org.topcased.requirement.Requirement>) object;
            Collection<Requirement> result = new LinkedHashSet<Requirement>();
            Boolean atLeastOneUntraced = false;
            
            for (org.topcased.requirement.Requirement requirement : listCurrent)
            {
                // Selects only the current requirements not the anonymous requirements
                if (requirement instanceof CurrentRequirement)
                {
                    for (Attribute attribute : requirement.getAttribute())
                    {
                        if (attribute instanceof AttributeLink)
                        {
                            if (((AttributeLink) attribute).getValue() instanceof Requirement)
                            {
                                Requirement upstream = (Requirement) ((AttributeLink) attribute).getValue();
                                result.add(upstream);                            
                            }
                            else
                            {
                                atLeastOneUntraced = true;
                            }
                        }
                    }
                }
            }
            // If there is some untraced current requirement, create a container for those untraced requirements
            if (atLeastOneUntraced)
            {
                Requirement req = TtmFactory.eINSTANCE.createRequirement();
                req.setIdent(Messages.getString("RequirementPropertySection.2")); //$NON-NLS-1$
                ttm.Attribute att = TtmFactory.eINSTANCE.createAttribute();
                att.setParent(req);
                result.add(req);
            }
            return result.toArray();
        }
        
        /**
         * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getChildren(java.lang.Object)
         */
        @Override
        public Object[] getChildren(Object object)
        {
            if (object instanceof Requirement)
            {
                Collection<org.topcased.requirement.Requirement> result = new LinkedHashSet<org.topcased.requirement.Requirement>();
                if (!listCurrent.isEmpty())
                {
                    for (org.topcased.requirement.Requirement requirement : listCurrent)
                    {
                        // Selects only the current requirements not the anonymous requirements
                        if (requirement instanceof CurrentRequirement)
                        {
                            for (Attribute attribute : requirement.getAttribute())
                            {
                                if (attribute instanceof AttributeLink )
                                {
                                    if (((AttributeLink) attribute).getValue() instanceof Requirement)
                                    {
                                        Requirement upstream = (Requirement) ((AttributeLink) attribute).getValue();
                                        
                                        if (upstream.equals((Requirement)object))
                                        {
                                            result.add(requirement); 
                                        }    
                                    }
                                    else
                                    {   //add the untraced requirements to the fake upstream requirement container
                                        if (((Requirement)object).getIdent() == Messages.getString("RequirementPropertySection.2")) //$NON-NLS-1$
                                        {
                                            result.add(requirement);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return result.toArray();
            }
            else
            {
                return null;
            }
        }
    }
    
    
    /**
     * Label provider for Table labels and icons display
     * 
     * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
     */
    private class RequirementPropertyTableLabelProvider extends AdapterFactoryLabelProvider
    {
        public RequirementPropertyTableLabelProvider(AdapterFactory adapterFactory)
        {
            super(adapterFactory);
        }

        /**
         * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getColumnImage(java.lang.Object, int)
         */
        @Override
        public Image getColumnImage(Object element, int columnIndex)
        {
            if (element instanceof CRegistry)
            {
                CRegistry tuple = (CRegistry) element;
                if (columnIndex == 0)
                {
                    return super.getImage(tuple.getCurrent());
                }
                if (columnIndex == 1)
                {
                    if (tuple.getUpstream() != null)
                    {
                        return super.getImage(tuple.getUpstream());
                    }
                }
            }
            return super.getColumnImage(element, columnIndex);
        }

        /**
         * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex)
        {
            if (element instanceof CRegistry)
            {
                CRegistry tuple = (CRegistry) element;
                if (columnIndex == 0)
                {
                    return tuple.getCurrent().getIdentifier();
                }
                if (columnIndex == 1)
                {
                    if (tuple.getUpstream() != null)
                    {
                        return tuple.getUpstream().getIdent();
                    }
                }
            }
            return Messages.getString("RequirementPropertySection.2"); //$NON-NLS-1$
        }
    }

    /**
     * Content provider for Table content to display.
     * 
     * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
     */
    private class RequirementPropertyTableContentProvider extends AdapterFactoryContentProvider
    {
        public RequirementPropertyTableContentProvider(AdapterFactory adapterFactory)
        {
            super(adapterFactory);
        }

        /**
         * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getElements(java.lang.Object)
         */
        @SuppressWarnings("unchecked")
        public Object[] getElements(Object object)
        {
            Collection<CRegistry> result = new LinkedHashSet<CRegistry>();
            for (org.topcased.requirement.Requirement requirement : (List<org.topcased.requirement.Requirement>) object)
            {
                // Selects only the current requirements not the anonymous requirements
                Boolean atLeastOne = false;
                if (requirement instanceof CurrentRequirement)
                {
                    for (Attribute attribute : requirement.getAttribute())
                    {
                        if (attribute instanceof AttributeLink && ((AttributeLink) attribute).getValue() instanceof Requirement)
                        {
                            Requirement upstream = (Requirement) ((AttributeLink) attribute).getValue();
                            result.add(new CRegistry(requirement, upstream));
                            atLeastOne = true;
                        }
                    }

                    // If any link attribute, add the requirement without attribute
                    if (!atLeastOne)
                    {
                        result.add(new CRegistry(requirement, null));
                    }
                }
            }            
            return result.toArray();
        }
    }

    /**
     * Internal class used to store the line to display in the property table.
     * 
     * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
     */
    private class CRegistry
    {
        // current requirement
        private org.topcased.requirement.Requirement cr;

        // upstream requirement
        private Requirement up;

        public CRegistry(org.topcased.requirement.Requirement current, Requirement upstream)
        {
            up = upstream;
            cr = current;
        }

        public org.topcased.requirement.Requirement getCurrent()
        {
            return cr;
        }

        public Requirement getUpstream()
        {
            return up;
        }
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
