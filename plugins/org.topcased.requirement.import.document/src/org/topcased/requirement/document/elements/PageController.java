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
package org.topcased.requirement.document.elements;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.topcased.requirement.core.wizards.operation.NewRequirementModelOperation;
import org.topcased.requirement.document.Activator;
import org.topcased.requirement.document.ui.ImportRequirementWizard;
import org.topcased.requirement.document.ui.ImportRequirementWizardPageMapping;
import org.topcased.requirement.document.ui.ImportRequirementWizardPageSelectDocument;
import org.topcased.requirement.document.ui.ImportRequirementWizardPageSelectFormat;
import org.topcased.requirement.document.utils.Constants;
import org.topcased.typesmodel.handler.IniManager;
import org.topcased.typesmodel.model.inittypes.Column;
import org.topcased.typesmodel.model.inittypes.DocumentType;
import org.topcased.typesmodel.model.inittypes.InittypesFactory;
import org.topcased.typesmodel.model.inittypes.Type;

/**
 * Control the page initialization of Import Requirement Document
 * 
 */
public class PageController
{

    /** the import document wizard */
    private ImportRequirementWizard wizard;

    /** the document type */
    private DocumentType documentType;
    
    /** the model type */
    private String modelType;

    /** Boolean to define if mapping has to be load from pref or not */
    private boolean loadMappingPref;

    /** Map of recognized element and type */
    private Map<RecognizedElement, Type> recognizedElementToType;

    /** collection of all the mappings */
    private Collection<Mapping> mapping;

    /** the model to attach */
    private IFile modelToAttach;

    /** the requirement path to attach */
    private String requirementToAttach;

    /** the requirement file */
    private IFile reqFile;

    /** the page to select document */
    private ImportRequirementWizardPageSelectDocument pageSelectDocument;

    /** the page to select format */
    private ImportRequirementWizardPageSelectFormat pageSelectFormat;

    /** the page to set the mapping */
    private ImportRequirementWizardPageMapping pageMapping;

    /** the end text expression */
    private String endText;

    /** the description regex */
    private String descriptionRegex;

    /** the hierarchical boolean */
    private boolean hierarchical;

    /** Boolean to define if the hierarchical has been set of not */
    private boolean hierarchicalSet = false;

    /** the id */
    private RecognizedElement identifier;

    /**
     * the page controller constructor
     * @param wizard the import requirement wizard
     */
    public PageController(ImportRequirementWizard wizard)
    {
        this.wizard = wizard;
    }

    /**
     * Creates all the pages for the import wizard
     * 
     * @param valueForInput the value for input
     * @param file the new document file
     * @param tree the tree of recognized elements
     * @param listAttributes the attributes list
     * @return list of created pages
     */
    public List<WizardPage> createPages(String valueForInput, File file, RecognizedTree tree, Collection<Attribute> listAttributes)
    {
        pageSelectDocument = new ImportRequirementWizardPageSelectDocument("Select Document", this);
        pageSelectFormat = new ImportRequirementWizardPageSelectFormat("Enter the file format", this);
        pageMapping = new ImportRequirementWizardPageMapping("Enter the file format", this);

        initPages(valueForInput, file, tree, listAttributes);
        return Arrays.asList(pageSelectDocument, pageSelectFormat, pageMapping);
    }

    /**
     * 
     * @param valueForInput the value for input
     * @param document the new document file
     * @param tree the tree of recognized elements
     * @param listAttributes the attributes list
     */
    private void initPages(String valueForInput, File document, RecognizedTree tree, Collection<Attribute> listAttributes)
    {
        if (valueForInput != null)
        {

            pageSelectDocument.setInputDocument(valueForInput);
        }

        if (document != null)
        {
            pageSelectFormat.setDocumentFile(document);
        }

        if (tree != null)
        {
            pageSelectFormat.setTree(tree);
            pageMapping.setTree(tree);
        }

        if (listAttributes != null)
        {
            pageMapping.setListAttributes(listAttributes);
        }
        // if (pageSelectDocument.getModelType() != null)
        // {
        // pageMapping.setModelType(pageSelectDocument.getModelType());
        // }

    }

