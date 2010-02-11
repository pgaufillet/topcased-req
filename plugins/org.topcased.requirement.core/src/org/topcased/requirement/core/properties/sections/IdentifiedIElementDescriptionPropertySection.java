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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.SWT;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.Messages;
import org.topcased.tabbedproperties.sections.AbstractStringPropertySection;

/**
 * The section is for the description of a <b>IdentifiedElement</b> model object.
 * 
 * Creation 16 dec. 2008
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class IdentifiedIElementDescriptionPropertySection extends AbstractStringPropertySection
{
    /**
     * @see org.topcased.modeler.editor.properties.sections.AbstractTextPropertySection#getLabelText()
     */
    protected String getLabelText()
    {
        return Messages.getString("IdentifiedIElementDescriptionPropertySection.0"); //$NON-NLS-1$
    }

    /**
     * @see org.topcased.modeler.editor.properties.sections.AbstractTextPropertySection#getFeature()
     */
    protected EAttribute getFeature()
    {
        return RequirementPackage.eINSTANCE.getIdentifiedElement_ShortDescription();
    }

    /**
     * @see org.topcased.modeler.editor.properties.sections.AbstractTextPropertySection#getStyle()
     */
    protected int getStyle()
    {
        return SWT.MULTI | SWT.WRAP;
    }

    /**
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#shouldUseExtraSpace()
     */
    public boolean shouldUseExtraSpace()
    {
        return true;
    }
}