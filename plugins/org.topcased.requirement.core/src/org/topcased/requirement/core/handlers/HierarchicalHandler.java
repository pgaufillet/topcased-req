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
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.views.upstream.UpstreamPage;

/**
 * Handler of the hierarchical commands
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class HierarchicalHandler extends AbstractHandlerWithState
{
    /** Reference to the upstream page **/
    private UpstreamPage page;

    /**
     * FIXME : for now there is two RegisteryTOGGLEState for each commands who should be RADIO styled!! Tried to put the
     * RegisteryRadioState for each but having bugs with this state
     * 
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        ICommandService cs = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
        Command flatCmd = cs.getCommand(ICommandConstants.FLAT_ID);

        HandlerUtil.toggleCommandState(event.getCommand());
        flatCmd.getState(RegistryToggleState.STATE_ID).setValue(!(Boolean) event.getCommand().getState(RegistryToggleState.STATE_ID).getValue());

        return null;
    }

    /**
     * Applies the right representation of the tree contents
     * 
     * @param isFlat should we use the flat representation or the tree one ?
     */
    private void applyRepresentation(boolean isFlat)
    {
        page = RequirementHelper.INSTANCE.getUpstreamPage();
        page.getUpstreamRequirementContentProvider().setIsFlat(isFlat);
        page.getViewer().refresh();
    }

    /**
     * @see org.eclipse.core.commands.AbstractHandlerWithState#handleStateChange(org.eclipse.core.commands.State,
     *      java.lang.Object)
     */
    public void handleStateChange(State state, Object oldValue)
    {
        if (state.getValue().equals(true))
        {
            applyRepresentation(true);
        }
    }
}
