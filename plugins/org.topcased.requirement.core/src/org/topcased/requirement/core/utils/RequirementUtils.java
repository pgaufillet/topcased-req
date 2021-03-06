/*****************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *      Olivier M�lois <a href="mailto:olivier.melois@atos.net">olivier.melois@atos.net</a>"
 *    
 ******************************************************************************/
package org.topcased.requirement.core.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;
import org.topcased.facilities.util.EditorUtil;
import org.topcased.requirement.AttributeAllocate;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.DeletedChapter;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.TrashChapter;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.services.RequirementModelSourceProvider;
import org.topcased.requirement.util.RequirementCacheAdapter;
import org.topcased.requirement.util.RequirementResource;
import org.topcased.typesmodel.model.inittypes.DeletionParameters;
import org.topcased.typesmodel.model.inittypes.DeletionParemeter;

import ttm.Document;
import ttm.Requirement;

/**
 * Utilities for the search in the Requirement Model
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public final class RequirementUtils
{
    /** Prefix for deleted documents */
    public static final String DELETED_PREFIX = "deleted_";//$NON-NLS-1$

    /** Pattern for detecting ident of deleted documents */
    private static final Pattern DELETED_DOCUMENT_PATTERN = Pattern.compile(DELETED_PREFIX.concat("\\d\\d\\d\\d-\\d\\d-\\d\\d(_\\d\\d-\\d\\d)?"));//$NON-NLS-1$

    /** Format string for constructing ident of deleted documents */
    private static final String DELETED_DOCUMENT_IDENT_FORMAT = DELETED_PREFIX.concat("%s");

    /** Date format for constructing ident of deleted documents */
    private static final String DELETED_DOCUMENT_DATE_FORMAT = "yyyy-MM-dd_HH-mm";

    /**
     * The shared adapter factory
     */
    private static ComposedAdapterFactory adapterFactory;

    static
    {
        // Create an adapter factory that yields item providers.
        adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
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
     * Gets the adapter factory from the editing domain (mainly used for label providers)
     * 
     * @param editingDomain the modeler's editing domain
     * @return the editing domain's adapter factory or default adapter factory if unavailable
     * @see #getAdapterFactory()
     */
    public static AdapterFactory getAdapterFactory(EditingDomain editingDomain)
    {
        if (editingDomain instanceof AdapterFactoryEditingDomain)
        {
            AdapterFactoryEditingDomain topcasedDomain = (AdapterFactoryEditingDomain) editingDomain;
            if (topcasedDomain.getAdapterFactory() instanceof ComposedAdapterFactory)
            {
                ComposedAdapterFactory factory = (ComposedAdapterFactory) topcasedDomain.getAdapterFactory();
                return factory;
            }
        }
        return getAdapterFactory();
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
        if (resource != null && !resource.getContents().isEmpty())
        {
            for (Iterator<EObject> h = resource.getContents().get(0).eAllContents(); h.hasNext();)
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
     * Gets all objects instance of clazz in the target model referenced in the model of requirement
     * 
     * @param the clazz to search
     * 
     * @return collection of model objects or empty collection
     */
    public static Collection<EObject> getAllObjects(EClass classifier)
    {
        IEditorServices services = getSpecificServices(null);
        if (services != null)
        {
            Resource modelResource = services.getModelResource(null);
            if (modelResource.getContents().size() > 0)
            {
                EObject model = modelResource.getContents().get(0);
                return services.getAllModelObjects(model, classifier);
            }
        }
        return new ArrayList<EObject>();
    }

    /**
     * Gets all Upstream Requirements from a starting point.
     * 
     * @param starting The model object representing the starting point.
     * @return A collection of Upstream Requirements found from the starting point.
     */
    public static Collection<Requirement> getUpstreams(EObject starting)
    {
        Collection<Requirement> result = new ArrayList<Requirement>();
        if (starting == null)
        {
            return result ;
        }
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
            return getUpstreams(resource.getContents().get(0));
        }
        return Collections.emptyList();
    }
    
    /**
     * Gets all Upstream Requirements for a Resource.
     * 
     * @param resource The requirement model as an EMF {@link Resource}
     * @return A collection of Upstream Requirements found from the starting point
     */
    public static Collection<Requirement> getAllUpstreams(ResourceSet set)
    {
        if (set != null)
        {
            Set<Requirement> result = new HashSet<Requirement>();
            for (Resource r : set.getResources()){
                if (RequirementResource.FILE_EXTENSION.equals(r.getURI().fileExtension())){
                    result.addAll(getUpstreams(r.getContents().get(0)));
                }
            }
            return result;
        }
        return Collections.emptyList();
    }
    
    /**
     * Gets all Upstream Requirements for a Resource.
     * 
     * @param resource The requirement model as an EMF {@link Resource}
     * @return A collection of Upstream Requirements found from the starting point
     */
    public static Collection<org.topcased.requirement.Requirement> getAllCurrents(ResourceSet set)
    {
        if (set != null)
        {
            Set<org.topcased.requirement.Requirement> result = new HashSet<org.topcased.requirement.Requirement>();
            for (Resource r : set.getResources()){
                if (RequirementResource.FILE_EXTENSION.equals(r.getURI().fileExtension())){
                    result.addAll(getAllCurrents(r));
                }
            }
            return result;
        }
        return Collections.emptyList();
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
        for (Setting setting : getCrossReferences(req))
        {
            if (setting.getEObject() instanceof AttributeLink)
            {
                AttributeLink link = (AttributeLink) setting.getEObject();
                if (link.getPartial())
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the cross references of the source object from the requirement cache adapter
     * 
     * @param source
     * @return collection of settings
     */
    public static Collection<Setting> getCrossReferences(EObject source)
    {
        Collection<Setting> collection = Collections.<Setting> emptyList();
        if (source == null)
        {
            return collection;
        }
        ECrossReferenceAdapter adapter = RequirementCacheAdapter.getExistingRequirementCacheAdapter(source);
        if (adapter == null)
        {
            adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(source);
        }
        if (adapter != null)
        {
            collection = adapter.getNonNavigableInverseReferences(source);
        }
        return collection;
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
     * Get a resource from a file and load it
     * 
     * @param file The model path
     * @return the resource
     */
    public static Resource getResource(IPath path)
    {
        ResourceSet resourceSet = new ResourceSetImpl();
        URI fileURI = URI.createPlatformResourceURI(path.toString(), true);
        return resourceSet.getResource(fileURI, true);
    }

    /**
     * Get a file path from a resource URI
     * 
     * @param resourceURI The resource URI
     * @return the model path
     */
    public static IPath getPath(URI resourceURI)
    {
        String scheme = resourceURI.scheme();
        IPath path = null;
        if ("platform".equals(scheme)) //$NON-NLS-1$
        {
            path = Path.fromPortableString(resourceURI.toPlatformString(true));
        }
        else if ("file".equals(scheme)) //$NON-NLS-1$
        {
            URI platformURI = resourceURI.deresolve(URI.createURI(ResourcesPlugin.getWorkspace().getRoot().getLocationURI().toString() + "/"), false, false, true);
            path = new Path("/" + platformURI.toString());
        }
        return path;
    }

    /**
     * Get a file path from a resource
     * 
     * @param resource The resource
     * @return the model path
     */
    public static IPath getPath(Resource resource)
    {
        URI uri = resource.getURI();
        return getPath(uri);
    }

    /**
     * Gets the IFile of a resource
     * 
     * @param resource
     * 
     * @return the IFile of a resource
     */
    public static IFile getFile(Resource resource)
    {
        IPath path = getPath(resource);

        if (path != null)
        {
            return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
        }
        return null;
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
            URI uri = resource.getURI();
            IResource toDelete = ResourcesPlugin.getWorkspace().getRoot().findMember(uri.toPlatformString(true));
            if (toDelete != null && toDelete.isAccessible() && toDelete.exists())
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
     * Loads the requirement model if required.
     * 
     * @param uri The uri of the requirement model
     * @param domain The shared editing domain
     */
    public static void loadRequirementModel(URI uri, EditingDomain domain)
    {
        // Gets the requirement model from the editing domain or loads it if
        // it's the first time
        domain.getResourceSet().getResource(uri, true);
        if (domain instanceof AdapterFactoryEditingDomain)
        {
            AdapterFactoryEditingDomain adapterFactoryDomain = (AdapterFactoryEditingDomain) domain;
            if (adapterFactoryDomain.getAdapterFactory() instanceof ComposedAdapterFactory)
            {
                ComposedAdapterFactory factory = (ComposedAdapterFactory) adapterFactoryDomain.getAdapterFactory();
                factory.addAdapterFactory(RequirementUtils.getAdapterFactory());
            }
        }
    }

    /**
     * Unloads the requirement model from the editing domain
     * 
     * @param domain The shared editing domain
     * @return true if the resource has been successfully removed from the editing domain
     */
    public static boolean unloadRequirementModel(EditingDomain domain)
    {
        if (domain instanceof AdapterFactoryEditingDomain)
        {
            AdapterFactoryEditingDomain adapterFactoryDomain = (AdapterFactoryEditingDomain) domain;
            if (adapterFactoryDomain.getAdapterFactory() instanceof ComposedAdapterFactory)
            {
                ComposedAdapterFactory factory = (ComposedAdapterFactory) adapterFactoryDomain.getAdapterFactory();
                factory.removeAdapterFactory(RequirementUtils.getAdapterFactory());
            }
        }

        Resource requirementRsc = getRequirementModel(domain);
        if (requirementRsc != null)
        {
            // Unload the requirement resource and notify commands that the
            // hasRequirement variable has changed
            requirementRsc.unload();

            return domain.getResourceSet().getResources().remove(requirementRsc);
        }
        return false;
    }

    /**
     * Gets the requirement model as an EMF resource.
     * 
     * @param domain the editing domain of the active modeler
     * @return the requirement model as a requirement resource
     */
    public static RequirementResource getRequirementModel(EditingDomain domain)
    {
        if (domain != null)
        {
            for (Resource resource : domain.getResourceSet().getResources())
            {
                if (resource instanceof RequirementResource)
                {
                    return (RequirementResource) resource;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given editing domain contains a loaded requirement model related to other resources.
     * 
     * @param domain the editing domain of the active modeler
     * @return true if the requirement model is found inside the editing domain, false otherwise.
     */
    public static boolean hasRequirementModel(EditingDomain domain)
    {
        Resource rsc = getRequirementModel(domain);
        return rsc != null && rsc.isLoaded();
    }

    /**
     * Gets a set of models loaded in the Topcased editing domain.
     * 
     * @return all models loaded as a set of Resources.
     * @deprecated use {@link #getAllObjects(EClass)} instead
     */
    public static Set<Resource> getTargetModels()
    {
        Set<Resource> toReturn = new HashSet<Resource>();
        IEditorServices services = getSpecificServices(null);
        if (services != null)
        {
            return Collections.singleton(services.getModelResource(null));
        }
        return toReturn;
    }

    /**
     * Gets the {@link RequirementProject} model object contained in this EMF resource.
     * 
     * @param requirement The requirement model
     * @return The {@link RequirementProject} found inside this resource.
     */
    public static RequirementProject getRequirementProject(EditingDomain editingDomain)
    {
        RequirementResource requirementModel = RequirementUtils.getRequirementModel(editingDomain);
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
        RequirementResource requirementModel = RequirementUtils.getRequirementModel(editingDomain);
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
        else
        {
            EObject tmp = getRoot(requirement, UpstreamModel.class);
            if (tmp instanceof UpstreamModel)
            {
                return (UpstreamModel) tmp ;
            }
        }
        return null;
    }

    /**
     * Gets the {@link Document} model objects contained under the {@link UpstreamModel}.
     * 
     * @param requirement The resource representing a requirement model
     * @return the upstream model found.
     */
    public static List<Document> getUpstreamDocuments(Resource requirement)
    {
        UpstreamModel upstreamModel = getUpstreamModel(requirement);
        if (upstreamModel != null)
        {
            return upstreamModel.getDocuments();
        }
        return Collections.emptyList();
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
     * Gets the Deleted Chapter model object contained in a Requirement Project.
     * 
     * @param editingDomain The Deleted chapter must be searched inside the domain.
     * @return the special Deleted chapter found.
     */
    public static DeletedChapter getDeletedChapter(EditingDomain editingDomain)
    {
        RequirementProject project = getRequirementProject(editingDomain);
        if (project != null)
        {
            for (SpecialChapter chapter : project.getChapter())
            {
                if (chapter instanceof DeletedChapter)
                {
                    return (DeletedChapter) chapter;
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
        listObjects.add(""); //$NON-NLS-1$
        if (selected instanceof AttributeAllocate)
        {
            // add all the model elements
            listObjects.addAll(RequirementUtils.getAllObjects(EcorePackage.eINSTANCE.getEModelElement()));
        }
        else
        {
            if (!(selected instanceof AttributeLink))
            {
                listObjects.addAll(RequirementUtils.getAllObjects(EcorePackage.eINSTANCE.getEModelElement()));
            }
            ObjectAttribute objAtt = (ObjectAttribute) selected;
            listObjects.addAll(RequirementUtils.getAllUpstreams(objAtt.eResource().getResourceSet()));
            listObjects.addAll(RequirementUtils.getAllCurrents(objAtt.eResource().getResourceSet()));
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
    public static void openDiagramEditor(IPath resourceToOpen)
    {
        try
        {
            IFile fileToOpen = ResourcesPlugin.getWorkspace().getRoot().getFile(resourceToOpen);
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
    public static boolean closeDiagramEditor(IPath resourceToClose)
    {
        boolean closed = false;
        // Look for a reference to the target model editor ?
        for (IEditorReference editorRef : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences())
        {
            try
            {
                String resourceName = editorRef.getEditorInput().getName();
                if (resourceToClose.lastSegment().equals(resourceName))
                {
                    IEditorPart part = editorRef.getEditor(false);
                    if (part != null && part instanceof IEditorPart)
                    {
                        IEditorPart editor = (IEditorPart) part;
                        editor.doSave(new NullProgressMonitor());
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

    /**
     * 
     * Notify commands that the isImpacted variable has changed. Handle the enablement/disablement of commands when no
     * current requirement are impacted
     */
    public static void fireIsImpactedVariableChanged()
    {
        Boolean noRequirementImpacted = true;

        IEditorServices services = getSpecificServices(null);

        // Modeler modeler = Utils.getCurrentModeler();
        ISourceProviderService spc = (ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
        RequirementModelSourceProvider myPro = (RequirementModelSourceProvider) spc.getSourceProvider(RequirementModelSourceProvider.IS_IMPACTED);

        if (services != null && myPro != null)
        {
            Resource requirement = getRequirementModel(services.getEditingDomain(null));
            if (requirement != null)
            {
                Collection<EObject> allRequirement = RequirementUtils.getAllObjects(requirement, CurrentRequirement.class);
                // checks that all CurrentRequirement are marked as not
                // impacted.
                for (EObject aReq : allRequirement)
                {
                    if (aReq instanceof CurrentRequirement && ((CurrentRequirement) aReq).isImpacted())
                    {
                        // action must be disabled.
                        noRequirementImpacted = false;
                        break;
                    }
                }
                myPro.setIsImpactedState(noRequirementImpacted);
            }
        }

        // if (modeler != null && myPro != null)
        // {
        // Resource requirement = RequirementUtils.getRequirementModel(modeler.getEditingDomain());
        // if (requirement != null)
        // {
        // Collection<EObject> allRequirement = RequirementUtils.getAllObjects(requirement, CurrentRequirement.class);
        // // checks that all CurrentRequirement are marked as not
        // // impacted.
        // for (EObject aReq : allRequirement)
        // {
        // if (aReq instanceof CurrentRequirement && ((CurrentRequirement) aReq).isImpacted())
        // {
        // // action must be disabled.
        // isImpacted = false;
        // break;
        // }
        // }
        // myPro.setIsImpactedState(isImpacted);
        // }
        // }
    }

    /**
     * 
     * Notify commands that the hasRequirement variable has changed. Handle the enablement/disablement of commands when
     * there is a requirement model attached to the current modeler.
     */
    public static void fireHasRequirementVariableChanged()
    {
        boolean enable = false;

        ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
        RequirementModelSourceProvider provider = (RequirementModelSourceProvider) service.getSourceProvider(RequirementModelSourceProvider.HAS_REQUIREMENT_MODEL);

        IEditorServices services = getSpecificServices(null);
        if (services != null)
        {
            EditingDomain domain = services.getEditingDomain(null);
            if (domain != null)
            {
                IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(domain);
                if (policy != null)
                {
                    enable = policy.getLinkedTargetModel(domain.getResourceSet()) != null;
                }
                else
                {
                    String extension = domain.getResourceSet().getResources().get(0).getURI().fileExtension();
                    String msg = NLS.bind(Messages.getString("ModelAttachmentPolicyManager.0"), extension);
                    RequirementCorePlugin.log(msg, Status.ERROR, null);//$NON-NLS-1$
                    enable = false;
                }
            }
        }

        // Modeler modeler = Utils.getCurrentModeler();
        //
        // if (modeler != null)
        // {
        // IModelAttachmentPolicy policy =
        // ModelAttachmentPolicyManager.getInstance().getModelPolicy(modeler.getEditingDomain());
        // if (policy != null)
        // {
        // enable = policy.getLinkedTargetModel(modeler.getEditingDomain().getResourceSet()) != null;
        // }
        // else
        // {
        // enable =
        // DefaultAttachmentPolicy.getInstance().getLinkedTargetModel(modeler.getEditingDomain().getResourceSet()) !=
        // null;
        // }
        // }
        // else
        // {
        // enable = false;
        // ModelSet resSet = EditorUtils.getDiResourceSet();
        // if (resSet != null)
        // {
        // IModel reqModel = resSet.getModel("org.topcased.requirement.papyrus.requirementModel");
        // if (reqModel != null)
        // {
        // enable = true;
        // }
        // }
        // }

        provider.setHasRequirementState(enable);
    }

    /**
     * 
     * Notify commands that the IsSectionEnabled variable has changed. Handle the enablement/disablement of commands
     * when the requirement property section is shown/hidden.
     */
    public static void fireIsSectionEnabledVariableChanged(boolean enable)
    {
        ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
        RequirementModelSourceProvider provider = (RequirementModelSourceProvider) service.getSourceProvider(RequirementModelSourceProvider.IS_SECTION_ENABLED);

        provider.setIsSectionEnabledState(enable);
    }

    /**
     * Get the currently active editor, whatever its type
     * 
     * @return the active editor or null
     */
    public static IEditorPart getCurrentEditor()
    {
        IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench != null)
        {
            IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
            if (activeWorkbenchWindow != null)
            {
                IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
                if (activePage != null)
                {
                    return activePage.getActiveEditor();
                }
            }
        }
        return null;
    }

    /**
     * Get the services specific to an editor.
     * 
     * @param editor the editor to find services for, or null to take the currently active editor
     * @return appropriated services or null if none
     */
    public static IEditorServices getSpecificServices(IEditorPart editor)
    {
        if (editor == null)
        {
            editor = getCurrentEditor();
        }
        if (editor != null)
        {
            return SupportingEditorsManager.getInstance().getServices(editor);
        }
        return null;
    }

    /**
     * Get ident to use for a document containing deleted requirements
     * 
     * @param date the deletion date
     * @return ident to use
     */
    public static String getDeletedDocumentIdent(Date date)
    {
        DateFormat dateFormat = new SimpleDateFormat(DELETED_DOCUMENT_DATE_FORMAT);
        return String.format(DELETED_DOCUMENT_IDENT_FORMAT, dateFormat.format(date));
    }

    /**
     * Test if ident of a document correspond to a document containing deleted requirements
     * 
     * @param ident ident to test
     * @return true if format is correct for deleted documents
     */
    public static boolean isDeletedDocumentIdent(String ident)
    {
        if (ident == null) {
            return false;
        }
        return DELETED_DOCUMENT_PATTERN.matcher(ident).matches();
    }

    public static Set<CurrentRequirement> getLinkedCurrentRequirements(ttm.Requirement upstreamReq)
    {
        Set<CurrentRequirement> currentReqs = new HashSet<CurrentRequirement>();
        if (upstreamReq.eResource() != null)
        {
            for (Setting setting : getCrossReferences(upstreamReq))
            {
                if (setting.getEObject() instanceof AttributeLink)
                {
                    AttributeLink attr = (AttributeLink) setting.getEObject();
                    if (attr.eContainer() instanceof CurrentRequirement)
                    {
                        currentReqs.add((CurrentRequirement) attr.eContainer());
                    }
                }
            }
        }
        return currentReqs;
    }

    /**
     * Predicate checking if an attribute points towards a trash.
     * 
     * @param attribute
     */
    protected static boolean isTrash(ObjectAttribute attribute)
    {
        boolean result = false; 
        if (attribute != null){
            EObject value = attribute.getValue();
            if (value != null){
                result = attribute.getValue().eContainer() instanceof TrashChapter;                
            }
        }
        return result ;
    }

    /**
     * Predicate checking if an requirement is trash.
     * 
     * @param req
     */
    protected static boolean isTrash(org.topcased.requirement.Requirement req)
    {
        boolean result = false; 
        if (req != null){
            result = req.eContainer() instanceof TrashChapter;            
        }
        return result;
    }

    /**
     * Returns whether a requirement contains a trash attribute. In other words, contains
     * 
     * @return
     */
    public static boolean refersToTrash(EObject eObject)
    {
        if (eObject instanceof org.topcased.requirement.Requirement)
        {
            org.topcased.requirement.Requirement req = (org.topcased.requirement.Requirement) eObject;
            EList<org.topcased.requirement.Attribute> attributes = req.getAttribute();

            // Tests whether one of the attributes is a Trash.
            for (org.topcased.requirement.Attribute attribute : attributes)
            {
                if (attribute instanceof ObjectAttribute)
                {
                    if (isTrash((ObjectAttribute) attribute))
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        else if (eObject instanceof ObjectAttribute)
        {
            return isTrash((ObjectAttribute) eObject);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Filters the document requirements according to the deletion parameters 
     * @param doc
     * @param deletionParameters
     */
    public static void filterRequirements(Document doc, DeletionParameters deletionParameters)
    {
        if(doc != null)
        {
            Collection<Requirement> upstreamRequirements = RequirementUtils.getUpstreams(doc);
            for(Requirement req:upstreamRequirements)
            {
                if(isReqFiltered(req, deletionParameters))
                {
                    req.getParent().getChildren().remove(req);
                }
            }
        }
    }
    
    
    /**
     * Checks if a given requirement should be filtered according to deletionParameters 
     * @param req The requirement to check
     * @param deletionParameters The deletion parameters
     * @return True if the requirement should be filtered
     */
    public static boolean isReqFiltered(Requirement req, DeletionParameters deletionParameters)
    {
        if (deletionParameters != null && !deletionParameters.getFilterRegexAttributes().isEmpty())
        {
            boolean isAnd = deletionParameters.isIsAnd();
            // in case of isAnd is true result must initialized true (and operation after)
            // in case of isAnd is false var must be initialized false (or operation after)
            boolean result = isAnd ;
            for(DeletionParemeter filterParam : deletionParameters.getFilterRegexAttributes())
            {
                boolean attributeFound = false ;
                for (ttm.Attribute att : req.getAttributes())
                {
                    if(filterParam.getNameAttribute().equals(att.getName()))
                    {
                        attributeFound = true ;
                        Pattern deletionPatternAttribute = Pattern.compile(filterParam.getRegexAttribute(), Pattern.CASE_INSENSITIVE);
                        if (isAnd)
                        {
                            result = result && deletionPatternAttribute.matcher(att.getValue()).matches();
                        }
                        else 
                        {
                            result = result || deletionPatternAttribute.matcher(att.getValue()).matches();
                        }
                    }
                }
                if (!attributeFound)
                {
                    if (isAnd)
                    {
                        result = false ;
                    }
                }
            }
            return result ;
        }
        return false;
    }
    
    /**
     * Checks if the given eobject is contained in trash chapter
     * @param eObject The eObject to check
     * @return True if the eObject is contained in the trash chapter
     */
    public static boolean istrashChapterChild(EObject eObject)
    {
        while (eObject != null)
        {
            if (eObject instanceof TrashChapter)
            {
                return true;
            }
            eObject = eObject.eContainer();
        }
        return false;
    }
}
