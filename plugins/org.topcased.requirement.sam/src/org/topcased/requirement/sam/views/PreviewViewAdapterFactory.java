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
package org.topcased.requirement.sam.views;

import org.eclipse.core.runtime.IAdapterFactory;
import org.topcased.modeler.sam.editor.SAMEditor;
import org.topcased.requirement.sam.views.preview.IPreviewPage;
import org.topcased.requirement.sam.views.preview.PreviewPage;

/**
 * A factory for creating adapter related to Preview view.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class PreviewViewAdapterFactory implements IAdapterFactory
{

    /**
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public Object getAdapter(Object adaptableObject, Class adapterType)
    {
        if (adapterType == IPreviewPage.class && adaptableObject instanceof SAMEditor)
        {
            return new PreviewPage();
        }
        return null;
    }

    
    /**
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
     */
    @SuppressWarnings("unchecked")
    public Class[] getAdapterList()
    {
        return new Class[] {IPreviewPage.class};
    }

}
