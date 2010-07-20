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
package org.topcased.requirement.service;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.topcased.bus.core.IService;
import org.topcased.bus.core.ServicesManager;
import org.topcased.requirement.core.extensions.IRequirementTransformation;
import org.topcased.requirement.internal.RequirementImportPlugin;

/**
 * Processes the ATL transformation to create the requirement model from a TTM source model.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class TTM2RequirementTransformation implements IRequirementTransformation
{

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementTransformation#transformation(org.eclipse.core.resources.IFile,
     *      org.eclipse.core.resources.IFile)
     */
    public void transformation(IFile source, IFile dest)
    {

        Map<String, Object> parameters = new HashMap<String, Object>();

        // The absolute path to the TRAMway model
        String inPath = source.getLocation().toOSString();

        // The requirement destination model
        String outPath = dest.getName();

        // The target destination path
        IResource targetContainer = dest.getParent();

        parameters.put("IN", inPath); //$NON-NLS-1$
        parameters.put("OUT", outPath); //$NON-NLS-1$
        parameters.put("Path", targetContainer.getFullPath()); //$NON-NLS-1$

        IService service = ServicesManager.getInstance().getService(TTM2RequirementService.ID);
        service.serviceRun(parameters);

        try
        {
            targetContainer.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
        }
        catch (CoreException e)
        {
            RequirementImportPlugin.log(e);
        }
    }

}
