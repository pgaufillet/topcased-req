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
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.requirement.AnonymousRequirement;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.AttributesType;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This class defined the <b>Add</b> operation for an {@link Attribute} that will be contained in a {@link Requirement}.<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class AddAttributeCommand extends CompoundCommand
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
    public AddAttributeCommand(EditingDomain domain, ConfiguratedAttribute confAtt)
    {
        super(Messages.getString("AddAttributeCommand.0")); //$NON-NLS-1$
        editingDomain = domain;
        attribute = confAtt;
        initializeCommands();
    }

    /**
     * Initializes the commands contained in this compound command
     */
    protected void initializeCommands()
    {
        // add this attribute to each current requirement.
        Resource requirementModel = RequirementUtils.getRequirementModel(editingDomain);
        for (Requirement requirement : RequirementUtils.getAllCurrents(requirementModel))
        {
            Attribute newAttribute = null;
            switch (attribute.getType().getValue())
            {
                case AttributesType.TEXT_VALUE:
                    newAttribute = RequirementFactory.eINSTANCE.createTextAttribute();
                    String value = attribute.getDefaultValue() != null ? attribute.getDefaultValue().getValue().getValue() : ""; //$NON-NLS-1$
                    ((TextAttribute) newAttribute).setValue(value);
                    break;
                case AttributesType.LINK_VALUE:
                    if (!(requirement instanceof AnonymousRequirement))
                    {
                        newAttribute = RequirementFactory.eINSTANCE.createAttributeLink();
                        ((AttributeLink) newAttribute).setPartial(false);
                    }
                    else
                    {
                        // A free text cannot own a #link_to....so, pass this iteration
                        continue;
                    }
                    break;
                case AttributesType.OBJECT_VALUE:
                    newAttribute = RequirementFactory.eINSTANCE.createObjectAttribute();
                    break;
                case AttributesType.ALLOCATE_VALUE:
                    newAttribute = RequirementFactory.eINSTANCE.createAttributeAllocate();
                    break;
                default:
                    break;
            }
            newAttribute.setName(attribute.getName());
            appendIfCanExecute(AddCommand.create(editingDomain, requirement, RequirementPackage.eINSTANCE.getRequirement_Attribute(), newAttribute));
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
