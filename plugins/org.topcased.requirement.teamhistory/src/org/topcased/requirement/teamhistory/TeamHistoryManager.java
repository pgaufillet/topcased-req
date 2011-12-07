/***********************************************************************************************************************
 * Copyright (c) 2011 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mathieu VELTEN (Atos) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.teamhistory;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * Exposes several functions for interaction with he history view
 * 
 * @author mvelten
 */
public class TeamHistoryManager
{

    protected static final IHistoryHandler[] orderedHandlersArray;

    protected static class HandlerPriorityPair implements Comparable<HandlerPriorityPair>
    {

        public IHistoryHandler handler;

        public int priority;

        public int compareTo(HandlerPriorityPair o)
        {
            if (o.priority > priority)
            {
                return 1;
            }
            else if (o.priority < priority)
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
    }

    static
    {
        IConfigurationElement[] configElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.topcased.requirement.teamhistory", "historyHandler");

        List<HandlerPriorityPair> handlerPriorityPairs = new LinkedList<HandlerPriorityPair>();
        for (IConfigurationElement elem : configElements)
        {
            if ("historyHandler".equals(elem.getName()))
            {
                try
                {
                    HandlerPriorityPair handlerPriorityPair = new HandlerPriorityPair();
                    handlerPriorityPair.handler = (IHistoryHandler) elem.createExecutableExtension("class");
                    handlerPriorityPair.priority = Integer.parseInt(elem.getAttribute("priority"));

                    handlerPriorityPairs.add(handlerPriorityPair);
                }
                catch (Exception e)
                {
                }
            }
        }

        Collections.sort(handlerPriorityPairs);

        orderedHandlersArray = new IHistoryHandler[handlerPriorityPairs.size()];

        for (int i = 0; i < orderedHandlersArray.length; i++)
        {
            orderedHandlersArray[i] = handlerPriorityPairs.get(i).handler;
        }
    }

    private static IHistoryHandler getHistoryHandler(IResource r) throws TeamHistoryException
    {
        for (int i = 0; i < orderedHandlersArray.length; i++)
        {
            if (orderedHandlersArray[i].handle(r))
            {
                return orderedHandlersArray[i];
            }
        }
        throw new TeamHistoryException("No team history handler available for this file");
    }

    /**
     * Gets current revision label for given resource
     * 
     * @param r the resource
     * @return its revision label. If local history, returns timestamp
     * @throws TeamHistoryException
     */
    public static String getCurrentRevisionLabel(IResource r) throws TeamHistoryException
    {
        return getHistoryHandler(r).getCurrentRevisionLabel(r);
    }

    /**
     * Shows history view for the given element, selecting the given revision
     * 
     * @param r the element to open the view for
     * @param revisionLabelToSelect its revision
     * @throws TeamHistoryException
     */
    public static void showHistoryView(IResource r, String revisionLabelToSelect) throws TeamHistoryException
    {
        getHistoryHandler(r).showHistoryView(r, revisionLabelToSelect);
    }

    /**
     * Retrieves an InputStream for the given revision of the given file
     * 
     * @param file the file
     * @param historyEntry the menu's selected entry
     * @return an input stream
     * @throws TeamHistoryException
     */
    public static InputStream getStreamFromHistoryEntry(IFile file, Object historyEntry) throws TeamHistoryException
    {
        return getHistoryHandler(file).getStreamFromHistoryEntry(file, historyEntry);
    }
}
