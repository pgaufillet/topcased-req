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
package org.topcased.requirement.core.listeners;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.modeler.DuplicationAdapter;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Adapter used to create hierarchical elements hierarchy when action 'Duplicate subtree' is used from the target
 * modeler.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class RequirementDuplicationAdapter implements DuplicationAdapter
{

    /**
     * @see org.topcased.modeler.DuplicationAdapter#getAdditionalObjects(org.eclipse.emf.ecore.EObject)
     */
    public Collection<EObject> getAdditionalObjects(EObject original)
    {
        return Collections.emptyList();
    }

    /**
     * @see org.topcased.modeler.DuplicationAdapter#getPostProcessingCommand(org.eclipse.emf.edit.domain.EditingDomain,
     *      org.eclipse.emf.ecore.EObject, java.util.Map, java.util.Map)
     */
    public Command getPostProcessingCommand(EditingDomain editingDomain, EObject original, Map<EObject, EObject> mainMapping, Map<EObject, EObject> additionalMapping)
    {
        CompoundCommand cc = new CompoundCommand("Duplicate Hierarchical Elements");
        HierarchicalElement hierarchicalElt = RequirementUtils.getHierarchicalElementFor(original);
        if (hierarchicalElt != null)
        {
            HierarchicalElement copiedObject = (HierarchicalElement) EcoreUtil.copy(hierarchicalElt);
            copiedObject.setElement(mainMapping.get(original));
            updateReferences(copiedObject, mainMapping);
            cc.appendIfCanExecute(AddCommand.create(editingDomain, hierarchicalElt.eContainer(), RequirementPackage.eINSTANCE.getHierarchicalElement_Children(), copiedObject));
        }
        return cc;
    }

    /**
     * Updates References contained in the hierarchical elements. Replaces the former to set the new ones contained in
     * the mapping map.
     * 
     * @param elt An element representing a hierarchical element.
     * @param mainMapping The mapping
     */
    private void updateReferences(HierarchicalElement elt, Map<EObject, EObject> mainMapping)
    {
        for (HierarchicalElement child : elt.getChildren())
        {
            updateReferences(child, mainMapping);
            EObject newReference = mainMapping.get(child.getElement());
            child.setElement(newReference);
        }
    }

}
