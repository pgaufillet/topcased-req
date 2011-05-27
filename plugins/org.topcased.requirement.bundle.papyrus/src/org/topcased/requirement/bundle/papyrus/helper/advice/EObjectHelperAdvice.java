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
package org.topcased.requirement.bundle.papyrus.helper.advice;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.jface.window.Window;
import org.eclipse.papyrus.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.core.utils.EditorUtils;
import org.eclipse.papyrus.diagram.common.command.wrappers.EMFtoGMFCommandWrapper;
import org.eclipse.swt.widgets.Display;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.bundle.papyrus.Activator;
import org.topcased.requirement.bundle.papyrus.internal.Messages;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.commands.CommandStub;
import org.topcased.requirement.core.commands.MoveHierarchicalElementCommand;
import org.topcased.requirement.core.commands.PasteHierarchicalElementCommand;
import org.topcased.requirement.core.commands.RemoveRequirementCommand;
import org.topcased.requirement.core.commands.RenameRequirementCommand;
import org.topcased.requirement.core.preferences.RequirementPreferenceConstants;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This class adds necessary requirement commands when an EObject is modified.
 * 
 * TODO : put up to date to have the same behaviors as those implemented for Topcased with resolvers in
 * org.topcased.requirement.bundle.topcased.resolvers package : <li>
 * <ul>
 * copy (CopyCommandResolver)
 * </ul>
 * <ul>
 * cut (CutCommandResolver)
 * </ul>
 * <ul>
 * paste (PasteCommandResolver)
 * </ul>
 * <ul>
 * </ul></li>
 * 
 * @author vhemery
 */
