/*******************************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE.
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Vincent HEMERY (ATOS ORIGIN INTEGRATION) vincent.hemery@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.core.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.views.current.CurrentPage;
import org.topcased.requirement.core.views.current.CurrentRequirementView;

/**
 * This handler is designed for expand/collapse inside the {@link CurrentRequirementView}.
 * 
 * @author vhemery
 */
public abstract class AbstractExpandCollapseHandler extends AbstractHandler
{
    /**
     * Expand/collapse current requirement page's selection, as defined by the subclass
     * 
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        CurrentPage currentPage = RequirementHelper.INSTANCE.getCurrentPage();
        Viewer viewer = currentPage.getViewer();
        if (viewer != null && viewer instanceof TreeViewer)
        {
            TreeViewer treeViewer = (TreeViewer) viewer;
            expandOrCollapse(treeViewer);
        }
        return null;
    }

    /**
     * Perform the expand/collapse on current requirement page's selection
     * 
     * @param treeViewer the tree viewer to expand/collapse
     */
    protected abstract void expandOrCollapse(TreeViewer treeViewer);

}
