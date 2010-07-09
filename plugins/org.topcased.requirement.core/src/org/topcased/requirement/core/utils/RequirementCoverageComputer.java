/*****************************************************************************
 * 
 * Copyright (c) 2009 Atos Origin.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Vincent Hemery [(Atos Origin)] [vincent.hemery@atosorigin.com]
 *    
 *
 *****************************************************************************/
package org.topcased.requirement.core.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.views.current.CurrentPage;
import org.topcased.requirement.core.views.upstream.UpstreamPage;

import ttm.Document;
import ttm.Requirement;
import ttm.TtmPackage;

/**
 * This singleton class provides information about the requirements coverage. It computes the total number of
 * requirements in the project's Upstream model, the number among them which are covered in Hierarchical Elements, and
 * the ratio between the two as a percentage.
 * 
 * It has also been enhanced to compute the number of current requirements, the number among them which are linked to an
 * upstream requirement and the number among these latest which are partial (number of not linked can be computed by
 * difference).
 * 
 * @author Vincent Hemery
 */
public final class RequirementCoverageComputer
{
    /**
     * Enumerate class indicating how a current requirement is linked to an upstream requirement.
     */
    private enum LinkType {
        /** linked to at least one upstream requirement, and no partial link */
        COMPLETE_LINK,
        /** linked to at least one upstream requirement with partial */
        PARTIAL_LINK,
        /** not linked to any upstream requirement */
        NO_LINK;

        /**
         * Get how a current requirement is linked to upstream requirements
         * 
         * @param currentRequirement current requirement
         * @return link state (linked with no partial, linked with partial or no link)
         */
        public static LinkType getLinkage(CurrentRequirement currentRequirement)
        {
            boolean linked = false;
            for (Attribute att : currentRequirement.getAttribute())
            {
                if (att instanceof AttributeLink)
                {
                    if (((AttributeLink) att).getValue() instanceof Requirement)
                    {
                        // there is at least one requirement linked
                        linked = true;
                        if (((AttributeLink) att).getPartial())
                        {
                            // there is at least one requirement linked with partial
                            return LinkType.PARTIAL_LINK;
                        }
                    }
                }
            }
            if (linked)
            {
                return LinkType.COMPLETE_LINK;
            }
            else
            {
                return LinkType.NO_LINK;
            }
        }
    }

    /** The singleton */
    public static final RequirementCoverageComputer INSTANCE = new RequirementCoverageComputer();

    /** The format for displaying a percentage. */
    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#0.##%"); //$NON-NLS-1$

    /** The message entry for displaying number of current requirements in the status bar */
    private static final String CURRENT_PAGE_STATUS_MESSAGE0 = "CurrentPage.0"; //$NON-NLS-1$

    /** The editing domain in which coverage rate is computed */
    private EditingDomain editingDomain = null;

    /** The number of requirements */
    private int totalNumberOfRequirements = 0;

    /** The number of current requirements */
    private int numberOfCurrentRequirements = 0;

    /** The number of linked current requirements */
    private int numberOfLinkedCurrentRequirements = 0;

    /** The number of linked current requirements with partial */
    private int numberOfPartialLinkedCurrentRequirements = 0;

    /** whether the totalNumberOfRequirements attribute is correct */
    private boolean totalNumberOfRequirementsSet = false;

    /** The current requirements in Hierarchical elements and their linkage type */
    private Map<CurrentRequirement, LinkType> currentRequirementsLinks = new HashMap<CurrentRequirement, LinkType>();

    /** The requirements covered by Current requirements in Hierarchical elements */
    private Map<EObject, List<CurrentRequirement>> coveredRequirements = new HashMap<EObject, List<CurrentRequirement>>();

    /** whether the coveredRequirements attribute is correct */
    private boolean coveredRequirementsSet = false;

    /** The adapter to listen at model modifications to update requirements coverage */
    private Adapter requirementsUpdaterAdapter = null;

    /** The adapter to listen at model modifications to update current requirements number */
    private Adapter currentRequirementsUpdaterAdapter = null;

    /** The model elements which modifications are listened at */
    private Collection<EObject> listenedModelElements = new ArrayList<EObject>();

    /** The model elements which modifications are listened at for current requirements number */
    private Collection<EObject> listenedModelElementsForCurrentOnly = new ArrayList<EObject>();

