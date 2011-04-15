/***********************************************************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
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

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.topcased.requirement.core.commands.CreateCurrentReqCommand;
import org.topcased.requirement.core.commands.CreateRequirementCommand;
import org.topcased.requirement.core.extensions.DropRestrictionManager;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.ISpecificDropAction;
import org.topcased.requirement.core.extensions.SpecificDropActionDescriptor;
import org.topcased.requirement.core.extensions.SpecificDropActionManager;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentRequirementView;
import org.topcased.requirement.core.views.upstream.UpstreamRequirementView;

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
    public RequirementDropListener(EditPartViewer viewer)
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
            IRunnableWithProgress runnable = new IRunnableWithProgress()
            {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                    doDrop();
                }
            };
            runnable = RequirementHelper.INSTANCE.encapsulateEMFRunnable(runnable, Messages.getString("CreateCurrentRequirementHandler.0")); //$NON-NLS-1$
            try
            {
                runnable.run(new NullProgressMonitor());
            }
            catch (Exception e)
            {
                RequirementCorePlugin.log(e);
            }
        }
        super.drop(event);
    }

    private void doDrop()
    {
        EObject eobject = getEObject();
        if (eobject != null)
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
                dropCmd = new CreateCurrentReqCommand(Messages.getString("CreateCurrentRequirementHandler.0")); //$NON-NLS-1$
                ((CreateRequirementCommand) dropCmd).setRequirements(source);
                ((CreateRequirementCommand) dropCmd).setTarget(eobject);
            }

            // execution of the command
            if (dropCmd != null && dropCmd.canExecute())
            {
                IEditorPart editor = RequirementUtils.getCurrentEditor();
                IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
                if (services != null)
                {
                    EditingDomain domain = services.getEditingDomain(editor);
                    if (domain instanceof TransactionalEditingDomain)
                    {
                        // do not use command stack, since it is called in parent transaction
                        dropCmd.execute();
                    }
                    else
                    {
                        domain.getCommandStack().execute(dropCmd);
                    }
                }
            }
        }
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

        if (CurrentRequirementView.getInstance() == null && UpstreamRequirementView.getInstance() == null)
        {
            // drag and drop must come from at least one of upstream or current requirement view
            event.operations = DND.DROP_NONE;
            event.detail = DND.DROP_NONE;
        }
        else
        {
            EObject eobject = getEObject();
            if (eobject != null)
            {
                IEditorInput input = RequirementUtils.getCurrentEditor().getEditorInput();
                if (input instanceof FileEditorInput)
                {
                    String fileExtension = ((FileEditorInput) input).getFile().getFileExtension();
                    // URI graphicalModelName = URI.createURI(RequirementUtils.getCurrentEditor().getPartName());
                    // String fileExtension = graphicalModelName.fileExtension();

                    // if the target is restricted by the extension point dropRestriction
                    if (!(DropRestrictionManager.getInstance().isDropAllowed(fileExtension, eobject)))
                    {
                        event.operations = DND.DROP_NONE;
                        event.detail = DND.DROP_NONE;
                    }
                }
            }
            else
            {
                event.operations = DND.DROP_NONE;
                event.detail = DND.DROP_NONE;
            }
            for (Object s : source)
            {
                if (!(s instanceof Requirement) && !(s instanceof org.topcased.requirement.Requirement))
                {
                    event.operations = DND.DROP_NONE;
                    event.detail = DND.DROP_NONE;
                }
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
            Set<Object> sortedSelection = new LinkedHashSet<Object>();
            for (Object obj : selection)
            {
                // this selection comes from the upstream view. It means that all requirements need to be extracted
                if (obj instanceof Document || obj instanceof Section)
                {
                    Collection<Requirement> allRequirements = RequirementUtils.getUpstreams((EObject) obj);
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
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
        EditPart targetPart = getTargetEditPart();
        if (services != null && targetPart != null)
        {
            return services.getEObject(targetPart);
        }
        return null;
    }

    /**
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#getTargetEditPart()
     */
    @Override
    protected EditPart getTargetEditPart()
    {

        EditPart currentPartTmp = super.getTargetEditPart();
        if (currentPartTmp != null)
        {
            IEditorPart editor = RequirementUtils.getCurrentEditor();
            IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
            if (services != null && services.getEObject(currentPartTmp) != null && services.canCreateRequirement(currentPartTmp))
            {
                currentPart = currentPartTmp;
            }
            else
            {
                currentPart = null;
            }
        }
        return currentPart;
    }

}
