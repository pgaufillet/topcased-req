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
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.Messages;
import org.topcased.requirement.core.views.upstream.UpstreamPage;

import ttm.Document;
import ttm.Requirement;
import ttm.TtmPackage;

/**
 * This singleton class provides information about the requirements coverage. It computes the total number of
 * requirements in the project's Upstream model, the number among them which are covered in Hierarchical Elements, and
 * the ratio between the two as a percentage.
 * 
 * @author Vincent Hemery
 */
public final class RequirementCoverageComputer
{
    /** The singleton */
    public static final RequirementCoverageComputer INSTANCE = new RequirementCoverageComputer();

    /** The format for displaying a percentage. */
    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#0.##%");

    /** The editing domain in which coverage rate is computed */
    private EditingDomain editingDomain = null;

    /** The number of requirements */
    private int totalNumberOfRequirements = 0;

    /** whether the totalNumberOfRequirements attribute is correct */
    private boolean totalNumberOfRequirementsSet = false;

    /** The requirements covered by Current requirements in Hierarchical elements */
    private Map<EObject, List<CurrentRequirement>> coveredRequirements = new HashMap<EObject, List<CurrentRequirement>>();

    /** whether the coveredRequirements attribute is correct */
    private boolean coveredRequirementsSet = false;

    /** The adapter to listen at model modifications to update requirements coverage */
    private Adapter requirementsUpdaterAdapter = null;

    /** The model elements which modifications are listened at */
    private Collection<EObject> listenedModelElements = new ArrayList<EObject>();

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
                    modelHasChanged(msg);
                }
            };
        }
        return requirementsUpdaterAdapter;
    }

    /**
     * Update coverage information when a model element has changed.
     * 
     * @param msg the notification message
     */
    protected void modelHasChanged(Notification msg)
    {
        Object modifiedFeature = msg.getFeature();
        final Object removed = msg.getOldValue();
        final Object added = msg.getNewValue();
        // features which may contain requirements definitions
        if (RequirementPackage.Literals.REQUIREMENT_PROJECT__UPSTREAM_MODEL.equals(modifiedFeature) || TtmPackage.Literals.PROJECT__DOCUMENTS.equals(modifiedFeature)
                || TtmPackage.Literals.HIERARCHICAL_ELEMENT__CHILDREN.equals(modifiedFeature))
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
        // features which may contain Link_to attribute for requirements coverage
        else if (RequirementPackage.Literals.REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT.equals(modifiedFeature) || RequirementPackage.Literals.HIERARCHICAL_ELEMENT__CHILDREN.equals(modifiedFeature)
                || RequirementPackage.Literals.HIERARCHICAL_ELEMENT__REQUIREMENT.equals(modifiedFeature) || RequirementPackage.Literals.REQUIREMENT__ATTRIBUTE.equals(modifiedFeature))
        {
            new NotificationEventTypeSwitcher(msg.getEventType())
            {
                protected void add()
                {
                    addCoverage(added);
                }

                protected void remove()
                {
                    removeCoverage(removed);
                }
            };
            // refresh coverage rate printing
            refreshCoverageRateDisplay();
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
                            addToCoveredRequirementsAttribute(coveringRequirement, (EObject) added);
                        }
                    }

                    protected void remove()
                    {
                        if (removed instanceof EObject)
                        {
                            removeFromCoveredRequirementsAttribute(coveringRequirement, (EObject) removed);
                        }
                    }
                };

            }
            // refresh coverage rate printing
            refreshCoverageRateDisplay();
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
     * Indicate coverage requirements have been added
     * 
     * @param added the added model object (or list of objects) containing Attribute Links for requirement coverage.
     */
    protected void addCoverage(Object added)
    {
        if (added == null)
        {
            return;
        }
        else if (added instanceof AttributeLink)
        {
            specifiedAddCoverage((AttributeLink) added);
        }
        else if (added instanceof List< ? >)
        {
            for (Object listElement : (List< ? >) added)
            {
                addCoverage(listElement);
            }
        }
        else if (added instanceof EObject)
        {
            listen((EObject) added);
            // check in object's content
            iterateForAddingCoverage(((EObject) added).eAllContents());
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
     */
    private void iterateForAddingCoverage(TreeIterator<EObject> iterator)
    {
        while (iterator.hasNext())
        {
            EObject nextElement = iterator.next();
            if (nextElement instanceof AttributeLink)
            {
                specifiedAddCoverage((AttributeLink) nextElement);
            }
            else
            {
                // check if can contain covered requirements
                boolean canContain = nextElement instanceof HierarchicalElement || nextElement instanceof CurrentRequirement;
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
     * Indicate coverage requirements have been removed. The parameter object must be removed from the model.
     * 
     * @param removed the removed model object (or list of objects) containing Attribute Links for requirement coverage.
     */
    protected void removeCoverage(Object removed)
    {
        if (removed == null)
        {
            return;
        }
        else if (removed instanceof AttributeLink)
        {
            specifiedRemoveCoverage((AttributeLink) removed);
        }
        else if (removed instanceof List< ? >)
        {
            for (Object listElement : (List< ? >) removed)
            {
                removeCoverage(listElement);
            }
        }
        else if (removed instanceof EObject)
        {
            stopListening((EObject) removed);
            // check in object's content
            iterateForRemovingCoverage(((EObject) removed).eAllContents());
        }
    }

    /**
     * Indicate coverage requirements have been removed. The parameter object must be removed from the model.
     * 
     * @param removedRequirementCoverage the added requirement covering Attribute Link object.
     */
    private void specifiedRemoveCoverage(AttributeLink removedRequirementCoverage)
    {
        if (removedRequirementCoverage != null)
        {
            stopListening((AttributeLink) removedRequirementCoverage);
            // remove (partial) coverage of attribute's value requirement
            EObject covering = ((AttributeLink) removedRequirementCoverage).eContainer();
            if (covering instanceof CurrentRequirement)
            {
                EObject coveredRequirement = ((AttributeLink) removedRequirementCoverage).getValue();
                removeFromCoveredRequirementsAttribute((CurrentRequirement) covering, coveredRequirement);
            }
        }
    }

    /**
     * Iterate other elements to remove coverage requirements.
     * 
     * @param iterator the iterator other EObjects.
     */
    private void iterateForRemovingCoverage(TreeIterator<EObject> iterator)
    {
        while (iterator.hasNext())
        {
            EObject nextElement = iterator.next();
            if (nextElement instanceof AttributeLink)
            {
                specifiedRemoveCoverage((AttributeLink) nextElement);
            }
            else
            {
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
     * Unregister from all listened model elements.
     */
    private void stopListening()
    {
        for (EObject model : listenedModelElements)
        {
            model.eAdapters().remove(getRequirementsUpdaterAdapter());
        }
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
            // recompute the number of covered requirements.
            coveredRequirements = new HashMap<EObject, List<CurrentRequirement>>();
            RequirementProject project = RequirementUtils.getRequirementProject(editingDomain);
            if (project != null)
            {
                listen(project);
                // check recursively for covered requirements in Hierarchical elements
                for (HierarchicalElement hierarchicalElement : project.getHierarchicalElement())
                {
                    listen(hierarchicalElement);
                    TreeIterator<EObject> iterator = hierarchicalElement.eAllContents();
                    iterateForAddingCoverage(iterator);
                }
            }
            coveredRequirementsSet = true;
        }
        return coveredRequirements.size();
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
     * Reset the coverage information (recomputing is not performed immediately, which allow to reset while
     * {@link RequirementHelper} is not fully updated).
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

}
