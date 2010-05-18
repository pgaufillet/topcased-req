/*****************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.topcased.modeler.diagrams.model.Diagrams;
import org.topcased.requirement.AnonymousRequirement;
import org.topcased.requirement.AttributeAllocate;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.AttributesType;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.core.extensions.DefaultAttachmentPolicy;
import org.topcased.requirement.core.extensions.IModelAttachmentPolicy;
import org.topcased.requirement.core.extensions.IRequirementCountingAlgorithm;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.extensions.RequirementCountingAlgorithmManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.preferences.ComputeRequirementIdentifier;
import org.topcased.requirement.core.views.AddRequirementMarker;
import org.topcased.requirement.core.views.current.CurrentPage;
import org.topcased.requirement.core.views.upstream.UpstreamPage;

import ttm.Requirement;

/**
 * A factory to create/modify/move/manipulate current requirements.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe MERTZ</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * 
 */
public final class RequirementHelper
{
    /** The singleton */
    public static final RequirementHelper INSTANCE = new RequirementHelper();

    /** Constant used to indicate that a current requirement creation is expected */
    private static final boolean CREATE_CURRENT = true;

    /** Constant used to indicate that an anonymous requirement creation is expected */
    private static final boolean CREATE_ANONYMOUS = false;

    /** Constant for requirement file extension */
    private static final String REQUIREMENT_FILE_EXTENSION = "requirement"; //$NON-NLS-1$

    /** Most of operations offer by this factory are based on the editing domain */
    private EditingDomain editingDomain;

    /** A single reference to the upstream page */
    private UpstreamPage upstreamPage;

    /** A single reference to the current page */
    private CurrentPage currentPage;

    /**
     * Constructor
     */
    private RequirementHelper()
    {
        // prevent from instanciation
    }

    /**
     * Get a requirement project from an eobject
     * 
     * @param eobject, the starting eobject
     * @return the requirement project
     */
    public RequirementProject getRequirementProject(EObject eobject)
    {
        if (eobject != null)
        {
            Resource source = eobject.eResource();
            return getRequirementProject(source);
        }
        return null;
    }

    /**
     * Returns the requirement project linked to the current resource
     * 
     * @param modelResource
     * @return the requirement project
     */
    public RequirementProject getRequirementProject(Resource modelResource)
    {
        RequirementProject result = null;
        if (modelResource != null)
        {
            String extension = modelResource.getURI().fileExtension();
            if (extension != null)
            {

                if (REQUIREMENT_FILE_EXTENSION.equals(extension))
                {
                    if (!modelResource.getContents().isEmpty() && modelResource.getContents().get(0) instanceof RequirementProject)
                    {
                        result = (RequirementProject) modelResource.getContents().get(0);
                    }
                }
                else if (extension.endsWith("di")) //$NON-NLS-1$
                {
                    if (modelResource.getContents().get(0) instanceof Diagrams)
                    {
                        result = getRequirementProject((Diagrams) modelResource.getContents().get(0));
                    }
                }
                else
                {
                    URI uri = URI.createURI(modelResource.getURI().toString() + "di"); //$NON-NLS-1$
                    Resource diResource = modelResource.getResourceSet().getResource(uri, true);
                    if (diResource != null && diResource.getContents().get(0) instanceof Diagrams)
                    {
                        result = getRequirementProject((Diagrams) diResource.getContents().get(0));
                    }
                }
            }

        }
        return result;
    }

    /**
     * Returns the requirement project linked to the current diagrams
     * 
     * @param diagrams
     * @return the requirement project
     */
    public RequirementProject getRequirementProject(Diagrams diagrams)
    {
        RequirementProject result = null;
        if (diagrams != null && diagrams.eResource() != null && diagrams.eResource().getResourceSet() != null)
        {
            IModelAttachmentPolicy policy = ModelAttachmentPolicyManager.getInstance().getModelPolicy(diagrams.eResource().getResourceSet());
            if (policy == null)
            {
                policy = DefaultAttachmentPolicy.getInstance();
            }
            if (policy != null)
            {
                result = policy.getRequirementProjectFromTargetDiagram(diagrams);
            }
        }
        return result;
    }

    /**
     * Called from the modeler. Allows to create current requirements (by drag'n'drop or directly from the contextual
     * menu).
     * 
     * @param droppedObjects The list of dropped object.
     * @param targetObject The target object
     */
    public Command createCurrentRequirements(Collection< ? > droppedObjects, EObject targetObject)
    {
        return executeRequirementCreation(droppedObjects, targetObject, CREATE_CURRENT);
    }

