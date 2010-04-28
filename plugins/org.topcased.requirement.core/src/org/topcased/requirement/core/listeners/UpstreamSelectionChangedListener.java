/*****************************************************************************
 * Copyright (c) 2009 ATOS ORIGIN INTEGRATION.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) tristan.faure@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.core.listeners;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.core.filters.CurrentViewFilterFromUpstreamSelection;
import org.topcased.requirement.core.views.current.CurrentPage;
import org.topcased.requirement.core.views.current.CurrentRequirementView;

import ttm.Requirement;

/**
 * The listener interface for receiving upstreamSelectionChanged events. The class that is interested in processing a
 * upstreamSelectionChanged event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addUpstreamSelectionChangedListener<code> method. When
 * the upstreamSelectionChanged event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see UpstreamSelectionChangedEvent
 */
public class UpstreamSelectionChangedListener implements ISelectionChangedListener
{
    private CurrentPage cPage = null;

    private TreeViewer viewer;

    private CurrentViewFilterFromUpstreamSelection currentFilter;

    /**
     * Constructor
     * 
     * @param page the current page
     */
    public UpstreamSelectionChangedListener(CurrentPage page)
    {
        cPage = page;
        if (cPage.getViewer() instanceof TreeViewer)
        {
            viewer = (TreeViewer) cPage.getViewer();
        }
    }

    
    /**
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event)
    {
        if (viewer != null && viewer.getTree().isDisposed())
        {
            CurrentRequirementView currentView = ((CurrentRequirementView) CurrentRequirementView.getInstance());
            if (currentView != null)
            {
                if (currentView.getCurrentPage() instanceof CurrentPage)
                {
                    viewer = (TreeViewer) ((CurrentPage) currentView.getCurrentPage()).getViewer();
                }
            }
        }
        if (viewer != null && !viewer.getControl().isDisposed())
        {
            if (event.getSelection() instanceof ITreeSelection)
            {
                ITreeSelection treeS = (ITreeSelection) event.getSelection();
                if (treeS.getFirstElement() instanceof Requirement)
                {
                    final Requirement current = (Requirement) treeS.getFirstElement();
                    for (int i = 0; i < viewer.getFilters().length; i++)
                    {
                        viewer.removeFilter(viewer.getFilters()[i]);
                    }
                    // we set the selection to null to avoid stack overflow
                    // ie : the current page will not try to restore a selection which is not visible
                    viewer.setSelection(null);
                    currentFilter = CurrentViewFilterFromUpstreamSelection.getInstance();
                    currentFilter.setSearchedRequirement(current);
                    viewer.addFilter(currentFilter);
                    viewer.expandAll();
                    for (Object element : viewer.getExpandedElements())
                    {
                        if (element instanceof AttributeConfiguration)
                        {
                            viewer.collapseToLevel(element, 1);
                        }
                        else if (element instanceof CurrentRequirement)
                        {
                            viewer.collapseToLevel(element, 1);
                        }
                    }

                }
                else
                {
                    if (currentFilter != null)
                    {
                        currentFilter.setSearchedRequirement(null);
                        viewer.refresh();
                    }
                }
            }
        }
    }
}
