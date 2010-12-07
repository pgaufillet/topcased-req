/***********************************************************************************************************************
 * Copyright (c) 2010 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.dialogs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.topcased.facilities.resources.SharedImageHelper;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.providers.DocumentSelectionContentProvider;
import org.topcased.requirement.core.providers.DocumentSelectionLabelProvider;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.util.RequirementResource;

import ttm.Document;
import ttm.TtmPackage;

/**
 * Dialog allowing to select {@link Document} contained inside requirement model.<br>
 * The 'Add' button is in charge of loading requirement models into a temporary resource set. 'Remove' and 'Remove All'
 * buttons unload resources from this resource set. When the dialog closes, all resources contained into the resource
 * set are unloaded.<br>
 * 
 * Creation : 06 december 2010<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since TOPCASED 4.3.0
 */
public class DocumentSelectionDialog extends TitleAreaDialog
{
    /** The checkbox tree viewer */
    private CheckboxTreeViewer viewer;

    /** the temporary resource set in which resources will be successively loaded/unloaded */
    private ResourceSet rscSet;

    private Collection<Object> checkedDocuments;

    /**
     * Constructor
     * 
     * @param path The model path
     * @param nsURI The URI of the model
     */
    public DocumentSelectionDialog()
    {
        super(Display.getCurrent().getActiveShell());
        rscSet = new ResourceSetImpl();
        checkedDocuments = Collections.emptyList();
        setHelpAvailable(false);
    }

    /**
     * Gets the checked documents selected by the user.
     * 
     * @return A filtered collection containing only the checked documents
     */
    public Collection<Object> getCheckedDocuments()
    {
        return checkedDocuments;
    }

    /**
     * Configures the shell
     */
    protected void configureShell(Shell shell)
    {
        super.configureShell(shell);
        shell.setText(Messages.getString("DocumentSelectionDialog.shell")); //$NON-NLS-1$
        shell.setMinimumSize(600, 400);
    }

    /**
     * @see org.eclipse.jface.window.Window#getShellStyle()
     */
    @Override
    protected int getShellStyle()
    {
        return super.getShellStyle() | SWT.RESIZE | SWT.MIN | SWT.MAX;
    }

