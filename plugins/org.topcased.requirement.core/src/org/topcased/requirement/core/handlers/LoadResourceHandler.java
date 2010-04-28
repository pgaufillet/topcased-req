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
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.dialogs.LoadResourceDialog;

/**
 * An action to load a resource into an editing domain's resource set.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class LoadResourceHandler extends AbstractHandler
{

    /**
     * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
     * 
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        EditingDomain domain = Utils.getCurrentModeler().getEditingDomain();
        LoadResourceDialog loadResourceDialog = new LoadResourceDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), domain);

        loadResourceDialog.open();

        if (loadResourceDialog.getReturnCode() == Dialog.OK)
        {
            URI uri = null;
            String res = loadResourceDialog.getModelFile();
            if (loadResourceDialog.isPlatform())
            {
                uri = URI.createPlatformResourceURI(res, false);
            }
            else
            {
                uri = URI.createFileURI(res);
            }
            domain.getResourceSet().getResource(uri, true);
        }
        return null;
    }
}
