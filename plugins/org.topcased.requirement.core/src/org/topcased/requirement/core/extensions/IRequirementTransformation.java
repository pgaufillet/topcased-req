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

import org.eclipse.core.resources.IFile;

/**
 *  This interface have to be implemented by the requirementTransformation extension point
 *  This must provide the way to transform a file source to a file dest. 
 *  By default (requirement way), the dest file must have the ".requirement" extension
 *  
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public interface IRequirementTransformation
{

    
    /**
     * Implement this method to define the transformation you want to use 
     * when the requirement project wizard is opened
     * 
     * @param source the source file to transform
     * @param dest the destination requirement file
     * @return not necessarily boolean
     */
    public void transformation(IFile source, IFile dest);
    
}
