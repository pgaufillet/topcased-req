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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.commands.RefreshRequirementsPropertiesCommand;
import org.topcased.requirement.core.commands.UpdateMonitorCommand;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.preferences.CurrentPreferenceHelper;
import org.topcased.requirement.core.utils.MergeRequirement;
import org.topcased.requirement.core.utils.RequirementDifferenceCalculator;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.utils.impact.MergeImpactProcessor;
import org.topcased.requirement.util.RequirementResource;

import ttm.Document;

/**
 * 
 * This class provides abstract operation to create the requirement model.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 * @since 2.1.0
 * 
 */
public abstract class AbstractRequirementModelOperation extends WorkspaceModifyOperation
{
    public static final String MODEL_OLD = "old"; //$NON-NLS-1$

    private Resource requirementResource;

    protected IFile targetModelFile; // target model

    protected IFile requirementModelFile; // Requirement model to create

    private String projectName;

    private String projectDescription;

    /**
     * Constructor
     * 
     * @param targetFile The target model
     * @param destFile The Requirement model to create
     */
    public AbstractRequirementModelOperation(IFile targetFile, IFile reqFile)
    {
        targetModelFile = targetFile;
        requirementModelFile = reqFile;
    }
    
    public AbstractRequirementModelOperation() {
        
    }

    /**
     * Sets project information coming from the wizard page.
     * 
     * @param name The name of the project
     * @param desc The short description of the project
     */
    public void setProjectInformations(String name, String desc)
    {
        projectName = name;
        projectDescription = desc;
    }

    /**
     * Execute all modifications in a single command executed in the editor's command stack
     * 
     * @param monitor the progress monitor to use to display progress and field user requests to cancel
     * @see org.eclipse.ui.actions.WorkspaceModifyOperation#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException
    {
        EditingDomain domain = null;
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices service = RequirementUtils.getSpecificServices(editor);
        if (service != null)
        {
            domain = service.getEditingDomain(editor);
        }
        if (domain != null)
        {
            execute(monitor, domain, editor);
        }
    }

    /**
     * @param monitor
     * @param domain
     * @param editor
     */
    protected void execute(IProgressMonitor monitor, EditingDomain domain, IEditorPart editor)
    {
        List<Command> cmds = getCommands(monitor);
        // Add commands to notify views that the diagram property has changed
        List<Command> commands = new ArrayList<Command>(cmds.size() + 2);
        commands.add(RefreshRequirementsPropertiesCommand.getAtUndo(editor));
        commands.addAll(cmds);
        commands.add(RefreshRequirementsPropertiesCommand.getAtRedo(editor));
        // execute global command
        CompoundCommand command = new CompoundCommand(getLabel(), commands);
        if (command.canExecute())
        {
            domain.getCommandStack().execute(command);
        }
    }

    /**
     * Return the operation's label
     * 
     * @return label to display
     */
    abstract public String getLabel();

    /**
     * Get the commands to execute to update model and workspace
     * 
     * @param monitor the progress monitor commands must update
     * @return list of commands to execute
     */
    abstract protected List<Command> getCommands(final IProgressMonitor monitor);

    /**
     * 
     * Updates into the target model the reference to the requirement model
     */
    protected Command getUpdateRequirementReferenceCmd(final IProgressMonitor monitor)
    {
        Command cmd = null;
        cmd = UpdateMonitorCommand.getCommand(monitor, 0, Messages.getString("AbstractRequirementModelOperation.0"), -1, null); //$NON-NLS-1$
        IModelAttachmentPolicy policy = null;
        Resource targetModelResource = RequirementUtils.getResource(targetModelFile.getFullPath());

        // Get the policy from the file extension of the target resource
        String fileExtension = targetModelResource.getURI().fileExtension();
        policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(fileExtension);

        // Link the model to the requirement model
        if (policy != null)
        {
            cmd = cmd.chain(policy.linkRequirementModel(targetModelResource, getRequirementResourcePath()));
        }
        else
        {
            String msg = NLS.bind(Messages.getString("ModelAttachmentPolicyManager.0"), fileExtension);
            RequirementCorePlugin.log(msg, Status.ERROR, null);//$NON-NLS-1$
            cmd = UnexecutableCommand.INSTANCE;
        }

        // update monitor
        cmd = cmd.chain(UpdateMonitorCommand.getCommand(monitor, 1, 0));
        return cmd;
    }

