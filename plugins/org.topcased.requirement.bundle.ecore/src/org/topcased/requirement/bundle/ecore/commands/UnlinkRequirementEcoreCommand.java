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
package org.topcased.requirement.bundle.ecore.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecoretools.diagram.part.EcoreDiagramEditor;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResource;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.impl.DiagramImpl;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.bundle.ecore.helper.advice.EMFtoGMFCommandWrapper;
import org.topcased.requirement.bundle.ecore.internal.Messages;
import org.topcased.requirement.bundle.ecore.utils.EcoreRequirementUtils;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentRequirementView;
import org.topcased.requirement.core.views.upstream.UpstreamRequirementView;

/**
 * The Class UnlinkRequirementEcoreCommand.
 */
public class UnlinkRequirementEcoreCommand extends AbstractCommand
{

    /** The requirement resource path. */
    private IPath requirementResourcePath;

    /** The editor. */
    private IEditorPart editor;

    /** The target model. */
    private Resource targetModel;

    /**
     * Instantiates a new unlink requirement ecore command.
     * 
     * @param ltargetModel the ltarget model
     * @param requirementModelPath the requirement model path
     */
    public UnlinkRequirementEcoreCommand(Resource ltargetModel, IPath requirementModelPath)
    {
        super(Messages.getString("UnlinkRequirementModelCommand.0")); //$NON-NLS-1$
        requirementResourcePath = requirementModelPath;
        targetModel = ltargetModel;
        editor = RequirementUtils.getCurrentEditor();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.common.command.Command#execute()
     */
    public void execute()
    {
        redo();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.common.command.Command#redo()
     */
    public void redo()
    {
        if (editor instanceof EcoreDiagramEditor)
        {
            IEditorServices services = RequirementUtils.getSpecificServices(editor);
            final EditingDomain editingDomain = services.getEditingDomain(editor);
            ResourceSet set = editingDomain.getResourceSet();
            Resource requirementResource = set.getResource(URI.createPlatformResourceURI(requirementResourcePath.toString(), true), true);
            RequirementUtils.loadRequirementModel(requirementResource.getURI(), editingDomain);
            if (targetModel instanceof GMFResource)
            {
                Resource ecoreResource = set.getResource(targetModel.getURI(), false);
                EObject diagram = (DiagramImpl) ecoreResource.getContents().get(0);
                if (diagram != null && diagram instanceof Diagram)
                {
                    final EObject ePackage = ((Diagram) diagram).getElement();
                    if (ePackage != null && ePackage instanceof EPackage)
                    {
                        EAnnotation eAnnotation = ((EPackage) ePackage).getEAnnotation("http://www.topcased.org/requirements");

                        Command command = DeleteCommand.create(editingDomain, eAnnotation);
                        EMFtoGMFCommandWrapper wrapper = new EMFtoGMFCommandWrapper(command);
                        try
                        {
                            wrapper.execute(new NullProgressMonitor(), null);

                            set.getResources().remove(eAnnotation.getReferences().get(0).eResource());
                        }
                        catch (ExecutionException e)
                        {
                        }
                    }
                }
            }
        }

        ((CurrentRequirementView) CurrentRequirementView.getInstance()).partClosed(editor);
        ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).partClosed(editor);

    }

    /**
     * Undo.
     * 
     * @see org.eclipse.emf.common.command.AbstractCommand#undo()
     */
    @Override
    public void undo()
    {
        if (editor instanceof EcoreDiagramEditor)
        {
            IEditorServices services = RequirementUtils.getSpecificServices(editor);
            final EditingDomain editingDomain = services.getEditingDomain(editor);
            ResourceSet set = editingDomain.getResourceSet();
            Resource requirementResource = set.getResource(URI.createPlatformResourceURI(requirementResourcePath.toString(), true), true);
            RequirementUtils.loadRequirementModel(requirementResource.getURI(), editingDomain);
            EObject requirementProject = requirementResource.getContents().get(0);
            if (requirementProject instanceof RequirementProject)
            {
                EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
                eAnnotation.setSource(EcoreRequirementUtils.REQUIREMENT_SOURCE);
                eAnnotation.getReferences().add(requirementProject);

                if (targetModel instanceof GMFResource)
                {
                    Resource ecoreResource = set.getResource(targetModel.getURI(), false);
                    EObject diagram = (DiagramImpl) ecoreResource.getContents().get(0);
                    if (diagram != null && diagram instanceof Diagram)
                    {
                        final EObject ePackage = ((Diagram) diagram).getElement();

                        if (ePackage != null && ePackage instanceof EPackage)
                        {
                            Command command = AddCommand.create(editingDomain, ePackage, EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS, eAnnotation);
                            EMFtoGMFCommandWrapper wrapper = new EMFtoGMFCommandWrapper(command);
                            try
                            {
                                wrapper.execute(new NullProgressMonitor(), null);
                            }
                            catch (ExecutionException e)
                            {
                            }
                        }
                    }
                }
            }
        }
        ((CurrentRequirementView) CurrentRequirementView.getInstance()).partActivated(editor);
        ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).partActivated(editor);

    }

    /**
     * Can execute.
     * 
     * @return true, if successful
     * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
     */
    @Override
    public boolean canExecute()
    {
        return editor != null;
    }
}
