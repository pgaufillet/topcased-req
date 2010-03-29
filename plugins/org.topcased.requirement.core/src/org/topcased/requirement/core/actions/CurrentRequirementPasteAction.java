/*****************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
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

import java.util.Collection;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * This class defines the EMF <b>paste</b> command for the Current requirements.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class CurrentRequirementPasteAction extends RequirementAbstractEMFAction
{
    private EditingDomain editingDomain;
    /**
     * Constructor
     * 
     * @param selection The current selection done
     * @param editingDomain The editing domain to use.
     */
    public CurrentRequirementPasteAction(IStructuredSelection selection, EditingDomain editDomain)
    {
        super(Messages.getString("CurrentRequirementPasteAction.0"), selection, editDomain); //$NON-NLS-1$
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
        setActionDefinitionId("org.topcased.requirement.core.pasteModelObject"); //$NON-NLS-1$
        setToolTipText(Messages.getString("CurrentRequirementPasteAction.1")); //$NON-NLS-1$
        editingDomain = editDomain;
    }

    /**
     * @see org.topcased.requirement.core.actions.RequirementAbstractEMFAction#initialize()
     */
    @Override
    public void initialize()
    {
        if (doPaste())
        {
            setCommand(PasteFromClipboardCommand.class);
            setParam(new CommandParameter(getSelection().getFirstElement(), null, getSelection().size()));
        }
    }

    /**
     * FIXME : selection bug!!!
     * 
     * @see org.topcased.requirement.core.actions.RequirementAbstractEMFAction#endAction(org.eclipse.emf.common.command.CompoundCommand)
     */
    @Override
    public void endAction(CompoundCommand cmd)
    {
        CurrentPage currentPage = RequirementHelper.INSTANCE.getCurrentPage();
        Collection< ? > source = cmd.getAffectedObjects();
        CompoundCommand compound = new CompoundCommand("Renaming moving requirements");
        
        //Process the renaming of the newly pasted requirements 
        if (doPaste())
        { 
            for (Object currSource : source)
            {
                if (currSource instanceof CurrentRequirement)
                {
                    // rename the current requirement
                    CurrentRequirement requirement = (CurrentRequirement) currSource;
                    compound.appendIfCanExecute(RequirementHelper.INSTANCE.renameRequirement(requirement));                    
                }
            }
        }
        
        if (!compound.isEmpty() && compound.canExecute())
        {
            // Execute the renaming commands
            editingDomain.getCommandStack().execute(compound);
        }
        
        if (currentPage != null && compound.getAffectedObjects() != null)
        {
            currentPage.refreshViewer(true);
            //currentPage.getViewer().setSelection(new StructuredSelection((List<?>) compound.getAffectedObjects()), true);
        }
    }

    /**
     * Test if the paste action is authorized for the current selection
     * 
     * @return false, if the current selection is a SpecialChapter. true, if the current selection is a
     *         HierarchicalElement
     */
    private Boolean doPaste()
    {
        Object target = getSelection().getFirstElement();

        if (target instanceof SpecialChapter)
        {
            return false;
        }

        if (target instanceof HierarchicalElement)
        {
            return true;
        }

        return false;
    }

    /**
     * @see org.topcased.requirement.core.actions.RequirementAbstractEMFAction#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        if (super.isEnabled() && getEditingDomain().getClipboard() != null)
        {
            for (Object obj : getSelection().toList())
            {
                if (!(obj instanceof HierarchicalElement))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}