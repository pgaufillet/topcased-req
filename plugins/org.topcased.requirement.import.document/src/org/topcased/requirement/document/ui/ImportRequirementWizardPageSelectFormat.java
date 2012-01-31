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

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.topcased.requirement.document.Activator;
import org.topcased.requirement.document.component.ComponentHelpCheckButton;
import org.topcased.requirement.document.component.ComponentHelpRadioButton;
import org.topcased.requirement.document.component.ComponentHelpTextFieldButtonWithDelete;
import org.topcased.requirement.document.elements.Column;
import org.topcased.requirement.document.elements.IStructuredContentProviderTree;
import org.topcased.requirement.document.elements.RecognizedElement;
import org.topcased.requirement.document.elements.RecognizedTree;
import org.topcased.requirement.document.elements.Regex;
import org.topcased.requirement.document.elements.Style;
import org.topcased.requirement.document.utils.Constants;
import org.topcased.requirement.document.utils.DocumentStyleBrowser;
import org.topcased.requirement.document.utils.Messages;
import org.topcased.requirement.document.utils.Serializer;

/**
 * The Class ImportRequirementWizardPageSelectFormat.
 */
public class ImportRequirementWizardPageSelectFormat extends WizardPage implements NotifyElement
{

    /** The Constants PREFERENCE. */
    public static final String PREFERENCE_FOR_VALUE_TO_RECOGNIZE_REQ = "value to recognize req"; //$NON-NLS-1$

    /** The Constant PREFERENCE_FOR_CHAPTER. */
    public static final String PREFERENCE_FOR_CHAPTER = "value for chapter"; //$NON-NLS-1$

    /** The Constant PREFERENCE_FOR_LIST_RECOGNIZED_ELEMENT. */
    public static final String PREFERENCE_FOR_LIST_RECOGNIZED_ELEMENT = "value for list of recognize element"; //$NON-NLS-1$

    /** Page components *. */
    private FormToolkit toolkit;

    /** The form. */
    private Form form;

    /** The section. */
    private Composite section;

    /** The list for input format. */
    private TreeViewer listFormat;

    /** The tree. */
    private RecognizedTree tree;

    /** Input Value *. */
    private RecognizedElement valueToRecognizeReq = null;

    /** The chapter pref. */
    private boolean chapterPref = true;

    /** Buttons *. */
    private Button buttonDelete;

    /** The button new style. */
    private Button buttonNewStyle;

    /** The button new column. */
    private Button buttonNewColumn;

    /** The button new regex. */
    private Button buttonNewRegex;

    /** Behavior Elements *. */
    private RecognizedElement selectedRule;

    /** The chapter recognize component. */
    private ComponentHelpRadioButton chapterRecognizeComponent;

    /** The req id component. */
    private ComponentHelpTextFieldButtonWithDelete reqIdComponent;

    /** The document file. */
    private File documentFile;

    /** The button delete all. */
    private Button buttonDeleteAll;

    /** The browser. */
    private DocumentStyleBrowser browser = new DocumentStyleBrowser();

    /** The is spreadsheet. */
    private boolean isSpreadsheet;

    /** The label chapter. */
    private Label labelChapter;

    /** The Current File System */
    private File currentFileSystem;

    /** The Current File */
    private IFile currentFile;
    
    /** Flag for initialization */
	private boolean init = true;
	
	/** Check button component for Description */
	private ComponentHelpCheckButton descriptionCheck;
	
	/** description composite */
	private DescriptionComposite descriptionComposite;
	
    
    /**
     * Instantiates a new import requirement wizard page select format.
     * 
     * @param pageName the page name
     * @param document the document
     * @param t the t
     * @param page1 
     */
    protected ImportRequirementWizardPageSelectFormat(String pageName, File document, RecognizedTree t)
    {
        super(pageName);
        documentFile = document;
        tree = t;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.ui.NotifyElement#handleModelChange()
     */
    public void handleModelChange()
    {
        if (reqIdComponent.isDelete())
        {
            deleteReqId();
        }
        updateWizard();
    }

    /**
     * Delete req id.
     */
    public void deleteReqId()
    {
        reqIdComponent.setHasToBeDelete(false);
        valueToRecognizeReq = null;
        reqIdComponent.setValueText("");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
    	init  = true;
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new FillLayout());
        // setImageDescriptor(workbench.getSharedImages().getImageDescriptor(Wizard.DEFAULT_IMAGE));
        this.setDescription("Requirement import from csv, docx, odt, ods or xlsx"); //$NON-NLS-1$
        toolkit = new FormToolkit(composite.getDisplay());
        form = toolkit.createForm(composite);
        // create the base form
        form.setText("Requirements Import"); //$NON-NLS-1$
        toolkit.decorateFormHeading(form);
        FillLayout layout = new FillLayout();
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
        section = createSection(form, "Requirement and attributes recognition", 2); //$NON-NLS-1$
        // Load Inputs from preferences
        loadPreferecences();
        createRowForChapter(section);
        createRowForReqIndification(section);
        createListFormat(section);
        createComponentForButtons(section);
        createDescription(section);
    }

