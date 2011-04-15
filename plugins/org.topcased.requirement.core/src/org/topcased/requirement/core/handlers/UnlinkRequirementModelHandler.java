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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.dialogs.UnlinkDialog;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentRequirementView;
import org.topcased.requirement.core.views.upstream.UpstreamRequirementView;
import org.topcased.requirement.core.wizards.operation.AbstractRequirementModelOperation;
import org.topcased.requirement.util.RequirementResource;

/**
 * Handler to deal with the unlink action in the upstream view
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class UnlinkRequirementModelHandler extends AbstractHandler
{
    /** result of the unlink dialog **/
    private int dialogResult;

    /**
     * Get the command which must be used for executing the action. This also triggers necessary popup windows.
     * 
     * @return the command to execute or null
     */
    public Command getExecutionCommand() throws ExecutionException
    {
        Command command = null;
        boolean deleteRequirementModel = false;

        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = RequirementUtils.getSpecificServices(editor);
        if (services != null)
        {
            EditingDomain domain = services.getEditingDomain(editor);
            if (domain != null)
            {
                // launch the unlink dialog
                UnlinkDialog dialog = new UnlinkDialog(Display.getCurrent().getActiveShell(),
                        Messages.getString("UnlinkRequirementModelHandler.0"), Messages.getString("UnlinkRequirementModelHandler.1"));//$NON-NLS-1$ //$NON-NLS-2$
                dialogResult = dialog.open();

                // If "delete file" is checked
                if (dialogResult == 2)
                {
                    deleteRequirementModel = true;
                }

                // In OK cases
                if (dialogResult == 0 || dialogResult == 2)
                {
                    IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(domain);
                    Resource requirementResource = RequirementUtils.getRequirementModel(domain);
                    IPath requirementResourcePath = new Path(URI.decode(requirementResource.getURI().toPlatformString(true)));

                    CompoundCommand compoundCmd = new CompoundCommand();
                    if (policy != null)
                    {
                        Command cmd = policy.unlinkRequirementModel(policy.getLinkedTargetModel(domain.getResourceSet()), requirementResourcePath);
                        compoundCmd.append(cmd);
                    }
                    else
                    {
                        String extension = domain.getResourceSet().getResources().get(0).getURI().fileExtension();
                        String msg = NLS.bind(Messages.getString("ModelAttachmentPolicyManager.0"), extension);
                        RequirementCorePlugin.log(msg, Status.ERROR, null);//$NON-NLS-1$
                        return UnexecutableCommand.INSTANCE;
                    }
                    // add Workspace command
                    if (deleteRequirementModel)
                    {
                        Command cmd = getDeleteResourceCommand(requirementResource);
                        compoundCmd.append(cmd);
                    }
                    else
                    {
                        Command cmd = getRenameResourceCommand(requirementResource);
                        compoundCmd.append(cmd);
                    }
                    command = compoundCmd;
                }
            }
        }
        return command;
    }

    /**
     * Get the command to rename the requirement resource
     * 
     * @param requirementResource requirement resource to rename
     * @return the rename command
     */
    private Command getRenameResourceCommand(final Resource requirementResource)
    {
        return new AbstractCommand()
        {
            @Override
            public boolean canExecute()
            {
                IFile oldRequirementFile = RequirementUtils.getFile(requirementResource);
                return oldRequirementFile.exists();
            }

            @Override
            public boolean canUndo()
            {
                IFile oldRequirementFile = RequirementUtils.getFile(requirementResource);
                IPath noExtensionPath = oldRequirementFile.getFullPath().removeFileExtension();
                IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                IPath path = noExtensionPath.addFileExtension(AbstractRequirementModelOperation.MODEL_OLD).addFileExtension(RequirementResource.FILE_EXTENSION);
                IFile fileOld = root.getFile(path);
                return fileOld.exists();
            }

            public void redo()
            {
                /*
                 * We rename the file with the extension ".old.requirement". If a file .old.requirement already exists,
                 * we rename it .old1.requirement and so over.
                 */
                // find biggest X .oldX.requirement existing file
                int seniorityIndex = 0;
                IFile oldRequirementFile = RequirementUtils.getFile(requirementResource);
                IPath noExtensionPath = oldRequirementFile.getFullPath().removeFileExtension();
                IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                IPath path = noExtensionPath.addFileExtension(AbstractRequirementModelOperation.MODEL_OLD).addFileExtension(RequirementResource.FILE_EXTENSION);
                IFile fileOld = root.getFile(path);
                while (fileOld.exists())
                {
                    seniorityIndex++;
                    path = noExtensionPath.addFileExtension(AbstractRequirementModelOperation.MODEL_OLD + seniorityIndex).addFileExtension(RequirementResource.FILE_EXTENSION);
                    fileOld = root.getFile(path);
                }
                try
                {
                    // rename all requirement files, beginning with biggest index to avoid erasing
                    for (int index = seniorityIndex; index >= 0; index--)
                    {
                        String newSuffix = AbstractRequirementModelOperation.MODEL_OLD;
                        String oldSuffix = null;
                        if (index > 0)
                        {
                            newSuffix += index;
                            oldSuffix = AbstractRequirementModelOperation.MODEL_OLD;
                            if (index > 1)
                            {
                                oldSuffix += index - 1;
                            }
                        }
                        IPath newPath = noExtensionPath.addFileExtension(newSuffix).addFileExtension(RequirementResource.FILE_EXTENSION);
                        IPath oldPath = null;
                        if (oldSuffix != null)
                        {
                            oldPath = noExtensionPath.addFileExtension(oldSuffix).addFileExtension(RequirementResource.FILE_EXTENSION);
                        }
                        else
                        {
                            oldPath = noExtensionPath.addFileExtension(RequirementResource.FILE_EXTENSION);
                        }
                        IFile previousRequirementFile = root.getFile(oldPath);
                        previousRequirementFile.move(newPath, true, new NullProgressMonitor());
                    }
                }
                catch (CoreException e)
                {
                    RequirementCorePlugin.log(e);
                }
            }

            public void execute()
            {
                redo();
            }

            @Override
            public void undo()
            {
                /*
                 * We rename the files having the extension ".oldX.requirement".
                 */
                // find biggest X .oldX.requirement existing file
                // (do not rely on old one, in case changes have been made in WS)
                int seniorityIndex = 0;
                IFile oldRequirementFile = RequirementUtils.getFile(requirementResource);
                IPath noExtensionPath = oldRequirementFile.getFullPath().removeFileExtension();
                IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                IPath path = noExtensionPath.addFileExtension(AbstractRequirementModelOperation.MODEL_OLD).addFileExtension(RequirementResource.FILE_EXTENSION);
                IFile fileOld = root.getFile(path);
                while (fileOld.exists())
                {
                    seniorityIndex++;
                    path = noExtensionPath.addFileExtension(AbstractRequirementModelOperation.MODEL_OLD + seniorityIndex).addFileExtension(RequirementResource.FILE_EXTENSION);
                    fileOld = root.getFile(path);
                }
                try
                {
                    // rename all requirement files, beginning with smallest index to avoid erasing
                    for (int index = 0; index < seniorityIndex; index++)
                    {
                        String oldSuffix = AbstractRequirementModelOperation.MODEL_OLD;
                        String newSuffix = null;
                        if (index > 0)
                        {
                            oldSuffix += index;
                            newSuffix = AbstractRequirementModelOperation.MODEL_OLD;
                            if (index > 1)
                            {
                                newSuffix += index - 1;
                            }
                        }
                        IPath oldPath = noExtensionPath.addFileExtension(oldSuffix).addFileExtension(RequirementResource.FILE_EXTENSION);
                        IPath newPath = null;
                        if (newSuffix != null)
                        {
                            newPath = noExtensionPath.addFileExtension(newSuffix).addFileExtension(RequirementResource.FILE_EXTENSION);
                        }
                        else
                        {
                            newPath = noExtensionPath.addFileExtension(RequirementResource.FILE_EXTENSION);
                        }
                        IFile renamedRequirementFile = root.getFile(oldPath);
                        renamedRequirementFile.move(newPath, true, new NullProgressMonitor());
                    }
                }
                catch (CoreException e)
                {
                    RequirementCorePlugin.log(e);
                }
            }
        };
    }

    /**
     * Get the command to delete the requirement resource
     * 
     * @param requirementResource requirement resource to delete
     * @return the delete command
     */
    private Command getDeleteResourceCommand(final Resource requirementResource)
    {
        return new AbstractCommand()
        {
            @Override
            public boolean canExecute()
            {
                IFile oldRequirementFile = RequirementUtils.getFile(requirementResource);
                return oldRequirementFile.exists();
            }

            public void redo()
            {
                RequirementUtils.deleteResource(requirementResource);
            }

            public void execute()
            {
                redo();
            }

            @Override
            public boolean canUndo()
            {
                return false;
            }
        };
    }

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        Command command = getExecutionCommand();

        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = RequirementUtils.getSpecificServices(editor);
        if (services != null)
        {
            EditingDomain domain = services.getEditingDomain(editor);
            // Execute the command
            if (domain != null && command != null && command.canExecute())
            {
                domain.getCommandStack().execute(command);
            }
        }
        // Notify views that the diagram property has changed
        ((CurrentRequirementView) CurrentRequirementView.getInstance()).partActivated(editor);
        ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).partActivated(editor);
        return null;
    }

    /**
     * @return dialogResult
     */
    public int getDialogResult()
    {
        return dialogResult;
    }
}
