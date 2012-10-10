/*****************************************************************************
 * Copyright (c) 2012 Atos.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthieu Boivineau - Initial API and implementation
 *    
 ******************************************************************************/
package org.topcased.requirement.core.transformation.impl.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.topcased.requirement.core.transformation.ITransformation;

public class TransformationLabelProvider extends LabelProvider
{
    
    @Override
    public String getText(Object element) 
    {
        if (element instanceof ITransformation)
        {
            return ((ITransformation)element).getText();
        }
        return super.getText(element);
    }

}
