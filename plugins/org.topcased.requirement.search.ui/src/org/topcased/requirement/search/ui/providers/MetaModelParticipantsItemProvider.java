package org.topcased.requirement.search.ui.providers;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.search.ui.providers.AbstractMetaModelParticipantsItemProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import org.topcased.requirement.RequirementPackage;

public class MetaModelParticipantsItemProvider extends AbstractMetaModelParticipantsItemProvider
{
    private Collection<String> nsURIs;

    public MetaModelParticipantsItemProvider(Collection<String> nsURIs)
    {
        this.nsURIs = nsURIs;
    }

    public Object[] getElements(Object inputElement)
    {
        List<EClassifier> eClassifierList = new ArrayList<EClassifier>();

        if (nsURIs.contains(RequirementPackage.eNS_URI))
        {
            eClassifierList.addAll(RequirementPackage.eINSTANCE.getEClassifiers());
        }

        return eClassifierList.toArray();
    }
}
