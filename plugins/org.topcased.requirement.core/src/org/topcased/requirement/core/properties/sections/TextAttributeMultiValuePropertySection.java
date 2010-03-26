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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.AttributesType;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.tabbedproperties.sections.AbstractEnumerationPropertySection;

/**
 * The section for the value feature of a <b>TextAttribute</b> model object.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class TextAttributeMultiValuePropertySection extends AbstractEnumerationPropertySection
{

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractBooleanPropertySection#getLabelText()
     */
    protected String getLabelText()
    {
        return Messages.getString("TextAttributeMultiValuePropertySection.0"); //$NON-NLS-1$
    }
    
    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getFeature()
     */
    protected EStructuralFeature getFeature()
    {
        return RequirementPackage.eINSTANCE.getTextAttribute_Value();
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractEnumerationPropertySection#getEnumerationFeatureValues()
     */
    @Override
    protected String[] getEnumerationFeatureValues()
    {
        List<String> toReturn = new ArrayList<String>();
        TextAttribute attribute = (TextAttribute) getEObject();
        AttributeConfiguration configuration = RequirementUtils.getAttributeConfiguration(getEditingDomain());
        for (ConfiguratedAttribute att : configuration.getListAttributes())
        {
            if (AttributesType.TEXT.equals(att.getType()) && attribute.getName().equals(att.getName()))
            {
                
                for(AttributeValue value : att.getListValue())
                {
                    toReturn.add(value.getValue());
                }
            }
        }
        return toReturn.toArray(new String[0]);
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractEnumerationPropertySection#getFeatureAsText()
     */
    @Override
    protected String getFeatureAsText()
    {
        TextAttribute attribute = (TextAttribute) getEObject();
        return attribute.getValue();
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractEnumerationPropertySection#getFeatureValue(int)
     */
    @Override
    protected Object getFeatureValue(int index)
    {
        String[] tab = getEnumerationFeatureValues();
        if (index < tab.length)
        {
            return tab[index];
        }
        return null;
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractEnumerationPropertySection#getOldFeatureValue()
     */
    @Override
    protected Object getOldFeatureValue()
    {
        TextAttribute attribute = (TextAttribute) getEObject();
        return attribute.getValue();
    }

}