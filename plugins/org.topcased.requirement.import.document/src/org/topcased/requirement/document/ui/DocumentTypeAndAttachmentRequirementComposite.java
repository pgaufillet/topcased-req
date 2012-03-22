/*****************************************************************************
 * Copyright (c) 2011 Atos.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Anass RADOUANI (Atos) anass.radouani@atos.net - Initial API and implementation
 *
 *****************************************************************************/

package org.topcased.requirement.document.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridData;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.topcased.typesmodel.handler.IniManager;
import org.topcased.typesmodel.model.inittypes.DocumentType;
import org.topcased.typesmodel.model.inittypes.provider.InittypesItemProviderAdapterFactory;
import org.eclipse.swt.widgets.Button;

/**
 * Composite to add a types document and attach a requirement
 */
public class DocumentTypeAndAttachmentRequirementComposite extends Composite
{
    private Text projectNameTextBox;
    private Text projectDescriptionTextBox;
    private ComboViewer inputTypedDocumentComboViewer;
    private NotifyElement notifyElement;
    private Text diagramTextBox;
    private Group groupTypedDocument;
    private Group groupAttachRequirement;
    private Button btnBrowse;
    protected IFile modelAttached;

    /**
     * Create the composite.
     */
    public DocumentTypeAndAttachmentRequirementComposite(Composite parent, int style)
    {
        super(parent, SWT.NONE);
        setLayout(new GridLayout(2, false));

        groupAttachRequirement = new Group(this, SWT.NONE);
        groupAttachRequirement.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        groupAttachRequirement.setLayout(new GridLayout(3, false));

        Label lblDiagram = new Label(groupAttachRequirement, SWT.NONE);
        lblDiagram.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        lblDiagram.setText("Graphical model (*.di)");

        diagramTextBox = new Text(groupAttachRequirement, SWT.BORDER);
        diagramTextBox.setEditable(false);
        diagramTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        btnBrowse = new Button(groupAttachRequirement, SWT.NONE);
        btnBrowse.setText("Browse");

        Label label_1 = new Label(groupAttachRequirement, SWT.NONE);
        label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        label_1.setText("Requirement links prefix : ");

        projectNameTextBox = new Text(groupAttachRequirement, SWT.BORDER);
        projectNameTextBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

        Label label_2 = new Label(groupAttachRequirement, SWT.NONE);
        label_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        label_2.setText("Links optional description : ");

        projectDescriptionTextBox = new Text(groupAttachRequirement, SWT.BORDER);
        projectDescriptionTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        new Label(groupAttachRequirement, SWT.NONE);
        new Label(groupAttachRequirement, SWT.NONE);
        new Label(groupAttachRequirement, SWT.NONE);

        groupTypedDocument = new Group(this, SWT.NONE);
        groupTypedDocument.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        groupTypedDocument.setBounds(0, 0, 70, 81);
        groupTypedDocument.setLayout(new GridLayout(2, false));

        Label label = new Label(groupTypedDocument, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        label.setText("Document type : ");

        inputTypedDocumentComboViewer = new ComboViewer(groupTypedDocument, SWT.READ_ONLY);
        Combo combo = inputTypedDocumentComboViewer.getCombo();
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        hookListeners();
        initViewer();

    }

    /**
     * initialize the combo viewer
     */
    public void initViewer()
    {
        AdapterFactory factory = new InittypesItemProviderAdapterFactory();
        AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(factory);
        inputTypedDocumentComboViewer.setLabelProvider(labelProvider);
        AdapterFactoryContentProvider contentProvider = new AdapterFactoryContentProvider(factory);
        inputTypedDocumentComboViewer.setContentProvider(contentProvider);

        inputTypedDocumentComboViewer.setInput(IniManager.getInstance().getModel());
    }

    private void hookListeners()
    {

        inputTypedDocumentComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {

            public void selectionChanged(SelectionChangedEvent event)
            {
                if (notifyElement != null)
                {
                    notifyElement.handleModelChange();
                }
            }
        });

        btnBrowse.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                IFile[] files = WorkspaceResourceDialog.openFileSelection(getShell(), null, null, false, null, null);
                if (files.length > 0 && files[0] != null )
                {
                    modelAttached = files[0]; //csDialog.getURIs().get(0);
                    diagramTextBox.setText(modelAttached.getLocationURI().toString());
                    notifyElement.handleModelChange();

                }
            }
        });

        diagramTextBox.addModifyListener(new ModifyListener()
        {

            public void modifyText(ModifyEvent e)
            {
                if (notifyElement != null)
                {
                    notifyElement.handleModelChange();
                }
            }
        });

        projectNameTextBox.addModifyListener(new ModifyListener()
        {

            public void modifyText(ModifyEvent e)
            {
                if (notifyElement != null)
                {
                    notifyElement.handleModelChange();
                }
            }
        });

        projectDescriptionTextBox.addModifyListener(new ModifyListener()
        {

            public void modifyText(ModifyEvent e)
            {
                if (notifyElement != null)
                {
                    notifyElement.handleModelChange();
                }
            }
        });

    }

    /**
     * Gets if typed document composite is filled or not
     * 
     * @return
     */
    public boolean isTypedDocumentComplete()
    {
        return !inputTypedDocumentComboViewer.getSelection().isEmpty();
    }

    /**
     * Sets the notifier
     * 
     * @param notifyElement
     */
    public void setNotifyElement(NotifyElement notifyElement)
    {
        this.notifyElement = notifyElement;

    }

    /**
     * Gets the selected document type element
     * 
     * @return
     */
    public DocumentType getSelectedType()
    {
        ISelection selection = inputTypedDocumentComboViewer.getSelection();
        if (!selection.isEmpty() && selection instanceof IStructuredSelection)
        {
            IStructuredSelection selec = (IStructuredSelection) selection;
            return (DocumentType) selec.iterator().next();
        }
        return null ;
    }

    /**
     * Sets if types composite is enabled and visible
     * 
     * @param enabled
     */
    public void setTypedDocumentGroupEnabled(boolean enabled)
    {
        groupTypedDocument.setEnabled(enabled);
        groupTypedDocument.setVisible(enabled);
    }

    /**
     * Sets if Attach Requirement composite is enabled and visible
     * 
     * @param selection
     */
    public void setAttachRequirementGroupEnabled(boolean selection)
    {
        groupAttachRequirement.setEnabled(selection);
        groupAttachRequirement.setVisible(selection);

    }

    /**
     * Gets if Attach Requirement composite is filled or not
     * 
     * @return
     */
    public boolean isAttachReqComplete()
    {
        return !diagramTextBox.getText().isEmpty() && !projectNameTextBox.getText().isEmpty(); 
    }

    /**
     * Gets if there isn't types document in the workspace
     * 
     * @return
     */
    public boolean isTypedDocumentEmpty()
    {
        return inputTypedDocumentComboViewer.getElementAt(0) == null;
    }

    /**
     * Gets the model to attach
     * 
     * @return
     */
    public IFile getModelToAttach()
    {
        return modelAttached;
    }

    /**
     * Gets the project name
     * 
     * @return
     */
    public String getProjectName()
    {
        return projectNameTextBox.getText();
    }

    /**
     * Gets the project Description
     * 
     * @return
     */
    public String getProjectDescription()
    {
        return projectDescriptionTextBox.getText();
    }

}
