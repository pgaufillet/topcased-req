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
package org.topcased.requirement.bundle.topcased.commands;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.ecore.resource.Resource;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.bundle.topcased.internal.Messages;
import org.topcased.requirement.bundle.topcased.resource.TopcasedAttachmentPolicy;
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
    private IPath requirementResourcePath;

    private Modeler modeler;

    /**
     * Constructor
     * 
     * @param targetModel
     * @param requirementModelPath
     */
    public LinkRequirementModelCommand(Resource targetModel, IPath requirementModelPath)
    {
        super(Messages.getString("LinkRequirementModelCommand.0")); //$NON-NLS-1$
        requirementResourcePath = requirementModelPath;
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
        // Set the property
        TopcasedAttachmentPolicy.setProperty(modeler, RequirementUtils.getResource(requirementResourcePath));

        // Notify views that the diagram property has changed
        CurrentRequirementView currentView = (CurrentRequirementView)CurrentRequirementView.getInstance();
        if (currentView != null)
        {
            ((CurrentRequirementView) CurrentRequirementView.getInstance()).partActivated(modeler);
        }
        UpstreamRequirementView upstreamView =  (UpstreamRequirementView) UpstreamRequirementView.getInstance();
        if (upstreamView != null)
        {
            ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).partActivated(modeler);
        }
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#undo()
     */
    @Override
    public void undo()
    {
        // unload the requirement model
        RequirementUtils.unloadRequirementModel(modeler.getEditingDomain());

        // Set the property
        TopcasedAttachmentPolicy.setProperty(modeler, null);

        // Notify views that the diagram property has changed
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
