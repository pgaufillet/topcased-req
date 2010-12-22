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
package org.topcased.requirement.bundle.topcased.services;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.core.dnd.RequirementDropListener;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.AbstractRequirementPage;

public class DropListenersManager
{
    /**
     * The Singleton holder class
     */
    private static class SingletonHolder
    {
        static protected DropListenersManager instance = new DropListenersManager();
    }

    /** map with known requirement pages and corresponding listeners */
    private Map<AbstractRequirementPage, RequirementDropListener> requirementPageAndDropListener;

    /**
     * Instantiate the singleton manager
     */
    private DropListenersManager()
    {
        requirementPageAndDropListener = new HashMap<AbstractRequirementPage, RequirementDropListener>();
    }

    /**
     * Get the only instance
     * 
     * @return drop listener manager
     */
    public static DropListenersManager getInstance()
    {
        return SingletonHolder.instance;
    }

    /**
     * Register a new drop listener to a graphical viewer
     * 
     * @param abstractRequirementPage the requirement page which correspond to the editor
     * @param graphViewer the viewer to register the listener to
     * @return true if a new listener has been created, false if one is already registered
     */
    public boolean registerDropListener(AbstractRequirementPage abstractRequirementPage, EditPartViewer graphViewer)
    {
        // check if requirement page is already known and register it otherwise
        if (!requirementPageAndDropListener.containsKey(abstractRequirementPage))
        {
            // register a new drop listener
            RequirementDropListener listener = new RequirementDropListener(graphViewer);
            graphViewer.addDropTargetListener(listener);
            requirementPageAndDropListener.put(abstractRequirementPage, listener);
            return true;
        }
        return false;
    }

    /**
     * Remove all drop listeners associated with the requirement page
     * 
     * @param abstractRequirementPage requirement page to remove listeners for
     */
    @SuppressWarnings("deprecation")
    public void removeListeners(AbstractRequirementPage abstractRequirementPage)
    {
        RequirementDropListener listener = requirementPageAndDropListener.remove(abstractRequirementPage);
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
        if (services != null && listener != null)
        {
            EditPartViewer graphViewer = services.getGraphicalViewer(editor);
            graphViewer.removeDropTargetListener(listener);
        }
    }
}
