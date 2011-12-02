/*****************************************************************************
 * Copyright (c) 2009 ATOS ORIGIN INTEGRATION.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Caroline Bourdeu d'Aguerre (ATOS ORIGIN INTEGRATION) caroline.bourdeudaguerre@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.document.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.wb.swt.SWTResourceManager;
import org.topcased.requirement.document.Activator;
import org.topcased.requirement.document.component.ComponentHelp;
import org.topcased.requirement.document.component.ComponentHelpCheckButton;
import org.topcased.requirement.document.component.ComponentHelpRadioButtonModel;
import org.topcased.requirement.document.component.ComponentHelpTextField;
import org.topcased.requirement.document.component.ComponentHelpTextFieldButton;
import org.topcased.requirement.document.utils.Constants;
import org.topcased.requirement.document.utils.Messages;
import org.topcased.typesmodel.model.inittypes.DocumentType;

/**
 * The Class ImportRequirementWizardPageSelectDocument.
 */
public class ImportRequirementWizardPageSelectDocument extends WizardPage implements NotifyElement
{

    /** The Constants PREFERENCE. */
    public static final String PREFERENCE_FOR_INPUT_DOC = "inputdoc_reqimport"; //$NON-NLS-1$

    /** The Constant PREFERENCE_FOR_OUTPUT_MODEL. */
    public static final String PREFERENCE_FOR_OUTPUT_MODEL = "outputmodel_reqimport"; //$NON-NLS-1$

    /** The Constant PREFERENCE_FOR_LEVEL. */
    public static final String PREFERENCE_FOR_LEVEL = "level_reqimport"; //$NON-NLS-1$

    /** The Constant PREFERENCE_FOR_STEREO. */
    public static final String PREFERENCE_FOR_STEREO = "stereo_reqimport"; //$NON-NLS-1$

    /** The Constant PREFERENCE_FOR_PROFILE. */
    public static final String PREFERENCE_FOR_PROFILE = "profile_reqimport"; //$NON-NLS-1$

    /** The Constant PREFERENCE_FOR_MODEL_TYPE. */
    public static final String PREFERENCE_FOR_MODEL_TYPE = "model_type"; //$NON-NLS-1$

    /** The composite. */
    private Composite composite;

    /** The workbench. */
    private IWorkbench workbench = PlatformUI.getWorkbench();

    /** The toolkit. */
    private FormToolkit toolkit;

    /** The form. */
    private Form form;

    /** The section. */
    private Composite section;

    // Page Components
    /** The input document component. */
    private ComponentHelpTextFieldButton inputDocumentComponent;

    /** The output model component. */
    private ComponentHelpTextFieldButton outputModelComponent;

    /** The level component. */
    private ComponentHelpTextField levelComponent;

    /** The stereotype component. */
    private ComponentHelpTextFieldButton stereotypeComponent;

    /** The radio button model type. */
    private ComponentHelpRadioButtonModel radioButtonModelType;

    // Inputs
    /** The input document. */
    private String inputDocument = null;

    /** The output model. */
    private String outputModel;

    /** The level. */
    private String level;

    /** The stereotype. */
    private Stereotype stereotype;

    /** The profile. */
    private Profile profile;

    /** The profile uri. */
    private String profileURI;

    /** The old model type. */
    private String oldModelType;

    /** The model type. */
    private String modelType;

    /** The button delete. */
    private Button buttonDelete;

    // Value load from preferences
    /** The input document from pref. */
    private String inputDocumentFromPref;

    /** The output model from pref. */
    private String outputModelFromPref;

    /** The model type from pref. */
    private String modelTypeFromPref;

    /** The profile uri from pref. */
    private String profileURIFromPref;

    /** The stereotype from pref. */
    private String stereotypeFromPref;
    
    /** The check button to input typed documents */
    private ComponentHelpCheckButton inputTypedDocuments;
    
