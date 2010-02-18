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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.core.preferences.CurrentPreferenceHelper;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * The label provider of the requirement tree
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * 
 */
public class CurrentRequirementLabelProvider extends AdapterFactoryLabelProvider
{

    /**
     * Constructor
     */
    public CurrentRequirementLabelProvider()
    {
        super(RequirementUtils.getAdapterFactory());
    }
    
    /**
     * Constructor
     * 
     * @param adapterFactory The default adapter factory
     */
    public CurrentRequirementLabelProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getImage(java.lang.Object)
     */
    public Image getImage(Object element)
    {
        if (element instanceof HierarchicalElement)
        {
            EObject eObject = ((HierarchicalElement) element).getElement();
            if (eObject != null)
            {
                return super.getImage(eObject);
            }
        }
        return super.getImage(element);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getText(java.lang.Object)
     */
    public String getText(Object element)
    {
        if (element instanceof HierarchicalElement)
        {
            EObject eObject = ((HierarchicalElement) element).getElement();
            if (eObject != null)
            {
                return super.getText(eObject);
            }
        }
        else if (element instanceof AttributeLink)
        {
            String label = super.getText(element).concat(" ").concat(super.getText(((AttributeLink) element).getValue())); //$NON-NLS-1$
            if (((AttributeLink) element).getPartial())
            {
                label = label.concat("#Partial");  //$NON-NLS-1$
            }
            return label;
        }
        else if (element instanceof ObjectAttribute)
        {
            return super.getText(element).concat(" ").concat(super.getText(((ObjectAttribute) element).getValue())); //$NON-NLS-1$
        }
        else if (element instanceof AttributeValue)
        {
            ConfiguratedAttribute confAttr = (ConfiguratedAttribute) ((AttributeValue) element).eContainer();
            if (confAttr.getDefaultValue() != null && confAttr.getDefaultValue().getValue() != null && confAttr.getDefaultValue().getValue().equals((AttributeValue) element))
            {
                return ((AttributeValue) element).getValue().concat(" ").concat(CurrentPreferenceHelper.STRING_DEFAULT_VALUE);
            }
            else
            {
                return ((AttributeValue) element).getValue();
            }
        }
        return super.getText(element);
    }

}