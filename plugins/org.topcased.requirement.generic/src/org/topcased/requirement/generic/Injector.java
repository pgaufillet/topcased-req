/*****************************************************************************
 * Copyright (c) 2009 ATOS ORIGIN INTEGRATION.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) tristan.faure@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.generic;

import java.lang.reflect.Field;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.topcased.modeler.di.model.Diagram;
import org.topcased.modeler.di.model.DiagramInterchangeFactory;
import org.topcased.modeler.di.model.DiagramInterchangePackage;
import org.topcased.modeler.di.model.Property;
import org.topcased.modeler.diagrams.model.Diagrams;
import org.topcased.modeler.editor.Modeler;
import org.topcased.requirement.generic.actions.UpstreamSelectionChangedListener;
import org.topcased.sam.presentation.SAMEditor;
import org.topcased.sam.requirement.AttributeLink;
import org.topcased.sam.requirement.CurrentRequirement;
import org.topcased.sam.requirement.RequirementFactory;
import org.topcased.sam.requirement.RequirementPackage;
import org.topcased.sam.requirement.RequirementProject;
import org.topcased.sam.requirement.core.actions.UpdateRequirementModelAction;
import org.topcased.sam.requirement.core.utils.MergeRequirement;
import org.topcased.sam.requirement.core.views.current.CurrentPage;
import org.topcased.sam.requirement.core.views.current.CurrentRequirementView;
import org.topcased.sam.requirement.core.views.upstream.UpstreamPage;
import org.topcased.sam.requirement.core.views.upstream.UpstreamRequirementView;
import org.topcased.sam.requirement.util.RequirementResourceImpl;

/**
 * The Class Injector.
 */
public class Injector
{
    private static IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

    private static Injector instance = new Injector();

    private CurrentPage current = null;

    private UpstreamPage upstream = null;

    /**
     * Gets the requirement project.
     * 
     * @param property the property
     * 
     * @return the requirement project
     */
    public static synchronized RequirementProject getRequirementProject(EObject eobject)
    {
        return getRequirementProject(getProperty(eobject));
    }
    
    /**
     * Gets the requirement project.
     * 
     * @param property the property
     * 
     * @return the requirement project
     */
    public static synchronized RequirementProject getRequirementProject(Property property)
    {
        RequirementProject result = null;
        if (property != null)
        {
            String uri = URI.createURI(property.getValue()).trimFragment().resolve(property.eResource().getURI()).toString();
            result = (RequirementProject) new ResourceSetImpl().getResource(URI.createURI(uri), true).getContents().get(0);
        }
        return result;
    }

