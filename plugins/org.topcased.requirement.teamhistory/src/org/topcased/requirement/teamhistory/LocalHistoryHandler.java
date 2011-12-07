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
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.ui.history.IHistoryView;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Implementation of IHistoryHandler for local history
 * 
 * @author mvelten
 */
public class LocalHistoryHandler implements IHistoryHandler
{

    public boolean handle(IResource r)
    {
        return true;
    }

    public String getCurrentRevisionLabel(IResource r)
    {
        return Long.toString(r.getLocalTimeStamp());
    }

    public void showHistoryView(IResource r, String revisionLabelToSelect) throws TeamHistoryException
    {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

        try
        {
            IHistoryView historyView = (IHistoryView) page.showView(IHistoryView.VIEW_ID);
            historyView.showHistoryFor(r);
        }
        catch (PartInitException e)
        {
            throw new TeamHistoryException("Error when opening the history view", e);
        }
    }

    public InputStream getStreamFromHistoryEntry(IFile file, Object historyEntry) throws TeamHistoryException
    {
        if (historyEntry instanceof IFileRevision)
        {
            return getStreamFromFileRevision(file, (IFileRevision) historyEntry);
        }
        throw new TeamHistoryException("Cannot retrieve the content of this revision");
    }

    /**
     * Gets InputStream for the given revision of the given file
     * 
     * @param file the file
     * @param fileRevision its revision
     * @return an InputStream
     * @throws TeamHistoryException
     */
    public static InputStream getStreamFromFileRevision(IFile file, IFileRevision fileRevision) throws TeamHistoryException
    {
        long stamp = fileRevision.getTimestamp();

        try
        {
            if (file.getLocalTimeStamp() == stamp)
            {
                return file.getContents();
            }

            IFileState[] fileStates = file.getHistory(null);
            for (IFileState fileState : fileStates)
            {
                if (stamp == fileState.getModificationTime())
                {
                    return fileState.getContents();
                }
            }
        }
        catch (CoreException e)
        {
            throw new TeamHistoryException("Cannot retrieve the content of this revision", e);
        }
        throw new TeamHistoryException("Cannot retrieve the content of this revision");
    }
}
