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
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.osgi.util.NLS;
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
     * Enumerate class indicating how an upstream requirement is covered by current requirements.
     */
    private enum CoverageType {
        /** covered by at least one current requirement, and no partial link */
        FULLY_COVERED,
        /** covered by at least one current requirement with partial */
        COVERED_WITH_PARTIAL,
        /** not covered by any current requirement */
        NOT_COVERED;

        /**
         * Get how an upstream requirement is covered by current requirements
         * 
         * @param upstreamRequirement upstream requirement
         * @return coverage state (covered with no partial, covered with partial or not covered)
         */
        public static CoverageType getCoverage(Requirement upstreamRequirement)
        {
            if (!RequirementUtils.isLinked(upstreamRequirement))
            {
                return CoverageType.NOT_COVERED;
            }
            else if (RequirementUtils.isPartial(upstreamRequirement))
            {
                return CoverageType.COVERED_WITH_PARTIAL;
            }
            else
            {
                return CoverageType.FULLY_COVERED;
            }
        }
    }

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

    /** The message entry for displaying coverage of upstream requirements in the status bar */
    private static final String UPSTREAM_PAGE_STATUS_MESSAGE = "UpstreamPage.2"; //$NON-NLS-1$

    /** The editing domain in which coverage rate is computed */
    private EditingDomain editingDomain = null;

    /** The number of upstream requirements */
    private int totalNumberOfUpstreamRequirements = 0;

    /** The number of upstream requirements covered with or without partial */
    private int numberOfCoveredUpstreamRequirements = 0;

    /** The number of upstream requirements covered with at least one partial */
    private int numberOfCoveredWithPartialUpstreamRequirements = 0;

    /** The number of current requirements */
    private int totalNumberOfCurrentRequirements = 0;

    /** The number of linked current requirements */
    private int numberOfLinkedCurrentRequirements = 0;

    /** The number of linked current requirements with partial */
    private int numberOfPartialLinkedCurrentRequirements = 0;

    /** whether the metrics about current and upstream requirements are correct */
    private boolean requirementMetricsSet = false;

    /** The current requirements in Hierarchical elements and their linkage type */
    private Map<CurrentRequirement, LinkType> currentRequirementsLinks = new HashMap<CurrentRequirement, LinkType>();

    /** The upstream requirements and their coverage type */
    private Map<Requirement, CoverageType> upstreamRequirementsCoverage = new HashMap<Requirement, CoverageType>();

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
     * @deprecated use {@link #getRequirementsUpdaterAdapter()} instead
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
                        if (added instanceof Requirement)
                        {
                            if (!ignoreForCoverage)
                            {
                                // update coverage metrics
                                updateUpstreamRequirementCoverage((Requirement) added, false);
                            }
                            // update number of current
                            updateCurrentRequirementNumbers(coveringRequirement, false);
                        }
                    }

                    protected void remove()
                    {
                        if (removed instanceof Requirement)
                        {
                            // update upstream coverage metrics, only if upstream is not being removed
                            if (!ignoreForCoverage && upstreamRequirementsCoverage.containsKey(removed))
                            {
                                updateUpstreamRequirementCoverage((Requirement) removed, false);
                            }
                            // update number of current
                            updateCurrentRequirementNumbers(coveringRequirement, false);
                        }
                    }
                };

            }
            if (!ignoreForCoverage)
            {
                // refresh coverage rate printing
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
                        // update number of current and upstream coverage metrics
                        addCoverage(coveringRequirement, ignoreForCoverage);
                    }

                    protected void remove()
                    {
                        // do nothing
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
                    totalNumberOfCurrentRequirements--;
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
                    totalNumberOfCurrentRequirements++;
            }
            currentRequirementsLinks.put(updatedRequirement, linkage);
        }
    }

    /**
     * Indicate that a change occurred concerning an upstream requirement
     * 
     * @param updatedRequirement the upstream requirement which has been updated
     * @param removing true if updatedRequirement is being removed (false if it is being updated or added)
     */
    private void updateUpstreamRequirementCoverage(Requirement updatedRequirement, boolean removing)
    {
        CoverageType coverage = CoverageType.getCoverage(updatedRequirement);
        if (upstreamRequirementsCoverage.containsKey(updatedRequirement))
        {
            // remove upstream requirement from count (updating or removing)
            CoverageType oldCoverage = upstreamRequirementsCoverage.get(updatedRequirement);
            if (coverage != null && coverage.equals(oldCoverage) && !removing)
            {
                // no significant change, no need to go further
                return;
            }
            switch (oldCoverage)
            {
                case COVERED_WITH_PARTIAL:
                    numberOfCoveredWithPartialUpstreamRequirements--;
                case FULLY_COVERED:
                    numberOfCoveredUpstreamRequirements--;
                case NOT_COVERED:
                default:
                    totalNumberOfUpstreamRequirements--;
            }
        }
        if (removing)
        {
            upstreamRequirementsCoverage.remove(updatedRequirement);
        }
        else
        {
            // add current requirement to count
            switch (coverage)
            {
                case COVERED_WITH_PARTIAL:
                    numberOfCoveredWithPartialUpstreamRequirements++;
                case FULLY_COVERED:
                    numberOfCoveredUpstreamRequirements++;
                case NOT_COVERED:
                default:
                    totalNumberOfUpstreamRequirements++;
            }
            upstreamRequirementsCoverage.put(updatedRequirement, coverage);
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
            // update upstream coverage metrics
            EObject coveredRequirement = addedRequirementCoverage.getValue();
            if (coveredRequirement instanceof Requirement)
            {
                updateUpstreamRequirementCoverage((Requirement) coveredRequirement, false);
            }
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

                // update upstream coverage metrics
                EObject coveredRequirement = ((AttributeLink) removedRequirementCoverage).getValue();
                if (coveredRequirement instanceof Requirement)
                {
                    updateUpstreamRequirementCoverage((Requirement) coveredRequirement, false);
                }
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
        // update upstream coverage metrics
        updateUpstreamRequirementCoverage(addedRequirement, false);
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
            // update upstream coverage metrics
            updateUpstreamRequirementCoverage(removedRequirement, true);

            // update number of current
            for (Setting setting : RequirementUtils.getCrossReferences(removedRequirement))
            {
                if (setting.getEObject() instanceof AttributeLink)
                {
                    EObject current = setting.getEObject().eContainer();
                    if (current instanceof CurrentRequirement)
                    {
                        updateCurrentRequirementNumbers((CurrentRequirement) current, false);
                    }
                }
            }
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
     * @deprecated use {@link #listen(EObject)} instead
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
     * Recompute all the metrics about number of current requirements and upstream requirements coverage
     */
    private void recomputeAllMetrics()
    {
        // reset number of current
        totalNumberOfCurrentRequirements = 0;
        numberOfLinkedCurrentRequirements = 0;
        numberOfPartialLinkedCurrentRequirements = 0;
        currentRequirementsLinks.clear();
        // reset coverage of upstream
        totalNumberOfUpstreamRequirements = 0;
        numberOfCoveredUpstreamRequirements = 0;
        numberOfCoveredWithPartialUpstreamRequirements = 0;
        upstreamRequirementsCoverage.clear();
        // compute from requirement project
        RequirementProject project = RequirementUtils.getRequirementProject(editingDomain);
        if (project != null)
        {
            listen(project);
            // check in upstream model
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
                // take special chapters in account for all modifications
                listen(chapter);
                // listenForCurrentOnly(chapter);
                TreeIterator<EObject> iterator = chapter.eAllContents();
                iterateForAddingCoverage(iterator, true);
            }
        }
        requirementMetricsSet = true;
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
        if (!requirementMetricsSet)
        {
            // recompute the metrics about upstream and current requirements.
            recomputeAllMetrics();
        }
        return totalNumberOfCurrentRequirements;
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
        if (!requirementMetricsSet)
        {
            // recompute the metrics about upstream and current requirements.
            recomputeAllMetrics();
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
        if (!requirementMetricsSet)
        {
            // recompute the metrics about upstream and current requirements.
            recomputeAllMetrics();
        }
        return numberOfPartialLinkedCurrentRequirements;
    }

    /**
     * Get the string to display a rate in percent.
     * 
     * @param numerator the integer to use as numerator
     * @param denominator the integer to use as denominator
     * @return the string representation of the rate numerator/denominator.
     */
    private String getRateDisplay(int numerator, int denominator)
    {
        String coverageRate;
        if (denominator > 0)
        {
            coverageRate = PERCENT_FORMAT.format(new Float(numerator) / new Float(denominator));
        }
        else
        {
            coverageRate = PERCENT_FORMAT.format(1);
        }
        return coverageRate;
    }

    /**
     * Get the total number of upstream requirements in the project's Upstream model.
     * 
     * @return the number of upstream requirements or 0 if editing domain has not been initialized.
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public int getNumberOfRequirements()
    {
        if (editingDomain == null)
        {
            return 0;
        }
        if (!requirementMetricsSet)
        {
            // recompute the metrics about upstream and current requirements.
            recomputeAllMetrics();
        }
        return totalNumberOfUpstreamRequirements;
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
        if (!requirementMetricsSet)
        {
            // recompute the metrics about upstream and current requirements.
            recomputeAllMetrics();
        }
        return numberOfCoveredUpstreamRequirements;
    }

    /**
     * Get the percentage rate of covered upstream requirements.
     * 
     * @return the string representation of the coverage rate (100% if editing domain has not been initialized).
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public String getCoverageRate()
    {
        int numberOfRequirements = getNumberOfRequirements();
        int numberOfCoveredRequirements = getNumberOfCoveredRequirements();
        return getRateDisplay(numberOfCoveredRequirements, numberOfRequirements);
    }

    /**
     * Get the number of upstream requirements which are covered by current requirements with at least one partial link.
     * 
     * @return the number of upstream requirements or 0 if editing domain has not been initialized.
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public int getNumberOfCoveredWithPartialUpstreamRequirements()
    {
        if (editingDomain == null)
        {
            return 0;
        }
        if (!requirementMetricsSet)
        {
            // recompute the metrics about upstream and current requirements.
            recomputeAllMetrics();
        }
        return numberOfCoveredWithPartialUpstreamRequirements;
    }

    /**
     * Get the percentage rate of upstream requirements covered by current requirements with at least one partial link.
     * 
     * @return the string representation of the coverage rate (100% if editing domain has not been initialized).
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public String getCoveredWithPartialUpstreamRate()
    {
        int numberOfRequirements = getNumberOfRequirements();
        int numberOfCoveredRequirements = getNumberOfCoveredWithPartialUpstreamRequirements();
        return getRateDisplay(numberOfCoveredRequirements, numberOfRequirements);
    }

    /**
     * Get the number of upstream requirements which are covered by current requirements with no partial link.
     * 
     * @return the number of upstream requirements or 0 if editing domain has not been initialized.
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public int getNumberOfFullyCoveredUpstreamRequirements()
    {
        if (editingDomain == null)
        {
            return 0;
        }
        if (!requirementMetricsSet)
        {
            // recompute the metrics about upstream and current requirements.
            recomputeAllMetrics();
        }
        return numberOfCoveredUpstreamRequirements - numberOfCoveredWithPartialUpstreamRequirements;
    }

    /**
     * Get the percentage rate of upstream requirements covered by current requirements with no partial link.
     * 
     * @return the string representation of the coverage rate (100% if editing domain has not been initialized).
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public String getFullyCoveredUpstreamRate()
    {
        int numberOfRequirements = getNumberOfRequirements();
        int numberOfCoveredRequirements = getNumberOfFullyCoveredUpstreamRequirements();
        return getRateDisplay(numberOfCoveredRequirements, numberOfRequirements);
    }

    /**
     * Get the number of upstream requirements which are not covered by any current requirement.
     * 
     * @return the number of upstream requirements or 0 if editing domain has not been initialized.
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public int getNumberOfNotCoveredUpstreamRequirements()
    {
        if (editingDomain == null)
        {
            return 0;
        }
        if (!requirementMetricsSet)
        {
            // recompute the metrics about upstream and current requirements.
            recomputeAllMetrics();
        }
        return totalNumberOfUpstreamRequirements - numberOfCoveredUpstreamRequirements;
    }

    /**
     * Get the percentage rate of upstream requirements covered by current requirements with no partial link.
     * 
     * @return the string representation of the coverage rate (100% if editing domain has not been initialized).
     * @see RequirementCoverageComputer#reset(EditingDomain)
     */
    public String getNotCoveredUpstreamRate()
    {
        int numberOfRequirements = getNumberOfRequirements();
        int numberOfNotCoveredRequirements = getNumberOfNotCoveredUpstreamRequirements();
        return getRateDisplay(numberOfNotCoveredRequirements, numberOfRequirements);
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
        requirementMetricsSet = false;
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
                    // get elements for coverage metrics
                    int numberOfRequirements = getNumberOfRequirements();
                    int numberOfCovered = getNumberOfFullyCoveredUpstreamRequirements();
                    String coveredRate = getFullyCoveredUpstreamRate();
                    int numberOfTraced = getNumberOfCoveredWithPartialUpstreamRequirements();
                    String tracedRate = getCoveredWithPartialUpstreamRate();
                    int numberOfNotCovered = getNumberOfNotCoveredUpstreamRequirements();
                    String notCoveredRate = getNotCoveredUpstreamRate();
                    // Construct message with coverage metrics
                    Object[] params = new Object[] {numberOfRequirements, numberOfCovered, coveredRate, numberOfTraced, tracedRate, numberOfNotCovered, notCoveredRate};
                    final String message = NLS.bind(Messages.getString(UPSTREAM_PAGE_STATUS_MESSAGE), params);

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
                    final String message = NLS.bind(Messages.getString(CURRENT_PAGE_STATUS_MESSAGE0), new Object[] {current, linkto, partial, current - linkto});

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
