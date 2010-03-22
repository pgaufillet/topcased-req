/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
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
import org.topcased.requirement.RequirementImportPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import ttm.TtmPackage;

/**
 * Defines the Tramway to Requirements import service.
 * 
 * @author <a href="mailto:christophe.le-camus@c-s.fr">Christophe LE CAMUS</a>
 */
public class TTM2RequirementService implements IService
{
    // Constante defining namespace
    private static final String XMLNS_EXTENSION = "xmlns";

    public static final String ID = "org.topcased.requirement.service.import";

    /**
     * @see org.topcased.bus.core.IService#serviceRun(java.util.Map)
     */
    public Object serviceRun(Map<String, Object> parameters)
    {
        // first checks the parameters
        if (checkServiceParameters(parameters))
        {
            String beginTime = new SimpleDateFormat("yyyy-MM-dd hh'h'mm").format(new Date());
            RequirementImportPlugin.log("Entering TRAMway to Requirements import service at ".concat(beginTime), IStatus.INFO);

            String inpath = (String) parameters.get("IN");
            IPath outpath = new Path((String) parameters.get("OUT"));
            outpath = outpath.addFileExtension("requirement");
            IPath path = (IPath) parameters.get("Path");

            HashMap<String, String> models = new HashMap<String, String>();
            models.put("IN", inpath);
            models.put("OUT", outpath.toOSString());

            HashMap<String, Object> serviceParameters = new HashMap<String, Object>();
            serviceParameters.put("Id", ID);
            serviceParameters.put("Models", models);
            serviceParameters.put("ProjectPath", path);
            serviceParameters.put("IsXml", new Boolean(false));

            Object newInputFilePath = convertTTMModel(parameters);
            models.put("IN", (String) newInputFilePath);

            IService transformation = ServicesManager.getInstance().getService(IServiceCst.TRANSFORMATION);
            transformation.serviceRun(serviceParameters);

            File tempFile = new File((String) newInputFilePath);
            tempFile.delete();

            String endTime = new SimpleDateFormat("yyyy-MM-dd hh'h'mm").format(new Date());
            RequirementImportPlugin.log("Leaving TRAMway to Requirements import service at ".concat(endTime), IStatus.INFO);
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
        File model = new File((String) parameters.get("IN"));
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
                if (currentHeader.getPrefix() != null && currentHeader.getPrefix().equals(XMLNS_EXTENSION))
                {
                    namespaces.put(currentHeader.getLocalName(), currentHeader.getNodeValue());
                }
            }
        }
        catch (ParserConfigurationException e)
        {
            RequirementImportPlugin.log("Parser configuration exception", IStatus.ERROR, e);
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
        IService changeUriService = ServicesManager.getInstance().getService("org.topcased.service.changeuri");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("InFilePath", parameters.get("IN"));
        params.put("OutFilePath", ((String) parameters.get("IN")).concat("_temp"));
        params.put("NSURI", namespaces);

        changeUriService.serviceRun(params);

        return params.get("OutFilePath");
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
            RequirementImportPlugin.log("The parameter IN must be set.", IStatus.ERROR);
            return false;
        }

        if (!parameters.containsKey("OUT"))
        {
            RequirementImportPlugin.log("The parameter OUT must be set.", IStatus.ERROR);
            return false;
        }

        if (!parameters.containsKey("Path"))
        {
            RequirementImportPlugin.log("The parameter Path must be set.", IStatus.ERROR);
            return false;
        }

        if (!(parameters.get("IN") instanceof String))
        {
            RequirementImportPlugin.log("The parameter IN must be a String.", IStatus.ERROR);
            return false;
        }

        if (!(parameters.get("OUT") instanceof String))
        {
            RequirementImportPlugin.log("The parameter OUT must be a String.", IStatus.ERROR);
            return false;
        }

        if (!(parameters.get("Path") instanceof IPath))
        {
            RequirementImportPlugin.log("The parameter Path must be a IPath.", IStatus.ERROR);
            return false;
        }
        return true;
    }

}
