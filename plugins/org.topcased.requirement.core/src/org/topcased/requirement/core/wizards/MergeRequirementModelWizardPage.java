/*****************************************************************************
 * Copyright (c) 2008, 2010 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *      Maxime AUDRAIN (CS) : API Changes
 ******************************************************************************/
package org.topcased.requirement.core.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.topcased.requirement.core.extensions.ModelAttachmentPolicyManager;
import org.topcased.requirement.core.extensions.RequirementTransformationManager;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.util.RequirementResource;

import ttm.Document;

/**
 * The "New" wizard page allows setting the container for the new file as well as the file name. The page will only
 * accept file name without the extension OR with the extension that matches the expected one.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class MergeRequirementModelWizardPage extends WizardPage
{

    private static final String DEFAULT_MODEL_NAME = "My"; //$NON-NLS-1$

    private Resource alreadyAttachedRequirement;

    private IStructuredSelection selection;

    /*
     * Groups
     */
    private Group mainGroup;

    private Group docsGroup;

    private Image folderImg;

    private ResourceSet resourceSet = new ResourceSetImpl();

    /*
     * target Model
     */
    private Text modelText;

    /*
     * imported source Model
     */
    private Text importModelFd;

    private Button importModelBt;

    /*
     * Documents list
     */
    List<Button> buttonsCheck = new ArrayList<Button>();

    List<Text> documentsTexts = new ArrayList<Text>();

    List<Button> documentsButtons = new ArrayList<Button>();

    List<Combo> documentsCombos = new ArrayList<Combo>();

    /*
     * requirement model name
     */
    private Text requirementNameFd;

    /*
     * Project
     */
    private Text projectFd;

    private Text projectDescrFd;

    private Button checkAllBt;

    private Button partialImportBt;

    private List<Document> inputDocuments;

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
    public MergeRequirementModelWizardPage(IStructuredSelection selection, Resource alreadyAttachedRequirementResource)
    {
        super("wizardPage"); //$NON-NLS-1$
        this.selection = selection;
        // Is this dialog an attach dialog or a merge dialog?
        setTitle(Messages.getString("RequirementWizardPage.merge.title")); //$NON-NLS-1$
        setDescription(Messages.getString("RequirementWizardPage.merge.desc")); //$NON-NLS-1$

        // Is this diagram already attached?
        if (alreadyAttachedRequirementResource != null)
        {
            alreadyAttachedRequirement = alreadyAttachedRequirementResource;
        }
        else
        {
            alreadyAttachedRequirement = null;
        }
        folderImg = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        Composite mainComp = new Composite(parent, SWT.NONE);
        mainComp.setLayout(new GridLayout());
        mainComp.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
        mainComp.setFont(parent.getFont());

        createDialog(mainComp);

        setErrorMessage(null);
        setMessage(null);
        setControl(mainComp);

        setPageComplete(validatePage());

        hookListeners();

        initialize();

        getShell().setMinimumSize(mainComp.getSize());
        getShell().pack();
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
        mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));

        /*
         * Selection of the existing model file
         */
        Label modelLbl = new Label(mainGroup, SWT.NONE);
        modelLbl.setText(Messages.getString("RequirementWizardPage.6")); //$NON-NLS-1$

        modelText = new Text(mainGroup, SWT.BORDER);
        modelText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
        modelText.setEditable(false);

        /**
         * Documents group
         */
        docsGroup = new Group(mainGroup, SWT.NONE);
        docsGroup.setText(Messages.getString("RequirementWizardPage.26"));
        docsGroup.setLayout(new GridLayout(4, false));
        docsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));

        /*
         * Selection of the requirement to import for all documents
         */
        checkAllBt = new Button(docsGroup, SWT.CHECK);
        checkAllBt.setText(Messages.getString("RequirementWizardPage.25"));
        checkAllBt.setToolTipText(Messages.getString("RequirementWizardPage.tooltip.selectAll.chk"));
        checkAllBt.setSelection(true);

        importModelFd = new Text(docsGroup, SWT.BORDER);
        importModelFd.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
        importModelFd.setEditable(false);

        importModelBt = new Button(docsGroup, SWT.PUSH);
        importModelBt.setImage(folderImg);
        importModelBt.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false, 2, 1));
        importModelBt.setToolTipText(Messages.getString("RequirementWizardPage.tooltip.selectAll.browse")); //$NON-NLS-1$

        Label separatorDocs = new Label(docsGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
        separatorDocs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));

        Label initialDocLbl = new Label(docsGroup, SWT.NONE);
        initialDocLbl.setText(Messages.getString("RequirementWizardPage.22"));

        Label requirementModelLbl = new Label(docsGroup, SWT.NONE);
        requirementModelLbl.setText(Messages.getString("RequirementWizardPage.23"));
        requirementModelLbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

        Label newDocument = new Label(docsGroup, SWT.NONE);
        newDocument.setText(Messages.getString("RequirementWizardPage.24"));
        
        inputDocuments = RequirementUtils.getUpstreamDocuments(alreadyAttachedRequirement);

        for (Document d : inputDocuments)
        {
            // Don't take deleted documents
            if (!RequirementUtils.isDeletedDocumentIdent(d.getIdent()))
            {
                // Check button
                Button docBt = new Button(docsGroup, SWT.CHECK);
                docBt.setText(d.getIdent());
                docBt.setSelection(true);
                docBt.addSelectionListener(selectionListener);
                docBt.setToolTipText(Messages.getString("RequirementWizardPage.tooltip.initialDocument"));
                buttonsCheck.add(docBt);
                // Requirement file
                Text t = new Text(docsGroup, SWT.BORDER);
                t.setEditable(false);
                documentsTexts.add(t);
                Button b = new Button(docsGroup, SWT.PUSH);
                b.setImage(folderImg);
                b.setToolTipText(Messages.getString("RequirementWizardPage.tooltip.requirementModel"));
                documentsButtons.add(b);
                // Document choice
                Combo c = new Combo(docsGroup, SWT.READ_ONLY);
                c.setToolTipText(Messages.getString("RequirementWizardPage.tooltip.newDocument"));
                documentsCombos.add(c);
            }
        }

        /*
         * Model requirement name
         */
        Label nameLbl = new Label(mainGroup, SWT.NONE);
        nameLbl.setText(Messages.getString("RequirementWizardPage.8")); //$NON-NLS-1$

        requirementNameFd = new Text(mainGroup, SWT.BORDER);
        requirementNameFd.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
        if (alreadyAttachedRequirement != null)
        {
            requirementNameFd.setEditable(false);
        }

        /*
         * Project requirement name
         */
        Label projectLbl = new Label(mainGroup, SWT.NONE);
        projectLbl.setText(Messages.getString("RequirementWizardPage.9")); //$NON-NLS-1$

        projectFd = new Text(mainGroup, SWT.BORDER);
        projectFd.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));

        /*
         * Projects requirement description
         */
        Label projectDescrLbl = new Label(mainGroup, SWT.NONE);
        projectDescrLbl.setText(Messages.getString("RequirementWizardPage.10")); //$NON-NLS-1$

        projectDescrFd = new Text(mainGroup, SWT.BORDER);
        projectDescrFd.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));

        Label separatorImport = new Label(mainGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
        separatorImport.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

        /*
         * Partial import
         */
        partialImportBt = new Button(mainGroup, SWT.CHECK);
        partialImportBt.setText(Messages.getString("RequirementWizardPage.21")); //$NON-NLS-1$
        partialImportBt.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 3, 1));
    }

    /**
     * Adds the listener to the widgets
     */
    private void hookListeners()
    {
        checkAllBt.addSelectionListener(selectionListener);
        importModelFd.addModifyListener(validationListener);
        requirementNameFd.addModifyListener(validationListener);
        projectFd.addModifyListener(validationListener);

        checkAllBt.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                for (Button b : buttonsCheck)
                {
                    b.setSelection(checkAllBt.getSelection());
                }
                dialogChanged();
            }
        });

        importModelBt.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                handleImportModelChoose();
                dialogChanged();
            }
        });

        // Check document buttons
        for (final Button b : buttonsCheck)
        {
            b.addSelectionListener(new SelectionAdapter()
            {
                public void widgetSelected(SelectionEvent e)
                {
                    if (!b.getSelection())
                    {
                        checkAllBt.setSelection(false);
                    }
                    dialogChanged();
                }
            });
        }

        // Documents buttons
        for (final Button b : documentsButtons)
        {
            b.addSelectionListener(new SelectionAdapter()
            {
                public void widgetSelected(SelectionEvent e)
                {
                    int index = documentsButtons.indexOf(b);
                    handleDocumentRequirementChoose(index, buttonsCheck.get(index).getText());
                    dialogChanged();
                }
            });
        }

        // Combos
        for (final Combo c : documentsCombos)
        {
            c.addSelectionListener(new SelectionAdapter()
            {
                public void widgetSelected(SelectionEvent e)
                {
                    dialogChanged();
                }
            });
        }

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

    private void handleDocumentRequirementChoose(int index, String documentToUpdate)
    {
        ResourceSelectionDialog dialog = new ResourceSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), Messages.getString("RequirementWizardPage.12")); //$NON-NLS-1$
        if (dialog.open() == Window.OK)
        {
            Object[] results = dialog.getResult();

            if (results.length == 1 && results[0] instanceof IFile)
            {
                Combo c = documentsCombos.get(index);
                c.removeAll();
                IFile f = (IFile) results[0];
                documentsTexts.get(index).setText(f.getFullPath().toString());
                List<Document> documents = getDocuments(f);
                addCombosTextsAndSelection(documentToUpdate, c, documents);
            }
        }
    }

    /**
     * Get all documents for the file
     * 
     * @param file
     * @return all documents
     */
    private List<Document> getDocuments(IFile file)
    {
        Resource r = getResource(file.getFullPath().toString());
        return RequirementUtils.getUpstreamDocuments(r);
    }

    /**
     * Add the combos texts
     * 
     * @param documentToUpdate , the name of the document to update
     * @param c , the combo to update
     * @param documents the documents list of selected resource
     */
    private void addCombosTextsAndSelection(String documentToUpdate, Combo c, List<Document> documents)
    {
        for (int i = 0; i < documents.size(); i++)
        {
            if (!RequirementUtils.isDeletedDocumentIdent(documents.get(i).getIdent()))
            {
                c.add(documents.get(i).getIdent());
                if (documents.get(i).getIdent().equals(documentToUpdate))
                {
                    c.select(i);
                }
            }
        }
    }

    /**
     * Handles the imported model choice
     */
    protected void handleImportModelChoose()
    {
        ResourceSelectionDialog dialog = new ResourceSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), Messages.getString("RequirementWizardPage.12")); //$NON-NLS-1$
        if (dialog.open() == Window.OK)
        {
            Object[] results = dialog.getResult();

            if (results.length == 1 && results[0] instanceof IFile)
            {
                importModelFd.setText(((IFile) results[0]).getFullPath().toString());
                changeAllDocumentsFile((IFile) results[0]);
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
                String targetModelFile = ((IResource) obj).getFullPath().toString();
                modelText.setText(targetModelFile);
            }

            if (alreadyAttachedRequirement == null)
            {
                name = ((IFile) obj).getLocation().removeFileExtension().lastSegment();
            }
            else
            {
                name = alreadyAttachedRequirement.getURI().trimFileExtension().lastSegment();
                Path path = new Path(alreadyAttachedRequirement.getURI().toPlatformString(true));
                importModelFd.setText(path.toString());
                changeAllDocumentsFile(ResourcesPlugin.getWorkspace().getRoot().getFile(path));
            }
        }
        requirementNameFd.setText(name);
        setPageComplete(false);
    }

    /**
     * If I select a file in the "select all document", all documents file must be change with this file
     * 
     * @param file , the file to change
     */
    private void changeAllDocumentsFile(IFile file)
    {
        for (Text t : documentsTexts)
        {
            t.setText(file.getFullPath().toString());
        }
        for (int i = 0; i < documentsCombos.size(); i++)
        {
            documentsCombos.get(i).removeAll();
            addCombosTextsAndSelection(buttonsCheck.get(i).getText(), documentsCombos.get(i), getDocuments(file));
        }
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

        if (getTargetModelFile() != null)
        {
            if (!ModelAttachmentPolicyManager.getInstance().isEnableFor(getTargetModelFile().getFileExtension()))
            {
                if (!getTargetModelFile().getFileExtension().endsWith("di")) //$NON-NLS-1$
                {
                    message = Messages.getString("RequirementWizardPage.1"); //$NON-NLS-1$
                }
            }
        }
        else if (newModel.length() == 0)
        {
            message = Messages.getString("RequirementWizardPage.13"); //$NON-NLS-1$
        }

        if (getSourceModelFile() != null)
        {
            if (!RequirementTransformationManager.getInstance().isEnableFor(getSourceModelFile().getFileExtension()))
            {
                if (!getSourceModelFile().getFileExtension().equals(RequirementResource.FILE_EXTENSION))
                {
                    message = Messages.getString("RequirementWizardPage.19"); //$NON-NLS-1$
                }
            }
        }
        String path = "";
        if (alreadyAttachedRequirement != null)
        {
            path = new Path(alreadyAttachedRequirement.getURI().toPlatformString(true)).toString();
        }

        if (importModelFd.getText().equals(path) && !"".equals(path)) //$NON-NLS-1$
        {
            setMessage(Messages.getString("RequirementWizardPage.20"), INFORMATION); //$NON-NLS-1$
        }
        else if (getMessageType() == INFORMATION)
        {
            setMessage(null);
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
        if (getErrorMessage() != null)
        {
            return false;
        }
        boolean allButtonUnchecked = true;
        for (Button b : buttonsCheck)
        {
            if (b.getSelection())
            {
                if ("".equals(documentsTexts.get(buttonsCheck.indexOf(b)).getText()))
                {
                    return false;
                }
                if (documentsCombos.get(buttonsCheck.indexOf(b)).getSelectionIndex() == -1)
                {
                    return false;
                }
                allButtonUnchecked = false;
            }
        }
        if (allButtonUnchecked)
        {
            return false;
        }
        return true;
    }

    /**
     * Gets the imported file containing the upstream requirements
     * 
     * @return IFile : the import file
     */
    public IFile getSourceModelFile()
    {
        Path path = new Path(getImportModelFd());
        if (path.getFileExtension() != null)
        {
            return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
        }
        return null;
    }

    /**
     * Gets the target model file
     * 
     * @return IFile : the import file
     */
    public IFile getTargetModelFile()
    {
        Path path = new Path(getTargetModelFd());
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
        IPath pathModelSource = new Path(getTargetModelFd());
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
     * The getter of the name of the target model selected
     * 
     * @return the name of the target model selected
     */
    protected String getTargetModelFd()
    {
        return modelText.getText();
    }

    /**
     * The getter of the name of the imported model selected
     * 
     * @return the name of the imported model selected
     */
    protected String getImportModelFd()
    {
        return importModelFd.getText();
    }

    /**
     * Return if the update is partial
     * 
     * @return true if is partial import
     */
    protected boolean isPartialImport()
    {
        return partialImportBt.getSelection();
    }

    /**
     * Return all documents to merge, key are older documents and value new documents
     * 
     * @return documents to merge
     */
    protected Map<Document, Document> getDocumentsToMerge()
    {
        Map<Document, Document> docsToMerge = new HashMap<Document, Document>();
        for (Button b : buttonsCheck)
        {
            if (b.getSelection())
            {
                int index = buttonsCheck.indexOf(b);
                String fileToMerge = documentsTexts.get(index).getText();
                List<Document> docs = RequirementUtils.getUpstreamDocuments(getResource(fileToMerge));
                for (Document d : docs)
                {
                    if (d.getIdent().equals(documentsCombos.get(index).getText()))
                    {
                        docsToMerge.put(inputDocuments.get(index), d);
                    }
                }
            }
        }
        return docsToMerge;
    }

    /**
     * Get the resource for the file
     * 
     * @param file
     * @return file resource
     */
    private Resource getResource(String file)
    {
        URI fileURI = URI.createPlatformResourceURI(file, true);
        // ensure resource is not the original req one before loading it
        if (alreadyAttachedRequirement.getURI().equals(fileURI))
        {
            return alreadyAttachedRequirement;
        }
        return resourceSet.getResource(fileURI, true);
    }

}