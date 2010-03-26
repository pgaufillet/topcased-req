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

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.tabbedproperties.utils.ObjectAdapter;
import ttm.Requirement;

/**
 * Section that displays a single table containing association upstream and current requirements.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe MERTZ</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class RequirementPropertySection extends AbstractPropertySection
{
    /**
     * The current selected object or the first object in the selection when multiple objects are selected.
     */
    private EObject eObject;

    /**
     * The table viewer containing the upstream and current requirements.
     */
    private TableViewer viewer;

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
        Composite composite = getWidgetFactory().createFlatFormComposite(parent);
        composite.setLayout(new GridLayout());

        final Table requirementTable = getWidgetFactory().createTable(composite, SWT.MULTI | SWT.BORDER);
        requirementTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        requirementTable.setHeaderVisible(true);
        requirementTable.setLinesVisible(true);

        viewer = new TableViewer(requirementTable);
        
        final TableViewerColumn upstreamCol = new TableViewerColumn(viewer, SWT.FILL);
        upstreamCol.getColumn().setText(Messages.getString("RequirementPropertySection.0")); //$NON-NLS-1$
        upstreamCol.getColumn().setWidth(230);

        final TableViewerColumn currentCol = new TableViewerColumn(viewer, SWT.FILL);
        currentCol.getColumn().setText(Messages.getString("RequirementPropertySection.1")); //$NON-NLS-1$
        currentCol.getColumn().setWidth(230);
        
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
            eObject = ObjectAdapter.adaptObject(ssel);

            EditingDomain domain = TopcasedAdapterFactoryEditingDomain.getEditingDomainFor(eObject);
            Resource resource = RequirementUtils.getRequirementModel(domain);
            if (resource != null)
            {
                viewer.setLabelProvider(new RequirementPropertyLabelProvider(RequirementUtils.getAdapterFactory()));
                viewer.setContentProvider(new RequirementPropertyContentProvider(RequirementUtils.getAdapterFactory()));
                viewer.setInput(resource);
            }
        }
    }

    /**
     * Listener to refresh the viewer when the model is modified from the current requirement view
     */
    private INotifyChangedListener modelListener = new INotifyChangedListener()
    {
        public void notifyChanged(Notification msg)
        {
            if (!viewer.getControl().isDisposed())
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
            viewer.refresh(updateLabel);
        }
    }

    /**
     * Refreshes the viewer
     * 
     * @param updateLabel : true if the label is update
     */
    private void syncRefreshViewer(final boolean updateLabel)
    {
        viewer.getControl().getDisplay().syncExec(new Runnable()
        {
            public void run()
            {
                viewer.refresh(updateLabel);
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
     * Label provider for labels and icons display
     * 
     * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
     */
    private class RequirementPropertyLabelProvider extends AdapterFactoryLabelProvider
    {
        public RequirementPropertyLabelProvider(AdapterFactory adapterFactory)
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
     * Content provider for getting content to display.
     * 
     * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
     */
    private class RequirementPropertyContentProvider extends AdapterFactoryContentProvider
    {
        public RequirementPropertyContentProvider(AdapterFactory adapterFactory)
        {
            super(adapterFactory);
        }

        /**
         * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object object)
        {
            Collection<CRegistry> result = new LinkedHashSet<CRegistry>();
            HierarchicalElement hierarchicalElement = RequirementUtils.getHierarchicalElementFor(eObject);
            if (hierarchicalElement != null)
            {
                for (org.topcased.requirement.Requirement requirement : hierarchicalElement.getRequirement())
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
}
