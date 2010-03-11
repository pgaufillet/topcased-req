/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
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
import org.topcased.requirement.core.utils.RequirementUtils;
import ttm.Requirement;

/**
 * This customized label provider manages the font's style to applied to the requirement contained in the Upstream Page.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class UpstreamRequirementLabelProvider extends AdapterFactoryLabelProvider implements IFontProvider
{

    private Font boldItalicFont = null;

    private Font initialFont = null;

    /**
     * Constructor
     * 
     * @param adapterFactory The adapter factory to use
     */
    public UpstreamRequirementLabelProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
        initialFont = getStyledFont(Display.getCurrent().getSystemFont(), SWT.NONE);
        boldItalicFont = getStyledFont(Display.getCurrent().getSystemFont(), SWT.BOLD | SWT.ITALIC);
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

    /**
     * Get a styled font
     * 
     * @param originalFont
     * @param style
     * 
     * @return the font
     */
    private Font getStyledFont(Font originalFont, int style)
    {
        FontData[] fontData = originalFont.getFontData();
        for (int i = 0; i < fontData.length; i++)
        {
            fontData[i].setStyle(fontData[i].getStyle() | style);
        }
        return new Font(Display.getDefault(), fontData);
    }

    /**
     * @see org.eclipse.jface.viewers.BaseLabelProvider#dispose()
     */
    public void dispose()
    {
        super.dispose();
        if (boldItalicFont != null)
        {
            boldItalicFont.dispose();
        }
        if (initialFont != null)
        {
            initialFont.dispose();
        }
    }

}
