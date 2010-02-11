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
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.Messages;
import org.topcased.tabbedproperties.sections.AbstractBooleanPropertySection;

/**
 * The section for the partial feature of a <b>AttributeLink</b> model object.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class AttributeLinkPartialPropertySection extends AbstractBooleanPropertySection
{

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getFeature()
     */
    @Override
    protected EStructuralFeature getFeature()
    {
        return RequirementPackage.eINSTANCE.getAttributeLink_Partial();
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractBooleanPropertySection#getFeatureValue()
     */
    @Override
    protected boolean getFeatureValue()
    {
        AttributeLink attributeLink = (AttributeLink) getEObject();
        return attributeLink.getPartial();
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractBooleanPropertySection#getLabelText()
     */
    protected String getLabelText()
    {
        return Messages.getString("AttributeLinkPartialPropertySection.0"); //$NON-NLS-1$
    }

}