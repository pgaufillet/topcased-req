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
import org.topcased.requirement.ConfiguratedAttribute;

/**
 * A Filter that checks if the {@link ConfiguratedAttribute} has a not empty list of <b>AttributeValue</b>.
 * 
 * Creation : 16 Dec. 2008
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class ConfiguratedAttributeFilter implements IFilter
{
    /**
     * @see org.eclipse.jface.viewers.IFilter#select(java.lang.Object)
     */
    public boolean select(Object toTest)
    {
        if (toTest instanceof ConfiguratedAttribute)
        {
            ConfiguratedAttribute attribute = (ConfiguratedAttribute) toTest;
            return !attribute.getListValue().isEmpty();
        }
        return false;
    }

}
