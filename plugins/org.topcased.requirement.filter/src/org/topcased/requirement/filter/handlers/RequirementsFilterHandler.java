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
package org.topcased.requirement.filter.handlers;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.topcased.requirement.filter.ui.RequirementFilterWizard;

/**
 * This handler open the requirement filter wizard
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class RequirementsFilterHandler extends AbstractHandler
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
            Collection<IPath> ipathes = new LinkedList<IPath>();
            for (Object element : elements)
            {
                if (element instanceof IFile)
                {
                    IFile file = (IFile) element;
                    ipathes.add(file.getFullPath());
                }
            }
            WizardDialog wd = new WizardDialog(Display.getCurrent().getActiveShell(), new RequirementFilterWizard(ipathes.toArray(new IPath[] {})));
            wd.open();
        }
        return null;
    }
}