    /**
     * This class provides a switch over Notification event types. It allows to launch an addition or a removal
     * operation, depending on the event's type.
     */
    private abstract class NotificationEventTypeSwitcher
    {
        /**
         * Constructor performs the switch other the event type.
         * 
         * @param notificationEventType type of the notification event.
         */
        public NotificationEventTypeSwitcher(int notificationEventType)
        {
            switch (notificationEventType)
            {
                case Notification.SET:
                    remove();
                    add();
                    break;
                case Notification.ADD:
                case Notification.ADD_MANY:
                    add();
                    break;
                case Notification.UNSET:
                case Notification.REMOVE:
                case Notification.REMOVE_MANY:
                    remove();
                    break;
                default:
                    // no impact on requirements coverage
                    break;
            }
        }

        /**
         * This operation must be defined to handle addition events
         */
        protected abstract void add();

        /**
         * This operation must be defined to handle removal events
         */
        protected abstract void remove();
    }

    /**
     * Constructor
     */
    private RequirementCoverageComputer()
    {
        // prevent from instantiation
    }

    /**
     * Get the listener that updates the coverage of requirements when model is modified.
     * 
     * @return the adapter listener
     */
    public Adapter getRequirementsUpdaterAdapter()
    {
        if (requirementsUpdaterAdapter == null)
        {
            requirementsUpdaterAdapter = new AdapterImpl()
            {
                @Override
                public void notifyChanged(Notification msg)
                {
                    modelHasChanged(msg, false);
                }
            };
        }
        return requirementsUpdaterAdapter;
    }

    /**
     * Get the listener that updates the number of current requirements when model is modified.
     * 
     * @return the adapter listener
     */
    public Adapter getCurrentRequirementsUpdaterAdapter()
    {
        if (currentRequirementsUpdaterAdapter == null)
        {
            currentRequirementsUpdaterAdapter = new AdapterImpl()
            {
                @Override
                public void notifyChanged(Notification msg)
                {
                    modelHasChanged(msg, true);
                }
            };
        }
        return currentRequirementsUpdaterAdapter;
    }

