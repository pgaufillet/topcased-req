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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.core.dialogs.UpdateAttributeDialog;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * The action "Update attribut" accessible from a set of hierarchical elements or a set of requirements (anonymous or
 * current).
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class UpdateAttributeAction extends RequirementAction
{
    private String attributeName;

    private EObject eObject;

    private String text;

    private CompoundCommand compoundCmd;

    /**
     * Constructor
     * 
     * @param selection The selection done
     * @param treeViewer The tree viewer
     * @param editingDomain The editing domain to use
     */
    public UpdateAttributeAction(IStructuredSelection selection, CurrentPage page)
    {
        super(selection, page.getViewer(), page.getEditingDomain());
        this.selection = wrapSelection(selection);
        setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/update.gif")); //$NON-NLS-1$
        setText(Messages.getString("UpdateAttributeAction.0")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run()
    {
        UpdateAttributeDialog dialog = new UpdateAttributeDialog(selection, viewer.getControl().getShell());
        if (dialog.open() == Dialog.OK)
        {
            setAttributeName(dialog.getAttributeName());
            setEObject(dialog.getAttributeValueObject());
            setText(dialog.getAttributeValueText());
            updateAttribute();
        }
    }

    /**
     * @see org.eclipse.jface.action.Action#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        for (Object obj : selection.toList())
        {
            if (!(obj instanceof HierarchicalElement || obj instanceof Requirement))
            {
                return false;
            }
        }
        return super.isEnabled();
    }

    public void updateAttribute()
    {
        compoundCmd = new CompoundCommand(Messages.getString("UpdateAttributeAction.0")); //$NON-NLS-1$

        for (Iterator< ? > iter = selection.iterator(); iter.hasNext();)
        {
            Object currObject = iter.next();

            if (currObject instanceof Requirement)
            {
                updateAttribute((Requirement) currObject);
            }
        }

        if (!compoundCmd.isEmpty() && compoundCmd.canExecute())
        {
            editingDomain.getCommandStack().execute(compoundCmd);
        }
    }

    private void updateAttribute(Requirement currReq)
    {
        for (Attribute attribute : currReq.getAttribute())
        {
            if (attribute.getName().equals(getAttributeName()))
            {
                if (attribute instanceof TextAttribute)
                {
                    updateAttribute((TextAttribute) attribute);
                }
                else if (attribute instanceof ObjectAttribute)
                {
                    updateAttribute((ObjectAttribute) attribute);
                }
            }
        }
    }

    /**
     * Updates the value field of a {@link TextAttribute} model object thanks to a basic EMF Command.
     * 
     * @param attribute a {@link TextAttribute} model object
     */
    private void updateAttribute(TextAttribute attribute)
    {
        compoundCmd.appendIfCanExecute(new SetCommand(editingDomain, attribute, RequirementPackage.eINSTANCE.getTextAttribute_Value(), getText()));
    }

    /**
     * Updates the value field of a {@link ObjectAttribute} model object thanks to a basic EMF Command.
     * 
     * @param attribute a {@link ObjectAttribute} model object
     */
    private void updateAttribute(ObjectAttribute attribute)
    {
        compoundCmd.appendIfCanExecute(new SetCommand(editingDomain, attribute, RequirementPackage.eINSTANCE.getObjectAttribute_Value(), getEObject()));
    }

    public String getAttributeName()
    {
        return attributeName;
    }

    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }

    public EObject getEObject()
    {
        return eObject;
    }

    public void setEObject(EObject object)
    {
        eObject = object;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    /**
     * Wraps the selection in order to obtain only {@link Requirement}.
     * 
     * @param oldSelection The selection received in the constructor.
     * @return a new {@link IStructuredSelection} without {@link HierarchicalElement}.
     */
    private IStructuredSelection wrapSelection(IStructuredSelection oldSelection)
    {
        Set<Requirement> newList = new HashSet<Requirement>();

        for (Object obj : oldSelection.toList())
        {
            if (obj instanceof HierarchicalElement)
            {
                newList.addAll(RequirementUtils.getCurrents((HierarchicalElement) obj));
            }
            else if (obj instanceof Requirement)
            {
                newList.add((Requirement) obj);
            }
        }
        return new StructuredSelection(newList.toArray());
    }

}
