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
import java.util.Collections;
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
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
    private Map<String, Boolean> inputs;

    private Vector<Couple> models;

    private RequirementProject requirementProject;

    private RequirementFactory factory = RequirementFactory.eINSTANCE;

    private Map<EObject, HierarchicalElement> objectsCreated = new HashMap<EObject, HierarchicalElement>();

    private String output;

    private IProgressMonitor thisMonitor;

    private ProblemChapter theProblemChapter;

    private UntracedChapter theUntracedChapter;

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
        	final IProgressMonitor old = monitor ;
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
            thisMonitor.beginTask("Process Filter", 7);
            this.getRequirementsFile();
            thisMonitor.worked(1);
            if (models.size() > 0)
            {
                this.initRequirementFile();
                thisMonitor.worked(1);
                this.initAttributeUpstream();
                thisMonitor.worked(1);
                this.mergeOtherCategory();
                thisMonitor.worked(1);
                this.initStructureRequirment();
                thisMonitor.worked(1);
                this.copyRequirement();
                thisMonitor.worked(1);
                this.filter();
                thisMonitor.worked(1);
                this.save();
                if (Activator.getDefault().shouldTrace())
                {
                	time = System.currentTimeMillis() - time ;
                	Activator.getDefault().log("End : " + time);
                }
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
            requirementProject.eResource().save(Collections.EMPTY_MAP);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void getRequirementsFile()
    {
        SubProgressMonitor subMonitor = new SubProgressMonitor(thisMonitor, 1);
        Set<String> keys = inputs.keySet();
        Iterator<String> it = keys.iterator();
        subMonitor.beginTask("Get requirements", keys.size());
        // for each inputs
        while (it.hasNext())
        {

            // Get the diagram
            String file = it.next();
            URI uri = URI.createURI(file);
            IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(uri.fileExtension());
            ResourceSet resourceSetImpl = createResourceSet(policy);
			EObject eobject = resourceSetImpl.getResource(uri, true).getContents().get(0);

            // if (eobject instanceof Diagrams)
            {
                RequirementProject project = null;
                // Diagrams diagram = (Diagrams) eobject;

                // Get the associated requirement model
                if (policy != null)
                {
                    project = policy.getRequirementProjectFromTargetMainResource(eobject.eResource());
                }

                if (project != null)
                {
                    // get the associate model
//                    URI uriDiagram = URI.createURI(file.substring(0, file.length() - 2));
                    EObject eobjectModel = getModelObject(project);

                    // Create a new Triplet
                    Couple t = new Couple(eobjectModel, project, inputs.get(file));
                    // Triplet t = new Triplet(eobjectModel, (Diagrams) eobject, project, inputs.get(file));
                    models.add(t);
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
        ResourceSet set = createResourceSet(null);
        Resource r = set.createResource(URI.createURI(output));
        r.getContents().add(requirementProject);

    }

	private ResourceSet createResourceSet(IModelAttachmentPolicy policy) {
		if (policy instanceof IRequirementFactoryProvider)
		{
			IRequirementFactoryProvider provider = (IRequirementFactoryProvider) policy;
			if (provider.provides(ResourceSet.class))
			{
				return provider.create(ResourceSet.class);
			}
		}
		return new ResourceSetImpl();
	}

    public void initAttributeUpstream()
    {
        // Add upstream model
        requirementProject.setUpstreamModel((UpstreamModel) EcoreUtil.copy(models.get(0).getRequirement().getUpstreamModel()));

        // Add attribute configuration
        requirementProject.setAttributeConfiguration((AttributeConfiguration) EcoreUtil.copy(models.get(0).getRequirement().getAttributeConfiguration()));

        // Add problemChapter folder
        theProblemChapter = (ProblemChapter) EcoreUtil.copy(getProblemChapter(models.get(0).getRequirement()));
        theProblemChapter.getRequirement().clear();
        requirementProject.getChapter().add(theProblemChapter);

        // Add TrashChapter folder
        TrashChapter theTrashChapter = (TrashChapter) EcoreUtil.copy(getTrashChapter(models.get(0).getRequirement()));
        theTrashChapter.getRequirement().clear();
        requirementProject.getChapter().add(theTrashChapter);

        // Add UntracedChapter folder
        theUntracedChapter = (UntracedChapter) EcoreUtil.copy(getUntracedChapter(models.get(0).getRequirement()));
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
        objectsCreated.put(object, h);
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
        for (TreeIterator<EObject> i = t.getRequirement().eAllContents(); i.hasNext();)
        {
            EObject tmp = i.next();
            if (tmp instanceof CurrentRequirement)
            {
                CurrentRequirement current = (CurrentRequirement) tmp;
                if (current.eContainer() instanceof HierarchicalElement)
                {
                    HierarchicalElement hier = (HierarchicalElement) current.eContainer();
                    EObject element = hier.getElement();
                    if (element.eIsProxy())
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
        for (EObject o : objectsCreated.keySet())
        {
            if (EcoreUtil.equals(o, samElement))
            {
                return objectsCreated.get(o);
            }
        }
        return null;
    }

    private void copyAndAdd(CurrentRequirement current, HierarchicalElement hierToAdd)
    {
        Requirement copy = (Requirement) EcoreUtil.copy(current);
        hierToAdd.getRequirement().add(copy);
        assignLink(copy);
    }

    private void copyAndAdd(Requirement current, SpecialChapter chapter)
    {
        Requirement copy = (Requirement) EcoreUtil.copy(current);
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
                        link.setValue(getEquivalent(copy.eResource(), link.getValue()));
                    }
                }
            }
        }
    }

    private EObject getEquivalent(Resource resource, EObject value)
    {
        for (TreeIterator<EObject> i = resource.getAllContents(); i.hasNext();)
        {
            EObject tmp = i.next();
            if (EcoreUtil.equals(value, tmp))
            {
                return tmp;
            }
        }
        return null;
    }
    
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
			Activator.getDefault().log("-- step : " + sum + " / " + newTime);
		}
		
		public void subTask(String name) {
			old.subTask(name);
		}
		
		public void setTaskName(String name) {
			old.setTaskName(name);
			long newTime = System.currentTimeMillis() - time ;
			Activator.getDefault().log("-- debut task : " + name + " / " + newTime);
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
			System.out.println("-- debut task : " + name + " / " + newTime);
		}
    }
}
