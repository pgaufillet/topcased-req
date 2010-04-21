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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This class extends {@link CompoundCommand} and handles requirement renaming when the name of a referenced element
 * change into a {@link HierarchicalElement}. At this moment, the contained requirements need to be renamed.<br>
 * This command is called when the F2 keyword is pressed from the modeler, or when the property name change from the
 * Property View.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 * 
 */
public class RenameRequirementCommand extends CompoundCommand
{
    /** A reference to the editing domain */
    private EditingDomain editingDomain;

    /** The requirement to rename */
    private EObject modelElement;

    /** The old attribute name */
    private String oldName;

    /** The new attribute name */
    private String newName;

    /**
     * Constructor
     * 
     * @param element The element whose the name changed.
     * @param oldValue The old attribute value
     * @param newValue The new attribute value
     */
    public RenameRequirementCommand(EObject element, String oldValue, String newValue)
    {
        this(TopcasedAdapterFactoryEditingDomain.getEditingDomainFor(element), element, oldValue, newValue);
    }

    /**
     * Constructor
     * 
     * @param domain The editing domain to use
     * @param element The element whose the name changed.
     * @param oldValue The old attribute value
     * @param newValue The new attribute value
     */
    public RenameRequirementCommand(EditingDomain domain, EObject element, String oldValue, String newValue)
    {
        super(Messages.getString("RenameRequirementCommand.0")); //$NON-NLS-1$
        editingDomain = domain;
        modelElement = element;
        oldName = oldValue;
        newName = newValue;
        initializeCommands();
    }

    /**
     * Initializes the commands contained in this compound command
     */
    protected void initializeCommands()
    {
        HierarchicalElement elt = RequirementUtils.getHierarchicalElementFor(modelElement);
        if (elt != null)
        {
            for (org.topcased.requirement.Requirement requirement : elt.getRequirement())
            {
                if (requirement instanceof CurrentRequirement)
                {
                    String identifier = requirement.getIdentifier();
                    if (oldName == null)
                    {
                        identifier = identifier.replaceFirst("null", newName); // null is the default value //$NON-NLS-1$
                    }
                    else if ("".equals(oldName)) //$NON-NLS-1$
                    {
                        identifier = identifier.replaceFirst("__", "_" + newName + "_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    }
                    else
                    {
                        identifier = identifier.replaceFirst(oldName, newName);
                    }
                    appendIfCanExecute(SetCommand.create(editingDomain, requirement, RequirementPackage.eINSTANCE.getIdentifiedElement_Identifier(), identifier));
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
        return super.canExecute() && modelElement != null && !oldName.equals(newName);
    }
}
