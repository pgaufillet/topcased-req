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

import org.eclipse.core.commands.AbstractHandlerWithState;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.ui.handlers.HandlerUtil;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.upstream.UpstreamPage;

/**
 * Handler to deals with the Sort command in the upstream view
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public class SortHandler extends AbstractHandlerWithState
{
    /** Reference to the upstream page **/
    private UpstreamPage page;

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        HandlerUtil.toggleCommandState(event.getCommand());
        return null; 
    }

    /**
     * @see org.eclipse.core.commands.AbstractHandlerWithState#handleStateChange(org.eclipse.core.commands.State, java.lang.Object)
     */
    public void handleStateChange(State state, Object oldValue)
    {
        page = RequirementHelper.INSTANCE.getUpstreamPage(); 
        if (page != null)
        {
            applySorter((Boolean)state.getValue());
        }
    }
      
    /**
     * @see org.eclipse.core.commands.AbstractHandler#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        Modeler modeler = Utils.getCurrentModeler();
        if (modeler != null)
        {
            Resource requirement = RequirementUtils.getRequirementModel(modeler.getEditingDomain());
            if (requirement != null)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Applies an eventual sorter on the tree viewer.
     * 
     * @param state The activation status of the sorter
     */
    private void applySorter(boolean state)
    {
        ((StructuredViewer)page.getViewer()).setSorter(state ? new ViewerSorter() : null);
    }

}
