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
package org.topcased.requirement.core.extensions;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.core.views.AbstractRequirementPage;

/**
 * This interface describes the services which must be specifically implemented for each supporting editor.
 * 
 * @author vhemery
 */
public interface IEditorServices
{
    /**
     * Get the resource containing requirements project.
     * 
     * @param editor the editor or null for currently active one
     * @return the requirement resource.
     */
    public Resource getRequirementsResource(IEditorPart editor);

    /**
     * Get the resource containing the model.
     * 
     * @param editor the editor or null for currently active one
     * @return the model resource.
     */
    public Resource getModelResource(IEditorPart editor);

    /**
     * Get the editing domain.
     * 
     * @param editor the editor or null for currently active one
     * @return the editing domain.
     */
    public EditingDomain getEditingDomain(IEditorPart editor);

    /**
     * Get the eobject represented by an edit part.
     * 
     * @param part edit part.
     * @return represented model eobject, or null if part is a diagram or has no eobject.
     */
    public EObject getEObject(EditPart part);

    /**
     * Test if requirements can be created on a given edit part.
     * 
     * @param part edit part.
     * @return true if requirements can be created on this part.
     */
    public boolean canCreateRequirement(EditPart part);

    /**
     * Get the currently active editor of the appropriated type.
     * 
     * @return the editor part or null.
     */
    public IEditorPart getActiveEditor();

    /**
     * Return all the model objects of specified type
     * 
     * @param eobjectInModelResource the model object from which to search starts
     * @param type the searched type
     * @return the list of instances of the specified type
     */
    public Collection<EObject> getAllModelObjects(EObject eobjectInModelResource, EClassifier type);

    /**
     * Get the graphical viewer of the editor.
     * 
     * @param editor the editor or null for currently active one
     * @return the graphical viewer.
     */
    public EditPartViewer getGraphicalViewer(IEditorPart editor);

    /**
     * Refresh the active diagram of the editor
     * 
     * @param editor the editor or null for currently active one
     */
    public void refreshActiveDiagram(IEditorPart editor);

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
    public void gotoEObjects(IEditorPart editor, List<EObject> listOfEObjects, boolean openDiagramIfNeeded);

    /**
     * Make a specific exception for the corresponding command stack to notify the user the resource is read only
     * 
     * @param resource the resource which is read-only
     */
    public RuntimeException makeReadOnlyException(Resource resource);

    /**
     * Handle the creation of listeners for drop from the requirement page
     * 
     * @param abstractRequirementPage page from which the drop can occur
     */
    public void hookRequirementDropListeners(AbstractRequirementPage abstractRequirementPage);

    /**
     * Remove all created listeners for drop from the requirement page
     * 
     * @param abstractRequirementPage page from which the drop could occur
     */
    public void unhookRequirementDropListeners(AbstractRequirementPage abstractRequirementPage);

}
