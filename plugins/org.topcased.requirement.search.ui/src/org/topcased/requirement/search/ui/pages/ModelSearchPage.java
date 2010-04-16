package org.topcased.requirement.search.ui.pages;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.search.core.engine.IModelSearchQuery;
import org.eclipse.emf.search.core.internal.replace.provisional.ITransformation;
import org.eclipse.emf.search.core.internal.replace.provisional.NullModelSearchTransformation;
import org.eclipse.emf.search.core.results.IModelResultEntry;
import org.eclipse.emf.search.ui.pages.AbstractModelSearchPage;
import org.topcased.requirement.search.replace.TextualReplaceTransformation;
import org.topcased.requirement.search.util.TextualFeaturesUtils;


public final class ModelSearchPage extends AbstractModelSearchPage
{

    @Override
    protected String getModelSearchPageID()
    {
        return "org.topcased.requirement.search.ui.pages.RequirementModelSearchPageID"; //$NON-NLS-1$
    }

    public String getOccurenceLabel(IModelResultEntry entry)
    {
        return entry.getSource() instanceof EObject ? TextualFeaturesUtils.instance().getTextFromEStructuralFeatureIfAny((EObject) entry.getSource()) : "ERROR"; //$NON-NLS-1$
    }

    @Override
    public ITransformation<EObject, IModelSearchQuery, String, String> getTransformation(EObject element, IModelSearchQuery query, String value)
    {
        return TextualFeaturesUtils.instance().getTextFromEStructuralFeatureIfAny(element) != null ? new TextualReplaceTransformation((EObject) element, query, value)
                : new NullModelSearchTransformation();
    }
}
