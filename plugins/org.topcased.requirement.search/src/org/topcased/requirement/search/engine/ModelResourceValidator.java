package org.topcased.requirement.search.engine;

import org.eclipse.emf.search.core.resource.AbstractModelResourceValidator;

/**
 * Allows users to describe all the specific query org.topcased.requirement.search supported model editor extensions.
 */
public class ModelResourceValidator extends AbstractModelResourceValidator
{
    public ModelResourceValidator()
    {

        addModelFileExtension("requirement"); //$NON-NLS-1$

    }
}
