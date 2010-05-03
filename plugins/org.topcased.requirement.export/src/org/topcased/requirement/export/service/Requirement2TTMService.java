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
import org.topcased.requirement.export.internal.Messages;
import org.topcased.requirement.export.internal.RequirementExportPlugin;

import ttm.TtmPackage;

/**
 * Defines the Tramway to Requirements import service.
 * 
 * @author <a href="mailto:christophe.le-camus@c-s.fr">Christophe LE CAMUS</a>
 */
public class Requirement2TTMService implements IService
{
    // Constant defining namespace
    private static final String TRANSFO_ID = "org.topcased.requirement.transformation.export"; //$NON-NLS-1$
    
    private static final String TTM_MODEL_ID = "http://org.topcased.traceability.model"; //$NON-NLS-1$
    
    private static final String CHANGE_URI_ID = "org.topcased.service.changeuri"; //$NON-NLS-1$
    
    //parameters
    private static final String inParam = "IN"; //$NON-NLS-1$
    
    private static final String outParam = "OUT"; //$NON-NLS-1$
    
    private static final String pathParam = "Path"; //$NON-NLS-1$
    
    private static final String tmpFileExtension = "tmp"; //$NON-NLS-1$
    
    private static final String ttmFileExtension = "ttm"; //$NON-NLS-1$
    
    //time format
    private static final String timeFormat = "yyyy-MM-dd hh'h'mm"; //$NON-NLS-1$
    
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

            String beginTime = new SimpleDateFormat(timeFormat).format(new Date());
            RequirementExportPlugin.log(Messages.getString("Requirement2TTMService.0").concat(beginTime), IStatus.INFO); //$NON-NLS-1$

            String inpath = (String) parameters.get(inParam);
            IPath outpath = new Path((String) parameters.get(outParam));
            outpath = outpath.addFileExtension(tmpFileExtension);
            IPath path = (IPath) parameters.get(pathParam);

            HashMap<String, String> models = new HashMap<String, String>();
            models.put(inParam, inpath);
            models.put(outParam, outpath.toOSString());

            HashMap<String, Object> serviceParameters = new HashMap<String, Object>();
            serviceParameters.put("Id", TRANSFO_ID); //$NON-NLS-1$
            serviceParameters.put("Models", models); //$NON-NLS-1$
            serviceParameters.put("ProjectPath", path); //$NON-NLS-1$
            serviceParameters.put("IsXml", new Boolean(false)); //$NON-NLS-1$

            IService transformation = ServicesManager.getInstance().getService(IServiceCst.TRANSFORMATION);
            transformation.serviceRun(serviceParameters);

            IService readService = ServicesManager.getInstance().getService(IServiceCst.READ_XMI_EMF);
            serviceParameters = new HashMap<String, Object>();
            serviceParameters.put("XMIFile", path.append(outpath.lastSegment()).toString()); //$NON-NLS-1$
            result = readService.serviceRun(serviceParameters);

            namespaces.put(TtmPackage.eNAME, TTM_MODEL_ID);

            // Prepare and call the service aimed to change URIs
            IService changeUriService = ServicesManager.getInstance().getService(CHANGE_URI_ID);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("InFilePath", path.append(((String) parameters.get(outParam)).concat(".").concat(tmpFileExtension)).toString()); //$NON-NLS-1$ //$NON-NLS-2$
            IPath outfinalpath = new Path((String) parameters.get(outParam));
            outfinalpath = outfinalpath.removeFileExtension().addFileExtension(ttmFileExtension);
            params.put("OutFilePath", path.append(outfinalpath.toOSString()).toString()); //$NON-NLS-1$
            params.put("NSURI", namespaces); //$NON-NLS-1$

            changeUriService.serviceRun(params);

            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IResource inResourceProject = root.findMember(path);
            IResource inResourceFile = root.findMember(new Path((String) params.get("InFilePath"))); //$NON-NLS-1$

            try
            {
                inResourceFile.delete(true, null);
                inResourceProject.refreshLocal(1, null);
            }
            catch (CoreException e)
            {
                RequirementExportPlugin.log(e);
            }

            String endTime = new SimpleDateFormat(timeFormat).format(new Date());
            RequirementExportPlugin.log(Messages.getString("Requirement2TTMService.1").concat(endTime), IStatus.INFO); //$NON-NLS-1$
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
        if (!parameters.containsKey(inParam))
        {
            RequirementExportPlugin.log(Messages.getString("Requirement2TTMService.3"), IStatus.ERROR); //$NON-NLS-1$
            return false;
        }

        if (!parameters.containsKey(outParam))
        {
            RequirementExportPlugin.log(Messages.getString("Requirement2TTMService.4"), IStatus.ERROR); //$NON-NLS-1$
            return false;
        }

        if (!parameters.containsKey(pathParam))
        {
            RequirementExportPlugin.log(Messages.getString("Requirement2TTMService.5"), IStatus.ERROR); //$NON-NLS-1$
            return false;
        }

        if (!(parameters.get(inParam) instanceof String))
        {
            RequirementExportPlugin.log(Messages.getString("Requirement2TTMService.6"), IStatus.ERROR); //$NON-NLS-1$
            return false;
        }

        if (!(parameters.get(outParam) instanceof String))
        {
            RequirementExportPlugin.log(Messages.getString("Requirement2TTMService.7"), IStatus.ERROR); //$NON-NLS-1$
            return false;
        }

        if (!(parameters.get(pathParam) instanceof IPath))
        {
            RequirementExportPlugin.log(Messages.getString("Requirement2TTMService.8"), IStatus.ERROR); //$NON-NLS-1$
            return false;
        }
        return true;
    }

}
