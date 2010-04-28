/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/
package org.topcased.requirement.sam.policies;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.topcased.modeler.diagrams.model.Diagrams;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.sam.Model;

/**
 * Defines the specific policy for requirement attachment of SAM models
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * @see IModelAttachmentPolicy
 */
public class SAMAttachmentPolicy implements IModelAttachmentPolicy
{

    /**
     * FIXME: Find a better way to refresh the modeler (the goal of the refresh is to pass in the RequirementAdapterFactory) : see line 61 and 75
     * 
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#linkRequirementModel(org.eclipse.emf.ecore.resource.Resource,
     *      org.eclipse.emf.ecore.resource.Resource)
     */
    public void linkRequirementModel(Resource targetModel, Resource requirementModel)
    {
        // Gather the SAM Model from the SAM diagram
        String uri = null;
        EObject root = targetModel.getContents().get(0);
        if (root instanceof Diagrams)
        {
            Diagrams di = (Diagrams) root;
            uri = EcoreUtil.getURI(di.getModel()).trimFragment().toString();
        }
        Resource samModel = targetModel.getResourceSet().getResource(URI.createURI(uri), true);

        // Try to close the SAM diagram
        IPath samDiagramPath = RequirementUtils.getPath(targetModel);
        boolean closed = RequirementUtils.closeDiagramEditor(samDiagramPath);

        // set the link between the SAM model and the requirement model
        Model rootModel = (Model) RequirementUtils.getRoot(samModel, Model.class);
        EObject reqObject = RequirementUtils.getRoot(requirementModel, RequirementProject.class);
        rootModel.setRequirementModel(reqObject);

        // save the SAM models
        RequirementUtils.saveResource(samModel);
        RequirementUtils.saveResource(targetModel);

        // The SAM diagram is re-opened if needed.
        if (closed)
        {
            RequirementUtils.openDiagramEditor(samDiagramPath);
        }
    }

    /**
     * FIXME: Find a better way to refresh the modeler (the goal of the refresh is to pass in the RequirementAdapterFactory) : see line 113
     * 
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#unlinkRequirementModel(org.eclipse.emf.ecore.resource.Resource,
     *      org.eclipse.emf.ecore.resource.Resource)
     */
    public void unlinkRequirementModel(Resource targetModel, Resource requirementModel, boolean deleteRequirementModel)
    {
        // Get the current modeler
        Modeler modeler = Utils.getCurrentModeler();
        
        // Gather the SAM Model from the SAM diagram
        String uri = null;
        EObject root = targetModel.getContents().get(0);
        if (root instanceof Diagrams)
        {
            Diagrams di = (Diagrams) root;
            uri = EcoreUtil.getURI(di.getModel()).trimFragment().toString();
        }
        Resource samModel = targetModel.getResourceSet().getResource(URI.createURI(uri), true);

        // set the link between the SAM model and the requirement model to null
        Model rootModel = (Model) samModel.getContents().get(0);
        rootModel.setRequirementModel(null);

        // save the graphical SAM model
        RequirementUtils.saveResource(samModel);
        RequirementUtils.saveResource(targetModel);

        // unload and delete the requirement model from file system.
        if (RequirementUtils.unloadRequirementModel(modeler.getEditingDomain()))
        {
            if (deleteRequirementModel)
            {
                RequirementUtils.deleteResource(requirementModel);
            }
        }
        
        //Refresh the diagram (for now by closing and re-opening it) to adapt the views
        IPath diagramFile = RequirementUtils.getPath(targetModel);
        boolean closed = RequirementUtils.closeDiagramEditor(diagramFile);
        
        if (closed)
        {
            RequirementUtils.openDiagramEditor(diagramFile);
        }
        
    }

    /**
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#getLinkedTargetModel(org.eclipse.emf.edit.domain.EditingDomain)
     */
    public Resource getLinkedTargetModel(ResourceSet resourceSet)
    {
        for (Resource resource : getSAMModels(resourceSet))
        {
            EObject model = resource.getContents().get(0);
            if (model instanceof Model)
            {
                if (((Model) model).getRequirementModel() != null)
                {
                    IPath path = new Path(resource.getURI().toString() + "di"); //$NON-NLS-1$
                    URI fileURI = URI.createURI(path.toString());
                    Resource samModel = resourceSet.getResource(fileURI, true);
                    return samModel;
                }
            }
        }
        return null;
    }

    /**
     * Gets a set of SAM models loaded in the editing domain.
     * 
     * @return all SAM models loaded as a set of Resources.
     */
    public static Set<Resource> getSAMModels(ResourceSet resourceSet)
    {
        Set<Resource> toReturn = new HashSet<Resource>();
        for (Resource resource : resourceSet.getResources())
        {
            if ("sam".equals(resource.getURI().fileExtension())) //$NON-NLS-1$
            {
                toReturn.add(resource);
            }
        }
        return toReturn;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#getRequirementProjectFromTargetDiagram(org.topcased.modeler.diagrams.model.Diagrams)
     */
    public RequirementProject getRequirementProjectFromTargetDiagram(Diagrams diagram)
    {
        if (diagram.getModel() instanceof Model)
        {
            return ((RequirementProject)((Model) diagram.getModel()).getRequirementModel());
        }
        return null;
    }

}
