/***********************************************************************************************************************
 * Copyright (c) 2012 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Laurent Devernay <a href="mailto:laurent.devernay@atos.net">laurent.devernay@atos.net</a>"
 * 
 **********************************************************************************************************************/

package org.topcased.requirement.core.handlers;

import org.eclipse.core.commands.AbstractHandlerWithState;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.topcased.requirement.core.filters.ValidRequirementFilter;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.views.current.CurrentPage;

public class ToggleValidRequirementFilterHandler extends AbstractHandlerWithState
{

    private final ValidRequirementFilter validReqFilter = new ValidRequirementFilter();

    private Command command;

    public Object execute(ExecutionEvent event) throws ExecutionException
    {

        command = event.getCommand();
        HandlerUtil.toggleCommandState(command);
        return null;
    }

    public void handleStateChange(State state, Object oldValue)
    {
        // Getting the value the state was toggled to.
        boolean newValue = (Boolean) state.getValue();
        CurrentPage currentPage = RequirementHelper.INSTANCE.getCurrentPage();
        Viewer viewer = currentPage.getViewer();
        Listener[] listeners = viewer.getControl().getListeners(SWT.Dispose);
        boolean listenerAdded = false;
        if (listeners != null)
        {
            for (Listener l : listeners)
            {
                if (l instanceof TypedListener)
                {
                    if (((TypedListener) l).getEventListener() instanceof FilterDisposeListener)
                    {
                        listenerAdded = true;
                        break;
                    }
                }
                else
                {
                    if (l instanceof FilterDisposeListener)
                    {
                        listenerAdded = true;
                        break;
                    }
                }
            }
        }
        if (!listenerAdded)
        {
            viewer.getControl().addDisposeListener(new FilterDisposeListener());
        }
        // transition false -> true : activation of the Filter
        if (newValue && (viewer instanceof TreeViewer))
        {
            ((TreeViewer) viewer).addFilter(validReqFilter);
        }
        // transition true -> false : inactivation of the Filter.
        else if (!newValue && (viewer instanceof TreeViewer))
        {
            ((TreeViewer) viewer).removeFilter(validReqFilter);
            // Emptying the cache of the filter to prevent inconsistencies
            // next time it is used.
            validReqFilter.emptyCache();
        }
    }

    /**
     * Listener to reset the toggle button when the view is closed.
     * 
     * @author omelois
     * 
     */
    private class FilterDisposeListener implements DisposeListener
    {

        public void widgetDisposed(DisposeEvent e)
        {
            CurrentPage currentPage = RequirementHelper.INSTANCE.getCurrentPage();
            TreeViewer viewer = (TreeViewer) currentPage.getViewer();
            try
            {
                State state = command.getState(RegistryToggleState.STATE_ID);
                if (state == null){
                    throw new ExecutionException("The command does not have a toggle state");
                }
                if (!(state.getValue() instanceof Boolean)){
                    throw new ExecutionException("The command's toggle state doesn't contain a boolean value");
                }
                state.setValue(false);
                viewer.removeFilter(ToggleValidRequirementFilterHandler.this.validReqFilter);
                validReqFilter.emptyCache();
            }
            catch (ExecutionException e1)
            {
                e1.printStackTrace();
            }
        }

    }

}
