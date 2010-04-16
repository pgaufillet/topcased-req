/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 *               Maxime AUDRAIN (CS) - API Changes
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.dnd;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.modeler.edit.DiagramEditPart;
import org.topcased.modeler.edit.IModelElementEditPart;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.commands.CreateCurrentReqCommand;
import org.topcased.requirement.core.commands.CreateRequirementCommand;
import org.topcased.requirement.core.extensions.DropRestrictionManager;
import org.topcased.requirement.core.extensions.ISpecificDropAction;
import org.topcased.requirement.core.extensions.SpecificDropActionDescriptor;
import org.topcased.requirement.core.extensions.SpecificDropActionManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentPage;
import org.topcased.requirement.core.views.current.CurrentRequirementView;

import ttm.Document;
import ttm.Requirement;
import ttm.Section;

/**
 * Listener that manages the drag from the requirements view to an EditPart.<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class RequirementDropListener extends AbstractTransferDropTargetListener
{

    private EditPart currentPart;

    /**
     * the command who will be executed private Command cmdWrapp;
     * 
     * /** Constructor
     * 
     * @param viewer the GraphicalViewer of the editing window
     */
    public RequirementDropListener(GraphicalViewer viewer)
    {
        super(viewer, RequirementTransfer.getInstance());
    }

    /**
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#createTargetRequest()
     */
    protected Request createTargetRequest()
    {
        return new Request(org.eclipse.gef.RequestConstants.REQ_SELECTION);
    }

    /**
     * Updates the Request with the corresponding location
     * 
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#updateTargetRequest()
     */
    @Override
    protected void updateTargetRequest()
    {
        // the request does not need to be updated....
    }

    /**
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
     */
    @Override
    public void drop(DropTargetEvent event)
    {
        if (currentPart != null)
        {
            EObject eobject = getEObject();

            if (eobject != null)
            {
                CurrentRequirementView view = (CurrentRequirementView) CurrentRequirementView.getInstance();
                if (view != null && view.getCurrentPage() instanceof CurrentPage)
                {
                    // extract source objects
                    ISelection selection = ((RequirementTransfer) getTransfer()).getSelection();
                    Collection< ? > source = extractDragSource(selection);

                    // handle specific action on drop
                    Command dropCmd = null;
                    ISpecificDropAction action = null;
                    String uri = EcoreUtil.getURI(eobject.eClass().getEPackage()).trimFragment().toString();
                    SpecificDropActionDescriptor descriptor = SpecificDropActionManager.getInstance().find(uri);
                    if (descriptor != null)
                    {
                        action = descriptor.getActionFor((EObject) eobject);
                    }
                    if (action != null)
                    {
                        dropCmd = action.createSpecificDropAction(source, eobject);
                    }
                    // default requirement creation
                    else if (eobject != null)
                    {
                        dropCmd = new CreateCurrentReqCommand(Messages.getString("CreateCurrentRequirementAction.0"));
                        ((CreateRequirementCommand) dropCmd).setRequirements(source);
                        ((CreateRequirementCommand) dropCmd).setTarget(eobject);
                    }

                    // execution of the command
                    if (dropCmd != null && dropCmd.canExecute())
                    {
                        getViewer().getEditDomain().getCommandStack().execute(new EMFtoGEFCommandWrapper(dropCmd));
                    }
                }
            }
        }
        super.drop(event);
    }

    /**
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
     */
    @Override
    public void dragOver(DropTargetEvent event)
    {
        ISelection selection = ((RequirementTransfer) getTransfer()).getSelection();
        Collection< ? > source = extractDragSource(selection);

        event.detail = DND.DROP_COPY;

        if (currentPart instanceof IModelElementEditPart)
        {
            EObject eobject = getEObject();
            URI graphicalModelName = URI.createURI(Utils.getCurrentModeler().getPartName());
            String fileExtension = graphicalModelName.fileExtension();

            // if the target is restricted by the extension dropRestriction
            if (!(DropRestrictionManager.getInstance().isDropAllowed(fileExtension, eobject)))
            {
                event.operations = DND.DROP_NONE;
                event.detail = DND.DROP_NONE;
            }
        }
        for (Object s : source)
        {
            if (!(s instanceof Requirement) && !(s instanceof org.topcased.requirement.Requirement))
            {
                event.operations = DND.DROP_NONE;
                event.detail = DND.DROP_NONE;
            }
        }
        super.dragOver(event);
    }

    /**
     * Extracts and sorts the selection.<br>
     * Actually, if the selection comes from the upstream view, a particular processing is required : upstream
     * requirements under a {@link Document} or a {@link Section} need to be copied to the new list.
     * 
     * @param object The selection provided by a viewer
     * @return sorted collection of the dragged objects
     */
    protected Collection< ? > extractDragSource(Object object)
    {
        // Transfer the data and convert the structured selection to a collection of objects.
        if (object instanceof IStructuredSelection)
        {
            Collection< ? > selection = ((IStructuredSelection) object).toList();
            Set<Object> sortedSelection = new HashSet<Object>();
            for (Object obj : selection)
            {
                // this selection comes from the upstream view. It means that all requirements need to be extracted
                if (obj instanceof Document || obj instanceof Section)
                {
                    Collection<Requirement> allRequirements = RequirementUtils.getUpstream((EObject) obj);
                    for (Requirement requirement : allRequirements)
                    {
                        sortedSelection.add(requirement);
                    }
                }
                else
                {
                    sortedSelection.add(obj);
                }
            }
            return sortedSelection;
        }
        else
        {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * Gets the semantic model object encapsulated in the targeted EditPart.
     * 
     * @return the EObject associated to the EditPart
     */
    protected EObject getEObject()
    {
        EObject semanticModelObject = null;
        if (getTargetEditPart() instanceof IModelElementEditPart)
        {
            semanticModelObject = ((IModelElementEditPart) getTargetEditPart()).getEObject();
        }
        return semanticModelObject;
    }

    @Override
    protected EditPart getTargetEditPart()
    {

        EditPart currentPartTmp = super.getTargetEditPart();
        if (currentPartTmp != null)
        {
            if (currentPartTmp instanceof DiagramEditPart)
            {
                currentPart = null;
            }
            else
            {
                currentPart = currentPartTmp;
            }
        }
        return currentPart;
    }

}
