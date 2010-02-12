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
package org.topcased.requirement.core.commands;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;

/**
 * Abstract command creating a coverage link between a requirement and a graphical element represented on the modeler.<br>
 * 
 * Update : 13 March 2009<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public abstract class CreateRequirementCommand extends AbstractCommand
{
    /**
     * List of dropped requirements
     */
    private Collection< ? > droppedObjects = Collections.emptyList();

    /**
     * Represents the targeted object
     */
    private EObject target;

    /**
     * Represents the encapsulated compound command presented as a single command
     */
    protected Command globalCmd;

    /**
     * Constructor
     * 
     * @param title The title of the command
     */
    public CreateRequirementCommand(String title)
    {
        super(title);
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
     */
    @Override
    public boolean canExecute()
    {
        return target != null;
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#undo()
     */
    @Override
    public void undo()
    {
        if (globalCmd != null && globalCmd.canUndo())
        {
            globalCmd.undo();
        }
    }

    /**
     * @see org.eclipse.emf.common.command.Command#redo()
     */
    public void redo()
    {
        if (globalCmd != null && globalCmd.canExecute())
        {
            globalCmd.redo();
        }
    }

    /**
     * Sets the list of dropped requirements
     * 
     * @param doppedReqs The dropped requirements
     */
    public void setRequirements(Collection< ? > doppedReqs)
    {
        droppedObjects = doppedReqs;
    }

    /**
     * Gets the list of dropped requirements
     * 
     * @param doppedReqs The dropped requirements
     */
    public Collection< ? > getRequirements()
    {
        return droppedObjects;
    }

    /**
     * Sets the model object target.
     * 
     * @param target The target model object
     */
    public void setTarget(EObject target)
    {
        this.target = target;
    }

    /**
     * Gets the model object target.
     */
    public EObject getTarget()
    {
        return target;
    }

}
