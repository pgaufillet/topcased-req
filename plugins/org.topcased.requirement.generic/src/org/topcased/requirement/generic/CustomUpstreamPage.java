/*****************************************************************************
 * Copyright (c) 2009 ATOS ORIGIN INTEGRATION.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) tristan.faure@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.generic;

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.widgets.Composite;
import org.topcased.modeler.di.model.Property;
import org.topcased.modeler.editor.Modeler;
import org.topcased.sam.requirement.core.views.upstream.UpstreamPage;

/**
 * The Class CustomUpstreamPage.
 */
public class CustomUpstreamPage extends UpstreamPage
{
    private Modeler modeler;

    public CustomUpstreamPage(Modeler adaptableObject)
    {
        modeler = (Modeler) adaptableObject;
    }

    @Override
    public void createControl(Composite parent)
    {
        super.createControl(parent);
        if (!Activator.isCurrentEditorSamEditor(modeler))
        {
//            System.out.println("PAS SAM");
            Property property = Injector.getProperty(modeler.getResourceSet().getResources().get(0).getContents().get(0));
            if (property != null)
            {
//                System.out.println("PROPOERTY ok");
                String uri = URI.createURI(property.getValue()).trimFragment().resolve(property.eResource().getURI()).toString();
                Injector injector = Injector.getInstance();
                injector.initUpstream(this, modeler, uri);
            }
        }
    }

    @Override
    public void dispose()
    {
        super.dispose();
        editingDomain = null ;
        modeler = null ;
    }
    
    

}
