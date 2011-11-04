/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.resourceloading.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.topcased.requirement.core.views.current.model.CurrentRequirementReference;

/**
 * Test if the ISelection is instance of {@link CurrentRequirementReference} and if it loaded or not. Return true if not
 * loaded
 * 
 * @author Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * 
 */
public class IsCurrentRequirementLoaded extends PropertyTester
{

    public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
    {
        if (receiver instanceof IStructuredSelection)
        {
            IStructuredSelection sel = (IStructuredSelection) receiver;
            Object selectedObject = sel.getFirstElement();
            if (selectedObject instanceof CurrentRequirementReference)
            {
                return !((CurrentRequirementReference) selectedObject).isResourceLoaded();
            }
        }
        return false;
    }

}
