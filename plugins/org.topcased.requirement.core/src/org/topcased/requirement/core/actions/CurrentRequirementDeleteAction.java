/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.actions;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeAllocate;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.core.Messages;

/**
 * This class defines the EMF <b>delete</b> command for the Current Requirements.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class CurrentRequirementDeleteAction extends RequirementAbstractEMFAction
{

    /**
     * Constructor
     * 
     * @param selection The current selection done
     * @param editingDomain The editing domain to use.
     */
    public CurrentRequirementDeleteAction(IStructuredSelection selection, EditingDomain editingDomain)
    {
        super(Messages.getString("CurrentRequirementView.23"), selection, editingDomain); //$NON-NLS-1$
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
        setToolTipText(Messages.getString("CurrentRequirementView.24")); //$NON-NLS-1$
    }

    /**
     * @see org.topcased.requirement.core.actions.RequirementAbstractEMFAction#initialize()
     */
    @Override
    public void initialize()
    {
        setCommand(DeleteCommand.class);
        setParam(new CommandParameter(null, null, getSelection().toList()));
    }

    /**
     * @see org.topcased.requirement.core.actions.RequirementAbstractEMFAction#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        if (super.isEnabled())
        {
            for (Object obj : getSelection().toList())
            {
                if (obj instanceof AttributeAllocate || obj instanceof TextAttribute || obj instanceof SpecialChapter || obj instanceof ConfiguratedAttribute || obj instanceof AttributeConfiguration
                        || obj instanceof AttributeValue)
                {
                    return false;
                }
                else if (obj instanceof ObjectAttribute)
                {
                    return notLastAttribute((ObjectAttribute) obj);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Before deleting an object of kind {@link ObjectAttribute}, we have to check that it is not the last one among the
     * list.<br>
     * 
     * Note : /!\ {@link AttributeAllocate} and {@link AttributeLink} are of kind {@link ObjectAttribute} /!\
     * 
     * @param attribute The attribute to test
     * @return <code>true</code> if pass this test, <code>false</code> otherwise.
     */
    private boolean notLastAttribute(ObjectAttribute attribute)
    {
        Requirement parent = (Requirement) attribute.eContainer();
        EList<Attribute> allAttributes = parent.getAttribute();

        if (attribute instanceof AttributeLink)
        {
            int numberAtt = 0;
            for (Attribute current : allAttributes)
            {
                if (current instanceof AttributeLink)
                {
                    numberAtt++;
                }
            }
            return numberAtt > 1 ? true : false;
        }
        else
        {
            int numberAtt = 0;
            for (Attribute current : allAttributes)
            {
                if (current instanceof ObjectAttribute && !(current instanceof AttributeAllocate || current instanceof AttributeLink))
                {
                    numberAtt++;
                }
            }
            return numberAtt > 1 ? true : false;
        }
    }

}