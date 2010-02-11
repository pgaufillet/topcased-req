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
import org.eclipse.swt.widgets.Button;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.DefaultAttributeValue;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.Messages;
import org.topcased.tabbedproperties.sections.AbstractBooleanPropertySection;

/**
 * The section for the default feature of a <b>DefaultAttributeValue</b> model object.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class DefaultAttributeValuePropertySection extends AbstractBooleanPropertySection
{

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getFeature()
     */
    @Override
    protected EStructuralFeature getFeature()
    {
        return RequirementPackage.eINSTANCE.getAttributeValue_Value();
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractBooleanPropertySection#getFeatureValue()
     */
    @Override
    protected boolean getFeatureValue()
    {
        AttributeValue attributeValue = (AttributeValue) getEObject();
        ConfiguratedAttribute configurated = (ConfiguratedAttribute) attributeValue.eContainer();
        DefaultAttributeValue defaultValue = configurated.getDefaultValue();
        if (defaultValue.getValue() != null)
        {
            return defaultValue.getValue().getValue().equals(attributeValue.getValue());
        }
        return false;
    }
    
    /**
     * @see org.topcased.tabbedproperties.sections.AbstractBooleanPropertySection#getCheckButton()
     */
    @Override
    protected Button getCheckButton()
    {
        Button checkBtn = super.getCheckButton();
        checkBtn.setEnabled(false);
        return checkBtn;
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractBooleanPropertySection#getLabelText()
     */
    protected String getLabelText()
    {
        return Messages.getString("DefaultAttributeValuePropertySection.0"); //$NON-NLS-1$
    }

}