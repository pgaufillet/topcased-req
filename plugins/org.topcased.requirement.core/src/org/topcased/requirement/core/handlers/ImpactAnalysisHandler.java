/***********************************************************************************************************************
 * Copyright (c) 2011 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mathieu VELTEN (Atos) - initial API and implementation
 *               Philippe ROLAND (Atos) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.team.ui.history.IHistoryView;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.common.utils.JFaceUtils;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementDifferenceCalculator;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.utils.impact.MergeImpactProcessor;
import org.topcased.requirement.core.utils.impact.RequirementTimestampMonitor;
import org.topcased.requirement.teamhistory.TeamHistoryException;
import org.topcased.requirement.teamhistory.TeamHistoryManager;
import org.topcased.requirement.util.RequirementResource;

import ttm.Document;

/**
 * Handles Requirement Impact Analysis
 * 
 * @author mvelten
 * @author proland
 */
public class ImpactAnalysisHandler extends AbstractHandler
{

    private Object[] getSelections(ResourceSet resourceSet)
    {
        Set<Object> objects = new HashSet<Object>();
        for (Resource r : resourceSet.getResources())
        {
            // if the resource is the controled model it is not selected
            if (r.getContents() != null && r.getContents().size() > 0 && r.getContents().get(0) instanceof UpstreamModel)
            {
                continue;
            }

            if (r.getURI().isPlatform())
            {
                IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(r.getURI().toPlatformString(true)));
                if (file != null && file.exists())
                {
                    objects.add(file);
                }
            }
        }

