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
import org.eclipse.swt.graphics.FontData;
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
    private Font initialFont;

    /**
     * Constructor
     * 
     * @param adapterFactory The adapter factory to use into this provider.
     */
    public UpstreamRequirementLabelProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
        
        FontData[] initialFontData = getStyledFont(Display.getCurrent().getSystemFont(), SWT.NONE);
        FontData[] boldItalicFontData = getStyledFont(Display.getCurrent().getSystemFont(), SWT.BOLD | SWT.ITALIC);
        
        initialFont = Utils.getFont(initialFontData[0]); //$NON-NLS-1$
        boldItalicFont = Utils.getFont(boldItalicFontData[0]); //$NON-NLS-1$
    }

    /**
     * Get a styled font
     * 
     * @param originalFont
     * @param style
     * 
     * @return the font
     */
    private FontData[] getStyledFont(Font originalFont, int style)
    {
        FontData[] fontData = originalFont.getFontData();
        for (int i = 0; i < fontData.length; i++)
        {
            fontData[i].setStyle(fontData[i].getStyle() | style);
        }
        return fontData;
    }
    
    /**
     * @see org.eclipse.jface.viewers.IFontProvider#getFont(java.lang.Object)
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
        return initialFont;
    }
}
