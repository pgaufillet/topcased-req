/*****************************************************************************
 * Copyright (c) 2010 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Vincent Hemery (Atos Origin}) {vincent.hemery@atosorigin.com} - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.core.commands;

import org.eclipse.emf.common.command.Command;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentRequirementView;
import org.topcased.requirement.core.views.upstream.UpstreamRequirementView;

/**
 * This utility class allow to construct commands to refresh requirement views regarding requirement attachment
 * properties.
 * 
 * @author vhemery
 */
public class RefreshRequirementsPropertiesCommand
{
    /**
     * Get command to refresh at both undo and redo
     * 
     * @param editor the active editor
     * @return refresh command
     */
    public static Command getCommand(final IEditorPart editor)
    {
        return new CommandStub()
        {
            public void redo()
            {
                refresh(editor);
            }

            @Override
            public void undo()
            {
                refresh(editor);
            }
        };
    }

    /**
     * Get command to refresh at redo only
     * 
     * @param editor the active editor
     * @return refresh command
     */
    public static Command getAtRedo(final IEditorPart editor)
    {
        return new CommandStub()
        {
            public void redo()
            {
                refresh(editor);
            }
        };
    }

    /**
     * Get command to refresh at undo only
     * 
     * @param editor the active editor
     * @return refresh command
     */
    public static Command getAtUndo(final IEditorPart editor)
    {
        return new CommandStub()
        {
            public void redo()
            {
            }

            @Override
            public void undo()
            {
                refresh(editor);
            }
        };
    }

    /**
     * Refresh requirement views regarding requirement attachment properties
     */
    protected static void refresh(IEditorPart editor)
    {
        if (editor.getAdapter(org.eclipse.emf.edit.domain.EditingDomain.class) == null)
        {
            // editor is disposed, take current editor instead
            editor = RequirementUtils.getCurrentEditor();
        }
        // Notify views that the diagram property has changed
        CurrentRequirementView currentRequirementView = (CurrentRequirementView) CurrentRequirementView.getInstance();
        if (currentRequirementView != null)
        {
            currentRequirementView.partActivated(editor);
        }
        UpstreamRequirementView upstreamRequirementView = (UpstreamRequirementView) UpstreamRequirementView.getInstance();
        if (upstreamRequirementView != null)
        {
            upstreamRequirementView.partActivated(editor);
        }
    }
}
