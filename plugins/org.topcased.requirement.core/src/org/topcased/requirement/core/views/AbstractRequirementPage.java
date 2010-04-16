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

import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.Page;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.dnd.RequirementDropListener;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Defines an abstract page common to Upstream and Current pages.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.2.0
 */
public abstract class AbstractRequirementPage extends Page implements IViewerProvider, IEditingDomainProvider
{
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
        RequirementUtils.getAdapterFactory().addListener(modelListener);
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
        RequirementUtils.getAdapterFactory().removeListener(modelListener);
    }

    /**
     * Listener to refresh the viewer when the model is modified from the current requirement view
     */
    private INotifyChangedListener modelListener = new INotifyChangedListener()
    {
        public void notifyChanged(Notification msg)
        {
            if (!getControl().isDisposed())
            {
                refreshViewer(true);
            }
        }
    };

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
     * Refreshes the viewer
     * 
     * @param updateLabel : true if the label is update
     */
    public final void refreshViewer(final boolean updateLabel)
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
     * @see org.eclipse.ui.part.Page#setFocus()
     */
    public void setFocus()
    {
        // do nothing
    }

    /**
     * Determines if the selection is an object of type clazz
     * 
     * @param theSelection
     * @param clazz
     * 
     * @return true if the selection is an object of type clazz
     */
    protected Boolean toDisplay(IStructuredSelection theSelection, java.lang.Class< ? > clazz)
    {
        if (theSelection == null)
        {
            return false;
        }
        for (Iterator< ? > object = theSelection.iterator(); object.hasNext();)
        {
            if (!clazz.isInstance(object.next()))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Add a listener to listen the key control
     */
    protected void hookKeyListeners()
    {
        KeyListener keyListener = new KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                if (e.keyCode == SWT.DEL)
                {
                    executeCodeForKey(getViewer().getSelection());
                }
            }
        };
        getViewer().getControl().addKeyListener(keyListener);
    }

    /**
     * Method call by the key listener.<br>
     * This method is intended to be implemented.
     * 
     * @param selection The current selection
     */
    protected abstract void executeCodeForKey(ISelection selection);

}
