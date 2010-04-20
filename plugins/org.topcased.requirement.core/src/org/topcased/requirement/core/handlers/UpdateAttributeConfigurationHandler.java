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
import org.eclipse.swt.widgets.Display;
import org.topcased.requirement.core.dialogs.UpdateAttributeConfigurationDialog;

/**
 * This handler allows to update attribute configuration of a requirement model.<br>
 * It has for effect to launch the update attribute configuration dialog.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class UpdateAttributeConfigurationHandler extends AbstractHandler
{

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {        
        if (((EvaluationContext)event.getApplicationContext()).getDefaultVariable() instanceof List<?>)
        {
            //Get the current selection
            List<?> elements = ((List<?>)((EvaluationContext)event.getApplicationContext()).getDefaultVariable());
            
            //Launch the update attribute configuration dialog
            new UpdateAttributeConfigurationDialog(elements, Display.getCurrent().getActiveShell()).open();
        }
        return null;
    }
}
