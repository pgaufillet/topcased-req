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
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * This handler adds an {@link Attribute} of a given type for a {@link Requirement}.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class AddAttributeHandler extends AbstractHandler
{

    private List<EObject> toSelect;

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        toSelect = new ArrayList<EObject>();
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
        CurrentPage page = RequirementHelper.INSTANCE.getCurrentPage();

        if (services != null && ((EvaluationContext) event.getApplicationContext()).getDefaultVariable() instanceof List< ? >)
        {
            EditingDomain editingDomain = services.getEditingDomain(editor);
            // Get the current selection
            List< ? > elements = ((List< ? >) ((EvaluationContext) event.getApplicationContext()).getDefaultVariable());
            CompoundCommand compoundCmd = new CompoundCommand(Messages.getString("AddAttributeHandler.1")); //$NON-NLS-1$
            for (Object currObject : elements)
            {
                EObject parent = null;
                if (currObject instanceof Attribute)
                {
                    parent = ((Attribute) currObject).eContainer();
                    int newIndex = calculateIndex(parent, (EObject) currObject);

                    Attribute toDuplicate = null;
                    if (currObject instanceof AttributeLink)
                    {
                        toDuplicate = RequirementHelper.INSTANCE.duplicateAttributeLink((AttributeLink) currObject);
                    }
                    else if (currObject instanceof ObjectAttribute)
                    {
                        toDuplicate = RequirementHelper.INSTANCE.duplicateObjectAttribute((ObjectAttribute) currObject);
                    }
                    if (parent != null && toDuplicate != null)
                    {
                        toSelect.add(toDuplicate);
                        compoundCmd.appendIfCanExecute(AddCommand.create(editingDomain, parent, RequirementPackage.eINSTANCE.getRequirement_Attribute(), toDuplicate, newIndex));
                    }
                }
            }
            if (!compoundCmd.isEmpty() && compoundCmd.canExecute())
            {
                editingDomain.getCommandStack().execute(compoundCmd);
                page.setSelection(new StructuredSelection(toSelect));
            }
        }
        return null;
    }

    /**
     * Deduces the index where the new attribute must be inserted.
     * 
     * @param parent The container of the child model object.
     * @param child An object belonging to an EList
     * @return The index where Attribute must be inserted
     */
    private int calculateIndex(EObject parent, EObject child)
    {
        int index = ((Requirement) parent).getAttribute().size();
        if (parent instanceof Requirement)
        {
            EList<Attribute> attList = ((Requirement) parent).getAttribute();
            index = attList.indexOf(child) + 1;
        }
        return index;
    }

}
