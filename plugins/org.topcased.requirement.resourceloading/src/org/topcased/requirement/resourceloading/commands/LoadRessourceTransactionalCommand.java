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
package org.topcased.requirement.resourceloading.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.topcased.requirement.core.views.current.model.CurrentRequirementReference;
import org.topcased.requirement.core.views.current.model.CurrentRequirementReference.URIState;
import org.topcased.requirement.resourceloading.Activator;

/**
 * Transactionnal command to load a Current Requirement Referece
 * 
 * @author Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * 
 */
public class LoadRessourceTransactionalCommand extends AbstractTransactionalCommand
{
    /**
     * List of all {@link CurrentRequirementReference} to load
     */
    private List<CurrentRequirementReference> currentReferences;

    /**
     * Constructor
     * 
     * @param domain {@link TransactionalEditingDomain}
     * @param label Label of the command
     * @param affectedFiles
     * @param currentReferences List of all {@link CurrentRequirementReference} to load
     */
    public LoadRessourceTransactionalCommand(TransactionalEditingDomain domain, String label, List<?> affectedFiles, List<CurrentRequirementReference> currentReferences)
    {
        super(domain, label, affectedFiles);
        this.currentReferences = currentReferences;
    }

    @Override
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
    {
        List<Object> result = new ArrayList<Object>();
        boolean error = false;
        List<URI> errorURI = new ArrayList<URI>();
        /**
         * Load the resource
         */
        for (CurrentRequirementReference currentRef : currentReferences)
        {
            if (!currentRef.isResourceLoaded())
            {
                ResourceSet resourceSet = (ResourceSet) currentRef.getParentReference().getAdapter(ResourceSet.class);
                if (resourceSet != null)
                {
                    Resource res = null;
                    try
                    {
                        res = resourceSet.getResource(currentRef.getUri(), true);
                        if (res != null)
                        {
                            currentRef.setUriState(URIState.OK);
                            result.add(res);
                        }
                        else
                        {
                            error = true;
                            errorURI.add(currentRef.getUri());
                            currentRef.setUriState(URIState.KO);
                        }
                    }
                    catch (RuntimeException e)
                    {
                        currentRef.setUriState(URIState.KO);
                        error = true;
                        errorURI.add(currentRef.getUri());
                        Activator.log(e);
                    }

                }
            }
        }
        /**
         * Check if everything went well
         */
        if (error)
        {
            /** Refresh the viewer */
            currentReferences.get(0).getParentReference().getNotifier().notifyChanged(new ViewerNotification(new NotificationImpl(ViewerNotification.SET, URIState.UNKOWN, URIState.KO)));
            return CommandResult.newErrorCommandResult("Unable to load the following URI : " + errorURI);
        }
        return CommandResult.newOKCommandResult(result);
    }

}
