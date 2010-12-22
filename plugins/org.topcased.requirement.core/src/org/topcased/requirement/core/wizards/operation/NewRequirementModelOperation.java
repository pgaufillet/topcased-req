/***********************************************************************************************************************
 * Copyright (c) 2008, 2010 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 *               Maxime AUDRAIN (CS) - API Changes
 **********************************************************************************************************************/
package org.topcased.requirement.core.wizards.operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.commands.CommandStub;
import org.topcased.requirement.core.commands.UpdateMonitorCommand;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.IRequirementTransformation;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.extensions.RequirementTransformationManager;
import org.topcased.requirement.core.handlers.UnlinkRequirementModelHandler;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.util.RequirementResource;

/**
 * 
 * This class provides operation to create new requirement models
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 * @since 2.1.0
 * 
 */
public class NewRequirementModelOperation extends AbstractRequirementModelOperation
{
    private IFile sourceModelFile; // requirement model already created or not

    /**
     * The constructor
     * 
     * @param targetFile
     * @param sourceFile
     * @param destFile
     */
    public NewRequirementModelOperation(IFile targetFile, IFile sourceFile, IFile reqFile)
    {
        super(targetFile, reqFile);
        sourceModelFile = sourceFile;
    }

    /**
     * Get commands to create new requirement model
     * 
     * @see AbstractRequirementModelOperation#getCommands(IProgressMonitor)
     */
    protected List<Command> getCommands(IProgressMonitor monitor)
    {
        EditingDomain domain = null;
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices service = RequirementUtils.getSpecificServices(editor);
        if (service != null)
        {
            domain = service.getEditingDomain(editor);
        }

        // Deals with source model file extension and the fact that the target model could already have a requirement
        // project attached
        if (isCurrentModelerAlreadyAttached(domain))
        {
            return getUnlinkAndCreateCmd(domain, monitor, false);
        }
        else if (sourceModelFile.getFileExtension().equals(RequirementResource.FILE_EXTENSION))
        {
            return getCopyAndCreateCmd(monitor, false);
        }
        else
        {
            return getTransformAndCreateCmd(monitor);
        }
    }

    /**
     * Get the label
     */
    public String getLabel()
    {
        EditingDomain domain = null;
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices service = RequirementUtils.getSpecificServices(editor);
        if (service != null)
        {
            domain = service.getEditingDomain(editor);
        }
        if (isCurrentModelerAlreadyAttached(domain))
        {
            return Messages.getString("NewRequirementModelOperation.2"); //$NON-NLS-1$
        }
        else if (RequirementResource.FILE_EXTENSION.equals(sourceModelFile.getFileExtension()))
        {
            return Messages.getString("NewRequirementModelOperation.1"); //$NON-NLS-1$
        }
        else
        {
            return Messages.getString("NewRequirementModelOperation.4"); //$NON-NLS-1$
        }
    }

    /**
     * Creates an initial Requirement model
     * 
     * @param resource : the requirement model
     */
    protected void createInitialModel(Resource resource)
    {
        RequirementProject rootObject = (RequirementProject) RequirementUtils.getRoot(resource, RequirementProject.class);
        updateRequirementProject(rootObject);
        createAttributeConfiguration(rootObject);
    }

    /**
     * Creates a new Requirement model
     * 
     * @param monitor The progress monitor to use
     */
    protected List<Command> getNewRequirementModelCmd(final IProgressMonitor monitor)
    {
        Command startTaskCmd = UpdateMonitorCommand.getCommand(monitor, 4, Messages.getString("NewRequirementModelOperation.0"), 0, null); //$NON-NLS-1$

        Command cmdWS = new CommandStub()
        {

            public void redo()
            {
                // Add the initial model object to the contents
                createInitialModel(getRequirementResource());
                monitor.worked(1);

                // Save the contents of the resource to the file system
                RequirementUtils.saveResource(getRequirementResource());
                monitor.worked(1);
            }

            @Override
            public void undo()
            {
                RequirementUtils.deleteResource(getRequirementResource());
            }
        };
        // Update the target model
        Command cmdModel = getUpdateRequirementReferenceCmd(monitor);
        return Arrays.asList(startTaskCmd, cmdWS, cmdModel);
    }

