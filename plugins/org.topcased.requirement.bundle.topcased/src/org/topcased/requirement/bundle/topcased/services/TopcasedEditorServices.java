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
package org.topcased.requirement.bundle.topcased.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.ui.IEditorPart;
import org.topcased.modeler.diagrams.model.Diagrams;
import org.topcased.modeler.edit.DiagramEditPart;
import org.topcased.modeler.edit.IModelElementEditPart;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.exceptions.EditingDomainReadOnlyException;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.views.AbstractRequirementPage;
import org.topcased.requirement.util.RequirementResourceImpl;
import org.topcased.tabbedproperties.utils.ITypeCacheAdapter;
import org.topcased.tabbedproperties.utils.TypeCacheAdapter;

/**
 * This class provides services for using requirements with the topcased editor. It is intended to be used only by
 * extension point org.topcased.requirement.core.supportingEditors.
 * 
 * @author vhemery
 */
public class TopcasedEditorServices implements IEditorServices
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
        for (Resource resource : resourceSet.getResources())
        {
            if (resource instanceof RequirementResourceImpl)
            {
                return resource;
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
        for (Resource resource : resourceSet.getResources())
        {
            URI uriResource = resource.getURI();
            if (uriResource != null && uriResource.fileExtension() != null)
            {
                if (uriResource.fileExtension().endsWith("di")) //$NON-NLS-1$
                {
                    String uri = null;
                    EObject root = resource.getContents().get(0);
                    if (root instanceof Diagrams)
                    {
                        Diagrams di = (Diagrams) root;
                        uri = EcoreUtil.getURI(di.getModel()).trimFragment().toString();
                    }
                    Resource targetModel = resourceSet.getResource(URI.createURI(uri), true);
                    if (targetModel != null)
                    {
                        return targetModel;
                    }
                }
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
        if (editor instanceof Modeler)
        {
            return ((Modeler) editor).getEditingDomain();
        }
        return null;
    }

    /**
     * Get the eobject represented by an edit part.
     * 
     * @param part edit part.
     * @return represented model eobject, or null if part is a diagram or has no eobject.
     */
    public EObject getEObject(EditPart part)
    {
        if (part instanceof IModelElementEditPart)
        {
            return ((IModelElementEditPart) part).getEObject();
        }
        return null;
    }

    /**
     * Test if requirements can be created on a given edit part.
     * 
     * @param part edit part.
     * @return true if requirements can be created on this part.
     */
    public boolean canCreateRequirement(EditPart part)
    {
        // do not accept diagram
        return !(part instanceof DiagramEditPart) && part instanceof IModelElementEditPart;
    }

    /**
     * Get the currently active Papyrus diagram editor.
     * 
     * @return the editor part or null.
     */
    public IEditorPart getActiveEditor()
    {
        return Utils.getCurrentModeler();
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
        ITypeCacheAdapter typeCacheAdapter = TypeCacheAdapter.getExistingTypeCacheAdapter(eobjectInModelResource);
        return new ArrayList<EObject>(typeCacheAdapter.getReachableObjectsOfType(eobjectInModelResource, type));
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
        if (editor instanceof Modeler)
        {
            return ((Modeler) editor).getGraphicalViewer();
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
        if (editor instanceof Modeler)
        {
            ((Modeler) editor).refreshActiveDiagram();
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
        if (editor instanceof Modeler)
        {
            ((Modeler) editor).gotoEObjects(listOfEObjects, openDiagramIfNeeded);
        }
    }

    /**
     * Make a specific exception for the corresponding command stack to notify the user the resource is read only
     * 
     * @param resource the resource which is read-only
     */
    public RuntimeException makeReadOnlyException(Resource resource)
    {
        return new EditingDomainReadOnlyException(resource);
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
        // register listener to the only existing viewer
        DropListenersManager.getInstance().registerDropListener(abstractRequirementPage, viewer);
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
