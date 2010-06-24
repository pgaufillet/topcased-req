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
package org.topcased.requirement.core.providers;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;

import ttm.Requirement;

/**
 * Label provider for Tree labels and icons displayed in the requirement property section
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class RequirementPropertyTreeLabelProvider extends AdapterFactoryLabelProvider
{

    /**
     * @param adapterFactory
     */
    public RequirementPropertyTreeLabelProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
    }
    
    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getImage(java.lang.Object)
     */
    @Override
    public Image getImage(Object object)
    {
        if (object instanceof Requirement)
        {
            return super.getImage((Requirement)object);
        }
        else if (object instanceof org.topcased.requirement.Requirement)
        {
            return super.getImage((org.topcased.requirement.Requirement)object);
        }
        return super.getImage(object);
    }
    
    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object object)
    {
        if (object instanceof Requirement)
        {
            return ((Requirement)object).getIdent();
        }
        else if (object instanceof org.topcased.requirement.Requirement)
        {
            return ((org.topcased.requirement.Requirement)object).getIdentifier();
        }
        return super.getText(object);
    }
    
}
