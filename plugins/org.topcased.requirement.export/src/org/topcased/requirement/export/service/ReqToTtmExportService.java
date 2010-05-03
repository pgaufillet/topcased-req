/***********************************************************************************************************************
 * Copyright (c) 2009 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe LE CAMUS (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.export.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.topcased.bus.core.IService;
import org.topcased.bus.core.ServicesManager;
import org.topcased.bus.core.constant.IServiceCst;
import org.topcased.requirement.export.Activator;

import ttm.TtmPackage;

/**
 * Defines the Tramway to Requirements import service.
 * 
 * @author <a href="mailto:christophe.le-camus@c-s.fr">Christophe LE CAMUS</a>
 */
public class ReqToTtmExportService implements IService
{
    /**
     * @see org.topcased.bus.core.IService#serviceRun(java.util.Map)
     */
    public Object serviceRun(Map<String, Object> parameters)
    {
        Object result = null;
        Map<String, Object> namespaces = new HashMap<String, Object>();

        // first checks the parameters
        if (checkServiceParameters(parameters))
        {

            String beginTime = new SimpleDateFormat("yyyy-MM-dd hh'h'mm").format(new Date());
            Activator.log("Entering Requirement to Tramway export service at ".concat(beginTime), IStatus.INFO);

            String inpath = (String) parameters.get("IN");
            IPath outpath = new Path((String) parameters.get("OUT"));
            outpath = outpath.addFileExtension("tmp");
            IPath path = (IPath) parameters.get("Path");

            HashMap<String, String> models = new HashMap<String, String>();
            models.put("IN", inpath);
            models.put("OUT", outpath.toOSString());

            HashMap<String, Object> serviceParameters = new HashMap<String, Object>();
            serviceParameters.put("Id", "org.topcased.export.requirement.transformation");
            serviceParameters.put("Models", models);
            serviceParameters.put("ProjectPath", path);
            serviceParameters.put("IsXml", new Boolean(false));

            IService transformation = ServicesManager.getInstance().getService(IServiceCst.TRANSFORMATION);
            transformation.serviceRun(serviceParameters);

            IService readService = ServicesManager.getInstance().getService(IServiceCst.READ_XMI_EMF);
            serviceParameters = new HashMap<String, Object>();
            serviceParameters.put("XMIFile", path.append(outpath.lastSegment()).toString());
            result = readService.serviceRun(serviceParameters);

            namespaces.put(TtmPackage.eNAME, "http://org.topcased.traceability.model");

            // Prepare and call the service aimed to change URIs
            IService changeUriService = ServicesManager.getInstance().getService("org.topcased.service.changeuri");

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("InFilePath", path.append(((String) parameters.get("OUT")).concat(".tmp")).toString());
            IPath outfinalpath = new Path((String) parameters.get("OUT"));
            outfinalpath = outfinalpath.removeFileExtension().addFileExtension("ttm");
            params.put("OutFilePath", path.append(outfinalpath.toOSString()).toString());
            params.put("NSURI", namespaces);

            changeUriService.serviceRun(params);

            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IResource inResourceProject = root.findMember(path);
            IResource inResourceFile = root.findMember(new Path((String) params.get("InFilePath")));

            try
            {
                inResourceFile.delete(true, null);
                inResourceProject.refreshLocal(1, null);
            }
            catch (CoreException e)
            {
                Activator.log(e);
            }

            String endTime = new SimpleDateFormat("yyyy-MM-dd hh'h'mm").format(new Date());
            Activator.log("Leaving Requirements to Tramway export service at ".concat(endTime), IStatus.INFO);
        }

        return result;
    }

    /**
     * Checks service parameters for this import.
     * 
     * @param parameters A map of required parameters
     */
    private boolean checkServiceParameters(Map<String, Object> parameters)
    {
        if (!parameters.containsKey("IN"))
        {
            Activator.log("The parameter IN must be set.", IStatus.ERROR);
            return false;
        }

        if (!parameters.containsKey("OUT"))
        {
            Activator.log("The parameter OUT must be set.", IStatus.ERROR);
            return false;
        }

        if (!parameters.containsKey("Path"))
        {
            Activator.log("The parameter Path must be set.", IStatus.ERROR);
            return false;
        }

        if (!(parameters.get("IN") instanceof String))
        {
            Activator.log("The parameter IN must be a String.", IStatus.ERROR);
            return false;
        }

        if (!(parameters.get("OUT") instanceof String))
        {
            Activator.log("The parameter OUT must be a String.", IStatus.ERROR);
            return false;
        }

        if (!(parameters.get("Path") instanceof IPath))
        {
            Activator.log("The parameter Path must be a IPath.", IStatus.ERROR);
            return false;
        }
        return true;
    }

}
