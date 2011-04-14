/*****************************************************************************
 * Copyright (c) 2010 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Vincent Hemery (Atos Origin}) {vincent.hemery@atosorigin.com} - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.bundle.papyrus.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.core.modelsetquery.ModelSetQuery;
import org.eclipse.papyrus.core.utils.EditorUtils;
import org.eclipse.papyrus.diagram.common.util.DiagramEditPartsUtil;
import org.eclipse.papyrus.resource.IModel;
import org.eclipse.papyrus.resource.ModelSet;
import org.eclipse.papyrus.resource.uml.UmlModel;
import org.eclipse.papyrus.sasheditor.contentprovider.IPageMngr;
import org.eclipse.papyrus.sasheditor.editor.AbstractMultiPageSashEditor;
import org.eclipse.papyrus.sasheditor.editor.ISashWindowsContainer;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.bundle.papyrus.Activator;
import org.topcased.requirement.bundle.papyrus.internal.Messages;
import org.topcased.requirement.bundle.papyrus.resource.RequirementModel;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.views.AbstractRequirementPage;

/**
 * This class provides services for using requirements with the papyrus core editor. It is intended to be used only by
 * extension point org.topcased.requirement.core.supportingEditors.
 * 
 * @author vhemery
 */
public class PapyrusEditorServices implements IEditorServices
{

    /**
     * Get the resource containing requirements project.
     * 
     * @param editor the editor or null for currently active one
     * @return the requirement resource.
     */
    public Resource getRequirementsResource(IEditorPart editor)
    {
        ResourceSet resourceSet = getEditingDomain(editor).getResourceSet();
        if (resourceSet instanceof ModelSet)
        {
            // return model resource
            IModel model = ((ModelSet) resourceSet).getModel(RequirementModel.REQ_MODEL_ID);
            if (model instanceof RequirementModel)
            {
                return ((RequirementModel) model).getResource();
            }
        }
        return null;
    }

    /**
     * Get the resource containing the model.
     * 
     * @param editor the editor or null for currently active one
     * @return the model resource.
     */
    public Resource getModelResource(IEditorPart editor)
    {
        ResourceSet resourceSet = getEditingDomain(editor).getResourceSet();
        if (resourceSet instanceof ModelSet)
        {
            // FIXME should not be only UML
            // return model resource
            IModel model = ((ModelSet) resourceSet).getModel(UmlModel.MODEL_ID);
            if (model instanceof UmlModel)
            {
                return ((UmlModel) model).getResource();
            }
        }
        return null;
    }

    /**
     * Get the editing domain.
     * 
     * @param editor the editor or null for currently active one
     * @return the editing domain.
     */
    public EditingDomain getEditingDomain(IEditorPart editor)
    {
        if (editor == null)
        {
            editor = getActiveEditor();
        }
        if (editor instanceof IMultiDiagramEditor)
        {
            Object domain = editor.getAdapter(EditingDomain.class);
            if (domain instanceof EditingDomain)
            {
                return (EditingDomain) domain;
            }
        }
        return null;
    }

    /**
     * Get the eobject represented by an edit part.
     * 
     * @param part edit part.
     * @return represented model eobject.
     */
    public EObject getEObject(EditPart part)
    {
        EObject element = null;
        if (part instanceof GraphicalEditPart)
        {
            element = ((GraphicalEditPart) part).resolveSemanticElement();
        }
        else if (part instanceof ConnectionEditPart)
        {
            element = ((ConnectionEditPart) part).resolveSemanticElement();
        }
        return element;
    }

    /**
     * Test if requirements can be created on a given edit part.
     * 
     * @param part edit part.
     * @return true if requirements can be created on this part.
     */
    public boolean canCreateRequirement(EditPart part)
    {
        if (part instanceof AbstractGraphicalEditPart)
        {
            // do not accept diagram
            return !(part instanceof DiagramEditPart || ((AbstractGraphicalEditPart) part).getModel() instanceof Diagram);
        }
        return false;
    }

    /**
     * Get the currently active Papyrus diagram editor.
     * 
     * @return the editor part or null.
     */
    public IEditorPart getActiveEditor()
    {
        return EditorUtils.getMultiDiagramEditor();
    }

