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
 *	David Ribeiro (Atos Origin}) {david.ribeirocampelo@atosorigin.com}
 *
 *****************************************************************************/
package org.topcased.requirement.bundle.ecore;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResource;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.impl.DiagramImpl;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.bundle.ecore.commands.LinkRequirementEcoreCommand;
import org.topcased.requirement.bundle.ecore.commands.UnlinkRequirementEcoreCommand;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;

/**
 * The Class EcoreAttachmentPolicy.
 */
public class EcoreAttachmentPolicy implements IModelAttachmentPolicy
{

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.requirement.core.extensions.IModelAttachmentPolicy#linkRequirementModel(org.eclipse.emf.ecore.resource
     * .Resource, org.eclipse.core.runtime.IPath)
     */
    public Command linkRequirementModel(Resource targetModel, IPath requirementModelPath)
    {
        return new LinkRequirementEcoreCommand(targetModel, requirementModelPath);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.requirement.core.extensions.IModelAttachmentPolicy#unlinkRequirementModel(org.eclipse.emf.ecore.
     * resource.Resource, org.eclipse.core.runtime.IPath)
     */
    public Command unlinkRequirementModel(Resource targetModel, IPath requirementModelPath)
    {
        return new UnlinkRequirementEcoreCommand(targetModel, requirementModelPath);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.requirement.core.extensions.IModelAttachmentPolicy#getLinkedTargetModel(org.eclipse.emf.ecore.resource
     * .ResourceSet)
     */
    public Resource getLinkedTargetModel(ResourceSet resourceSet)
    {
        Resource linkedTargetModel = null;
        for (Resource resource : resourceSet.getResources())
        {
            if (resource instanceof GMFResource)
            {
                EObject diagram = (DiagramImpl) resource.getContents().get(0);
                if (diagram != null && diagram instanceof Diagram)
                {
                    EObject ePackage = ((Diagram) diagram).getElement();
                    if (ePackage != null && ePackage instanceof EPackage)
                    {
                        EAnnotation eAnnotation = ((EPackage) ePackage).getEAnnotation("http://www.topcased.org/requirements");

                        if (eAnnotation != null)
                        {
                            linkedTargetModel = diagram.eResource();
                        }
                    }
                }
            }
        }
        return linkedTargetModel;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.requirement.core.extensions.IModelAttachmentPolicy#getRequirementProjectFromTargetMainResource(org
     * .eclipse.emf.ecore.resource.Resource)
     */
    public RequirementProject getRequirementProjectFromTargetMainResource(Resource mainResource)
    {
        RequirementProject requirementProject = null;
        if (mainResource instanceof GMFResource)
        {
            EObject diagram = (DiagramImpl) mainResource.getContents().get(0);
            if (diagram != null && diagram instanceof Diagram)
            {
                EObject ePackage = ((Diagram) diagram).getElement();
                if (ePackage != null && ePackage instanceof EPackage)
                {
                    EAnnotation eAnnotation = ((EPackage) ePackage).getEAnnotation("http://www.topcased.org/requirements");

                    if (eAnnotation != null)
                    {
                        requirementProject = (RequirementProject) eAnnotation.getReferences().get(0);
                    }
                }
            }
        }
        return requirementProject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.requirement.core.extensions.IModelAttachmentPolicy#isRootContainer(org.eclipse.emf.ecore.EObject)
     */
    public boolean isRootContainer(EObject parentElt)
    {
        return parentElt == null;
    }

}
