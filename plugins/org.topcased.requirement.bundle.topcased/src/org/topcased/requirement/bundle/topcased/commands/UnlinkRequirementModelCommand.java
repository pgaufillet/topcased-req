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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.topcased.requirement.core.wizards.operation.AbstractRequirementModelOperation;
import org.topcased.requirement.util.RequirementResource;

/**
 * A command to unlink a requirement model from a topcased diagram
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class UnlinkRequirementModelCommand extends AbstractCommand
{
    private IPath requirementResourcePath;

    private Modeler modeler;

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
        TopcasedAttachmentPolicy.setProperty(modeler, null);

        // unload the requirement model from file system.
        RequirementUtils.unloadRequirementModel(modeler.getEditingDomain());

        // Notify views that the diagram property has changed
        ((CurrentRequirementView) CurrentRequirementView.getInstance()).partClosed(modeler);
        ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).partClosed(modeler);
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#undo()
     */
    @Override
    public void undo()
    {
        // Set the property
        TopcasedAttachmentPolicy.setProperty(modeler, RequirementUtils.getResource(requirementResourcePath));

        // Notify views that the diagram property has changed
        ((CurrentRequirementView) CurrentRequirementView.getInstance()).partActivated(modeler);
        ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).partActivated(modeler);
    }

    /**
     * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
     */
    @Override
    public boolean canExecute()
    {
        return modeler != null;
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

        return !fileOld.exists();
    }
}
