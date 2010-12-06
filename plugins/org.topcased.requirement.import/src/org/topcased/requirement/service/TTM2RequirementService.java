/***********************************************************************************************************************
 * Copyright (c) 2008,2010 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Christophe LE CAMUS (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.topcased.bus.core.IService;
import org.topcased.bus.core.ServicesManager;
import org.topcased.bus.core.constant.IServiceCst;
import org.topcased.requirement.internal.Messages;
import org.topcased.requirement.internal.RequirementImportPlugin;
import org.topcased.requirement.util.RequirementResource;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import ttm.TtmPackage;

/**
 * Defines the Tramway to Requirements import service.
 * 
 * @author <a href="mailto:christophe.le-camus@c-s.fr">Christophe LE CAMUS</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class TTM2RequirementService implements IService
{
    // Constant defining namespace
    public static final String ID = "org.topcased.requirement.service.import"; //$NON-NLS-1$

    private static final String TRANSFO_ID = "org.topcased.requirement.transformation.import"; //$NON-NLS-1$

    private static final String CHANGE_URI_ID = "org.topcased.service.changeuri"; //$NON-NLS-1$

    // parameters
    private static final String inParam = "IN"; //$NON-NLS-1$

    private static final String outParam = "OUT"; //$NON-NLS-1$

    private static final String pathParam = "Path"; //$NON-NLS-1$

    // time format
    private static final String timeFormat = "yyyy-MM-dd hh'h'mm"; //$NON-NLS-1$

    /**
     * @see org.topcased.bus.core.IService#serviceRun(java.util.Map)
     */
    public Object serviceRun(Map<String, Object> parameters)
    {
        // first checks the parameters
        if (checkServiceParameters(parameters))
        {
            String beginTime = new SimpleDateFormat(timeFormat).format(new Date());
            RequirementImportPlugin.log(Messages.getString("TTM2RequirementService.0").concat(beginTime), IStatus.INFO); //$NON-NLS-1$

            String inpath = (String) parameters.get(inParam);
            IPath outpath = new Path((String) parameters.get(outParam));
            outpath = outpath.addFileExtension(RequirementResource.FILE_EXTENSION);
            IPath path = (IPath) parameters.get(pathParam);

            HashMap<String, String> models = new HashMap<String, String>();
            models.put(inParam, inpath);
            models.put(outParam, outpath.toOSString());

            HashMap<String, Object> serviceParameters = new HashMap<String, Object>();
            serviceParameters.put("Id", TRANSFO_ID); //$NON-NLS-1$
            serviceParameters.put("Models", models); //$NON-NLS-1$
            serviceParameters.put("ProjectPath", path); //$NON-NLS-1$
            serviceParameters.put("IsXml", new Boolean(false)); //$NON-NLS-1$

            Object newInputFilePath = convertTTMModel(parameters);
            models.put(inParam, (String) newInputFilePath);

            IService transformation = ServicesManager.getInstance().getService(IServiceCst.TRANSFORMATION);
            transformation.serviceRun(serviceParameters);

            File tempFile = new File((String) newInputFilePath);
            tempFile.delete();

            String endTime = new SimpleDateFormat(timeFormat).format(new Date());
            RequirementImportPlugin.log(Messages.getString("TTM2RequirementService.1").concat(endTime), IStatus.INFO); //$NON-NLS-1$
        }

        return null;
    }

    /**
     * Converts the TTM model to be compliant.
     * 
     * @param parameters
     * @return
     */
    private Object convertTTMModel(Map<String, Object> parameters)
    {

        DocumentBuilderFactory factoryIn = DocumentBuilderFactory.newInstance();
        factoryIn.setIgnoringComments(true);
        factoryIn.setCoalescing(true);
        factoryIn.setIgnoringElementContentWhitespace(true);
        factoryIn.setNamespaceAware(true);
        File model = new File((String) parameters.get(inParam));
        Map<String, Object> namespaces = new HashMap<String, Object>();

        try
        {
            DocumentBuilder parser = factoryIn.newDocumentBuilder();

            // Document
            Document document = parser.parse(model);
            NamedNodeMap attNodes = document.getFirstChild().getAttributes();
            for (int i = 0; i < attNodes.getLength(); i++)
            {
                Node currentHeader = attNodes.item(i);
                // Looks for all the attributes with an 'xmlns' namespace
                if (currentHeader.getPrefix() != null && currentHeader.getPrefix().equals("xmlns")) //$NON-NLS-1$
                {
                    namespaces.put(currentHeader.getLocalName(), currentHeader.getNodeValue());
                }
            }
        }
        catch (ParserConfigurationException e)
        {
            RequirementImportPlugin.log(Messages.getString("TTM2RequirementService.2"), IStatus.ERROR, e); //$NON-NLS-1$
        }
        catch (IOException e)
        {
            RequirementImportPlugin.log(e);
        }
        catch (SAXException e)
        {
            RequirementImportPlugin.log(e);
        }

        namespaces.put(TtmPackage.eNAME, TtmPackage.eNS_URI);

        // Prepare and call the service aimed to change URIs
        IService changeUriService = ServicesManager.getInstance().getService(CHANGE_URI_ID);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("InFilePath", parameters.get(inParam));//$NON-NLS-1$
        params.put("OutFilePath", ((String) parameters.get(inParam)).concat("_temp"));//$NON-NLS-1$ //$NON-NLS-2$
        params.put("NSURI", namespaces); //$NON-NLS-1$

        changeUriService.serviceRun(params);

        return params.get("OutFilePath"); //$NON-NLS-1$
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
            RequirementImportPlugin.log(Messages.getString("TTM2RequirementService.3"), IStatus.ERROR); //$NON-NLS-1$
            return false;
        }

        if (!parameters.containsKey(outParam))
        {
            RequirementImportPlugin.log(Messages.getString("TTM2RequirementService.4"), IStatus.ERROR); //$NON-NLS-1$
            return false;
        }

        if (!parameters.containsKey(pathParam))
        {
            RequirementImportPlugin.log(Messages.getString("TTM2RequirementService.5"), IStatus.ERROR); //$NON-NLS-1$
            return false;
        }

        if (!(parameters.get(inParam) instanceof String))
        {
            RequirementImportPlugin.log(Messages.getString("TTM2RequirementService.6"), IStatus.ERROR); //$NON-NLS-1$
            return false;
        }

        if (!(parameters.get(outParam) instanceof String))
        {
            RequirementImportPlugin.log(Messages.getString("TTM2RequirementService.7"), IStatus.ERROR); //$NON-NLS-1$
            return false;
        }

        if (!(parameters.get(pathParam) instanceof IPath))
        {
            RequirementImportPlugin.log(Messages.getString("TTM2RequirementService.8"), IStatus.ERROR); //$NON-NLS-1$
            return false;
        }
        return true;
    }

}
