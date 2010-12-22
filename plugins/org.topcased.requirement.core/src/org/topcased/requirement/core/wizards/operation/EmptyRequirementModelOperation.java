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

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.commands.UpdateMonitorCommand;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.util.RequirementResource;

/**
 * 
 * This class provides operation to create an empty requirement model
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * 
 * @since 2.1.0
 */
public class EmptyRequirementModelOperation extends AbstractRequirementModelOperation
{
    /**
     * The constructor
     * 
     * @param sourceFile
     * @param destFile
     */
    public EmptyRequirementModelOperation(IFile inFile, IFile destFile)
    {
        super(inFile, destFile);
    }

    /**
     * Get commands to create empty requirement model
     * 
     * @see AbstractRequirementModelOperation#getCommands(IProgressMonitor)
     */
    protected List<Command> getCommands(final IProgressMonitor monitor)
    {
        Command startTaskCmd = UpdateMonitorCommand.getCommand(monitor, getLabel(), 4, null, -1);
        Command cmdWS = new AbstractCommand()
        {
            public void redo()
            {
                // Create the empty requirement model
                createEmptyRequirementModel(monitor);
            }

            public void execute()
            {
                redo();
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
     * Get the label
     */
    public String getLabel()
    {
        return Messages.getString("EmptyRequirementModelOperation.0"); //$NON-NLS-1$
    }

    /**
     * Creates an empty requirement model.
     * 
     * @param monitor The progress monitor
     */
    protected void createEmptyRequirementModel(IProgressMonitor monitor)
    {
        monitor.subTask(Messages.getString("EmptyRequirementModelOperation.1")); //$NON-NLS-1$
        ResourceSet resourceSet = new ResourceSetImpl();
        URI fileURI = URI.createPlatformResourceURI(requirementModelFile.getFullPath().addFileExtension(RequirementResource.FILE_EXTENSION).toString(), true);
        setRequirementResource(resourceSet.createResource(fileURI));
        monitor.worked(1);

        // Add the initial model object to the contents.
        RequirementProject newProject = RequirementFactory.eINSTANCE.createRequirementProject();
        getRequirementResource().getContents().add(newProject);

        RequirementProject rootObject = (RequirementProject) RequirementUtils.getRoot(getRequirementResource(), RequirementProject.class);
        updateRequirementProject(rootObject);

        // additional operation
        rootObject.setUpstreamModel(RequirementFactory.eINSTANCE.createUpstreamModel());
        rootObject.getChapter().add(RequirementFactory.eINSTANCE.createProblemChapter());
        rootObject.getChapter().add(RequirementFactory.eINSTANCE.createTrashChapter());
        rootObject.getChapter().add(RequirementFactory.eINSTANCE.createUntracedChapter());
        createAttributeConfiguration(rootObject);

        monitor.worked(1);

        // Save the contents of the resource to the file system
        RequirementUtils.saveResource(getRequirementResource());
        monitor.worked(1);
    }

}
