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
 * This interface defines the behavior to implement for the "requirementIdentifierDefinition" extension point The
 * methods getIndex, increaseIndex and resetIndex must be implemented instead of pattern methods .
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public interface IRequirementIdentifierDefinition
{

    /**
     * This is here that you can map your added patterns to a pattern walue
     * 
     * @param the map of patterns already constructed
     * @param editingDomain the editing domain
     * @return the map with added elements
     */
    public Map<String, String> addValuesToPatterns(EditingDomain editingDomain, Map<String, String> alreadyCreatedMap);

    /**
     * The default patterns are : project name, number, upstream identifier and hierarchical element parent
     * 
     * @return the Pattern list you want to add
     */
    public List<String> addPatterns();
}