    /**
     * Allows to create a current requirements.
     * 
     * @param targetObject The target object
     */
    public Command createCurrentRequirement(EObject targetObject)
    {
        return executeRequirementCreation(Collections.emptyList(), targetObject, CREATE_CURRENT);
    }

    /**
     * Allows to create an anonymous requirement.
     * 
     * @param targetObject The target object
     */
    public Command createAnonymousRequirement(EObject targetObject)
    {
        return executeRequirementCreation(Collections.emptyList(), targetObject, CREATE_ANONYMOUS);
    }

    /**
     * Executes the creation command(s) and returns this later.
     * 
     * @param droppedObjects The list of dropped object in case of a drag'n'drop.
     * @param targetObject The target model object
     * @param kind The operation kind
     * @return
     */
    private Command executeRequirementCreation(Collection< ? > droppedObjects, EObject targetObject, boolean kind)
    {
        CompoundCommand globalCmd = new CompoundCommand();

        if (targetObject != null)
        {
            List<org.topcased.requirement.Requirement> createdRequirements = new ArrayList<org.topcased.requirement.Requirement>();
            HierarchicalElement hierarchicalElement = getHierarchicalElement(targetObject, globalCmd);

            if (kind == CREATE_CURRENT)
            {
                createdRequirements = addCommandRequirement(droppedObjects, hierarchicalElement, globalCmd);
            }
            else
            {
                AnonymousRequirement anonymmous = createAnonymousRequirement(hierarchicalElement);
                createdRequirements.add(anonymmous);
                globalCmd.appendIfCanExecute(AddCommand.create(editingDomain, hierarchicalElement, RequirementPackage.eINSTANCE.getHierarchicalElement_Requirement(), anonymmous));
            }

            // it is only here that all the commands are executed
            if (!globalCmd.isEmpty() && globalCmd.canExecute())
            {
                globalCmd.execute();
            }

            // then the selection is done on the new inserted element(s).
            currentPage.setSelection(new StructuredSelection(createdRequirements));

            return globalCmd.unwrap();
        }
        return UnexecutableCommand.INSTANCE;
    }

    /**
     * Gets the hierarchical element corresponding to the targeted model object.<br>
     * If the corresponding hierarchical element does not already exist, it is created and returned.<br>
     * Note that all the hierarchy is implicitly created when a new element is created.
     * 
     * @param targetObject The dropped target model object
     * @param globalCmd The compound command in which other commands must be added.
     * @return The {@link HierarchicalElement} corresponding to the targeted model object.
     */
    public HierarchicalElement getHierarchicalElement(EObject targetObject, CompoundCommand globalCmd)
    {
        // Find the hierarchical element corresponding to the targetObject in the model
        HierarchicalElement hierarchicalElement = RequirementUtils.getHierarchicalElementFor(targetObject);

        if (hierarchicalElement == null)
        {
            hierarchicalElement = createHierarchicalElement(targetObject);
            attach(targetObject.eContainer(), hierarchicalElement, globalCmd);
        }
        return hierarchicalElement;
    }

    /**
     * Adds a child to a {@link HierarchicalElement}.<br>
     * If the targetObject does not exists, the hierarchical element is created and search the parent of this new
     * element.<br>
     * If the targetObject object exists, add the EObject as a child to this element.
     * 
     * @param parent The parent model object
     * @param child The child model object
     * @param cmd A compound command in which EMF command must be stacked.
     * @return the hierarchical element found or just created
     */
    private HierarchicalElement attach(EObject parent, EObject child, CompoundCommand cmd)
    {
        if (parent.eContainer() == null)
        {
            // Add child to the model as a hierarchical element
            EObject project = RequirementUtils.getRequirementProject(editingDomain);
            cmd.appendIfCanExecute(AddCommand.create(editingDomain, project, RequirementPackage.eINSTANCE.getRequirementProject_HierarchicalElement(), child));
            return null;
        }
        else
        {
            HierarchicalElement element = getHierarchicalElement(parent, cmd);
            cmd.appendIfCanExecute(AddCommand.create(editingDomain, element, RequirementPackage.eINSTANCE.getHierarchicalElement_Children(), child));
            return element;
        }
    }