    /**
     * Update coverage information when a model element has changed.
     * 
     * @param msg the notification message
     * @param ignoreForCoverage true if the coverage rate must not be changed
     */
    protected void modelHasChanged(final Notification msg, final boolean ignoreForCoverage)
    {
        Object modifiedFeature = msg.getFeature();
        final Object removed = msg.getOldValue();
        final Object added = msg.getNewValue();
        // features which may contain requirements definitions
        if (RequirementPackage.Literals.REQUIREMENT_PROJECT__UPSTREAM_MODEL.equals(modifiedFeature) || TtmPackage.Literals.PROJECT__DOCUMENTS.equals(modifiedFeature)
                || TtmPackage.Literals.HIERARCHICAL_ELEMENT__CHILDREN.equals(modifiedFeature))
        {
            if (!ignoreForCoverage)
            {
                new NotificationEventTypeSwitcher(msg.getEventType())
                {
                    protected void add()
                    {
                        requirementsAdded(added);
                    }

                    protected void remove()
                    {
                        requirementsRemoved(removed);
                    }
                };
                // refresh coverage rate printing
                refreshCoverageRateDisplay();
            }
        }
        // features which may contain Link_to attribute for requirements coverage
        else if (RequirementPackage.Literals.SPECIAL_CHAPTER__HIERARCHICAL_ELEMENT.equals(modifiedFeature) || RequirementPackage.Literals.SPECIAL_CHAPTER__REQUIREMENT.equals(modifiedFeature)
                || RequirementPackage.Literals.REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT.equals(modifiedFeature) || RequirementPackage.Literals.HIERARCHICAL_ELEMENT__CHILDREN.equals(modifiedFeature)
                || RequirementPackage.Literals.HIERARCHICAL_ELEMENT__REQUIREMENT.equals(modifiedFeature) || RequirementPackage.Literals.REQUIREMENT__ATTRIBUTE.equals(modifiedFeature))
        {
            new NotificationEventTypeSwitcher(msg.getEventType())
            {
                protected void add()
                {
                    addCoverage(added, ignoreForCoverage);
                }

                protected void remove()
                {
                    removeCoverage(removed, msg.getNotifier(), ignoreForCoverage);
                }
            };
            if (!ignoreForCoverage)
            {// refresh coverage rate printing
                refreshCoverageRateDisplay();
            }
            // refresh number of current requirements printing
            refreshNumberOfCurrentRequirementsDisplay();
        }
        else if (RequirementPackage.Literals.OBJECT_ATTRIBUTE__VALUE.equals(modifiedFeature))
        {
            Object attribute = msg.getNotifier();
            if (attribute instanceof AttributeLink)
            {
                final CurrentRequirement coveringRequirement = (CurrentRequirement) ((AttributeLink) attribute).eContainer();
                new NotificationEventTypeSwitcher(msg.getEventType())
                {
                    protected void add()
                    {
                        if (added instanceof EObject)
                        {
                            if (!ignoreForCoverage)
                            {
                                addToCoveredRequirementsAttribute(coveringRequirement, (EObject) added);
                            }
                            // update number of current
                            updateCurrentRequirementNumbers(coveringRequirement, false);
                        }
                    }

                    protected void remove()
                    {
                        if (removed instanceof EObject)
                        {
                            if (!ignoreForCoverage)
                            {
                                removeFromCoveredRequirementsAttribute(coveringRequirement, (EObject) removed);
                            }
                            // update number of current
                            updateCurrentRequirementNumbers(coveringRequirement, false);
                        }
                    }
                };

            }
            if (!ignoreForCoverage)
            {// refresh coverage rate printing
                refreshCoverageRateDisplay();
            }
            // refresh number of current requirements printing
            refreshNumberOfCurrentRequirementsDisplay();
        }
        // features which may contain Link_to attribute for partial links
        else if (RequirementPackage.Literals.ATTRIBUTE_LINK__PARTIAL.equals(modifiedFeature))
        {
            Object attribute = msg.getNotifier();
            if (attribute instanceof AttributeLink)
            {
                final CurrentRequirement coveringRequirement = (CurrentRequirement) ((AttributeLink) attribute).eContainer();
                new NotificationEventTypeSwitcher(msg.getEventType())
                {
                    protected void add()
                    {
                        updateCurrentRequirementNumbers(coveringRequirement, false);
                    }

                    protected void remove()
                    {
                        // do nothing
                    }
                };

            }
            // refresh number of current requirements printing
            refreshNumberOfCurrentRequirementsDisplay();
        }
    }

    /**
     * Update the coveredRequirements attribute, by adding a requirement coverage.
     * 
     * @param coveringRequirementToAdd the current requirement to add at coverage
     * @param coveredRequirement the requirement newly covered by the this current requirement
     */
    private void addToCoveredRequirementsAttribute(CurrentRequirement coveringRequirementToAdd, EObject coveredRequirement)
    {
        if (coveredRequirement != null)
        {
            if (!coveredRequirements.containsKey(coveredRequirement))
            {
                coveredRequirements.put((EObject) coveredRequirement, new ArrayList<CurrentRequirement>());
            }
            coveredRequirements.get(coveredRequirement).add(coveringRequirementToAdd);
        }
    }

    /**
     * Update the coveredRequirements attribute, by removing a requirement coverage.
     * 
     * @param coveringRequirementToRemove the current requirement to remove from coverage
     * @param coveredRequirement the requirement no longer covered by the this current requirement
     */
    private void removeFromCoveredRequirementsAttribute(CurrentRequirement coveringRequirementToRemove, EObject coveredRequirement)
    {
        if (coveredRequirements.containsKey(coveredRequirement))
        {
            coveredRequirements.get(coveredRequirement).remove(coveringRequirementToRemove);
            if (coveredRequirements.get(coveredRequirement).isEmpty())
            {
                coveredRequirements.remove(coveredRequirement);
            }
        }
    }