public class EObjectHelperAdvice extends AbstractEditHelperAdvice
{
    /**
     * Add a command to handle associated requirements of an element to delete (move them to trash chapter).
     * 
     * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getBeforeDestroyElementCommand(org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest)
     * @param request the request
     * @return the command to execute before the edit helper work is done
     */
    protected ICommand getBeforeDestroyElementCommand(DestroyElementRequest request)
    {
        IMultiDiagramEditor editor = EditorUtils.getMultiDiagramEditor();
        // check parameter to avoid executing twice
        if (editor != null && request.getParameter(DestroyElementRequest.INITIAL_ELEMENT_TO_DESTROY_PARAMETER) != null)
        {
            Object editingdomain = editor.getAdapter(EditingDomain.class);
            if (editingdomain instanceof TransactionalEditingDomain)
            {
                // ask user to confirm deletion
                final TransactionalEditingDomain domain = (TransactionalEditingDomain) editingdomain;
                final EObject eobject = request.getElementToDestroy();
                AbstractTransactionalCommand deleteCmdWithCancel = new AbstractTransactionalCommand(domain, Messages.getString("DeleteModelObjectAction.CmdLabel"), null)
                {

                    @Override
                    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable adapt) throws ExecutionException
                    {
                        boolean canContinue = true;
                        HierarchicalElement hierarchicalElement = RequirementUtils.getHierarchicalElementFor(eobject);
                        if (hierarchicalElement != null && !hierarchicalElement.eContents().isEmpty())
                        {
                            ConfirmationDialog dialog = new ConfirmationDialog(Display.getCurrent().getActiveShell(), Messages.getString("DeleteModelObjectAction.CmdLabel"),
                                    Messages.getString("DeleteModelObjectAction.ConfirmMessage"), RequirementCorePlugin.getDefault().getPreferenceStore(),
                                    RequirementPreferenceConstants.DELETE_MODEL_WITHOUT_CONFIRM);
                            int result = dialog.open();
                            canContinue = result == Window.OK;
                        }
                        if (canContinue)
                        {
                            RemoveRequirementCommand removeReqCmd = new RemoveRequirementCommand(domain, eobject);
                            removeReqCmd.execute();
                            return new CommandResult(new Status(IStatus.OK, Activator.PLUGIN_ID, Messages.getString("DeleteModelObjectAction.CmdLabel")));
                        }
                        else
                        {
                            return new CommandResult(new Status(IStatus.CANCEL, Activator.PLUGIN_ID, Messages.getString("DeleteModelObjectAction.CmdLabel")));
                        }
                    }
                };
                return deleteCmdWithCancel;
            }
            else if (editingdomain instanceof EditingDomain)
            {
                // Should not happen, but better take precautions.
                // Do not ask user to confirm deletion, as we do not know how to cancel
                final EditingDomain domain = (EditingDomain) editingdomain;
                final EObject eobject = request.getElementToDestroy();
                AbstractCommand deleteCmd = new CommandStub()
                {
                    @Override
                    public void execute()
                    {
                        RemoveRequirementCommand removeReqCmd = new RemoveRequirementCommand(domain, eobject);
                        removeReqCmd.execute();
                    }

                    public void redo()
                    {
                        // no redo, let the transaction handle it.
                    }
                    // no undo, let the transaction handle it.
                };
                return new EMFtoGMFCommandWrapper(deleteCmd);
            }
        }
        return null;
    }

    /**
     * Add a command to handle associated requirements of a duplicated element.
     * 
     * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getAfterDuplicateCommand(org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest)
     * @param request the request
     * @return the command to execute after the edit helper work is done
     */
    protected ICommand getAfterDuplicateCommand(final DuplicateElementsRequest request)
    {
        CommandStub copyCommand = new CommandStub()
        {
            @Override
            public void execute()
            {
                Map< ? , ? > map = request.getAllDuplicatedElementsMap();
                for (Entry< ? , ? > entry : map.entrySet())
                {
                    HierarchicalElement elt = RequirementUtils.getHierarchicalElementFor(entry.getKey());
                    Object copied = entry.getValue();
                    if (elt != null && copied instanceof EObject && entry.getKey() instanceof EObject)
                    {
                        EObject objectOriginal = (EObject) entry.getKey();
                        EObject objectCopy = (EObject) copied;
                        EObject hierEltCopy = EcoreUtil.copy(elt);
                        hierEltCopy.eSet(RequirementPackage.eINSTANCE.getHierarchicalElement_Element(), objectCopy);
                        PasteHierarchicalElementCommand cmd = new PasteHierarchicalElementCommand(AdapterFactoryEditingDomain.getEditingDomainFor(objectOriginal), objectCopy.eContainer(),
                                Collections.singletonList(hierEltCopy));
                        cmd.execute();
                    }
                }
            }

            public void redo()
            {
                // no redo, let the transaction handle it.
            }
            // no undo, let the transaction handle it.
        };
        return new EMFtoGMFCommandWrapper(copyCommand);
    }

    /**
     * Add a command to handle associated requirements of a moved element.
     * 
     * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getAfterMoveCommand(org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest)
     * @param request the request
     * @return the command to execute after the edit helper work is done
     */
    protected ICommand getAfterMoveCommand(MoveRequest request)
    {
        final Set< ? > movedElements = request.getElementsToMove().keySet();
        final EObject owner = request.getTargetContainer();
        if (!movedElements.isEmpty() && owner != null)
        {
            /*
             * The MoveHierarchicalElementCommand command must be enclosed in another command, since prepare already
             * performs model modifications. Hence, the transactional approach could not work.
             */
            CommandStub moveCmd = new CommandStub()
            {
                @Override
                public void execute()
                {
                    MoveHierarchicalElementCommand cmd = new MoveHierarchicalElementCommand(owner, movedElements);
                    cmd.execute();
                }

                public void redo()
                {
                    // no redo, let the transaction handle it.
                }
                // no undo, let the transaction handle it.
            };
            return new EMFtoGMFCommandWrapper(moveCmd);
        }
        return super.getAfterMoveCommand(request);
    }

    /**
     * Add a command to handle renaming requirements of a renamed element.
     * 
     * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getAfterSetCommand(org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest)
     * @param request the request
     * @return the command to execute after the edit helper work is done
     */
    protected ICommand getAfterSetCommand(SetRequest request)
    {
        EStructuralFeature modifiedFeature = request.getFeature();
        if (isNameFeature(modifiedFeature))
        {
            EObject namedElt = request.getElementToEdit();
            Object oldName = namedElt.eGet(modifiedFeature);
            if (oldName instanceof String && request.getValue() instanceof String)
            {
                String oldNameValue = (String) oldName;
                String newNameValue = (String) request.getValue();
                if (!oldNameValue.equals(newNameValue))
                {
                    RenameRequirementCommand cmd = new RenameRequirementCommand(namedElt, oldNameValue, newNameValue);
                    return new EMFtoGMFCommandWrapper(cmd);
                }
            }
        }
        return super.getAfterSetCommand(request);
    }

    /**
     * Add a command to handle renaming requirements of a renamed element.
     * 
     * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getAfterEditCommand(org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest)
     * @param request the request
     * @return the command to execute after the edit helper work is done
     */
    public ICommand getAfterEditCommand(IEditCommandRequest request)
    {
        if (request instanceof SetRequest)
        {
            SetRequest renameReq = (SetRequest) request;
            EStructuralFeature modifiedFeature = renameReq.getFeature();
            if (isNameFeature(modifiedFeature))
            {
                EObject namedElt = renameReq.getElementToEdit();
                Object oldName = namedElt.eGet(modifiedFeature);
                if (oldName instanceof String && renameReq.getValue() instanceof String)
                {
                    String oldNameValue = (String) oldName;
                    String newNameValue = (String) renameReq.getValue();
                    if (!oldNameValue.equals(newNameValue))
                    {
                        RenameRequirementCommand cmd = new RenameRequirementCommand(namedElt, oldNameValue, newNameValue);
                        return new EMFtoGMFCommandWrapper(cmd);
                    }
                }

            }
        }
        return super.getAfterEditCommand(request);
    }

    /**
     * Test if the passed feature is the element's name
     * 
     * @param feature feature to test
     * @return true if the feature is the name or main label
     */
    private boolean isNameFeature(EStructuralFeature feature)
    {
        if (feature instanceof EAttribute)
        {
            EClassifier dataType = feature.getEType();
            if (String.class.getCanonicalName().equals(dataType.getInstanceClassName()))
            {
                if (feature.getName().equals(EcorePackage.eINSTANCE.getENamedElement_Name().getName()))
                {
                    return true;
                }
                else
                {
                    for (EAttribute att : feature.getEContainingClass().getEAllAttributes())
                    {
                        // check whether this is the first met String attribute
                        if (String.class.getCanonicalName().equals(dataType.getInstanceClassName()))
                        {
                            return att.equals(feature);
                        }
                    }
                }
            }
        }
        return false;
    }
}