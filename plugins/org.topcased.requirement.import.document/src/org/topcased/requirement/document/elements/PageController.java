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

import java.lang.reflect.InvocationTargetException;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
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

    private ImportRequirementWizard wizard;

    private DocumentType documentType;

    private String modelType;

    private boolean loadMappingPref;

    private Map<RecognizedElement, Type> recognizedElementToType;

    private List<Mapping> mapping;

    private IFile modelToAttach;

    private String requirementToAttach;
    
    private IFile reqFile;

    /**
     * constructor 
     * 
     * @param wizard the import requirement wizard
     */
    public PageController(ImportRequirementWizard wizard)
    {
        this.wizard = wizard;
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
        this.modelType = modelType;
        this.documentType = documentType;
        this.loadMappingPref = loadMappingPref;
        wizard.getContainer().getCurrentPage();
        for (IWizardPage iter : wizard.getPages())
        {
            if (iter instanceof ImportRequirementWizardPageSelectFormat)
            {
                ImportRequirementWizardPageSelectFormat page = (ImportRequirementWizardPageSelectFormat) iter;
                page.pageChanged(InputDocument);
            }
        }
    }

    /**
     *  return the type model element
     * @return
     */
    public String getModelType()
    {
        return modelType;
    }
    
    /**
     * Called when select format page of import requirement document changes
     * 
     * @param recognizedTree the recognized tree of select format page
     */
    public void pageSelectFormatChanged(RecognizedTree recognizedTree)
    {

        updateMap(recognizedTree);

        for (IWizardPage iter : wizard.getPages())
        {
            if (iter instanceof ImportRequirementWizardPageMapping)
            {
                ImportRequirementWizardPageMapping page = (ImportRequirementWizardPageMapping) iter;
                page.pageChanged(getAttributes(), getAllMapping(), modelType);
            }
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

    private List<Mapping> getAllMapping()
    {
        return mapping;
    }

    /**
     * return list of attributes from the types documents then are mapped with a column, style or regex
     * 
     * @return list of attributes
     */
    public List<Attribute> getAttributes()
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
                AttributeRequirement attributeTemp = new AttributeRequirement(entryset.getValue().getName(), false, "Requirement");
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
     * @return
     */
    public RecognizedTree getStylesAndRegex()
    {
        recognizedElementToType = new HashMap<RecognizedElement, Type>();
        if (documentType == null)
        {
            return null;
        }

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
        recognizedElementToType = new HashMap<RecognizedElement, Type>();
        if (documentType == null)
        {
            return null;
        }
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
                result = new Style(((org.topcased.typesmodel.model.inittypes.Style) id).getLabel(),"");
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
                    endText = page.getDescription();
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
        IniManager.getInstance().save(typesFile, types, id, hierarchicalToSave, endText);

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
                types.add(column);
            }
            else if (mapping.getElement() instanceof Style)
            {
                Style recognizedStyle = (Style) mapping.getElement();
                org.topcased.typesmodel.model.inittypes.Style style = InittypesFactory.eINSTANCE.createStyle();
                style.setExpression(recognizedStyle.getRegex());
                style.setLabel(recognizedStyle.getStyle());
                style.setName(mapping.getAttribute().getProperName());
                types.add(style);
            }
            else if (mapping.getElement() instanceof Regex)
            {
                Regex recognizedRegex = (Regex) mapping.getElement();
                org.topcased.typesmodel.model.inittypes.Regex regex = InittypesFactory.eINSTANCE.createRegex();
                regex.setExpression(recognizedRegex.getRegex());
                regex.setName(mapping.getAttribute().getProperName());
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
    public String getEndText()
    {
        if (documentType == null)
        {
            return null;
        }
        return documentType.getTextType();
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
        for (IWizardPage iter : wizard.getPages())
        {
            if (iter instanceof ImportRequirementWizardPageSelectDocument)
            {
                ImportRequirementWizardPageSelectDocument page = (ImportRequirementWizardPageSelectDocument) iter;
                modelToAttach = page.getModeltoAttach();
                requirementToAttach = page.getOutputModel();
                final String projectName = page.getProjectName();
                final String projectDescription = page.getProjectDescription();
                
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
                                for (IEditorReference editorReference : editorReferences) {
                                    IEditorInput input = editorReference.getEditorInput();
                                    IFile file = (IFile)input.getAdapter(IFile.class);
                                    IPath path = file.getFullPath().removeFileExtension();
                                    if (modelToAttach.getFullPath().removeFileExtension().equals(path))
                                    {
                                        MessageDialog dialog = new MessageDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), "Information", null, "The model : " + modelToAttach.getName() + " will be saved and closed.\n Would you continue?", MessageDialog.CONFIRM, new String[] {IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL}, Window.OK|Window.CANCEL);
                                        if (dialog.open() == Window.CANCEL)
                                        {
                                            result.set(false);
                                            return;
                                        }
                                        if (editorReference.getId().equals(defaultEditorId)) {
                                            matchingOpenedEditorReference = editorReference;
                                        } else {
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
                                
                            if (!reqFile.exists()){
                                try
                                {
                                    reqFile.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
                                }
                                catch (CoreException e)
                                {
                                }
                            }
                            
                            NewRequirementModelOperation operation = new NewRequirementModelOperation(modelToAttach,reqFile, regFileTarget);
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
            }
        }
        return result;
    }

}
