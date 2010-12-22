/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/
package org.topcased.requirement.core.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.core.dialogs.UpdateAttributeDialog;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * The "Update attribute" handler is accessible from a set of hierarchical elements or a set of requirements (anonymous
 * or current).
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class UpdateAttributeHandler extends AbstractHandler
{

    private String attributeName;

    private EObject eObject;

    private String text;

    private CompoundCommand compoundCmd;

    private EditingDomain editingDomain;

    private List< ? > selection;

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
        if (services != null)
        {
            editingDomain = services.getEditingDomain(editor);
        }
        CurrentPage page = RequirementHelper.INSTANCE.getCurrentPage();

        if (((EvaluationContext) event.getApplicationContext()).getDefaultVariable() instanceof List< ? >)
        {
            // Get the current selection and wrap it
            selection = wrapSelection(((List< ? >) ((EvaluationContext) event.getApplicationContext()).getDefaultVariable()));

            UpdateAttributeDialog dialog = new UpdateAttributeDialog(selection, page.getViewer().getControl().getShell());
            if (dialog.open() == Dialog.OK)
            {
                setAttributeName(dialog.getAttributeName());
                setEObject(dialog.getAttributeValueObject());
                setText(dialog.getAttributeValueText());
                updateAttribute();
            }
        }
        return null;
    }

    /**
     * Update the attributes
     */
    public void updateAttribute()
    {
        compoundCmd = new CompoundCommand(Messages.getString("UpdateAttributeHandler.0")); //$NON-NLS-1$

        for (Object currObject : selection)
        {
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

    /**
     * Update the attributes
     */
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
     * @param oldSelection The selection received from the event.
     * @return a new {@link List} without {@link HierarchicalElement}.
     */
    private List< ? > wrapSelection(List< ? > oldSelection)
    {
        List<Requirement> newList = new ArrayList<Requirement>();

        for (Object obj : oldSelection)
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
        return (List< ? >) newList;
    }
}
