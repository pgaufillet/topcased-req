/*****************************************************************************
 * Copyright (c) 2010 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Vincent Hemery (Atos Origin}) {vincent.hemery@atosorigin.com} - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.bundle.papyrus.resource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.papyrus.resource.IModel;
import org.eclipse.papyrus.resource.ModelException;
import org.eclipse.papyrus.resource.ModelSet;
import org.eclipse.papyrus.resource.sasheditor.SashModel;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.bundle.papyrus.commands.LinkRequirementModelCommand;
import org.topcased.requirement.bundle.papyrus.commands.UnlinkRequirementModelCommand;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;

/**
 * Defines the specific policy for requirement attachment of Papyrus diagram models
 * 
 * @author vhemery
 * @see IModelAttachmentPolicy
 */
public class PapyrusAttachmentPolicy implements IModelAttachmentPolicy
{

    /**
     * Sets a link between a requirement model and a target model
     * 
     * @param targetModel the target model
     * @param requirementModelPath the requirement model path
     * @return Command, the emf command that will be executed in order to link the requirement model
     */
    public Command linkRequirementModel(Resource targetModel, IPath requirementModelPath)
    {
        return new LinkRequirementModelCommand(targetModel, requirementModelPath);
    }

    /**
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#unlinkRequirementModel(org.eclipse.emf.ecore.resource.Resource,
     *      IPath)
     */
    public Command unlinkRequirementModel(Resource targetModel, IPath requirementModelPath)
    {
        return new UnlinkRequirementModelCommand(targetModel, requirementModelPath);
    }

    /**
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#getLinkedTargetModel(org.eclipse.emf.edit.domain.EditingDomain)
     */
    public Resource getLinkedTargetModel(ResourceSet resourceSet)
    {
        if (resourceSet instanceof ModelSet)
        {
            // check that requirements exist
            IModel reqModel = ((ModelSet) resourceSet).getModel(RequirementModel.REQ_MODEL_ID);
            if (reqModel instanceof RequirementModel == false || ((RequirementModel) reqModel).getResource() == null)
            {
                return null;
            }
            // return di model resource
            IModel model = ((ModelSet) resourceSet).getModel(SashModel.MODEL_ID);
            if (model instanceof SashModel)
            {
                return ((SashModel) model).getResource();
            }
        }
        return null;
    }

    /**
     * Gets the Requirement project from the target main resource (usually a "di" resource)
     * 
     * @param mainResource the main resource attached to a requirement model
     * @return the requirement project
     */
    public RequirementProject getRequirementProjectFromTargetMainResource(Resource mainResource)
    {
        ResourceSet resourceSet = mainResource.getResourceSet();
        if (resourceSet instanceof ModelSet)
        {
            // check that requirements exist
            IModel reqModel = ((ModelSet) resourceSet).getModel(RequirementModel.REQ_MODEL_ID);
            if (reqModel instanceof RequirementModel)
            {
                Resource reqRes = ((RequirementModel) reqModel).getResource();
                if (reqRes == null)
                {
                    // try loading the model
                    IFile file = null;
                    URI uriWitExt = mainResource.getURI();
                    String fileName = uriWitExt.toFileString();
                    if (fileName != null)
                    {
                        file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(fileName));
                    }
                    if (uriWitExt.toString().startsWith("platform:/resource")) { //$NON-NLS-1$
                        String path = uriWitExt.toString().substring("platform:/resource".length()); //$NON-NLS-1$
                        IResource workspaceResource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path));
                        if (workspaceResource instanceof IFile)
                        {
                            file = (IFile) workspaceResource;
                        }
                    }
                    if (file != null)
                    {
                        try
                        {
                            reqModel = ((ModelSet) resourceSet).importModel(RequirementModel.REQ_MODEL_ID, file);
                        }
                        catch (ModelException e)
                        {
                        }
                        reqRes = ((RequirementModel) reqModel).getResource();
                    }
                }
                if (reqRes != null && reqRes.getContents().size() > 0 && reqRes.getContents().get(0) instanceof RequirementProject)
                {
                    return (RequirementProject) reqRes.getContents().get(0);
                }
            }
        }
        return null;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#isHierarchicalElementRoot(org.eclipse.emf.ecore.EObject)
     */
    public boolean isRootContainer(EObject parentElt)
    {
        return parentElt == null;
    }
}
