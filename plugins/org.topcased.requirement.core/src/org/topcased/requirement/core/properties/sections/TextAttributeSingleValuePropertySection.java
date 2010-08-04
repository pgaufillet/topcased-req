/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.properties.sections;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.tabbedproperties.sections.AbstractStringPropertySection;

/**
 * The section for the value feature of a <b>TextAttribute</b> model object.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class TextAttributeSingleValuePropertySection extends AbstractStringPropertySection
{

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractBooleanPropertySection#getLabelText()
     */
    protected String getLabelText()
    {
        return Messages.getString("TextAttributeSingleValuePropertySection.0"); //$NON-NLS-1$
    }
    
    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getFeature()
     */
    protected EStructuralFeature getFeature()
    {
        return RequirementPackage.eINSTANCE.getTextAttribute_Value();
    }
    
    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTextPropertySection#getStyle()
     */
    protected int getStyle()
    {
        return SWT.MULTI | SWT.WRAP | SWT.V_SCROLL;
    }
    
    /**
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#shouldUseExtraSpace()
     */
    public boolean shouldUseExtraSpace()
    {
        return true;
    }
}