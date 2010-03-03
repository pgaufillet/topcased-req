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

/**
 * This interface defines the behavior to implement for the "modelAttachementPolicy" extension point
 * Provide the way to link and unlink your metamodel to a requirement model
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public interface IModelAttachementPolicy
{
    public void linkRequirementModel(Resource model, Resource requirementModel);
    
    public void unlinkRequirementModel(Resource model);
    
    public void updateRequirementModel(Resource model);
}