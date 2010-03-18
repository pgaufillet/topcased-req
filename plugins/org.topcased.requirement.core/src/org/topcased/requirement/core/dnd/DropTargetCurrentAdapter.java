/***********************************************************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe MERTZ (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.dnd;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeAllocate;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.ProblemChapter;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.UntracedChapter;
import org.topcased.requirement.core.Messages;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.extensions.IRequirementIdentifierDefinition;
import org.topcased.requirement.core.extensions.RequirementIdentifierDefinitionManager;
import org.topcased.requirement.core.utils.DefaultRequirementIdentifierDefinition;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.AddRequirementMarker;
import org.topcased.requirement.core.views.current.CurrentPage;
import org.topcased.requirement.core.views.current.CurrentRequirementView;

import ttm.Document;
import ttm.Requirement;
import ttm.Section;

/**
 * The current requirement view drop adapter.<br>
 * 
 * Update : 17 april 2009.<br>
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class DropTargetCurrentAdapter extends EditingDomainViewerDropAdapter
{
    private List<EObject> toSelect;

    /**
     * Constructor
     * 
     * @param domain The editing domain to use.
     * @param viewer The viewer
     */
    public DropTargetCurrentAdapter(EditingDomain domain, Viewer viewer)
    {
        super(domain, viewer);
        command = new CompoundCommand(Messages.getString("DropTargetCurrentAdapter.0")); //$NON-NLS-1$);
    }

    /**
     * Gets the command specialized as a {@link CompoundCommand}.
     * 
     * @return The EMF command as a {@link CompoundCommand}.
     */
    public CompoundCommand getCommand()
    {
        return (CompoundCommand) command;
    }

    /**
     * @see org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
     */
    @Override
    public void dragOver(DropTargetEvent event)
    {
        Object target = extractDropTarget(event.item);
        Collection< ? > source = extractDragSource(event.getSource());
        // use only the first to perform tests
        Object firstSrc = source.toArray()[0];

        if (firstSrc instanceof org.topcased.requirement.Requirement)
        {
            handleDragFromCurrent(event, firstSrc, target);
        }
        else
        {
            handleDragFromUpstream(event, target);
        }
    }

    /**
     * Handles the drag operation coming from the current view itself.
     * 
     * @event the drop target event
     * @param firstSrc The first element of the source selection.
     * @param target The target of the drop operation.
     */
    private void handleDragFromCurrent(DropTargetEvent event, Object firstSrc, Object target)
    {
        org.topcased.requirement.Requirement req = (org.topcased.requirement.Requirement) firstSrc;
        if (target instanceof org.topcased.requirement.Requirement)
        {
            org.topcased.requirement.Requirement targetReq = (org.topcased.requirement.Requirement) target;
            if (haveSameContainer(Collections.<EObject> singletonList(req), targetReq))
            {
                event.detail = originalOperation;
                event.operations = DND.DROP_DEFAULT;
            }
            else
            {
                event.operations = DND.DROP_NONE;
                event.detail = DND.DROP_NONE;
            }
        }
        else if (target instanceof HierarchicalElement)
        {
            // No operation to perform if the source container is the same that the target.
            if (req.eContainer() == target)
            {
                event.operations = DND.DROP_NONE;
                event.detail = DND.DROP_NONE;
            }
            else
            {
                event.detail = originalOperation;
                event.operations = DND.DROP_DEFAULT;
            }
        }
        else if (target instanceof ObjectAttribute && !(target instanceof AttributeAllocate))
        {
            event.detail = originalOperation;
            event.operations = DND.DROP_DEFAULT;
        }
        else
        {
            event.operations = DND.DROP_NONE;
            event.detail = DND.DROP_NONE;
        }
    }

    /**
     * Handles the drag operation coming from the upstream view.
     * 
     * @event the drop target event
     * @param target The target of the drop operation.
     */
    private void handleDragFromUpstream(DropTargetEvent event, Object target)
    {
        if (target instanceof HierarchicalElement || target instanceof org.topcased.requirement.Requirement || target instanceof ProblemChapter || target instanceof UntracedChapter
                || (target instanceof ObjectAttribute && !(target instanceof AttributeAllocate)))
        {
            event.detail = originalOperation;
            event.operations = DND.DROP_DEFAULT;
        }
        else
        {
            event.operations = DND.DROP_NONE;
            event.detail = DND.DROP_NONE;
        }
    }

    /**
     * @see org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter#extractDragSource(java.lang.Object)
     */
    @Override
    protected Collection< ? > extractDragSource(Object object)
    {
        if (object instanceof IStructuredSelection)
        {
            Collection< ? > selection = ((IStructuredSelection) object).toList();
            Collection<Object> sortedSelection = new ArrayList<Object>();
            for (Object obj : selection)
            {
                // particular case : the object is a section or a document.
                if (obj instanceof Document || obj instanceof Section)
                {
                    // this selection comes from the upstream view. It means that all requirements need to be extracted.
                    sortedSelection.addAll(RequirementUtils.getUpstream((EObject) obj));
                }
                else
                {
                    sortedSelection.add(obj);
                }
            }
            return sortedSelection;
        }
        else if (object instanceof DropTarget)
        {
            DropTarget dropTarget = (DropTarget) object;
            for (Transfer transfer : dropTarget.getTransfer())
            {
                if (transfer instanceof RequirementTransfer)
                {
                    return extractDragSource(((RequirementTransfer) transfer).getSelection());
                }
            }
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * @see org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter#drop(org.eclipse.swt.dnd.DropTargetEvent)
     */
    public void drop(final DropTargetEvent event)
    {
        IRunnableWithProgress runnable = new IRunnableWithProgress()
        {
            /**
             * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
             */
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
            {
                handleDrop(event, monitor);
            }
        };
        try
        {
            new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(false, false, runnable);
        }
        catch (InvocationTargetException e)
        {
            RequirementCorePlugin.log(e);
        }
        catch (InterruptedException e)
        {
            RequirementCorePlugin.log(e);
        }
    }

    /**
     * Handles the different drop cases.
     * 
     * @param event The event to process
     */
    private void handleDrop(DropTargetEvent event, IProgressMonitor monitor)
    {
        // Gets the source of the drag
        source = extractDragSource(event.data);

        // Gets the target of the drop
        Object target = extractDropTarget(event.item);

        // empty the selection list and the compound command
        toSelect = new ArrayList<EObject>();
        command = new CompoundCommand(Messages.getString("DropTargetCurrentAdapter.0")); //$NON-NLS-1$); 

        final int nbWorks = source.size() + 1;
        monitor.beginTask(Messages.getString("DropTargetCurrentAdapter.0"), nbWorks); //$NON-NLS-1$
        monitor.subTask(Messages.getString("DropTargetCurrentAdapter.2")); //$NON-NLS-1$

        // For all the sources --> execute an EMF command
        for (Object currSrc : source)
        {
            if (currSrc instanceof Requirement)
            {
                Requirement upstreamReq = (Requirement) currSrc;

                // Drag & drop an upstream requirement to a Special Chapter
                if (target instanceof ProblemChapter || target instanceof UntracedChapter)
                {
                    SpecialChapter chapter = (SpecialChapter) target;
                    addUpstreamToSpecialChapterCommand(upstreamReq, chapter);
                }
                // Drag & drop an upstream requirement to a hierarchical element
                else if (target instanceof HierarchicalElement)
                {
                    HierarchicalElement hierarchicalElt = (HierarchicalElement) target;
                    addUpstreamToHierarchicalElementCommand(upstreamReq, hierarchicalElt);
                }
                // Drag & drop upstream requirement to a current requirement
                else if (target instanceof CurrentRequirement)
                {
                    CurrentRequirement currentReq = (CurrentRequirement) target;
                    addUpstreamToCurrentRequirementCommand(upstreamReq, currentReq);
                }
                // Drag & drop upstream requirement to an ObjectAttribute (AttributeLink or ObjectAttribute but not
                // AttributeAllocate)
                else if (target instanceof ObjectAttribute)
                {
                    ObjectAttribute attribute = (ObjectAttribute) target;
                    addRequirementToObjectAttributeCommand(upstreamReq, attribute);
                }
            }
            // Drag & drop a current requirement to a hierarchical element
            else if (currSrc instanceof org.topcased.requirement.Requirement)
            {
                // represents a drag and drop operation outside the current container
                org.topcased.requirement.Requirement requirement = (org.topcased.requirement.Requirement) currSrc;
                if (target instanceof HierarchicalElement)
                {
                    HierarchicalElement hierarchicalElement = (HierarchicalElement) target;
                    addDragAndDropCommand(requirement, hierarchicalElement, event);
                }
                // represents a move operation inside the same container
                else if (target instanceof org.topcased.requirement.Requirement)
                {
                    org.topcased.requirement.Requirement requirementTarget = (org.topcased.requirement.Requirement) target;
                    addMoveRequirementCommand(requirement, requirementTarget);
                }
            }
            // Drag & drop upstream requirement to an ObjectAttribute (AttributeLink or ObjectAttribute but not
            // AttributeAllocate)
            else if (currSrc instanceof CurrentRequirement && target instanceof ObjectAttribute)
            {
                CurrentRequirement currentReq = (CurrentRequirement) currSrc;
                ObjectAttribute attribute = (ObjectAttribute) target;
                addRequirementToObjectAttributeCommand(currentReq, attribute);
            }
            monitor.worked(1);
        }

        // Give the focus on the current requirement view
        CurrentRequirementView.getInstance().setFocus();

        // Execute the compound command
        monitor.subTask(Messages.getString("DropTargetCurrentAdapter.3")); //$NON-NLS-1$
        execute();
        monitor.worked(1);
        monitor.done();
    }

    /**
     * Executes the {@link CompoundCommand} for the drag&drop action
     */
    private void execute()
    {
        if (!getCommand().isEmpty() && getCommand().canExecute())
        {
            domain.getCommandStack().execute(getCommand());
            CurrentPage page = RequirementHelper.INSTANCE.getCurrentPage();
            page.getViewer().setSelection(new StructuredSelection(toSelect));
        }

        // Clean up the state.
        command = null;
        commandTarget = null;
        source = null;
    }

    /**
     * Adds to the compound command the command allowing to create a current requirement from an upstream requirement.
     * 
     * @param upstreamReq The upstream requirement
     * @param specialChapter The target special chapter (except Not Affected chapter)
     */
    private void addUpstreamToSpecialChapterCommand(Requirement upstreamReq, SpecialChapter specialChapter)
    {
        CurrentRequirement current = RequirementHelper.INSTANCE.create(specialChapter, upstreamReq);
        Command addCmd = AddCommand.create(domain, specialChapter, RequirementPackage.eINSTANCE.getSpecialChapter_Requirement(), current);
        getCommand().appendAndExecute(addCmd);
        toSelect.add(current);
    }

    /**
     * Adds to the compound command the command allowing to create a current requirement from an upstream requirement.
     * 
     * @param upstreamReq The upstream requirement.
     * @param hierarchicalElt The target hierarchical element.
     */
    private void addUpstreamToHierarchicalElementCommand(Requirement upstreamReq, HierarchicalElement hierarchicalElt)
    {
        // handle the computing of the next index stored at the hierarchical level
        long index = 0;
        IRequirementIdentifierDefinition definition = RequirementIdentifierDefinitionManager.getInstance().getIdentifierDefinition(domain);
        
        if (definition!=null)
        {
            index = definition.increaseIndexWhenCreateRequirement(hierarchicalElt, definition.getCurrentIndex(hierarchicalElt));
        }
        else
        {
            index = DefaultRequirementIdentifierDefinition.getInstance().increaseIndexWhenCreateRequirement(null,DefaultRequirementIdentifierDefinition.getInstance().getCurrentIndex(null));  
        }
        
        Integer pos = AddRequirementMarker.eINSTANCE.computeIndex(hierarchicalElt);
        CurrentRequirement current = RequirementHelper.INSTANCE.create(hierarchicalElt, upstreamReq, index);
        Command addCmd = AddCommand.create(domain, hierarchicalElt, RequirementPackage.eINSTANCE.getHierarchicalElement_Requirement(), current, pos);
        getCommand().appendAndExecute(addCmd);
        toSelect.add(current);
    }

    /**
     * Executes a command allowing to create additional AttributeLink by drag and drop.
     * 
     * @param upstreamReq The upstream requirement to reference.
     * @param currentReq The target current requirement.
     */
    private void addUpstreamToCurrentRequirementCommand(Requirement upstreamReq, CurrentRequirement currentReq)
    {
        // Add new link attribute to the target
        Collection<AttributeLink> attLinks = RequirementHelper.INSTANCE.createAttributeLink(upstreamReq);
        for (AttributeLink toInsert : attLinks)
        {
            int indexToInsert = currentReq.getAttribute().size();

            // try do deduced the index where the new attribute link must be inserted
            for (Attribute attribute : currentReq.getAttribute())
            {
                if (attribute instanceof AttributeLink && attribute.getName().equals(toInsert.getName()))
                {
                    indexToInsert = currentReq.getAttribute().indexOf(attribute);
                }
            }
            // commands are created and executed on the heap otherwise it more difficult to compute the index...
            Command addCmd = AddCommand.create(domain, currentReq, RequirementPackage.eINSTANCE.getRequirement_Attribute(), toInsert, indexToInsert + 1);
            getCommand().appendAndExecute(addCmd);
        }
        toSelect.addAll(attLinks);
    }

    /**
     * Add the command allowing to set a Requirement to an Object Attribute.
     * 
     * @param source The source object
     * @param target The target object
     */
    private void addRequirementToObjectAttributeCommand(EObject source, EObject target)
    {
        // the target AttributeLink is updated
        Command setCmd = SetCommand.create(domain, target, RequirementPackage.eINSTANCE.getObjectAttribute_Value(), source);
        getCommand().appendAndExecute(setCmd);
        toSelect.add(target);
    }

    /**
     * Handles internal drag and drop in the Current View
     * 
     * @param source The source object (a requirement)
     * @param target The target object (a hierarchical element)
     * @param event The drop target event
     */
    private void addDragAndDropCommand(org.topcased.requirement.Requirement source, HierarchicalElement target, DropTargetEvent event)
    {
        if (source instanceof CurrentRequirement)
        {
            long nextIndex = 0;
            CurrentRequirement currentReq = (CurrentRequirement) source;
            IRequirementIdentifierDefinition definition = RequirementIdentifierDefinitionManager.getInstance().getIdentifierDefinition(domain);
            
            if (definition!=null)
            {
                nextIndex = definition.resetIndexWhenCreateNewContainer(target, definition.getCurrentIndex(target));
                nextIndex = definition.increaseIndexWhenCreateRequirement(target, nextIndex);
            }
            else
            {
                nextIndex = DefaultRequirementIdentifierDefinition.getInstance().increaseIndexWhenCreateRequirement(null,DefaultRequirementIdentifierDefinition.getInstance().getCurrentIndex(null));
            }

            
            // handle the requirement renaming before drop
            Command renameCmd = RequirementHelper.INSTANCE.renameRequirement(target, currentReq, nextIndex);
            getCommand().appendAndExecute(renameCmd);
        }

        Command dndCmd = DragAndDropCommand.create(domain, target, getLocation(event), event.operations, originalOperation, Collections.singleton(source));
        getCommand().appendIfCanExecute(dndCmd);
        toSelect.add(source);
    }

    /**
     * Add a move operation inside the same container.
     * 
     * @param source The source requirement
     * @param target The target requirement
     */
    private void addMoveRequirementCommand(org.topcased.requirement.Requirement source, org.topcased.requirement.Requirement target)
    {
        // check that all requirement come from the same container
        EList<org.topcased.requirement.Requirement> requirements = ECollections.emptyEList();
        EReference reference = null;
        if (target.eContainer() instanceof HierarchicalElement)
        {
            requirements = ((HierarchicalElement) target.eContainer()).getRequirement();
            reference = RequirementPackage.eINSTANCE.getHierarchicalElement_Requirement();
        }
        else
        {
            requirements = ((SpecialChapter) target.eContainer()).getRequirement();
            reference = RequirementPackage.eINSTANCE.getSpecialChapter_Requirement();
        }
        int indexTarget = ECollections.indexOf(requirements, target, 0);
        int indexSource = ECollections.indexOf(requirements, source, 0);
        if (indexSource <= indexTarget)
        {
            List< ? > tempList = Arrays.asList(this.source.toArray());
            indexTarget += tempList.indexOf(source);
        }
        Command moveCmd = MoveCommand.create(domain, target.eContainer(), reference, source, indexTarget);
        getCommand().appendAndExecute(moveCmd);
        toSelect.add(source);
    }

    /**
     * Convenient method to determine if the source element and the target have the same container.
     * 
     * @param source A collection of source objects
     * @param target The target
     * @return <code>true</code> if every element have the same container, <code>false</code> otherwise.
     */
    private boolean haveSameContainer(Collection< ? extends EObject> source, EObject target)
    {
        EObject targetContainer = target.eContainer();
        for (EObject obj : source)
        {
            if (obj.eContainer() != targetContainer)
            {
                return false;
            }
        }
        return true;
    }

}
