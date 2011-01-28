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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.topcased.requirement.core.extensions.DropRestrictionManager;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
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
        if (receiver instanceof EditPart && ((EditPart) receiver).getViewer() != null)
        {
            EditDomain gefDomain = ((EditPart) receiver).getViewer().getEditDomain();
            IEditorPart editor = null;
            if (gefDomain instanceof DefaultEditDomain)
            {
                editor = ((DefaultEditDomain) gefDomain).getEditorPart();
                // get top editor in case of multi-tab editor
                editor = editor.getEditorSite().getPage().getActiveEditor();
            }
            else
            {
                editor = RequirementUtils.getCurrentEditor();
            }
            IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
            if (services != null)
            {
                EditingDomain domain = services.getEditingDomain(editor);

                // Check if the modeler is linked to requirements
                if (RequirementUtils.getRequirementModel(domain) != null && editor.getEditorInput() instanceof IFileEditorInput)
                {
                    IFile file = ((IFileEditorInput) editor.getEditorInput()).getFile();
                    // check restrictions
                    if (services.canCreateRequirement((EditPart) receiver))
                    {
                        EObject eobject = services.getEObject((EditPart) receiver);
                        return DropRestrictionManager.getInstance().isDropAllowed(file.getFileExtension(), eobject);
                    }
                }
            }
        }
        return false;
    }
}
