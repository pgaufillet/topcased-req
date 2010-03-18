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
import org.topcased.requirement.HierarchicalElement;


/**
 * This interface defines the behavior to implement for the "requirementIdentifierDefinition" extension point
 * The methods getIndex, increaseIndex and resetIndex must be implemented instead of pattern methods .
 *  
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public interface IRequirementIdentifierDefinition
{
    
    /**
     * Get the current index of your counting algorithm
     * For the Default counting algorithm, there is no need for a hierarchical element parameter as the index is stored somewhere else
     * 
     * @param requirementContainer if you choose to store the index in the hierarchical element
     * @return current index of your counting algorithm
     */
    public long getCurrentIndex(HierarchicalElement requirementContainer);
    
    /**
     * Implement here the increasing of your index on current requirement creation
     * 
     * @param requirementContainer if you choose to store the index in the hierarchical element
     * @param index the current index
     * @return the increased/next index 
     */
    public long increaseIndexWhenCreateRequirement(HierarchicalElement requirementContainer, long index);
   
    /**
     * Implement here the decreasing of your index on current requirement deletion
     * 
     * @param theDeletedRequirement deleted requirement
     * @param index the current index 
     */
//    public void decreaseIndexWhenDeleteRequirement(Requirement theDeletedRequirement);
   
    /**
     * If you choose to reset index when a new hierarchical element is created
     * 
     * @param newHierarchicalElement if you choose to store the index in the hierarchical element
     * @param index the current index
     * @return the new index
     */
    public long resetIndexWhenCreateNewContainer(HierarchicalElement newHierarchicalElement, long index);
    
    
    
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
