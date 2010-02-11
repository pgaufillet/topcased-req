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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.preferences.CurrentPreferenceHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.sam.Model;

/**
 * 
 * This class provides abstract operation to create the requirement model.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * 
 * @since 2.1.0
 * 
 */
public abstract class AbstractModelCreationOperation extends WorkspaceModifyOperation
{

    public static final String MODEL_EXTENSION = "requirement"; //$NON-NLS-1$

    protected Resource requirementResource;

    protected IFile samModelFile; // Sam model

    protected IFile destModelFile; // Requirement model to create

    private String projectName;

    private String projectDescription;

    /**
     * Constructor
     * 
     * @param samFile The SAM mode l
     * @param destFile The Requirement model to create
     */
    public AbstractModelCreationOperation(IFile samFile, IFile destFile)
    {
        samModelFile = samFile;
        destModelFile = destFile;
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
     * Updates into the SAM model the reference to the requirement model
     */
    protected void updateRequirementReference(IProgressMonitor monitor)
    {
        monitor.subTask("updating references in SAM model");
        // Close the corresponding diagram if open
        IPath diagramFile = samModelFile.getFullPath().removeFileExtension().addFileExtension("samdi");
        boolean closed = RequirementUtils.closeSAMDiagramEditor(diagramFile);

        Resource samResource = RequirementUtils.getResource(samModelFile.getFullPath());
        Model root = (Model) RequirementUtils.getRoot(samResource, Model.class);
        EObject reqObject = RequirementUtils.getRoot(requirementResource, RequirementProject.class);
        root.setRequirementModel(reqObject);

        // Save the contents of the resource to the file system.
        RequirementUtils.saveResource(samResource);

        // The diagram is re-opened if needed.
        if (closed)
        {
            RequirementUtils.openSAMDiagramEditor(diagramFile);
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

};
