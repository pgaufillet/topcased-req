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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.views.current.model.CurrentRequirementReference;
import org.topcased.requirement.core.views.current.model.CurrentRequirementReferenceContainer;

/**
 * Default provider for the current requirement view.<br>
 * Manages the order between requirements and hierarchical elements presented into the view.<br>
 * Actually, it's better to present first the requirements then the eventual hierarchical elements.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class CurrentRequirementContentProvider extends AdapterFactoryContentProvider
{
    /**
     * Simulate the containment link between a {@link RequirementProject} and
     * {@link CurrentRequirementReferenceContainer}
     */
    private HashMap<RequirementProject, CurrentRequirementReferenceContainer> currentRequirementReferenRegistry;

    /**
     * Constructor
     * 
     * @param adapterFactory the default adapter factory
     */
    public CurrentRequirementContentProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getChildren(java.lang.Object)
     */
    @Override
    public Object[] getChildren(Object object)
    {
        if (object instanceof HierarchicalElement)
        {
            EList<Requirement> requirements = ((HierarchicalElement) object).getRequirement();
            EList<HierarchicalElement> children = ((HierarchicalElement) object).getChildren();
            List<Object> toReturn = new ArrayList<Object>();
            toReturn.addAll(requirements);
            toReturn.addAll(children);
            return toReturn.toArray();
        }
        else if (object instanceof CurrentRequirementReferenceContainer)
        {
            return ((CurrentRequirementReferenceContainer) object).getChildren(object);
        }
        return super.getChildren(object);
    }

    @Override
    public Object[] getElements(Object object)
    {
        /**
         * Virtually add the References element which will contained references current requirement
         */
        if (object instanceof RequirementProject)
        {
            ArrayList<Object> result = new ArrayList<Object>(Arrays.asList(super.getElements(object)));
            RequirementProject reqProject = (RequirementProject) object;
            CurrentRequirementReferenceContainer container = currentRequirementReferenRegistry.get(reqProject);
            if (container == null)
            {
                container = new CurrentRequirementReferenceContainer(reqProject, this);
                currentRequirementReferenRegistry.put(reqProject, container);
            }
            if (!container.getReferences().isEmpty())
            {
                result.add(container);
            }
            return result.toArray();
        }
        else if (object instanceof CurrentRequirementReferenceContainer)
        {
            return ((CurrentRequirementReferenceContainer) object).getElements(object);
        }
        return super.getElements(object);
    }

    @Override
    public boolean hasChildren(Object object)
    {
        if (object instanceof CurrentRequirementReferenceContainer)
        {
            return ((CurrentRequirementReferenceContainer) object).hasChildren(object);
        }
        else if (object instanceof CurrentRequirementReference)
        {
            CurrentRequirementReference ref = (CurrentRequirementReference) object;
            return ref.getParentReference().hasChildren(ref);
        }
        return super.hasChildren(object);
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
        currentRequirementReferenRegistry = new HashMap<RequirementProject, CurrentRequirementReferenceContainer>();
        super.inputChanged(viewer, oldInput, newInput);
    }

    @Override
    public void dispose()
    {
        currentRequirementReferenRegistry.clear();
        super.dispose();
    }

}
