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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.extensions.DefaultAttachmentPolicy;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.IRequirementTransformation;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.extensions.RequirementTransformationManager;
import org.topcased.requirement.core.handlers.UnlinkRequirementModelHandler;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;

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

    private static final String MODEL_OLD = "old"; //$NON-NLS-1$

    private IFile sourceModelFile; //requirement model already created or not

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
     * @see org.eclipse.ui.actions.WorkspaceModifyOperation#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    protected void execute(IProgressMonitor monitor)
    {
        EditingDomain domain = null;
        Modeler modeler = Utils.getCurrentModeler();
        if (modeler != null)
        {
            domain = modeler.getEditingDomain();
        }  
        
        // Deals with source model file extension and the fact that the target model could already have a requirement project attached
        if (sourceModelFile.getFileExtension().equals("requirement"))
        {
            if (isCurrentModelerAlreadyAttached(domain))
            {
                unlinkAndCreate(domain, monitor, false);
            }
            else
            {
                copyAndCreate(monitor);
            }
        }
        else
        {
            if (isCurrentModelerAlreadyAttached(domain))
            {
                unlinkAndCreate(domain, monitor, true);
            }
            else
            {
                copyAndCreate(monitor);
            }
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
    protected void newRequirementModel(IProgressMonitor monitor)
    {
        monitor.beginTask("Creation : ", 4);

        // Get a resource of the destination file
        requirementResource = RequirementUtils.getResource(requirementModelFile.getFullPath().addFileExtension(MODEL_EXTENSION));

        // Add the initial model object to the contents
        createInitialModel(requirementResource);
        monitor.worked(1);

        // Save the contents of the resource to the file system
        RequirementUtils.saveResource(requirementResource);
        monitor.worked(1);

        // Update the target model
        updateRequirementReference(monitor);
        monitor.worked(1);
    }

    /**
     * Copy and rename the source model file (with .requirement extension) and create the requirement model
     * 
     * @param monitor The progress monitor to use
     */
    protected void copyAndCreate(IProgressMonitor monitor)
    {
        IFile fileDest = ResourcesPlugin.getWorkspace().getRoot().getFile(requirementModelFile.getFullPath().addFileExtension(MODEL_EXTENSION));
        
        //If the requirement model file is not close to the target model file
        if (!fileDest.exists())
        {
            monitor.subTask("Copy requirement file ");
            // rename the file from the name given in the dialog and copy it next to the target model
            try
            {
                sourceModelFile.copy(requirementModelFile.getFullPath().addFileExtension(MODEL_EXTENSION), true, monitor);
            }
            catch (CoreException e)
            {
                RequirementCorePlugin.log(e);
            }
            monitor.worked(1);
        }

        // Create the requirement model
        newRequirementModel(monitor);
    }

    /**
     * Unlink the already attached requirement model and create the new requirement model for the source model file
     * 
     * @param domain The editing domain of the requirement project
     * @param monitor The progress monitor to use
     * @param toTransform if the source model file has to be transformed into a .requirement file
     */
    protected void unlinkAndCreate(EditingDomain domain, IProgressMonitor monitor, boolean toTransform)
    {
        monitor.subTask("Unlink Old Requirement File ");

        Resource oldRequirementResource = RequirementUtils.getRequirementModel(domain);
        IFile oldRequirementFile = RequirementUtils.getFile(oldRequirementResource);

        //Launch the unlink model action via the handler of this action
        UnlinkRequirementModelHandler unlinkAction = new UnlinkRequirementModelHandler();
        try
        {
            unlinkAction.execute(null);
        }
        catch (ExecutionException e)
        {
            RequirementCorePlugin.log(e);
        }

        //Cancel has not been pressed
        if (unlinkAction.getDialogResult() != 1)
        {
            // The file has not been deleted by the user, we need to rename it
            if (unlinkAction.getDialogResult() == 0)
            {
                monitor.subTask("Rename Old Requirement File ");
                
                // rename the file to add with the extension ".old.requirement"
                IPath path = oldRequirementFile.getFullPath().removeFileExtension().addFileExtension(MODEL_OLD); 
                try
                {
                    oldRequirementFile.move(path.addFileExtension(MODEL_EXTENSION), true, monitor);
                }
                catch (CoreException e)
                {
                    RequirementCorePlugin.log(e);
                }
                monitor.worked(1);
            }
    
            // Deals with the source model file extension
            if (toTransform)
            {
                transformAndCreate(monitor);
            }
            else
            {
                copyAndCreate(monitor);
            }
        }

    }

    /**
     * Process the transformation of the source model file and create the requirement model
     * 
     * @param monitor The progress monitor to use
     */
    protected void transformAndCreate(IProgressMonitor monitor)
    {
        monitor.subTask("Importing requirement model");
        // Get the transformation from the requirementTransformation extension point
        IRequirementTransformation reqTransfo = RequirementTransformationManager.getInstance().getRequirementTransformation(sourceModelFile.getFileExtension());
        if (reqTransfo != null)
        {
            // Process the transformation to create the requirement model
            reqTransfo.transformation(sourceModelFile, requirementModelFile);
        }
        monitor.worked(1);

        // Create the requirement model
        newRequirementModel(monitor);
    }
    
    /**
     * Check if the current modeler is already attached to a requirement model
     * 
     * @return true if already attached, false otherwise
     */
    protected boolean isCurrentModelerAlreadyAttached(EditingDomain domain)
    {
        Resource linkedTargetModel = null;
        
        //Get the attachment policy for the target model file
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
            linkedTargetModel = DefaultAttachmentPolicy.getInstance().getLinkedTargetModel(domain.getResourceSet());
            if (linkedTargetModel != null)
            {
                return RequirementUtils.getFile(linkedTargetModel).equals(targetModelFile);
            }           
        }
        return false;
    }

}
