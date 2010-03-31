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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.extensions.IRequirementTransformation;
import org.topcased.requirement.core.extensions.RequirementTransformationManager;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * 
 * This class provides operations to merge requirement models
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 * @since 2.1.0
 * 
 */
public class MergeRequirementModelOperation extends AbstractRequirementModelOperation
{
    private static final String MODEL_TMP = "tmp"; //$NON-NLS-1$

    private IFile sourceModelFile; //requirement model already created or not

    /**
     * The constructor
     * 
     * @param targetFile
     * @param sourceFile
     * @param destFile
     */
    public MergeRequirementModelOperation(IFile targetFile, IFile sourceFile, IFile reqFile)
    {
        super(targetFile, reqFile);
        sourceModelFile = sourceFile;
    }

    /**
     * @see org.eclipse.ui.actions.WorkspaceModifyOperation#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    protected void execute(IProgressMonitor monitor)
    {

        if (sourceModelFile.getFileExtension().equals("requirement"))
        {
            monitor.subTask("Copy requirement file ");
            // rename the file from the name given in the dialog and temporally copy it next to the target model
            IPath mergePath = requirementModelFile.getFullPath().addFileExtension(MODEL_TMP);
            try
            {
                sourceModelFile.copy(mergePath.addFileExtension(MODEL_EXTENSION), true, monitor);
            }
            catch (CoreException e)
            {
                RequirementCorePlugin.log(e);
            }
            monitor.worked(1);

            // Merge the existing requirement model
            mergeRequirementModel(mergePath, monitor);

        }
        else
        {
            monitor.subTask("Importing requirement model");

            IPath mergePath = requirementModelFile.getFullPath().addFileExtension(MODEL_TMP);
            IFile mergeFile = ResourcesPlugin.getWorkspace().getRoot().getFile(mergePath);

            // Get the transformation from the requirementTransformation extension point
            IRequirementTransformation reqTransfo = RequirementTransformationManager.getInstance().getRequirementTransformation(sourceModelFile.getFileExtension());
            if (reqTransfo != null)
            {
                // Process the transformation to create the temporally requirement model
                reqTransfo.transformation(sourceModelFile, mergeFile);
            }
            monitor.worked(1);

            // Merge the existing requirement model
            mergeRequirementModel(mergePath, monitor);
        }
    }

    /**
     * Creates a Requirement model
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
     * Merges two models of requirements
     * 
     * @param monitor The progress monitor to use
     */
    protected void mergeRequirementModel(IPath mergePath, IProgressMonitor monitor)
    {
        monitor.beginTask("Update : ", 3);

        // Get a resource of the destination file
        requirementResource = RequirementUtils.getResource(requirementModelFile.getFullPath().addFileExtension(MODEL_EXTENSION));

        // Get the resource to update/merge
        Resource requirementResourceMerged = RequirementUtils.getResource(mergePath.addFileExtension(MODEL_EXTENSION));

        // Add the initial model object to the contents
        createInitialModel(requirementResourceMerged);
        monitor.worked(1);

        // merge operation
        mergeOperation(requirementResourceMerged, monitor);

        // Delete the temporary model
        monitor.subTask("deleting temporary file");
        Resource toDelete = RequirementUtils.getResource(mergePath.addFileExtension(MODEL_EXTENSION));
        RequirementUtils.deleteResource(toDelete);
        monitor.worked(1);
    }
}
