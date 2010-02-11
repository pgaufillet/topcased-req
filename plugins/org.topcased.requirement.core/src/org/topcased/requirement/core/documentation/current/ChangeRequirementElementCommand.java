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
package org.topcased.requirement.core.documentation.current;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.TextAttribute;

/**
 * An EMF {@link AbstractCommand} use to update value of : 
 * <ul>
 * <li>the <b>shortDescription</b> feature of a {@link Requirement}</li>
 * <li>the <b>shortDescription</b> feature of a {@link HierarchicalElement}</li>
 * <li><b>value</b> feature of a {@link TextAttribute}.</li>
 * </ul>
 * <br>
 * Updated : 12 August 2009 <br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.2.0
 */
public class ChangeRequirementElementCommand extends AbstractCommand
{
    private EObject element;

    private String newDocumentation;

    private String oldDocumentation;

    /**
     * An EMF {@link AbstractCommand} to change the documentation associated with the selected element
     * 
     * @param element The {@link EModelElement} on which the documentation should be updated
     * @param documentation A {@link String} representing the new documentation text to update
     */
    public ChangeRequirementElementCommand(EObject element, String documentation)
    {
        super("Change documentation of EModelElement"); //$NON-NLS-1$

        this.element = element;
        this.newDocumentation = documentation;
    }

    /**
     * @see org.eclipse.gef.commands.Command#execute()
     */
    public void execute()
    {
        // Record previous comment
        if (element instanceof Requirement)
        {
            oldDocumentation = ((Requirement) element).getShortDescription();
        }
        else if (element instanceof HierarchicalElement)
        {
            oldDocumentation = ((HierarchicalElement) element).getShortDescription();
        }
        else if (element instanceof TextAttribute)
        {
            oldDocumentation = ((TextAttribute) element).getValue();
        }
        redo();
    }

    /**
     * @see org.eclipse.gef.commands.Command#redo()
     */
    public void redo()
    {
        if (element instanceof Requirement)
        {
            ((Requirement) element).setShortDescription(newDocumentation);
        }
        else if (element instanceof HierarchicalElement)
        {
            ((HierarchicalElement) element).setShortDescription(newDocumentation);
        }
        else if (element instanceof TextAttribute)
        {
            ((TextAttribute) element).setValue(newDocumentation);
        }
    }

    /**
     * @see org.eclipse.gef.commands.Command#undo()
     */
    @Override
    public void undo()
    {
        if (element instanceof Requirement)
        {
            ((Requirement) element).setShortDescription(oldDocumentation);
        }
        else if (element instanceof TextAttribute)
        {
            ((TextAttribute) element).setValue(oldDocumentation);
        }
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
     */
    @Override
    public boolean canExecute()
    {
        return true;
    }
}
