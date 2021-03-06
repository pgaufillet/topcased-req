/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/
package org.topcased.requirement.core.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandlerWithState;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * Handler for the link with editor command in the current requirement view
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class LinkWithEditorHandler extends AbstractHandlerWithState implements ISelectionListener, ISelectionChangedListener
{
    /** Reference to the current page **/
    private CurrentPage page;

    /** Flag set during selection modification */
    private boolean isDispatching = false;

    /**
     * Set when selection is in modification
     * 
     * @param isDispatching true if selection is in modification, false if modification ends
     */
    public void setDispatching(boolean isDispatching)
    {
        this.isDispatching = isDispatching;
    }

    /**
     * Adds a selection listener on the page when the action is checked
     */
    private void addSelectionListener()
    {
        page.getSite().getPage().addSelectionListener(this);
    }

    /**
     * Adds a selection changed listener to the viewer of the page
     */
    private void addSelectionChangedListener()
    {
        page.setLinkWithEditorHandler(this);
        page.getViewer().addSelectionChangedListener(this);
    }

    /**
     * Removes the selection listener on the page when the action is unchecked
     */
    private void removeSelectionListener()
    {
        page.getSite().getPage().removeSelectionListener(this);
    }

    /**
     * Removes selection changed listener on the viewer of the page
     */
    private void removeSelectionChangedListener()
    {
        page.getViewer().removeSelectionChangedListener(this);
    }

    /**
     * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart,
     *      org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IWorkbenchPart part, ISelection selection)
    {
        if (isDispatching)
        {
            return;
        }

        if (part instanceof IEditorPart && SupportingEditorsManager.getInstance().getKey((IEditorPart) part) != null)
        {
            // synchronize the selection in the Current Page
            syncSelection(selection);
        }

    }

    /**
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event)
    {
        if (isDispatching)
        {
            return;
        }

        // try to select element in the modeler
        selectAssociatedPartsInEditor();
    }

    /**
     * Synchronize the Current View content with selection done in the Modeler
     * 
     * @param selection The selection coming from the modeler
     */
    protected void syncSelection(ISelection selection)
    {
        isDispatching = true;
        List<EObject> toSelect = new ArrayList<EObject>();
        if (selection != null && !selection.isEmpty() && selection instanceof StructuredSelection)
        {
            StructuredSelection selectionDone = (StructuredSelection) selection;
            for (Object objInSelection : selectionDone.toArray())
            {
                IEditorPart editor = RequirementUtils.getCurrentEditor();
                IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
                EObject selectedElt = null;
                if (services != null && objInSelection instanceof EditPart)
                {
                    selectedElt = services.getEObject((EditPart) objInSelection);
                }
                if (selectedElt != null && RequirementUtils.getHierarchicalElementFor(selectedElt) != null)
                {
                    toSelect.add(RequirementUtils.getHierarchicalElementFor(selectedElt));
                }
            }
            if (!toSelect.isEmpty())
            {
                page.setSelection(new StructuredSelection(toSelect));
            }
        }
        isDispatching = false;
    }

    /**
     * When the Current View is linked with the modeler, try to select existing graphical occurrences selected from the
     * Current View.
     */
    protected void selectAssociatedPartsInEditor()
    {
        isDispatching = true;
        IStructuredSelection selection = (IStructuredSelection) page.getViewer().getSelection();
        List<EObject> list = new ArrayList<EObject>(selection.size());
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
        if (services != null)
        {
            for (Iterator< ? > i = selection.iterator(); i.hasNext();)
            {
                Object first = i.next();
                if (first instanceof HierarchicalElement)
                {
                    HierarchicalElement hierarchicalElement = (HierarchicalElement) first;
                    list.add(hierarchicalElement.getElement());
                }
            }
            if (!list.isEmpty())
            {
                services.gotoEObjects(editor, list, true);
            }
        }
        isDispatching = false;
    }

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        HandlerUtil.toggleCommandState(event.getCommand());
        return null;
    }

    /**
     * @see org.eclipse.core.commands.AbstractHandlerWithState#handleStateChange(org.eclipse.core.commands.State,
     *      java.lang.Object)
     */
    public void handleStateChange(State state, Object oldValue)
    {
        page = RequirementHelper.INSTANCE.getCurrentPage();
        if (page != null)
        {
            if (state.getValue().equals(true))
            {
                addSelectionListener();
                addSelectionChangedListener();
            }
            else
            {
                removeSelectionListener();
                removeSelectionChangedListener();
            }
        }
    }

    @Override
    public void setEnabled(Object evaluationContext)
    {
        super.setEnabled(evaluationContext);
        if (page == null)
        {
            // if handler is enabled, at startup, synchronize
            page = RequirementHelper.INSTANCE.getCurrentPage();
            if (page != null && isEnabled())
            {
                addSelectionListener();
                addSelectionChangedListener();
            }
        }
    }
}
