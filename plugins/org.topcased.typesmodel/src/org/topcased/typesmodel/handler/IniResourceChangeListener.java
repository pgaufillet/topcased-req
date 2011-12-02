/*****************************************************************************
 * Copyright (c) 2011 Atos.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Anass RADOUANI (Atos) anass.radouani@atos.net - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.typesmodel.handler;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;

/**
 *  listener of resource changes 
 */
public class IniResourceChangeListener implements IResourceChangeListener
{

    private final IniManagerRegistry visitor;

    /**
     * Constructor
     * @param visitor resource visitor
     */
    public IniResourceChangeListener(IniManagerRegistry visitor)
    {
        this.visitor = visitor;
    }

    public void resourceChanged(IResourceChangeEvent event)
    {
        try
        {
            event.getDelta().accept(visitor);
        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }
    }

}