    /**
     * Return all the model objects of specified type
     * 
     * @param eobjectInModelResource the model object from which to search starts
     * @param type the searched type
     * @return the list of instances of the specified type
     */
    public Collection<EObject> getAllModelObjects(EObject eobjectInModelResource, EClassifier type)
    {
        return ModelSetQuery.getObjectsOfType(eobjectInModelResource, type);
    }

    /**
     * Get the graphical viewer of the editor.
     * 
     * @param editor the editor or null for currently active one
     * @return the graphical viewer.
     */
    public EditPartViewer getGraphicalViewer(IEditorPart editor)
    {
        if (editor == null)
        {
            editor = getActiveEditor();
        }
        if (editor instanceof IMultiDiagramEditor)
        {
            Object viewer = editor.getAdapter(IDiagramGraphicalViewer.class);
            if (viewer instanceof EditPartViewer)
            {
                return (EditPartViewer) viewer;
            }
        }
        return null;
    }

    /**
     * Refresh the active diagram of the editor
     * 
     * @param editor the editor or null for currently active one
     */
    public void refreshActiveDiagram(IEditorPart editor)
    {
        if (editor == null)
        {
            editor = getActiveEditor();
        }
        if (editor instanceof IMultiDiagramEditor)
        {
            IEditorPart active = ((IMultiDiagramEditor) editor).getActiveEditor();
            if (active instanceof DiagramEditor)
            {
                EditPartViewer viewer = getGraphicalViewer(editor);
                viewer.setContents(((DiagramEditor) active).getDiagram());
            }
        }
    }

    /**
     * Sets the selection state for the editor to reveal the position of the given EObject.<br>
     * It searches in the different diagrams the occurrences of the EObject and ask the user to select the destination
     * if the given EObject has several graphical representation.
     * 
     * @param editor the editor or null for currently active one
     * @param listOfEObjects list of eobjects to select
     * @param openDiagramIfNeeded if true, when the object is not in current active diagram, a representing diagram will
     *        open
     */
    public void gotoEObjects(IEditorPart editor, List<EObject> listOfEObjects, boolean openDiagramIfNeeded)
    {
        if (editor == null)
        {
            editor = getActiveEditor();
        }
        if (editor instanceof IMultiDiagramEditor)
        {
            List<EditPart> editPartsToSelect = new ArrayList<EditPart>(listOfEObjects.size());
            // find appropriate viewer and edit parts to select
            EditPartViewer currentViewer = getGraphicalViewer(editor);
            Map< ? , ? > currentRegistry = currentViewer.getEditPartRegistry();
            if (openDiagramIfNeeded)
            {
                /*
                 * Reference diagrams which represent these elements to know the best one (with most represented
                 * elements)
                 */
                Map<Diagram, Integer> numberOfRepresentedElementsByDiagram = new HashMap<Diagram, Integer>();
                for (EObject toSelect : listOfEObjects)
                {
                    Set<Diagram> diagramsRepresentingThisObject = new HashSet<Diagram>();
                    List< ? > views = DiagramEditPartsUtil.getEObjectViews(toSelect);
                    for (Object view : views)
                    {
                        if (view instanceof View && !(view instanceof Diagram))
                        {
                            Diagram diagram = ((View) view).getDiagram();
                            // test if a representation has already been counted for this diagram
                            if (!diagramsRepresentingThisObject.contains(diagram))
                            {
                                // the element is represented in this diagram
                                diagramsRepresentingThisObject.add(diagram);
                                if (!numberOfRepresentedElementsByDiagram.containsKey(diagram))
                                {
                                    numberOfRepresentedElementsByDiagram.put(diagram, 0);
                                }
                                Integer num = numberOfRepresentedElementsByDiagram.get(diagram);
                                numberOfRepresentedElementsByDiagram.put(diagram, num + 1);
                            }
                        }
                    }
                }
                // find diagram where the largest number of elements are available
                Diagram bestDiagram = null;
                int numberOfRepresented = 0;
                for (Entry<Diagram, Integer> entry : numberOfRepresentedElementsByDiagram.entrySet())
                {
                    if (entry.getValue() > numberOfRepresented)
                    {
                        // take better diagram
                        bestDiagram = entry.getKey();
                        numberOfRepresented = entry.getValue();
                    }
                    else if (entry.getValue() == numberOfRepresented)
                    {
                        // take the currently opened diagram if it is as good
                        if (currentRegistry.get(entry.getKey()) != null)
                        {
                            bestDiagram = entry.getKey();
                        }
                    }
                }
                // change active diagram if necessary
                if (bestDiagram != null && currentRegistry.get(bestDiagram) == null)
                {
                    Object pageMngr = editor.getAdapter(IPageMngr.class);
                    if (pageMngr instanceof IPageMngr)
                    {
                        IPageMngr pageManager = (IPageMngr) pageMngr;
                        if (pageManager.isOpen(bestDiagram))
                        {
                            // bring to top
                            pageManager.closePage(bestDiagram);
                            pageManager.openPage(bestDiagram);
                        }
                        else
                        {
                            // open
                            pageManager.openPage(bestDiagram);
                        }
                    }
                    currentViewer = getGraphicalViewer(editor);
                    currentRegistry = currentViewer.getEditPartRegistry();
                }
            }
            // select edit parts in the current viewer
            for (EObject toSelect : listOfEObjects)
            {
                List< ? > views = DiagramEditPartsUtil.getEObjectViews(toSelect);
                for (Object view : views)
                {
                    if (currentRegistry.containsKey(view))
                    {
                        Object part = currentRegistry.get(view);
                        if (part instanceof EditPart)
                        {
                            editPartsToSelect.add((EditPart) part);
                        }
                    }
                }
            }
            // select edit parts if selection has to change
            if (!(editPartsToSelect.containsAll(currentViewer.getSelectedEditParts()) && currentViewer.getSelectedEditParts().contains(editPartsToSelect)))
            {
                boolean firstSelectedPartInViewer = true;
                for (EditPart editPart : editPartsToSelect)
                {
                    if (firstSelectedPartInViewer)
                    {
                        currentViewer.select(editPart);
                        firstSelectedPartInViewer = false;
                    }
                    else
                    {
                        currentViewer.appendSelection(editPart);
                    }
                }
            }
        }
    }