    /**
     * Indicate that a change occurred concerning a current requirement
     * 
     * @param updatedRequirement the current requirement which has been updated
     * @param removing true if updatedRequirement is being removed (false if it is being updated or added)
     */
    private void updateCurrentRequirementNumbers(CurrentRequirement updatedRequirement, boolean removing)
    {
        LinkType linkage = LinkType.getLinkage(updatedRequirement);
        if (currentRequirementsLinks.containsKey(updatedRequirement))
        {
            // remove current requirement from count (updating or removing)
            LinkType oldLinkage = currentRequirementsLinks.get(updatedRequirement);
            if (linkage != null && linkage.equals(oldLinkage) && !removing)
            {
                // no significant change, no need to go further
                return;
            }
            switch (oldLinkage)
            {
                case PARTIAL_LINK:
                    numberOfPartialLinkedCurrentRequirements--;
                case COMPLETE_LINK:
                    numberOfLinkedCurrentRequirements--;
                case NO_LINK:
                default:
                    numberOfCurrentRequirements--;
            }
        }
        if (removing)
        {
            currentRequirementsLinks.remove(updatedRequirement);
        }
        else
        {
            // add current requirement to count
            switch (linkage)
            {
                case PARTIAL_LINK:
                    numberOfPartialLinkedCurrentRequirements++;
                case COMPLETE_LINK:
                    numberOfLinkedCurrentRequirements++;
                case NO_LINK:
                default:
                    numberOfCurrentRequirements++;
            }
            currentRequirementsLinks.put(updatedRequirement, linkage);
        }
    }

    /**
     * Indicate coverage requirements have been added
     * 
     * @param added the added model object (or list of objects) containing Attribute Links for requirement coverage.
     * @param ignoreForCoverage true if the coverage rate must not be changed
     */
    protected void addCoverage(Object added, boolean ignoreForCoverage)
    {
        if (added == null)
        {
            return;
        }
        else if (added instanceof AttributeLink)
        {
            if (!ignoreForCoverage)
            {
                specifiedAddCoverage((AttributeLink) added);
            }
            else
            {
                listenForCurrentOnly((AttributeLink) added);
            }
            EObject curr = ((AttributeLink) added).eContainer();
            if (curr instanceof CurrentRequirement)
            {
                // update number of current
                updateCurrentRequirementNumbers((CurrentRequirement) curr, false);
            }
        }
        else if (added instanceof List< ? >)
        {
            for (Object listElement : (List< ? >) added)
            {
                addCoverage(listElement, ignoreForCoverage);
            }
        }
        else if (added instanceof EObject)
        {
            if (ignoreForCoverage)
            {
                listenForCurrentOnly((EObject) added);
            }
            else
            {
                listen((EObject) added);
            }
            // check in object's content
            iterateForAddingCoverage(((EObject) added).eAllContents(), ignoreForCoverage);
            if (added instanceof CurrentRequirement)
            {
                // update number of current
                updateCurrentRequirementNumbers((CurrentRequirement) added, false);
            }
        }
    }

    /**
     * Indicate coverage requirements have been added
     * 
     * @param addedRequirementCoverage the added requirement covering Attribute Link object.
     */
    private void specifiedAddCoverage(AttributeLink addedRequirementCoverage)
    {
        if (addedRequirementCoverage != null)
        {
            listen(addedRequirementCoverage);
            // add (partial) coverage of attribute's value requirement
            CurrentRequirement coveringCurrentRequirement = (CurrentRequirement) addedRequirementCoverage.eContainer();
            EObject coveredRequirement = addedRequirementCoverage.getValue();
            addToCoveredRequirementsAttribute(coveringCurrentRequirement, coveredRequirement);
        }
    }

    /**
     * Iterate other elements to add covering requirements.
     * 
     * @param iterator the iterator other EObjects.
     * @param ignoreForCoverage true if the coverage rate must not be changed
     */
    private void iterateForAddingCoverage(TreeIterator<EObject> iterator, boolean ignoreForCoverage)
    {
        while (iterator.hasNext())
        {
            EObject nextElement = iterator.next();
            if (nextElement instanceof AttributeLink)
            {
                if (!ignoreForCoverage)
                {
                    specifiedAddCoverage((AttributeLink) nextElement);
                }
                else
                {
                    listenForCurrentOnly((AttributeLink) nextElement);
                }
            }
            else
            {
                if (nextElement instanceof CurrentRequirement)
                {
                    // update number of current
                    updateCurrentRequirementNumbers((CurrentRequirement) nextElement, false);
                }
                // check if can contain covered requirements
                boolean canContain = nextElement instanceof HierarchicalElement || nextElement instanceof CurrentRequirement;
                if (canContain)
                {
                    if (ignoreForCoverage)
                    {
                        listenForCurrentOnly(nextElement);
                    }
                    else
                    {
                        listen(nextElement);
                    }
                }
                else
                {
                    iterator.prune();

                }
            }
        }
    }

