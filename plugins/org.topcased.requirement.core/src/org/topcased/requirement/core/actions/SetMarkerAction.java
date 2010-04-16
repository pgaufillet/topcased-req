/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.topcased.requirement.core.views.AddRequirementMarker;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * The action set marker position for the creation of a new requirement
 * {@link org.topcased.requirement.AnonymousRequirement} and {@link org.topcased.requirement.CurrentRequirement}
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class SetMarkerAction extends RequirementAction
{

    private Integer type;

    /**
     * Constructor
     * 
     * @param selection The selection done
     * @param page The current page
     * @param aType The action type
     */
    public SetMarkerAction(IStructuredSelection selection, CurrentPage page, Integer aType)
    {
        super(selection, page.getEditingDomain());
        type = aType;
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run()
    {
        AddRequirementMarker.eINSTANCE.setPosition(type);
        if (type == AddRequirementMarker.eINSTANCE.pOS)
        {
            AddRequirementMarker.eINSTANCE.setMarkedObject(selection.getFirstElement());
        }
    }

}
