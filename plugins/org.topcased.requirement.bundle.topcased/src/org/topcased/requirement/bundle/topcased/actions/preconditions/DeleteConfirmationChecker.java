/*******************************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Vincent Hemery (Atos Origin) - Initial API and implementation
 *******************************************************************************/
package org.topcased.requirement.bundle.topcased.actions.preconditions;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.topcased.modeler.ActionConditionChecker;
import org.topcased.modeler.di.model.GraphElement;
import org.topcased.modeler.dialogs.ConfirmationDialog;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.bundle.topcased.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.preferences.RequirementPreferenceConstants;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Check, in case the deleted object has requirements, that the user has confirmed the deletion, either through a popup
 * or through the corresponding preference.
 * 
 * @author vhemery
 */
public class DeleteConfirmationChecker implements ActionConditionChecker
{
    /**
     * Check that the user confirms the deletion through a popup or preference (if there are requirements).
     * 
     * @param actionToCheck the action which must be checked.
     * @param modeler the modeler part
     * @param selection the selection on which the action is performed
     * @return true if popup is acknowledged or must not be displayed
     */
    public boolean checkCondition(Action actionToCheck, Modeler modeler, IStructuredSelection selection)
    {
        boolean hasRequirements = hasAttachedRequirements(selection);
        if (hasRequirements)
        {
            ConfirmationDialog dialog = new ConfirmationDialog(Display.getCurrent().getActiveShell(), Messages.getString("DeleteModelObjectAction.CmdLabel"),
                    Messages.getString("DeleteModelObjectAction.ConfirmMessage"), RequirementCorePlugin.getDefault().getPreferenceStore(), RequirementPreferenceConstants.DELETE_MODEL_WITHOUT_CONFIRM);
            int result = dialog.open();
            return result == Window.OK;
        }
        return true;
    }

    /**
     * Check whether the selection has attached requirements
     * 
     * @param selection a selection of edit parts or eobjects
     * @return true if an eobject has requirements
     */
    private boolean hasAttachedRequirements(IStructuredSelection selection)
    {
        Iterator< ? > iterator = selection.iterator();
        while (iterator.hasNext())
        {
            // get next eobject
            Object next = iterator.next();
            EObject nextEObject = null;
            if (next instanceof EObject)
            {
                nextEObject = (EObject) next;
            }
            else if (next instanceof EditPart)
            {
                GraphElement childNode = (GraphElement) ((EditPart) next).getModel();
                nextEObject = Utils.getElement(childNode);
            }
            // check if eobject has requirements
            if (nextEObject != null)
            {
                HierarchicalElement hierarchical = RequirementUtils.getHierarchicalElementFor(nextEObject);
                if (hierarchical != null && !hierarchical.eContents().isEmpty())
                {
                    return true;
                }
            }
        }
        return false;
    }
}
