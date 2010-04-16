package org.topcased.requirement.search.helper.builder;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.search.core.engine.IModelSearchQuery;
import org.eclipse.emf.search.core.parameters.IModelSearchQueryParameters;
import org.eclipse.emf.search.core.scope.IModelSearchScope;
import org.eclipse.emf.search.ecore.helper.builder.AbstractEcoreTextualModelSearchQueryBuilderHelper;
import org.eclipse.emf.search.ecore.regex.ModelSearchQueryTextualExpressionEnum;
import org.topcased.requirement.search.evaluators.ModelTextualModelSearchQueryEvaluator;
import org.topcased.requirement.search.factories.ModelSearchQueryFactory;
import org.topcased.requirement.search.factories.ModelSearchQueryParametersFactory;


public abstract class AbstractTextualModelSearchQueryBuilderHelper extends AbstractEcoreTextualModelSearchQueryBuilderHelper
{

    protected IModelSearchQueryParameters createParameters(IModelSearchScope<Object, Resource> scope, List< ? extends Object> participants, ModelSearchQueryTextualExpressionEnum textualExpression)
    {

        IModelSearchQueryParameters parameters = ModelSearchQueryParametersFactory.getInstance().createSearchQueryParameters();

        parameters.setEvaluator(new ModelTextualModelSearchQueryEvaluator<IModelSearchQuery, Object>());
        parameters.setParticipantElements(participants);
        parameters.setScope(scope);

        initTextualQueryParametersFromPatternKind(parameters, textualExpression);

        return parameters;
    }

    protected IModelSearchQuery createQuery(String pattern, IModelSearchQueryParameters parameters)
    {

        return ModelSearchQueryFactory.getInstance().createModelSearchQuery(pattern, parameters);
    }
}
