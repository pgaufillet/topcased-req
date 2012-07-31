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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.resource.Resource;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.commands.CommandStub;
import org.topcased.requirement.core.commands.UpdateMonitorCommand;
import org.topcased.requirement.core.extensions.IRequirementTransformation;
import org.topcased.requirement.core.extensions.RequirementTransformationManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.util.RequirementResource;
import org.topcased.typesmodel.model.inittypes.DeletionParameters;

import ttm.Document;

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

    private IFile sourceModelFile; // requirement model already created or not
    
    private Map<Document,Document> documentsToMerge;
    
    private boolean isPartialImport;
    
    private boolean isImpactAnalysis;

    private Map<Document, DeletionParameters> deletionParametersDocMap;

    
    public MergeRequirementModelOperation(IFile targetFile, IFile sourceFile, IFile reqFile, Map<Document,Document> docs, Map<Document, DeletionParameters> deletionParametersDocMap, boolean isPartialImport)
    {
        this(targetFile,sourceFile,reqFile,docs,deletionParametersDocMap,isPartialImport,true);
    }
    
    /**
     * The constructor
     * 
     * @param targetFile
     * @param sourceFile
     * @param isImpactAnalysis 
     * @param destFile
     */
    public MergeRequirementModelOperation(IFile targetFile, IFile sourceFile, IFile reqFile, Map<Document,Document> docs, Map<Document, DeletionParameters> deletionParametersDocMap, boolean isPartialImport, boolean isImpactAnalysis)
    {
        super(targetFile, reqFile);
        sourceModelFile = sourceFile;
        documentsToMerge = docs;
        this.deletionParametersDocMap = deletionParametersDocMap;
        this.isPartialImport = isPartialImport;
        this.isImpactAnalysis = isImpactAnalysis;
    }


    /**
     * Get commands to merge requirement models
     * 
     * @see AbstractRequirementModelOperation#getCommands(IProgressMonitor)
     */
    protected List<Command> getCommands(final IProgressMonitor monitor)
    {
        if (RequirementResource.FILE_EXTENSION.equals(sourceModelFile.getFileExtension()))
        {
            Command startTaskCmd = UpdateMonitorCommand.getCommand(monitor, 0, Messages.getString("MergeRequirementModelOperation.0"), 0, null); //$NON-NLS-1$
            Command worked1Cmd = UpdateMonitorCommand.getCommand(monitor, 1, 0);

            Command merge = new CommandStub()
            {
                public void redo()
                {
                    // Merge the existing requirement model
                    mergeRequirementModel(monitor);
                }
            };
            return Arrays.asList(startTaskCmd, worked1Cmd, merge);

        }
        else
        {
            Command startTaskCmd = UpdateMonitorCommand.getCommand(monitor, 0, Messages.getString("MergeRequirementModelOperation.1"), 0, null); //$NON-NLS-1$

            final IPath mergePath = requirementModelFile.getFullPath().addFileExtension(MODEL_TMP);
            final IFile mergeFile = ResourcesPlugin.getWorkspace().getRoot().getFile(mergePath);

            Command transform = new CommandStub()
            {
                public void redo()
                {
                    // Get the transformation from the requirementTransformation extension point
                    IRequirementTransformation reqTransfo = RequirementTransformationManager.getInstance().getRequirementTransformation(sourceModelFile.getFileExtension());
                    if (reqTransfo != null)
                    {
                        // Process the transformation to create the temporally requirement model
                        reqTransfo.transformation(sourceModelFile, mergeFile);
                    }
                }
            };
            Command worked1Cmd = UpdateMonitorCommand.getCommand(monitor, 1, 0);

            Command merge = new CommandStub()
            {
                public void redo()
                {
                    // Merge the existing requirement model
                    mergeRequirementModel(monitor);
                }
            };
            return Arrays.asList(startTaskCmd, transform, worked1Cmd, merge);
        }
    }

    /**
     * Get the label
     */
    public String getLabel()
    {
        // TODO Auto-generated method stub
        return null;
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
    protected void mergeRequirementModel(IProgressMonitor monitor)
    {
        monitor.beginTask(Messages.getString("MergeRequirementModelOperation.2"), 3); //$NON-NLS-1$      

        monitor.worked(1);

        // merge operation
        mergeOperation(documentsToMerge, deletionParametersDocMap, isPartialImport, isImpactAnalysis, monitor);
    }
}
