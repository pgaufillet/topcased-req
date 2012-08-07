/***********************************************************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.markers.MarkerItem;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Defines an abstract page common to Upstream and Current pages.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.2.0
 */
public abstract class AbstractRequirementPage extends Page implements IViewerProvider, IEditingDomainProvider, IGotoMarker
{
    protected static String firstPopupMenuSeparator = "firstSeparator"; //$NON-NLS-1$

    protected static String lastPopupMenuSeparator = "lastSeparator"; //$NON-NLS-1$

    protected TreeViewer viewer;

    protected EditingDomain editingDomain;

    protected Composite mainComposite;

    protected static ISelectionChangedListener upstreamListener = null;

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
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
        if (services != null)
        {
            services.hookRequirementDropListeners(this);
        }
    }

    /**
     * Removes listener listening to model changes.
     */
    protected void unhookListeners()
    {
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
        if (services != null)
        {
            services.unhookRequirementDropListeners(this);
        }
    }

    /**
     * @see org.eclipse.ui.part.Page#getControl()
     */
    public Control getControl()
    {
        return mainComposite;
    }

    /**
     * The setter of the editing domain
     * 
     * @param ed The editing domain
     */
    public void setEditingDomain(EditingDomain ed)
    {
        editingDomain = ed;
    }

    /**
     * The getter of the editing domain
     * 
     * @see org.eclipse.emf.edit.domain.IEditingDomainProvider#getEditingDomain()
     */
    public EditingDomain getEditingDomain()
    {
        return editingDomain;
    }

    /**
     * @see org.eclipse.emf.common.ui.viewer.IViewerProvider#getViewer()
     */
    public Viewer getViewer()
    {
        return viewer;
    }

    /**
     * @see org.eclipse.ui.part.Page#setFocus()
     */
    public void setFocus()
    {
        // do nothing
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
                IMarker marker = ((MarkerItem) object).getMarker();
                gotoMarker(marker);
            }
        }
    }

    /**
     * Select an EObject in the TreeViewer
     * 
     * @param marker The marker related to a current requirement.
     * @throws CoreException If something failed.
     */
    public void gotoMarker(IMarker marker)
    {
        if (marker != null)
        {
            String emfURI = (String) marker.getAttribute(EValidator.URI_ATTRIBUTE, null);
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
