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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.CommandStack;
import org.topcased.modeler.editor.Modeler;
import org.topcased.modeler.utils.Utils;

/**
 * This handler perform a very simple redo only for the key binding CTRL+Y.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class RedoHandler extends AbstractHandler
{

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        Modeler modeler = Utils.getCurrentModeler();
        if (modeler != null)
        {
            CommandStack stack = modeler.getEditingDomain().getCommandStack();
            if (stack.getRedoCommand() != null)
            {
                stack.redo();
            }
        }
        return null;
    }
   
}
