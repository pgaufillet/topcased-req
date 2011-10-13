/*****************************************************************************
 * Copyright (c) 2010 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Vincent Hemery (Atos Origin}) {vincent.hemery@atosorigin.com} - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.bundle.papyrus.commands;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.papyrus.resource.IModel;
import org.eclipse.papyrus.resource.ModelSet;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.bundle.papyrus.internal.Messages;
import org.topcased.requirement.bundle.papyrus.resource.RequirementModel;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentRequirementView;
import org.topcased.requirement.core.views.upstream.UpstreamRequirementView;

/**
 * A command to link a requirement model to a papyrus diagram
 * 
 */
public class LinkRequirementModelCommand extends AbstractCommand
{
    private IPath requirementResourcePath;

    private IEditorPart editor;

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
        editor = RequirementUtils.getCurrentEditor();
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
        // add requirement resource to ModelSet
        IEditorServices services = RequirementUtils.getSpecificServices(editor);
        if (services != null)
        {
            EditingDomain domain = services.getEditingDomain(editor);
            ResourceSet resourceSet = domain.getResourceSet();
            if (resourceSet instanceof ModelSet)
            {
                IModel requirementModel = ((ModelSet) resourceSet).getModel(RequirementModel.REQ_MODEL_ID);
                if (requirementModel instanceof RequirementModel)
                {
                    // reload created requirement model
                    RequirementModel requirements = (RequirementModel) requirementModel;
                    requirements.loadModel(requirementResourcePath.removeFileExtension());
                }
            }
        }

        // Notify views that the diagram property has changed
        CurrentRequirementView currentView = (CurrentRequirementView)CurrentRequirementView.getInstance();
        if (currentView != null)
        {
            ((CurrentRequirementView) CurrentRequirementView.getInstance()).partActivated(editor);
        }
        UpstreamRequirementView upstreamView =  (UpstreamRequirementView) UpstreamRequirementView.getInstance();
        if (upstreamView != null)
        {
            ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).partActivated(editor);
        }
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#undo()
     */
    @Override
    public void undo()
    {
        // remove requirement resource from ModelSet
        IEditorServices services = RequirementUtils.getSpecificServices(editor);
        if (services != null)
        {
            EditingDomain domain = services.getEditingDomain(editor);
            ResourceSet resourceSet = domain.getResourceSet();
            if (resourceSet instanceof ModelSet)
            {
                IModel requirementModel = ((ModelSet) resourceSet).getModel(RequirementModel.REQ_MODEL_ID);
                if (requirementModel instanceof RequirementModel)
                {
                    // unload requirement model
                    requirementModel.unload();
                }
            }
            // unload the requirement model
            RequirementUtils.unloadRequirementModel(services.getEditingDomain(editor));
        }

        // Notify views that the diagram property has changed
        // ((CurrentRequirementView) CurrentRequirementView.getInstance()).partClosed(editor);
        // ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).partClosed(editor);
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
     */
    @Override
    public boolean canExecute()
    {
        return editor != null;
    }

}
