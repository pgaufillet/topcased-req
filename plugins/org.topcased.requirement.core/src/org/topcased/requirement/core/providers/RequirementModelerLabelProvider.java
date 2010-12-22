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
package org.topcased.requirement.core.providers;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Label provider for Requirement toolkit. <br>
 * 
 * Creation 17 dec. 2008
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 * 
 */
public class RequirementModelerLabelProvider extends LabelProvider
{

    /**
     * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
     */
    public Image getImage(Object element)
    {
        Object theElement = element;
        if (theElement == null || theElement.equals(StructuredSelection.EMPTY))
        {
            return null;
        }
        if (theElement instanceof IStructuredSelection)
        {
            IStructuredSelection structuredSelection = (IStructuredSelection) theElement;
            if (areDifferentTypes(structuredSelection))
            {
                return null;
            }
            theElement = structuredSelection.getFirstElement();
        }
        theElement = AdapterFactoryEditingDomain.unwrap(theElement);
        if (theElement instanceof EObject || theElement instanceof Resource)
        {
            if (getAdapterFactoryLabelProvider() != null)
            {
                return getAdapterFactoryLabelProvider().getImage(theElement);
            }
        }
        return null;
    }

    /**
     * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
     */
    public String getText(Object element)
    {
        Object theElement = element;
        if (theElement == null || theElement.equals(StructuredSelection.EMPTY))
        {
            return null;
        }
        if (theElement instanceof IStructuredSelection)
        {
            IStructuredSelection structuredSelection = (IStructuredSelection) theElement;
            if (areDifferentTypes(structuredSelection))
            {
                return structuredSelection.size() + " items selected";//$NON-NLS-1$
            }
            theElement = structuredSelection.getFirstElement();
        }
        theElement = AdapterFactoryEditingDomain.unwrap(theElement);
        if (theElement instanceof EObject && getAdapterFactoryLabelProvider() != null)
        {
            return getAdapterFactoryLabelProvider().getText(theElement);
        }
        else if (theElement instanceof Resource)
        {
            return "\u00ABResource\u00BB";
        }

        return null;
    }

    /**
     * Return the AdapterFactoryLabelProvider
     * 
     * @return AdapterFactoryLabelProvider
     */
    protected AdapterFactoryLabelProvider getAdapterFactoryLabelProvider()
    {
        return new CurrentRequirementLabelProvider(RequirementUtils.getAdapterFactory());
    }

    /**
     * Determine there are objects in the structured selection of different types.
     * 
     * @param structuredSelection the structured selection.
     * @return true if there are objects of different types in the selection.
     */
    private boolean areDifferentTypes(IStructuredSelection structuredSelection)
    {
        if (structuredSelection.size() == 1)
        {
            return false;
        }
        Iterator< ? > i = structuredSelection.iterator();
        Object element = i.next();
        for (; i.hasNext();)
        {
            if (i.next().getClass() != element.getClass())
            {
                return true;
            }
        }

        return false;
    }
}
