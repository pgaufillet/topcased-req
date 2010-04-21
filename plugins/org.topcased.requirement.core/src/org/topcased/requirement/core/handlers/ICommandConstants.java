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
package org.topcased.requirement.core.handlers;

/**
 * Command ids constants
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public interface ICommandConstants
{

    static final String LINK_WITH_EDITOR_ID = "org.topcased.requirement.core.linkWithEditor"; //$NON-NLS-1$

    static final String SORT_ID = "org.topcased.requirement.core.sort"; //$NON-NLS-1$

    static final String FLAT_ID = "org.topcased.requirement.core.flat"; //$NON-NLS-1$

    static final String HIERARCHICAL_ID = "org.topcased.requirement.core.hierarchical";     //$NON-NLS-1$
    
    static final String LINK_TO_UPSTREAM_ID = "org.topcased.requirement.core.linkToUpstream";  //$NON-NLS-1$
    
    static final String FILTER_CURRENT_REQ_ID ="org.topcased.requirement.core.filterCurrentRequirements"; //$NON-NLS-1$
    
    static final String SET_MARKER_TYPE_PARAM = "org.topcased.requirement.core.setMarkerCommandParameter"; //$NON-NLS-1$

}
