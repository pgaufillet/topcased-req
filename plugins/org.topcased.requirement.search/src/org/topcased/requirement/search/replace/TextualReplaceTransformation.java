package org.topcased.requirement.search.replace;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.search.core.engine.IModelSearchQuery;
import org.eclipse.emf.search.core.internal.replace.provisional.AbstractModelSearchTransformation;
import org.topcased.requirement.search.engine.ModelSearchQuery;
import org.topcased.requirement.search.util.TextualFeaturesUtils;


public class TextualReplaceTransformation extends AbstractModelSearchTransformation<EObject, IModelSearchQuery, String, String>
{

    public TextualReplaceTransformation(EObject element, IModelSearchQuery query, String valuation)
    {
        super(element, query, valuation);
    }

    public boolean isValid()
    {
        return getModifiedElement() instanceof EObject;
    }

    public IStatus perform()
    {
        try
        {
            if (getQuery() instanceof ModelSearchQuery)
            {
                ModelSearchQuery query = (ModelSearchQuery) getQuery();

                String input = TextualFeaturesUtils.instance().getTextFromEStructuralFeatureIfAny(getModifiedElement());
                String expr = getQuery().getQueryExpression();

                if (input != null)
                {
                    EObject eObj = getModifiedElement();
                    if (TextualFeaturesUtils.instance().getTextFromEStructuralFeatureIfAny(eObj) != null)
                    {
                        for (ETypedElement elem : TextualFeaturesUtils.instance().getOwnedETypedElementsFromEObject(eObj))
                        {
                            String curValue = TextualFeaturesUtils.instance().getTextFromETypedElement(eObj, elem);

                            switch (query.getKind())
                            {
                                case REGULAR_EXPRESSION:
                                    Pattern pattern = Pattern.compile(expr);

                                    String newValue = pattern.matcher(curValue).replaceFirst(getValuation());
                                    TextualFeaturesUtils.instance().setTextForETypedElement(eObj, elem, newValue);

                                    break;
                                case CASE_SENSITIVE:
                                case NORMAL_TEXT:
                                    TextualFeaturesUtils.instance().setTextForETypedElement(eObj, elem, getValuation());
                                    break;
                            }
                        }
                    }
                }
            }
        }
        catch (Throwable t)
        {
            return Status.CANCEL_STATUS;
        }
        return Status.OK_STATUS;
    }

    @Override
    public String getResult()
    {
        return TextualFeaturesUtils.instance().getTextFromEStructuralFeatureIfAny((EObject) getModifiedElement());
    }
}