        return objects.toArray();
    }

    private boolean isReadOnly(Object element)
    {
        if (element instanceof IFile)
        {
            IFile file = (IFile) element;
            return file.isReadOnly();
        }
        return false;
    }

    private Object[] appendFolders(Object[] selections)
    {
        Set<Object> set = new HashSet<Object>();
        for (Object o : selections)
        {
            if (o instanceof IFile)
            {
                IFile file = (IFile) o;
                set.add(file);
                IContainer parent = file.getParent();
                while (parent != null && !(parent instanceof IWorkspaceRoot))
                {
                    set.add(parent);
                    parent = parent.getParent();
                }
            }
        }
        return set.toArray();
    }

    public Object execute(final ExecutionEvent event) throws ExecutionException
    {
        final ISelection sel = HandlerUtil.getActiveMenuSelection(event);
        final IHistoryView view = (IHistoryView) HandlerUtil.getActivePart(event);
        final IEditorPart editor = HandlerUtil.getActiveEditor(event);
        Job job = new Job("Impact Analysis") //$NON-NLS-1$
        {

            @Override
            protected IStatus run(IProgressMonitor monitor)
            {
                if (monitor == null)
                {
                    monitor = new NullProgressMonitor();
                }
                monitor.beginTask("Impact Process", 4); //$NON-NLS-1$
                IEditorServices services = RequirementUtils.getSpecificServices(editor);
                if (services == null)
                {
                    Display.getDefault().syncExec(new ImpactErrorRunnable(Messages.getString("ImpactAnalysisHandler.2"))); //$NON-NLS-1$
                }
                EditingDomain editingDomain = services.getEditingDomain(editor);
                if (sel instanceof IStructuredSelection)
                {
                    IStructuredSelection structSel = (IStructuredSelection) sel;
                    Object obj = structSel.getFirstElement();
                    IFile fileInput = (IFile) view.getHistoryPage().getInput();
                    try
                    {
                        InputStream is = TeamHistoryManager.getStreamFromHistoryEntry(fileInput, obj);
                        ResourceSet rs = editingDomain.getResourceSet();
                        monitor.worked(1);
                        Resource oldModelResource = rs.createResource(URI.createURI("dummy://dummy")); //$NON-NLS-1$
                        try
                        {
                            Map<Object, Object> map = new HashMap<Object, Object>();
                            map.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
                            map.put(XMLResource.OPTION_DEFER_ATTACHMENT, true);
                            oldModelResource.eSetDeliver(false);
                            oldModelResource.load(is, map);
                            if (oldModelResource != null && oldModelResource.getContents().size() > 0)
                            {
                                monitor.worked(1);
                                // Recover local upstream file
                                IPath path = fileInput.getFullPath();
                                URI uriPath = URI.createPlatformResourceURI(path.toString(), true);
                                Resource modelResource = rs.getResource(uriPath, true);
                                if (modelResource != null && modelResource.getContents().size() > 0)
                                {
                                    FileSelectionRunnable run = new FileSelectionRunnable(modelResource);
                                    Display.getDefault().syncExec(run);
                                    monitor.worked(1);
                                    if (run.isOk())
                                    {
                                        processDialogResults(run.getDialogResult(), modelResource, oldModelResource, editingDomain);
                                    }

                                }
                            }
                            else
                            {
                                Display.getDefault().syncExec(new ImpactErrorRunnable(Messages.getString("ImpactAnalysisHandler.4"))); //$NON-NLS-1$
                            }
                            monitor.done();
                        }
                        catch (IOException e)
                        {
                            Display.getDefault().syncExec(new ImpactErrorRunnable(Messages.getString("ImpactAnalysisHandler.5"))); //$NON-NLS-1$
                            monitor.done();
                        }

                    }
                    catch (TeamHistoryException e)
                    {
                        Display.getDefault().syncExec(new ImpactErrorRunnable(Messages.getString("ImpactAnalysisHandler.6"))); //$NON-NLS-1$
                        monitor.done();
                    }

                }
                return Status.OK_STATUS;
            }
        };
        job.schedule();

        return null;
    }

    private void processDialogResults(Object[] objects, Resource modelResource, Resource oldModelResource, EditingDomain domain)
    {
        // recover selections
        Set<URI> resources = new HashSet<URI>();
        Map<Document, Document> documentsToImpactAnalyze = new HashMap<Document, Document>();
        for (Object object : objects)
        {
            if (object instanceof IFile)
            {
                IFile ifile = (IFile) object;
                IPath path = ifile.getFullPath();
                URI uriPath = URI.createPlatformResourceURI(path.toString(), true);
                Resource checkedResource = modelResource.getResourceSet().getResource(uriPath, true);
                if (checkedResource != null && checkedResource.getContents().size() > 0 && checkedResource.getContents().get(0) instanceof RequirementProject)
                {
                    List<Document> docs = RequirementUtils.getUpstreamDocuments(checkedResource);
                    UpstreamModel oldUpstream = findUpstream(oldModelResource);
                    for (Document oldDoc : oldUpstream.getDocuments())
                    {
                        for (Document newDoc : docs)
                        {
                            // Find those documents in the old upstream model which are also in the selected sub-model,
                            // Then launch an analysis on them
                            if (oldDoc.getIdent().equals(newDoc.getIdent()))
                            {
                                documentsToImpactAnalyze.put(newDoc, oldDoc);
                                if (!resources.contains(checkedResource.getURI()))
                                {
                                    resources.add(checkedResource.getURI());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (!documentsToImpactAnalyze.isEmpty())
        {
            ImpactCommand command = new ImpactCommand(documentsToImpactAnalyze, resources, oldModelResource, modelResource);
            domain.getCommandStack().execute(command);
        }
        else
        {
            Display.getDefault().syncExec(new ImpactErrorRunnable(Messages.getString("ImpactAnalysisHandler.7"))); //$NON-NLS-1$
        }
    }

    private UpstreamModel findUpstream(Resource oldModelResource)
    {
        if (oldModelResource instanceof UpstreamModel)
        {
            return (UpstreamModel) oldModelResource.getContents().get(0);
        }
        else
        {
            return RequirementUtils.getUpstreamModel(oldModelResource);
        }
    }

    private class ImpactCommand extends AbstractCommand
    {

        private Map<Document, Document> documentsToImpactAnalyze;

        private Set<URI> resources;

        private Resource oldModelResource;

        private Resource modelResource;

        public ImpactCommand(Map<Document, Document> documentsToImpactAnalyze, Set<URI> resources, Resource oldModelResource, Resource modelResource)
        {
            this.documentsToImpactAnalyze = documentsToImpactAnalyze;
            this.resources = resources;
            this.oldModelResource = oldModelResource;
            this.modelResource = modelResource;
        }

        @Override
        public boolean canExecute()
        {
            return true;
        }

        public void execute()
        {
            RequirementDifferenceCalculator calculator;
            try
            {
                calculator = new RequirementDifferenceCalculator(documentsToImpactAnalyze, false, null);
                new MergeImpactProcessor(resources, oldModelResource.getResourceSet(), calculator).processImpact();
                ResourceSet set = oldModelResource.getResourceSet();
                oldModelResource.unload();
                set.getResources().remove(oldModelResource);

                // redo hash
                UpstreamModel model = findUpstream(modelResource);
                RequirementProject project = (RequirementProject) model.eContainer();
                String hash = RequirementTimestampMonitor.hashModel(model);
                IFile modelFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(modelResource.getURI().toPlatformString(true)));
                String currentRevision = TeamHistoryManager.getCurrentRevisionLabel(modelFile);
                RequirementTimestampMonitor.createUpdateAnnotation(project, hash, currentRevision);

            }
            catch (InterruptedException e)
            {
                Display.getDefault().syncExec(new ImpactErrorRunnable(Messages.getString("ImpactAnalysisHandler.8"))); //$NON-NLS-1$
            }
            catch (TeamHistoryException e)
            {
                Display.getDefault().syncExec(new ImpactErrorRunnable(Messages.getString("ImpactAnalysisHandler.9"))); //$NON-NLS-1$
            }
        }

        public void redo()
        {
        }
    }

    private class FileSelectionRunnable implements Runnable
    {
        private final Resource modelResource;

        private int result;

        private Object[] objects;

        public FileSelectionRunnable(Resource modelResource)
        {
            this.modelResource = modelResource;

        }

        public Object[] getDialogResult()
        {
            return objects;
        }

        public boolean isOk()
        {
            return result == CheckedTreeSelectionDialog.OK;
        }

        public void run()
        {
            CheckedTreeSelectionDialog dialog = createDialog(modelResource);
            result = dialog.open();
            objects = dialog.getResult();
        }

        private CheckedTreeSelectionDialog createDialog(Resource modelResource)
        {
            CheckedTreeSelectionDialog dialog = new CheckedTreeSelectionDialog(Display.getDefault().getActiveShell(), new WorkbenchLabelProvider()
            {

                @Override
                public Color getForeground(Object element)
                {
                    if (isReadOnly(element))
                    {
                        return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
                    }
                    return super.getForeground(element);
                }

                @Override
                public Font getFont(Object element)
                {
                    if (isReadOnly(element))
                    {
                        return JFaceUtils.getFont(Display.getCurrent().getSystemFont().getFontData()[0], SWT.ITALIC);
                    }
                    return super.getFont(element);
                }

            }, new BaseWorkbenchContentProvider());
            dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
            Object[] selections = getSelections(modelResource.getResourceSet());
            dialog.setInitialSelections(selections);
            dialog.setExpandedElements(appendFolders(selections));
            dialog.setTitle(Messages.getString("ChooseImpactDocumentsDialog.title")); //$NON-NLS-1$
            dialog.setMessage(Messages.getString("ChooseImpactDocumentsDialog.message")); //$NON-NLS-1$
            dialog.addFilter(new ViewerFilter()
            {
                @Override
                public boolean select(Viewer viewer, Object parentElement, Object element)
                {
                    if (element instanceof IFile)
                    {
                        IFile file = (IFile) element;
                        return RequirementResource.FILE_EXTENSION.equals(file.getFileExtension());
                    }
                    return true;
                }
            });
            return dialog;
        }

    }

    private class ImpactErrorRunnable implements Runnable
    {

        private String message;

        private String reason;

        public ImpactErrorRunnable(String reason)
        {
            this.message = ""; //$NON-NLS-1$
            this.reason = reason;
        }

        public void run()
        {
            ErrorDialog newDialog = new ErrorDialog(Display.getDefault().getActiveShell(), Messages.getString("ImpactAnalysisHandler.13"), message, new Status(IStatus.ERROR, //$NON-NLS-1$
                    RequirementCorePlugin.getId(), reason), 4);
            newDialog.open();
        }
    }
}
