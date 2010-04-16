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
package org.topcased.requirement.core.listeners;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.topcased.facilities.dialogs.ChooseDialog;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.providers.AdvancedRequirementLabelProvider;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Handles double-click on tree viewer present in the current and upstream viewer pages.<br>
 * According to the kind of Attribute, a dialog box is opened and allows the user to make a choice.<br>
 * 
 * Update : 26 February 2009<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class RequirementDoubleClickListener implements IDoubleClickListener
{
    /**
     * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
     */
    public void doubleClick(DoubleClickEvent event)
    {
        if (!event.getSelection().isEmpty() && event.getSelection() instanceof IStructuredSelection)
        {
            Object firstElement = ((IStructuredSelection) event.getSelection()).getFirstElement();
            if (firstElement instanceof ObjectAttribute)
            {
                handleObjectAttribute(event, firstElement);
            }
            else
            {
                handleDefaultNode(event, firstElement);
            }
        }
    }

    /**
     * Handles a double-click on a model object of kind {@link ObjectAttribute}.
     * 
     * @param event The event containing the selection and the viewer.
     * @param firstElement The first element contained in the selection.
     */
    private void handleObjectAttribute(DoubleClickEvent event, Object firstElement)
    {
        Collection<Object> content = RequirementUtils.getChooseDialogContent(firstElement);
        ChooseDialog chooseDialog = new ChooseDialog(Display.getCurrent().getActiveShell(), content.toArray());
        chooseDialog.setLabelProvider(new AdapterFactoryLabelProvider(RequirementUtils.getAdapterFactory()));
        chooseDialog.setAdvancedLabelProvider(new AdvancedRequirementLabelProvider(RequirementUtils.getAdapterFactory()));
        if (chooseDialog.open() == Dialog.OK)
        {
            Object theResult = chooseDialog.getResult()[0];
            EditingDomain domain = TopcasedAdapterFactoryEditingDomain.getEditingDomainFor((ObjectAttribute) firstElement);
            Command cmd = SetCommand.create(domain, firstElement, RequirementPackage.eINSTANCE.getObjectAttribute_Value(), "".equals(theResult) ? null : theResult);
            if (cmd.canExecute())
            {
                domain.getCommandStack().execute(cmd);
            }
        }
    }

    /**
     * Expands/Collapses nodes in the tree viewer.
     * 
     * @param event The event containing the selection and the viewer.
     * @param firstElement The first element contained in the selection.
     */
    private void handleDefaultNode(DoubleClickEvent event, Object firstElement)
    {
        TreeViewer viewer = (TreeViewer) event.getViewer();
        if (viewer.getExpandedState(firstElement))
        {
            viewer.collapseToLevel(firstElement, AbstractTreeViewer.ALL_LEVELS);
        }
        else
        {
            viewer.expandToLevel(firstElement, 1);
        }
    }
}
