/*****************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.common.utils.JFaceUtils;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.preferences.CurrentPreferenceHelper;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * The label provider of the requirement tree
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * 
 */
public class CurrentRequirementLabelProvider extends AdapterFactoryLabelProvider implements IFontProvider, IColorProvider
{
    /** Font for impacted current requirements */
    private Font italicFont;

    /** Default Font for current requirements */
    private Font defaultFont;

    /** Red color for italic font */
    private Color italicColor;

    /** Default color */
    private Color defaultColor;

    /**
     * Constructor
     */
    public CurrentRequirementLabelProvider()
    {
        this(RequirementUtils.getAdapterFactory());
    }

    /**
     * Constructor
     * 
     * @param adapterFactory The default adapter factory
     */
    public CurrentRequirementLabelProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);

        defaultFont = JFaceUtils.getFont(Display.getCurrent().getSystemFont().getFontData()[0], SWT.NONE);
        italicFont = JFaceUtils.getFont(Display.getCurrent().getSystemFont().getFontData()[0], SWT.ITALIC);
        setDefaultFont(defaultFont);
        defaultColor = JFaceUtils.getColor("0,0,0"); //$NON-NLS-1$
        italicColor = JFaceUtils.getColor("255,0,0"); //$NON-NLS-1$
        setDefaultForeground(defaultColor);
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
                if(eObject.eIsProxy())
                {
                    //Displaying the image used for HierarchicalElements. 
                    return super.getImage(element);
                }
                else
                {
                    return super.getImage(eObject);
                }
            }
        }
        else if (element instanceof EObject){
            //tests if the requirement refers to trash requirements.
            EObject eObject = (EObject) element;
            if(RequirementUtils.refersToTrash(eObject)){
                Image original = super.getImage(eObject);
                
                List<Object> images = new ArrayList<Object>(2);
                images.add(original);
                images.add(RequirementCorePlugin.getDefault().getImage("icons/trashOverlay.gif"));
                Object imageWithOverlay = new ComposedImage(images);
                //Creating the message from the ComposedImage object, and returning it.
                return this.getImageFromObject(imageWithOverlay);
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
                if (eObject.eIsProxy())
                {
                    //Returning the URI of the associated resource.
                    URI eObjectUri = EcoreUtil.getURI(eObject);
                    String resourcePath = eObjectUri.path();
                    String label = "Element ("+resourcePath+")";
                    return label;
                }
                else
                {
                    return super.getText(eObject);
                }
            }
        }
        else if (element instanceof AttributeLink)
        {
            String label = super.getText(element).concat(" ").concat(super.getText(((AttributeLink) element).getValue())); //$NON-NLS-1$
            if (((AttributeLink) element).getPartial())
            {
                label = label.concat("#Partial"); //$NON-NLS-1$
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
                return ((AttributeValue) element).getValue().concat(" ").concat(CurrentPreferenceHelper.STRING_DEFAULT_VALUE); //$NON-NLS-1$
            }
            else
            {
                return ((AttributeValue) element).getValue();
            }
        }
        return super.getText(element);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getFont(java.lang.Object)
     */
    @Override
    public Font getFont(Object object)
    {
        if (object instanceof AttributeLink)
        {
            AttributeLink link = (AttributeLink) object;
            return getFont(link.eContainer());
        }
        if (object instanceof CurrentRequirement)
        {
            CurrentRequirement req = (CurrentRequirement) object;
            return req.isImpacted() ? italicFont : defaultFont;
        }
        return super.getFont(object);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getForeground(java.lang.Object)
     */
    @Override
    public Color getForeground(Object object)
    {
        if (object instanceof AttributeLink)
        {
            AttributeLink link = (AttributeLink) object;
            return getForeground(link.eContainer());
        }
        if (object instanceof CurrentRequirement)
        {
            CurrentRequirement req = (CurrentRequirement) object;
            return req.isImpacted() ? italicColor : defaultColor;
        }
        return super.getForeground(object);
    }

}
