package org.topcased.requirement.core.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.core.utils.RequirementUtils;

public class DeleteRequirementCommand extends DeleteCommand
{

    protected List<CurrentRequirement> deletedReqs;

    protected SpecialChapter deletedChapter;

    public DeleteRequirementCommand(EditingDomain domain, Collection< ? > collection)
    {
        super(domain, collection);
    }

    @Override
    public void execute()
    {
        deletedChapter = RequirementUtils.getDeletedChapter(domain);

        if (deletedChapter != null)
        {
            deletedReqs = new ArrayList<CurrentRequirement>();
            List<CurrentRequirement> reqs = new ArrayList<CurrentRequirement>();

            for (Object wrappedObject : collection)
            {
                Object object = AdapterFactoryEditingDomain.unwrap(wrappedObject);
                if (object instanceof CurrentRequirement)
                {
                    reqs.add((CurrentRequirement) object);
                }
                else if (object instanceof EObject)
                {
                    for (Iterator<EObject> j = ((EObject) object).eAllContents(); j.hasNext();)
                    {
                        EObject eo = j.next();
                        if (eo instanceof CurrentRequirement)
                        {
                            reqs.add((CurrentRequirement) eo);
                        }
                    }
                }
                else if (object instanceof Resource)
                {
                    for (Iterator<EObject> j = ((Resource) object).getAllContents(); j.hasNext();)
                    {
                        EObject eo = j.next();
                        if (eo instanceof CurrentRequirement)
                        {
                            reqs.add((CurrentRequirement) eo);
                        }
                    }
                }
            }

            for (CurrentRequirement req : reqs)
            {
                CurrentRequirement ghostReq = RequirementFactory.eINSTANCE.createCurrentRequirement();
                ghostReq.setIdentifier(req.getIdentifier());

                if (!deletedChapter.getRequirement().contains(req))
                {
                    deletedChapter.getRequirement().add(ghostReq);
                    deletedReqs.add(ghostReq);
                }
            }
        }
        
        super.execute();
    }

    @Override
    public void undo()
    {
        if (deletedChapter != null)
        {
            deletedChapter.getRequirement().removeAll(deletedReqs);
        }
        super.undo();
    }

    @Override
    public void redo()
    {
        if (deletedChapter != null)
        {
            deletedChapter.getRequirement().addAll(deletedReqs);
        }
        super.redo();
    }

}
