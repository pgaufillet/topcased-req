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

package org.topcased.requirement.core.views;

import org.eclipse.core.runtime.IAdapterFactory;
import org.topcased.modeler.editor.Modeler;
import org.topcased.requirement.core.views.current.CurrentPage;
import org.topcased.requirement.core.views.current.ICurrentRequirementPage;
import org.topcased.requirement.core.views.upstream.IUpstreamRequirementPage;
import org.topcased.requirement.core.views.upstream.UpstreamPage;

/**
 * A factory for creating Adapter objects.
 */
public class RequirementAdapterFactory implements IAdapterFactory
{

    @SuppressWarnings("unchecked")
    public Object getAdapter(Object adaptableObject, Class adapterType)
    {

        if (adapterType == IUpstreamRequirementPage.class && adaptableObject instanceof Modeler)
        {
            return new UpstreamPage();
        }
        else if (adapterType == ICurrentRequirementPage.class && adaptableObject instanceof Modeler)
        {
            return new CurrentPage();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Class[] getAdapterList()
    {
        return new Class[] {ICurrentRequirementPage.class, IUpstreamRequirementPage.class};
    }

}
