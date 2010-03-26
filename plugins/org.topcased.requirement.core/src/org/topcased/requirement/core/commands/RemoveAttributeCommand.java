/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
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
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This class defined the <b>Remove</b> operation for an {@link Attribute} that is normally contained in a {@link Requirement}.<br>
 * So, if the attribute exists in the {@link Requirement}, the attribute is removed.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class RemoveAttributeCommand extends CompoundCommand
{
    /** The local configured attribute on which the command will be built */
    private ConfiguratedAttribute attribute;

    /** The editing domain to use */
    private EditingDomain editingDomain;

    /**
     * Constructor
     * 
     * @param domain The editing domain to use
     * @param confAtt The <b>local</b> configured attribute
     */
    public RemoveAttributeCommand(EditingDomain domain, ConfiguratedAttribute confAtt)
    {
        super(Messages.getString("RemoveAttributeCommand.0")); //$NON-NLS-1$
        editingDomain = domain;
        attribute = confAtt;
        initializeCommands();
    }

    /**
     * Initializes the commands contained in this compound command
     */
    protected void initializeCommands()
    {
        // remove this attribute to each current requirement.
        Resource requirementModel = RequirementUtils.getRequirementModel(editingDomain);
        for (Requirement requirement : RequirementUtils.getAllCurrents(requirementModel))
        {
            for (Attribute oldAttribute : requirement.getAttribute())
            {
                if (oldAttribute.getName().equals(attribute.getName()))
                {
                    appendIfCanExecute(RemoveCommand.create(editingDomain, requirement, RequirementPackage.eINSTANCE.getRequirement_Attribute(), oldAttribute));
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
        return super.canExecute() && attribute != null && editingDomain != null;
    }
}
