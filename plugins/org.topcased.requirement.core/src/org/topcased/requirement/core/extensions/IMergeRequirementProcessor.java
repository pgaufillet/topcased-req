/*****************************************************************************
 * Copyright (c) 2010 Rockwell Collins.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Vincent Hemery (Atos Origin) - Initial API and implementation
 *    
 ******************************************************************************/
package org.topcased.requirement.core.extensions;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;

/**
 * This interface must be used to define new processes on requirement merge.
 * 
 * @author vhemery
 */
public interface IMergeRequirementProcessor
{
    /**
     * Handle a special treatment for move differences
     * 
     * @param difference the move difference
     */
    public void processMoved(DiffElement difference);

    /**
     * Handle a special treatment for add differences
     * 
     * @param difference the add difference
     */
    public void processAdded(DiffElement difference);

    /**
     * Handle a special treatment for modify differences
     * 
     * @param difference the modify difference
     */
    public void processModified(DiffElement difference);

    /**
     * Handle a special treatment for delete differences
     * 
     * @param difference the delete difference
     */
    public void processDeleted(DiffElement difference);
}
