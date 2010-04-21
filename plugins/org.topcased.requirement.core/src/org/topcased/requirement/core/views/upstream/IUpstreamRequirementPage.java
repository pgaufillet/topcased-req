/***********************************************************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe Mertz (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views.upstream;

import org.eclipse.ui.part.IPageBookViewPage;

/**
 * This interface is used to define a page where the requirement model can be filled.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public interface IUpstreamRequirementPage extends IPageBookViewPage
{
    Boolean DEFAULT_SHOW_TREE_HIERARCHY = true;

    Boolean DEFAULT_SHOW_FLAT_HIERARCHY = false;

    Boolean DEFAULT_SORT_UPSTREAM_ALPHABETICALLY = false;
    
    String SHOW_TREE_HIERARCHY_PREF = "show_tree_hierarchy_pref"; //$NON-NLS-1$
    
    String SHOW_FLAT_HIERARCHY_PREF = "show_flat_hierarchy_pref"; //$NON-NLS-1$
    
    String SORT_UPSTREAM_ALPHABETICALLY_PREF = "sort_upstream_alphabetically_pref"; //$NON-NLS-1$
    
}