    /**
     * 
     * Process the merge operation
     * @param isImpactAnalysis 
     * @param tempURI 
     */
    protected void mergeOperation(Map<Document,Document> documentsToMerge,boolean isPartialImport, boolean isImpactAnalysis, IProgressMonitor monitor)
    {
        monitor.subTask(Messages.getString("AbstractRequirementModelOperation.1")); //$NON-NLS-1$
        try
        {
            // Close the corresponding diagram if open
            IPath diagramFile = targetModelFile.getFullPath();
            if (!diagramFile.getFileExtension().endsWith("di")) //$NON-NLS-1$
            {
                diagramFile = diagramFile.removeFileExtension().addFileExtension(diagramFile.getFileExtension() + "di"); //$NON-NLS-1$
            }
            // Call the EMF comparison service in order to merge/update the current requirement model
            IEditorPart editor = RequirementUtils.getCurrentEditor();
            IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
            Resource requirementResource2 = null;
            if (services == null)
            {
                // TODO ERROR
            }
            EditingDomain domain = services.getEditingDomain(editor);
            requirementResource2 = domain.getResourceSet().getResource(
                    URI.createPlatformResourceURI(requirementModelFile.getFullPath().addFileExtension(RequirementResource.FILE_EXTENSION).toString(), true), true);
            List<Document> upstreamDocuments = RequirementUtils.getUpstreamDocuments(requirementResource2);
            Set<URI> resources = new HashSet<URI>();
            Map<Document,Document> mergedDocuments = new HashMap<Document, Document>();
            for (int i = 0; i < upstreamDocuments.size(); i++)
            {
                Document d = upstreamDocuments.get(i);
                for (Document d2 : documentsToMerge.keySet())
                {
                    if (d.getIdent().equals(d2.getIdent()))
                    {
                        Document currentRoot = d;
                        Document mergeRoot = documentsToMerge.get(d2);
                        mergedDocuments.put(mergeRoot, currentRoot);
                        // it seems it is useless but it is a set so it is okay
                        resources.add(currentRoot.eResource().getURI());
                        resources.add(requirementResource2.getURI());
                    }
                }
            }
            RequirementDifferenceCalculator calculator = new RequirementDifferenceCalculator(mergedDocuments, isPartialImport);
            calculator.calculate(monitor);
            if (isImpactAnalysis) {
                new MergeImpactProcessor(resources, requirementResource2.getResourceSet(), calculator).processImpact();
            }
            MergeRequirement.INSTANCE.merge(calculator,isPartialImport,monitor);
            RequirementProject rp = RequirementUtils.getRequirementProject(requirementResource2);
            rp.setIdentifier(projectName);
            rp.setShortDescription(projectDescription);
            monitor.worked(1);

        }
        catch (InterruptedException e)
        {
            RequirementCorePlugin.log(e);
        }
        monitor.worked(1);
    }
    
    /**
     * Updates the RequirementProject object in the requirement model
     * 
     * @param requirementProject : the requirement project to update
     */
    protected void updateRequirementProject(RequirementProject requirementProject)
    {
        requirementProject.setIdentifier(projectName);
        requirementProject.setShortDescription(projectDescription);
    }

    /**
     * Creates the Attribute configuration
     * 
     * @param rootObject : the EObject corresponding to the RequirementProject object of the requirement model
     */
    protected void createAttributeConfiguration(RequirementProject rootObject)
    {
        AttributeConfiguration attrConfiuration = CurrentPreferenceHelper.getConfigurationInWorkspace();
        rootObject.setAttributeConfiguration(attrConfiuration);
    }

    /**
     * Get the requirement resource path
     * 
     * @return resource path
     */
    protected IPath getRequirementResourcePath()
    {
        return requirementModelFile.getFullPath().addFileExtension(RequirementResource.FILE_EXTENSION);
    }

    /**
     * Get the requirement resource
     * 
     * @return initialized requirement resource
     */
    protected Resource getRequirementResource()
    {
        if (requirementResource == null)
        {
            requirementResource = RequirementUtils.getResource(requirementModelFile.getFullPath().addFileExtension(RequirementResource.FILE_EXTENSION));
        }
        return requirementResource;
    }

    /**
     * Set the requirement resource if not initialized yet
     * 
     * @param resource initializing value
     * @return initialized requirement resource
     */
    protected Resource setRequirementResource(Resource resource)
    {
        if (requirementResource == null)
        {
            requirementResource = resource;
        }
        return requirementResource;
    }
};