    /**
     * Copy and rename the source model file (with .requirement extension) and create the requirement model
     * 
     * @param monitor The progress monitor to use
     * @param destinationFileWillBeMoved true if destination location is occupied for the moment, but will be moved
     *        before execution
     * @return
     */
    protected List<Command> getCopyAndCreateCmd(final IProgressMonitor monitor, boolean destinationFileWillBeMoved)
    {
        List<Command> result = new ArrayList<Command>(2);
        IFile fileDest = ResourcesPlugin.getWorkspace().getRoot().getFile(requirementModelFile.getFullPath().addFileExtension(RequirementResource.FILE_EXTENSION));

        // If the requirement model file is not close to the target model file
        if (!fileDest.exists() || destinationFileWillBeMoved)
        {
            result.add(UpdateMonitorCommand.getCommand(monitor, 0, getLabel(), 0, null));
            Command cmd = new CommandStub()
            {
                public void redo()
                {
                    // rename the file from the name given in the dialog and copy it next to the target model
                    try
                    {
                        sourceModelFile.copy(requirementModelFile.getFullPath().addFileExtension(RequirementResource.FILE_EXTENSION), true, monitor);
                    }
                    catch (CoreException e)
                    {
                        RequirementCorePlugin.log(e);
                    }
                }
            };
            result.add(cmd);
            result.add(UpdateMonitorCommand.getCommand(monitor, 1, 0));
        }

        // Create the requirement model
        result.addAll(getNewRequirementModelCmd(monitor));
        return result;
    }

    /**
     * Unlink the already attached requirement model and create the new requirement model for the source model file
     * 
     * @param domain The editing domain of the requirement project
     * @param monitor The progress monitor to use
     * @param toTransform if the source model file has to be transformed into a .requirement file
     * @return
     */
    protected List<Command> getUnlinkAndCreateCmd(EditingDomain domain, IProgressMonitor monitor, boolean toTransform)
    {
        monitor.subTask(getLabel());
        List<Command> result = new ArrayList<Command>();

        UnlinkRequirementModelHandler unlinkAction = new UnlinkRequirementModelHandler();
        try
        {
            Command command = unlinkAction.getExecutionCommand();
            result.add(command);
        }
        catch (ExecutionException e)
        {
            RequirementCorePlugin.log(e);
        }

        // Cancel has not been pressed
        if (unlinkAction.getDialogResult() != 1)
        {
            // // The file has not been deleted by the user, we need to rename it
            // if (unlinkAction.getDialogResult() == 0)
            // {
            //                monitor.subTask(Messages.getString("NewRequirementModelOperation.3")); //$NON-NLS-1$
            //
            // // rename the file to add with the extension ".old.requirement"
            // IPath path = oldRequirementFile.getFullPath().removeFileExtension().addFileExtension(MODEL_OLD);
            // try
            // {
            // oldRequirementFile.move(path.addFileExtension(MODEL_EXTENSION), true, monitor);
            // }
            // catch (CoreException e)
            // {
            // RequirementCorePlugin.log(e);
            // }
            // monitor.worked(1);
            // }

            // Deals with the source model file extension
            if (toTransform)
            {
                result.addAll(getTransformAndCreateCmd(monitor));
            }
            else
            {
                result.addAll(getCopyAndCreateCmd(monitor, true));
            }
        }
        return result;
    }

    /**
     * Process the transformation of the source model file and create the requirement model
     * 
     * @param monitor The progress monitor to use
     * @return
     */
    protected List<Command> getTransformAndCreateCmd(final IProgressMonitor monitor)
    {
        List<Command> result = new ArrayList<Command>(2);
        IFile fileDest = ResourcesPlugin.getWorkspace().getRoot().getFile(requirementModelFile.getFullPath().addFileExtension(RequirementResource.FILE_EXTENSION));

        // If the requirement model file is not close to the target model file
        if (!fileDest.exists())
        {
            result.add(UpdateMonitorCommand.getCommand(monitor, 0, getLabel(), 0, null));
            Command cmd = new CommandStub()
            {
                public void redo()
                {
                    // Get the transformation from the requirementTransformation extension point
                    IRequirementTransformation reqTransfo = RequirementTransformationManager.getInstance().getRequirementTransformation(sourceModelFile.getFileExtension());
                    if (reqTransfo != null)
                    {
                        // Process the transformation to create the requirement model
                        reqTransfo.transformation(sourceModelFile, requirementModelFile);
                    }
                }
            };
            result.add(cmd);
            result.add(UpdateMonitorCommand.getCommand(monitor, 1, 0));
        }

        // Create the requirement model
        result.addAll(getNewRequirementModelCmd(monitor));
        return result;
    }

    /**
     * Check if the current modeler is already attached to a requirement model
     * 
     * @return true if already attached, false otherwise
     */
    protected boolean isCurrentModelerAlreadyAttached(EditingDomain domain)
    {
        Resource linkedTargetModel = null;

        // Get the attachment policy for the target model file
        IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(targetModelFile.getFileExtension());

        // Check if the target model file is already linked to a requirement project
        if (policy != null)
        {
            linkedTargetModel = policy.getLinkedTargetModel(domain.getResourceSet());
            if (linkedTargetModel != null)
            {
                return RequirementUtils.getFile(linkedTargetModel).equals(targetModelFile);
            }
        }
        else
        {
            String extension = domain.getResourceSet().getResources().get(0).getURI().fileExtension();
            String msg = NLS.bind(Messages.getString("ModelAttachmentPolicyManager.0"), extension);
            RequirementCorePlugin.log(msg, Status.ERROR, null);//$NON-NLS-1$
            return false;
        }
        return false;
    }

}
