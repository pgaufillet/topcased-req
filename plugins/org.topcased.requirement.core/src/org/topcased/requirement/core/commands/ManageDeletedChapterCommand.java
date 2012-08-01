package org.topcased.requirement.core.commands;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.requirement.DeletedChapter;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.utils.RequirementUtils;

public class ManageDeletedChapterCommand extends AbstractCommand
{

    private EditingDomain editingDomain;

    private DeletedChapter previousDeletedChapter;

    private RequirementProject project;

    public ManageDeletedChapterCommand(EditingDomain domain)
    {
        editingDomain = domain;
    }

    public void execute()
    {
        project = RequirementUtils.getRequirementProject(editingDomain);
        previousDeletedChapter = RequirementUtils.getDeletedChapter(editingDomain);
        if (previousDeletedChapter == null)
        {
            previousDeletedChapter = RequirementFactory.eINSTANCE.createDeletedChapter();
            project.getChapter().add(previousDeletedChapter);
        }
        else
        {
            project.getChapter().remove(previousDeletedChapter);
        }
    }

    public void redo()
    {
        DeletedChapter deletedChapter = RequirementUtils.getDeletedChapter(editingDomain);
        if (deletedChapter == null)
        {
            project.getChapter().add(previousDeletedChapter);
        }
        else
        {
            project.getChapter().remove(previousDeletedChapter);
        }
    }

    public void undo()
    {
        redo();
    }

    @Override
    public boolean canExecute()
    {
        return true;
    }

    
}
