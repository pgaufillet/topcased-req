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
package org.topcased.requirement.bundle.topcased.resource;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.topcased.modeler.di.model.Diagram;
import org.topcased.modeler.di.model.util.DIUtils;
import org.topcased.modeler.diagrams.model.Diagrams;
import org.topcased.modeler.diagrams.model.util.DiagramsResourceImpl;
import org.topcased.modeler.diagrams.model.util.DiagramsUtils;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.bundle.topcased.commands.LinkRequirementModelCommand;
import org.topcased.requirement.bundle.topcased.commands.UnlinkRequirementModelCommand;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Define the default policy for requirement attachment on DI models.<br>
 * 
 * @author <a href="mailto:tristan.faure@atosorigin.com">Tristan FAURE (ATOS ORIGIN INTEGRATION)</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * @since Topcased 3.4.0
 */
public class TopcasedAttachmentPolicy implements IModelAttachmentPolicy
{

    public static final String REQUIREMENT_PROPERTY_KEY = "requirements"; //$NON-NLS-1$

    /**
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#setAttachement(org.eclipse.emf.ecore.resource.Resource,
     *      org.eclipse.emf.ecore.resource.Resource)
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
        String resourcePath = getRequirementResourcePath((Diagrams) targetModel.getContents().get(0));
        if (!"".equals(resourcePath))
        {
            return new UnlinkRequirementModelCommand(targetModel, requirementModelPath);
        }
        return null;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#getLinkedTargetModel(org.eclipse.emf.edit.domain.EditingDomain)
     */
    public Resource getLinkedTargetModel(ResourceSet resourceSet)
    {
        for (Resource resource : resourceSet.getResources())
        {
            if (resource instanceof DiagramsResourceImpl && !resource.getContents().isEmpty())
            {
                String resourcePath = getRequirementResourcePath((Diagrams) resource.getContents().get(0));
                if (resourcePath != null && !"".equals(resourcePath))
                {
                    return resource;
                }
            }
        }
        return null;
    }

    /**
     * Set the requirement property to the DI model. This property has always this format : key = requirements, value =
     * the platform resource path of the requirement model.
     * 
     * @param modeler the modeler
     * @param requirements the requirements
     */
    public static void setProperty(Modeler targetModeler, Resource requirementModel)
    {
        Resource candidate = getDiagramModel(targetModeler.getResourceSet());
        if (candidate != null && !candidate.getContents().isEmpty())
        {
            EObject eobject = candidate.getContents().get(0);
            if (eobject instanceof Diagrams)
            {
                Diagram rootDiagram = DiagramsUtils.getRootDiagram((Diagrams) eobject);
                if (rootDiagram != null && requirementModel != null)
                {
                    DIUtils.setProperty(rootDiagram, REQUIREMENT_PROPERTY_KEY, requirementModel.getURI().trimFragment().deresolve(eobject.eResource().getURI()).toString());
                }
                else
                {
                    DIUtils.setProperty(rootDiagram, REQUIREMENT_PROPERTY_KEY, null);
                }
            }
        }
    }

    /**
     * Get the resource path to the requirement model attached with the given diagrams when it exists
     * 
     * @param diagrams the diagrams where requirement model will be search
     * @return the requirement resource path (URI like) found or an empty string when no requirement file exists
     */
    public static String getRequirementResourcePath(Diagrams diagrams)
    {
        String prop = getRequirementProperty(diagrams);
        if (!"".equals(prop))
        {
            URI resourceURI = diagrams.eResource().getURI().trimSegments(1);
            URI reqURI = resourceURI.appendSegment(prop);
            IPath path = RequirementUtils.getPath(reqURI);
            IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

            if (!file.exists())
            {
                setProperty(Utils.getCurrentModeler(), null);
                return "";
            }
        }
        
        return prop;
    }

    /**
     * Get the 'requirements' property determining the attached requirement file from diagrams
     * 
     * @param diagrams the diagrams where requirement model will be search
     * @return the relative requirement resource path (file may not actually exist)
     */
    private static String getRequirementProperty(Diagrams diagrams)
    {
        Diagram rootDiagram = DiagramsUtils.getRootDiagram(diagrams);
        String resourcePath = DIUtils.getPropertyValue(rootDiagram, REQUIREMENT_PROPERTY_KEY);
        if ("".equals(resourcePath))
        {
            List<Diagram> diagramList = DiagramsUtils.findAllDiagrams(diagrams);
            for (Diagram diagram : diagramList)
            {
                resourcePath = DIUtils.getPropertyValue(diagram, REQUIREMENT_PROPERTY_KEY);
                if (!"".equals(resourcePath))
                {
                    break;
                }
            }
        }
        return resourcePath;
    }

    /**
     * Default behavior consist in trying to find the diagram model from the Modeler's resource set.
     * 
     * @param rscSet The resource set coming from the modeler.
     * @return the resource corresponding to the diagram model.
     */
    private static Resource getDiagramModel(ResourceSet rscSet)
    {
        if (rscSet != null)
        {
            for (Resource resource : rscSet.getResources())
            {
                if (resource instanceof DiagramsResourceImpl)
                {
                    return resource;
                }
            }
        }
        return null;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#getRequirementProjectFromTargetMainResource(Resource)
     */
    public RequirementProject getRequirementProjectFromTargetMainResource(Resource mainResource)
    {
        if (mainResource != null && mainResource.getContents() != null && !mainResource.getContents().isEmpty())
        {
            EObject content = mainResource.getContents().get(0);
            if (content instanceof Diagrams)
            {
                Diagrams diagram = (Diagrams) content;
                String resourcePath = getRequirementResourcePath(diagram);
                if (!"".equals(resourcePath) && diagram.eResource() != null && diagram.eResource().getResourceSet() != null)
                {
                    URI uri = URI.createURI(resourcePath).trimFragment().resolve(diagram.eResource().getURI());
                    Resource reqResource = diagram.eResource().getResourceSet().getResource(uri, true);
                    if (!reqResource.getContents().isEmpty())
                    {
                        return (RequirementProject) reqResource.getContents().get(0);
                    }
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
