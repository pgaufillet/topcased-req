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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;

/**
 * General interface for handling history-related tasks
 * 
 * @author mvelten
 */
public interface IHistoryHandler
{
    /**
     * Returns true if this particular handler can handle this resource
     * 
     * @param r the resource to test
     * @return true if this particular handler can handle this resource
     */
    public boolean handle(IResource r);

    /**
     * Gets the latest revision label for this resource
     * 
     * @param r the resource
     * @return its latest revision's label
     * @throws TeamHistoryException
     */
    public String getCurrentRevisionLabel(IResource r) throws TeamHistoryException;

    /**
     * Opens a history view for the given resource, selecting it for the given revision
     * 
     * @param r the resource
     * @param revisionLabelToSelect its revision label
     * @throws TeamHistoryException
     */
    public void showHistoryView(IResource r, String revisionLabelToSelect) throws TeamHistoryException;

    /**
     * Gets InputStream for the given revision of the given file
     * 
     * @param file the file
     * @param historyEntry its historyEntry as given by the selected element in a history view
     * @return an InputStream
     * @throws TeamHistoryException
     */
    public InputStream getStreamFromHistoryEntry(IFile file, Object historyEntry) throws TeamHistoryException;
}
