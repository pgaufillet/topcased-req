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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.topcased.modeler.di.model.Property;
import org.topcased.modeler.editor.Modeler;
import org.topcased.requirement.generic.actions.CurrentSearchFilter;
import org.topcased.sam.requirement.core.views.SearchComposite;
import org.topcased.sam.requirement.core.views.current.CurrentPage;

/**
 * The Class CustomCurrentPage.
 */
public class CustomCurrentPage extends CurrentPage
{

    private Modeler modeler;

    public CustomCurrentPage(Modeler adaptableObject)
    {
        modeler = adaptableObject;
    }

    @Override
    public void createControl(Composite parent)
    {
        super.createControl(parent);
        if (!Activator.isCurrentEditorSamEditor(modeler))
        {
            Property property = Injector.getProperty(modeler.getResourceSet().getResources().get(0).getContents().get(0));
            if (property != null)
            {
                String uri = URI.createURI(property.getValue()).trimFragment().resolve(property.eResource().getURI()).toString();
                Injector injector = Injector.getInstance();
                injector.initCurrent(this, modeler, uri);
            }
        }
        injectSearch();
    }

    private void injectSearch()
    {
        Composite mainCompo = mainComposite;
        for (Control c : mainCompo.getChildren())
        {
            if (c instanceof SearchComposite)
            {
                SearchComposite search = (SearchComposite) c;
                search.dispose();
            }
        }
        CustomSearchComposite search = new CustomSearchComposite(mainCompo, SWT.None);
        search.setFilter(getViewer(), CurrentSearchFilter.getInstance());
        mainCompo.layout(true);
    }

}
