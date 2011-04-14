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
package org.topcased.requirement.bundle.ecore.helper.advice;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecoretools.diagram.part.EcoreDiagramEditor;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.bundle.ecore.internal.Messages;
import org.topcased.requirement.bundle.ecore.utils.ConfirmationDialog;
import org.topcased.requirement.core.commands.CommandStub;
import org.topcased.requirement.core.commands.MoveHierarchicalElementCommand;
import org.topcased.requirement.core.commands.PasteHierarchicalElementCommand;
import org.topcased.requirement.core.commands.RemoveRequirementCommand;
import org.topcased.requirement.core.commands.RenameRequirementCommand;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.preferences.RequirementPreferenceConstants;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * The Class EModelElementHelperAdvice.
 */
public class EModelElementHelperAdvice extends AbstractEditHelperAdvice
{

    /**
     * Add a command to handle associated requirements of an element to delete (move them to trash chapter).
     * 
     * @param request the request
     * @return the command to execute before the edit helper work is done
     * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getBeforeDestroyElementCommand(org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest)
     */
    protected ICommand getBeforeDestroyElementCommand(DestroyElementRequest request)
    {
        // FIXME ask user to confirm deletion like in Topcased
        EcoreDiagramEditor editor = getMultiDiagramEditor();
        if (editor != null)
        {
            Object domainProvider = editor.getAdapter(IEditingDomainProvider.class);
            Object editingdomain = null;
            if (domainProvider instanceof IEditingDomainProvider)
            {
                editingdomain = ((IEditingDomainProvider) domainProvider).getEditingDomain();
            }

            if (editingdomain instanceof EditingDomain)
            {
                final EditingDomain domain = (EditingDomain) editingdomain;
                final EObject eobject = request.getElementToDestroy();
                AbstractCommand deleteCmd = new CommandStub()
                {
                    public boolean canExecute = true;

                    @Override
                    public boolean canExecute()
                    {
                        return super.canExecute() && canExecute;
                    }

                    @Override
                    public void execute()
                    {
                        HierarchicalElement hierarchicalElement = RequirementUtils.getHierarchicalElementFor(eobject);
                        if (hierarchicalElement != null && !hierarchicalElement.eContents().isEmpty())
                        {
                            ConfirmationDialog dialog = new ConfirmationDialog(Display.getCurrent().getActiveShell(), Messages.getString("DeleteModelObjectAction.CmdLabel"),
                                    Messages.getString("DeleteModelObjectAction.ConfirmMessage"), RequirementCorePlugin.getDefault().getPreferenceStore(),
                                    RequirementPreferenceConstants.DELETE_MODEL_WITHOUT_CONFIRM);
                            int result = dialog.open();
                            canExecute = result == Window.OK;

                        }
                        if (canExecute)
                        {
                            RemoveRequirementCommand removeReqCmd = new RemoveRequirementCommand(domain, eobject);
                            removeReqCmd.execute();
                        }
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
     * @param request the request
     * @return the command to execute after the edit helper work is done
     * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getAfterDuplicateCommand(org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest)
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
     * @param request the request
     * @return the command to execute after the edit helper work is done
     * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getAfterMoveCommand(org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest)
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
     * @param request the request
     * @return the command to execute after the edit helper work is done
     * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getAfterSetCommand(org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest)
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
     * @param request the request
     * @return the command to execute after the edit helper work is done
     * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getAfterEditCommand(org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest)
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
     * Test if the passed feature is the element's name.
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

    /**
     * Gets the {@link IMultiDiagramEditor} interface of the a Eclipse active editor, if possible, or null if not
     * possible. <br>
     * WARNING - This method doesn't work during the initialization of the main editor. See note in class doc. <br>
     * This method return null if there is no active editor, or if the editor is not instance of IMultiDiagramEditor. <br>
     * This method is designed to be used by ui actions that interact with the active editor. <br>
     * This method should not be used during the editor initialization phase. <br>
     * In any case, a check should be done on the returned value that can be null. Usage of this method is discouraged.
     * Use {@link #getMultiDiagramEditorChecked()} instead.
     * 
     * 
     * @return Get the current {@link IMultiDiagramEditor} or null if not found.
     */
    public static EcoreDiagramEditor getMultiDiagramEditor()
    {
        // Lookup ServiceRegistry
        IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (workbenchWindow == null)
        {
            return null;
        }
        IWorkbenchPage page = workbenchWindow.getActivePage();
        if (page == null)
        {
            return null;
        }
        IEditorPart editor = page.getActiveEditor();
        if (editor instanceof EcoreDiagramEditor)
        {
            return (EcoreDiagramEditor) editor;
        }
        else
        {
            return null;
        }
    }
}
