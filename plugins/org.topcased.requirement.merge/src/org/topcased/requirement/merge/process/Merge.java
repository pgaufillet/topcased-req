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
 *  Caroline Bourdeu d'Aguerre (ATOS ORIGIN INTEGRATION) caroline.bourdeudaguerre@atosorigin.com - Initial API and implementation
 *  Maxime AUDRAIN (CS) - maxime.audrain@c-s.fr 
 *****************************************************************************/
package org.topcased.requirement.merge.process;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.ProblemChapter;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.TrashChapter;
import org.topcased.requirement.UntracedChapter;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.IRequirementFactoryProvider;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.merge.Activator;
import org.topcased.requirement.merge.utils.Couple;
import org.topcased.requirement.util.RequirementResource;

public class Merge
{
	private static int NB_MEG_FOR_MEMORY_FREE = 50 ;
	
	private static int MEMORY_FREE = NB_MEG_FOR_MEMORY_FREE * 1024 * 1024 ;
	
	private static int ONE_MEG = 1 * 1024 * 1024 ;
	
	private Map<String, Boolean> inputs;

    private Vector<Couple> models;

    private RequirementProject requirementProject;

    private RequirementFactory factory = RequirementFactory.eINSTANCE;

    private Map<String, EObject> objectsCreated = new HashMap<String, EObject>();
    
    private Map<EObject, EObject> correspondances = new HashMap<EObject, EObject>();
    private Map<String, EObject> reqCorrespondances = new HashMap<String, EObject>();
    
    private LinkedList<AssignPPA> ppas = new LinkedList<Merge.AssignPPA>();

    private String output;

    private IProgressMonitor thisMonitor;

    private ProblemChapter theProblemChapter;

    private UntracedChapter theUntracedChapter;
    
    private ResourceSet resourceSet = null ;

    public Merge(Map<String, Boolean> inputs, String output)
    {
        super();
        this.inputs = inputs;
        this.output = output;
        models = new Vector<Couple>();
    }

    public void process()
    {
        process(new NullProgressMonitor());
    }