    /**
     * Called when Select Document Page changes in Import requirement Wizard
     * 
     * @param InputDocument the input document
     * @param documentType the document type element
     * @param modelType the model type (Requirement, UML or SysML)
     * @param loadMappingPref boolean defining if preferences should be loaded or not
     */
    public void pageSelectDocumentChanged(String InputDocument, DocumentType documentType, String modelType, boolean loadMappingPref)
    {
        wizard.getContainer().updateMessage();
        wizard.getContainer().updateButtons();

        this.modelType = modelType;
        this.documentType = documentType;
        this.loadMappingPref = loadMappingPref;
        pageSelectFormat.pageChanged(InputDocument, modelType, documentType != null);
    }

    /**
     * return the type model element
     * 
     * @return
     */
    public String getModelType()
    {
        return modelType;
    }

    /**
     * Sets the type model element
     * 
     */
    public void setModelType(String modelType)
    {
        this.modelType = modelType;
    }

    /**
     * Called when select format page of import requirement document changes
     * 
     * @param recognizedTree the recognized tree of select format page
     */
    public void pageSelectFormatChanged(RecognizedTree recognizedTree)
    {
        if (documentType != null)
        {
            updateMap(recognizedTree);
            pageMapping.pageChanged(getAttributes(), mapping, modelType, documentType != null);
        }
    }

    private void updateMap(RecognizedTree recognizedTree)
    {
        if (recognizedElementToType == null || recognizedElementToType.isEmpty())
        {
            return;
        }

        HashMap<RecognizedElement, Type> tempMap = new HashMap<RecognizedElement, Type>();
        for (RecognizedElement iter : recognizedTree.getChildren())
        {
            if (recognizedElementToType.containsKey(iter))
            {
                tempMap.put(iter, recognizedElementToType.get(iter));
            }
        }
        recognizedElementToType = tempMap;
    }

    /**
     * return list of attributes from the types documents then are mapped with a column, style or regex
     * 
     * @return list of attributes
     */
    public Collection<Attribute> getAttributes()
    {
        if (recognizedElementToType == null || recognizedElementToType.isEmpty())
        {
            return null;
        }
        mapping = new LinkedList<Mapping>();
        List<Attribute> attributes = new LinkedList<Attribute>();

        for (Entry<RecognizedElement, Type> entryset : recognizedElementToType.entrySet())
        {
            if (Constants.REQUIREMENT_EXTENSION.equals(modelType))
            {
                AttributeRequirement attributeTemp = new AttributeRequirement(entryset.getValue().getName(), false, entryset.getValue().isIsText(), "Requirement");
                mapping.add(new Mapping(entryset.getKey(), attributeTemp));
                attributes.add(attributeTemp);
            }
            else if (Constants.SYSML_EXTENSION.equals(modelType))
            {
                AttributeSysml attributeTemp = new AttributeSysml(entryset.getValue().getName(), false, "Requirement");
                mapping.add(new Mapping(entryset.getKey(), attributeTemp));
                attributes.add(attributeTemp);
            }
            else if (Constants.UML_EXTENSION.equals(modelType))
            {
                AttributeUml attributeTemp = new AttributeUml(entryset.getValue().getName(), false, "Requirement");
                mapping.add(new Mapping(entryset.getKey(), attributeTemp));
                attributes.add(attributeTemp);
            }

        }

        return attributes;

    }

    /**
     * Gets tree of recognized elements of style and regex from types document
     * 
     * @return
     */
    public RecognizedTree getStylesAndRegex()
    {
        if (documentType == null)
        {
            return null;
        }
        recognizedElementToType = new HashMap<RecognizedElement, Type>();

        RecognizedTree result = new RecognizedTree();

        List<org.topcased.typesmodel.model.inittypes.Style> styles = IniManager.getInstance().getStyles(documentType);
        List<org.topcased.typesmodel.model.inittypes.Regex> regex = IniManager.getInstance().getRegex(documentType);

        for (org.topcased.typesmodel.model.inittypes.Regex oneRegex : regex)
        {
            if (oneRegex.getExpression() != null && oneRegex.getName() != null)
            {
                Regex regexTemp = new Regex(oneRegex.getExpression());
                recognizedElementToType.put(regexTemp, oneRegex);
                result.add(regexTemp);
            }
            else
            {
                Activator.getDefault().getLog().log(
                        new Status(Status.WARNING, Activator.PLUGIN_ID, "The regex " + oneRegex.getName() + " has been ignored because it doesn't contains a name or an expression"));
            }
        }

        for (org.topcased.typesmodel.model.inittypes.Style style : styles)
        {
            if (style.getName() != null && style.getLabel() != null)
            {
                Style styleTemp;
                if (style.getExpression() != null)
                {
                    styleTemp = new Style(style.getLabel(), style.getExpression());
                }
                else
                {
                    styleTemp = new Style(style.getLabel(), "");
                }
                recognizedElementToType.put(styleTemp, style);
                result.add(styleTemp);
            }
            else
            {
                Activator.getDefault().getLog().log(
                        new Status(Status.WARNING, Activator.PLUGIN_ID, "The style " + style.getName() + " " + style.getName()
                                + " has been ignored because it doesn't contains a name or an expression"));
            }
        }

        return result;
    }

