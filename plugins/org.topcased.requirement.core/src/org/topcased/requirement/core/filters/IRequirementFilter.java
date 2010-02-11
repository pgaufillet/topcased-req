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
package org.topcased.requirement.core.filters;

/**
 * Defines an interface for filters acting on requirement management.<br>
 * Requirements are handled through a {@link TreeViewer} on which {@link ViewerFilter} can be defined.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.2.0
 * 
 */
public interface IRequirementFilter
{
    /**
     * Sets a searched text
     * 
     * @param request The text corresponding to the user request
     */
    void setSearched(String request);

}
