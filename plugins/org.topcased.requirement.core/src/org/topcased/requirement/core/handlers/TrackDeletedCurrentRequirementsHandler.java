package org.topcased.requirement.core.handlers;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.utils.RequirementUtils;

public class TrackDeletedCurrentRequirementsHandler extends DeleteCurrentRequirementsHandler
{

    @Override
    public void setEnabled(Object evaluationContext)
    {
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
        if(editor != null && services!=null)
        {
            EditingDomain domain = services.getEditingDomain(editor);
            if (RequirementUtils.getDeletedChapter(domain) == null)
            {
                super.setBaseEnabled(true);
            }
            else
            {
                super.setBaseEnabled(false);
            }
        }
    }

}
