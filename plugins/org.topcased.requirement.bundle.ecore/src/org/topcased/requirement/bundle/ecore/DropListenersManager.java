/*****************************************************************************
 * Copyright (c) 2010 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Vincent Hemery (Atos Origin}) {vincent.hemery@atosorigin.com} - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.bundle.ecore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.topcased.requirement.core.dnd.RequirementDropListener;
import org.topcased.requirement.core.views.AbstractRequirementPage;

/**
 * The Class DropListenersManager.
 */
public class DropListenersManager
{

    /**
     * The Singleton holder class.
     */
    private static class SingletonHolder
    {

        /** The instance. */
        static protected DropListenersManager instance = new DropListenersManager();
    }

    /** map with known viewers and corresponding drop listener. */
    private Map<IDiagramGraphicalViewer, RequirementDropListener> viewersAndDropListeners;

    /** map with known requirement pages and their corresponding known viewers. */
    private Map<AbstractRequirementPage, List<IDiagramGraphicalViewer>> requirementPageAndViewers;

    /**
     * Instantiate the singleton manager.
     */
    private DropListenersManager()
    {
        viewersAndDropListeners = new HashMap<IDiagramGraphicalViewer, RequirementDropListener>();
        requirementPageAndViewers = new HashMap<AbstractRequirementPage, List<IDiagramGraphicalViewer>>();
    }

    /**
     * Get the only instance.
     * 
     * @return drop listener manager
     */
    public static DropListenersManager getInstance()
    {
        return SingletonHolder.instance;
    }

    /**
     * Register a new drop listener to a graphical viewer.
     * 
     * @param abstractRequirementPage the requirement page which correspond to the editor
     * @param graphViewer the viewer to register the listener to
     * @return true if a new listener has been created, false if one is already registered
     */
    public boolean registerDropListener(AbstractRequirementPage abstractRequirementPage, IDiagramGraphicalViewer graphViewer)
    {
        // check if requirement page is already known and register it otherwise
        if (!requirementPageAndViewers.containsKey(abstractRequirementPage))
        {
            requirementPageAndViewers.put(abstractRequirementPage, new ArrayList<IDiagramGraphicalViewer>());
        }
        // check if viewer is already known and register it otherwise
        if (!requirementPageAndViewers.get(abstractRequirementPage).contains(graphViewer))
        {
            requirementPageAndViewers.get(abstractRequirementPage).add(graphViewer);
        }
        // check if a drop is already registered and create it otherwise
        if (!viewersAndDropListeners.containsKey(graphViewer))
        {
            // register a new drop listener
            RequirementDropListener listener = new RequirementDropListener(graphViewer);
            graphViewer.addDropTargetListener(listener);
            viewersAndDropListeners.put(graphViewer, listener);
            return true;
        }
        return false;
    }

    /**
     * Remove all drop listeners associated with the requirement page.
     * 
     * @param abstractRequirementPage requirement page to remove listeners for
     */
    @SuppressWarnings("deprecation")
    public void removeListeners(AbstractRequirementPage abstractRequirementPage)
    {
        List<IDiagramGraphicalViewer> viewers = requirementPageAndViewers.remove(abstractRequirementPage);
        if (viewers != null)
        {
            for (IDiagramGraphicalViewer graphViewer : viewers)
            {
                RequirementDropListener dropListener = viewersAndDropListeners.remove(graphViewer);
                if (dropListener != null)
                {
                    graphViewer.removeDropTargetListener(dropListener);
                }
            }
        }
    }
}