    /**
     * Creates a <b>{@link HierarchicalElement</b>} object.
     * 
     * @param modelElement A model element referenced by the hierarchical element to create.
     * @return the hierarchical element created
     */
    private HierarchicalElement createHierarchicalElement(EObject modelElement)
    {
        HierarchicalElement newSystem = RequirementFactory.eINSTANCE.createHierarchicalElement();
        newSystem.setElement(modelElement);
        return newSystem;
    }

    /**
     * Executes the command to create the current requirements with the dropped requirements (upstream or a current
     * requirements)
     * 
     * @param droppedObjects A collection of requirements (upstream or current)
     * @param target The hierarchical element where to create the current requirements
     * @return a list of Requirements created
     */
    private List<org.topcased.requirement.Requirement> addCommandRequirement(Collection< ? > droppedObjects, EObject target, CompoundCommand compoundCmd)
    {
        List<org.topcased.requirement.Requirement> toSelect = new ArrayList<org.topcased.requirement.Requirement>();

        if (target != null)
        {
            HierarchicalElement hierarchicalElt = (HierarchicalElement) target;
            if (droppedObjects.isEmpty())
            {
                CurrentRequirement toCreate = create(hierarchicalElt, null, compoundCmd);
                toSelect.add(toCreate);
            }
            else
            {
                for (Object object : droppedObjects)
                {
                    Integer pos = AddRequirementMarker.eINSTANCE.computeIndex(target);
                    org.topcased.requirement.Requirement toCreate = null;
                    if (object instanceof Requirement)
                    {
                        // from the upstream view...
                        toCreate = create(hierarchicalElt, (Requirement) object, compoundCmd);
                        toSelect.add(toCreate);
                    }
                    else if (object instanceof org.topcased.requirement.Requirement)
                    {
                        org.topcased.requirement.Requirement requirement = (org.topcased.requirement.Requirement) object;
                        // need to delete the dropped object (the source of the drag) only if the parent is not the same
                        if (!target.equals(requirement.eContainer()))
                        {
                            Command dndCmd = DragAndDropCommand.create(editingDomain, target, pos, DND.DROP_MOVE, DND.DROP_MOVE, Collections.singleton(requirement));
                            compoundCmd.appendIfCanExecute(dndCmd);
                        }
                        toSelect.add(requirement);
                    }
                }
            }
        }
        return toSelect;
    }

    /**
     * Creates a <b>{@link CurrentRequirement}</b> object from an upstream requirement.
     * 
     * @param target The target is a {@link SpecialChapter}
     * @param upstream The upstream requirement
     * @return the created current requirement
     */
    public CurrentRequirement create(SpecialChapter target, Requirement upstream)
    {
        CurrentRequirement newCurrentReq = createCurrentRequirementFromUpstream(upstream);
        newCurrentReq.setIdentifier(upstream.getIdent());
        return newCurrentReq;
    }

    /**
     * Creates a <b>{@link CurrentRequirement}</b> object.
     * 
     * @param target The target is a {@link HierarchicalElement}
     * @param upstream The upstream requirement or null
     * @param nextIndex The next index to use to set the identifier feature of the {@link CurrentRequirement}.
     * @return the created current requirement
     */
    public CurrentRequirement create(HierarchicalElement target, Requirement upstream, CompoundCommand compoundCmd)
    {
        long index = 0;
        HierarchicalElement root = RequirementHelper.INSTANCE.getHierarchicalElementRoot();
        String source = ""; //$NON-NLS-1$
        IRequirementCountingAlgorithm algorithm = RequirementCountingAlgorithmManager.getInstance().getCountingAlgorithm(ComputeRequirementIdentifier.getCurrentAlgorithm());

        // Handle the create from upstream case
        if (upstream != null)
        {
            source = upstream.getIdent();
        }

        // Create the requirement
        Integer pos = AddRequirementMarker.eINSTANCE.computeIndex(target);
        CurrentRequirement newCurrentReq = createCurrentRequirementFromUpstream(upstream);

        // Attach the requirement to the model
        compoundCmd.appendAndExecute(AddCommand.create(editingDomain, target, RequirementPackage.eINSTANCE.getHierarchicalElement_Requirement(), newCurrentReq, pos));

        // Handle the case when this created requirement is the first requirement in the model
        if (root == null)
        {
            algorithm.setFirstIndex(newCurrentReq);
        }

        // Compute the identifier of the newly created requirement
        index = algorithm.getCurrentIndex(newCurrentReq);
        newCurrentReq.setIdentifier(ComputeRequirementIdentifier.INSTANCE.computeIdentifier(editingDomain, target, source, index));

        // Increase index after requirement creation
        algorithm.increaseIndexWhenCreateRequirement(newCurrentReq, index);

        return newCurrentReq;
    }