    /**
     * Creates Description Composite
     * 
     * @param composite
     */
    private void createDescription(Composite composite)
    {
        String helpText = "<form><p>Option to specify End Text and description regex</p></form>";
        descriptionCheck = new ComponentHelpCheckButton(new NotifyElement()
        {
            public void handleModelChange()
            {
                descriptionComposite.setEnabled(descriptionCheck.getSelection());
                descriptionComposite.setVisible(descriptionCheck.getSelection());
                ImportRequirementWizardPageSelectFormat.this.handleModelChange();
            }
        }, composite, toolkit, SWT.NONE, helpText);
        descriptionCheck.setValueText("Description");
        descriptionComposite = new DescriptionComposite(composite, SWT.NONE);
        descriptionComposite.setNotifyElement(this);
        descriptionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        descriptionComposite.setEnabled(false);
        descriptionComposite.setVisible(false);
    }


    
    /**
     * Creates the component for buttons.
     * 
     * @param section the section
     */
    private void createComponentForButtons(Composite section)
    {
        createRowForNewStyleButton(section);
        createRowForNewRegexButton(section);
        createRowForNewColumnButton(section);
        createRowForDeleteButton(section);
        createRowForDeleteAllButton(section);
    }

    /**
     * Creates the row for new regex button.
     * 
     * @param compo the compo
     */
    private void createRowForNewRegexButton(Composite compo)
    {
        buttonNewRegex = toolkit.createButton(section, "New Regex...", SWT.PUSH); //$NON-NLS-1$
        buttonGridData(buttonNewRegex, SWT.TOP);
        buttonNewRegex.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                PopupRegexDialog dialog = new PopupRegexDialog(getShell());
                if (dialog.open() == Dialog.OK)
                {
                    // if the first field is not set
                    if (valueToRecognizeReq == null)
                    {
                        valueToRecognizeReq = new Regex(dialog.getRegexInput());
                        reqIdComponent.setValueText(valueToRecognizeReq.getText());
                        updateWizard();
                    }
                    else
                    {
                        tree.add(new Regex(dialog.getRegexInput()));
                        listFormat.refresh();
                    }
                    updateWizard();
                }
            }
        });
    }

    /**
     * Creates the row for delete button.
     * 
     * @param compo the compo
     */
    private void createRowForDeleteButton(Composite compo)
    {
        buttonDelete = toolkit.createButton(section, "Remove", SWT.PUSH); //$NON-NLS-1$
        buttonGridData(buttonDelete, SWT.BOTTOM);
        buttonDelete.setEnabled(false);
        buttonDelete.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                tree.getChildren().remove(selectedRule);
                listFormat.refresh();
                buttonDelete.setEnabled(false);
                updateWizard();
            }
        });
    }

    /**
     * Creates the row for delete all button.
     * 
     * @param compo the compo
     */
    private void createRowForDeleteAllButton(Composite compo)
    {
        buttonDeleteAll = toolkit.createButton(section, "Remove All", SWT.PUSH); //$NON-NLS-1$
        buttonGridData(buttonDeleteAll, SWT.BOTTOM);
        buttonDeleteAll.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                // Erase all the list
                tree.getChildren().clear();
                // Refresh
                listFormat.refresh();
                // Remove req id
                deleteReqId();
            }
        });
    }

    /**
     * Creates the row for new column button.
     * 
     * @param section2 the section2
     */
    private void createRowForNewColumnButton(Composite section2)
    {
        buttonNewColumn = toolkit.createButton(section, "New Column...", SWT.PUSH); //$NON-NLS-1$
        buttonGridData(buttonNewColumn, SWT.TOP);
        buttonNewColumn.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                NewColumnPopup dialog = new NewColumnPopup(getShell());
                if (dialog.open() == Dialog.OK)
                {
                    // if the first field is not set
                    if (valueToRecognizeReq == null)
                    {
                        valueToRecognizeReq = new Column(dialog.getColumnInput(), dialog.getRegexInput());
                        reqIdComponent.setValueText(valueToRecognizeReq.getText());
                    }
                    else
                    {
                        tree.add(new Column(dialog.getColumnInput(), dialog.getRegexInput()));
                        listFormat.refresh();
                    }
                    updateWizard();
                }
            }
        });

    }

    /**
     * Creates the row for new style button.
     * 
     * @param section the section
     */
    private void createRowForNewStyleButton(Composite section)
    {
        buttonNewStyle = toolkit.createButton(section, "New Style...", SWT.PUSH); //$NON-NLS-1$
        buttonGridData(buttonNewStyle, SWT.TOP);
        buttonNewStyle.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                NewStylePopup dialog = new NewStylePopup(getShell(), documentFile, browser);
                if (dialog.open() == Dialog.OK)
                {
                    if (dialog.getStyleInput() != null && dialog.getStyleInput().length() > 0)
                    {
                        // if the first field is not set
                        if (valueToRecognizeReq == null)
                        {
                            valueToRecognizeReq = new Style(dialog.getStyleInput(), dialog.getRegexInput());
                            reqIdComponent.setValueText(valueToRecognizeReq.getText());
                        }
                        else
                        {
                            tree.add(new Style(dialog.getStyleInput(), dialog.getRegexInput()));
                            listFormat.refresh();
                        }
                        updateWizard();
                    }
                }
            }
        });
    }

    /**
     * Update wizard.
     */
    private void updateWizard()
    {
        getWizard().getContainer().updateMessage();
        getWizard().getContainer().updateButtons();
        ((ImportRequirementWizard)getWizard()).getPageController().pageSelectFormatChanged( tree );
    }

    /**
     * Button grid data.
     * 
     * @param b the b
     * @param where the where
     */
    private void buttonGridData(Button b, int where)
    {
        b.setLayoutData(new GridData(SWT.FILL, where, false, false));
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
        // Create section
        Section section = toolkit.createSection(mform.getBody(), Section.TITLE_BAR | Section.EXPANDED);
        section.setText(title);

        // Create composite
        Composite client = toolkit.createComposite(section);

        // Add a grid layout
        GridLayout layout = new GridLayout(numColumns, false);
        layout.marginWidth = 1;
        layout.marginHeight = 1;
        client.setLayout(layout);

        section.setClient(client);
        return client;
    }

    /**
     * Creates the row for chapter.
     * 
     * @param section the section
     */
    private void createRowForChapter(Composite section)
    {
        // Create the composite
        Composite compo = toolkit.createComposite(section);
        compo.setLayout(new GridLayout(2, false));
        // Create Label
        labelChapter = toolkit.createLabel(compo, "Chapter organisation: "); //$NON-NLS-1$
        // Create the text component
        String helpText = Messages.ImportRequirementWizardPageSelectFormat_FLAT_OR_HIERARCHICAL;
        chapterRecognizeComponent = new ComponentHelpRadioButton(this, compo, toolkit, SWT.NONE, helpText);
        chapterRecognizeComponent.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        compo.setLayoutData(data);
        // load pref
        chapterRecognizeComponent.setHierachical(chapterPref);

    }

    /**
     * Creates the row for req indification.
     * 
     * @param section the section
     */
    private void createRowForReqIndification(Composite section)
    {
        // Create the composite
        Composite compo = toolkit.createComposite(section);
        compo.setLayout(new GridLayout(2, false));
        // Create Label
        toolkit.createLabel(compo, "Requirement identification: "); //$NON-NLS-1$
        // Create the text component
        String helpText = Messages.ImportRequirementWizardPageSelectFormat_IDENTIFICATION;
        reqIdComponent = new ComponentHelpTextFieldButtonWithDelete(this, compo, toolkit, SWT.NONE, helpText);
        reqIdComponent.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
        GridData data1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        compo.setLayoutData(data1);
        GridData data2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        reqIdComponent.setLayoutData(data2);

        // Load preferences from a previous use
        if (valueToRecognizeReq != null)
        {
            reqIdComponent.setValueText(valueToRecognizeReq.getText(), true);
            updateWizard();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
     */
    @Override
    public boolean canFlipToNextPage()
    {
        return super.canFlipToNextPage() && isPageComplete();
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
        StringBuffer error = new StringBuffer("");

        // Validation Input document
        if (valueToRecognizeReq == null)
        {
            result = false;
            error.append("Enter a rule to recognize requirements\n");
        }
        if (!isListValid())
        {
            result = false;
            error.append("Some rules are not valid\nxlsx, ods, csv => only column\ndocx, odt => only style and regex"); //$NON-NLS-1$
        }
        
        
        if (descriptionCheck!=null && descriptionCheck.getSelection() && !(descriptionComposite.isDescriptionRegexComplete() ||descriptionComposite.isTextComplete()))
        {
            result = false;
            error.append("Please fill EndLabel or Description regex");
        }
        else if (descriptionCheck != null && descriptionCheck.getSelection() && descriptionComposite.isDescriptionRegexComplete())
        {
            if (!isRegexValid(descriptionComposite.getDescriptionRegex()))
            {
                result = false;
                error.append("Description regex doesn't compile");
            }
        }
        
        
        // Display error message
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

    /**
     * Check the validity of a regex 
     * 
     * @param string the regex to check
     * @return true if the regex is valid and false otherwise
     */
    private boolean isRegexValid(String string)
    {
            try
            {
                Pattern.compile(string, Pattern.MULTILINE);
                return true;
            }
            catch (PatternSyntaxException e)
            {
                return false;
            }
    }

    /**
     * Check if all the rules are input are valid according to the input file type xlsx, ods, csv => only column docx,
     * odt => only style and regex.
     * 
     * @return true if the list input is valid
     */
    private boolean isListValid()
    {
        boolean isValid = true;
        Collection<RecognizedElement> elementsList = tree.getChildren();
        for (Iterator<RecognizedElement> iterator = elementsList.iterator(); iterator.hasNext();)
        {
            RecognizedElement recognizedElement = (RecognizedElement) iterator.next();
            if (isSpreadsheet)
            {
                if (recognizedElement instanceof Style || recognizedElement instanceof Regex)
                {
                    isValid = false;
                    break;
                }
            }
            else
            {
                if (recognizedElement instanceof Column)
                {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }

    /**
     * This method will make visible or not the button according to the input document type.
     * 
     * @param isASpreadsheet the is a spreadsheet
     */
    public void setIsSpreadsheet(boolean isASpreadsheet)
    {
        isSpreadsheet = isASpreadsheet;
        if (isASpreadsheet)
        {
            // Case xlsx or ods or csv
            buttonNewColumn.setVisible(true);
            buttonNewRegex.setVisible(false);
            buttonNewStyle.setVisible(false);
            chapterRecognizeComponent.setVisible(false);
            labelChapter.setVisible(false);
            descriptionCheck.setVisible(false);
            descriptionComposite.setVisible(false);
        }
        else
        {
            // Case docx or odt
            buttonNewColumn.setVisible(false);
            buttonNewRegex.setVisible(true);
            buttonNewStyle.setVisible(true);
            chapterRecognizeComponent.setVisible(true);
            labelChapter.setVisible(true);
            descriptionCheck.setVisible(true);
        }
    }

    /**
     * Checks if is spreadsheet.
     * 
     * @return true, if is spreadsheet
     */
    public boolean isSpreadsheet()
    {
        return isSpreadsheet;
    }

    /**
     * Sets the document file.
     * 
     * @param documentFile the new document file
     */
    public void setDocumentFile(File documentFile)
    {
        this.documentFile = documentFile;
    }

    /**
     * Gets the value to recognize req.
     * 
     * @return the value to recognize req
     */
    public String getValueToRecognizeReq()
    {
        if (valueToRecognizeReq != null)
        {
            Serializer<RecognizedElement> serializer = new Serializer<RecognizedElement>();
            return serializer.serialize(valueToRecognizeReq);
        }
        else
        {
            return "";
        }
    }

    /**
     * Gets the identification.
     * 
     * @return the identification
     */
    public RecognizedElement getIdentification()
    {
        return valueToRecognizeReq;
    }

    /**
     * Gets the checks if is hiearachical.
     * 
     * @return the checks if is hiearachical
     */
    public boolean getIsHiearachical()
    {
        return chapterRecognizeComponent.isHierarchical();
    }

    /**
     * Gets the list attributes pref.
     * 
     * @return the list attributes pref
     */
    public String getListAttributesPref()
    {
        Serializer<Collection<RecognizedElement>> serializer = new Serializer<Collection<RecognizedElement>>();
        return serializer.serialize(tree.getChildren());
    }

    /**
     * Gets the tree.
     * 
     * @return the tree
     */
    public RecognizedTree getTree()
    {
        return tree;
    }

    public boolean getDescriptionState()
    {
        return descriptionCheck.getSelection();
    }
    
    public String getDescription()
    {
        return descriptionComposite.getText();
    }
    
    public boolean isDescriptionRegex()
    {
        return descriptionComposite.isDescriptionRegexComplete();
    }
    
    public boolean isDescriptionText()
    {
        return descriptionComposite.isTextComplete();
    }
    
    public String getDescriptionRegex()
    {
        return descriptionComposite.getDescriptionRegex();
    }
    
    
    /**
     * Load Preferences from a previous use.
     */
    public void loadPreferecences()
    {
        // Load preferences for chapter
        chapterPref = Activator.getDefault().getPluginPreferences().getBoolean(PREFERENCE_FOR_CHAPTER);

        // Load preferences for value to recognize req
        String pref = Activator.getDefault().getPluginPreferences().getString(PREFERENCE_FOR_VALUE_TO_RECOGNIZE_REQ);
        if (pref != null && pref.length() > 0)
        {
            Serializer<RecognizedElement> serializer = new Serializer<RecognizedElement>();
            RecognizedElement element = serializer.unSerialize(pref);
            if (element != null)
            {
                valueToRecognizeReq = element;
            }
        }

        // Load preferences for attributes list
        pref = Activator.getDefault().getPluginPreferences().getString(PREFERENCE_FOR_LIST_RECOGNIZED_ELEMENT);
        if (pref != null && pref.length() > 0)
        {
            Serializer<Collection<RecognizedElement>> serializer = new Serializer<Collection<RecognizedElement>>();
            Collection<RecognizedElement> paramDecoded = serializer.unSerialize(pref);
            if (paramDecoded != null && paramDecoded.size() > 0)
            {
                for (Iterator<RecognizedElement> iterator = paramDecoded.iterator(); iterator.hasNext();)
                {
                    tree.add((RecognizedElement) iterator.next());
                }
            }
        }
    }

    /**
     * Creates the list stereotypes.
     * 
     * @param section2 the section2
     */
    private void createListFormat(Composite section2)
    {
        // Create Label
        Label label = toolkit.createLabel(section2, "Attributes identification:"); //$NON-NLS-1$
        label.setData(new GridData(SWT.FILL, SWT.LEFT, false, false, 2, 1));

        // Create treeViewer
        listFormat = new TreeViewer(section2, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 6);
        listFormat.getTree().setLayoutData(data);

        // Set the label provider
        listFormat.setLabelProvider(new LabelProvider()
        {
            public String getText(Object element)
            {
                // Return the label.
                if (element instanceof String)
                {
                    return (String) element;
                }
                else if (element instanceof RecognizedElement)
                {
                    return ((RecognizedElement) element).getText();
                }
                else if (element instanceof RecognizedTree)
                {
                    return "root"; //$NON-NLS-1$
                }
                return null;
            }
        });
            // Set the content provider
            listFormat.setContentProvider(new IStructuredContentProviderTree());

        
        // Add inputs
        listFormat.setInput(tree);

        listFormat.addSelectionChangedListener(new ISelectionChangedListener()
        {
            private ISelection selection;

            public void selectionChanged(SelectionChangedEvent event)
            {
                selection = listFormat.getSelection();
                if (selection instanceof IStructuredSelection)
                {
                    IStructuredSelection stru = (IStructuredSelection) selection;

                    // enable button delete
                    buttonDelete.setEnabled(true);

                    // Get the selection
                    if (stru.getFirstElement() instanceof RecognizedElement)
                        selectedRule = (RecognizedElement) stru.getFirstElement();
                }
            }
        });

    }
    
    /**
     * Performs initialization of the page at each changes in the wizard
     * 
     * @param inputDocument
     * @param modelType 
     */
    public void pageChanged(String inputDocument, String modelType)
    {
    	if (init) {
			init = false;
    		return;
		}
        
        if (inputDocument.endsWith(".docx") || inputDocument.endsWith(".odt"))
        {
            setIsSpreadsheet(false);
            
            
            chapterRecognizeComponent.setHierachical(((ImportRequirementWizard)getWizard()).getPageController().isHierarchical());
            
            valueToRecognizeReq = ((ImportRequirementWizard)getWizard()).getPageController().getIDRegexOrStyle();
            if (valueToRecognizeReq != null)
            {
                reqIdComponent.setValueText(valueToRecognizeReq.getText());
            } else {
                reqIdComponent.setValueText("");
            }
            
            RecognizedTree newTree = ((ImportRequirementWizard)getWizard()).getPageController().getStylesAndRegex();
            tree.getChildren().clear();
            if (newTree != null)
            {
                tree.getChildren().addAll(newTree.getChildren());
            }
            listFormat.refresh(true);
           
           
        } 
        else
        {
            setIsSpreadsheet(true);
            
            valueToRecognizeReq = ((ImportRequirementWizard)getWizard()).getPageController().getIDColumn();
             
            if (valueToRecognizeReq != null)
            {
                reqIdComponent.setValueText(valueToRecognizeReq.getText());
            } else {
                reqIdComponent.setValueText("");
            }
            
            RecognizedTree newTree = ((ImportRequirementWizard)getWizard()).getPageController().getColumns();
            tree.getChildren().clear();
            if (newTree != null)
            {
                tree.getChildren().addAll(newTree.getChildren());
            }
            listFormat.refresh(true);
            
            descriptionCheck.setSelection(false);
            descriptionComposite.setEnabled(false);
            descriptionComposite.setVisible(false);
            descriptionComposite.setText("");

        }
        
        descriptionCheck.setSelection(false);
        descriptionComposite.setEnabled(false);
        descriptionComposite.setVisible(false);
        descriptionComposite.setText("");

        if (Constants.REQUIREMENT_EXTENSION.equals(modelType))
        {
            descriptionCheck.setVisible(true);
            descriptionCheck.setEnabled(true);
            
            String endText = ((ImportRequirementWizard)getWizard()).getPageController().getEndText();
            String descriptionRegex = ((ImportRequirementWizard)getWizard()).getPageController().getDescriptionRegex();
            if (endText != null || descriptionRegex != null)
            {
                descriptionCheck.setSelection(true);
                descriptionComposite.setEnabled(true);
                descriptionComposite.setVisible(true);
                if (endText != null)
                {
                    descriptionComposite.setText(endText);
                }
                if (descriptionRegex != null)
                {
                    descriptionComposite.setDescriptionRegex(descriptionRegex);
                }
            }
            
            
        } else
        {
            descriptionCheck.setVisible(false);
            descriptionCheck.setEnabled(false);
            descriptionComposite.setEnabled(false);
            descriptionComposite.setVisible(false);
        }
        
        updateWizard();
        
        if (inputDocument != null)
        {
            if (inputDocument.contains("file:"))
            {
                currentFileSystem = new File(URI.createURI(inputDocument).toFileString());
                ((ImportRequirementWizard)getWizard()).setCurrentFileSystem(currentFileSystem);
            }
            else if (inputDocument.contains("platform:"))
            {
                currentFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(URI.createURI(inputDocument).toPlatformString(true)));
                ((ImportRequirementWizard)getWizard()).setCurrentFile(currentFile);
                if (currentFile != null)
                {
                    currentFileSystem = currentFile.getLocation().toFile();
                    ((ImportRequirementWizard)getWizard()).setCurrentFileSystem(currentFileSystem);
                }
            }
            else
            {
                currentFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(URI.createURI(inputDocument).toFileString()));
                ((ImportRequirementWizard)getWizard()).setCurrentFile(currentFile);
                if (currentFile != null)
                {
                    currentFileSystem = currentFile.getLocation().toFile();
                    ((ImportRequirementWizard)getWizard()).setCurrentFileSystem(currentFileSystem);
                }
            }
        }
        setDocumentFile(currentFileSystem);
    }
    
}
