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
import org.eclipse.swt.widgets.Text;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.tabbedproperties.sections.AbstractStringPropertySection;
import org.topcased.tabbedproperties.sections.widgets.IText;

/**
 * The section for the identifier property of a <b>IdentifiedElement</b> model object.
 * 
 * Creation 16 dec. 2008 
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class AttributeNamePropertySection extends AbstractStringPropertySection
{
    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getLabelText()
     */
    protected String getLabelText()
    {
        return Messages.getString("AttributeNamePropertySection.0"); //$NON-NLS-1$
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTextPropertySection#getText()
     */
    @Override
    protected IText getText()
    {
        IText textWidget = super.getText();
        textWidget.setEditable(false);
        return textWidget;
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getFeature()
     */
    protected EStructuralFeature getFeature()
    {
        return RequirementPackage.eINSTANCE.getAttribute_Name();
    }

}