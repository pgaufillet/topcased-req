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
package org.topcased.requirement.sam.commands;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.ecore.EObject;
import org.topcased.modeler.sam.util.GlobalFlowChangesManager;
import org.topcased.modeler.sam.util.TransformationInfo;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Command changing the {@link HierarchicalElement} if necessary<br>
 * 
 * Creation : 11th june 2009.<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 3.0.0
 * @see {@link GlobalFlowChangesWithRequirementCommand}
 * @see {@link GlobalFlowChangesManager}
 */
public class ChangeHierarchicalElementCommand extends AbstractCommand
{
    /**
     * Model object after the transformation
     */
    private TransformationInfo transformation;

    /**
     * The {@link HierarchicalElement} to change
     */
    private HierarchicalElement hierarchicalElt;

    /**
     * Constructor
     * 
     * @param info The information related to the transformation to perform.
     */
    public ChangeHierarchicalElementCommand(TransformationInfo info)
    {
        transformation = info;

    }

    /**
     * @see org.eclipse.emf.common.command.Command#redo()
     */
    public void redo()
    {
        EObject before = transformation.getBefore();
        EObject after = transformation.getAfter();
        if (hierarchicalElt == null)
        {
            hierarchicalElt = RequirementUtils.getHierarchicalElementFor(before);
        }

        if (hierarchicalElt != null)
        {
            hierarchicalElt.setElement(after);
        }
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#undo()
     */
    @Override
    public void undo()
    {
        EObject before = transformation.getBefore();
        EObject after = transformation.getAfter();
        if (hierarchicalElt != null)
        {
            hierarchicalElt.setElement(before);
        }
        if (hierarchicalElt == null)
        {
            if (after != null)
            {
                hierarchicalElt = RequirementUtils.getHierarchicalElementFor(after);
            }
        }
    }

    /**
     * @see org.eclipse.emf.common.command.Command#execute()
     */
    public void execute()
    {
        redo();
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
     */
    @Override
    public boolean canExecute()
    {
        return transformation != null;
    }

}
