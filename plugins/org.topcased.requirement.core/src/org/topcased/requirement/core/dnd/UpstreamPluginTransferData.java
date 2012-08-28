/***********************************************************************************************************************
 * Copyright (c) 2012 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Philippe ROLAND (Atos) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.dnd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.PluginTransferData;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.utils.RequirementUtils;

import ttm.HierarchicalElement;
import ttm.Requirement;

/**
 * Subclass of PluginTransferData, intended to be instanced and used by DragSourceUpstreamAdapter in order to add
 * PluginTransfer support to upstream drag-and-drops. The byte array being transmitted is the serialization of a List of
 * Strings, where the first String represents the upstream resource's URI and all subsequent Strings consist of the
 * dropped upstream's XMI IDs
 * 
 * @author proland
 */
public class UpstreamPluginTransferData extends PluginTransferData
{
    /**
     * Default constructor. It is strongly recommended that you use the static getInstance method unless you are willing
     * to parse the raw byte array within your corresponding IDropActionDelegate
     * 
     * @param extensionId
     * @param data
     */
    public UpstreamPluginTransferData(String extensionId, byte[] data)
    {
        super(extensionId, data);
    }

    /**
     * Creates a new UpstreamPluginTransferData object with the byte data set, as determined by the selection
     * @param extensionId the extension id
     * @param selection the selection
     * @return
     */
    public static UpstreamPluginTransferData getInstance(String extensionId, IStructuredSelection selection)
    {
        try
        {
            List< ? > selectionList = selection.toList();
            List<Requirement> droppedRequirements = new ArrayList<Requirement>();
            for (Object object : selectionList)
            {
                List<Object> list = new ArrayList<Object>();
                list.add(object);
                addAll(list, droppedRequirements);
            }

            if (droppedRequirements.isEmpty())
            {
                return null;
            }
            List<String> stringData = new ArrayList<String>();
            XMIResource res = (XMIResource) droppedRequirements.get(0).eResource();
            stringData.add(res.getURI().toPlatformString(true));
            for (EObject obj : droppedRequirements)
            {
                stringData.add(res.getID(obj));
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream buffered = new ObjectOutputStream(bos);
            buffered.writeObject(stringData);
            byte[] data = bos.toByteArray();
            bos.close();
            buffered.close();
            UpstreamPluginTransferData upstreamPluginTransferData = new UpstreamPluginTransferData(extensionId, data);
            return upstreamPluginTransferData;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static void addAll(List< ? > objects, List<Requirement> droppedRequirements)
    {
        for (Object object : objects)
        {
            if (object instanceof Requirement)
            {
                droppedRequirements.add((Requirement) object);
            }
            else if (object instanceof HierarchicalElement)
            {
                addAll(((HierarchicalElement) object).getChildren(), droppedRequirements);
            }
        }
    }

    public static Resource getUpstreamResource(byte[] data)
    {
        List<String> stringData = parseData(data);
        if (stringData != null && !stringData.isEmpty())
        {
            Path path = new Path(stringData.get(0));
            URI uri = URI.createPlatformResourceURI(path.toString(), true);

            IEditorPart editor = RequirementUtils.getCurrentEditor();
            IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
            EditingDomain domain = services.getEditingDomain(editor);

            Resource res = domain.getResourceSet().getResource(uri, true);
            return res;
        }
        return null;
    }

    public static List<Requirement> getDroppedRequirements(Resource res, byte[] data)
    {
        List<Requirement> result = new ArrayList<Requirement>();
        List<String> parseData = parseData(data);
        List<String> requirementStringData = parseData.subList(1, parseData.size());
        for (String id : requirementStringData)
        {
            EObject eobj = res.getEObject(id);
            if (eobj instanceof Requirement)
            {
                result.add((Requirement) eobj);
            }
        }
        return result;
    }

    /**
     * Parses the byteData to recover the resource and requirements.
     */
    protected static List<String> parseData(byte[] data)
    {
        // Users could potentially put anything here - we must be careful
        List<String> stringList = new ArrayList<String>();
        try
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInput in = new ObjectInputStream(bis);
            Object obj = in.readObject();
            if (obj instanceof List< ? >)
            {
                List< ? > list = (List< ? >) obj;
                for (Object child : list)
                {
                    if (child instanceof String)
                    {
                        String string = (String) child;
                        stringList.add(string);
                    }
                }
            }
            bis.close();
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return stringList;
    }

}
