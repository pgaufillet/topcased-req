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

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.topcased.requirement.core.views.current.model.CurrentRequirementReference;

/**
 * Command to load a Requirement reference. This Command is for now neither used in TOPCASED or PAPYRUS? It's aim is to
 * be available for any editor with non transactional editing domain
 * 
 * @author Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 */
public class LoadRequirementReference extends AbstractCommand
{
    /**
     * All the currents references to load
     */
    private List<CurrentRequirementReference> currentReferences;

    /**
     * Register all the URI which has been loaded UseFull if REDO and UNDO are implemented
     */
    private List<URI> errorURI;
    /**
     * All the loaded resources
     */
    private ArrayList<Object> result;
    /**
     * Constructor.
     * @param currentReferences {@link List} of all {@link CurrentRequirementReference} to load
     */
    public LoadRequirementReference(List<CurrentRequirementReference> currentReferences)
    {
        super();
        this.currentReferences = currentReferences;
    }
    /**
     * Implementation of the Command method.
     */
    public void execute()
    {
        result = new ArrayList<Object>();
        errorURI = new ArrayList<URI>();

        for (CurrentRequirementReference currentRef : currentReferences)
        {
            if (!currentRef.isResourceLoaded())
            {
                ResourceSet resourceSet = (ResourceSet) currentRef.getParentReference().getAdapter(ResourceSet.class);
                if (resourceSet != null)
                {
                    Resource res = resourceSet.getResource(currentRef.getUri(), true);
                    if (res != null)
                    {
                        result.add(res);
                    }
                    else
                    {
                        errorURI.add(currentRef.getUri());
                    }
                }
            }
        }
    }
    /**
     * This method is not supported
     */
    public void redo()
    {
        throw new UnsupportedOperationException("REDO operation is not supported.\n Loaded resources : " + result + " Ressource not loaded : " + errorURI);
    }
    /**
     * This method is not supported
     */
    @Override
    public void undo()
    {
        throw new UnsupportedOperationException("UNDO operation is not supported.\n Loaded resources : " + result + " Ressource not loaded : " + errorURI);
    }
}
