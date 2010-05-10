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
package org.topcased.requirement.core.commands;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.ecore.resource.Resource;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.extensions.DefaultAttachmentPolicy;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentRequirementView;
import org.topcased.requirement.core.views.upstream.UpstreamRequirementView;

/**
 * A command to link a requirement model to a topcased diagram
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public class LinkRequirementModelCommand extends AbstractCommand
{   
    private Resource requirementResource;
    
    private Modeler modeler;
    
    /**
     * Constructor
     * 
     * @param targetModel
     * @param requirementModel
     */
    public LinkRequirementModelCommand(Resource targetModel, Resource requirementModel)
    {
        super(Messages.getString("LinkRequirementModelCommand.0")); //$NON-NLS-1$
        requirementResource = requirementModel;
        modeler = Utils.getCurrentModeler();
    }

    /**
     * @see org.eclipse.emf.common.command.Command#execute()
     */
    public void execute()
    {
        redo();
    }

    /**
     * @see org.eclipse.emf.common.command.Command#redo()
     */
    public void redo()
    {
        //Set the property
        DefaultAttachmentPolicy.getInstance().setProperty(modeler, requirementResource);
        
        //Notify views that the diagram property has changed
        ((CurrentRequirementView) CurrentRequirementView.getInstance()).partActivated(modeler);
        ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).partActivated(modeler);
    }
    
    
    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#undo()
     */
    @Override
    public void undo()
    {
        // unload the requirement model
        RequirementUtils.unloadRequirementModel(modeler.getEditingDomain());
        
        //Set the property
        DefaultAttachmentPolicy.getInstance().setProperty(modeler, null);
        
        //Notify views that the diagram property has changed
        ((CurrentRequirementView) CurrentRequirementView.getInstance()).partClosed(modeler);
        ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).partClosed(modeler);
    }
    
    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
     */
    @Override
    public boolean canExecute()
    {
        return Utils.getCurrentModeler() != null;
    }

}
