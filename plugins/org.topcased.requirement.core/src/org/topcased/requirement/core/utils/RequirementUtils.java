/*****************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.topcased.facilities.util.EditorUtil;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain;
import org.topcased.requirement.AttributeAllocate;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.TrashChapter;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.provider.RequirementItemProviderAdapterFactory;
import org.topcased.requirement.util.RequirementAdapterFactory;
import org.topcased.sam.Model;
import org.topcased.sam.NamedItem;
import org.topcased.sam.provider.SAMItemProviderAdapterFactory;
import org.topcased.sam.util.SAMAdapterFactory;

import ttm.Requirement;

/**
 * Utilities for the search in the Requirement Model
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * 
 */
public final class RequirementUtils
{
    /**
     * The shared adapter factory
     */
    private static ComposedAdapterFactory adapterFactory;

    static
    {
        // Create an adapter factory that yields item providers.
        adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
        adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
        adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
        adapterFactory.addAdapterFactory(new RequirementAdapterFactory());
        adapterFactory.addAdapterFactory(new RequirementItemProviderAdapterFactory());
        adapterFactory.addAdapterFactory(new SAMAdapterFactory());
        adapterFactory.addAdapterFactory(new SAMItemProviderAdapterFactory());
    }

    /**
     * Private constructor
     */
    private RequirementUtils()
    {
        // cannot be instancied
    }

    /**
     * Gets the adapter factory
     * 
     * @return The adapter factory
     */
    public static ComposedAdapterFactory getAdapterFactory()
    {
        return adapterFactory;
    }

    /**
     * Get all objects clazz in the resource
     * 
     * @param resource : a model
     * @param clazz : the object to search in the model
     * @return
     */
    public static Collection<EObject> getAllObjects(Resource resource, java.lang.Class< ? > clazz)
    {
        Collection<EObject> result = new ArrayList<EObject>();
        if (resource != null)
        {
            for (Iterator<EObject> h = resource.getAllContents(); h.hasNext();)
            {
                EObject currEo = h.next();
                if (clazz.isInstance(currEo))
                {
                    result.add(currEo);
                }
            }
        }
        return result;
    }

    /**
     * Gets all objects instance of clazz in the model referenced in the model of requirement
     * 
     * @param object
     * @param the clazz to search
     * 
     * @return
     */
    public static Collection<EObject> getAllObjects(EObject object, java.lang.Class< ? > clazz)
    {
        Collection<EObject> result = new ArrayList<EObject>();

        // Get all models referenced in the model requirement
        EditingDomain ed = TopcasedAdapterFactoryEditingDomain.getEditingDomainFor(object);
        Set<Resource> allSamModel = RequirementUtils.getModels(ed);

        // Get all EObject element in the models
        for (Resource r : allSamModel)
        {
            result.addAll(getAllObjects(r, clazz));
        }

        return result;
    }

    /**
     * Gets all Upstream Requirements from a starting point.
     * 
     * @param starting The model object representing the starting point.
     * @return A collection of Upstream Requirements found from the starting point.
     */
    public static Collection<Requirement> getUpstream(EObject starting)
    {
        Collection<Requirement> result = new ArrayList<Requirement>();
        for (Iterator<EObject> h = starting.eAllContents(); h.hasNext();)
        {
            EObject current = h.next();
            if (current instanceof Requirement)
            {
                result.add((Requirement) current);
            }
        }
        return result;
    }

    /**
     * Gets all {@link org.topcased.requirement.Requirement} starting from a starting point that should be of type
     * {@link HierarchicalElement}.
     * 
     * @param starting The model object representing the starting point.
     * @return A collection of Current Requirements found from the starting point
     */
    public static Collection<org.topcased.requirement.Requirement> getCurrents(EObject starting)
    {
        Collection<org.topcased.requirement.Requirement> result = new ArrayList<org.topcased.requirement.Requirement>();
        if (starting != null)
        {
            for (Iterator<EObject> h = starting.eAllContents(); h.hasNext();)
            {
                EObject current = h.next();
                if (current instanceof org.topcased.requirement.Requirement)
                {
                    result.add((org.topcased.requirement.Requirement) current);
                }
            }
        }
        return result;
    }

    /**
     * Gets all Current Requirements for a Resource.
     * 
     * @param resource The requirement model as an EMF {@link Resource}
     * @return A collection of Current Requirements found from the starting point
     */
    public static Collection<org.topcased.requirement.Requirement> getAllCurrents(Resource resource)
    {
        if (resource != null)
        {
            return getCurrents(resource.getContents().get(0));
        }
        return Collections.emptyList();
    }

