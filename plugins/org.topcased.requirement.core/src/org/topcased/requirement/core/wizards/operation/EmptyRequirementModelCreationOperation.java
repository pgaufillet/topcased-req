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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * 
 * This class provides operation to create an empty requirement model
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * 
 * @since 2.1.0
 */
public class EmptyRequirementModelCreationOperation extends AbstractModelCreationOperation
{
    /**
     * The constructor
     * 
     * @param sourceFile
     * @param destFile
     */
    public EmptyRequirementModelCreationOperation(IFile inFile, IFile destFile)
    {
        super(inFile, destFile);
    }

    /**
     * @see org.eclipse.ui.actions.WorkspaceModifyOperation#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    protected void execute(IProgressMonitor monitor)
    {
        // Create the empty requirement model
        monitor.beginTask("Requirement Model Creation : ", 4);
        createEmptyRequirementModel(monitor);

        // Update the model
        updateRequirementReference(monitor);
    }

    /**
     * Creates an empty requirement model.
     * 
     * @param monitor The progress monitor
     */
    protected void createEmptyRequirementModel(IProgressMonitor monitor)
    {
        monitor.subTask("creating empty requirement model");
        ResourceSet resourceSet = new ResourceSetImpl();
        URI fileURI = URI.createPlatformResourceURI(requirementModelFile.getFullPath().addFileExtension(MODEL_EXTENSION).toString(), true);
        requirementResource = resourceSet.createResource(fileURI);
        monitor.worked(1);

        // Add the initial model object to the contents.
        RequirementProject newProject = RequirementFactory.eINSTANCE.createRequirementProject();
        requirementResource.getContents().add(newProject);


        RequirementProject rootObject = (RequirementProject) RequirementUtils.getRoot(requirementResource, RequirementProject.class);
        updateRequirementProject(rootObject);

        // additional operation
        rootObject.setUpstreamModel(RequirementFactory.eINSTANCE.createUpstreamModel());
        rootObject.getChapter().add(RequirementFactory.eINSTANCE.createProblemChapter());
        rootObject.getChapter().add(RequirementFactory.eINSTANCE.createTrashChapter());
        rootObject.getChapter().add(RequirementFactory.eINSTANCE.createUntracedChapter());
        createAttributeConfiguration(rootObject);
        
        monitor.worked(1);

        // Save the contents of the resource to the file system
        RequirementUtils.saveResource(requirementResource);
        monitor.worked(1);
    }

    /**
     * Creates an Requirement model with an empty upstream model
     */
    protected void createEmptyModel()
    {

    }

}
