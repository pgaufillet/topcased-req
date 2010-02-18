/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.topcased.requirement.core.Messages;

/**
 * The "New" wizard page allows setting the container for the new file as well as the file name. The page will only
 * accept file name without the extension OR with the extension that matches the expected one.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class RequirementWizardPage extends WizardPage
{

    private static final String BROWSE_TEXT = "..."; //$NON-NLS-1$

    private static final String DEFAULT_MODEL_NAME = "My"; //$NON-NLS-1$

    private IStructuredSelection selection;

    private Group mainGroup;

    /*
     * Model SAM
     */
    private Text modelSAMFd;

    private Button modelSAMBt;

    /*
     * Model Ttm
     */
    private Text importTtmFd;

    private Button importTtmBt;

    /*
     * requirement model name
     */
    private Text requirementNameFd;

    private Button emptySource;

    /*
     * Project
     */
    private Text projectFd;

    private Text projectDescrFd;

    private ModifyListener validationListener = new ModifyListener()
    {
        public void modifyText(ModifyEvent e)
        {
            setPageComplete(validatePage());
        }
    };

    private SelectionListener selectionListener = new SelectionAdapter()
    {
        public void widgetSelected(SelectionEvent e)
        {
            setPageComplete(validatePage());
        }
    };

    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param pageName
     */
    public RequirementWizardPage(IStructuredSelection selection)
    {
        super("wizardPage"); //$NON-NLS-1$
        setTitle(Messages.getString("RequirementWizardPage.4")); //$NON-NLS-1$
        setDescription(Messages.getString("RequirementWizardPage.5")); //$NON-NLS-1$
        this.selection = selection;
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        Composite mainComp = new Composite(parent, SWT.NONE);
        mainComp.setLayout(new GridLayout());
        mainComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        mainComp.setFont(parent.getFont());

        createDialog(mainComp);

        setErrorMessage(null);
        setMessage(null);
        setControl(mainComp);

        setPageComplete(validatePage());

        hookListeners();

        initialize();
    }

    /**
     * Creates the dialog of the requirement model wizard
     * 
     * @param parent The composite parent
     */
    private void createDialog(Composite parent)
    {
        mainGroup = new Group(parent, SWT.NONE);
        mainGroup.setLayout(new GridLayout(3, false));
        mainGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

        /*
         * Selection of the existing model file
         */
        Label modelLbl = new Label(mainGroup, SWT.NONE);
        modelLbl.setText(Messages.getString("RequirementWizardPage.6")); //$NON-NLS-1$

        modelSAMFd = new Text(mainGroup, SWT.BORDER);
        modelSAMFd.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        modelSAMFd.setEditable(false);

        modelSAMBt = new Button(mainGroup, SWT.PUSH);
        modelSAMBt.setText(BROWSE_TEXT);

        /*
         * Selection of the upsteam requirement to import
         */
        Label importLbl = new Label(mainGroup, SWT.NONE);
        importLbl.setText(Messages.getString("RequirementWizardPage.7")); //$NON-NLS-1$

        importTtmFd = new Text(mainGroup, SWT.BORDER);
        importTtmFd.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        importTtmFd.setEditable(false);

        importTtmBt = new Button(mainGroup, SWT.PUSH);
        importTtmBt.setText(BROWSE_TEXT);

        /*
         * Model requirement name
         */
        Label nameLbl = new Label(mainGroup, SWT.NONE);
        nameLbl.setText(Messages.getString("RequirementWizardPage.8")); //$NON-NLS-1$

        requirementNameFd = new Text(mainGroup, SWT.BORDER);
        GridData layoutNameFd = new GridData(GridData.FILL_HORIZONTAL);
        layoutNameFd.horizontalSpan = 2;
        requirementNameFd.setLayoutData(layoutNameFd);

        /*
         * Project requirement name
         */
        Label projectLbl = new Label(mainGroup, SWT.NONE);
        projectLbl.setText(Messages.getString("RequirementWizardPage.9")); //$NON-NLS-1$

        projectFd = new Text(mainGroup, SWT.BORDER);
        GridData layoutprojectFd = new GridData(GridData.FILL_HORIZONTAL);
        layoutprojectFd.horizontalSpan = 2;
        projectFd.setLayoutData(layoutprojectFd);

        /*
         * Projects requirement description
         */
        Label projectDescrLbl = new Label(mainGroup, SWT.NONE);
        projectDescrLbl.setText(Messages.getString("RequirementWizardPage.10")); //$NON-NLS-1$

        projectDescrFd = new Text(mainGroup, SWT.BORDER);
        GridData layoutprojectDescrFd = new GridData(GridData.FILL_HORIZONTAL);
        layoutprojectDescrFd.horizontalSpan = 2;
        projectDescrFd.setLayoutData(layoutprojectDescrFd);

        emptySource = new Button(mainGroup, SWT.CHECK);
        emptySource.setText(Messages.getString("RequirementWizardPage.0")); //$NON-NLS-1$
    }

    /**
     * Adds the listener to the widgets
     */
    private void hookListeners()
    {
        modelSAMFd.addModifyListener(validationListener);
        importTtmFd.addModifyListener(validationListener);
        requirementNameFd.addModifyListener(validationListener);
        projectFd.addModifyListener(validationListener);
        emptySource.addSelectionListener(selectionListener);

        modelSAMBt.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                handleSAMModelChoose();
                dialogChanged();
            }
        });

        importTtmBt.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                handleTtmModelChoose();
            }
        });

        requirementNameFd.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
        projectFd.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
    }

    /**
     * Handles the model choice
     */
    protected void handleSAMModelChoose()
    {
        ResourceSelectionDialog dialog = new ResourceSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), Messages.getString("RequirementWizardPage.12")); //$NON-NLS-1$
        if (dialog.open() == Window.OK)
        {
            Object[] results = dialog.getResult();

            if (results.length == 1 && results[0] instanceof IFile)
            {
                modelSAMFd.setText(((IFile) results[0]).getFullPath().toString());

            }
        }
    }

    /**
     * Handles the ttm model choice
     */
    protected void handleTtmModelChoose()
    {
        ResourceSelectionDialog dialog = new ResourceSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), Messages.getString("RequirementWizardPage.12")); //$NON-NLS-1$
        if (dialog.open() == Window.OK)
        {
            Object[] results = dialog.getResult();

            if (results.length == 1 && results[0] instanceof IFile)
            {
                importTtmFd.setText(((IFile) results[0]).getFullPath().toString());

            }
        }
    }

    /**
     * Tests if the current workbench selection is a suitable container to use.
     */
    protected void initialize()
    {
        String name = DEFAULT_MODEL_NAME;

        if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection)
        {
            IStructuredSelection ssel = (IStructuredSelection) selection;
            if (ssel.size() > 1)
            {
                return;
            }
            Object obj = ssel.getFirstElement();
            if (obj instanceof IFile)
            {
                name = ((IFile) obj).getLocation().removeFileExtension().lastSegment();
                modelSAMFd.setText(((IResource) obj).getFullPath().toString());
            }
        }
        setPageComplete(false);
        requirementNameFd.setText(name);
    }

    /**
     * Ensures that both text fields are set.
     */
    protected void dialogChanged()
    {
        /*
         * Control the name of the model requirement
         */
        String newModel = getRequirementNameFd();
        String message = null;
        if (getModelFile() != null && !getModelFile().getFileExtension().equals("sam")) //$NON-NLS-1$
        {
            message = Messages.getString("RequirementWizardPage.1"); //$NON-NLS-1$
        }
        else if (newModel.length() == 0)
        {
            message = Messages.getString("RequirementWizardPage.13"); //$NON-NLS-1$
        }

        if (message == null)
        {
            /*
             * Control the project name
             */
            String project = getProjectFd();
            if (project.length() == 0)
            {
                message = Messages.getString("RequirementWizardPage.18"); //$NON-NLS-1$
            }
        }

        setErrorMessage(message);
        setPageComplete(validatePage());
    }

    /**
     * Validate the page wizard
     * 
     * @return true or false
     */
    protected boolean validatePage()
    {
        Boolean result;

        if (getErrorMessage() != null)
        {
            result = false;
        }
        else
        {
            if ((getModelSAMFd() == null) || ((getImportTtmFd() == null) && (!emptySource.getSelection())) || (getRequirementNameFd() == null))
            {
                result = false;
            }
            else
            {
                if (getModelSAMFd().length() > 0 && ((emptySource.getSelection()) || ((getImportTtmFd().length() > 0) && (!emptySource.getSelection()))) && getRequirementNameFd().length() > 0)
                {
                    result = true;
                }
                else
                {
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * Gets the imported file containing the upstream requirements
     * 
     * @return IFile : the import file
     */
    public IFile getSourceModelFile()
    {
        return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(getImportTtmFd()));
    }

    /**
     * Gets the SAM model
     * 
     * @return IFile : the import file
     */
    public IFile getModelFile()
    {
        Path path = new Path(getModelSAMFd());
        if (path.getFileExtension() != null)
        {
            return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
        }
        return null;
    }

    /**
     * Gets the requirement model to create
     * 
     * @return IFile : the destination requirement model
     */
    public IFile getDestModelFile()
    {
        IPath pathModelSource = new Path(getModelSAMFd());
        IPath pathNewModel = new Path(pathModelSource.removeLastSegments(1).toString());
        return ResourcesPlugin.getWorkspace().getRoot().getFile(pathNewModel.append(getRequirementNameFd()));
    }

    /**
     * The getter of the project text
     * 
     * @return the project
     */
    public String getProjectFd()
    {
        return projectFd.getText();
    }

    /**
     * The setter of the project text
     * 
     * @param value The value to affect to this field
     */
    public void setProjectFd(String value)
    {
        projectFd.setText(value);
    }

    /**
     * The getter of the description text
     * 
     * @return the description
     */
    public String getProjectDescrFd()
    {
        return projectDescrFd.getText();
    }

    /**
     * The setter of the description text
     * 
     * @param value The value to affect to this field
     */
    public void setProjectDescrFd(String value)
    {
        projectDescrFd.setText(value);
    }

    /**
     * The getter of the requirement model to create
     * 
     * @return the name of the requirement model
     */
    protected String getRequirementNameFd()
    {
        return requirementNameFd.getText();
    }

    /**
     * The getter of the name of the SAM model selected
     * 
     * @return the name of the SAM model selected
     */
    protected String getModelSAMFd()
    {
        return modelSAMFd.getText();
    }

    /**
     * The getter of the name of the Ttm model selected
     * 
     * @return the name of the Ttm model selected
     */
    protected String getImportTtmFd()
    {
        return importTtmFd.getText();
    }

    /**
     * The getter of the emptySource attribute
     * 
     * @return false if no model to import, otherwise true
     */
    public Boolean getEmptySource()
    {
        return emptySource.getSelection();
    }
    
    /**
     * The getter of the checkbox.
     * 
     * @return false if no model to import, otherwise true
     */
    public Button getButton()
    {
        return emptySource;
    }

}