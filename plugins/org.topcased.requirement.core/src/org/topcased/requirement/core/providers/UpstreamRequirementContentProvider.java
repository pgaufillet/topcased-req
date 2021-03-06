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

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.utils.impact.RequirementTimestampMonitor;

import ttm.Requirement;

/**
 * Provider for the upstream requirement view.<br>
 * Manages the <b>flat</b> mode and the <b>hierarchical</b> mode of the view
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class UpstreamRequirementContentProvider extends AdapterFactoryContentProvider
{
    private Boolean isFlat;

    protected EditingDomain editingDomain;

    /**
     * Constructor
     * 
     * @param adapterFactory The adapter factory to use for this provider
     * @param editingDomain
     */
    public UpstreamRequirementContentProvider(AdapterFactory adapterFactory, EditingDomain editingDomain)
    {
        super(adapterFactory);
        this.editingDomain = editingDomain;
        setIsFlat(false);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getChildren(java.lang.Object)
     */
    @Override
    public Object[] getChildren(Object object)
    {
        if (getIsFlat())
        {
            if (object instanceof Requirement)
            {
                return super.getChildren(object);
            }
            if (object instanceof EObject)
            {
                return RequirementUtils.getUpstreams((EObject) object).toArray();
            }
        }
        return super.getChildren(object);
    }

    /**
     * Getter for the boolean attribute <code>isFlat</code>
     * 
     * @return
     */
    public Boolean getIsFlat()
    {
        return isFlat;
    }

    /**
     * Setter for the boolean attribute <code>isFlat</code>
     * 
     * @param isFlat The attribute value to set
     */
    public void setIsFlat(Boolean isFlat)
    {
        this.isFlat = isFlat;
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
        //Upon opening upstream requirement view, check whether we need to perform impact analysis
        if (newInput != null && newInput instanceof UpstreamModel)
        {
            UpstreamModel model = (UpstreamModel) newInput;
            if (!model.eResource().getURI().equals(model.eContainer().eResource().getURI()))
            {
                try
                {
                    Command command = new RequirementTimestampMonitor().getOnLoadCommand(model);
                    if (command != null)
                    {
                        editingDomain.getCommandStack().execute(command);
                    }
                }
                catch (InterruptedException e)
                {
                    RequirementCorePlugin.log(e);
                }
            }
        }
        super.inputChanged(viewer, oldInput, newInput);
    }

}
