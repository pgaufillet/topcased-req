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
package org.topcased.requirement.core.providers;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.topcased.modeler.editor.properties.ModelerLabelProvider;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Label provider for Requirement toolkit. <br>
 * 
 * Creation 17 dec. 2008
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 * 
 */
public class RequirementModelerLabelProvider extends ModelerLabelProvider
{
    /**
     * @see org.topcased.modeler.editor.properties.ModelerLabelProvider#getAdapterFactoryLabelProvider()
     */
    @Override
    protected AdapterFactoryLabelProvider getAdapterFactoryLabelProvider()
    {
        return new CurrentRequirementLabelProvider(RequirementUtils.getAdapterFactory());
    }
}
