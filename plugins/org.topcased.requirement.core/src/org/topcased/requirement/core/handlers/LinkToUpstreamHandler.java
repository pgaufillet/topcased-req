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
import org.topcased.requirement.core.views.upstream.UpstreamRequirementView;

/**
 * Handler of the Link to upstream requirement command
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class LinkToUpstreamHandler extends AbstractHandlerWithState
{

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        HandlerUtil.toggleCommandState(event.getCommand());
        return null;
    }

    /**
     * @see org.eclipse.core.commands.AbstractHandlerWithState#handleStateChange(org.eclipse.core.commands.State,
     *      java.lang.Object)
     */
    public void handleStateChange(State state, Object oldValue)
    {
        UpstreamRequirementView upstreamView = (UpstreamRequirementView) UpstreamRequirementView.getInstance();
        if (state.getValue().equals(true))
        {
            if (upstreamView != null)
            {
                upstreamView.hookListener();
            }
        }
        else
        {
            if (upstreamView != null)
            {
                upstreamView.unhookListener();
            }
        }
    }
}
