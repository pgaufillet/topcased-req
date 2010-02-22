/***********************************************************************************************************************
 * Copyright (c) 2008,2009 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.providers;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.facilities.dialogs.ChooseDialog;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.utils.RequirementUtils;

import ttm.Requirement;

/**
 * This Label Provider extends the default {@link CurrentRequirementLabelProvider} and allows to display full path of a
 * model element regarding its position in the model. Designed for working with the Topcased {@link ChooseDialog}.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 * @see {@link CurrentRequirementLabelProvider}
 * @see {@link ChooseDialog}
 */
public class AdvancedRequirementLabelProvider extends CurrentRequirementLabelProvider
{
    /**
     * Constructor
     * 
     * @param adapterFactory The adapter factory
     */
    public AdvancedRequirementLabelProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
    }

    /**
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object object)
    {
        EditingDomain domain = TopcasedAdapterFactoryEditingDomain.getEditingDomainFor(object);
        if (object instanceof Requirement)
        {
            EObject requirement = (Requirement) object;
            RequirementProject project = RequirementUtils.getRequirementProject(domain);
            return getTreePathText(project.getUpstreamModel().getDocuments(), requirement);
        }
        else if (object instanceof org.topcased.requirement.Requirement)
        {
            EObject requirement = (org.topcased.requirement.Requirement) object;
            RequirementProject project = RequirementUtils.getRequirementProject(domain);
            return getTreePathText(project.getHierarchicalElement(), requirement);
        }
        return "";
    }

    /**
     * Starting from the ending point to the starting point, this method builds the tree path to access this object in
     * the model.
     * 
     * @param startingPoint The starting point
     * @param endingPoint The ending point
     * @return The full tree path related to the position in the model.
     */
    private String getTreePathText(EList< ? > startingPoint, EObject endingPoint)
    {
        StringBuffer sb = new StringBuffer();
        sb = sb.append(super.getText(endingPoint));
        while (endingPoint.eContainer() != null && !startingPoint.contains(endingPoint))
        {
            endingPoint = endingPoint.eContainer();
            sb = sb.insert(0, super.getText(endingPoint) + "::");
        }
        return sb.toString();
    }
}