    /**
     * Indicate coverage requirements have been removed. The parameter object must be removed from the model.
     * 
     * @param removed the removed model object (or list of objects) containing Attribute Links for requirement coverage.
     * @param container the object which used to contain or still contains 'removed' parameter
     * @param ignoreForCoverage true if the coverage rate must not be changed
     */
    protected void removeCoverage(Object removed, Object container, boolean ignoreForCoverage)
    {
        if (removed == null)
        {
            return;
        }
        else if (removed instanceof AttributeLink)
        {
            if (!ignoreForCoverage)
            {
                specifiedRemoveCoverage((AttributeLink) removed, container);
            }
            else
            {
                stopListening((AttributeLink) removed);
            }
            if (container instanceof CurrentRequirement)
            {
                // update number of current, current is deleted, except if only its contained attribute is
                boolean attributeOnlyIsDeleted = ((AttributeLink) removed).eContainer() == null;
                updateCurrentRequirementNumbers((CurrentRequirement) container, !attributeOnlyIsDeleted);
            }
        }
        else if (removed instanceof List< ? >)
        {
            for (Object listElement : (List< ? >) removed)
            {
                removeCoverage(listElement, container, ignoreForCoverage);
            }
        }
        else if (removed instanceof EObject)
        {
            stopListening((EObject) removed);
            // check in object's content
            iterateForRemovingCoverage(((EObject) removed).eAllContents(), ignoreForCoverage);
            if (removed instanceof CurrentRequirement)
            {
                // update number of current
                updateCurrentRequirementNumbers((CurrentRequirement) removed, true);
            }
        }
    }

    /**
     * Indicate coverage requirements have been removed. The parameter object must be removed from the model.
     * 
     * @param removedRequirementCoverage the added requirement covering Attribute Link object.
     * @param container the object which used to contain or still contains 'removedRequirementCoverage' parameter
     */
    private void specifiedRemoveCoverage(AttributeLink removedRequirementCoverage, Object container)
    {
        if (removedRequirementCoverage != null)
        {
            stopListening((AttributeLink) removedRequirementCoverage);
            // remove (partial) coverage of attribute's value requirement
            if (container instanceof CurrentRequirement)
            {
                EObject coveredRequirement = ((AttributeLink) removedRequirementCoverage).getValue();
                removeFromCoveredRequirementsAttribute((CurrentRequirement) container, coveredRequirement);
            }
        }
    }

    /**
     * Iterate other elements to remove coverage requirements.
     * 
     * @param iterator the iterator other EObjects.
     * @param ignoreForCoverage true if the coverage rate must not be changed
     */
    private void iterateForRemovingCoverage(TreeIterator<EObject> iterator, boolean ignoreForCoverage)
    {
        while (iterator.hasNext())
        {
            EObject nextElement = iterator.next();
            if (nextElement instanceof AttributeLink)
            {
                if (!ignoreForCoverage)
                {
                    specifiedRemoveCoverage((AttributeLink) nextElement, nextElement.eContainer());
                }
                else
                {
                    stopListening((AttributeLink) nextElement);
                }
            }
            else
            {
                if (nextElement instanceof CurrentRequirement)
                {
                    // update number of current
                    updateCurrentRequirementNumbers((CurrentRequirement) nextElement, true);
                }
                // check if can contain covered requirements
                boolean canContain = nextElement instanceof HierarchicalElement || nextElement instanceof CurrentRequirement;
                if (canContain)
                {
                    stopListening(nextElement);
                }
                else
                {
                    iterator.prune();

                }
            }
        }
    }

    /**
     * Indicate that requirements definitions have been added
     * 
     * @param added the added model object (or list of objects) containing requirement definition.
     */
    protected void requirementsAdded(Object added)
    {
        if (added == null)
        {
            return;
        }
        if (added instanceof Requirement)
        {
            specifiedAddRequirement((Requirement) added);
        }
        else if (added instanceof List< ? >)
        {
            for (Object listElement : (List< ? >) added)
            {
                requirementsAdded(listElement);
            }
        }
        else if (added instanceof EObject)
        {
            listen((EObject) added);
            // check in object's content
            iterateForAddingRequirementsDefinitions(((EObject) added).eAllContents());
        }
    }

