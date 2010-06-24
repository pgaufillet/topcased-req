/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/
package org.topcased.requirement.core.providers;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.core.internal.Messages;

import ttm.Requirement;
import ttm.TtmFactory;

/**
 * Content provider for Tree content to display in the requirement property section
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class RequirementPropertyTreeContentProvider extends AdapterFactoryContentProvider
{

    /**
     *  The list of current requirements associated with the selected diagram element
     */
    List<org.topcased.requirement.Requirement> listCurrent;
    
    /**
     * @param adapterFactory
     */
    public RequirementPropertyTreeContentProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
    }
    
    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getElements(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object[] getElements(Object object)
    {
        listCurrent = (List<org.topcased.requirement.Requirement>) object;
        Collection<Requirement> result = new LinkedHashSet<Requirement>();
        Boolean atLeastOneUntraced = false;
        
        for (org.topcased.requirement.Requirement requirement : listCurrent)
        {
            // Selects only the current requirements not the anonymous requirements
            if (requirement instanceof CurrentRequirement)
            {
                for (Attribute attribute : requirement.getAttribute())
                {
                    if (attribute instanceof AttributeLink)
                    {
                        if (((AttributeLink) attribute).getValue() instanceof Requirement)
                        {
                            Requirement upstream = (Requirement) ((AttributeLink) attribute).getValue();
                            result.add(upstream);                            
                        }
                        else
                        {
                            atLeastOneUntraced = true;
                        }
                    }
                }
            }
        }
        // If there is some untraced current requirement, create a container for those untraced requirements
        if (atLeastOneUntraced)
        {
            Requirement req = TtmFactory.eINSTANCE.createRequirement();
            req.setIdent(Messages.getString("RequirementPropertySection.2")); //$NON-NLS-1$
            ttm.Attribute att = TtmFactory.eINSTANCE.createAttribute();
            att.setParent(req);
            result.add(req);
        }
        return result.toArray();
    }
    
    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getChildren(java.lang.Object)
     */
    @Override
    public Object[] getChildren(Object object)
    {
        if (object instanceof Requirement)
        {
            Collection<org.topcased.requirement.Requirement> result = new LinkedHashSet<org.topcased.requirement.Requirement>();
            if (!listCurrent.isEmpty())
            {
                for (org.topcased.requirement.Requirement requirement : listCurrent)
                {
                    // Selects only the current requirements not the anonymous requirements
                    if (requirement instanceof CurrentRequirement)
                    {
                        for (Attribute attribute : requirement.getAttribute())
                        {
                            if (attribute instanceof AttributeLink )
                            {
                                if (((AttributeLink) attribute).getValue() instanceof Requirement)
                                {
                                    Requirement upstream = (Requirement) ((AttributeLink) attribute).getValue();
                                    
                                    if (upstream.equals((Requirement)object))
                                    {
                                        result.add(requirement); 
                                    }    
                                }
                                else
                                {   //add the untraced requirements to the fake upstream requirement container
                                    if (((Requirement)object).getIdent() == Messages.getString("RequirementPropertySection.2")) //$NON-NLS-1$
                                    {
                                        result.add(requirement);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return result.toArray();
        }
        else
        {
            return new Object[0];
        }
    }
}
