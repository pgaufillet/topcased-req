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
package org.topcased.requirement.core.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeAllocate;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * This action adds an {@link Attribute} of a given type for a {@link Requirement}.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class AddAttributeAction extends RequirementAction
{

    private List<EObject> toSelect;

    /**
     * Constructor
     * 
     * @param selection The selection done
     * @param treeViewer The tree viewer
     * @param editingDomain The editing domain to use
     */
    public AddAttributeAction(IStructuredSelection selection, CurrentPage page)
    {
        super(selection, page.getViewer(), page.getEditingDomain());
        AdapterFactoryLabelProvider factory = new AdapterFactoryLabelProvider(RequirementUtils.getAdapterFactory());
        setImageDescriptor(ImageDescriptor.createFromImage(factory.getImage(selection.getFirstElement())));
        setText(Messages.getString("AddAttributeAction.0")); //$NON-NLS-1$
    }

    /**
     * @see org.topcased.requirement.core.actions.RequirementAction#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        if (super.isEnabled())
        {
            if (selection != null && !selection.isEmpty())
            {
                Object first = selection.getFirstElement();
                for (Object obj : selection.toList())
                {
                    if (obj instanceof AttributeAllocate || !first.getClass().equals(obj.getClass()))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run()
    {
        toSelect = new ArrayList<EObject>();
        CompoundCommand compoundCmd = new CompoundCommand(Messages.getString("AddAttributeAction.1")); //$NON-NLS-1$
        for (Object currObject : selection.toList())
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
            viewer.setSelection(new StructuredSelection(toSelect), true);
        }
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