    /**
     * Indicate that a requirement definition has been added
     * 
     * @param addedRequirement the added requirement.
     */
    private void specifiedAddRequirement(Requirement addedRequirement)
    {
        if (addedRequirement != null)
        {
            totalNumberOfRequirements++;
        }
    }

    /**
     * Iterate other elements to add requirements definitions.
     * 
     * @param iterator the iterator other EObjects.
     */
    private void iterateForAddingRequirementsDefinitions(TreeIterator<EObject> iterator)
    {
        while (iterator.hasNext())
        {
            EObject nextElement = iterator.next();
            if (nextElement instanceof Requirement)
            {
                specifiedAddRequirement((Requirement) nextElement);
            }
            else
            {
                // check if can contain requirement declaration
                boolean canContain = nextElement instanceof ttm.HierarchicalElement;
                if (canContain)
                {
                    listen(nextElement);
                }
                else
                {
                    iterator.prune();
                }
            }
        }
    }

    /**
     * Indicate that requirements definitions have been removed. The parameter object must be removed from the model.
     * 
     * @param removed the removed model object (or list of objects) containing requirement definition.
     */
    protected void requirementsRemoved(Object removed)
    {
        if (removed == null)
        {
            return;
        }
        if (removed instanceof Requirement)
        {
            specifiedRemoveRequirement((Requirement) removed);
        }
        else if (removed instanceof List< ? >)
        {
            for (Object listElement : (List< ? >) removed)
            {
                requirementsRemoved(listElement);
            }
        }
        else if (removed instanceof EObject)
        {
            stopListening((EObject) removed);
            // check in object's content
            iterateForRemovingRequirementsDefinitions(((EObject) removed).eAllContents());
        }
    }

    /**
     * Indicate that a requirement definition has been removed
     * 
     * @param removedRequirement the removed requirement.
     */
    private void specifiedRemoveRequirement(Requirement removedRequirement)
    {
        if (removedRequirement != null)
        {
            totalNumberOfRequirements--;
            // update number of current
            for (CurrentRequirement current : coveredRequirements.get(removedRequirement))
            {
                updateCurrentRequirementNumbers(current, false);
            }
            // remove all coverage for this requirement
            coveredRequirements.remove(removedRequirement);
        }
    }

    /**
     * Iterate other elements to remove requirements definitions.
     * 
     * @param iterator the iterator other EObjects.
     */
    private void iterateForRemovingRequirementsDefinitions(TreeIterator<EObject> iterator)
    {
        while (iterator.hasNext())
        {
            EObject nextElement = iterator.next();
            if (nextElement instanceof Requirement)
            {
                specifiedRemoveRequirement((Requirement) nextElement);
            }
            else
            {
                // check if can contain requirement declaration
                boolean canContain = nextElement instanceof ttm.HierarchicalElement;
                if (canContain)
                {
                    stopListening(nextElement);
                }
                else
                {
                    iterator.prune();
                }
            }
        }
    }

    /**
     * Start listening at modifications of a model element.
     * 
     * Note that we do not need to listen at {@link Requirement} objects, since their modification means that only the
     * content of the requirement has changed. On the other hand, we need to listen at all their parent hierarchy and at
     * {@link AttributeLink} objects and at all their parent hierarchy.
     * 
     * @param model the model element to listen.
     */
    private void listen(EObject model)
    {
        if (!listenedModelElements.contains(model))
        {
            model.eAdapters().add(getRequirementsUpdaterAdapter());
            listenedModelElements.add(model);
        }
    }

    /**
     * Start listening at modifications of a model element (for current requirements view only, not for upstream
     * coverage).
     * 
     * @param model the model element to listen.
     */
    private void listenForCurrentOnly(EObject model)
    {
        if (!listenedModelElementsForCurrentOnly.contains(model))
        {
            model.eAdapters().add(getCurrentRequirementsUpdaterAdapter());
            listenedModelElementsForCurrentOnly.add(model);
        }
    }

