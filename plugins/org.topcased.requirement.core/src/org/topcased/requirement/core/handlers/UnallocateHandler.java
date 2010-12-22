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
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeAllocate;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * The "Unallocate" handler for a hierarchical element or a current requirement
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class UnallocateHandler extends AbstractHandler
{
    /** A single EMF compound command */
    private CompoundCommand compoundCmd;

    private EditingDomain editingDomain;

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);

        if (services != null && ((EvaluationContext) event.getApplicationContext()).getDefaultVariable() instanceof List< ? >)
        {
            editingDomain = services.getEditingDomain(editor);
            // Get the current selection
            List< ? > elements = ((List< ? >) ((EvaluationContext) event.getApplicationContext()).getDefaultVariable());
            compoundCmd = new CompoundCommand(Messages.getString("UnallocateHandler.0")); //$NON-NLS-1$
            for (Object currObject : elements)
            {
                if (currObject instanceof HierarchicalElement)
                {
                    unallocate((HierarchicalElement) currObject);
                }
                else if (currObject instanceof Requirement)
                {
                    unallocate((Requirement) currObject);
                }
            }
            if (!compoundCmd.isEmpty() && compoundCmd.canExecute())
            {
                editingDomain.getCommandStack().execute(compoundCmd);
            }
        }
        return null;
    }

    /**
     * Unallocates a current requirement.
     * 
     * @param currReq A {@link CurrentRequirement}.
     */
    private void unallocate(Requirement currReq)
    {
        for (Attribute attribute : currReq.getAttribute())
        {
            if (attribute instanceof AttributeAllocate)
            {
                Command set = new SetCommand(editingDomain, attribute, RequirementPackage.eINSTANCE.getObjectAttribute_Value(), null);
                compoundCmd.appendIfCanExecute(set);
            }
        }
    }

    /**
     * Unallocates all requirements found inside a given Hierarchical Element.
     * 
     * @param element A {@link HierarchicalElement}.
     */
    private void unallocate(HierarchicalElement element)
    {
        for (Requirement currentReq : element.getRequirement())
        {
            unallocate(currentReq);
        }
        for (HierarchicalElement children : element.getChildren())
        {
            unallocate(children);
        }
    }
}
