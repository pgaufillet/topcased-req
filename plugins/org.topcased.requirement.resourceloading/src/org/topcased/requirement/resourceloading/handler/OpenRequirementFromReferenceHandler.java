/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Olivier Mélois <a href="mailto:olivier.melois@atos.net">olivier.melois@atos.net</a>"
 * 
 **********************************************************************************************************************/

package org.topcased.requirement.resourceloading.handler;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.topcased.requirement.resourceloading.commands.LoadRequirementReference;
import org.topcased.requirement.resourceloading.commands.OpenRequirementFromReferenceTransactionalCommand;
import org.topcased.requirement.resourceloading.wrapper.GMFtoEMFCommandWrapper;

/**
 * Implementation of {@link AbstractCurrentRequirementPageHandler} in order to handle differently {@link TransactionalEditingDomain} and simple {@link EditingDomain}
 * @author omelois
 *
 */
public class OpenRequirementFromReferenceHandler extends AbstractCurrentRequirementPageHandler
{

    @Override
    protected Command getCommand()
    {
        return new LoadRequirementReference(getSelectedCurrentRequirements());
    }

    @Override
    protected Command getTransactionnalCommand(TransactionalEditingDomain editingDomain)
    {
        return new GMFtoEMFCommandWrapper(new OpenRequirementFromReferenceTransactionalCommand(editingDomain, "Open Requirement from selected element", null, getSelectedCurrentRequirements()));
    }
    
    @Override
    protected String getEditingDomainExceptionLabel()
    {
        return "Unable to find and editing domain to execute Open Requirement From Reference command";
    }

    @Override
    protected  String getCommandStackException()
    {
        return "Unable to get a command stact to execute Open Requirement From Reference resource command";
    }

    
}