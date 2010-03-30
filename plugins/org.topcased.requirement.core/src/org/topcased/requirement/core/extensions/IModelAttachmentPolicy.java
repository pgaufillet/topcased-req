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

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * This interface defines the behavior to implement for the "modelAttachmentPolicy" extension point Provide the way to
 * link and unlink your target model to a requirement model
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
     */
    void linkRequirementModel(Resource targetModel, Resource requirementModel);

    /**
     * Removes the link between a requirement model and a target model
     * 
     * @param targetModel the target model
     * @param requirementModel the requirement model
     */
    void unlinkRequirementModel(Resource targetModel, Resource requirementModel);

    /**
     * Gets the target model linked with the requirement model from the resource set of your editing domain
     * 
     * @param resourceSet the resource set of your editing domain
     * @return the linked target model or null if no requirement model is attached.
     */
    Resource getLinkedTargetModel(ResourceSet resourceSet);

}