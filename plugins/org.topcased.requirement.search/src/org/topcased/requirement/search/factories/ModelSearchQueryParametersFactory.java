package org.topcased.requirement.search.factories;

import org.eclipse.emf.search.core.factories.IModelSearchQueryParametersFactory;
import org.eclipse.emf.search.core.parameters.AbstractModelSearchQueryParameters;
import org.eclipse.emf.search.core.parameters.IModelSearchQueryParameters;

/**
 * Wraps ModelSearchQueryParameters creation.
 */
public class ModelSearchQueryParametersFactory implements IModelSearchQueryParametersFactory
{
    private static ModelSearchQueryParametersFactory instance;

    public ModelSearchQueryParametersFactory()
    {
    }

    public static ModelSearchQueryParametersFactory getInstance()
    {
        return instance == null ? instance = new ModelSearchQueryParametersFactory() : instance;
    }

    protected final class ModelSearchQueryParameters extends AbstractModelSearchQueryParameters
    {
        public String getModelSearchEngineID()
        {
            return "org.topcased.requirement.search.RequirementSearchEngine"; //$NON-NLS-1$
        }
    }

    public IModelSearchQueryParameters createSearchQueryParameters()
    {
        return new ModelSearchQueryParameters();
    }
}
