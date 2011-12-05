package org.topcased.requirement.core.utils.impact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.utils.RequirementDifferenceCalculator;
import org.topcased.requirement.core.utils.RequirementUtils;

import ttm.Document;

//TODO copyright, documentation
public class RequirementTimestampMonitor
{
    private static final String LASTANALYZED_SUFFIX = "LA";
    private static final String LASTUSERCHOICE_SUFFIX = "LUC";
    
    public static void onLoad(List<Document> documents) throws InterruptedException {
        //All documents are assumed to have the same project
        RequirementProject project = RequirementUtils.getRequirementProject(documents.get(0).eResource());
        if (project == null) {
            return;
        }
        List<Document> foundDiff = new ArrayList<Document>();
        for (Document document : documents) {
            long timestamp = document.eResource().getTimeStamp();
            EAnnotation annotation  = findTimeStampAnnotation(document);
            //if no hash-bearing annotation is found, create hash
            if (annotation == null) {
                updateCreateTimestamp(document, String.valueOf(timestamp), String.valueOf(timestamp));
            }
            //otherwise, if hash is different, offer impact analysis
            else if (!annotation.getDetails().get(getDocumentLastAnalyzedKey(document)).equals(String.valueOf(timestamp))
                    && !annotation.getDetails().get(getDocumentLastUserChoiceKey(document)).equals(String.valueOf(timestamp))) {
                foundDiff.add(document);
            }
        }
        
        //Ensure this is not called again even if no analysis is then made
        for (Document document : foundDiff) {
            long timestamp = document.eResource().getTimeStamp();
            updateCreateTimestamp(document, null, String.valueOf(timestamp));
        }
        
        //TODO add wizard page and retrieve these variables
        //map contains documents to analyze as key and the version to compare against as value
        Map<Document,Document> docsToAnalyze = new HashMap<Document,Document>();
        for(Document document : foundDiff) {
            docsToAnalyze.put(document, document);
        }
        //TODO end todo
        
        List<Resource> resources = new ArrayList<Resource>();
        for (Document document : docsToAnalyze.keySet()) {
            //update timestamp
            long timestamp = document.eResource().getTimeStamp();
            updateCreateTimestamp(document, String.valueOf(timestamp), String.valueOf(timestamp));
            resources.add(docsToAnalyze.get(document).eResource());
        }
        RequirementDifferenceCalculator calculator = new RequirementDifferenceCalculator(docsToAnalyze, false, null);
        new MergeImpactProcessor(resources, calculator).processImpact();
        
    }
    
    public static void onMerge(Document document) {
        long timestamp = document.eResource().getTimeStamp();
        List<Document> upstreams = RequirementUtils.getUpstreamDocuments(document.eResource());
        //if an upstream document exists, update stamp
        if(!upstreams.isEmpty()) {
            updateCreateTimestamp(document, String.valueOf(timestamp), String.valueOf(timestamp));
        }
    }
    
    private static EAnnotation findTimeStampAnnotation(Document document) {
        RequirementProject project = RequirementUtils.getRequirementProject(document.eResource());
        for (EAnnotation annotation : project.getEAnnotations()) {
            if (annotation.getDetails().keySet().contains(getDocumentLastAnalyzedKey(document))) {
                return annotation;
            }
            
        }
        return null;
    }
    
    /**
     * Sets document hash within an annotation. If null, creates annotation beforehand
     * @param document the document to hash
     */
    public static void updateCreateTimestamp(Document document, String lastAnalyzed, String lastUserChecked) {
        
        RequirementProject project = RequirementUtils.getRequirementProject(document.eResource());
        EAnnotation annotation  = findTimeStampAnnotation(document);
        //if no hash-bearing annotation is found, create hash
        if (annotation == null) {
            EcoreFactory factory = EcoreFactory.eINSTANCE;
            annotation = factory.createEAnnotation();
            project.getEAnnotations().add(annotation);
        }
        if(lastAnalyzed != null) {
            annotation.getDetails().put(getDocumentLastAnalyzedKey(document), lastAnalyzed);
        }
        if(lastUserChecked != null) {
            annotation.getDetails().put(getDocumentLastUserChoiceKey(document), lastUserChecked);
        }
    }
    
    /**
     * Calculates String key that will contain the document's hash.
     * @param document the document
     * @return the hash key
     */
    public static String getDocumentLastAnalyzedKey(Document document) {
        return document.getIdent()+LASTANALYZED_SUFFIX;
    }
    
    /**
     * Calculates String key that will contain the document's hash.
     * @param document the document
     * @return the hash key
     */
    public static String getDocumentLastUserChoiceKey(Document document) {
        return document.getIdent()+LASTUSERCHOICE_SUFFIX;
    }
}
