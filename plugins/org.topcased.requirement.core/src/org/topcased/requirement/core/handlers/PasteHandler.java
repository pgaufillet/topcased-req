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

import java.util.Collection;
import java.util.List;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * This class defines the EMF <b>copy</b> command
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public class PasteHandler extends RequirementAbstractEMFCommandHandler
{

    /**
     * @see org.topcased.requirement.core.handlers.RequirementAbstractEMFCommandHandler#getCommand()
     */
    @Override
    public Class< ? extends Command> getCommand()
    {
        return PasteFromClipboardCommand.class;
    }

    /**
     * @see org.topcased.requirement.core.handlers.RequirementAbstractEMFCommandHandler#getParam()
     */
    @Override
    public CommandParameter getParam()
    {
        if (((EvaluationContext)evt.getApplicationContext()).getDefaultVariable() instanceof List<?>)
        {
            return new CommandParameter(((List<?>)((EvaluationContext)evt.getApplicationContext()).getDefaultVariable()).get(0), null,((List<?>)((EvaluationContext)evt.getApplicationContext()).getDefaultVariable()).size());
        }
        else
        {
            return null;
        }
        
    }
    
    /**
     * FIXME : selection bug!!!
     * 
     * @see org.topcased.requirement.core.actions.RequirementAbstractEMFAction#endAction(org.eclipse.emf.common.command.CompoundCommand)
     */
    @Override
    public void endAction(CompoundCommand cmd)
    {   
        CurrentPage currentPage = RequirementHelper.INSTANCE.getCurrentPage();
        Collection< ? > source = cmd.getAffectedObjects();
        CompoundCommand compound = new CompoundCommand("Renaming moving requirements");

        // Process the renaming of the newly pasted requirements
        for (Object currSource : source)
        {
            if (currSource instanceof CurrentRequirement)
            {
                // rename the current requirement
                CurrentRequirement requirement = (CurrentRequirement) currSource;
                compound.appendIfCanExecute(RequirementHelper.INSTANCE.renameRequirement(requirement));
            }
        }

        if (!compound.isEmpty() && compound.canExecute())
        {
            // Execute the renaming commands
            editingDomain.getCommandStack().execute(compound);
        }

        if (currentPage != null && compound.getAffectedObjects() != null)
        {
            currentPage.refreshViewer(true);
//            currentPage.getViewer().setSelection(new StructuredSelection((List<?>) compound.getAffectedObjects()), true);

        }
    }

}
