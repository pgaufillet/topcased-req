/***********************************************************************************************************************
 * Copyright (c) 2010 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.providers;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.topcased.requirement.util.RequirementResource;

import ttm.Document;

/**
 * Label provider used for the Checkbox tree viewer allowing the user to select {@link Document} to import to his
 * current requirement model.
 * 
 * Creation : 06 december 2010<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since TOPCASED 4.3.0
 */
public class DocumentSelectionLabelProvider extends AdapterFactoryLabelProvider
{
    public DocumentSelectionLabelProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object object)
    {
        if (object instanceof RequirementResource)
        {
            String encodedURI =((RequirementResource) object).getURI().lastSegment(); 
            return URI.decode(encodedURI);
        }
        return super.getText(object);
    }
}
