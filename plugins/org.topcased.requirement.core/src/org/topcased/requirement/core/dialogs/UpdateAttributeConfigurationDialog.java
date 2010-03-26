/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.dialogs;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.topcased.facilities.resources.SharedImageHelper;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.AttributesType;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.core.filters.UpdateAttributeConfigurationFilter;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.preferences.CurrentPreferenceHelper;
import org.topcased.requirement.core.providers.CurrentRequirementLabelProvider;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * Dialog allowing to update attribute configuration for a requirement project.<br>
 * On the left side, the workspace attribute configuration is shown.<br>
 * On the right side, the local project attribute configuration is shown.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * 
 */
public class UpdateAttributeConfigurationDialog extends TitleAreaDialog
{
    private TreeViewer leftViewer;

    private TreeViewer rightViewer;

    private AttributeConfiguration workspaceConf;

    private AttributeConfiguration localConf;

    private UpdateAttributeManager manager;

    /**
     * Constructor
     * 
     * @param parentShell The parent shell
     */
    public UpdateAttributeConfigurationDialog(IStructuredSelection theSelection, Shell parentShell)
    {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE);
        setTitleImage(SharedImageHelper.getTopcasedDialogImage());
        if (theSelection.getFirstElement() instanceof AttributeConfiguration)
        {
            localConf = (AttributeConfiguration) theSelection.getFirstElement();
            workspaceConf = CurrentPreferenceHelper.getConfigurationInWorkspace();
            manager = new UpdateAttributeManager(localConf);
        }
    }

    /**
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell)
    {
        super.configureShell(shell);
        shell.setText(Messages.getString("UpdateAttributeConfigurationDialog.0")); //$NON-NLS-1$
        shell.setSize(550, 450);
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
     */
    @Override
    protected void cancelPressed()
    {
        super.cancelPressed();
        manager.undoExecuted();
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed()
    {
        super.okPressed();
        manager.execute();
    }

    /**
     * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        setTitle(Messages.getString("UpdateAttributeConfigurationDialog.1")); //$NON-NLS-1$
        setMessage(Messages.getString("UpdateAttributeConfigurationDialog.2")); //$NON-NLS-1$
        final Composite composite = (Composite) super.createDialogArea(parent);

        final Composite child = new Composite(composite, SWT.NONE);
        child.setLayout(new GridLayout(3, false));
        child.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // left panel : workspace configuration
        createLeftPanel(child);
        createButtonPanel(child);
        // right panel : current configuration of the requirement model
        createRightPanel(child);

        return composite;
    }

    /**
     * Creates the button panel.
     * 
     * @param parent The parent composite
     */
    private void createButtonPanel(Composite parent)
    {
        final GridLayout buttonPanelLayout = new GridLayout();
        buttonPanelLayout.marginHeight = 0;
        buttonPanelLayout.marginWidth = 0;

        final Composite buttonPanel = new Composite(parent, SWT.NONE);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

        final Button addBtn = new Button(buttonPanel, SWT.PUSH);
        addBtn.setText(Messages.getString("UpdateAttributeConfigurationDialog.3")); //$NON-NLS-1$
        addBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        addBtn.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                handleAddOperation(leftViewer.getSelection());
            }
        });

        final Button removeBtn = new Button(buttonPanel, SWT.PUSH);
        removeBtn.setText(Messages.getString("UpdateAttributeConfigurationDialog.4")); //$NON-NLS-1$
        removeBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        removeBtn.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                handleRemoveOperation(rightViewer.getSelection());
            }
        });

        final Button replaceBtn = new Button(buttonPanel, SWT.PUSH);
        replaceBtn.setText(Messages.getString("UpdateAttributeConfigurationDialog.5")); //$NON-NLS-1$
        replaceBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        replaceBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                handleReplaceOperation(leftViewer.getSelection(), rightViewer.getSelection());
            }
        });

        final Button renameBtn = new Button(buttonPanel, SWT.PUSH);
        renameBtn.setText(Messages.getString("UpdateAttributeConfigurationDialog.13")); //$NON-NLS-1$
        renameBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        renameBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                handleRenameOperation(leftViewer.getSelection(), rightViewer.getSelection());
            }
        });

        final Button moveUpBtn = new Button(buttonPanel, SWT.PUSH);
        moveUpBtn.setText(Messages.getString("UpdateAttributeConfigurationDialog.6")); //$NON-NLS-1$
        moveUpBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        moveUpBtn.setImage(RequirementCorePlugin.getImageDescriptor("icons/up.gif").createImage()); //$NON-NLS-1$
        moveUpBtn.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                handleMoveOperation(rightViewer.getSelection(), true);
            }
        });

        final Button moveDownBtn = new Button(buttonPanel, SWT.PUSH);
        moveDownBtn.setText(Messages.getString("UpdateAttributeConfigurationDialog.7")); //$NON-NLS-1$
        moveDownBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        moveDownBtn.setImage(RequirementCorePlugin.getImageDescriptor("icons/down.gif").createImage()); //$NON-NLS-1$
        moveDownBtn.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                handleMoveOperation(rightViewer.getSelection(), false);
            }
        });
    }

    /**
     * Creates the content of the left panel
     * 
     * @param parent The parent composite
     */
    protected void createLeftPanel(Composite parent)
    {
        final Group leftGroup = createGroup(Messages.getString("UpdateAttributeConfigurationDialog.8"), parent); //$NON-NLS-1$
        leftViewer = createTree(leftGroup);
        leftViewer.setInput(workspaceConf);
    }

    /**
     * Creates the content of the right panel.
     * 
     * @param parent The parent composite
     */
    protected void createRightPanel(Composite parent)
    {
        final Group rightGroup = createGroup(Messages.getString("UpdateAttributeConfigurationDialog.9"), parent); //$NON-NLS-1$
        rightViewer = createTree(rightGroup);
        rightViewer.setInput(localConf);
    }

    /**
     * Creates a group contained in a panel.
     * 
     * @param titleGroup The title group
     * @return A group
     */
    private Group createGroup(String titleGroup, Composite parent)
    {
        final Group group = new Group(parent, SWT.NONE);
        group.setText(titleGroup);
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        group.setLayout(new GridLayout());
        return group;
    }

    /**
     * Creates a tree viewer contained in a group of a panel.
     * 
     * @param group An existing SWT Group
     * @return A tree viewer
     */
    private TreeViewer createTree(Group group)
    {
        TreeViewer viewer = new TreeViewer(group, SWT.SINGLE);
        viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        viewer.setContentProvider(new AdapterFactoryContentProvider(RequirementUtils.getAdapterFactory()));
        viewer.setLabelProvider(new CurrentRequirementLabelProvider(RequirementUtils.getAdapterFactory()));
        ViewerFilter[] filters = new ViewerFilter[1];
        filters[0] = new UpdateAttributeConfigurationFilter();
        viewer.setFilters(filters);
        return viewer;
    }

    /**
     * Handles the <b>Add</b> operations.<br>
     * This class is intended to be extended by client.
     * 
     * @param selection The current selection of one of the two viewer.
     */
    protected void handleAddOperation(ISelection selection)
    {
        if (checkSelection(selection))
        {
            Object inSelection = ((IStructuredSelection) selection).getFirstElement();
            if (inSelection instanceof ConfiguratedAttribute)
            {
                // a global attribute cannot be added if the local configuration already contains
                // an attribute with the same name and type.
                ConfiguratedAttribute selected = (ConfiguratedAttribute) inSelection;
                if (checkAttribute(selected))
                {
                    manager.handleAdd(selected);
                }
                else
                {
                    setMessage(Messages.getString("UpdateAttributeConfigurationDialog.10") + selected.getName() + Messages.getString("UpdateAttributeConfigurationDialog.11"), IMessageProvider.WARNING); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }
    }

    /**
     * Checks the presence of an attribute in the local configuration.
     * 
     * @param attribute The configured attribute coming from the global preferences.
     * @return <code>true</code> If this attributes can be added, <code>false</code> otherwise.
     */
    private boolean checkAttribute(ConfiguratedAttribute attribute)
    {
        if (attribute == null || attribute.getName() == null || attribute.getType() == null)
        {
            return false;
        }
        for (ConfiguratedAttribute localAtt : localConf.getListAttributes())
        {
            if (attribute.getName().equals(localAtt.getName()) && attribute.getType().equals(localAtt.getType()))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Handles the <b>Add</b> operations.<br>
     * This class is intended to be extended by client.
     * 
     * @param selection The current selection of one of the two viewer.
     */
    protected void handleRemoveOperation(ISelection selection)
    {
        if (checkSelection(selection))
        {
            Object inSelection = ((IStructuredSelection) selection).getFirstElement();
            if (inSelection instanceof ConfiguratedAttribute)
            {
                manager.handleRemove((ConfiguratedAttribute) inSelection);
            }
        }
    }

    /**
     * Handles the <b>Move</b> operations.<br>
     * This class is intended to be extended by client.
     * 
     * @param selection The current selection of one of the two viewer.
     * @param direction A boolean giving the direction of the move operation.
     */
    protected void handleMoveOperation(ISelection selection, boolean direction)
    {
        if (checkSelection(selection))
        {
            Object inSelection = ((IStructuredSelection) selection).getFirstElement();
            if (inSelection instanceof ConfiguratedAttribute)
            {
                manager.handleMove((ConfiguratedAttribute) inSelection, direction);
            }
        }
    }

    /**
     * Handles the <b>Rename</b> operations.<br>
     * This class is intended to be extended by client.
     * 
     * @param rightSelection The selection done on the left part of the dialog.
     * @param leftSelection The selection done on the right part of the dialog.
     */
    protected void handleRenameOperation(ISelection leftSelection, ISelection rightSelection)
    {
        if (checkSelection(leftSelection) && checkSelection(rightSelection))
        {
            Object rightElement = ((IStructuredSelection) rightSelection).getFirstElement();
            Object leftElement = ((IStructuredSelection) leftSelection).getFirstElement();

            if (rightElement instanceof ConfiguratedAttribute && leftElement instanceof ConfiguratedAttribute)
            {
                ConfiguratedAttribute rightConfiguration = (ConfiguratedAttribute) rightElement;
                ConfiguratedAttribute leftConfiguration = (ConfiguratedAttribute) leftElement;
                if (rightConfiguration.getType().equals(leftConfiguration.getType()))
                {
                    if (checkAttribute(leftConfiguration))
                    {
                        manager.handleRename(leftConfiguration, rightConfiguration);
                    }
                    else
                    {
                        setMessage(
                                Messages.getString("UpdateAttributeConfigurationDialog.10") + leftConfiguration.getName() + Messages.getString("UpdateAttributeConfigurationDialog.11"), IMessageProvider.WARNING); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
                else
                {
                    setMessage(Messages.getString("UpdateAttributeConfigurationDialog.12"), IMessageProvider.ERROR); //$NON-NLS-1$
                }
            }
        }
    }

    /**
     * Handles the <b>Replace</b> operations.<br>
     * This class is intended to be extended by client.
     * 
     * @param rightSelection The selection done on the left part of the dialog.
     * @param leftSelection The selection done on the right part of the dialog.
     */
    protected void handleReplaceOperation(ISelection leftSelection, ISelection rightSelection)
    {
        if (checkSelection(leftSelection) && checkSelection(rightSelection))
        {
            Object rightElement = ((IStructuredSelection) rightSelection).getFirstElement();
            Object leftElement = ((IStructuredSelection) leftSelection).getFirstElement();

            if (rightElement instanceof ConfiguratedAttribute && leftElement instanceof ConfiguratedAttribute)
            {
                ConfiguratedAttribute rightConfiguration = (ConfiguratedAttribute) rightElement;
                ConfiguratedAttribute leftConfiguration = (ConfiguratedAttribute) leftElement;
                if (rightConfiguration.getType().equals(leftConfiguration.getType()))
                {
                    if (rightConfiguration.getType().equals(AttributesType.TEXT) && leftConfiguration.getType().equals(AttributesType.TEXT))
                    {
                        if (checkSubsetAttributes(leftConfiguration.getListValue(), rightConfiguration.getListValue()))
                        {
                            manager.handleReplace(leftConfiguration, rightConfiguration);
                        }
                        else
                        {
                            boolean answer = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), Messages.getString("UpdateAttributeConfigurationDialog.14"), //$NON-NLS-1$
                                    Messages.getString("UpdateAttributeConfigurationDialog.15")); //$NON-NLS-1$
                            if (answer)
                            {
                                manager.handleReplace(leftConfiguration, rightConfiguration);
                            }
                        }
                    }
                }
                else
                {
                    setMessage(Messages.getString("UpdateAttributeConfigurationDialog.12"), IMessageProvider.ERROR); //$NON-NLS-1$
                }
            }

        }

    }

    /**
     * Checks the content of a selection.
     * 
     * @param selection The selection to check
     * @return <code>true</code> if the selection is conformed, <code>false</code> otherwise.
     */
    private boolean checkSelection(ISelection selection)
    {
        // the original message is re set.
        setMessage(Messages.getString("UpdateAttributeConfigurationDialog.2")); //$NON-NLS-1$
        // the selection is checked
        return selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection;
    }

    /**
     * Checks that all attributes contained in <b>subset</b> are present in <b>set</b> list.
     * 
     * @param set Actually, represents the attributes list coming from the left part of the dialog.
     * @param subset Actually represents the attributes list coming from the right part of the dialog.
     * @return <code>true</code> if subset belongs to set, <code>false otherwise</code>
     */
    private boolean checkSubsetAttributes(EList<AttributeValue> set, EList<AttributeValue> subset)
    {
        boolean isSubset = true;
        for (AttributeValue attribute : subset)
        {
            boolean found = false;
            for (AttributeValue attribute2 : set)
            {
                if (attribute.getValue().equals(attribute2.getValue()))
                {
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                isSubset = false;
                break;
            }
        }
        return isSubset;
    }
}