    /** Composite to input typed documents */
    private DocumentTypeAndAttachmentRequirementComposite typedDocumentsComposite;

    /** flag for initialization */
	private boolean init = true;
	
	/** help check button component for requirement */
    private ComponentHelpCheckButton attachRequirement;

    /**
     * Instantiates a new import requirement wizard page select document.
     * 
     * @param pageName the page name
     * @param valueForInput the value for input
     */
    protected ImportRequirementWizardPageSelectDocument(String pageName, String valueForInput)
    {
        super(pageName);
        inputDocument = valueForInput;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
    	init = true;
        composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new FillLayout());
        setImageDescriptor(workbench.getSharedImages().getImageDescriptor(Wizard.DEFAULT_IMAGE));
        this.setDescription("Select an input document and an output model"); //$NON-NLS-1$
        toolkit = new FormToolkit(composite.getDisplay());
        form = toolkit.createForm(composite);
        // create the base form
        form.setText("Requirements Import"); //$NON-NLS-1$
        toolkit.decorateFormHeading(form);
        FillLayout layout = new FillLayout();
        layout.type = SWT.VERTICAL;
        layout.spacing = 5;
        layout.marginHeight = 5;
        layout.marginWidth = 5;
        form.getBody().setLayout(layout);
        createSection();

