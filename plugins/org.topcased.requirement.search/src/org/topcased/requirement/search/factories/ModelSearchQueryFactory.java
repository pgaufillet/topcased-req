package org.topcased.requirement.search.factories;

import org.eclipse.emf.search.core.engine.AbstractModelSearchQuery;
import org.eclipse.emf.search.core.factories.IModelSearchQueryFactory;
import org.eclipse.emf.search.core.parameters.IModelSearchQueryParameters;
import org.topcased.requirement.search.engine.ModelSearchQuery;


/**
 * Wraps ModelSearchQuery creation.
 */
public class ModelSearchQueryFactory implements IModelSearchQueryFactory
{
    private static ModelSearchQueryFactory instance;

    public ModelSearchQueryFactory()
    {
    }

    public static ModelSearchQueryFactory getInstance()
    {
        return instance == null ? instance = new ModelSearchQueryFactory() : instance;
    }

    public AbstractModelSearchQuery createModelSearchQuery(String expr, IModelSearchQueryParameters p)
    {
        return new ModelSearchQuery(expr, p);
    }
}
