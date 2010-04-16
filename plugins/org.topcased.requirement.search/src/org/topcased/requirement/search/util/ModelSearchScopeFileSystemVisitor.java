package org.topcased.requirement.search.util;

import java.io.File;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.search.core.scope.IModelSearchScope;
import org.eclipse.emf.search.ecore.common.utils.file.EcoreModelSearchScopeFileSystemVisitor;

public class ModelSearchScopeFileSystemVisitor extends EcoreModelSearchScopeFileSystemVisitor
{

    public ModelSearchScopeFileSystemVisitor(IModelSearchScope<Object, Resource> scope)
    {
        super(scope);
    }

    protected boolean isParticipantCurrentSearchEngineValid(File f)
    {
        if (f instanceof File && f.canRead() && f.exists() && !f.isHidden())
        {

            if (f.getName().endsWith(".requirement")) { //$NON-NLS-1$
                return true;
            }

        }
        return false;
    }
}