    /**
     * Unregister from all listened model elements.
     */
    private void stopListening()
    {
        for (EObject model : listenedModelElements)
        {
            model.eAdapters().remove(getRequirementsUpdaterAdapter());
        }
        for (EObject model : listenedModelElementsForCurrentOnly)
        {
            model.eAdapters().remove(getCurrentRequirementsUpdaterAdapter());
        }
        listenedModelElementsForCurrentOnly.clear();
        listenedModelElements.clear();
    }

    /**
     * Unregister from a listened model element.
     * 
     * @param model the model object to stop listening at.
     */
    private void stopListening(EObject model)
    {
        model.eAdapters().remove(getRequirementsUpdaterAdapter());
        listenedModelElements.remove(model);
        model.eAdapters().remove(getCurrentRequirementsUpdaterAdapter());
        listenedModelElementsForCurrentOnly.remove(model);
    }

    /**
     * Get the total number of current requirements in the Current Requirements view.
     * 
     * @return the number of current requirements or 0 if editing domain has not been initialized.
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public int getNumberOfCurrentRequirements()
    {
        if (editingDomain == null)
        {
            return 0;
        }
        if (!coveredRequirementsSet)
        {
            // recompute the number of current requirements.
            getNumberOfCoveredRequirements();
        }
        return numberOfCurrentRequirements;
    }

    /**
     * Get the number of current requirements linked to an upstream requirement in the Current Requirements view.
     * 
     * @return the number of linked current requirements or 0 if editing domain has not been initialized.
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public int getNumberOfLinkedCurrentRequirements()
    {
        if (editingDomain == null)
        {
            return 0;
        }
        if (!coveredRequirementsSet)
        {
            // recompute the number of current requirements.
            getNumberOfCoveredRequirements();
        }
        return numberOfLinkedCurrentRequirements;
    }

    /**
     * Get the number of current requirements linked to an upstream requirement with partial attribute at true in the
     * Current Requirements view.
     * 
     * @return the number of partial linked current requirements or 0 if editing domain has not been initialized.
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public int getNumberOfPartialLinkedCurrentRequirements()
    {
        if (editingDomain == null)
        {
            return 0;
        }
        if (!coveredRequirementsSet)
        {
            // recompute the number of current requirements.
            getNumberOfCoveredRequirements();
        }
        return numberOfPartialLinkedCurrentRequirements;
    }

    /**
     * Get the number of requirements covered by a Current requirement in Hierarchical elements.
     * 
     * @return the number of covered requirements or 0 if editing domain has not been initialized.
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public int getNumberOfCoveredRequirements()
    {
        if (editingDomain == null)
        {
            return 0;
        }
        if (!coveredRequirementsSet)
        {
            // recompute the numbers of covered requirements and of current requirements.
            numberOfCurrentRequirements = 0;
            numberOfLinkedCurrentRequirements = 0;
            numberOfPartialLinkedCurrentRequirements = 0;
            currentRequirementsLinks.clear();
            coveredRequirements.clear();
            RequirementProject project = RequirementUtils.getRequirementProject(editingDomain);
            if (project != null)
            {
                listen(project);
                // check recursively for covered requirements in Hierarchical elements
                for (HierarchicalElement hierarchicalElement : project.getHierarchicalElement())
                {
                    listen(hierarchicalElement);
                    TreeIterator<EObject> iterator = hierarchicalElement.eAllContents();
                    iterateForAddingCoverage(iterator, false);
                }
                // check for other requirements in current requirements view
                for (SpecialChapter chapter : project.getChapter())
                {
                    listenForCurrentOnly(chapter);
                    TreeIterator<EObject> iterator = chapter.eAllContents();
                    iterateForAddingCoverage(iterator, true);
                }
            }
            coveredRequirementsSet = true;
        }
        return coveredRequirements.size();
    }

    /**
     * Get the total number of requirements in the project's Upstream model.
     * 
     * @return the number of requirements or 0 if editing domain has not been initialized.
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public int getNumberOfRequirements()
    {
        if (editingDomain == null)
        {
            return 0;
        }
        if (!totalNumberOfRequirementsSet)
        {
            // recompute the number of requirements.
            totalNumberOfRequirements = 0;
            RequirementProject project = RequirementUtils.getRequirementProject(editingDomain);
            if (project != null)
            {
                listen(project);
                UpstreamModel upstreamModel = project.getUpstreamModel();
                if (upstreamModel != null)
                {
                    listen(upstreamModel);
                    // check recursively for requirements in documents
                    for (Document doc : project.getUpstreamModel().getDocuments())
                    {
                        listen(doc);
                        TreeIterator<EObject> iterator = doc.eAllContents();
                        iterateForAddingRequirementsDefinitions(iterator);
                    }
                }
            }
            totalNumberOfRequirementsSet = true;
        }
        return totalNumberOfRequirements;
    }

    /**
     * Get the percentage rate of covered requirements.
     * 
     * @return the string representation of the coverage rate (100% if editing domain has not been initialized).
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public String getCoverageRate()
    {
        int numberOfRequirements = getNumberOfRequirements();
        int numberOfCoveredRequirements = getNumberOfCoveredRequirements();
        String coverageRate;
        if (numberOfRequirements > 0)
        {
            coverageRate = PERCENT_FORMAT.format(new Float(numberOfCoveredRequirements) / new Float(numberOfRequirements));
        }
        else
        {
            coverageRate = PERCENT_FORMAT.format(1);
        }
        return coverageRate;
    }

    /**
     * Reset the coverage and number of current requirements information (recomputing is not performed immediately,
     * which allows to reset while {@link RequirementHelper} is not fully updated).
     * 
     * @param newEditingDomain the new editing domain, or null if this class must not be used again (until next call).
     */
    public void reset(EditingDomain newEditingDomain)
    {
        editingDomain = newEditingDomain;
        stopListening();
        coveredRequirementsSet = false;
        totalNumberOfRequirementsSet = false;
    }

