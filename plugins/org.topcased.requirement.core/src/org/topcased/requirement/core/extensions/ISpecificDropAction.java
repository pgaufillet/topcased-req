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

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;


/**
 * This interface defines the behavior to implement for the "specificDropAction" extension point
 * This method get in parameter the source objects and the target object of a requirement drag and drop
 * This method return a command who will be executed in the drop listener {@link RequirementDropListener}
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public interface ISpecificDropAction
{
    
    /**
     * implement this method to add new action before command execution when a drag'n'drop occur
     * 
     * @param source the source elements of the dnd
     * @param target the target element of the dnd
     * @return the command you want to execute when a drag'n'drop occur
     */
    public Command createSpecificDropAction(Collection< ? > source, EObject target);
}
