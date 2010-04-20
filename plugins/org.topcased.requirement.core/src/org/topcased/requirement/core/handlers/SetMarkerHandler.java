/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/
package org.topcased.requirement.core.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationContext;
import org.topcased.requirement.core.views.AddRequirementMarker;

/**
 * This handler set marker position for the creation of a new requirement
 * {@link org.topcased.requirement.AnonymousRequirement} and {@link org.topcased.requirement.CurrentRequirement}
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class SetMarkerHandler extends AbstractHandler
{
    private Integer type;

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        // Get the command type
        String cmdType = event.getParameter(ICommandConstants.SET_MARKER_TYPE_PARAM);
        type = Integer.valueOf(cmdType);

        if (((EvaluationContext) event.getApplicationContext()).getDefaultVariable() instanceof List< ? >)
        {
            // Get the current selection
            List< ? > elements = ((List< ? >) ((EvaluationContext) event.getApplicationContext()).getDefaultVariable());

            AddRequirementMarker.eINSTANCE.setPosition(type);
            if (type == AddRequirementMarker.eINSTANCE.pOS)
            {
                AddRequirementMarker.eINSTANCE.setMarkedObject(elements.get(0));
            }
        }
        return null;
    }
}