    public void process(IProgressMonitor monitor)
    {
        try
        {
        	long time = System.currentTimeMillis() ;
        	if (Activator.getDefault().shouldTrace())
            {
        		Activator.getDefault().log("Start process ---");
            }
            thisMonitor = monitor;
            if (thisMonitor == null)
            {
                thisMonitor = new NullProgressMonitor();
            }
            if (Activator.getDefault().shouldTrace())
            {
            	thisMonitor = new TimeProgressMonitor(time, thisMonitor);
            }
            thisMonitor.beginTask("Merge", 9);
            if (thisMonitor.isCanceled())
            {
            	return ;
            }
            thisMonitor.subTask("Load models");
            this.getRequirementsFile();
            thisMonitor.worked(1);
            if (models.size() > 0)
            {
            	if (thisMonitor.isCanceled())
                {
                	return ;
                }
            	thisMonitor.subTask("Output initialization");
                this.initRequirementFile();
                thisMonitor.worked(1);
                if (thisMonitor.isCanceled())
                {
                	return ;
                }
                thisMonitor.subTask("Attribute initialization");
                this.initAttributeUpstream();
                thisMonitor.worked(1);
                if (thisMonitor.isCanceled())
                {
                	return ;
                }
                this.mergeOtherCategory();
                thisMonitor.worked(1);
                if (thisMonitor.isCanceled())
                {
                	return ;
                }
                thisMonitor.subTask("Structure initialization");
                this.initStructureRequirment();
                thisMonitor.worked(1);
                if (thisMonitor.isCanceled())
                {
                	return ;
                }
                thisMonitor.subTask("Manage current requirements");
                this.copyRequirement();
                thisMonitor.worked(1);
                if (thisMonitor.isCanceled())
                {
                	return ;
                }
                thisMonitor.subTask("Assign references");
                this.assignPpa();
                thisMonitor.worked(1);
                if (thisMonitor.isCanceled())
                {
                	return ;
                }
                thisMonitor.subTask("Filter");
            	clear();
                this.filter();
                thisMonitor.worked(1);
                if (thisMonitor.isCanceled())
                {
                	return ;
                }
                thisMonitor.subTask("Save");
                this.save();
                if (Activator.getDefault().shouldTrace())
                {
                	time = System.currentTimeMillis() - time ;
                	Activator.getDefault().log("End : " + time);
                }
                monitor.worked(1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            monitor.done();
        }
    }

    private void assignPpa() {
    	for (AssignPPA ppa : ppas)
    	{
    		ppa.run();
    	}
	}

	private void filter()
    {
        AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new ReflectiveItemProviderAdapterFactory(), new BasicCommandStack());
        CompoundCommand command = new CompoundCommand();
        Collection<HierarchicalElement> todeletes = new LinkedList<HierarchicalElement>();
        for (HierarchicalElement h : requirementProject.getHierarchicalElement())
        {
            if (delete(h, domain, command))
            {
                todeletes.add(h);
            }
        }
        for (HierarchicalElement h : todeletes)
        {
            command.append(DeleteCommand.create(domain, h));
        }
        domain.getCommandStack().execute(command);
        domain.getCommandStack().flush();
    }

    private boolean delete(HierarchicalElement root, AdapterFactoryEditingDomain domain, CompoundCommand command)
    {
        boolean deleteMe = true;
        Collection<HierarchicalElement> todeletes = new LinkedList<HierarchicalElement>();
        if (!root.getRequirement().isEmpty())
        {
            deleteMe = false;
        }
        for (HierarchicalElement h : root.getChildren())
        {
            if (!(h instanceof CurrentRequirement))
            {
                if (delete(h, domain, command))
                {
                    todeletes.add(h);
                }
                else
                {
                    deleteMe = false;
                }
            }
        }
        if (!deleteMe)
        {
            for (HierarchicalElement h : todeletes)
            {
                command.append(DeleteCommand.create(domain, h));
            }
        }
        return deleteMe;
    }

    private void save()
    {
        try
        {
        	ResourceSet set = getResourceSet(null);
            Map<Object, Object> options = new HashMap<Object, Object>();
            // augment the time but decrease memory consumption
            options.put(XMLResource.OPTION_FLUSH_THRESHOLD, getMemoryAvailable());
            options.put(XMLResource.OPTION_USE_FILE_BUFFER, Boolean.TRUE);
			requirementProject.eResource().save(options);
			set.eAdapters().clear();
            for (int i = 0; i < set.getResources().size(); i++) {
				try {
					set.getResources().get(i).unload();
				} catch (Exception e) {
				}
			}
            set.getResources().clear();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

	private Integer getMemoryAvailable() {
		long free = Runtime.getRuntime().freeMemory();
		if (free > Integer.MAX_VALUE)
		{
			return Integer.MAX_VALUE;
		}
		if (free - MEMORY_FREE > ONE_MEG)
		{
			return (int)(free - MEMORY_FREE) ;
		}
		else
		{
			return ONE_MEG;
		}
	}

	private void clear() {
		// clear data to free memory
		this.objectsCreated.clear();
		this.inputs.clear();
		this.correspondances.clear();
		this.reqCorrespondances.clear();
		this.models.clear();
		this.theProblemChapter = null ;
		this.theUntracedChapter = null ;
		this.ppas.clear();
	}

    public void getRequirementsFile()
    {
        SubProgressMonitor subMonitor = new SubProgressMonitor(thisMonitor, 1);
        Set<String> keys = inputs.keySet();
        Iterator<String> it = keys.iterator();
        // for each inputs
        while (it.hasNext())
        {
            // Get the diagram
            String file = it.next();
            URI uri = URI.createURI(file);
            subMonitor.subTask("load " + uri.toString());
            IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(uri.fileExtension());
            ResourceSet resourceSetImpl = getResourceSet(policy);
			EObject eobject = resourceSetImpl.getResource(uri, true).getContents().get(0);

            // if (eobject instanceof Diagrams)
            {
                RequirementProject project = null;
                // Diagrams diagram = (Diagrams) eobject;

                // Get the associated requirement model
                if (policy != null)
                {
                    project = policy.getRequirementProjectFromTargetMainResource(eobject.eResource());
                    // FIXME whenpolicy will not be bugged
                    if (project != null && project.eResource() != null && !project.eResource().getURI().trimFileExtension().equals(eobject.eResource().getURI().trimFileExtension()))
                    {
                    	try
                    	{
                    		Resource resource = resourceSetImpl.getResource(eobject.eResource().getURI().trimFileExtension().appendFileExtension("requirement"), true);
                    		if (resource != null && resource.getContents().size() > 0)
                    		{
                    			project = (RequirementProject) resource.getContents().get(0);
                    		}
                    	}
                    	catch (Exception e)
                    	{
                    		Activator.getDefault().log("no requirement found for " + project.eResource().getURI().toString());
                    	}
                    }
                }

                if (project != null)
                {
                    // get the associate model
//                    URI uriDiagram = URI.createURI(file.substring(0, file.length() - 2));
                    EObject eobjectModel = getModelObject(project);

                    // Create a new Triplet
                    if (eobjectModel != null)
                    {
                    	Couple t = new Couple(eobjectModel, project, inputs.get(file));
                    	// Triplet t = new Triplet(eobjectModel, (Diagrams) eobject, project, inputs.get(file));
                    	models.add(t);
                    }
                    else
                    {
                    	Activator.getDefault().log("The requirement is not linked to a model : " + project.eResource().getURI().toString());
                    }
                    	
                    	
                }
            }
            subMonitor.worked(1);
        }

    }

    protected EObject getModelObject(RequirementProject project) {
    	// FIXME when generic methods will exist
    	for (Iterator<EObject> i = EcoreUtil.getAllProperContents(project, false) ; i.hasNext() ; )
    	{
    		EObject next = i.next();
			if (next instanceof HierarchicalElement) {
				HierarchicalElement h = (HierarchicalElement) next;
				EObject element = h.getElement();
				if (element != null && element.eResource() != null)
				{
					Resource eResource = project.eResource();
					if (eResource != null && eResource.getURI() != null && element.eResource().getURI() != null)
					{
						if (eResource.getURI().trimFileExtension().equals(element.eResource().getURI().trimFileExtension()))
						{
							return element.eResource().getContents().get(0);
						}
					}
				}
			}
    	}
		return null;
	}

	public void initRequirementFile()
    {
        requirementProject = factory.createRequirementProject();
        ResourceSet set = getResourceSet(null);
        Resource r = set.createResource(URI.createURI(output));
        r.getContents().add(requirementProject);

    }

	private ResourceSet getResourceSet(IModelAttachmentPolicy policy) {
		if (resourceSet == null)
		{
			if (policy instanceof IRequirementFactoryProvider)
			{
				IRequirementFactoryProvider provider = (IRequirementFactoryProvider) policy;
				if (provider.provides(ResourceSet.class))
				{
					resourceSet = provider.create(ResourceSet.class);
				}
			}
			if (resourceSet == null)
			{
				resourceSet = new ResourceSetImpl()
				{
					@Override
					public Resource getResource(URI uri, boolean loadOnDemand) {
						Resource r = super.getResource(uri, loadOnDemand);
						if (r instanceof ResourceImpl)
						{
							ResourceImpl impl = (ResourceImpl) r ;
							if (impl.getIntrinsicIDToEObjectMap() == null)
							{
								impl.setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());
							}
						}
						return r ;
					}
				};
			}
			resourceSet.getLoadOptions().put(XMLResource.OPTION_DISABLE_NOTIFY, true);
			resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
			resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_ATTACHMENT, true);
		}
		return resourceSet;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T copy (EObject eObject)
	{
		T t = (T) correspondances.get(eObject);
		if (t == null)
		{
			t = (T) objectsCreated.get(getID(eObject));
		}
		if (t == null)
		{
			if (t instanceof ttm.Requirement) {
				ttm.Requirement req = (ttm.Requirement) t;
				EObject eObject2 = reqCorrespondances.get(req.getIdent());
				if (eObject2 != null)
				{
					return (T) eObject2;
				}
			}
			else if (t instanceof Requirement)
			{
				Requirement req = (Requirement) t;
				EObject eObject2 = reqCorrespondances.get(req.getIdentifier());
				if (eObject2 != null)
				{
					return (T) eObject2;
				}
			}
		}
		if (t == null)
		{
			t = copy2(eObject);
		}
		return t;
	}

	
    private <T> T copy2(EObject eObject) {
    	Copier copier = new Copier()
    	{
			@Override
			public EObject copy(EObject eObject) {
				EObject result = super.copy(eObject);
				if (eObject instanceof ttm.Requirement)
				{
					ttm.Requirement req = (ttm.Requirement)eObject;
					EObject previousValue = reqCorrespondances.put(req.getIdent(), result);
				}
				else if (eObject instanceof CurrentRequirement)
				{
					CurrentRequirement req = (CurrentRequirement) eObject;
					reqCorrespondances.put(req.getIdentifier(), result);
				}
				objectsCreated.put(getID(eObject), result);
				correspondances.put(eObject, result);
				return result ;
			}
    	};
        EObject result = copier.copy(eObject);
        copier.copyReferences();
        
        @SuppressWarnings("unchecked")T t = (T)result;
        return t;
	}

	public void initAttributeUpstream()
    {
		// name and description
		requirementProject.setIdentifier(models.get(0).getRequirement().getIdentifier());
		requirementProject.setShortDescription(models.get(0).getRequirement().getIdentifier());
		
        // Add upstream model
        requirementProject.setUpstreamModel((UpstreamModel) copy(models.get(0).getRequirement().getUpstreamModel()));
        
        // Add attribute configuration
        requirementProject.setAttributeConfiguration((AttributeConfiguration) copy(models.get(0).getRequirement().getAttributeConfiguration()));

        // Add problemChapter folder
        theProblemChapter = (ProblemChapter) copy(getProblemChapter(models.get(0).getRequirement()));
        theProblemChapter.getRequirement().clear();
        requirementProject.getChapter().add(theProblemChapter);

        // Add TrashChapter folder
        TrashChapter theTrashChapter = (TrashChapter) copy(getTrashChapter(models.get(0).getRequirement()));
        theTrashChapter.getRequirement().clear();
        requirementProject.getChapter().add(theTrashChapter);

        // Add UntracedChapter folder
        theUntracedChapter = (UntracedChapter) copy(getUntracedChapter(models.get(0).getRequirement()));
        theUntracedChapter.getRequirement().clear();
        requirementProject.getChapter().add(theUntracedChapter);

    }

    private void mergeOtherCategory()
    {

        // For all models
        for (Iterator<Couple> iterator = models.iterator(); iterator.hasNext();)
        {

            Couple t = (Couple) iterator.next();

            // get the current requirement
            RequirementProject r = t.getRequirement();

            // Add problems
            List<Requirement> problemChapterRequirement = getProblemChapterRequirement(r);
            if (problemChapterRequirement != null)
            {
                for (Requirement p : problemChapterRequirement)
                {
                    // If the problem is not present
                    if (!this.contains(getProblemChapterRequirement(requirementProject), p))
                    {
                        // Add the problem
                        if (theProblemChapter != null)
                        {
                            copyAndAdd(p, theProblemChapter);
                        }
                    }
                }
            }

            // Add untraced
            List<Requirement> untracedChapterRequirement = getUntracedChapterRequirement(r);
            if (untracedChapterRequirement != null)
            {
                for (Requirement u : untracedChapterRequirement)
                {
                    // If the problem is not present
                    if (!this.contains(getUntracedChapterRequirement(requirementProject), u))
                    {
                        // Add the problem
                        if (theUntracedChapter != null)
                        {
                            copyAndAdd(u, theUntracedChapter);
                        }
                    }
                }
            }
        }

    }

    private boolean contains(List<Requirement> list, Requirement r)
    {
        boolean contain = false;
        for (Requirement rCurrent : list)
        {
            if (EcoreUtil.equals(rCurrent, r))
            {
                contain = true;
            }
        }
        return contain;
    }

    private void initStructureRequirment()
    {
        for (Iterator<Couple> iterator = models.iterator(); iterator.hasNext();)
        {
            Couple t = (Couple) iterator.next();
            EObject model = t.getModel();

            // if it is not a sub model
            if (!t.isSubModel())
            {
                HierarchicalElement h = createHierachicalElement(model);
                // Add the hierarchical element to the
                // currentHierarchicalElement
                requirementProject.getHierarchicalElement().add(h);
                addHierarchicalElement(h, model.eContents());
            }
        }
    }

    private void addHierarchicalElement(HierarchicalElement currentHierarchicalElement, EList<EObject> list)
    {
        for (EObject object : list)
        {
            // Create a hierarchical element link to o
            HierarchicalElement h = createHierachicalElement(object);

            // Add the hierarchical element to the currentHierarchicalElement
            currentHierarchicalElement.getChildren().add(h);

            // Add all object sub element to h
            if (object.eContents().size() > 0)
            {
                addHierarchicalElement(h, object.eContents());
            }
        }
    }

    private HierarchicalElement createHierachicalElement(EObject object)
    {
        HierarchicalElement h = factory.createHierarchicalElement();
        h.setElement(object);
        if (object.eResource() instanceof XMIResource) {
        	XMIResource res = (XMIResource) object.eResource();
        	objectsCreated.put(res.getID(object), h);
		}
        return h;
    }

    private List<Requirement> getProblemChapterRequirement(RequirementProject r)
    {
        ProblemChapter u = getProblemChapter(r);
        if (u != null)
        {
            return u.getRequirement();
        }
        return null;
    }

    private ProblemChapter getProblemChapter(RequirementProject r)
    {
        // Get all chapter
        for (SpecialChapter s : r.getChapter())
        {
            if (s instanceof ProblemChapter)
            {
                return (ProblemChapter) s;
            }
        }
        return null;
    }

    private List<Requirement> getUntracedChapterRequirement(RequirementProject r)
    {
        UntracedChapter u = getUntracedChapter(r);
        if (u != null)
        {
            return u.getRequirement();
        }
        return null;
    }

    private UntracedChapter getUntracedChapter(RequirementProject r)
    {
        // Get all chapter
        for (SpecialChapter s : r.getChapter())
        {
            if (s instanceof UntracedChapter)
            {
                return (UntracedChapter) s;
            }
        }
        return null;
    }

    private TrashChapter getTrashChapter(RequirementProject r)
    {
        // Get all chapter
        for (SpecialChapter s : r.getChapter())
        {
            if (s instanceof TrashChapter)
            {
                return (TrashChapter) s;
            }
        }
        return null;
    }

    private void copyRequirement()
    {
        for (Iterator<Couple> iterator = models.iterator(); iterator.hasNext();)
        {
            Couple t = (Couple) iterator.next();

            addRequirements(t);
        }
    }

    private void addRequirements(Couple t)
    {
        for (TreeIterator<EObject> i = EcoreUtil.getAllContents(t.getRequirement(),true); i.hasNext();)
        {
            EObject tmp = i.next();
            if (tmp instanceof Requirement)
            {
                Requirement current = (Requirement) tmp;
                if (current.eContainer() instanceof HierarchicalElement)
                {
                    HierarchicalElement hier = (HierarchicalElement) current.eContainer();
                    EObject element = hier.getElement();
                    if (element != null && element.eIsProxy())
                    {
                        element = (EObject) hier.eGet(RequirementPackage.Literals.HIERARCHICAL_ELEMENT__ELEMENT, true);
                    }
                    if (element != null && !element.eIsProxy())
                    {
                        HierarchicalElement hierToAdd = get(hier.getElement());
                        if (hierToAdd != null)
                        {
                            copyAndAdd(current, hierToAdd);
                        }
                    }
                }
            }
        }
    }

    private HierarchicalElement get(EObject samElement)
    {
    	if (samElement.eResource() instanceof XMIResource)
    	{
    		String id = getID(samElement);
    		EObject eObject = objectsCreated.get(id);
			return (HierarchicalElement) (eObject instanceof HierarchicalElement ? eObject : null);
    	}
        return null;
    }

	private String getID(EObject samElement) {
		return ((XMIResource)samElement.eResource()).getID(samElement);
	}

    private void copyAndAdd(Requirement current, HierarchicalElement hierToAdd)
    {
        Requirement copy = (Requirement) copy(current);
        hierToAdd.getRequirement().add(copy);
        assignLink(copy);
    }

    private void copyAndAdd(Requirement current, SpecialChapter chapter)
    {
        Requirement copy = (Requirement) copy(current);
        chapter.getRequirement().add(copy);
        assignLink(copy);
    }

    private void assignLink(Requirement copy)
    {
        for (Attribute a : copy.getAttribute())
        {
            if (a instanceof AttributeLink)
            {
                AttributeLink link = (AttributeLink) a;
                if (link.getValue() != null)
                {
                    Resource resource = link.getValue().eResource();
                    if (RequirementResource.FILE_EXTENSION.equals(resource.getURI().fileExtension()) && resource != copy.eResource())
                    {
                    	AssignPPA ppa = new AssignPPA(link.getValue(), link);
                    	ppas.add(ppa);
//                        link.setValue(getEquivalent(copy.eResource(), link.getValue()));
                    }
                }
            }
        }
    }

//    private EObject getEquivalent(Resource resource, EObject value)
//    {
//        for (TreeIterator<EObject> i = resource.getAllContents(); i.hasNext();)
//        {
//            EObject tmp = i.next();
//            if (EcoreUtil.equals(value, tmp))
//            {
//                return tmp;
//            }
//        }
//        return null;
//    }
    
    private class TimeProgressMonitor implements IProgressMonitor 
    {
    	private final long time;
		private final IProgressMonitor old;
		private int sum = 0 ;

		public TimeProgressMonitor (long time, IProgressMonitor old)
    	{
			this.time = time;
			this.old = old;
    		
    	}
		public void worked(int work) {
			old.worked(work);
			sum++;
			long newTime = System.currentTimeMillis() - time ;
			Activator.getDefault().log("-- step : " + sum + " / " + newTime + " | " + Runtime.getRuntime().freeMemory());
		}
		
		public void subTask(String name) {
			old.subTask(name);
		}
		
		public void setTaskName(String name) {
			old.setTaskName(name);
			long newTime = System.currentTimeMillis() - time ;
			Activator.getDefault().log("-- start task : " + name + " / " + newTime + " | " + Runtime.getRuntime().freeMemory());
		}
		
		public void setCanceled(boolean value) {
			old.setCanceled(value);
		}
		
		public boolean isCanceled() {
			return old.isCanceled();
		}
		
		public void internalWorked(double work) {
			old.internalWorked(work);
		}
		
		public void done() {
			old.done();
		}
		
		public void beginTask(String name, int totalWork) {
			old.beginTask(name, totalWork);
			long newTime = System.currentTimeMillis() - time ;
			System.out.println("-- debut task : " + name + " / " + newTime + " | " + Runtime.getRuntime().freeMemory());
		}
    }
    
    protected class AssignPPA 
    {
		private final EObject reference;
		private final AttributeLink newOne;

		public AssignPPA (EObject reference, AttributeLink newOne)
    	{
			this.reference = reference;
			this.newOne = newOne;
    	}
    	
    	public void run ()
    	{
    		EObject get = correspondances.get(reference);
    		if (get == null && reference instanceof ttm.Requirement)
    		{
    			get = reqCorrespondances.get(((ttm.Requirement)reference).getIdent());
    		}
    		if (get == null)
    		{
    			Activator.getDefault().log("no correspondance found for " + newOne.toString());
    		}
    		newOne.setValue(get);
    	}
    }
}
