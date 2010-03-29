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

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.topcased.modeler.di.model.Diagram;
import org.topcased.modeler.di.model.DiagramInterchangeFactory;
import org.topcased.modeler.di.model.DiagramInterchangePackage;
import org.topcased.modeler.di.model.Property;
import org.topcased.modeler.diagrams.model.Diagrams;
import org.topcased.modeler.diagrams.model.util.DiagramsResourceImpl;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Define the static default policy of requirement attachment for di models
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class DefaultAttachmentPolicy implements IModelAttachmentPolicy
{

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
     * FIXME : find a better way to refresh upstream and current views than closing and reopening the diagram to adapt
     * the views
     * 
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#setAttachement(org.eclipse.emf.ecore.resource.Resource,
     *      org.eclipse.emf.ecore.resource.Resource)
     */
    public void linkRequirementModel(Resource targetModel, Resource requirementModel)
    {
        // get the diagram file from the target model resource
        IPath diagramFile = RequirementUtils.getPath(targetModel);
        if (!diagramFile.getFileExtension().endsWith("di"))
        {
            diagramFile = diagramFile.removeFileExtension().addFileExtension(diagramFile.getFileExtension() + "di");
            targetModel = RequirementUtils.getResource(diagramFile);
        }

        // Save the contents of the resource to the file system.
        RequirementUtils.saveResource(targetModel);

        // in case the diagram is already open
        if (Utils.getCurrentModeler() != null)
        {
            setProperty(Utils.getCurrentModeler(), requirementModel);
            RequirementUtils.closeDiagramEditor(diagramFile);
            RequirementUtils.openDiagramEditor(diagramFile);
        }
        else
        {
            RequirementUtils.openDiagramEditor(diagramFile);
            setProperty(Utils.getCurrentModeler(), requirementModel);
            RequirementUtils.closeDiagramEditor(diagramFile);
            RequirementUtils.openDiagramEditor(diagramFile);
        }

    }

    /**
     * @see org.topcased.requirement.core.extensions.IModelAttachmentPolicy#unlinkRequirementModel(org.eclipse.emf.ecore.resource.Resource,
     *      org.eclipse.emf.ecore.resource.Resource)
     */
    public void unlinkRequirementModel(Resource targetModel, Resource requirementModel)
    {
        if (getProperty(targetModel.getContents().get(0)) != null)
        {
            // Get the current modeler
            Modeler modeler = Utils.getCurrentModeler();

            // Detach the current modeler from the requirement project
            setProperty(modeler, null);

            // save the target model
            RequirementUtils.saveResource(targetModel);

            // unload and delete the requirement model from file system.
            if (modeler.getEditingDomain().getResourceSet().getResources().remove(requirementModel))
            {
                RequirementUtils.deleteResource(requirementModel);
            }
        }
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
                DiagramsResourceImpl res = (DiagramsResourceImpl) resource;

                if (getProperty(res.getContents().get(0)) != null)
                {
                    return res;
                }

            }
        }
        return null;
    }

    /**
     * Get the requirement property of a di. this property has always this format : key = requirements, value =
     * xxx.requirement
     * 
     * @param eobject the diagram
     * 
     * @return the requirement property
     */
    public Property getProperty(EObject eobject)
    {
        if (eobject != null)
        {
            URI uriOriginal = eobject.eResource().getURI();
            for (TreeIterator<EObject> i = eobject.eAllContents(); i.hasNext();)
            {
                EObject tmp = i.next();
                if (tmp.eResource() != null && tmp.eResource().getURI().equals(uriOriginal))
                {
                    if (tmp instanceof Property)
                    {
                        Property element = (Property) tmp;
                        if ("requirements".equals(element.getKey()))
                        {
                            return element;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Set the requirement property of the di this property has always this format : key = requirements, value =
     * xxx.requirement
     * 
     * @param modeler the modeler
     * @param requirements the requirements
     */
    public void setProperty(Modeler targetModeler, Resource requirementModel)
    {
        EObject eobject = targetModeler.getResourceSet().getResources().get(0).getContents().get(0);

        if (eobject instanceof Diagrams)
        {
            Command command = null;
            Diagram firstDiagram = null;
            Property property = null;
            property = getProperty(eobject);

            // when we want to delete the property
            if (requirementModel == null && property != null)
            {
                command = RemoveCommand.create(targetModeler.getEditingDomain(), property.eContainer(), DiagramInterchangePackage.Literals.DIAGRAM_ELEMENT__PROPERTY, property);
            }
            else
            {

                if (property == null)
                {
                    for (TreeIterator<EObject> i = eobject.eAllContents(); i.hasNext();)
                    {
                        EObject tmp = i.next();
                        if (tmp instanceof Diagram)
                        {
                            Diagram element = (Diagram) tmp;
                            if (firstDiagram == null)
                            {
                                firstDiagram = element;
                            }
                        }
                    }
                }

                String fragment = requirementModel.getURI().trimFragment().deresolve(URI.createURI(eobject.eResource().getURI().toString().replace(" ", "%20"))).toString();
                // when we want to add the property
                if (property == null && firstDiagram != null)
                {
                    property = DiagramInterchangeFactory.eINSTANCE.createProperty();
                    property.setKey("requirements");
                    property.setValue(fragment);
                    command = AddCommand.create(targetModeler.getEditingDomain(), firstDiagram, DiagramInterchangePackage.Literals.DIAGRAM_ELEMENT__PROPERTY, property);
                }
                // when we want to update the property
                else if (property != null)
                {
                    command = SetCommand.create(targetModeler.getEditingDomain(), property, DiagramInterchangePackage.Literals.PROPERTY__VALUE, fragment);
                }
            }
            command.execute();
        }
    }

}
