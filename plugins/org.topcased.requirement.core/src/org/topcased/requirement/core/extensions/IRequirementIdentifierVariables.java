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

import java.util.List;
import java.util.Map;

import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * This interface defines the behavior to implement for the "requirementIdentifierVariables" extension point. The added
 * variables can be seen in the table of the requirement naming preference page.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public interface IRequirementIdentifierVariables
{

    /**
     * This is here that you can map your added variables to a variable value
     * 
     * @param the map of variables already constructed
     * @param editingDomain the editing domain
     * @return the map plus the new variables and there values
     */
    public Map<String, String> setValuesToVariables(EditingDomain editingDomain, Map<String, String> alreadyCreatedMap);

    /**
     * You can see the default variables in the {@link DefaultRequirementIdentifierVariables} method
     * 
     * @return the Variable list you want to add
     */
    public List<String> getVariables();
}
