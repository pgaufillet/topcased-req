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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.topcased.requirement.core.wizards.operation.AbstractRequirementModelOperation;
import org.topcased.requirement.util.RequirementResource;

/**
 * A command to unlink a requirement model from a papyrus diagram
 * 
 */
public class UnlinkRequirementModelCommand extends AbstractCommand
{
    private IPath requirementResourcePath;

    private IEditorPart editor;

    /**
     * Constructor
     * 
     * @param targetModel
     * @param requirementModelPath
     */
    public UnlinkRequirementModelCommand(Resource targetModel, IPath requirementModelPath)
    {
        super(Messages.getString("UnlinkRequirementModelCommand.0")); //$NON-NLS-1$
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

            // unload the requirement model.
            RequirementUtils.unloadRequirementModel(services.getEditingDomain(editor));
        }

        // Notify views that the diagram property has changed
        // ((CurrentRequirementView) CurrentRequirementView.getInstance()).partClosed(editor);
        // ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).partClosed(editor);
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#undo()
     */
    @Override
    public void undo()
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
        // ((CurrentRequirementView) CurrentRequirementView.getInstance()).partActivated(editor);
        // ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).partActivated(editor);
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
     */
    @Override
    public boolean canExecute()
    {
        return editor != null;
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#canUndo()
     */
    @Override
    public boolean canUndo()
    {
        // Get the old requirement file (handle cases when a new requirement model is attached to a previously attached
        // diagram)
        IPath path = requirementResourcePath.removeFileExtension().addFileExtension(AbstractRequirementModelOperation.MODEL_OLD).addFileExtension(RequirementResource.FILE_EXTENSION);
        IFile fileOld = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

        return fileOld.exists();
    }
}
