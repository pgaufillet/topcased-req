/***********************************************************************************************************************
 * Copyright (c) 2008-2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.tabbedproperties.AbstractTabbedPropertySheetPage;

/**
 * Customized property sheet page for requirement management.
 * 
 * Creation 16 dec. 2008
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class RequirementPropertySheetPage extends AbstractTabbedPropertySheetPage
{

    /**
     * Constructor for this property sheet page.
     * 
     * @param editor the editor contributor of the property sheet page.
     */
    public RequirementPropertySheetPage(ITabbedPropertySheetPageContributor contributor)
    {
        super(contributor);
    }

    /**
     * @see org.topcased.tabbedproperties.AbstractTabbedPropertySheetPage#getAdapterFactories()
     */
    @Override
    public List<AdapterFactory> getAdapterFactories()
    {
        List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
        factories.add(RequirementUtils.getAdapterFactory());
        factories.addAll(super.getAdapterFactories());
        return factories;
    }
}
