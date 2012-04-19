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
package org.topcased.requirement.teamhistory.svn;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.svn.core.connector.ISVNConnector;
import org.eclipse.team.svn.core.connector.SVNChangeStatus;
import org.eclipse.team.svn.core.connector.SVNConnectorException;
import org.eclipse.team.svn.core.connector.SVNEntryInfo;
import org.eclipse.team.svn.core.connector.SVNEntryReference;
import org.eclipse.team.svn.core.connector.SVNEntryRevisionReference;
import org.eclipse.team.svn.core.connector.SVNRevision;
import org.eclipse.team.svn.core.operation.SVNNullProgressMonitor;
import org.eclipse.team.svn.core.resource.IRepositoryLocation;
import org.eclipse.team.svn.core.svnstorage.SVNRemoteStorage;
import org.eclipse.team.svn.core.utility.SVNUtility;
import org.eclipse.team.svn.ui.history.SVNHistoryPage;
import org.eclipse.team.svn.ui.history.model.LocalLogNode;
import org.eclipse.team.svn.ui.history.model.SVNLogNode;
import org.eclipse.team.ui.history.IHistoryView;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.topcased.requirement.teamhistory.IHistoryHandler;
import org.topcased.requirement.teamhistory.LocalHistoryHandler;
import org.topcased.requirement.teamhistory.TeamHistoryException;

/**
 * Handles IHistoryHandler operations for SVN history
 * 
 * @author mvelten
 */
public class SVNHistoryHandler implements IHistoryHandler {

	public boolean handle(IResource r) {
		File file = r.getFullPath().toFile();
		SVNEntryInfo svnInfos = SVNUtility.getSVNInfo(file);
		if (svnInfos == null) {
			SVNChangeStatus svnInfos2 = SVNUtility.getSVNInfoForNotConnected(r
					.getProject());
			return svnInfos2 != null;
		}
		return true;
	}

	public String getCurrentRevisionLabel(IResource r) {
		SVNChangeStatus svnInfos = SVNUtility.getSVNInfoForNotConnected(r);
		if (svnInfos != null) {
			return Long.toString(svnInfos.lastChangedRevision);
		} else {
			return Long.toString(r.getLocalTimeStamp());
		}
	}

	public void showHistoryView(IResource r, String revisionLabelToSelect)
			throws TeamHistoryException {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		try {
			IHistoryView historyView = (IHistoryView) page
					.showView(IHistoryView.VIEW_ID);
			SVNHistoryPage historyPage = (SVNHistoryPage) historyView
					.showHistoryFor(r);

			long rev = Long.parseLong(revisionLabelToSelect);
			historyPage.selectRevision(rev);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (PartInitException e) {
			throw new TeamHistoryException(
					"Error when opening the history view", e);
		}

	}

	public InputStream getStreamFromHistoryEntry(final IFile file,
			Object historyEntry) throws TeamHistoryException {

		if (historyEntry instanceof LocalLogNode) {
			LocalLogNode localLogNode = (LocalLogNode) historyEntry;
			return LocalHistoryHandler.getStreamFromFileRevision(file,
					(IFileRevision) localLogNode
							.getAdapter(IFileRevision.class));
		}

		if (historyEntry instanceof SVNLogNode) {
			SVNLogNode svnLogNode = (SVNLogNode) historyEntry;
			long revision = svnLogNode.getRevision();

			SVNChangeStatus svnInfos = SVNUtility
					.getSVNInfoForNotConnected(file);

			final SVNEntryRevisionReference reference = new SVNEntryRevisionReference(
					new SVNEntryReference(svnInfos.url),
					SVNRevision.fromNumber(revision));

			PipedInputStream in = new PipedInputStream(10240);
			try {
				final PipedOutputStream out = new PipedOutputStream(in);
				new Thread(new Runnable() {
					public void run() {
						IRepositoryLocation repoLocation = SVNRemoteStorage
								.instance().getRepositoryLocation(file);
						ISVNConnector proxy = repoLocation.acquireSVNProxy();
						try {

							proxy.streamFileContent(reference, 10240, out,
									new SVNNullProgressMonitor());
						} catch (SVNConnectorException e) {
							e.printStackTrace();
						} finally {
							repoLocation.releaseSVNProxy(proxy);
						}
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
				return in;
			} catch (IOException e) {
				throw new TeamHistoryException(
						"Cannot retrieve the content of this revision", e);
			}
		}

		throw new TeamHistoryException(
				"Cannot retrieve the content of this revision");
	}

}