    /**
     * Gets tree of recognized element of Columns from types documents
     * 
     * @return
     */
    public RecognizedTree getColumns()
    {
        if (documentType == null)
        {
            return null;
        }
        recognizedElementToType = new HashMap<RecognizedElement, Type>();
        RecognizedTree result = new RecognizedTree();

        List<Column> columns = IniManager.getInstance().getColumns(documentType);

        for (Column column : columns)
        {
            if (column.getExpression() != null && column.getName() != null)
            {
                org.topcased.requirement.document.elements.Column columnTemp = new org.topcased.requirement.document.elements.Column(column.getNumber(), column.getExpression());
                recognizedElementToType.put(columnTemp, column);
                result.add(columnTemp);
            }
            else
            {
                Activator.getDefault().getLog().log(
                        new Status(Status.WARNING, Activator.PLUGIN_ID, "Column with number " + column.getNumber() + " has been ignored because it doesn't contains a name or an expression"));
            }
        }

        return result;
    }

    /**
     * Gets if is hierarchical or not from the types document
     * 
     * @return
     */
    public boolean isHierarchical()
    {
        if (hierarchicalSet && documentType == null)
        {
            return hierarchical;
        }
        if (documentType == null)
        {
            return false;
        }

        return documentType.isHierarchical();
    }

    /**
     * Gets recognized ID element of regex or style from types document
     * 
     * @return
     */
    public RecognizedElement getIDRegexOrStyle()
    {

        if (documentType == null || documentType.getId() == null)
        {
            return null;
        }

        RecognizedElement result = null;
        Type id = documentType.getId();

        if (id instanceof org.topcased.typesmodel.model.inittypes.Style)
        {
            if (((org.topcased.typesmodel.model.inittypes.Style) id).getExpression() != null)
            {
                result = new Style(((org.topcased.typesmodel.model.inittypes.Style) id).getLabel(), ((org.topcased.typesmodel.model.inittypes.Style) id).getExpression());
            }
            else
            {
                result = new Style(((org.topcased.typesmodel.model.inittypes.Style) id).getLabel(), "");
            }
        }
        else if (id instanceof org.topcased.typesmodel.model.inittypes.Regex)
        {
            result = new Regex(((org.topcased.typesmodel.model.inittypes.Regex) id).getExpression());
        }

        return result;
    }

    /**
     * Gets recognized ID element of column from types document
     * 
     * @return
     */
    public RecognizedElement getIDColumn()
    {

        if (documentType == null || documentType.getId() == null)
        {
            return null;
        }

        RecognizedElement result = null;
        Type id = documentType.getId();

        if (id instanceof Column)
        {
            result = new org.topcased.requirement.document.elements.Column(((Column) id).getNumber(), ((Column) id).getExpression());
        }

        return result;
    }

    /**
     * Gets if loading mapping Pref have to be load or not
     * 
     * @return
     */
    public boolean loadMappingPref()
    {
        return loadMappingPref;
    }

