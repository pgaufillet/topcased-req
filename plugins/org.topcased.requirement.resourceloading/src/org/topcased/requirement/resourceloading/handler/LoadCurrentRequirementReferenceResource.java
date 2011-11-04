/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.resourceloading.handler;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.topcased.requirement.resourceloading.commands.LoadRequirementReference;
import org.topcased.requirement.resourceloading.commands.LoadRessourceTransactionalCommand;
import org.topcased.requirement.resourceloading.wrapper.GMFtoEMFCommandWrapper;
/**
 * Implementation of {@link AbstractCurrentRequirementPageHandler} in order to handle differently {@link TransactionalEditingDomain} and simple {@link EditingDomain}
 * @author Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 *
 */
public class LoadCurrentRequirementReferenceResource extends AbstractCurrentRequirementPageHandler
{

    @Override
    protected Command getCommand()
    {
        return new LoadRequirementReference(getSelectedCurrentRequirements());
    }

    @Override
    protected Command getTransactionnalCommand(TransactionalEditingDomain editingDomain)
    {
        return new GMFtoEMFCommandWrapper(new LoadRessourceTransactionalCommand(editingDomain, "Load Ressource of selected elements", null, getSelectedCurrentRequirements()));
    }

}
