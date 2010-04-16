package org.topcased.requirement.search.helper.builder;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.search.core.engine.IModelSearchQuery;
import org.eclipse.emf.search.core.parameters.IModelSearchQueryParameters;
import org.eclipse.emf.search.core.scope.IModelSearchScope;
import org.eclipse.emf.search.ecore.regex.ModelSearchQueryTextualExpressionEnum;
import org.topcased.requirement.search.evaluators.ModelTextualModelSearchQueryEvaluator;
import org.topcased.requirement.search.factories.ModelSearchQueryFactory;
import org.topcased.requirement.search.factories.ModelSearchQueryParametersFactory;


public class ModelSearchQueryBuilderHelper extends AbstractTextualModelSearchQueryBuilderHelper

{

    private static ModelSearchQueryBuilderHelper instance;

    // Singleton
    public static ModelSearchQueryBuilderHelper getInstance()
    {
        return instance == null ? instance = new ModelSearchQueryBuilderHelper() : instance;
    }

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

    public IModelSearchQuery buildGlobalTextualrequirementModelSearchQuery(String expr, IModelSearchScope<Object, Resource> scope)
    {
        return buildTextualModelSearchQuery(expr, org.topcased.requirement.RequirementPackage.eINSTANCE.getEClassifiers(), ModelSearchQueryTextualExpressionEnum.NORMAL_TEXT, scope,
                "http://org.topcased.requirement/1.0" //$NON-NLS-1$
        );
    }

    public IModelSearchQuery buildGlobalRegexrequirementModelSearchQuery(String expr, IModelSearchScope<Object, Resource> scope)
    {
        return buildTextualModelSearchQuery(expr, org.topcased.requirement.RequirementPackage.eINSTANCE.getEClassifiers(), ModelSearchQueryTextualExpressionEnum.REGULAR_EXPRESSION, scope,
                "http://org.topcased.requirement/1.0" //$NON-NLS-1$
        );
    }

    public IModelSearchQuery buildGlobalCaseSensitiverequirementModelSearchQuery(String expr, IModelSearchScope<Object, Resource> scope)
    {
        return buildTextualModelSearchQuery(expr, org.topcased.requirement.RequirementPackage.eINSTANCE.getEClassifiers(), ModelSearchQueryTextualExpressionEnum.CASE_SENSITIVE, scope,
                "http://org.topcased.requirement/1.0" //$NON-NLS-1$
        );
    }

    public IModelSearchQuery buildGlobalTextualModelSearchQuery(String pattern, IModelSearchScope<Object, Resource> scope, String nsURI)
    {

        if (nsURI.equals("http://org.topcased.requirement/1.0")) { //$NON-NLS-1$
            return buildGlobalTextualrequirementModelSearchQuery(pattern, scope);
        }

        return null;
    }

    public IModelSearchQuery buildGlobalRegexModelSearchQuery(String pattern, IModelSearchScope<Object, Resource> scope, String nsURI)
    {

        if (nsURI.equals("http://org.topcased.requirement/1.0")) { //$NON-NLS-1$
            return buildGlobalRegexrequirementModelSearchQuery(pattern, scope);
        }

        return null;
    }

    public IModelSearchQuery buildGlobalCaseSensitiveModelSearchQuery(String pattern, IModelSearchScope<Object, Resource> scope, String nsURI)
    {

        if (nsURI.equals("http://org.topcased.requirement/1.0")) { //$NON-NLS-1$
            return buildGlobalCaseSensitiverequirementModelSearchQuery(pattern, scope);
        }

        return null;
    }
}
