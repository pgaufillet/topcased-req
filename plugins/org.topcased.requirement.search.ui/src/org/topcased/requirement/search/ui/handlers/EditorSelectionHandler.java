package org.topcased.requirement.search.ui.handlers;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.search.ui.handlers.AbstractModelElementEditorSelectionHandler;
import org.eclipse.ui.IEditorPart;

/**
 * Defines entity responsible of editor selection handling.
 * 
 * In other words users defines here how the org.topcased.requirement.search double clicked result will be handled in terms of corresponding
 * editor selection.
 * 
 */
public class EditorSelectionHandler extends AbstractModelElementEditorSelectionHandler
{

    public boolean isCompatibleModelElementEditorSelectionHandler(IEditorPart part)
    {
        return false;
    }

    public IStatus handleOpenTreeEditorWithSelection(IEditorPart part, Object selection)
    {
        return Status.CANCEL_STATUS;
    }

    @Override
    protected String getNsURI()
    {
        return ""; // TODO: user to return appropriate nsURI
    }
}