    /**
     * Handles the display of the coverage rate.
     * 
     * Coverage printing is linked to Upstream Requirement view. The coverage rate is computed on requirements which are
     * contained in this view only. These requirements are not covered by the editor's model only, but can be covered by
     * several models. This is the reason why linking coverage analysis to Upstream Requirement view is better than
     * linking it to the editor.
     */
    public void refreshCoverageRateDisplay()
    {
        UpstreamPage page = RequirementHelper.INSTANCE.getUpstreamPage();
        if (page != null)
        {
            IActionBars actionBars = page.getSite().getActionBars();
            if (actionBars != null)
            {
                final IStatusLineManager statusLineManager = actionBars.getStatusLineManager();
                if (statusLineManager != null)
                {
                    // get elements for coverage rate
                    int numberOfRequirements = getNumberOfRequirements();
                    int numberOfcoveredRequirements = getNumberOfCoveredRequirements();
                    String coverageRate = getCoverageRate();
                    // Construct message with coverage rate
                    final String message = String.format(Messages.getString("UpstreamPage.1"), numberOfcoveredRequirements, numberOfRequirements, coverageRate); //$NON-NLS-1$

                    // status line must be updated in a particular thread to avoid thread access violation.
                    Display.getDefault().syncExec(new Runnable()
                    {
                        /**
                         * Update status line message
                         */
                        public void run()
                        {
                            statusLineManager.setMessage(message);
                        }
                    });
                }
            }
        }
    }

    /**
     * Handles the display of the numbers of current requirements.
     * 
     * Numbers of current requirements printing is linked to Current Requirement view. Theses numbers are computed on
     * requirements which are contained in this view only.
     */
    public void refreshNumberOfCurrentRequirementsDisplay()
    {
        CurrentPage page = RequirementHelper.INSTANCE.getCurrentPage();
        if (page != null)
        {
            IActionBars actionBars = page.getSite().getActionBars();
            if (actionBars != null)
            {
                final IStatusLineManager statusLineManager = actionBars.getStatusLineManager();
                if (statusLineManager != null)
                {
                    // get elements for numbers of current requirements
                    int current = getNumberOfCurrentRequirements();
                    int linkto = getNumberOfLinkedCurrentRequirements();
                    int partial = getNumberOfPartialLinkedCurrentRequirements();
                    // Construct message with numbers of current requirements
                    final String message = String.format(Messages.getString(CURRENT_PAGE_STATUS_MESSAGE0), current, linkto, partial, current - linkto);

                    // status line must be updated in a particular thread to avoid thread access violation.
                    Display.getDefault().syncExec(new Runnable()
                    {
                        /**
                         * Update status line message
                         */
                        public void run()
                        {
                            statusLineManager.setMessage(message);
                        }
                    });
                }
            }
        }
    }

}
