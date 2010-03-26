/*******************************************************************************
 * Copyright (c) 2006 Anyware Technologies. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jacques Lescot (Anyware Technologies) - initial API and
 * implementation
 ******************************************************************************/
package org.topcased.requirement.core.properties.sections;

import java.util.Collection;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.providers.AdvancedRequirementLabelProvider;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.tabbedproperties.sections.AbstractChooserPropertySection;

/**
 * The section for the value feature of an <b>Object Attribute</b>
 * 
 * Creation 16 dec. 2008
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class ObjectAttributeValuePropertySection extends AbstractChooserPropertySection
{
    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getFeature()
     */
    protected EStructuralFeature getFeature()
    {
        return RequirementPackage.eINSTANCE.getObjectAttribute_Value();
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getLabelText()
     */
    protected String getLabelText()
    {
        return Messages.getString("ObjectAttributeValuePropertySection.0"); //$NON-NLS-1$
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractChooserPropertySection#getComboFeatureValues()
     */
    protected Object[] getComboFeatureValues()
    {
        Collection<Object> listEObjects = RequirementUtils.getChooseDialogContent(getEObject());
        return listEObjects.toArray();
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractChooserPropertySection#getLabelProvider()
     */
    protected ILabelProvider getLabelProvider()
    {
        return new AdapterFactoryLabelProvider(RequirementUtils.getAdapterFactory());
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractChooserPropertySection#getAdvancedLabeProvider()
     */
    protected ILabelProvider getAdvancedLabeProvider()
    {
        return new AdvancedRequirementLabelProvider(RequirementUtils.getAdapterFactory());
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractChooserPropertySection#getFeatureValue()
     */
    protected Object getFeatureValue()
    {
        return ((ObjectAttribute) getEObject()).getValue();
    }
}