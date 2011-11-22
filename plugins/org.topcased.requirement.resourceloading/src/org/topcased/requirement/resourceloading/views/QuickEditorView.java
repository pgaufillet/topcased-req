/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Olivier Mélois <a href="mailto:olivier.melois@atos.net">olivier.melois@atos.net</a>"
 * 
 **********************************************************************************************************************/

package org.topcased.requirement.resourceloading.views;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.topcased.requirement.core.dnd.DragSourceCurrentAdapter;
import org.topcased.requirement.core.dnd.RequirementTransfer;
import org.topcased.requirement.core.properties.RequirementPropertySheetPage;
import org.topcased.requirement.core.providers.CurrentRequirementLabelProvider;
import org.topcased.requirement.core.views.current.model.CurrentRequirementReference;
import org.topcased.requirement.presentation.RequirementViewerComposite;

/**
 * View used to display requirements from their references.
 * 
 * @author omelois
 * 
 */

public class QuickEditorView extends ViewPart implements IViewerProvider
{

    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = "org.topcased.requirement.resourceloading.views.QuickEditorView";

    protected TreeViewer viewer;

    protected ViewerPane viewerPane;

    private RequirementViewerComposite reqViewerComposite;

    protected AdapterFactoryEditingDomain editingDomain;

    private RequirementPropertySheetPage sheetPage;
    
    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    public void createPartControl(Composite parent)
    {
        this.reqViewerComposite = new RequirementViewerComposite(parent, SWT.NONE, getSite().getPage(), this);
        this.viewerPane = reqViewerComposite.getViewerPane();
        this.viewer = (TreeViewer) reqViewerComposite.getViewerPane().getViewer();
        
        AdapterFactory factory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
        AdapterFactoryContentProvider contentProvider = new AdapterFactoryContentProvider(factory);
        CurrentRequirementLabelProvider labelProvider = new CurrentRequirementLabelProvider(factory);

        // Setting the viewer's providers and the input.
        this.viewer.setContentProvider(contentProvider);
        this.viewer.setLabelProvider(labelProvider);

        //Connecting the Property Sheet to the viewer, through the site. 
        getSite().setSelectionProvider(this.viewer);

        // Drag support.
        int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
        Transfer[] transfers = new Transfer[] {RequirementTransfer.getInstance()};
        viewer.addDragSupport(dndOperations, transfers, new DragSourceCurrentAdapter(viewer));
        // viewer.addDragSupport(dndOperations, transfers, new DragSourceUpstreamAdapter(viewer));
        // Drop is not supported.
    }

    /**
     * This is used to connect the propertySheet to the viewer.
     */
    @Override
    public Object getAdapter(Class adapter)
    {
        if (adapter.equals(IPropertySheetPage.class))
        {
            sheetPage = new RequirementPropertySheetPage(new ITabbedPropertySheetPageContributor()
            {
                public String getContributorId()
                {
                    return "org.topcased.requirement.core.contributor";
                }
            });
            return sheetPage;
        }
        return super.getAdapter(adapter);
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus()
    {
        viewer.getControl().setFocus();
    }
    
    
    public void setInput(CurrentRequirementReference currentRequirementReference) {
        EObject eobject = (EObject) currentRequirementReference.getAdapter(EObject.class);
        ResourceSet resourceSet = eobject.eResource().getResourceSet();

        final URI reqUri = currentRequirementReference.getUri();

        this.viewer.setInput(resourceSet.getResource(reqUri, false));
        
        // Changing the name and icon of the view.
        this.setPartName("Requirement Viewer - " + reqUri.lastSegment());
        this.viewerPane.setTitle(currentRequirementReference.getResource());
        //this.setTitleImage((Image) currentRequirementReference.getImage(currentRequirementReference));

    }

    public Viewer getViewer()
    {
        return viewer;
    }

}