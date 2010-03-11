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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.preferences.CurrentPreferenceHelper;
import org.topcased.requirement.core.utils.DefaultAttachmentPolicy;
import org.topcased.requirement.core.utils.RequirementUtils;

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
public abstract class AbstractModelCreationOperation extends WorkspaceModifyOperation
{

    public static final String MODEL_EXTENSION = "requirement"; //$NON-NLS-1$

    protected Resource requirementResource;

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
    public AbstractModelCreationOperation(IFile targetFile, IFile reqFile)
    {
        targetModelFile = targetFile;
        requirementModelFile = reqFile;
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
     *
     * Updates into the target model the reference to the requirement model
     */
    protected void updateRequirementReference(IProgressMonitor monitor)
    {
        monitor.subTask("updating references in target model");
        
        Resource targetModelResource = RequirementUtils.getResource(targetModelFile.getFullPath());
        
        EObject root = targetModelResource.getContents().get(0);
        String uri = EcoreUtil.getURI(root.eClass().getEPackage()).trimFragment().toString();
        IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(uri);
        
        if (policy != null)
        {            
            policy.linkRequirementModel(targetModelResource, requirementResource);         
        } 
        else
        {
            DefaultAttachmentPolicy.getInstance().linkRequirementModel(targetModelResource, requirementResource);
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
