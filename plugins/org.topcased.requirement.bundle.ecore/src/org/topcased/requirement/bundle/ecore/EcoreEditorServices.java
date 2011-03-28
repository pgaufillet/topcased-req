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
 *	David Ribeiro (Atos Origin}) {david.ribeirocampelo@atosorigin.com}
 *
 *****************************************************************************/
package org.topcased.requirement.bundle.ecore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecoretools.diagram.part.EcoreDiagramEditor;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResource;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.impl.DiagramImpl;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.bundle.ecore.helper.advice.EObjectHelperAdvice;
import org.topcased.requirement.bundle.ecore.internal.Messages;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.views.AbstractRequirementPage;
import org.topcased.requirement.util.RequirementResourceImpl;

/**
 * The Class EcoreEditorServices.
 */
public class EcoreEditorServices implements IEditorServices
{

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.requirement.core.extensions.IEditorServices#getRequirementsResource(org.eclipse.ui.IEditorPart)
     */
    public Resource getRequirementsResource(IEditorPart editor)
    {
        RequirementResourceImpl requirementResource = null;
        ResourceSet resourceSet = getEditingDomain(editor).getResourceSet();
        for (Resource resource : resourceSet.getResources())
        {
            if (resource instanceof RequirementResourceImpl)
            {
                requirementResource = (RequirementResourceImpl) resource;
                break;
            }
        }
        return requirementResource;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.requirement.core.extensions.IEditorServices#getModelResource(org.eclipse.ui.IEditorPart)
     */
    public Resource getModelResource(IEditorPart editor)
    {

        ResourceSet resourceSet = getEditingDomain(editor).getResourceSet();
        Resource linkedTargetModel = null;
        for (Resource resource : resourceSet.getResources())
        {
            if (resource instanceof GMFResource)
            {
                EObject diagram = (DiagramImpl) resource.getContents().get(0);
                if (diagram != null && diagram instanceof Diagram)
                {
                    EObject ePackage = ((Diagram) diagram).getElement();
                    if (ePackage != null && ePackage instanceof EPackage)
                    {
                        EAnnotation eAnnotation = ((EPackage) ePackage).getEAnnotation("http://www.topcased.org/requirements");

                        if (eAnnotation != null)
                        {
                            linkedTargetModel = ePackage.eResource();
                        }
                    }
                }
            }
        }
        return linkedTargetModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.requirement.core.extensions.IEditorServices#getEditingDomain(org.eclipse.ui.IEditorPart)
     */
    public EditingDomain getEditingDomain(IEditorPart editor)
    {
        if (editor == null)
        {
            editor = getActiveEditor();
        }
        if (editor instanceof EcoreDiagramEditor)
        {
            Object domainProvider = editor.getAdapter(IEditingDomainProvider.class);

            if (domainProvider instanceof IEditingDomainProvider)
            {
                return ((IEditingDomainProvider) domainProvider).getEditingDomain();
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.requirement.core.extensions.IEditorServices#getEObject(org.eclipse.gef.EditPart)
     */
    public EObject getEObject(EditPart part)
    {
        if (part instanceof GraphicalEditPart)
        {
            EObject element = ((GraphicalEditPart) part).resolveSemanticElement();
            return element;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.requirement.core.extensions.IEditorServices#canCreateRequirement(org.eclipse.gef.EditPart)
     */
    public boolean canCreateRequirement(EditPart part)
    {
        if (part instanceof GraphicalEditPart)
        {
            // do not accept diagram
            return !(part instanceof DiagramEditPart || ((GraphicalEditPart) part).getModel() instanceof Diagram);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.requirement.core.extensions.IEditorServices#getActiveEditor()
     */
    public IEditorPart getActiveEditor()
    {
        return EObjectHelperAdvice.getMultiDiagramEditor();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.requirement.core.extensions.IEditorServices#getAllModelObjects(org.eclipse.emf.ecore.EObject,
     * org.eclipse.emf.ecore.EClassifier)
     */
    public Collection<EObject> getAllModelObjects(EObject eobjectInModelResource, EClassifier ptype)
    {
        List<EObject> modelObjects = new ArrayList<EObject>();
        TreeIterator<EObject> eAllContents = eobjectInModelResource.eAllContents();
        while (eAllContents.hasNext())
        {

            EObject next = eAllContents.next();

            if (ptype.getInstanceClass().isInstance(next))
            {
                modelObjects.add(next);
            }
        }
        return modelObjects;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.requirement.core.extensions.IEditorServices#getGraphicalViewer(org.eclipse.ui.IEditorPart)
     */
    public EditPartViewer getGraphicalViewer(IEditorPart editor)
    {
        if (editor == null)
        {
            editor = getActiveEditor();
        }
        if (editor instanceof EcoreDiagramEditor)
        {
            Object viewer = editor.getAdapter(GraphicalViewer.class);
            if (viewer instanceof EditPartViewer)
            {
                return (EditPartViewer) viewer;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.requirement.core.extensions.IEditorServices#refreshActiveDiagram(org.eclipse.ui.IEditorPart)
     */
    public void refreshActiveDiagram(IEditorPart editor)
    {
        if (editor == null)
        {
            editor = getActiveEditor();
        }
        if (editor instanceof EcoreDiagramEditor)
        {
            EditPartViewer viewer = getGraphicalViewer(editor);
            viewer.setContents(((DiagramEditor) editor).getDiagram());

        }
    }

    /**
     * Gets the e object views.
     * 
     * @param element the element
     * @return the e object views
     */
    public static List getEObjectViews(EObject element)
    {
        List views = new ArrayList();
        if (element != null)
        {
            EReference[] features = {NotationPackage.eINSTANCE.getView_Element()};
            views.addAll(EMFCoreUtil.getReferencers(element, features));
        }
        return views;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.requirement.core.extensions.IEditorServices#gotoEObjects(org.eclipse.ui.IEditorPart,
     * java.util.List, boolean)
     */
    public void gotoEObjects(IEditorPart editor, List<EObject> listOfEObjects, boolean openDiagramIfNeeded)
    {
        if (editor == null)
        {
            editor = getActiveEditor();
        }
        if (editor instanceof EcoreDiagramEditor)
        {
            List<EditPart> editPartsToSelect = new ArrayList<EditPart>(listOfEObjects.size());
            EditPartViewer currentViewer = getGraphicalViewer(editor);

            Map< ? , ? > currentRegistry = currentViewer.getEditPartRegistry();
            // select edit parts in the current viewer
            for (EObject toSelect : listOfEObjects)
            {
                List< ? > views = getEObjectViews(toSelect);
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.requirement.core.extensions.IEditorServices#makeReadOnlyException(org.eclipse.emf.ecore.resource
     * .Resource)
     */
    public RuntimeException makeReadOnlyException(Resource resource)
    {
        String msg = NLS.bind(Messages.getString("ReadOnlyExceptionMessage.0"), resource.getURI());//$NON-NLS-1$        
        Exception rollback = new RollbackException(new Status(IStatus.CANCEL, Activator.PLUGIN_ID, msg));
        return new WrappedException(rollback);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.requirement.core.extensions.IEditorServices#hookRequirementDropListeners(org.topcased.requirement
     * .core.views.AbstractRequirementPage)
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.requirement.core.extensions.IEditorServices#unhookRequirementDropListeners(org.topcased.requirement
     * .core.views.AbstractRequirementPage)
     */
    public void unhookRequirementDropListeners(AbstractRequirementPage abstractRequirementPage)
    {
        DropListenersManager.getInstance().removeListeners(abstractRequirementPage);

    }

}
