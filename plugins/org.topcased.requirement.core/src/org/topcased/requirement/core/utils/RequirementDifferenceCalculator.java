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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DifferenceKind;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.topcased.requirement.core.utils.ContainerAssigner.ContainerAssignerFactory;
import org.topcased.typesmodel.model.inittypes.DeletionParameters;

import ttm.Attribute;
import ttm.Document;
import ttm.IdentifiedElement;
import ttm.Requirement;
import ttm.Text;
import ttm.TtmFactory;
import ttm.TtmPackage;

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
    
    private EList<DiffElement> addToRoot;

    private EList<DiffElement> additions;

    private EList<DiffElement> moves;

    private boolean isPartialImport = false;

    private Map<Document, Document> mergedDocuments;

    private Map<Document, DeletionParameters> deletionParametersDocMap;
    
    private Set<Requirement> coveredReq;
    

    public RequirementDifferenceCalculator(Map<Document, Document> mergedDocuments, Map<Document, DeletionParameters> deletionParametersDocMap, boolean isPartialImport)
    {
        this.isPartialImport = isPartialImport;

        deletions = new BasicEList<DiffElement>();
        changes = new BasicEList<DiffElement>();
        additions = new BasicEList<DiffElement>();
        moves = new BasicEList<DiffElement>();
        addToRoot = new BasicEList<DiffElement>();
        
        this.mergedDocuments = mergedDocuments;

        if (deletionParametersDocMap == null) {
            this.deletionParametersDocMap = new HashMap<Document, DeletionParameters>();
        } else {
            this.deletionParametersDocMap = deletionParametersDocMap;
        }
    }

    public void calculate(IProgressMonitor monitor) throws InterruptedException {

        coveredReq  = new HashSet<Requirement>();
        
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
                buildDifferenceLists(aDifference, entry.getValue());
            }
            diff.getOwnedElements().addAll(addToRoot);
            container1.backup();
            container2.backup();
        }
    }

    /**
     * According to the difference kind, the element itself is included in on of the four lists.
     * 
     * @param difference A difference object
     */
    protected void buildDifferenceLists(DiffElement difference, Document originalDocument)
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
                if (!(removedElement instanceof ttm.Requirement && isPartialImport))
                {
                    deletions.add(difference);
                }
            }

            if (difference.getKind().equals(DifferenceKind.CHANGE))
            {
                // we always compare equivalent documents ident is not relevant 
                if (difference instanceof UpdateAttribute)
                {
                    UpdateAttribute update = (UpdateAttribute) difference;
                    
                    Requirement reqToDelete = matchDeleteOnAttributeChange(update, originalDocument);
                    if (reqToDelete != null) {
                        ModelElementChangeRightTarget deleteDiffElement = DiffFactory.eINSTANCE.createModelElementChangeRightTarget();
                        deleteDiffElement.setRightElement(reqToDelete);
                        deleteDiffElement.setLeftParent(originalDocument);
                        deletions.add(deleteDiffElement);
                    }

                    //Workaround? setting the ident before the doMatch is done yeilded strange results...
                    else {
                        String newText = matchTextOnAttributeChange(update, originalDocument);
                        if (newText != null)
                        {
                            Text text = TtmFactory.eINSTANCE.createText();
                            text.setValue(newText);

                            UpdateAttribute updateAttribute = DiffFactory.eINSTANCE.createUpdateAttribute();
                            updateAttribute.setAttribute(TtmPackage.Literals.TEXT__VALUE);
                            updateAttribute.setLeftElement(text);
                            updateAttribute.setRightElement(((Text)update.getRightElement()));
                            addToRoot.add(updateAttribute);
                            changes.add(updateAttribute);

                            EList<Text> texts = ((Text)update.getRightElement()).getParent().getTexts();
                            if (texts.size()>1)
                            {
                                for (int i = 1; i < texts.size(); i++)
                                {
                                    ModelElementChangeRightTarget deleteElement = DiffFactory.eINSTANCE.createModelElementChangeRightTarget();
                                    deleteElement.setRightElement(texts.get(i));
                                    deleteElement.setLeftParent(originalDocument);
                                    addToRoot.add(deleteElement);
                                    deletions.add(deleteElement);
                                }
                            }

                        } else if (!(update.getRightElement() instanceof Document && update.getLeftElement() instanceof Document &&
                                update.getAttribute() != null && TtmPackage.Literals.IDENTIFIED_ELEMENT__IDENT.equals(update.getAttribute()))) 
                        {
                            changes.add(difference);
                        }
                    }
                }

            }

            if (difference.getKind().equals(DifferenceKind.ADDITION))
            {

                Requirement reqToDelete = null;
                String newText = null;
                if (difference instanceof ModelElementChangeLeftTarget) {
                    ModelElementChangeLeftTarget change = (ModelElementChangeLeftTarget) difference;
                    reqToDelete = matchDeleteOnAddition(change, originalDocument);

                    if (reqToDelete != null) {
                        ModelElementChangeRightTarget deleteDiffElement = DiffFactory.eINSTANCE.createModelElementChangeRightTarget();
                        deleteDiffElement.setRightElement(reqToDelete);
                        deleteDiffElement.setLeftParent(originalDocument);
                        deletions.add(deleteDiffElement);
                    } else {
                        newText = matchTextOnAttributeAddition(change);
                        if (newText != null) {
                            // retrieve the exisiting add text and modify it
                            Text t = (Text)change.getLeftElement();
                            t.setValue(newText);

                            additions.add(change);
                        } else if (!(((ModelElementChangeLeftTarget) difference).getLeftElement() instanceof Text)
                                || !(((Text)((ModelElementChangeLeftTarget) difference).getLeftElement()).getParent() instanceof Requirement))
                        {
                            additions.add(difference);
                        }
                    }
                }

            }
        }

        // build by sorting differences according to their kind
        if (difference.getSubDiffElements() != null)
        {
            for (DiffElement subDiff : difference.getSubDiffElements())
            {
                buildDifferenceLists(subDiff, originalDocument);
            }
        }
    }
    
    protected String matchTextOnAttributeAddition(ModelElementChangeLeftTarget change)
    {
        
        if (change.getLeftElement() instanceof Text && ((Text)change.getLeftElement()).getParent() instanceof Requirement && !coveredReq.contains(((Text)change.getLeftElement()).getParent()))
        {
            CharSequence description = buildDescription(((Text)change.getLeftElement()).getParent());
            coveredReq.add((Requirement) ((Text)change.getLeftElement()).getParent());
            return description.toString();
        }

        return null;
        
    }
    
    protected String matchTextOnAttributeDeletion(ModelElementChangeRightTarget change)
    {
        Requirement modifiedReq = null;
        if (change.getRightElement() instanceof Text && ((Text)change.getRightElement()).getParent() instanceof Requirement) {
            modifiedReq = (Requirement)((Text)change.getRightElement()).getParent();
        }
        
        if (modifiedReq != null)
        {
            return "";
        }
        
        return null;
    }

    protected String matchTextOnAttributeChange(UpdateAttribute update, Document originalDocument)
    {
        if (TtmPackage.Literals.TEXT__VALUE.equals(update.getAttribute()) && !coveredReq.contains(((Text)update.getLeftElement()).getParent())) {
            coveredReq.add((Requirement) ((Text)update.getLeftElement()).getParent());
            return computeTextToChange((Requirement)((Text)update.getLeftElement()).getParent(), (Requirement)((Text)update.getRightElement()).getParent());
        }

        return null;
    }
    
    protected String computeTextToChange(Requirement modifiedReq, Requirement originalReq) {
        
        CharSequence origDesc = buildDescription(originalReq);
        CharSequence modifiedDesc = buildDescription(modifiedReq);
        
        if (modifiedDesc.equals(origDesc)) {
            return null;
        }
                
        return modifiedDesc.toString();
    }
    
    protected Requirement matchDeleteOnAddition(ModelElementChangeLeftTarget change, Document originalDocument)
    {
        EObject modifiedReq = null;
        if (change.getLeftElement() instanceof Requirement) {
            modifiedReq = change.getLeftElement();
        } else if (change.getLeftElement() instanceof Text) {
            modifiedReq = ((Text)change.getLeftElement()).getParent();
        } else if (change.getLeftElement() instanceof Attribute) {
            modifiedReq = ((Attribute)change.getLeftElement()).getParent();
        }

        if (modifiedReq != null) {
           return computeReqToDelete(modifiedReq, originalDocument);
        }

        return null;
    }

    protected Requirement matchDeleteOnAttributeChange(UpdateAttribute update, Document originalDocument)
    {
        if (TtmPackage.Literals.TEXT__VALUE.equals(update.getAttribute())) {
            return computeReqToDelete(((Text)update.getRightElement()).getParent(), originalDocument);
        }

        else if (TtmPackage.Literals.ATTRIBUTE__VALUE.equals(update.getAttribute())) {
            return computeReqToDelete(((Attribute)update.getRightElement()).getParent(), originalDocument);
        }

        else if (TtmPackage.Literals.IDENTIFIED_ELEMENT__IDENT.equals(update.getAttribute())) {
            return computeReqToDelete(update.getRightElement(), originalDocument);
        }

        return null;
    }

    protected Requirement computeReqToDelete(EObject modifiedReq, Document originalDocument) {
        DeletionParameters deletionParameters = deletionParametersDocMap.get(originalDocument);
        if (isPartialImport && deletionParameters != null) {
            Pattern deletionPattern = Pattern.compile(deletionParameters.getRegex(), Pattern.CASE_INSENSITIVE);

            if (modifiedReq instanceof Requirement) {
                Requirement req = (Requirement) modifiedReq;
                if (deletionParameters.isMatchId()) {
                    Matcher matcher = deletionPattern.matcher(req.getIdent());

                    if (matcher.matches()) {
                        String reqId = matcher.group(1);

                        return getUpstreamWithId(originalDocument, reqId);
                    }
                }

                if (deletionParameters.isMatchDescription()) {
                    if (deletionPattern.matcher(buildDescription(req)).matches()) {
                        return getUpstreamWithId(originalDocument, req.getIdent());
                    }
                }

                for (Attribute att : req.getAttributes()) {
                    if (deletionParameters.getAttributesToMatch().contains(att.getName()) && deletionPattern.matcher(att.getValue()).matches()) {
                        return getUpstreamWithId(originalDocument, req.getIdent());
                    }
                }
            }
        }
        return null;
    }
    
    protected Requirement getUpstreamWithId(Document document, String id) {
        Collection<Requirement> upstreams = RequirementUtils.getUpstreams(document);

        for (Requirement upstream : upstreams) {
            if (id.equals(upstream.getIdent())) {
                return upstream;
            }
        }
        return null;
    }

    protected CharSequence buildDescription(IdentifiedElement elem) {
        StringBuilder descBuilder = new StringBuilder();
        for (Text t : elem.getTexts()) {
            descBuilder.append(t.getValue());
            descBuilder.append("\n");
        }
        return descBuilder;
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
