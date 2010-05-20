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
package org.topcased.requirement.core.extensions;

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
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.commands.LinkRequirementModelCommand;
import org.topcased.requirement.core.commands.UnlinkRequirementModelCommand;

/**
 * Define the default policy for requirement attachment on DI models.<br>
 * 
 * @author <a href="mailto:tristan.faure@atosorigin.com">Tristan FAURE (ATOS ORIGIN INTEGRATION)</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * @since Topcased 3.4.0
 */
public class DefaultAttachmentPolicy implements IModelAttachmentPolicy
{

    public static final String REQUIREMENT_PROPERTY_KEY = "requirements"; //$NON-NLS-1$

    /** the shared instance */
    private static DefaultAttachmentPolicy policy;

    /**
     * Private constructor
     */
    private DefaultAttachmentPolicy()
    {
        // avoid instantiation
    }

    /**
     * Gets the shared instance.
     * 
     * @return the default Attachment policy
     */
    public static DefaultAttachmentPolicy getInstance()
    {
        if (policy == null)
        {
            policy = new DefaultAttachmentPolicy();
        }
        return policy;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#setAttachement(org.eclipse.emf.ecore.resource.Resource,
     *      org.eclipse.emf.ecore.resource.Resource)
     */
    public Command linkRequirementModel(Resource targetModel, Resource requirementModel)
    {
        return new LinkRequirementModelCommand(targetModel, requirementModel);
    }

    /**
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#unlinkRequirementModel(org.eclipse.emf.ecore.resource.Resource,
     *      org.eclipse.emf.ecore.resource.Resource)
     */
    public Command unlinkRequirementModel(Resource targetModel, Resource requirementModel, boolean deleteRequirementModel)
    {
        Diagram rootDiagram = DiagramsUtils.getRootDiagram((Diagrams) targetModel.getContents().get(0));
        String resourcePath = DIUtils.getPropertyValue(rootDiagram, REQUIREMENT_PROPERTY_KEY);
        if (!"".equals(resourcePath))
        {
            return new UnlinkRequirementModelCommand(targetModel, requirementModel, deleteRequirementModel);
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
            if (resource instanceof DiagramsResourceImpl)
            {
                Diagrams res = (Diagrams) resource.getContents().get(0);
                Diagram root = DiagramsUtils.getRootDiagram(res);
                if (DIUtils.getProperty(root, REQUIREMENT_PROPERTY_KEY) != null)
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
    public void setProperty(Modeler targetModeler, Resource requirementModel)
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
     * Default behavior consist in trying to find the diagram model from the Modeler's resource set.
     * 
     * @param rscSet The resource set coming from the modeler.
     * @return the resource corresponding to the diagram model.
     */
    private Resource getDiagramModel(ResourceSet rscSet)
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
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#getRequirementProjectFromTargetDiagram(org.topcased.modeler.diagrams.model.Diagrams)
     */
    public RequirementProject getRequirementProjectFromTargetDiagram(Diagrams diagram)
    {
        Diagram rootDiagram = DiagramsUtils.getRootDiagram(diagram);
        String resourcePath = DIUtils.getPropertyValue(rootDiagram, REQUIREMENT_PROPERTY_KEY);
        if (!"".equals(resourcePath) && diagram.eResource() != null && diagram.eResource().getResourceSet() != null)
        {
            URI uri = URI.createURI(resourcePath).trimFragment().resolve(rootDiagram.eResource().getURI());
            return (RequirementProject) diagram.eResource().getResourceSet().getResource(uri, true).getContents().get(0);
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
