/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/
package org.topcased.requirement.core.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.IEditorPart;
import org.topcased.facilities.util.EMFMarkerUtil;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * This handler allows to set a Current Requirement as valid after an update operation.<br>
 * The warning is so removed from the Problem View and the decorator presented in the viewer is also deleted.<br>
 * The <b>impacted</b> feature of the Current Requirement(s) is/are switch to <code>false</code><br>
 * The feature <b>status</b> of the upstream requirement(s) is/are also modified.<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class SetAsValidHandler extends AbstractHandler
{
    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
        if (services != null && ((EvaluationContext) event.getApplicationContext()).getDefaultVariable() instanceof List< ? >)
        {
            EditingDomain editingDomain = services.getEditingDomain(editor);
            // Get the current selection
            List< ? > elements = ((List< ? >) ((EvaluationContext) event.getApplicationContext()).getDefaultVariable());
            CompoundCommand compoundCmd = new CompoundCommand(Messages.getString("SetAsValidHandler.0")); //$NON-NLS-1$
            for (Object element : elements)
            {
                try
                {
                    // gets the concerned EObject
                    EObject currentRequirement = (EObject) element;

                    if (currentRequirement instanceof CurrentRequirement)
                    {
                        CurrentRequirement req = (CurrentRequirement) currentRequirement;
                        for (Attribute anAttribute : req.getAttribute())
                        {
                            // Try to remove marker for each attribute.
                            EMFMarkerUtil.removeMarkerFor(anAttribute);
                        }
                    }

                    // then update the feature
                    compoundCmd.appendIfCanExecute(RequirementHelper.INSTANCE.revertImpact(currentRequirement));
                }
                catch (CoreException e)
                {
                    RequirementCorePlugin.log(Messages.getString("SetAsValidHandler.1"), IStatus.ERROR, e); //$NON-NLS-1$
                }
            }
            if (!compoundCmd.isEmpty() && compoundCmd.canExecute())
            {
                editingDomain.getCommandStack().execute(compoundCmd);
            }
            
            CurrentPage page = RequirementHelper.INSTANCE.getCurrentPage();
            page.getViewer().refresh();
        }
        return null;
    }
}
