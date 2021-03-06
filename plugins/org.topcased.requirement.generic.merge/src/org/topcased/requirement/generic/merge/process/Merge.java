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
 *
 *****************************************************************************/
package org.topcased.requirement.generic.merge.process;

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
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.topcased.modeler.di.model.Property;
import org.topcased.modeler.diagrams.model.Diagrams;
import org.topcased.requirement.generic.Injector;
import org.topcased.requirement.generic.merge.utils.Triplet;
import org.topcased.sam.requirement.Attribute;
import org.topcased.sam.requirement.AttributeConfiguration;
import org.topcased.sam.requirement.AttributeLink;
import org.topcased.sam.requirement.CurrentRequirement;
import org.topcased.sam.requirement.HierarchicalElement;
import org.topcased.sam.requirement.ProblemChapter;
import org.topcased.sam.requirement.Requirement;
import org.topcased.sam.requirement.RequirementFactory;
import org.topcased.sam.requirement.RequirementPackage;
import org.topcased.sam.requirement.RequirementProject;
import org.topcased.sam.requirement.SpecialChapter;
import org.topcased.sam.requirement.TrashChapter;
import org.topcased.sam.requirement.UntracedChapter;
import org.topcased.sam.requirement.UpstreamModel;

public class Merge
{
    private Map<String, Boolean> inputs;

    private Vector<Triplet> models;

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
        models = new Vector<Triplet>();
    }

    public void process()
    {
        process(new NullProgressMonitor());
    }

    public void process(IProgressMonitor monitor)
    {
        try
        {
            thisMonitor = monitor;
            if (thisMonitor == null)
            {
                thisMonitor = new NullProgressMonitor();
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
            EObject eobject = new ResourceSetImpl().getResource(uri, true).getContents().get(0);

            // Get the associate requirement
            ((Diagrams) eobject).getModel();
            Property property = Injector.getProperty(eobject);

            // If there is an associate requirement
            if (property != null)
            {
                RequirementProject r = Injector.getRequirementProject(property);
                if (r != null)
                {
                    if (eobject instanceof Diagrams)
                    {
                        // get the associate model
                        URI uriModel = URI.createURI(file.substring(0, file.length() - 2));
                        EObject eobjectModel = new ResourceSetImpl().getResource(uriModel, true).getContents().get(0);

                        // Create a new Triplet
                        Triplet t = new Triplet(eobjectModel, (Diagrams) eobject, r, inputs.get(file));
                        models.add(t);
                    }
                }
            }
            subMonitor.worked(1);
        }

    }

    public void initRequirementFile()
    {
        requirementProject = factory.createRequirementProject();
        ResourceSet set = new ResourceSetImpl();
        Resource r = set.createResource(URI.createURI(output));
        r.getContents().add(requirementProject);

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
        for (Iterator<Triplet> iterator = models.iterator(); iterator.hasNext();)
        {

            Triplet t = (Triplet) iterator.next();

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
        for (Iterator<Triplet> iterator = models.iterator(); iterator.hasNext();)
        {
            Triplet t = (Triplet) iterator.next();
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
        h.setSamElement(object);
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
        for (Iterator<Triplet> iterator = models.iterator(); iterator.hasNext();)
        {
            Triplet t = (Triplet) iterator.next();

            addRequirements(t);
        }
    }

    private void addRequirements(Triplet t)
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
                    EObject samE = hier.getSamElement() ;
                    if (samE.eIsProxy())
                    {
                        samE = (EObject) hier.eGet(RequirementPackage.Literals.HIERARCHICAL_ELEMENT__SAM_ELEMENT,true);
                    }
                    if (samE != null && !samE.eIsProxy() && hier.getSamElement().eResource().getURI().equals(t.getModel().eResource().getURI()))
                    {
                        HierarchicalElement hierToAdd = get(hier.getSamElement());
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
                    if ("requirement".equals(resource.getURI().fileExtension()) && resource != copy.eResource())
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

}
