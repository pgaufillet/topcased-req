package org.topcased.requirement.search.ui.factories;

import org.eclipse.emf.search.ui.areas.IModelSearchArea;
import org.eclipse.emf.search.ui.areas.IModelSearchAreaFactory;
import org.eclipse.emf.search.ui.pages.AbstractModelSearchPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.topcased.requirement.search.ui.areas.ModelSearchParticipantArea;


/**
 * Wrapper class which create a specific participant area into the model org.topcased.requirement.search page.
 */
public class ModelSearchParticipantAreaFactory implements IModelSearchAreaFactory
{
    public IModelSearchArea createArea(Composite parent, AbstractModelSearchPage searchPage)
    {
        return new ModelSearchParticipantArea(parent, searchPage, SWT.NONE);
    }

    public IModelSearchArea createArea(Composite parent, AbstractModelSearchPage searchPage, String nsURI)
    {
        return new ModelSearchParticipantArea(parent, searchPage, SWT.NONE, nsURI);
    }
}
