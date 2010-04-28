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
import org.eclipse.emf.common.util.EList;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeAllocate;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.Requirement;

/**
 * A property tester to check if the ObjectAttribute selected is the last element of a current requirement
 * This property tester is used by the current requirement view to enable or disable delete commands
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public class NotLastAttributePropertyTester extends PropertyTester
{
    /**
     * Before deleting an object of kind {@link ObjectAttribute}, we have to check that it is not the last one among the
     * list.<br>
     * 
     * Note : /!\ {@link AttributeAllocate} and {@link AttributeLink} are of kind {@link ObjectAttribute} /!\
     * 
     * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
     * 
     * @param attribute The attribute to test
     * @return <code>true</code> if pass this test, <code>false</code> otherwise.
     */
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
    {
        if (receiver instanceof ObjectAttribute)
        {
            ObjectAttribute attribute = (ObjectAttribute) receiver;
            Requirement parent = (Requirement) attribute.eContainer();
            EList<Attribute> allAttributes = parent.getAttribute();
    
            if (attribute instanceof AttributeLink)
            {
                int numberAtt = 0;
                for (Attribute current : allAttributes)
                {
                    if (current instanceof AttributeLink)
                    {
                        numberAtt++;
                    }
                }
                return numberAtt > 1 ? true : false;
            }
            else
            {
                int numberAtt = 0;
                for (Attribute current : allAttributes)
                {
                    if (current instanceof ObjectAttribute && !(current instanceof AttributeAllocate || current instanceof AttributeLink))
                    {
                        numberAtt++;
                    }
                }
                return numberAtt > 1 ? true : false;
            }
        }
        return false;
    }
}
