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
package org.topcased.requirement.core.testers;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;

/**
 * A Property tester which checks if the active editor supports the attachment of requirements. This tester is used in
 * the toolbar menu to enable or disable the attachment action.
 * 
 * @author vhemery
 */
public class EnablesRequirementsPropertyTester extends PropertyTester
{

    /**
     * Test if the active editor supports attachment of requirements.
     * 
     * @param receiver the active editor to test
     * @param property the property to test (unused)
     * @param args additional arguments (an array of length 0)
     * @param expectedValue the expected value of the property.
     * 
     * @return returns <code>true</code> if the active editor supports attachment of requirements
     * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[],
     *      java.lang.Object)
     */
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
    {
        if (receiver instanceof IEditorPart)
        {
            String key = SupportingEditorsManager.getInstance().getKey((IEditorPart) receiver);
            return key != null;
        }
        return false;
    }
}
