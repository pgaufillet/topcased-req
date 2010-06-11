/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 *                Sebastien GABEL (CS) - Bug fix when context are changing.
 * 
 *****************************************************************************/
package org.topcased.requirement.core.testers;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.gef.EditPart;
import org.eclipse.ui.IFileEditorInput;
import org.topcased.modeler.edit.IModelElementEditPart;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.editor.ModelerGraphicalViewer;
import org.topcased.requirement.core.extensions.DropRestrictionManager;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * A Property tester who check if a IModelElementEditPart of a modeler can have requirement creation This property This
 * tester is used in the popup menu of a modeler to enable or disable requirement creation commands
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class CanCreateRequirementPropertyTester extends PropertyTester
{

    /**
     * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[],
     *      java.lang.Object)
     */
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
    {
        if (receiver instanceof IModelElementEditPart)
        {
            ModelerGraphicalViewer graphicalEditor = (ModelerGraphicalViewer) ((EditPart) receiver).getViewer();
            Modeler modeler = graphicalEditor.getModelerEditor();
            // Check if the modeler is linked to requirements
            if (RequirementUtils.getRequirementModel(modeler.getEditingDomain()) != null)
            {
                IFile file = ((IFileEditorInput) modeler.getEditorInput()).getFile();
                IModelElementEditPart editPart = (IModelElementEditPart) receiver;
                return DropRestrictionManager.getInstance().isDropAllowed(file.getFileExtension(), editPart.getEObject());
            }
        }
        return false;
    }
}
