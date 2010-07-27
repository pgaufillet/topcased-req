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

import java.util.Iterator;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.topcased.requirement.core.views.current.CurrentRequirementView;

/**
 * This handler is designed for collapsing a selected branch inside the {@link CurrentRequirementView}.
 * 
 * @author vhemery
 */
public class CollapseHandler extends AbstractExpandCollapseHandler
{

    /**
     * Collapses all selected branches, or everything if no selection
     * 
     * @param treeViewer the tree viewer to collapse
     * @see AbstractExpandCollapseHandler#expandOrCollapse(TreeViewer)
     */
    protected void expandOrCollapse(TreeViewer treeViewer)
    {
        ISelection selection = treeViewer.getSelection();
        if (selection != null && selection.isEmpty())
        {
            treeViewer.collapseAll();
        }
        else if (selection != null && selection instanceof IStructuredSelection)
        {
            Iterator< ? > iterator = ((IStructuredSelection) selection).iterator();
            while (iterator.hasNext())
            {
                Object selectedElt = iterator.next();
                treeViewer.collapseToLevel(selectedElt, TreeViewer.ALL_LEVELS);
            }
        }
    }
}
