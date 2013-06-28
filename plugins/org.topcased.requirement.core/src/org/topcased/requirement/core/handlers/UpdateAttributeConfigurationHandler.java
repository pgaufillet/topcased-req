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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.State;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.core.dialogs.UpdateAttributeConfigurationDialog;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This handler allows to update attribute configuration of a requirement model.<br>
 * It has for effect to launch the update attribute configuration dialog.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class UpdateAttributeConfigurationHandler extends AbstractHandler
{

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {        
        if (((EvaluationContext)event.getApplicationContext()).getDefaultVariable() instanceof List<?>)
        {
            //Get the current selection
            List<?> elements = ((List<?>)((EvaluationContext)event.getApplicationContext()).getDefaultVariable());

            ICommandService cs = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
            Command tableCmd = cs.getCommand("org.topcased.requirement.core.filterInvalidRequirements");

            ExecutionEvent eEvent = new ExecutionEvent(tableCmd, new HashMap<String, String>(), event.getTrigger(), event.getApplicationContext());
            String message ="";
            if(isFilterSet(tableCmd)){
                message = Messages.getString("UpdateAttributeConfirmationDialog.3");
            }else{
                message = Messages.getString("UpdateAttributeConfirmationDialog.2");
            }

            //Launch the update attribute configuration dialog
            if(IsImpacted()){
                Dialog msgDialog = new MessageDialog(Display.getCurrent().getActiveShell(), 
                        Messages.getString("UpdateAttributeConfirmationDialog.1"), null, message, 
                        MessageDialog.WARNING, new String[] {IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL}, Window.OK);

                if((msgDialog.open()==Window.OK) && (!isFilterSet(tableCmd))) {

                    try
                    {
                        tableCmd.executeWithChecks(eEvent);
                    }
                    catch (NotDefinedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (NotEnabledException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (NotHandledException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }else{
                  new UpdateAttributeConfigurationDialog(elements, Display.getCurrent().getActiveShell()).open();
            }

        }
        return null;
    }
    
    public boolean IsImpacted()
    {
        Boolean noRequirementImpacted = false;

        IEditorServices services = RequirementUtils.getSpecificServices(null);

        // Modeler modeler = Utils.getCurrentModeler();
        //ISourceProviderService spc = (ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
        //RequirementModelSourceProvider myPro = (RequirementModelSourceProvider) spc.getSourceProvider(RequirementModelSourceProvider.IS_IMPACTED);

        if (services != null)
        {
            Resource requirement = RequirementUtils.getRequirementModel(services.getEditingDomain(null));
            if (requirement != null)
            {
                Collection<EObject> allRequirement = RequirementUtils.getAllObjects(requirement, CurrentRequirement.class);
                // checks that all CurrentRequirement are marked as not
                // impacted.
                for (EObject aReq : allRequirement)
                {
                    if (aReq instanceof CurrentRequirement && ((CurrentRequirement) aReq).isImpacted())
                    {
                        // action must be disabled.
                        noRequirementImpacted = true;
                        break;
                    }
                }
            }
        }
        return noRequirementImpacted;
    }
    
    public boolean isFilterSet(Command command) throws ExecutionException {
        State state = command.getState(RegistryToggleState.STATE_ID);
        if(state == null)
            throw new ExecutionException("The command does not have a toggle state"); //$NON-NLS-1$
         if(!(state.getValue() instanceof Boolean))
            throw new ExecutionException("The command's toggle state doesn't contain a boolean value"); //$NON-NLS-1$
             
        boolean Value = ((Boolean) state.getValue()).booleanValue();
        return Value;
    }
}
