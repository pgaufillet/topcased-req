/***********************************************************************************************************************
 * Copyright (c) 2011 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Philippe ROLAND (Atos) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DifferenceKind;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.topcased.requirement.core.utils.ContainerAssigner.ContainerAssignerFactory;

import ttm.Document;

/**
 * Calculates differences between two requirement resources<br>
 * Update : 29 november 2011<br>
 * 
 * @author <a href="mailto:philippe.roland@atos.net">Philippe ROLAND</a>
 * @since Topcased 5.2.0
 */
public class RequirementDifferenceCalculator
{
    private EList<DiffElement> deletions;

    private EList<DiffElement> changes;

    private EList<DiffElement> additions;

    private EList<DiffElement> moves;

    private boolean isPartialImport = false;

    private Map<Document, Document> mergedDocuments;

    public RequirementDifferenceCalculator(Map<Document, Document> mergedDocuments, boolean isPartialImport)
    {
        this.isPartialImport = isPartialImport;

        deletions = new BasicEList<DiffElement>();
        changes = new BasicEList<DiffElement>();
        additions = new BasicEList<DiffElement>();
        moves = new BasicEList<DiffElement>();

        this.mergedDocuments = mergedDocuments;
    }
    
    public void calculate(IProgressMonitor monitor) throws InterruptedException {
        // Call the EMF comparison service
        HashMap<String, Object> matchOptions = new HashMap<String, Object>();
        matchOptions.put(MatchOptions.OPTION_IGNORE_ID, false);
        matchOptions.put(MatchOptions.OPTION_IGNORE_XMI_ID, true);
        matchOptions.put(MatchOptions.OPTION_PROGRESS_MONITOR, monitor);

        for (Entry<Document, Document> entry : mergedDocuments.entrySet())
        {
            ContainerAssignerFactory factory = new ContainerAssignerFactory();
            ContainerAssigner container1 = factory.create(entry.getValue());
            Resource r1dummy = new XMIResourceImpl();
            r1dummy.getContents().add(entry.getValue());

            ContainerAssigner container2 = factory.create(entry.getKey());
            Resource r2dummy = new XMIResourceImpl();
            r2dummy.getContents().add(entry.getKey());

            MatchModel match = MatchService.doMatch(entry.getKey(), entry.getValue(), matchOptions);
            DiffModel diff = DiffService.doDiff(match);
            for (DiffElement aDifference : diff.getOwnedElements())
            {
                buildDifferenceLists(aDifference);
            }
            container1.backup();
            container2.backup();
        }
    }

    /**
     * According to the difference kind, the element itself is included in on of the four lists.
     * 
     * @param difference A difference object
     */
    private void buildDifferenceLists(DiffElement difference)
    {
        if (!(difference instanceof DiffGroup))
        {
            if (difference.getKind().equals(DifferenceKind.MOVE))
            {
                moves.add(difference);
            }

            if (difference.getKind().equals(DifferenceKind.DELETION))
            {
                // Do not add deletion if difference element is a requirement
                // and this is a partial import
                EObject removedElement = ((ModelElementChangeRightTarget) difference).getRightElement();
                if (!(removedElement instanceof ttm.Requirement) || !isPartialImport)
                {
                    deletions.add(difference);
                }

            }

            if (difference.getKind().equals(DifferenceKind.CHANGE))
            {
                changes.add(difference);
            }

            if (difference.getKind().equals(DifferenceKind.ADDITION))
            {
                additions.add(difference);
            }
        }

        // build by sorting differences according to their kind
        if (difference.getSubDiffElements() != null)
        {
            for (DiffElement subDiff : difference.getSubDiffElements())
            {
                buildDifferenceLists(subDiff);
            }
        }
    }

    public EList<DiffElement> getDeletions()
    {
        return deletions;
    }

    public EList<DiffElement> getChanges()
    {
        return changes;
    }

    public EList<DiffElement> getAdditions()
    {
        return additions;
    }

    public EList<DiffElement> getMoves()
    {
        return moves;
    }

    public Map<Document, Document> getMergedDocuments()
    {
        return mergedDocuments;
    }

}
