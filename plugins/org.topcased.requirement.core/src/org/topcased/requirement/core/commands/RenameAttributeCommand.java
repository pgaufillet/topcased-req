/***********************************************************************************************************************
 * Copyright (c) 2008 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.commands;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This class defined the <b>Rename</b> operation for an {@link Attribute} that will be contained in a
 * {@link Requirement}.<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class RenameAttributeCommand extends CompoundCommand
{
    /** The new Attribute name */
    private String newName;

    /** The old Attribute name */
    private String oldName;

    /** The editing domain to use */
    private EditingDomain editingDomain;

    /**
     * Constructor
     * 
     * @param domain The editing domain to use
     * @param old The old attribute name
     * @param current The new attribute name
     */
    public RenameAttributeCommand(EditingDomain domain, String old, String current)
    {
        super(Messages.getString("RenameAttributeCommand.0")); //$NON-NLS-1$
        editingDomain = domain;
        oldName = old;
        newName = current;
        initializeCommands();
    }

    /**
     * Initializes the commands contained in this compound command
     */
    protected void initializeCommands()
    {
        // process all current requirements
        Resource requirementModel = RequirementUtils.getRequirementModel(editingDomain);
        for (Requirement requirement : RequirementUtils.getAllCurrents(requirementModel))
        {
            for (Attribute attribute : requirement.getAttribute())
            {
                // when the concerned attribute is found, the command is put to the stack
                if (attribute.getName().equals(oldName))
                {
                    appendIfCanExecute(SetCommand.create(editingDomain, attribute, RequirementPackage.eINSTANCE.getAttribute_Name(), newName));
                }
            }
        }  
    }
    
    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
     */
    @Override
    public boolean canExecute()
    {
        return super.canExecute() && !"".equals(newName) && !"".equals(oldName) && editingDomain != null;
    }
}
