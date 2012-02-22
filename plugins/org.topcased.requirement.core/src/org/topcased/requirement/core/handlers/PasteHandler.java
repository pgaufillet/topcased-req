/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 *  Anass RADOUANI (Atos) <anass.radouani@atos.net> - Adding RequirementPasteFromClipboardCommand to rename the requirement before pasting it
 *****************************************************************************/
package org.topcased.requirement.core.handlers;

import java.util.List;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.jface.viewers.StructuredSelection;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.views.AddRequirementMarker;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * This class defines the EMF <b>paste</b> command
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class PasteHandler extends RequirementAbstractEMFCommandHandler
{

	/**
     * @see org.topcased.requirement.core.handlers.RequirementAbstractEMFCommandHandler#run()
     */
    protected void run(final IProgressMonitor monitor)
    {
        monitor.beginTask(Messages.getString("RequirementAbstractEMFCommandHandler.0"), 2); //$NON-NLS-1$
        CurrentPage currentPage = RequirementHelper.INSTANCE.getCurrentPage();
        monitor.worked(1);

        if (((EvaluationContext) evt.getApplicationContext()).getDefaultVariable() instanceof List< ? >)
        {
            Object owner = ((List< ? >) ((EvaluationContext) evt.getApplicationContext()).getDefaultVariable()).get(0);
            Integer pos = AddRequirementMarker.eINSTANCE.computeIndex(owner);
            Command cmd = new RequirementPasteFromClipboardCommand(editingDomain, owner, RequirementPackage.eINSTANCE.getHierarchicalElement_Requirement(), pos);
            compoundCmd.appendIfCanExecute(cmd);

            if (!compoundCmd.isEmpty() && compoundCmd.canExecute())
            {
                // Execute it.
                editingDomain.getCommandStack().execute(compoundCmd);
                
                if (currentPage != null && !compoundCmd.getAffectedObjects().isEmpty())
                {
                    currentPage.setSelection(new StructuredSelection((List< ? >) compoundCmd.getAffectedObjects()));
                }
                
            }
        }
        monitor.worked(1);
        monitor.done();
    }

    /**
     * @see org.topcased.requirement.core.handlers.RequirementAbstractEMFCommandHandler#getParam()
     */
    @Override
    public CommandParameter getParam()
    {
        return null;
    }

    /**
     * @see org.topcased.requirement.core.handlers.RequirementAbstractEMFCommandHandler#getCommand()
     */
    @Override
    public Class< ? extends Command> getCommand()
    {
        return null;
    }
}
