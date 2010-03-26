/*****************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.providers;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.utils.RequirementUtils;

import ttm.Requirement;

/**
 * This customized label provider manages the font's style to applied to the requirement contained in the Upstream Page.<br>
 * 
 * Updated : 11 march 2010<br>
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class UpstreamRequirementLabelProvider extends AdapterFactoryLabelProvider implements IFontProvider
{

    /** Font for covered upstream requirements */
    private Font boldItalicFont;

    /** Font for not covered upstream requirements */
    private Font defautlFont;

    /**
     * Constructor
     * 
     * @param adapterFactory The adapter factory to use into this provider.
     */
    public UpstreamRequirementLabelProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);

        defautlFont = Utils.getFont(Display.getCurrent().getSystemFont().getFontData()[0], SWT.NONE);
        boldItalicFont = Utils.getFont(Display.getCurrent().getSystemFont().getFontData()[0], SWT.BOLD | SWT.ITALIC);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getFont(java.lang.Object)
     */
    public Font getFont(Object element)
    {
        if (element instanceof Requirement)
        {
            if (RequirementUtils.isLinked((Requirement) element))
            {
                return boldItalicFont;
            }
        }
        return defautlFont;
    }
}
