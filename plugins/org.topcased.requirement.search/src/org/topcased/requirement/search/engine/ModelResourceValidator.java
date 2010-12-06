package org.topcased.requirement.search.engine;

import org.eclipse.emf.search.core.resource.AbstractModelResourceValidator;
import org.topcased.requirement.util.RequirementResource;

/**
 * Allows users to describe all the specific query org.topcased.requirement.search supported model editor extensions.
 */
public class ModelResourceValidator extends AbstractModelResourceValidator
{
    public ModelResourceValidator()
    {
        addModelFileExtension(RequirementResource.FILE_EXTENSION);
    }
}
