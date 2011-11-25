/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.resourceloading.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.views.current.CurrentPage;
import org.topcased.requirement.core.views.current.model.CurrentRequirementReference;
import org.topcased.requirement.resourceloading.exception.RequirementResourceException;

/**
 * Abstract handler useull to implement handler of the Current Requirement Page
 * 
 * @author Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * 
 */
public abstract class AbstractCurrentRequirementPageHandler extends AbstractHandler
{

    /**
     * Returns the editing domain to handle {@link CurrentRequirementReference}
     * 
     * @return the current editing domain
     */
    protected EditingDomain getEditingDomain()
    {
        EditingDomain editingDomain = null;
        List<CurrentRequirementReference> ref = getSelectedCurrentRequirements();
        if (!ref.isEmpty())
        {
            editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(ref.get(0).getAdapter(EObject.class));
        }
        return editingDomain;

    }

    /**
     * Returns the list of selected {@link CurrentRequirementReference}
     * 
     * @return the list of selected diagrams
     */
    protected List<CurrentRequirementReference> getSelectedCurrentRequirements()
    {
        List<CurrentRequirementReference> references = new ArrayList<CurrentRequirementReference>();
        ISelection selection = null;

        // Get current selection
        selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

        // Get first element if the selection is an IStructuredSelection
        if (selection instanceof IStructuredSelection)
        {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            Iterator< ? > iter = structuredSelection.iterator();
            while (iter.hasNext())
            {
                Object current = iter.next();
                CurrentRequirementReference toAdd = null;
                if (current instanceof IAdaptable)
                {
                    IAdaptable adap = (IAdaptable) current;
                    toAdd = (CurrentRequirementReference) adap.getAdapter(CurrentRequirementReference.class);
                }
                else if (current instanceof CurrentRequirementReference)
                {
                    toAdd = (CurrentRequirementReference) current;
                }
                if (toAdd != null)
                {
                    references.add(toAdd);
                }
            }
        }

        return references;
    }

    /**
     * 
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     * 
     * @param event
     * @return Object
     * @throws ExecutionException
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        EditingDomain editingDomain = getEditingDomain();
        Command cmd = null;
        if (editingDomain instanceof TransactionalEditingDomain)
        {
            cmd = getTransactionnalCommand((TransactionalEditingDomain) editingDomain);
        }
        else
        {
            cmd = getCommand();
        }
        if (editingDomain != null)
        {
            CommandStack commandStack = editingDomain.getCommandStack();
            if (commandStack != null)
            {
                commandStack.execute(cmd);
            }
            else
            {
                throw new RequirementResourceException(getCommandStackException());
            }
        }
        else
        {
            throw new RequirementResourceException(getEditingDomainExceptionLabel());
        }
        //Refreshing the view.
        CurrentPage currentPage = RequirementHelper.INSTANCE.getCurrentPage();
        currentPage.getViewer().refresh();
        return null;
    }

    protected abstract String getEditingDomainExceptionLabel();

    protected abstract String getCommandStackException();

    /**
     * <pre>
     * 
     * Returns the command to execute (to be implemented 
     * in children implementing this class)
     * 
     * @return the command to execute
     * 
     * </pre>
     */
    protected abstract Command getCommand();

    /**
     * <pre>
     * 
     * Returns the command to execute (to be implemented 
     * in children implementing this class)
     * @param editingDomain 
     * 
     * @return the command to execute
     * 
     * </pre>
     */
    protected abstract Command getTransactionnalCommand(TransactionalEditingDomain editingDomain);

}
