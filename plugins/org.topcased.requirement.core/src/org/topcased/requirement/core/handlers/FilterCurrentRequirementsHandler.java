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
import org.eclipse.ui.handlers.HandlerUtil;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.views.upstream.UpstreamPage;

/**
 * Handler for the filter of the current view from the upstream view
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public class FilterCurrentRequirementsHandler extends AbstractHandlerWithState
{    
    /** Reference to the current upstreamPage **/
    private UpstreamPage upstreamPage;

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

        upstreamPage = RequirementHelper.INSTANCE.getUpstreamPage();
        if (upstreamPage != null)
        {
            if (state.getValue().equals(true))
            {
                upstreamPage.hookUpstreamSelectionChangedListener();
            }
            else
            {
                upstreamPage.unhookUpstreamSelectionChangedListener();
            } 
        }
    }
}