    /**
     * Gets all Upstream Requirements for a Resource.
     * 
     * @param resource The requirement model as an EMF {@link Resource}
     * @return A collection of Upstream Requirements found from the starting point
     */
    public static Collection<Requirement> getAllUpstreams(Resource resource)
    {
        if (resource != null)
        {
            return getUpstream(resource.getContents().get(0));
        }
        return Collections.emptyList();
    }

    /**
     * Compute the label for an eobject
     * 
     * @return the label
     */
    public static String getLabelAttibute(EObject o)
    {
        String label = ""; //$NON-NLS-1$
        if (o != null)
        {
            // label += o.eClass().getName() + " ";
            if (o instanceof NamedItem && ((NamedItem) o).getName() != null)
            {
                label += ((NamedItem) o).getName();
            }
            else if (o instanceof Requirement && ((Requirement) o).getIdent() != null)
            {
                label += ((Requirement) o).getIdent();
            }
            else if (o instanceof EObject)
            {
                label += o.toString();
            }
        }
        return label;
    }

    /**
     * Determines if the upstream requirement is linked (link_to attribute) to a current requirement
     * 
     * @param an upstream requirement
     * @return true if the upstream requirement is linked (link_to attribute) to a current requirement
     */
    public static Boolean isLinked(Requirement req)
    {
        for (Setting setting : getCrossReferences(req))
        {
            if (setting.getEObject() instanceof AttributeLink)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the upstream requirement is partial (link_to attribute)
     * 
     * @param an upstream requirement
     * @return true if the upstream requirement is partial (link_to attribute)
     */
    public static Boolean isPartial(Requirement req)
    {
        Boolean result = false;
        for (Setting setting : getCrossReferences(req))
        {
            if (setting.getEObject() instanceof AttributeLink)
            {
                AttributeLink link = (AttributeLink) setting.getEObject();
                result |= link.getPartial();
            }
        }
        return result;
    }

    public static Collection<Setting> getCrossReferences(EObject source)
    {
        Collection<Setting> collection = null;
        ECrossReferenceAdapter crossReferenceAdapter = ECrossReferenceAdapter.getCrossReferenceAdapter(source);
        if (crossReferenceAdapter != null)
        {
            collection = crossReferenceAdapter.getNonNavigableInverseReferences(source);
        }
        else
        {
            collection = EcoreUtil.UsageCrossReferencer.find(source, source.eResource().getResourceSet());
        }
        return !collection.isEmpty() ? collection : Collections.<Setting> emptyList();
    }

    /**
     * Count the number of children of type Hierarchical Element
     * 
     * @param hierarchicalElement
     * 
     * @return the number of children of type Hierarchical Element
     */
    public static int countChildrens(Object hierarchicalElement)
    {
        int result = 0;

        if (!(hierarchicalElement instanceof HierarchicalElement))
        {
            return result;
        }

        for (Iterator<EObject> children = ((HierarchicalElement) hierarchicalElement).eAllContents(); children.hasNext();)
        {
            EObject currEo = children.next();
            if (currEo instanceof HierarchicalElement && (((HierarchicalElement) currEo).getParent() == hierarchicalElement))
            {
                result++;
            }
        }

        return result;
    }

    /**
     * Get a resource to the file
     * 
     * @param file The model path
     * @return the resource
     */
    public static Resource getResource(IPath file)
    {
        ResourceSet resourceSet = new ResourceSetImpl();
        URI fileURI = URI.createPlatformResourceURI(file.toString(), true);
        return resourceSet.getResource(fileURI, true);
    }

    /**
     * Deletes a given resource
     * 
     * @param resource
     */
    public static void deleteResource(Resource resource)
    {
        try
        {
            resource.unload();
            URI uri = resource.getURI();
            IResource toDelete = ResourcesPlugin.getWorkspace().getRoot().findMember(uri.toPlatformString(true));
            if (toDelete.isAccessible() && toDelete.exists())
            {
                toDelete.delete(true, new NullProgressMonitor());
                toDelete.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
            }
        }
        catch (CoreException e)
        {
            RequirementCorePlugin.log("Error while deleting Requirement model. Please, delete it manually.", IStatus.ERROR, e); //$NON-NLS-1$
        }
    }

    /**
     * Gets the EObject corresponding to the clazz in the resource
     * 
     * @param resource : the requirement model
     * @param clazz
     * 
     * @return EObject of the RequirementProject
     */
    public static EObject getRoot(Resource resource, java.lang.Class< ? > clazz)
    {
        if (resource != null)
        {
            for (EObject o : resource.getContents())
            {
                if (clazz.isInstance(o))
                {
                    return o;
                }
            }
        }
        return null;
    }

    /**
     * Saves a resource
     * 
     * @param resource
     */
    public static void saveResource(Resource resource)
    {
        try
        {
            Map<Object, Object> options = new HashMap<Object, Object>();
            options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
            resource.save(options);
        }
        catch (IOException exception)
        {
            RequirementCorePlugin.log(exception);
        }
    }

    /**
     * Loads the requirement model if required and returns the resource.
     * 
     * @param uri The uri of the requirement model
     * @param domain The shared editing domain
     * @return the requirement model as an EMF resource
     */
    public static void loadRequirementModel(URI uri, EditingDomain domain)
    {
        // Gets the requirement model from the editing domain or loads it if it's the first time
        domain.getResourceSet().getResource(uri, true);
    }

    /**
     * Unloads the requirement model from the editing domain
     * 
     * @param domain The shared editing domain
     * @return
     */
    public static boolean unloadRequirementModel(EditingDomain domain)
    {
        Resource requirementRsc = getRequirementModel(domain);
        requirementRsc.unload();
        return domain.getResourceSet().getResources().remove(requirementRsc);
    }

    /**
     * Gets the requirement model as an EMF resource.
     * 
     * @return the requirement model as a resource
     */
    public static Resource getRequirementModel(EditingDomain editingDomain)
    {
        for (Resource resource : editingDomain.getResourceSet().getResources())
        {
            if ("requirement".equals(resource.getURI().fileExtension())) //$NON-NLS-1$
            {
                return resource;
            }
        }
        return null;
    }

    /**
     * Gets a set of models loaded in the Topcased editing domain.
     * 
     * @return all models loaded as a set of Resources.
     */
    public static Set<Resource> getModels(EditingDomain editingDomain)
    {
        Set<Resource> toReturn = new HashSet<Resource>();
        for (Resource resource : editingDomain.getResourceSet().getResources())
        {
            if ("sam".equals(resource.getURI().fileExtension())) //$NON-NLS-1$
            {
                System.out.println("getModels : sam");
                toReturn.add(resource);
            }
            if ("uml".equals(resource.getURI().fileExtension())) //$NON-NLS-1$
            {
                System.out.println("getModels : uml");
                toReturn.add(resource);
                
            }
        }
        return toReturn;
    }

    /**
     * Gets a the model link with requirement.
     * 
     * @return the model links with requirement model.
     */
    public static Resource getSAMModel(EditingDomain editingDomain)
    {
        for (Resource resource : getModels(editingDomain))
        {
            EObject model = resource.getContents().get(0);
            if (model instanceof Model)
            {
                if (((Model) model).getRequirementModel() != null)
                {
                    return resource;
                }
            }
        }
        return null;
    }

    /**
     * Gets the {@link RequirementProject} model object contained in this EMF resource.
     * 
     * @param requirement The requirement model
     * @return The {@link RequirementProject} found inside this resource.
     */
    public static RequirementProject getRequirementProject(EditingDomain editingDomain)
    {
        Resource requirementModel = RequirementUtils.getRequirementModel(editingDomain);
        return getRequirementProject(requirementModel);
    }

    /**
     * Gets the {@link RequirementProject} model object contained in this EMF resource.
     * 
     * @param requirement The requirement model
     * @return The {@link RequirementProject} found inside this resource.
     */
    public static RequirementProject getRequirementProject(Resource requirement)
    {
        return (RequirementProject) getRoot(requirement, RequirementProject.class);
    }

    /**
     * Gets the {@link AttributeConfiguration} model object contained in a {@link RequirementProject}.
     * 
     * @param requirement The resource representing a requirement model
     * @return the current attribute configuration.
     */
    public static AttributeConfiguration getAttributeConfiguration(Resource requirement)
    {
        RequirementProject project = getRequirementProject(requirement);
        if (project != null)
        {
            return project.getAttributeConfiguration();
        }
        return null;
    }

    /**
     * Gets the {@link AttributeConfiguration} model object contained in a {@link RequirementProject}.
     * 
     * @param editingDomain The configuration must be searched inside the domain.
     * @return the current attribute configuration.
     */
    public static AttributeConfiguration getAttributeConfiguration(EditingDomain editingDomain)
    {
        Resource requirementModel = RequirementUtils.getRequirementModel(editingDomain);
        return RequirementUtils.getAttributeConfiguration(requirementModel);
    }

    /**
     * Gets the {@link UpstreamModel} model object contained in a {@link RequirementProject}.
     * 
     * @param requirement The resource representing a requirement model
     * @return the upstream model found.
     */
    public static UpstreamModel getUpstreamModel(Resource requirement)
    {
        RequirementProject project = (RequirementProject) getRoot(requirement, RequirementProject.class);
        if (project != null)
        {
            return project.getUpstreamModel();
        }
        return null;
    }

    /**
     * Gets the Trash Chapter model object contained in a Requirement Project.
     * 
     * @param editingDomain The trash chapter must be searched inside the domain.
     * @return the special trash chapter found.
     */
    public static TrashChapter getTrashChapter(EditingDomain editingDomain)
    {
        RequirementProject project = getRequirementProject(editingDomain);
        if (project != null)
        {
            for (SpecialChapter chapter : project.getChapter())
            {
                if (chapter instanceof TrashChapter)
                {
                    return (TrashChapter) chapter;
                }
            }
        }
        return null;
    }

    /**
     * Before displaying the choose dialog, we need to compute the content to display according to the selected object.
     * 
     * @param selected The first element selected
     * @return An object collection to pass to the dialog.
     */
    public static Collection<Object> getChooseDialogContent(Object selected)
    {
        Collection<Object> listObjects = new ArrayList<Object>();
        // this is the value inserted to reset the affected value
        listObjects.add("");
        if (selected instanceof AttributeAllocate)
        {
            // add all the model elements
            listObjects.addAll(RequirementUtils.getAllObjects((EObject) selected, NamedItem.class));
        }
        else
        {
            if (!(selected instanceof AttributeLink))
            {
                listObjects.addAll(RequirementUtils.getAllObjects((EObject) selected, NamedItem.class));
            }
            ObjectAttribute objAtt = (ObjectAttribute) selected;
            listObjects.addAll(RequirementUtils.getAllUpstreams(objAtt.eResource()));
            listObjects.addAll(RequirementUtils.getAllCurrents(objAtt.eResource()));
        }
        return listObjects;
    }

    /**
     * Gets the hierarchical element for a given model object.
     * 
     * @param toFind The EObject for which the corresponding hierarchical element must be found.
     */
    public static HierarchicalElement getHierarchicalElementFor(Object toFind)
    {
        if (toFind instanceof EObject)
        {
            return getHierarchicalElementFor((EObject) toFind);
        }
        return null;
    }

    /**
     * Gets the hierarchical element for a given model object.
     * 
     * @param toFind The EObject for which the corresponding hierarchical element must be found.
     */
    public static HierarchicalElement getHierarchicalElementFor(EObject toFind)
    {
        for (Setting setting : getCrossReferences(toFind))
        {
            if (setting.getEObject() instanceof HierarchicalElement)
            {
                return (HierarchicalElement) setting.getEObject();
            }
        }
        return null;
    }

    /**
     * Opens the given diagram resource given in parameter.
     * 
     * @param resourceToClose The diagram resource to close
     * @return <code>true</code> if the diagram editor has been closed, <code>false</code> otherwise.
     */
    public static void openSAMDiagramEditor(IPath resourceToClose)
    {
        try
        {
            IFile fileToOpen = ResourcesPlugin.getWorkspace().getRoot().getFile(resourceToClose);
            EditorUtil.open(fileToOpen);
        }
        catch (PartInitException e)
        {
            RequirementCorePlugin.log(e);
        }
    }

    /**
     * Closes the given resource if already opened.
     * 
     * @param resourceToClose The diagram resource to close
     * @return <code>true</code> if the diagram editor has been closed, <code>false</code> otherwise.
     */
    public static boolean closeSAMDiagramEditor(IPath resourceToClose)
    {
        boolean closed = false;
        // Look for a reference to the editor ?
        for (IEditorReference editorRef : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences())
        {
            try
            {
                String resourceName = editorRef.getEditorInput().getName();
                if (resourceToClose.lastSegment().equals(resourceName))
                {
                    IEditorPart part = editorRef.getEditor(false);
                    if (part != null && part instanceof Modeler)
                    {
                        Modeler modeler = (Modeler) part;
                        modeler.doSave(new NullProgressMonitor());
                        closed = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(part, false);
                    }
                }
            }
            catch (PartInitException e)
            {
                RequirementCorePlugin.log(e);
            }
        }
        return closed;
    }

}