    /**
     * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent)
    {
        final GridLayout layout = new GridLayout();
        layout.marginHeight = 5;
        layout.marginWidth = 5;

        parent.setLayout(layout);

        // defines the group
        final Group group = new Group(parent, SWT.NONE);
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        group.setText(Messages.getString("DocumentSelectionDialog.label")); //$NON-NLS-1$

        // Add the table widget
        createCheckboxTree(group);

        // Add a composite containing the 2 buttons
        createButtons(group);

        return parent;
    }

    /**
     * @see org.eclipse.jface.dialogs.TrayDialog#createButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createButtonBar(Composite parent)
    {
        Control buttonBar = super.createButtonBar(parent);
        // handle the state of the OK button
        updateOKButton();
        return buttonBar;
    }

    /**
     * @see org.eclipse.jface.dialogs.TitleAreaDialog#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent)
    {
        Control control = super.createContents(parent);
        setTitle(Messages.getString("DocumentSelectionDialog.title")); //$NON-NLS-1$
        setTitleImage(SharedImageHelper.getTopcasedDialogImage());
        setMessage(Messages.getString("DocumentSelectionDialog.desc")); //$NON-NLS-1$
        return control;
    }

    /**
     * Adds the checkbox tree viewer widget.
     * 
     * @param composite The parent composite
     */
    private void createCheckboxTree(Composite composite)
    {
        viewer = new CheckboxTreeViewer(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        viewer.setLabelProvider(new DocumentSelectionLabelProvider(RequirementUtils.getAdapterFactory()));
        viewer.setContentProvider(new DocumentSelectionContentProvider(RequirementUtils.getAdapterFactory()));
        viewer.setInput(rscSet);
        viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        viewer.addCheckStateListener(new ICheckStateListener()
        {
            public void checkStateChanged(CheckStateChangedEvent event)
            {
                // all subtree part is checked
                viewer.setSubtreeChecked(event.getElement(), event.getChecked());
                updateOKButton();
            }
        });
        viewer.addDoubleClickListener(new IDoubleClickListener()
        {
            public void doubleClick(DoubleClickEvent event)
            {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                Object firstElement = selection.getFirstElement();
                if (viewer.getExpandedState(firstElement))
                {
                    viewer.collapseToLevel(firstElement, AbstractTreeViewer.ALL_LEVELS);
                }
                else
                {
                    viewer.expandToLevel(firstElement, 1);
                }
            }
        });
    }

    /**
     * Adds the three buttons to the right-side of the dialog.
     * 
     * @param composite the parent composite.
     */
    private void createButtons(Composite composite)
    {
        final Composite compositeForButtons = new Composite(composite, SWT.NONE);
        compositeForButtons.setLayout(new FillLayout(SWT.VERTICAL));
        compositeForButtons.setLayoutData(new GridData(SWT.NONE, SWT.TOP, false, false));

        createOpenButton(compositeForButtons);
        createRemoveButton(compositeForButtons);
        createRemoveAllButton(compositeForButtons);
    }

    /**
     * Adds the <b>Add</b> button to the dedicated composite.
     * 
     * @param composite The button composite
     */
    private void createOpenButton(Composite composite)
    {
        // Add select File button
        Button openButton = new Button(composite, SWT.NONE);
        openButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
        openButton.setToolTipText(Messages.getString("DocumentSelectionDialog.add")); //$NON-NLS-1$
        openButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                ResourceSelectionDialog dialog = new ResourceSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), Messages.getString("DocumentSelectionDialog.msg")); //$NON-NLS-1$
                if (dialog.open() == Window.OK)
                {
                    Object[] result = dialog.getResult();
                    if (result != null)
                    {
                        loadResources(result);
                    }
                }
            }
        });
    }

    /**
     * Adds the <b>Remove</b> button to the dedicated composite.
     * 
     * @param composite The button composite
     */
    private void createRemoveAllButton(Composite composite)
    {
        Button removeAllBtn = new Button(composite, SWT.NONE);
        removeAllBtn.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_REMOVEALL));
        removeAllBtn.setToolTipText(Messages.getString("DocumentSelectionDialog.removeAll")); //$NON-NLS-1$
        removeAllBtn.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                unloadResources();
            }
        });
    }

    /**
     * Adds the <b>Remove All</b> button to the dedicated composite.
     * 
     * @param composite The button composite
     */
    private void createRemoveButton(Composite composite)
    {
        Button removeBtn = new Button(composite, SWT.NONE);
        removeBtn.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_REMOVE));
        removeBtn.setToolTipText(Messages.getString("DocumentSelectionDialog.remove")); //$NON-NLS-1$
        removeBtn.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                ISelection selection = viewer.getSelection();
                if (selection instanceof IStructuredSelection)
                {
                    unloadResources(((IStructuredSelection) selection).toArray());
                }
            }
        });
    }

    /**
     * Loads EMF resources (requirement models) into the temporary resource set. This task is carried out through a
     * progress monitor dialog in the same thread.
     * 
     * @param rscToLoad The list of selected IFile corresponding to potential resources to load into the resource set.
     */
    private void loadResources(final Object[] rscToLoad)
    {
        try
        {
            ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());
            progressDialog.run(false, false, new IRunnableWithProgress()
            {
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                    Collection<IFile> filteredRsc = filterUserSelection(rscToLoad);
                    monitor.beginTask(Messages.getString("DocumentSelectionDialog.loadingTitle"), filteredRsc.size()); //$NON-NLS-1$
                    for (IFile file : filteredRsc)
                    {
                        monitor.subTask(Messages.getString("DocumentSelectionDialog.loading") + file.getName()); //$NON-NLS-1$
                        URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
                        rscSet.getResource(uri, true);
                        monitor.worked(1);
                    }
                }
            });
        }
        catch (InvocationTargetException ite)
        {
            RequirementCorePlugin.log(Messages.getString("DocumentSelectionDialog.loadingErrorMsg"), IStatus.WARNING, ite); //$NON-NLS-1$
        }
        catch (InterruptedException ie)
        {
            RequirementCorePlugin.log(Messages.getString("DocumentSelectionDialog.loadingErrorMsg"), IStatus.WARNING, ie); //$NON-NLS-1$
        }
        finally
        {
            viewer.refresh();
            viewer.expandAll();
        }
    }

    /**
     * @param selection An array containing all elements selected by the user. This selection may contain invalid
     *        elements (folder, no-requirement models, etc...)
     * @return A Collection of IFile corresponding to requirement models contained into the workspace.
     */
    private Collection<IFile> filterUserSelection(Object[] selection)
    {
        Collection<IFile> validSelection = new ArrayList<IFile>();
        for (Object obj : selection)
        {
            if (obj instanceof IFile && RequirementResource.FILE_EXTENSION.equals(((IFile) obj).getFileExtension()))
            {
                validSelection.add((IFile) obj);
            }
        }
        return validSelection;
    }

    /**
     * Unloads all the resources already loaded into the resource set.
     */
    private void unloadResources()
    {
        try
        {
            ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());
            progressDialog.run(false, false, new IRunnableWithProgress()
            {
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                    monitor.beginTask(Messages.getString("DocumentSelectionDialog.unloadingTitle"), rscSet.getResources().size() + 1); //$NON-NLS-1$
                    for (int i = 0; i < rscSet.getResources().size(); i++)
                    {
                        String name = URI.decode(rscSet.getResources().get(i).getURI().lastSegment());
                        monitor.subTask(Messages.getString("DocumentSelectionDialog.unloading") + name); //$NON-NLS-1$
                        rscSet.getResources().get(i).unload();
                        monitor.worked(1);
                    }

                    // clear all the list content after having unloaded all resources.
                    monitor.subTask(Messages.getString("DocumentSelectionDialog.clearResourceSet")); //$NON-NLS-1$
                    rscSet.getResources().clear();
                    monitor.worked(1);
                }
            });
        }
        catch (InvocationTargetException ite)
        {
            RequirementCorePlugin.log(Messages.getString("DocumentSelectionDialog.unloadingErrorMsg"), IStatus.WARNING, ite); //$NON-NLS-1$
        }
        catch (InterruptedException ie)
        {
            RequirementCorePlugin.log(Messages.getString("DocumentSelectionDialog.unloadingErrorMsg"), IStatus.WARNING, ie); //$NON-NLS-1$
        }
        finally
        {
            viewer.refresh();
            updateOKButton();
        }
    }

    /**
     * Unloads one targeted resource from the resource set.
     * 
     * @param selected A set of selected resources to unload from the resource set.
     */
    private void unloadResources(final Object[] rscToUnload)
    {
        try
        {
            ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());
            progressDialog.run(false, false, new IRunnableWithProgress()
            {
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                    monitor.beginTask(Messages.getString("DocumentSelectionDialog.unloadingTitle"), rscToUnload.length); //$NON-NLS-1$
                    for (int i = 0; i < rscToUnload.length; i++)
                    {
                        String name = URI.decode(rscSet.getResources().get(i).getURI().lastSegment());
                        monitor.subTask(Messages.getString("DocumentSelectionDialog.unloading") + name); //$NON-NLS-1$
                        int index = rscSet.getResources().indexOf(rscToUnload[i]);
                        rscSet.getResources().get(index).unload();
                        rscSet.getResources().remove(index);
                        monitor.worked(1);
                    }
                }
            });
        }
        catch (InvocationTargetException ite)
        {
            RequirementCorePlugin.log(Messages.getString("DocumentSelectionDialog.unloadingErrorMsg"), IStatus.WARNING, ite); //$NON-NLS-1$
        }
        catch (InterruptedException ie)
        {
            RequirementCorePlugin.log(Messages.getString("DocumentSelectionDialog.unloadingErrorMsg"), IStatus.WARNING, ie); //$NON-NLS-1$
        }
        finally
        {
            viewer.refresh();
            updateOKButton();
        }
    }

    /**
     * In charge of enabling/disabling the OK button and informing the user.
     * 
     * @return <code>true</code> if the OK button can be enabled, <code>false</code> otherwise.
     */
    private void updateOKButton()
    {
        if (rscSet.getResources().isEmpty())
        {
            setErrorMessage(Messages.getString("DocumentSelectionDialog.errorMsg1")); //$NON-NLS-1$
            getButton(OK).setEnabled(false);
        }
        else if (viewer.getCheckedElements().length == 0)
        {
            setErrorMessage(Messages.getString("DocumentSelectionDialog.errorMsg2")); //$NON-NLS-1$
            getButton(OK).setEnabled(false);
        }
        else
        {
            setErrorMessage(null);
            getButton(OK).setEnabled(true);
        }
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed()
    {
        // need to filter and keep a reference to those elements. Next step will unload resources from the resource set.
        Collection<Object> filteredObjs = EcoreUtil.getObjectsByType(Arrays.asList(viewer.getCheckedElements()), TtmPackage.Literals.DOCUMENT);
        // then these objects are copied because next step will consist in unloading these resources
        Copier copier = new Copier();
        checkedDocuments = copier.copyAll(filteredObjs);
        copier.copyReferences();
        super.okPressed();
    }

    /**
     * @see org.eclipse.jface.dialogs.TrayDialog#close()
     */
    @Override
    public boolean close()
    {
        // Unload resources previously loaded avoiding memory-lakes
        unloadResources();
        return super.close();
    }
}
