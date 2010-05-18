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

import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.Page;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.dnd.RequirementDropListener;

/**
 * Defines an abstract page common to Upstream and Current pages.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.2.0
 */
public abstract class AbstractRequirementPage extends Page implements IViewerProvider, IEditingDomainProvider
{
    protected static String firstPopupMenuSeparator = "firstSeparator"; //$NON-NLS-1$

    protected static String lastPopupMenuSeparator = "lastSeparator"; //$NON-NLS-1$

    protected TreeViewer viewer;

    protected EditingDomain editingDomain;

    protected Composite mainComposite;

    private TransferDropTargetListener listener = null;

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
        Modeler modeler = Utils.getCurrentModeler();
        if (modeler != null)
        {
            listener = new RequirementDropListener(modeler.getGraphicalViewer());
            modeler.getGraphicalViewer().addDropTargetListener(listener);
        }
    }

    /**
     * Removes listener listening to model changes.
     */
    protected void unhookListeners()
    {
        Modeler modeler = Utils.getCurrentModeler();
        if (modeler != null)
        {
            modeler.getGraphicalViewer().removeDropTargetListener(listener);
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
}
