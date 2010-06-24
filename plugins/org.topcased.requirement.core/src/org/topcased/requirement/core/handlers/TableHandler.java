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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.views.properties.PropertySheet;
import org.topcased.modeler.editor.properties.ModelerPropertySheetPage;
import org.topcased.requirement.core.properties.sections.RequirementPropertySection;

/**
 * Handler of the Table command
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class TableHandler extends AbstractHandlerWithState
{
    /**
     * the property view
     */
    PropertySheet sheet;
    
    /**
     * FIXME : for now there is two RegisteryTOGGLEState for each commands who should be RADIO styled!! Tried to put the
     * RegisteryRadioState for each but having bugs with this state
     * 
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        ICommandService cs = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
        Command treeCmd = cs.getCommand(ICommandConstants.TREE_ID);

        if (HandlerUtil.getActivePart(event) instanceof PropertySheet)
        {
            sheet = (PropertySheet) HandlerUtil.getActivePart(event);
        }
        HandlerUtil.toggleCommandState(event.getCommand());
        treeCmd.getState(RegistryToggleState.STATE_ID).setValue(!(Boolean) event.getCommand().getState(RegistryToggleState.STATE_ID).getValue());

        return null;
    }

    /**
     * @see org.eclipse.core.commands.AbstractHandlerWithState#handleStateChange(org.eclipse.core.commands.State,
     *      java.lang.Object)
     */
    public void handleStateChange(State state, Object oldValue)
    {
        if (state.getValue().equals(true))
        {
            if (sheet != null)
            {
                if (sheet.getCurrentPage() instanceof ModelerPropertySheetPage)
                {
                    ModelerPropertySheetPage page = (ModelerPropertySheetPage) sheet.getCurrentPage();
                    page.getCurrentTab();
                    if (page.getCurrentTab().getSectionAtIndex(0) instanceof RequirementPropertySection)
                    {
                        //Get the requirement property section
                        RequirementPropertySection section = (RequirementPropertySection) page.getCurrentTab().getSectionAtIndex(0);
                        
                        //Get the parent composite
                        Composite parent = section.getParentCompo();
                        IWorkbenchPart part = section.getPart();
                        ISelection selection = section.getSelection();
                        
                        //Dispose the current viewer
                        section.disposeViewer();
                        
                        //Create the new viewer
                        section.createTable(parent);
                        section.setInput(part, selection);  
                        
                        //Repack, resize and refresh it
                        parent.pack(false);
                        parent.setSize(1000, 500);
                        section.getViewer().refresh(true);  
                    }
                }
            }
        }
    }
}