    /**
     * Gets the property.
     * 
     * @param eobject the eobject
     * 
     * @return the property
     */
    public static synchronized Property getProperty(EObject eobject)
    {
        if (eobject != null)
        {
            for (TreeIterator<EObject> i = eobject.eAllContents(); i.hasNext();)
            {
                EObject tmp = i.next();
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
        return null;
    }
    
    public static Injector getInstance()
    {
        return instance;
    }

    private Injector()
    {
    }

    public void loadRequirement(String uri, IEditorPart editor, boolean load)
    {
        if (editor == null)
        {
            editor = Activator.getActiveEditor();
        }
        if (!(editor instanceof Modeler))
        {
            return;
        }
        Modeler modeler = (Modeler) editor;
        try
        {
            RequirementProject project = RequirementProjectManager.getInstance().getRequirementProject(uri, modeler);
            if (!load)
            {
                manageEAnnotation(modeler, project);
                initCurrent((CurrentPage) ((CurrentRequirementView) CurrentRequirementView.getInstance()).getCurrentPage(), Activator.getActiveEditor(), uri);
                initUpstream((UpstreamPage) ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).getCurrentPage(), Activator.getActiveEditor(), uri);
            }
            Resource r = project.eResource();
            if (!modeler.getResourceSet().getResources().contains(r))
            {
                modeler.getResourceSet().getResources().add(r);
            }
            // if (page != null)
            // {
            // enableActions(page);
            // }
            // add requirements to modeler

        }
        catch (Exception e)
        {
            Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, "error during loading requirements"));
            e.printStackTrace();
        }

    }

    protected void initUpstream(UpstreamPage page, IEditorPart m, String uri)
    {
//        System.out.println("DEBUT init");
        upstream = page;
        RequirementProject project = RequirementProjectManager.getInstance().getRequirementProject(uri, m);
        if (project != null)
        {
//            System.out.println("Project OK");
            page.getViewer().setInput(project.getUpstreamModel());
            if (current != null)
            {
//                System.out.println("Input OK");
                page.getViewer().addSelectionChangedListener(new UpstreamSelectionChangedListener(current));
            }
            enableActions(page);
            page.getViewer().refresh();
        }
    }

    protected void initCurrent(CurrentPage page, IEditorPart m, String uri)
    {
        current = page;
        applyCurrent(RequirementProjectManager.getInstance().getRequirementProject(uri, m), page);
        if (upstream != null)
        {
            upstream.getViewer().addSelectionChangedListener(new UpstreamSelectionChangedListener(page));
        }
        createActions(page);
    }

    /**
     * Creates the actions.
     * 
     * @param page the page
     */
    private void createActions(CurrentPage page)
    {
        IToolBarManager manager = page.getSite().getActionBars().getToolBarManager();
        manager.add(new NewLinkToAction(page));
        manager.update(true);
    }

    /**
     * Apply current.
     * 
     * @param requirements the requirements
     * @param page the page
     */
    private void applyCurrent(RequirementProject requirements, CurrentPage page)
    {
        if (requirements != null)
        {
            if (page != null)
            {
                page.setModel(requirements);
                page.getViewer().setInput(requirements);
                page.getViewer().refresh();
            }
        }
    }

    /**
     * Load requirement.
     * 
     * @param uri the uri
     */
    public void loadRequirement(String uri)
    {
        loadRequirement(uri, null, false);
    }

    /**
     * Manage e annotation.
     * 
     * @param modeler the modeler
     * @param requirements the requirements
     */
    private void manageEAnnotation(Modeler modeler, RequirementProject requirements)
    {
        EObject eobject = modeler.getResourceSet().getResources().get(0).getContents().get(0);
        if (eobject instanceof Diagrams)
        {
            Property property = Injector.getProperty(eobject);
            Diagram firstDiagram = null;
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
            CompoundCommand command = new CompoundCommand();

            String fragment = requirements.eResource().getURI().trimFragment().deresolve(URI.createURI(eobject.eResource().getURI().toString().replace(" ", "%20"))).toString();
            if (property == null && firstDiagram != null)
            {
                property = DiagramInterchangeFactory.eINSTANCE.createProperty();
                property.setKey("requirements");
                property.setValue(fragment);
                command.append(AddCommand.create(modeler.getEditingDomain(), firstDiagram, DiagramInterchangePackage.Literals.DIAGRAM_ELEMENT__PROPERTY, property));
            }
            else if (property != null)
            {
                command.append(SetCommand.create(modeler.getEditingDomain(), property, DiagramInterchangePackage.Literals.PROPERTY__VALUE, fragment));
            }
            modeler.getEditingDomain().getCommandStack().execute(command);

        }
    }

    /**
     * Enable actions.
     * 
     * @param page the page
     */
    private void enableActions(UpstreamPage page)
    {
        IToolBarManager manager = page.getSite().getActionBars().getToolBarManager();
        for (IContributionItem item : manager.getItems())
        {
            if (item instanceof ActionContributionItem)
            {
                ActionContributionItem action = (ActionContributionItem) item;
                if (action.getAction() instanceof UpdateRequirementModelAction)
                {
                    try
                    {
                        Field f = ActionContributionItem.class.getDeclaredField("action");
                        f.setAccessible(true);
                        f.set(action, new CustomUpdateRequirementModelAction(page.getEditingDomain()));
                        f.setAccessible(false);
                        action.update();
                        break;
                    }
                    catch (SecurityException e)
                    {
                    }
                    catch (NoSuchFieldException e)
                    {
                    }
                    catch (IllegalArgumentException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        }
        manager.update(true);
    }

    /**
     * Open resource for requirement.
     * 
     * @return the string
     */
    public static String openResourceForRequirement()
    {
        ResourceDialog dialog = new ResourceDialog(window.getShell(), "Choose requirements file", SWT.OPEN);
        if (dialog.open() == ResourceDialog.OK)
        {
            String uri = dialog.getURIText();
            if (uri.length() > 0 && uri.endsWith("requirement"))
            {
                return uri;
            }
            else
            {
                Utils.error("please provide requirements file");
            }
        }
        return "";
    }

    /**
     * The Class NewLinkToAction.
     */
    private class NewLinkToAction extends Action
    {
        private CurrentPage currentPage;

        public NewLinkToAction(CurrentPage page)
        {
            setEnabled(false);
            setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/link_obj.gif"));
            setText("add link to selection");
            currentPage = page;
            currentPage.getViewer().addSelectionChangedListener(new ISelectionChangedListener()
            {
                public void selectionChanged(SelectionChangedEvent event)
                {
                    if (event.getSelection() instanceof IStructuredSelection)
                    {
                        IStructuredSelection struct = (IStructuredSelection) event.getSelection();
                        NewLinkToAction.this.setEnabled(struct.getFirstElement() instanceof CurrentRequirement);
                    }
                }
            });
        }

        private Object getSelection()
        {
            if (currentPage != null)
            {
                ISelection select = currentPage.getViewer().getSelection();
                if (select instanceof IStructuredSelection)
                {
                    IStructuredSelection struct = (IStructuredSelection) select;
                    return struct.getFirstElement();
                }
            }
            return null;
        }

        @Override
        public void run()
        {
            Object selec = getSelection();
            if (selec instanceof CurrentRequirement)
            {
                CurrentRequirement current = (CurrentRequirement) selec;
                AttributeLink att = RequirementFactory.eINSTANCE.createAttributeLink();
                att.setName("#Link_to");
                att.setValue(null);
                att.setPartial(false);
                EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(current);
                domain.getCommandStack().execute(new AddCommand(domain, current, RequirementPackage.Literals.REQUIREMENT__ATTRIBUTE, att));
            }
        }

    }

    /**
     * The Class CustomUpdateRequirementModelAction.
     */
    private class CustomUpdateRequirementModelAction extends UpdateRequirementModelAction
    {

        private EditingDomain editingDomain;

        public CustomUpdateRequirementModelAction(EditingDomain domain)
        {
            super(domain);
            editingDomain = domain;
        }

        private boolean isSam()
        {
            return Activator.isCurrentEditorSamEditor();
        }

        private boolean isTopcased()
        {
            IEditorPart activeEditor = Activator.getActiveEditor();
            if (activeEditor != null && activeEditor instanceof Modeler)
            {
                if (!(activeEditor instanceof SAMEditor))
                {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean isEnabled()
        {
            if (isSam() && !isTopcased())
            {
                return super.isEnabled();
            }
            else
            {
                return true;
            }
        }

        private Resource getRequirementResource()
        {
            ResourceSet set = editingDomain.getResourceSet();
            for (Resource r : set.getResources())
            {
                if (r instanceof RequirementResourceImpl)
                {
                    return r;
                }
            }
            return null;
        }

        @Override
        public void run()
        {
            if (isSam() && !isTopcased())
            {
                super.run();
            }
            else
            {
                String uri = openResourceForRequirement();
                if (uri.length() > 0)
                {
                    Resource requirementMerge = new ResourceSetImpl().getResource(URI.createURI(uri), true);
                    try
                    {
                        MergeRequirement.INSTANCE.merge(getRequirementResource(), requirementMerge, new NullProgressMonitor());
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