    /**
     * Creates an <b>{@link AnonymousRequirement}</b> model object.
     * 
     * @param target The target is a {@link HierarchicalElement}
     * @return the created anonymous requirement
     */
    private AnonymousRequirement createAnonymousRequirement(HierarchicalElement target)
    {
        AnonymousRequirement newAnonymous = RequirementFactory.eINSTANCE.createAnonymousRequirement();
        newAnonymous.setShortDescription(Messages.getString("RequirementHelper.0")); //$NON-NLS-1$
        newAnonymous.setIdentifier(Messages.getString("RequirementHelper.0")); //$NON-NLS-1$
        // createDefaultAttributes(null, newAnonymous); Commented to fix bug #2538
        return newAnonymous;
    }

    /**
     * Creates a default current requirement from an upstream requirement.<br>
     * Note that here, the identifier of the requirement is not handled.
     * 
     * @param upstream The upstream requirement
     * @return A default hierarchical element
     */
    private CurrentRequirement createCurrentRequirementFromUpstream(Requirement upstream)
    {
        CurrentRequirement newCurrentReq = RequirementFactory.eINSTANCE.createCurrentRequirement();
        // Copy the description
        String description = ""; //$NON-NLS-1$
        if (upstream != null)
        {
            if (upstream.getShortDescription() != null)
            {
                description = upstream.getShortDescription();
            }

            // Copy the Text given by the upstream requirement
            for (Object textSource : upstream.getTexts())
            {
                description += (description.length() > 0 ? "\n" : "") + ((ttm.Text) textSource).getValue(); //$NON-NLS-1$ //$NON-NLS-2$
            }
            newCurrentReq.setShortDescription(description);
        }
        createDefaultAttributes(upstream, newCurrentReq);
        return newCurrentReq;
    }

    /**
     * Creates the default attributes
     * 
     * @param upstream The source requirement that is an upstream requirement
     * @param current The target requirement that is a current requirement
     */
    private void createDefaultAttributes(Requirement upstream, org.topcased.requirement.Requirement current)
    {
        AttributeConfiguration configuration = RequirementUtils.getAttributeConfiguration(editingDomain);
        for (ConfiguratedAttribute attribute : configuration.getListAttributes())
        {
            org.topcased.requirement.Attribute newAttribute = null;
            if (AttributesType.TEXT_VALUE == attribute.getType().getValue())
            {
                newAttribute = createAttributeText(attribute, upstream);
            }
            else if (AttributesType.ALLOCATE_VALUE == attribute.getType().getValue())
            {
                newAttribute = createAttributeAllocate(attribute.getName());
            }
            else if (current instanceof CurrentRequirement && AttributesType.LINK_VALUE == attribute.getType().getValue())
            {
                newAttribute = createAttributeLink(attribute.getName(), upstream);
            }
            else if (AttributesType.OBJECT_VALUE == attribute.getType().getValue())
            {
                newAttribute = createAttributeReference(attribute.getName());
            }
            if (newAttribute != null)
            {
                current.getAttribute().add(newAttribute);
            }
        }
    }

    /**
     * Creates a <b>{@link TextAttribute}</b> with the default values or with the value of the same attribute from the
     * source requirement
     * 
     * @param attName : the attribute name
     * @param reqSource : the source requirement
     * @return the attribute text created
     */
    private TextAttribute createAttributeText(ConfiguratedAttribute attribute, Requirement reqSource)
    {
        TextAttribute newAtt = RequirementFactory.eINSTANCE.createTextAttribute();
        newAtt.setName(attribute.getName());
        String value = attribute.getDefaultValue() != null ? attribute.getDefaultValue().getValue().getValue() : ""; //$NON-NLS-1$
        newAtt.setValue(value);
        return newAtt;
    }

