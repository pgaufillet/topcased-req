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
package org.topcased.requirement.core.extensions;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.topcased.modeler.diagrams.model.Diagrams;
import org.topcased.requirement.RequirementProject;

/**
 * This interface defines the behavior to implement for the "modelAttachmentPolicy" extension point Provide the way to
 * link and unlink your target model to a requirement model and to get your requirement model
 * from your target model
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public interface IModelAttachmentPolicy
{
    /**
     * Sets a link between a requirement model and a target model
     * 
     * @param targetModel the target model
     * @param requirementModel the requirement model
     * @return Command, the emf command that will be executed in order to link the requirement model
     */
    Command linkRequirementModel(Resource targetModel, Resource requirementModel);

    /**
     * Removes the link between a requirement model and a target model
     * 
     * @param targetModel the target model
     * @param requirementModel the requirement model
     * @return Command, the emf command that will be executed in order to unlink the requirement model
     */
    Command unlinkRequirementModel(Resource targetModel, Resource requirementModel, boolean deleteRequirementModel);

    /**
     * Gets the target model linked with the requirement model from the resource set of your editing domain
     * 
     * @param resourceSet the resource set of your editing domain
     * @return the linked target model or null if no requirement model is attached.
     */
    Resource getLinkedTargetModel(ResourceSet resourceSet);
    

    /**
     * Gets the Requirement project from the target diagram (usually a "di" diagram)
     * 
     * @param diagram the diagram attached to a requirement model
     * @return the requirement project
     */
    RequirementProject getRequirementProjectFromTargetDiagram(Diagrams diagram);
    
    
    /**
     * Check if the current parent element is the root of the requirement model
     * 
     * @param parentElt the current parent element
     * @return true if the current parent element is the root else false
     */
    boolean isRootContainer(EObject parentElt);

}