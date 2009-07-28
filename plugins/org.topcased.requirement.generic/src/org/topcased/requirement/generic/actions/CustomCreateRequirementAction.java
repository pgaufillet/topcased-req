package org.topcased.requirement.generic.actions;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jface.action.Action;
import org.topcased.requirement.generic.Drop;
import org.topcased.sam.requirement.HierarchicalElement;
import org.topcased.sam.requirement.Requirement;
import org.topcased.sam.requirement.core.Messages;
import org.topcased.sam.requirement.core.RequirementPlugin;
import org.topcased.sam.requirement.core.utils.RequirementUtils;

public class CustomCreateRequirementAction extends Action
{

    private Object selection;

    public CustomCreateRequirementAction(Object selection)
    {
        super();
        this.selection = selection;
        setText(Messages.getString("CreateCurrentRequirementAction.0"));
        setImageDescriptor(RequirementPlugin.getImageDescriptor("icons/current.gif")); //$NON-NLS-1$
    }

    @Override
    public void run()
    {
        if (selection instanceof Requirement)
        {
            selection = ((Requirement) selection).eContainer();
        }
        if (selection instanceof HierarchicalElement)
        {
            HierarchicalElement hierarchicalElt = (HierarchicalElement) selection;
            Drop.addNewRequirement(null,RequirementUtils.getRequirementProject(((HierarchicalElement) selection).eResource()), new CompoundCommand(), hierarchicalElt);
        }
    }

}
