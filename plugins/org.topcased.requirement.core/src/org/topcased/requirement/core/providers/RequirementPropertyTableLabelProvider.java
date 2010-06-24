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
import org.topcased.requirement.core.internal.Messages;

/**
 * Label provider for Table labels and icons displayed in the requirement property section
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class RequirementPropertyTableLabelProvider extends AdapterFactoryLabelProvider
{
    public RequirementPropertyTableLabelProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getColumnImage(java.lang.Object, int)
     */
    @Override
    public Image getColumnImage(Object element, int columnIndex)
    {
        if (element instanceof RequirementCRegistry)
        {
            RequirementCRegistry tuple = (RequirementCRegistry) element;
            if (columnIndex == 0)
            {
                return super.getImage(tuple.getCurrent());
            }
            if (columnIndex == 1)
            {
                if (tuple.getUpstream() != null)
                {
                    return super.getImage(tuple.getUpstream());
                }
            }
        }
        return super.getColumnImage(element, columnIndex);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object element, int columnIndex)
    {
        if (element instanceof RequirementCRegistry)
        {
            RequirementCRegistry tuple = (RequirementCRegistry) element;
            if (columnIndex == 0)
            {
                return tuple.getCurrent().getIdentifier();
            }
            if (columnIndex == 1)
            {
                if (tuple.getUpstream() != null)
                {
                    return tuple.getUpstream().getIdent();
                }
            }
        }
        return Messages.getString("RequirementPropertySection.2"); //$NON-NLS-1$
    }
}
