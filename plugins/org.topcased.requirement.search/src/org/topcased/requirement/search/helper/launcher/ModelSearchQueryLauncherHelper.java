package org.topcased.requirement.search.helper.launcher;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.search.core.engine.IModelSearchQuery;
import org.eclipse.emf.search.core.results.IModelSearchResult;
import org.eclipse.emf.search.core.scope.IModelSearchScope;
import org.eclipse.emf.search.ecore.helper.launcher.EcoreModelSearchQueryLauncherHelper;
import org.eclipse.emf.search.ecore.regex.ModelSearchQueryTextualExpressionEnum;

public class ModelSearchQueryLauncherHelper extends EcoreModelSearchQueryLauncherHelper

{

    private static ModelSearchQueryLauncherHelper instance;

    // Singleton
    public static ModelSearchQueryLauncherHelper getInstance()
    {
        return instance == null ? instance = new ModelSearchQueryLauncherHelper() : instance;
    }

    //
    // NORMAL TEXT
    //

    public IModelSearchResult launchGlobalTextualrequirementModelSearchQuery(String pattern, IModelSearchScope<Object, Resource> scope)
    {
        IModelSearchQuery q = buildTextualModelSearchQuery(pattern, org.topcased.requirement.RequirementPackage.eINSTANCE.getEClassifiers(), ModelSearchQueryTextualExpressionEnum.NORMAL_TEXT, scope,
                "http://org.topcased.requirement/1.0" //$NON-NLS-1$
        );
        q.run(new NullProgressMonitor());
        return q.getModelSearchResult();
    }

    //
    // REGEX
    //

    public IModelSearchResult launchGlobalRegexrequirementModelSearchQuery(String pattern, IModelSearchScope<Object, Resource> scope)
    {
        IModelSearchQuery q = buildTextualModelSearchQuery(pattern, org.topcased.requirement.RequirementPackage.eINSTANCE.getEClassifiers(), ModelSearchQueryTextualExpressionEnum.REGULAR_EXPRESSION,
                scope, "http://org.topcased.requirement/1.0" //$NON-NLS-1$
        );
        q.run(new NullProgressMonitor());
        return q.getModelSearchResult();
    }

    //
    // CASE SENSITIVE
    //

    public IModelSearchResult launchGlobalCaseSensitiverequirementModelSearchQuery(String pattern, IModelSearchScope<Object, Resource> scope)
    {
        IModelSearchQuery q = buildTextualModelSearchQuery(pattern, org.topcased.requirement.RequirementPackage.eINSTANCE.getEClassifiers(), ModelSearchQueryTextualExpressionEnum.CASE_SENSITIVE,
                scope, "http://org.topcased.requirement/1.0" //$NON-NLS-1$
        );
        q.run(new NullProgressMonitor());
        return q.getModelSearchResult();
    }

}