    /**
     * Creates the <b>{@link LinkAttributes}</b> corresponding to the upstream requirement dropped.
     * 
     * @param reqSource The upstream requirement
     * @return collection of link attribute
     */
    public Collection<AttributeLink> createAttributeLink(Requirement reqSource)
    {
        Collection<AttributeLink> result = new ArrayList<AttributeLink>();
        AttributeConfiguration configuration = RequirementUtils.getAttributeConfiguration(editingDomain);
        for (ConfiguratedAttribute attribute : configuration.getListAttributes())
        {
            if (attribute.getType().getValue() == AttributesType.LINK_VALUE)
            {
                result.add(createAttributeLink(attribute.getName(), reqSource));
            }
        }
        return result;
    }

    /**
     * Creates an <b>{@link AttributeLink}</b> object. The value is set to the reqSource requirement
     * 
     * @param name The attribute name
     * @param reqSource The source requirement
     * @return the attribute link created
     */
    private AttributeLink createAttributeLink(String name, Requirement reqSource)
    {
        AttributeLink att = RequirementFactory.eINSTANCE.createAttributeLink();
        att.setName(name);
        att.setValue(reqSource);
        att.setPartial(false);
        return att;
    }

    /**
     * Duplicates an existing <b>{@link AttributeLink} in taking into account only its name</b>.
     * 
     * @param attribute The attribute to duplicate
     * @return the new duplicated attribute.
     */
    public AttributeLink duplicateAttributeLink(AttributeLink attribute)
    {
        AttributeLink newOne = RequirementFactory.eINSTANCE.createAttributeLink();
        newOne.setName(attribute.getName());
        newOne.setPartial(attribute.getPartial());
        return newOne;
    }

    /**
     * Creates an <b>{@link ObjectAttribute}</b> object.
     * 
     * @param name The attribute name
     * @return the attribute object created
     */
    private ObjectAttribute createAttributeReference(String name)
    {
        ObjectAttribute att = RequirementFactory.eINSTANCE.createObjectAttribute();
        att.setName(name);
        return att;
    }

    /**
     * Creates an <b>{@link AttributeAllocate}</b> object.
     * 
     * @param name The attribute name
     * @return the attribute allocate created
     */
    private AttributeAllocate createAttributeAllocate(String name)
    {
        AttributeAllocate att = RequirementFactory.eINSTANCE.createAttributeAllocate();
        att.setName(name);
        return att;
    }

    /**
     * Duplicates an existing <b>{@link AttributeAllocate} in taking into account only its name</b>.
     * 
     * @param attribute The attribute to duplicate
     * @return the new duplicated attribute.
     */
    public AttributeAllocate duplicateAttributeAllocate(AttributeAllocate attribute)
    {
        AttributeAllocate newOne = RequirementFactory.eINSTANCE.createAttributeAllocate();
        newOne.setName(attribute.getName());
        return newOne;
    }

    /**
     * Duplicates an existing <b>{@link ObjectAttribute} in taking into account only its name</b>.
     * 
     * @param attribute The attribute to duplicate
     * @return the new duplicated attribute.
     */
    public ObjectAttribute duplicateObjectAttribute(ObjectAttribute attribute)
    {
        ObjectAttribute newOne = RequirementFactory.eINSTANCE.createObjectAttribute();
        newOne.setName(attribute.getName());
        return newOne;
    }

    /**
     * Renames a current requirement in the target hierarchical element
     * 
     * @param editingDomain The editing domain to use
     * @param parent The hierarchical element
     * @param element The current requirement to rename
     * @param nextIndex The next index
     * @return the EMF command which is of kind {@link SetCommand}
     */
    public Command renameRequirement(CurrentRequirement requirement)
    {
        long index = 0;
        IRequirementCountingAlgorithm algorithm = RequirementCountingAlgorithmManager.getInstance().getCountingAlgorithm(ComputeRequirementIdentifier.getCurrentAlgorithm());
        if (requirement != null && algorithm != null)
        {
            String ident = requirement.getIdentifier();
            HierarchicalElement parent = (HierarchicalElement) requirement.eContainer();
            index = algorithm.getCurrentIndex(requirement);
            String newIdent = ComputeRequirementIdentifier.INSTANCE.computeIdentifier(editingDomain, parent, ident, index);
            algorithm.increaseIndexWhenCreateRequirement(requirement, index);
            return SetCommand.create(editingDomain, requirement, RequirementPackage.eINSTANCE.getIdentifiedElement_Identifier(), newIdent);
        }
        return UnexecutableCommand.INSTANCE;
    }

