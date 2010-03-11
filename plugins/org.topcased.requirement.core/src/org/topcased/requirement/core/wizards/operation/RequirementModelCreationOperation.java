/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.wizards.operation;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.utils.MergeRequirement;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.sam.requirement.service.TtmToReqImportService;

/**
 * 
 * This class provides operation to create the requirement model
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * 
 * @since 2.1.0
 * 
 */
public class RequirementModelCreationOperation extends AbstractModelCreationOperation
{
    private static final String MODEL_TMP = "tmp"; //$NON-NLS-1$

    private IFile sourceModelFile; // Ttm model

    /**
     * The constructor
     * 
     * @param targetFile
     * @param sourceFile
     * @param destFile
     */
    public RequirementModelCreationOperation(IFile targetFile, IFile sourceFile, IFile reqFile)
    {
        super(targetFile, reqFile);
        sourceModelFile = sourceFile;
    }

    /**
     * @see org.eclipse.ui.actions.WorkspaceModifyOperation#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    protected void execute(IProgressMonitor monitor)
    {
        IFile fileDest = ResourcesPlugin.getWorkspace().getRoot().getFile(requirementModelFile.getFullPath().addFileExtension(MODEL_EXTENSION));
        boolean requirementMerge = fileDest.exists();
        if (requirementMerge)
        {
            // Merge the existing requirement model
            mergeRequirementModel(monitor);
        }
        else
        {
            // Create the requirement model
            newRequirementModel(monitor);
        }
    }

    /**
     * Creates a new Requirement model Process an ATL transformation to import the the model of upstream requirements
     * 
     * @param monitor The progress monitor to use
     */
    protected void newRequirementModel(IProgressMonitor monitor)
    {
        monitor.beginTask("Creation : ", 5);
        // Process the ATL transformation to create the requirement model
        monitor.subTask("importing requirement model");
        monitor.worked(1);
        transformation(sourceModelFile, requirementModelFile);
        monitor.worked(1);

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
     * @param source : the model of upstream requirements
     * @param dest : the requirement model to create
     */
    protected void mergeRequirementModel(IProgressMonitor monitor)
    {
        monitor.beginTask("Update : ", 5);
        // 1) import operation of temporary model
        monitor.subTask("importing requirement model");
        monitor.worked(1);
        IPath mergePath = requirementModelFile.getFullPath().addFileExtension(MODEL_TMP);
        IFile mergeFile = ResourcesPlugin.getWorkspace().getRoot().getFile(mergePath);

        // Process the ATL transformation to create the requirement model
        transformation(sourceModelFile, mergeFile);

        // Get a resource of the destination file
        requirementResource = RequirementUtils.getResource(requirementModelFile.getFullPath().addFileExtension(MODEL_EXTENSION));

        // Get the resource to update/merge
        Resource requirementResourceMerged = RequirementUtils.getResource(mergePath.addFileExtension(MODEL_EXTENSION));

        // Add the initial model object to the contents
        createInitialModel(requirementResourceMerged);
        monitor.worked(1);

        // 2) merge operation
        monitor.subTask("merging requirement model");
        try
        {
            // Close the corresponding diagram if open
            IPath diagramFile = targetModelFile.getFullPath();
            if (!diagramFile.getFileExtension().endsWith("di"))
            {
                diagramFile = diagramFile.removeFileExtension().addFileExtension(diagramFile.getFileExtension() + "di");
            }
            boolean closed = RequirementUtils.closeDiagramEditor(diagramFile);

            // Call the EMF comparison service in order to merge/update the current requirement model
            MergeRequirement.INSTANCE.merge(requirementResource, requirementResourceMerged, monitor);
            monitor.worked(1);

            // Save the contents of the resource to the file system
            RequirementUtils.saveResource(requirementResource);

            // The diagram is re-opened if needed.
            if (closed)
            {
                RequirementUtils.openDiagramEditor(diagramFile);
            }
        }
        catch (InterruptedException e)
        {
            RequirementCorePlugin.log(e);
        }
        monitor.worked(1);

        // 3) Delete the temporary model
        monitor.subTask("deleting temporary file");
        Resource toDelete = RequirementUtils.getResource(mergePath.addFileExtension(MODEL_EXTENSION));
        RequirementUtils.deleteResource(toDelete);
        monitor.worked(1);
    }

    /**
     * 
     * Processes the ATL transformation to create the requirement model.
     * 
     * @param source : IFile to the ttm source model
     * @param dest : IFile to the requirement destination model
     */
    private void transformation(IFile source, IFile dest)
    {
        TtmToReqImportService service;

        Map<String, Object> parameters = new HashMap<String, Object>();

        // The absolute path to the ttm model
        String inPath = source.getLocation().toOSString();

        // The requirement destination model
        String outPath = dest.getName();

        // The workspace destination
        IPath path = dest.getParent().getFullPath(); // destination dans le workspace

        parameters.put("IN", inPath);
        parameters.put("OUT", outPath);
        parameters.put("Path", path);

        service = new TtmToReqImportService();
        service.serviceRun(parameters);
    }
}