    /**
     * Perfom a save of types
     * 
     * @param path the path to save types file
     */
    public void PerformSavingModel(IPath path)
    {
        Collection<Mapping> mappingToSave = null;
        RecognizedElement idToSave = null;
        boolean hierarchicalToSave = false;
        String endText = null;
        String descriptionRegex = null;
        for (IWizardPage iter : wizard.getPages())
        {
            if (iter instanceof ImportRequirementWizardPageMapping)
            {
                ImportRequirementWizardPageMapping page = (ImportRequirementWizardPageMapping) iter;
                mappingToSave = page.getListMapping();
            }
            if (iter instanceof ImportRequirementWizardPageSelectFormat)
            {
                ImportRequirementWizardPageSelectFormat page = (ImportRequirementWizardPageSelectFormat) iter;
                idToSave = page.getIdentification();
                hierarchicalToSave = page.getIsHiearachical();
                if (page.getDescriptionState())
                {
                    if (page.isDescriptionText())
                    {
                        endText = page.getDescription();
                    }
                    if (page.isDescriptionRegex())
                    {
                        descriptionRegex = page.getDescriptionRegex();
                    }
                }
            }
        }
        Collection<Type> types = transformModel(mappingToSave);
        Type id = null;
        if (idToSave != null)
        {
            if (idToSave instanceof org.topcased.requirement.document.elements.Column)
            {
                id = InittypesFactory.eINSTANCE.createColumn();
                ((Column) id).setExpression(((org.topcased.requirement.document.elements.Column) idToSave).getRegex());
                ((Column) id).setNumber(((org.topcased.requirement.document.elements.Column) idToSave).getColumn());
            }
            else if (idToSave instanceof Style)
            {
                id = InittypesFactory.eINSTANCE.createStyle();
                ((org.topcased.typesmodel.model.inittypes.Style) id).setExpression(((Style) idToSave).getRegex());
                ((org.topcased.typesmodel.model.inittypes.Style) id).setLabel(((Style) idToSave).getStyle());

            }
            else if (idToSave instanceof Regex)
            {
                id = InittypesFactory.eINSTANCE.createRegex();
                ((org.topcased.typesmodel.model.inittypes.Regex) id).setExpression(((Regex) idToSave).getRegex());
            }
        }
        IFile typesFile = ImportRequirementWizard.getFile(path.toString());
        IniManager.getInstance().save(typesFile, types, id, hierarchicalToSave, endText, descriptionRegex);
    }

    private Collection<Type> transformModel(Collection<Mapping> mappingToSave)
    {
        Set<Type> types = new HashSet<Type>();
        for (Mapping mapping : mappingToSave)
        {
            if (mapping.getElement() instanceof org.topcased.requirement.document.elements.Column)
            {
                org.topcased.requirement.document.elements.Column recognizedColumn = (org.topcased.requirement.document.elements.Column) mapping.getElement();
                Column column = InittypesFactory.eINSTANCE.createColumn();
                column.setExpression(recognizedColumn.getRegex());
                column.setNumber(recognizedColumn.getColumn());
                column.setName(mapping.getAttribute().getProperName());
                if (mapping.getAttribute() instanceof AttributeRequirement)
                {
                    AttributeRequirement att = (AttributeRequirement) mapping.getAttribute();
                    column.setIsText(att.isText());
                }
                types.add(column);
            }
            else if (mapping.getElement() instanceof Style)
            {
                Style recognizedStyle = (Style) mapping.getElement();
                org.topcased.typesmodel.model.inittypes.Style style = InittypesFactory.eINSTANCE.createStyle();
                style.setExpression(recognizedStyle.getRegex());
                style.setLabel(recognizedStyle.getStyle());
                style.setName(mapping.getAttribute().getProperName());
                if (mapping.getAttribute() instanceof AttributeRequirement)
                {
                    AttributeRequirement att = (AttributeRequirement) mapping.getAttribute();
                    style.setIsText(att.isText());
                }
                types.add(style);
            }
            else if (mapping.getElement() instanceof Regex)
            {
                Regex recognizedRegex = (Regex) mapping.getElement();
                org.topcased.typesmodel.model.inittypes.Regex regex = InittypesFactory.eINSTANCE.createRegex();
                regex.setExpression(recognizedRegex.getRegex());
                regex.setName(mapping.getAttribute().getProperName());
                if (mapping.getAttribute() instanceof AttributeRequirement)
                {
                    AttributeRequirement att = (AttributeRequirement) mapping.getAttribute();
                    regex.setIsText(att.isText());
                }
                types.add(regex);
            }
        }
        return types;
    }

    /**
     * Gets the end text description
     * 
     * @return
     */
    public String getDescriptionEndText()
    {
        if (endText != null)
        {
            return endText;
        }
        if (documentType == null)
        {
            return null;
        }
        return documentType.getTextType();
    }