    /**
     * Used by the default counting algorithm because the requirement index is stored on the hierarchical element root
     * 
     * @return the hierarchical element root of the current page model
     */
    public HierarchicalElement getHierarchicalElementRoot()
    {
        if (currentPage.getModel() instanceof RequirementProject)
        {
            RequirementProject reqRoot = (RequirementProject) currentPage.getModel();
            if (!reqRoot.getHierarchicalElement().isEmpty())
            {
                return reqRoot.getHierarchicalElement().get(0);
            }
        }
        return null;
    }

    /**
     * Moves up or moves down a <b>@link{CurrentRequirement}</b>. This command alters the order of the requirements into
     * the list.
     * 
     * @param current The current requirement to move
     * @param up If <b>true</b> move up, otherwise move down
     * @return the EMF command which is of kind {@link MoveCommand}
     */
    public Command move(org.topcased.requirement.Requirement current, Boolean up)
    {
        if (current.eContainer() instanceof HierarchicalElement)
        {
            EList<org.topcased.requirement.Requirement> list = ((HierarchicalElement) current.eContainer()).getRequirement();
            return MoveCommand.create(editingDomain, current.eContainer(), RequirementPackage.eINSTANCE.getHierarchicalElement_Requirement(), current, list.lastIndexOf(current) + (up ? -1 : 1));
        }
        else if (current.eContainer() instanceof SpecialChapter)
        {
            EList<org.topcased.requirement.Requirement> list = ((SpecialChapter) current.eContainer()).getRequirement();
            return MoveCommand.create(editingDomain, current.eContainer(), RequirementPackage.eINSTANCE.getSpecialChapter_Requirement(), current, list.lastIndexOf(current) + (up ? -1 : 1));
        }
        return UnexecutableCommand.INSTANCE;
    }

    /**
     * Moves up or moves down a <b>@link{HierarchicalElement}</b>. This command alters the order of the hierarchical
     * element into the list.
     * 
     * @param hierarchicalElt The @link{HierarchicalElement} to move in its container
     * @param up If <b>true</b> move up, otherwise move down
     * @return the EMF command which is of kind {@link MoveCommand}
     */
    public Command move(HierarchicalElement hierarchicalElt, Boolean up)
    {
        EList<HierarchicalElement> list = null;
        EReference reference = null;
        // high level
        if (hierarchicalElt.eContainer() instanceof RequirementProject)
        {
            list = ((RequirementProject) hierarchicalElt.eContainer()).getHierarchicalElement();
            reference = RequirementPackage.eINSTANCE.getRequirementProject_HierarchicalElement();
        }
        else
        // default case
        {
            list = ((HierarchicalElement) hierarchicalElt.eContainer()).getChildren();
            reference = RequirementPackage.eINSTANCE.getHierarchicalElement_Children();

        }
        return MoveCommand.create(editingDomain, hierarchicalElt.eContainer(), reference, hierarchicalElt, list.lastIndexOf(hierarchicalElt) + (up ? -1 : 1));
    }

    /**
     * Sets the <b>impacted</b> feature of a <b>{@link CurrentRequirement}</b>.
     * 
     * @param requirement The requirement to update
     * @return the EMF command which is of kind {@link SetCommand}
     */
    public Command revertImpact(EObject requirement)
    {
        return SetCommand.create(editingDomain, requirement, RequirementPackage.eINSTANCE.getCurrentRequirement_Impacted(), false);
    }

    /**
     * Sets the Upstream Page
     * 
     * @param page The content of the upstream view
     */
    public void setUpstreamPage(UpstreamPage page)
    {
        if (page != null)
        {
            if (!page.equals(upstreamPage))
            {
                // the upstream page has changed, so have the requirements. Recompute coverage data
                RequirementCoverageComputer.INSTANCE.reset(page.getEditingDomain());
            }
            editingDomain = page.getEditingDomain();
        }
        else
        {
            editingDomain = null;
        }
        upstreamPage = page;
    }

    /**
     * Sets the Current Page
     * 
     * @param page The content of the upstream view
     */
    public void setCurrentPage(CurrentPage page)
    {
        if (page != null)
        {
            editingDomain = page.getEditingDomain();
        }
        else
        {
            editingDomain = null;
        }
        currentPage = page;
    }

    /**
     * Gets the Upstream Page
     * 
     * @return the Upstream Page
     */
    public UpstreamPage getUpstreamPage()
    {
        return upstreamPage;
    }

    /**
     * Gets the Current Page
     * 
     * @return the Current Page
     */
    public CurrentPage getCurrentPage()
    {
        return currentPage;
    }

}
