package org.topcased.requirement.search.util;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.search.core.scope.IModelSearchScope;
import org.eclipse.emf.search.ecore.common.utils.file.EcoreModelSearchDirectoryScopeFactory;
import org.eclipse.emf.search.ecore.common.utils.file.EcoreModelSearchScopeFileSystemVisitor;

public class ModelSearchDirectoryScopeFactory extends EcoreModelSearchDirectoryScopeFactory
{

    // shared ModelSearchScopeFactory instance
    private static ModelSearchDirectoryScopeFactory instance;

    /**
     * Singleton access to the ModelSearchDirectoryScopeFactory instance.
     * 
     * @return New ModelSearchDirectoryScopeFactory instance or previously created one
     */
    public static ModelSearchDirectoryScopeFactory getInstance()
    {
        return instance == null ? instance = new ModelSearchDirectoryScopeFactory() : instance;
    }

    @Override
    protected EcoreModelSearchScopeFileSystemVisitor getModelSearchFileSystemVisitor(IModelSearchScope<Object, Resource> scope)
    {
        return new ModelSearchScopeFileSystemVisitor(scope);
    }
}
