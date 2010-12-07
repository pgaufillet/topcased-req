/***********************************************************************************************************************
 * Copyright (c) 2010 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.handlers;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.window.Window;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.dialogs.DocumentSelectionDialog;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.util.RequirementResource;

import ttm.TtmPackage;

/**
 * Handler for importing a set of Upstream Documents into current requirement model.<br>
 * The Upstream View is refreshed consequently to display the new elements.<br>
 * 
 * Creation : 06 december 2010<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since TOPCASED 4.3.0
 */
public class ImportUpstreamDocumentHandler extends AbstractHandler
{
    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        DocumentSelectionDialog dialog = new DocumentSelectionDialog();
        Collection<Object> result = Collections.emptyList();
        if (dialog.open() == Window.OK)
        {
            EditingDomain domain = Utils.getCurrentModeler().getEditingDomain();
            RequirementResource reqRsc = RequirementUtils.getRequirementModel(domain);
            UpstreamModel upstreamModel = RequirementUtils.getUpstreamModel(reqRsc);

            AbstractCommand cmd = (AbstractCommand) AddCommand.create(domain, upstreamModel, TtmPackage.Literals.PROJECT__DOCUMENTS, dialog.getCheckedDocuments());
            cmd.setLabel(Messages.getString("ImportUpstreamDocumentHandler.cmdLabel")); //$NON-NLS-1$
            domain.getCommandStack().execute(cmd);
        }
        return result;
    }

}