    /**
     * Make a specific exception for the corresponding command stack to notify the user the resource is read only
     * 
     * @param resource the resource which is read-only
     */
    public RuntimeException makeReadOnlyException(Resource resource)
    {
        String msg = NLS.bind(Messages.getString("ReadOnlyExceptionMessage.0"), resource.getURI());//$NON-NLS-1$        
        Exception rollback = new RollbackException(new Status(IStatus.CANCEL, Activator.PLUGIN_ID, msg));
        return new WrappedException(rollback);
    }

    /**
     * Handle the creation of listeners for drop from the requirement page
     * 
     * @param abstractRequirementPage page from which the drop can occur
     */
    public void hookRequirementDropListeners(AbstractRequirementPage abstractRequirementPage)
    {
        final IEditorPart editor = getActiveEditor();
        EditPartViewer viewer = getGraphicalViewer(editor);
        // register listener to current editor
        if (viewer instanceof IDiagramGraphicalViewer)
        {
            DropListenersManager.getInstance().registerDropListener(abstractRequirementPage, (IDiagramGraphicalViewer) viewer);
        }
        // handle creation of new listeners for new editor pages
        if (editor instanceof AbstractMultiPageSashEditor)
        {
            ISashWindowsContainer sash = ((AbstractMultiPageSashEditor) editor).getISashWindowsContainer();
            DropListenersManager.getInstance().listenPageChanges(sash, abstractRequirementPage);
        }
    }

    /**
     * Remove all created listeners for drop from the requirement page
     * 
     * @param abstractRequirementPage page from which the drop could occur
     */
    public void unhookRequirementDropListeners(AbstractRequirementPage abstractRequirementPage)
    {
        DropListenersManager.getInstance().removeListeners(abstractRequirementPage);
    }
}
