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

import java.util.Iterator;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.ecore.EObject;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;

/**
 * A Property tester who check if a selected current requirement is impacted
 * This tester is used in the popup menu of the current view to enable or disable the set as valid command
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class IsImpactedPropertyTester extends PropertyTester
{

    /**
     * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[],
     *      java.lang.Object)
     */
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
    {
        if (receiver instanceof CurrentRequirement)
        {
            CurrentRequirement current = (CurrentRequirement) receiver;
            if (current.isImpacted())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if (receiver instanceof HierarchicalElement)
        {
            for (Iterator<EObject> children = ((HierarchicalElement) receiver).eAllContents(); children.hasNext();)
            {
                EObject currEo = children.next();
                
                if (currEo instanceof CurrentRequirement)
                { 
                    if (((CurrentRequirement) currEo).isImpacted())
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }
}
