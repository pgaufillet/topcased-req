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
package org.topcased.requirement.core.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.topcased.modeler.edit.EMFGraphEdgeEditPart;
import org.topcased.modeler.edit.EMFGraphNodeEditPart;
import org.topcased.modeler.editor.Modeler;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.Messages;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.AbstractRequirementPage;

/**
 * This actions listens to selection changes done from the editor or the outline :
 * <ul>
 * <li>done from the modeler and selects the {@link HierarchicalElement} in the tree viewer of the
 * {@link AbstractRequirementPage}.<br>
 * </li>
 * <li>done from the tree viewer of the {@link AbstractRequirementPage} and selects the corresponding {@link EditPart}
 * in the current Modeler.
 * </ul>
 * <br>
 * 
 * Update : 19 January 2009<br>
 * 
 * 
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.2.0
 * 
 */
public class LinkWithEditorAction extends Action implements ISelectionListener, ISelectionChangedListener
{
    /** Gets the preference store of the plug-in */
    private static final IPreferenceStore PS = RequirementCorePlugin.getDefault().getPreferenceStore();

    /** Constant for Link_Editor operation */
    private static final String REQUIREMENT_LINK_EDITOR = "requirementLinkEditor";

    /** Reference to the current page **/
    private AbstractRequirementPage page;

    /** Constant for the page which uses this special action */
    private final String pageConstant;

    /** Flag set during selection modification */
    private boolean isDispatching = false;

    /**
     * Constructor
     * 
     * @param page The abstract page sent to the constructor
     */
    public LinkWithEditorAction(AbstractRequirementPage thePage)
    {
        super(Messages.getString("LinkWithEditorAction.0"), IAction.AS_CHECK_BOX); //$NON-NLS-1$
        page = thePage;
        pageConstant = REQUIREMENT_LINK_EDITOR.concat("_" + page.getClass().getSimpleName());
        setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/synced.gif")); //$NON-NLS-1$
        addPropertyChangeListener(new IPropertyChangeListener()
        {
            /**
             * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
             */
            public void propertyChange(PropertyChangeEvent event)
            {
                if (event.getNewValue() == Boolean.TRUE)
                {
                    addSelectionListener();
                    addSelectionChangedListener();
                    PS.setValue(pageConstant, true);
                }
                else
                {
                    removeSelectionListener();
                    removeSelectionChangedListener();
                    PS.setValue(pageConstant, false);
                }
            }
        });
        // by default, this action is enabled
        setChecked(PS.getBoolean(pageConstant));
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

        if (part instanceof Modeler)
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
                EObject selectedElt = null;
                if (objInSelection instanceof EMFGraphNodeEditPart)
                {
                    selectedElt = ((EMFGraphNodeEditPart) objInSelection).getEObject();
                }
                if (objInSelection instanceof EMFGraphEdgeEditPart)
                {
                    selectedElt = ((EMFGraphEdgeEditPart) objInSelection).getEObject();
                }

                if (selectedElt != null && RequirementUtils.getHierarchicalElementFor(selectedElt) != null)
                {
                    toSelect.add(RequirementUtils.getHierarchicalElementFor(selectedElt));
                }
            }
            if (!toSelect.isEmpty())
            {
                page.getViewer().setSelection(new StructuredSelection(toSelect), true);
            }
        }
        isDispatching = false;
    }

    /**
     * When the Current View is linked with the modeler, try to select existing graphical occurrences selected from
     * the Current View.
     */
    protected void selectAssociatedPartsInEditor()
    {
        isDispatching = true;
        IStructuredSelection selection = (IStructuredSelection) page.getViewer().getSelection();
        Object first = selection.getFirstElement();

        if (first instanceof HierarchicalElement && getActiveModelerEditor() instanceof Modeler)
        {
            Modeler modeler = (Modeler) getActiveModelerEditor();
            HierarchicalElement hierarchicalElement = (HierarchicalElement) first;
            modeler.gotoEObject(hierarchicalElement.getElement());
        }
        isDispatching = false;
    }

    /**
     * Gets the active page.
     * 
     * @return the active modeler page.
     */
    private IWorkbenchPage getActiveModelerPage()
    {
        return RequirementCorePlugin.getActivePage();
    }

    /**
     * Gets the active modeler editor.
     * 
     * @return the active modeler editor
     */
    private IEditorPart getActiveModelerEditor()
    {
        return getActiveModelerPage().getActiveEditor();
    }
}
