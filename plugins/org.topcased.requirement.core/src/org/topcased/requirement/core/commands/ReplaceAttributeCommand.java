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

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.core.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Define the <b>Replace</b> operation for {@link Attribute} contained in {@link Requirement}.<br>
 * This compound command is in charge of replacing the default attribute value for a given {@link Attribute}. Of course,
 * this compound command may apply only on {@link TextAttribute} and if a default attribute value is defined at the
 * local configuration level.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class ReplaceAttributeCommand extends CompoundCommand
{
    /** The local configured attribute on which the command will be built */
    private ConfiguratedAttribute attribute;

    /** The editing domain to use */
    private EditingDomain editingDomain;

    /**
     * Constructor
     * 
     * @param domain The editing domain to use.
     * @param confAtt The <b>local</b> configured attribute.
     */
    public ReplaceAttributeCommand(EditingDomain domain, ConfiguratedAttribute confAtt)
    {
        super(Messages.getString("ReplaceAttributeCommand.0")); //$NON-NLS-1$
        editingDomain = domain;
        attribute = confAtt;
        initializeCommands();
    }

    /**
     * Initializes the commands contained in this compound command
     */
    protected void initializeCommands()
    {
        // all the model need to be updated
        Resource requirementModel = RequirementUtils.getRequirementModel(editingDomain);
        for (Requirement requirement : RequirementUtils.getAllCurrents(requirementModel))
        {
            for (Attribute oldAttribute : requirement.getAttribute())
            {
                if (oldAttribute.getName().equals(attribute.getName()))
                {
                    String toSet = attribute.getDefaultValue() != null ? attribute.getDefaultValue().getValue().getValue() : ""; //$NON-NLS-1$
                    appendIfCanExecute(SetCommand.create(editingDomain, oldAttribute, RequirementPackage.eINSTANCE.getTextAttribute_Value(), toSet));
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