    /**
     * Gets the description Regex
     * 
     * @return
     */
    public String getDescriptionRegex()
    {
        if (descriptionRegex != null)
        {
            return descriptionRegex;
        }
        if (documentType == null && pageSelectFormat.getDescriptionState())
        {
            return pageSelectFormat.getDescriptionRegex();
        }
        if (documentType == null)
        {
            return null;
        }
        return documentType.getTextRegex();
    }

    /**
     * Perform a requirement attachment
     * 
     * @param myMonitor
     * @return
     */
    public AtomicBoolean attachRequirement(final IProgressMonitor myMonitor)
    {
        final AtomicBoolean result = new AtomicBoolean();
        result.set(false);
        modelToAttach = pageSelectDocument.getModeltoAttach();
        requirementToAttach = pageSelectDocument.getOutputModel();
        final String projectName = pageSelectDocument.getProjectName();
        final String projectDescription = pageSelectDocument.getProjectDescription();

        reqFile = ImportRequirementWizard.getFile(requirementToAttach);

        final IFile regFileTarget = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(modelToAttach.getFullPath().removeFileExtension().toString()));
        Display.getDefault().syncExec(new Runnable()
        {

            public void run()
            {
                try
                {
                    IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();

                    IEditorReference matchingOpenedEditorReference = null;
                    IEditorPart editor;

                    IEditorDescriptor descriptor = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(modelToAttach.getFullPath().toString());
                    String defaultEditorId = null;
                    if (descriptor != null)
                    {
                        defaultEditorId = descriptor.getId();
                    }
                    for (IEditorReference editorReference : editorReferences)
                    {
                        IEditorInput input = editorReference.getEditorInput();
                        IFile file = (IFile) input.getAdapter(IFile.class);
                        IPath path = file.getFullPath().removeFileExtension();
                        if (modelToAttach.getFullPath().removeFileExtension().equals(path))
                        {
                            MessageDialog dialog = new MessageDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), "Information", null, "The model : "
                                    + modelToAttach.getName() + " will be saved and closed.\n Would you continue?", MessageDialog.CONFIRM, new String[] {IDialogConstants.OK_LABEL,
                                    IDialogConstants.CANCEL_LABEL}, Window.OK | Window.CANCEL);
                            if (dialog.open() == Window.CANCEL)
                            {
                                result.set(false);
                                return;
                            }
                            if (editorReference.getId().equals(defaultEditorId))
                            {
                                matchingOpenedEditorReference = editorReference;
                            }
                            else
                            {
                                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editorReference.getEditor(true), true);
                            }
                        }
                    }

                    if (matchingOpenedEditorReference == null)
                    {
                        editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(modelToAttach), defaultEditorId);
                    }
                    else
                    {
                        editor = matchingOpenedEditorReference.getEditor(false);
                    }

                    if (!reqFile.exists())
                    {
                        try
                        {
                            reqFile.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
                        }
                        catch (CoreException e)
                        {
                        }
                    }

