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
package org.topcased.requirement.merge.testers;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.topcased.modeler.utils.Utils;

/**
 * A simple property tester to check if the current IFile selected is a ".di" file,
 * And also check that no currentModeler is opened (prevent problems when modifying requirement file while opened)
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public class CanMergePropertyTester extends PropertyTester
{

    /**
     * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[], java.lang.Object)
     */
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
    {
        if (Utils.getCurrentModeler() == null)
        {
            if (receiver instanceof IFile)
            {
                IFile file = (IFile) receiver;
                
                if (file.getFileExtension().endsWith("di")) //$NON-NLS-1$
                {
                    return true;
                }
            }
        }
        return false;
    }

}
