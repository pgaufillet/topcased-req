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
package org.topcased.requirement.core.testers;

import org.eclipse.core.expressions.PropertyTester;
import org.topcased.modeler.edit.IModelElementEditPart;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.extensions.DropRestrictionManager;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * A Property tester who check if a IModelElementEditPart of a modeler can have requirement creation This property
 * tester is used in the popup menu of a modeler to enable or disable requirement creation commands
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class CanCreateRequirementPropertyTester extends PropertyTester
{

    /**
     * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[],
     *      java.lang.Object)
     */
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
    {
        // Check if the modeler is linked to requirements
        if (RequirementUtils.getRequirementModel(Utils.getCurrentModeler().getEditingDomain()) != null)
        {
            String fileExtension = Modeler.getCurrentIFile().getFileExtension();

            if (receiver instanceof IModelElementEditPart)
            {
                // Check if the selected element has drop allowed for it
                IModelElementEditPart editPart = (IModelElementEditPart) receiver;
                return DropRestrictionManager.getInstance().isDropAllowed(fileExtension, editPart.getEObject());
            }
            else
            {
                return false;
            }
        }
        return false;

    }
}
