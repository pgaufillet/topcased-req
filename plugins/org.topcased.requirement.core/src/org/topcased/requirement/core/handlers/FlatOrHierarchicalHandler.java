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
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.upstream.UpstreamPage;

/**
 * Handler of the flat and the hierarchical commands
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public class FlatOrHierarchicalHandler extends AbstractHandlerWithState
{
    /** Reference to the upstream page **/
    private UpstreamPage page;
    
    /**current flat state storage due to the double toggle state for the commands **/    
    private boolean currentFlatState = false;
    
    /**current hierarchical state storage due to the double toggle state for the commands **/    
    private boolean currentHierarchicalState = false;

    
    /**
     * FIXME : for now there is two RegisteryTOGGLEState for each commands who are RADIO styled!!
     * Tried to put the RegisteryRadioState for each but having bugs with this state
     * 
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {         
        if (event.getCommand().getId().equals(ICommandConstants.FLAT_ID))
        {
            currentFlatState = (Boolean) event.getCommand().getState(RegistryToggleState.STATE_ID).getValue();
            currentHierarchicalState = !currentFlatState;
        }
        else if (event.getCommand().getId().equals(ICommandConstants.HIERARCHICAL_ID))
        {
            currentHierarchicalState = (Boolean) event.getCommand().getState(RegistryToggleState.STATE_ID).getValue();
            currentFlatState = !currentHierarchicalState;
        }
        
        HandlerUtil.toggleCommandState(event.getCommand());
        return null; 
    }

    /**
     * Applies the right representation of the tree contents
     * 
     * @param isFlat should we use the flat representation or the tree one ?
     */
    private void applyRepresentation(boolean isFlat)
    {
        page.getUpstreamRequirementContentProvider().setIsFlat(isFlat);
        page.getViewer().refresh();
    }

    /**
     * @see org.eclipse.core.commands.AbstractHandlerWithState#handleStateChange(org.eclipse.core.commands.State, java.lang.Object)
     */
    public void handleStateChange(State state, Object oldValue)
    {
        page = RequirementHelper.INSTANCE.getUpstreamPage(); 
        if (page != null)
        {
            if (currentFlatState == true && currentHierarchicalState == false && !page.getUpstreamRequirementContentProvider().getIsFlat())
            {
                applyRepresentation(true);
            }
            else if (currentFlatState == false && currentHierarchicalState == true && page.getUpstreamRequirementContentProvider().getIsFlat())
            {
                applyRepresentation(false);
            }
            else if (currentFlatState == false && currentHierarchicalState == false)
            {
                applyRepresentation((Boolean) state.getValue());
            }
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
}