        setControl(composite);
        init = false;

    }



    /**
     * Creates the section.
     */
    private void createSection()
    {
        section = createSection(form, "Select document", 2); //$NON-NLS-1$
        createRowForInputFile(section);
        createRowForOutputModel(section);
        createRowForLevel(section);
        createRowForStererotype(section);
        createImportTypedDocumentAndAttachReq(section);
        loadPreferences();
    }

    /**
     * Create composite to attach requirement and initialize the wizard with types document
     *  
     * @param composite
     */
    private void createImportTypedDocumentAndAttachReq(Composite composite)
    {
        
        String helpText = "<form><p>Check this option if you want to attach the requirement model to a diagram</p></form>";
        attachRequirement =  new ComponentHelpCheckButton(new NotifyElement(){
            public void handleModelChange()
            {
                typedDocumentsComposite.setAttachRequirementGroupEnabled(attachRequirement.getSelection());
                outputModelComponent.setButtonEnable(!attachRequirement.getSelection());
                outputModelComponent.setEnabled(!attachRequirement.getSelection());
                radioButtonModelType.setEnabled(!attachRequirement.getSelection());
                if (attachRequirement.getSelection())
                {
                    outputModelComponent.setTextForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
                    radioButtonModelType.setselection(0);
                } else {
                    outputModelComponent.setTextForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
                }
                ImportRequirementWizardPageSelectDocument.this.handleModelChange();
            }
        }, composite, toolkit, SWT.NONE, helpText);
        attachRequirement.setValueText("Attach Requirement");
        attachRequirement.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false, true,1,1));
        
        helpText = "<form><p>Check this option if you want to initialize the wizard with types file (.types)</p></form>";
        inputTypedDocuments = new ComponentHelpCheckButton(new NotifyElement()
        {
            
            public void handleModelChange()
            {
                typedDocumentsComposite.setTypedDocumentGroupEnabled(inputTypedDocuments.getSelection());
                ImportRequirementWizardPageSelectDocument.this.handleModelChange();
            }
        }, composite, toolkit, SWT.NONE, helpText); // toolkit.createButton(composite, "Input typed documents", SWT.CHECK);
        inputTypedDocuments.setValueText("Input typed documents");
        inputTypedDocuments.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false, true,1,1));
        
        typedDocumentsComposite = new DocumentTypeAndAttachmentRequirementComposite(composite, SWT.NONE);
        typedDocumentsComposite.setNotifyElement(this);
        typedDocumentsComposite.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
        
        if (typedDocumentsComposite.isTypedDocumentEmpty())
        {
            inputTypedDocuments.setEnabled(false);
            inputTypedDocuments.setVisible(false);
        }
        
        typedDocumentsComposite.setTypedDocumentGroupEnabled(false);
        typedDocumentsComposite.setAttachRequirementGroupEnabled(false);
    }

    /**
     * Load preferences.
     */
    private void loadPreferences()
    {
        // Get InputDocument
        if (inputDocument == null || inputDocument.length() == 0)
        {
            inputDocumentFromPref = Activator.getDefault().getPluginPreferences().getString(PREFERENCE_FOR_INPUT_DOC);
            inputDocument = inputDocumentFromPref;
        }
        if (inputDocument != null && inputDocument.length() > 0)
        {
            inputDocumentComponent.setValueText(inputDocument, true);
        }

        // Get Output Model
        outputModelFromPref = Activator.getDefault().getPluginPreferences().getString(PREFERENCE_FOR_OUTPUT_MODEL);
        if (outputModelFromPref != null && outputModelFromPref.length() > 0)
        {
            outputModel = outputModelFromPref;
            outputModelComponent.setValueText(outputModel, true);
        }

        // Get the output model type
        modelTypeFromPref = Activator.getDefault().getPluginPreferences().getString(PREFERENCE_FOR_MODEL_TYPE);
        if (modelTypeFromPref != null && modelTypeFromPref.length() > 0)
        {
            oldModelType = modelTypeFromPref;
            modelType = modelTypeFromPref;
            if (Constants.REQUIREMENT_EXTENSION.equals(modelType))
            {
                radioButtonModelType.setselection(0);
            }
            else if (Constants.UML_EXTENSION.equals(modelType))
            {
                radioButtonModelType.setselection(1);
            }
            else
            {
                radioButtonModelType.setselection(2);
            }
        }

        // Get Level
        level = Activator.getDefault().getPluginPreferences().getString(PREFERENCE_FOR_LEVEL);
        if (level != null && level.length() > 0)
        {
            levelComponent.setValueText(level);
        }

        // Get Stereotype
        // Load Profile
        profileURIFromPref = Activator.getDefault().getPluginPreferences().getString(PREFERENCE_FOR_PROFILE);
        if (profileURIFromPref != null && profileURIFromPref.length() > 0)
        {
            profileURI = profileURIFromPref;
            this.profile = loadProfile(profileURI);
            // Load Stereotype
            if (this.profile != null)
            {
                stereotypeFromPref = Activator.getDefault().getPluginPreferences().getString(PREFERENCE_FOR_STEREO);
                if (stereotypeFromPref != null && stereotypeFromPref.length() > 0)
                {
                    this.stereotype = this.profile.getOwnedStereotype(stereotypeFromPref);
                    if (this.stereotype != null)
                    {
                        stereotypeComponent.setValueText(this.stereotype.getName());
                    }
                }
            }
        }
    }

    /**
     * Load profile.
     * 
     * @param uriText the uri text
     * 
     * @return the profile
     */
    protected Profile loadProfile(String uriText)
    {
        URI uri = URI.createURI(uriText);
        EObject eobject = (EObject) EcoreUtil.getObjectByType(new ResourceSetImpl().getResource(uri, true).getContents(), UMLPackage.Literals.PROFILE);
        return (Profile) eobject;
    }

    /**
     * Creates the section.
     * 
     * @param mform the mform
     * @param title the title
     * @param numColumns the num columns
     * 
     * @return the composite
     */
    private Composite createSection(Form mform, String title, int numColumns)
    {
        Section section = toolkit.createSection(mform.getBody(), Section.TITLE_BAR | Section.EXPANDED);
        section.setText(title);
        Composite client = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 1;
        layout.marginHeight = 1;
        layout.numColumns = numColumns;
        layout.makeColumnsEqualWidth = false;
        client.setLayout(layout);
        section.setClient(client);
        return client;
    }

    /**
     * Creates the row for Input file.
     * 
     * @param composite the composite
     */
    private void createRowForInputFile(Composite composite)
    {
        toolkit.createLabel(composite, "Input Document: "); //$NON-NLS-1$
        String helpText = Messages.ImportRequirementWizardPageSelectDocument_INPUT_DOCUMENT;
        inputDocumentComponent = new ComponentHelpTextFieldButton(this, composite, toolkit, SWT.NONE, helpText, SWT.OPEN, ".*\\.docx|.*\\.odt|.*\\.csv|.*\\.xlsx|.*\\.ods"); //$NON-NLS-1$
        fill(inputDocumentComponent);
    }

    /**
     * Fill.
     * 
     * @param componentHelpTextFieldButton the component help text field button
     */
    private void fill(ComponentHelp componentHelpTextFieldButton)
    {
        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        componentHelpTextFieldButton.setLayoutData(data);
    }

    /**
     * Creates the row for OutPut model.
     * 
     * @param composite the composite
     */
    private void createRowForOutputModel(Composite composite)
    {
        // Create label
        toolkit.createLabel(composite, "Output Model: "); //$NON-NLS-1$
        // Create Text field
        String helpText = Messages.ImportRequirementWizardPageSelectDocument_OUTPUT_MODEL;
        outputModelComponent = new ComponentHelpTextFieldButton(this, composite, toolkit, SWT.NONE, helpText, SWT.SAVE, ".*\\.uml|.*\\.sysml|.*\\.requirement"); //$NON-NLS-1$
        outputModelComponent.setEditable(false);
        fill(outputModelComponent);

        // Create label
        toolkit.createLabel(composite, "Output Model Type: "); //$NON-NLS-1$
        // Create radio button
        radioButtonModelType = new ComponentHelpRadioButtonModel(this, composite, toolkit, SWT.NONE, helpText);
    }

    /**
     * Creates the row for Level.
     * 
     * @param composite the composite
     */
    private void createRowForLevel(Composite composite)
    {
        toolkit.createLabel(composite, "Level: "); //$NON-NLS-1$
        String helpText = Messages.ImportRequirementWizardPageSelectDocument_EANNOTATION;
        levelComponent = new ComponentHelpTextField(this, composite, toolkit, SWT.NONE, helpText);
        fill(levelComponent);
    }

    /**
     * Creates the row for Stereotype.
     * 
     * @param composite the composite
     */
    private void createRowForStererotype(Composite composite)
    {
        Composite compo = toolkit.createComposite(composite);
        compo.setLayout(new GridLayout(3, false));
        compo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        // Create the label
        toolkit.createLabel(compo, "Stererotype: "); //$NON-NLS-1$

        // Create a delete button
        buttonDelete = toolkit.createButton(compo, "", SWT.PUSH); //$NON-NLS-1$
        buttonDelete.setImage(new Image(Display.getDefault(), PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE).getImageData()));
        buttonDelete.addSelectionListener(new SelectionListener()
        {

            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                setStereotype(null, true);
            }

        });
        buttonDelete.setEnabled(false);
        // Create the field with an help and a brows button
        String helpText = Messages.ImportRequirementWizardPageSelectDocument_APPLY_STEREOTYPES;
        stereotypeComponent = new ComponentHelpTextFieldButton(this, compo, toolkit, SWT.NONE, helpText, -1);
        stereotypeComponent.setTextEnable(false);
        fill(stereotypeComponent);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
     */
    @Override
    public boolean canFlipToNextPage()
    {
        return isPageComplete();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
     */
    @Override
    public boolean isPageComplete()
    {
        boolean result = true;
        StringBuffer error = new StringBuffer(""); //$NON-NLS-1$

        // Validation Input document
        if (!inputDocumentComponent.isValid())
        {
            result = false;
            error.append("Choose an input document (docx, odt, csv, ods or xlsx)\n"); //$NON-NLS-1$
        }
        else
        {
            // Save input document
            inputDocument = inputDocumentComponent.getInput();
        }

        // Validation Output document
        if (!outputModelComponent.isValid())
        {
            result = false;
            error.append("Choose an output model (uml, sysml or requirement)\n"); //$NON-NLS-1$
            stereotypeComponent.setButtonEnable(false);
        }
        else
        {
            // Save output model
            outputModel = outputModelComponent.getInput();
            if (outputModel.endsWith(".uml") || outputModel.endsWith(".sysml")) //$NON-NLS-1$ //$NON-NLS-2$
            {
                if (!modelType.equals(oldModelType))
                {
                    setStereotype(null, false);

                }
                stereotypeComponent.setButtonEnable(true);

            }
            else
            {
                stereotypeComponent.setButtonEnable(false);
                setStereotype(null, false);
            }
        }

        if (inputTypedDocuments.getSelection() && !typedDocumentsComposite.isTypedDocumentComplete() )
        {
            result = false;
            error.append("Choose a Document type\n"); //$NON-NLS-1$
        }
        
        if (attachRequirement.getSelection() && !typedDocumentsComposite.isAttachReqComplete())
        {
            result = false;
            error.append("Choose a Diagram, a Project name and a Project description\n");
        }
        
        if (result)
        {
            setErrorMessage(null);
        }
        else
        {
            setErrorMessage(error.toString());
        }
        return result;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.ui.NotifyElement#handleModelChange()
     */
    public void handleModelChange()
    {
        getWizard().getContainer().updateMessage();
        getWizard().getContainer().updateButtons();
        // Save Level
        level = levelComponent.getInput();
        // Save output model
        if (attachRequirement.getSelection() && typedDocumentsComposite.getModelToAttach() != null)
        {
            outputModelComponent.setValueText(URI.createURI(typedDocumentsComposite.getModelToAttach().getLocationURI().toString()).trimFileExtension().toString().concat("TEMP.requirement"),true);//getLocationURI().trimFileExtension().appendFileExtension("temp").appendFileExtension("requirement").toString(),true);
        }
        outputModel = outputModelComponent.getInput();
        // Get the model type
        oldModelType = modelType;
        modelType = getModelTypeFromComponent();
        if(init )
        {
        	init = true;
        	return;
        }
        ((ImportRequirementWizard)getWizard()).getPageController().pageSelectDocumentChanged(getInputDocument(), getDocumentType(), modelType, loadMappingPref());
    }

    /**
     * 
     * @return
     */
    public DocumentType getDocumentType()
    {
        if (inputTypedDocuments.getSelection() && typedDocumentsComposite.getSelectedType() != null)
        {
            return typedDocumentsComposite.getSelectedType();
        }
        return null;
    }
    
    // //////////////////////////////////
    // / Getter and setter ///
    // //////////////////////////////////

    /**
     * Gets the model type from component.
     * 
     * @return the model type from component
     */
    private String getModelTypeFromComponent()
    {
        String modelType;
        if (radioButtonModelType.isRequirement())
        {
            modelType = Constants.REQUIREMENT_EXTENSION;
        }
        else if (radioButtonModelType.isUml())
        {
            modelType = Constants.UML_EXTENSION;
        }
        else
        {
            modelType = Constants.SYSML_EXTENSION;
        }
		if (outputModel != null && outputModel.length() > 0) {
			int lastIndexOfPoint = outputModel.lastIndexOf(".");
			String type = outputModel.substring(lastIndexOfPoint + 1,
					outputModel.length());

			if (type.equals(Constants.REQUIREMENT_EXTENSION)
					|| type.equals(Constants.UML_EXTENSION)
					|| type.equals(Constants.SYSML_EXTENSION)) {
				String splitedOutputModel = outputModel.substring(0,
						lastIndexOfPoint);

				outputModel = splitedOutputModel + "." + modelType;

			} else {
				outputModel = outputModel + "." + modelType;
			}
			outputModelComponent.setValueText(outputModel, true);
		}

        return modelType;

    }

    /**
     * The mapping pref have to be load only if the inputs (model type, document type and stereotype) have not changed
     * since last use.
     * 
     * @return true if the mapping pref have to be load
     */
    public boolean loadMappingPref()
    {
        boolean load = true;

        // Check input document type
        if (inputDocumentFromPref != null && inputDocumentFromPref.length() > 0)
        {
            String[] splited = inputDocumentFromPref.split("\\.");
            splited[0] += ".";
            if (!inputDocument.endsWith(splited[1]))
            {
                load = false;
            }
        }

        // Check output model type
        if (modelTypeFromPref!= null && !modelTypeFromPref.equalsIgnoreCase(modelType))
        {
            load = false;
        }

        // Check profile
        if (profileURI != null && profileURI != null && !profileURIFromPref.equalsIgnoreCase(profileURI))
        {
            load = false;
        }

        // Check stereotype
        if (stereotype != null && stereotypeFromPref != null && !stereotypeFromPref.equalsIgnoreCase(stereotype.getName()))
        {
            load = false;
        }

        return load;
    }

    /**
     * Gets the output model.
     * 
     * @return the output model
     */
    public String getOutputModel()
    {
        return outputModel;
    }

    /**
     * Gets the input document.
     * 
     * @return the input document
     */
    public String getInputDocument()
    {
        return inputDocument;
    }

    /**
     * Gets the model type.
     * 
     * @return the model type
     */
    public String getModelType()
    {
        return modelType;
    }

    /**
     * Gets the stereotype.
     * 
     * @return the stereotype
     */
    public Stereotype getStereotype()
    {
        return this.stereotype;
    }

    /**
     * Gets the stereotype name.
     * 
     * @return the stereotype name
     */
    public String getStereotypeName()
    {
        if (stereotype != null)
            return stereotype.getName();
        else
            return "";
    }

    /**
     * Sets the stereotype.
     * 
     * @param s the new stereotype
     */
    public void setStereotype(Stereotype s, boolean handleChanges)
    {
        this.stereotype = s;
        if (stereotype != null)
        {
            stereotypeComponent.setValueText(stereotype.getName(),!handleChanges);
            buttonDelete.setEnabled(true);
        }
        else
        {
            stereotypeComponent.setValueText("",!handleChanges);
            buttonDelete.setEnabled(false);

        }
    }

    /**
     * Sets the profile.
     * 
     * @param p the new profile
     */
    public void setProfile(Profile p)
    {
        this.profile = p;
    }

    /**
     * Gets the profile.
     * 
     * @return the profile
     */
    public Profile getProfile()
    {
        return this.profile;
    }

    /**
     * Gets the level.
     * 
     * @return the level
     */
    public String getLevel()
    {
        return level;
    }

    /**
     * Gets the profile uri.
     * 
     * @return the profile uri
     */
    public String getProfileURI()
    {
        if (profileURI != null)
            return profileURI;
        else
            return "";
    }

    /**
     * Sets the profile uri.
     * 
     * @param profileURI the new profile uri
     */
    public void setProfileURI(String profileURI)
    {
        this.profileURI = profileURI;
    }

    /**
     * Gets if an attach requirement is selected
     * 
     * @return
     */
    public boolean isAttachRequirement()
    {
        return attachRequirement.getSelection();
    }

    /**
     * Gets the model to Attach
     * 
     * @return
     */
    public IFile getModeltoAttach()
    {
        return typedDocumentsComposite.getModelToAttach();
    }

    /**
     * Gets the project name for requirement attachment
     * 
     * @return
     */
    public String getProjectName()
    {
        return typedDocumentsComposite.getProjectName();
    }

    /**
     * Gets the project Description for requirement attachment
     * @return
     */
    public String getProjectDescription()
    {
        return typedDocumentsComposite.getProjectDescription();
    }

}
