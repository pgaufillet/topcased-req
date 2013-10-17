/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.properties.filters;

import org.eclipse.jface.viewers.IFilter;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributesType;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * A Filter that checks if the TextAttribute has multi values.
 * 
 * Creation : 16 Dec. 2008
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class TextAttributeMultiValueFilter implements IFilter
{
    /**
     * @see org.eclipse.jface.viewers.IFilter#select(java.lang.Object)
     */
    public boolean select(Object toTest)
    {
        if (toTest instanceof TextAttribute)
        {
            TextAttribute attribute = (TextAttribute) toTest;
            AttributeConfiguration configuration = RequirementUtils.getAttributeConfiguration(attribute.eResource());
            // TODO find a way to get Attribute configuration
            if (configuration != null){
                for (ConfiguratedAttribute att : configuration.getListAttributes())
                {
                    if (AttributesType.TEXT.equals(att.getType()) && attribute.getName().equals(att.getName()))
                    {
                        return att.getListValue().size() > 1;
                    }
                }
            }
        }
        return false;
    }

}