                    NewRequirementModelOperation operation = new NewRequirementModelOperation(modelToAttach, reqFile, regFileTarget);
                    operation.setProjectInformations(projectName, projectDescription);
                    operation.run(myMonitor);
                    editor.doSave(myMonitor);
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editor, true);
                    reqFile.delete(true, myMonitor);
                    result.set(true);
                }
                catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                catch (PartInitException e1)
                {
                    e1.printStackTrace();
                }
                catch (CoreException e)
                {
                    e.printStackTrace();
                }

            }
        });
        return result;
    }

    /**
     * Sets the wizard current file system
     * 
     * @param currentFileSystem the current file system
     */
    public void setCurrentFileSystem(File currentFileSystem)
    {
        wizard.setCurrentFileSystem(currentFileSystem);
    }

    /**
     * set the wizard current file
     * 
     * @param currentFile the wizard current file
     */
    public void setCurrentFile(IFile currentFile)
    {
        wizard.setCurrentFile(currentFile);
    }

    /**
     * Gets the description state
     * 
     * @return the description state
     */
    public boolean getDescriptionState()
    {
        return pageSelectFormat.getDescriptionState();
    }

    /**
     * get the level
     * 
     * @return the level
     */
    public String getLevel()
    {
        return pageSelectDocument.getLevel();
    }

    /**
     * return if the description regex has been set or not
     * 
     * @return
     */
    public boolean isDescriptionRegex()
    {
        return pageSelectFormat.isDescriptionRegex();
    }

    /**
     * return if the description End Label has been set or not
     * 
     * @return
     */
    public boolean isDescriptionText()
    {
        return pageSelectFormat.isDescriptionText();
    }

    /**
     * Gets the identification.
     * 
     * @return the identification
     */
    public RecognizedElement getIdentification()
    {
        return pageSelectFormat.getIdentification();
    }

    /**
     * Gets the list mapping.
     * 
     * @return the list mapping
     */
    public Collection<Mapping> getListMapping()
    {
        return pageMapping.getListMapping();
    }

    /**
     * Checks if is spreadsheet.
     * 
     * @return true, if is spreadsheet
     */
    public boolean isSpreadsheet()
    {
        return pageSelectFormat.isSpreadsheet();
    }

    /**
     * Gets the profile uri.
     * 
     * @return the profile uri
     */
    public String getProfileURI()
    {
        return pageSelectDocument.getProfileURI();
    }

    /**
     * Gets the stereotype.
     * 
     * @return the stereotype
     */
    public Stereotype getStereotype()
    {
        return pageSelectDocument.getStereotype();
    }

    /**
     * Gets the output model.
     * 
     * @return the output model
     */
    public String getOutputModel()
    {
        return pageSelectDocument.getOutputModel();
    }

    /**
     * Gets the list attributes.
     * 
     * @return the list attributes
     */
    public Collection<Attribute> getListAttributes()
    {
        return pageMapping.getListAttributes();
    }

    /**
     * Gets the profile.
     * 
     * @return the profile
     */
    public Profile getProfile()
    {
        return pageSelectDocument.getProfile();
    }

    /**
     * Sets the document file.
     * 
     * @param documentFile the new document file
     */
    public void setDocumentFile(File currentFileSystem)
    {
        pageSelectFormat.setDocumentFile(currentFileSystem);
    }

    /**
     * Gets if an attach requirement is selected
     * 
     * @return
     */
    public boolean isAttachRequirement()
    {
        return pageSelectDocument.isAttachRequirement();
    }

    /**
     * Gets the input document.
     * 
     * @return the input document
     */
    public String getInputDocument()
    {
        return pageSelectDocument.getInputDocument();
    }

    /**
     * Gets the stereotype name.
     * 
     * @return the stereotype name
     */
    public String getStereotypeName()
    {
        return pageSelectDocument.getStereotypeName();
    }

    /**
     * Gets the value to recognize req.
     * 
     * @return the value to recognize req
     */
    public String getValueToRecognizeReq()
    {
        return pageSelectFormat.getValueToRecognizeReq();
    }

    /**
     * Gets the list attributes pref.
     * 
     * @return the list attributes pref
     */
    public String getListAttributesPref()
    {
        return pageSelectFormat.getListAttributesPref();
    }

    /**
     * Gets the list mapping pref.
     * 
     * @return the list mapping pref
     */
    public String getListMappingPref()
    {
        return pageMapping.getListMappingPref();
    }

    /**
     * Sets the description End text
     * 
     * @param endText the description End Text
     */
    public void setDescriptionEndText(String endText)
    {
        this.endText = endText;
    }

    /**
     * Sets the description Regex
     * 
     * @param descriptionRegex the description regex
     */
    public void setDescriptionRegex(String descriptionRegex)
    {
        this.descriptionRegex = descriptionRegex;
    }

    /**
     * Set if it's hierarchical or not
     * 
     * @param hierarchical
     */
    public void setHierarchical(boolean hierarchical)
    {
        hierarchicalSet = true;
        this.hierarchical = hierarchical;
    }

    /**
     * Sets the req ID
     * 
     * @param identifier
     */
    public void setIdentifier(RecognizedElement identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Clears the req ID, the end Text description and the regex description
     */
    public void clear()
    {
        identifier = null;
        endText = null;
        descriptionRegex = null;
    }

    /**
     * Remove the selection of the types document
     */
    public void removeDocumentType()
    {
        documentType = null;
        pageSelectDocument.clearDocumentType();
    }

}
