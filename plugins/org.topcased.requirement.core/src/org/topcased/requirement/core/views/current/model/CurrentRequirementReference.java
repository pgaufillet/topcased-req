/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views.current.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.topcased.requirement.common.utils.JFaceUtils;
import org.topcased.requirement.core.RequirementCorePlugin;

/**
 * {@link CurrentRequirementReference} is an object which point to a Resource which is a current requirement model
 * 
 * @author "Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * @since 5.1
 */
public class CurrentRequirementReference implements IItemLabelProvider, IItemFontProvider, IItemColorProvider, IAdaptable
{
    /**
     * State of checking the URI
     */
    public enum URIState {
        OK, /** URI has been checked and is OK */
        KO, /** URI has been check and is wrong */
        UNKOWN
        /** URI has never been checked */
    }

    /** starting state of URI checking */
    private URIState uriState = URIState.UNKOWN;

    /**
     * Font for impacted current requirements
     */
    private static Font italicFont;

    /**
     * Default Font for current requirements
     */
    private static Font defaultFont;

    /**
     * Red color for italic font
     */
    private static Color italicColor;

    /**
     * Default color
     */
    private static Color defaultColor;

    /**
     * Error Font
     */
    private static Color errorColor;

    /**
     * Container
     */
    private CurrentRequirementReferenceContainer parentReference;

    /**
     * {@link URI} of the resource
     */
    private URI uri;

    /**
     * {@link Resource} referenced by this object. This can be null is initialized lazily WARNING: In order to use this
     * always use {@link CurrentRequirementReference#getResource()}
     */
    private Resource resource;

    /**
     * {@link ResourceSet} of the resource. (Alway available even if the resource is not loaded
     */
    private ResourceSet resourceSet;

    /**
     * List of all object which reference this object
     */
    private List<Object> refenredBy = new ArrayList<Object>();

    static
    {
        /**
         * Initialize fonts and color
         */
        defaultFont = JFaceUtils.getFont(Display.getCurrent().getSystemFont().getFontData()[0], SWT.NONE);
        italicFont = JFaceUtils.getFont(Display.getCurrent().getSystemFont().getFontData()[0], SWT.ITALIC);
        defaultColor = JFaceUtils.getColor("0,0,0"); //$NON-NLS-1$
        italicColor = JFaceUtils.getColor("206,206,206"); //$NON-NLS-1$
        errorColor = JFaceUtils.getColor("255,0,0"); //$NON-NLS-1$
    }

    /**
     * Constructor.
     * 
     * @param parentReference Container of this object
     * @param uri {@link URI} of the referenced resource
     * @param resourceSet {@link ResourceSet} of the referenced resource
     */
    public CurrentRequirementReference(CurrentRequirementReferenceContainer parentReference, URI uri, ResourceSet resourceSet)
    {
        super();
        this.parentReference = parentReference;
        this.resourceSet = resourceSet;
        this.uri = uri;

    }

    /**
     * Get the container
     * 
     * @return {@link CurrentRequirementReferenceContainer}
     */
    public CurrentRequirementReferenceContainer getParentReference()
    {
        return parentReference;
    }

    /**
     * Get the resource or try to resolve the {@link URI} if the resource is null
     * 
     * @return {@link Resource}
     */
    public Resource getResource()
    {
        if (resource == null && resourceSet != null)
        {
            resource = resourceSet.getResource(uri, false);
        }
        return resource;
    }

    /**
     * @return true if the resource is loaded
     */
    public boolean isResourceLoaded()
    {
        if (getResource() != null)
        {
            return getResource().isLoaded();
        }
        /** If get resource get back null this means that the resource is not loaded */
        return false;
    }

    /**
     * Get URI of the resource
     * 
     * @return {@link URI}
     */
    public URI getUri()
    {
        return uri;
    }

    /**
     * Override in order to be used in a Set
     */
    @Override
    public boolean equals(Object arg0)
    {
        if (arg0 instanceof CurrentRequirementReference)
        {
            CurrentRequirementReference cur = (CurrentRequirementReference) arg0;
            return cur.getUri().equals(uri);
        }
        return super.equals(arg0);
    }

    /**
     * Override in order to be used in a Set
     */
    @Override
    public int hashCode()
    {
        return uri.toString().hashCode();
    }

    /**
     * {@link IItemLabelProvider#getText(Object)}
     */
    public String getText(Object object)
    {
        StringBuilder result = new StringBuilder();
        if (object instanceof CurrentRequirementReference)
        {
            URI uri = ((CurrentRequirementReference) object).getUri();
            if (uri.isPlatform())
            {
                return result.append(getErrorMessage()).append(uri.toPlatformString(true)).toString();
            }
            return result.append(getErrorMessage()).append(uri.devicePath()).toString();
        }
        return Messages.CurrentRequirementReference_0;
    }

    private Object getErrorMessage()
    {
        switch (uriState)
        {
            case KO:
                return Messages.CurrentRequirementReference_Error;
            default:
                return Messages.CurrentRequirementReference_2;
        }
    }

    /**
     * {@link IItemLabelProvider#getImage(Object)}
     */
    public Object getImage(Object object)
    {
        if (object instanceof CurrentRequirementReference)
        {
            return RequirementCorePlugin.getDefault().getImageRegistry().get(RequirementCorePlugin.ICONS_CURRENT_REQUIREMENT_REFERENCE_GIF);
        }
        return null;
    }

    /**
     * {@link IItemColorProvider#getForeground(Object)}
     */
    public Color getForeground(Object element)
    {

        switch (uriState)
        {
            case KO:
                return errorColor;
            default:
                if (!isResourceLoaded())
                {
                    return italicColor;
                }
                return defaultColor;
        }
    }

    /**
     * {@link IItemColorProvider#getBackground(Object)}
     */
    public Color getBackground(Object element)
    {
        return null;
    }

    /**
     * {@link IItemFontProvider#getFont(Object)F}
     */
    public Font getFont(Object element)
    {
        if (!isResourceLoaded())
        {
            return italicFont;
        }
        return defaultFont;
    }

    /**
     * Implementation of {@link IAdaptable}
     */
    public Object getAdapter(Class adapter)
    {
        if (adapter == CurrentRequirementReference.class)
        {
            return this;
        }
        else if (adapter == EObject.class)
        {
            return parentReference.getAdapter(EObject.class);
        }
        else if (adapter == CurrentRequirementReferenceContainer.class)
        {
            return parentReference;
        }
        else if (adapter == INotifyChangedListener.class)
        {
            return parentReference.getNotifier();
        }
        return null;
    }

    /**
     * Get the URI State
     * 
     * @return {@link URIState}
     */
    public URIState getUriState()
    {
        return uriState;
    }

    /**
     * Set the {@link URIState}
     * 
     * @param uriState {@link URIState}
     */
    public void setUriState(URIState uriState)
    {
        this.uriState = uriState;
    }

    /**
     * Add a object to the "Reference By" list
     * 
     * @param object
     */
    public void addReferencedBy(Object object)
    {
        if (object != null)
        {
            refenredBy.add(object);
        }
    }

    /**
     * Get the elements which reference this element
     * 
     * @return
     */
    public List<Object> getRefenredBy()
    {
        return refenredBy;
    }

}
