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
package org.topcased.requirement.generic.importrequirement.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.topcased.doc2model.parser.Doc2ModelParser;
import org.topcased.doc2model.parser.ProgressionObserver;
import org.topcased.requirement.generic.importrequirement.Activator;
import org.topcased.requirement.generic.importrequirement.doc2model.Doc2ModelCreator;
import org.topcased.requirement.generic.importrequirement.elements.Attribute;
import org.topcased.requirement.generic.importrequirement.elements.AttributeSysml;
import org.topcased.requirement.generic.importrequirement.elements.AttributeSysmlReference;
import org.topcased.requirement.generic.importrequirement.elements.AttributeUml;
import org.topcased.requirement.generic.importrequirement.elements.Mapping;
import org.topcased.requirement.generic.importrequirement.elements.RecognizedTree;
import org.topcased.requirement.generic.importrequirement.utils.Constants;
import org.topcased.sam.requirement.RequirementFactory;
import org.topcased.sam.requirement.RequirementProject;
import org.topcased.sam.requirement.core.preferences.CurrentPreferenceHelper;

import doc2modelMapping.doc2model;

/**
 * The Class ImportRequirementWizard.
 */
public class ImportRequirementWizard extends Wizard implements IImportWizard
{

    private static final String CONSTANT_DEBUG = "ADMIN_DOC_MAPPING";

    /** The current file. */
    private IFile currentFile;

    /** The stereotype. */
    private Stereotype stereotype;

    /** The tree. */
    private RecognizedTree tree = new RecognizedTree();

    /** The list attributes. */
    private Collection<Attribute> listAttributes = new LinkedList<Attribute>();

    /** The page1. */
    private ImportRequirementWizardPageSelectDocument page1;

    /** The page2. */
    private ImportRequirementWizardPageSelectFormat page2;

    /** The page3. */
    private ImportRequirementWizardPageMapping page3;
    
    /** The file   */
    private File currentFileSystem;
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        setWindowTitle("File Import Wizard");
        setNeedsProgressMonitor(true);
        String valueForInput = null;
        if (selection.getFirstElement() instanceof IFile)
        {
            currentFile = (IFile) selection.getFirstElement();
            if ("docx".equals(currentFile.getLocation().getFileExtension().toLowerCase()) || "csv".equals(currentFile.getLocation().getFileExtension().toLowerCase())
                    || "xlsx".equals(currentFile.getLocation().getFileExtension().toLowerCase()) || "odt".equals(currentFile.getLocation().getFileExtension().toLowerCase())
                    || "ods".equals(currentFile.getLocation().getFileExtension().toLowerCase()))
            {
                valueForInput = currentFile.getLocationURI().toString();
            }
        }
        // Create page one
        page1 = new ImportRequirementWizardPageSelectDocument("Select Document", valueForInput);
        addPage(page1);

        // Create page two
        page2 = new ImportRequirementWizardPageSelectFormat("Enter the file format", currentFile != null ? currentFile.getLocation().toFile() : null, tree);
        addPage(page2);

        // Create page three
        page3 = new ImportRequirementWizardPageMapping("Enter the file format", tree, listAttributes, page1.getModelType());
        addPage(page3);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish()
    {
        String pathForDebug = getPathForDebug(page1.getLevel());
        /** Process **/
        Doc2ModelCreator d2mc = new Doc2ModelCreator(page3.getListMapping(), page1.getModelType(), page2.isSpreadsheet(), page1.getProfileURI(), page1.getStereotype(), page2.getIsHiearachical(),
                page2.getIdentification(),pathForDebug);
        final doc2model model = d2mc.createDoc2Model();
        if (model != null)
        {
            try
            {
                getContainer().run(false, true, new IRunnableWithProgress()
                {

                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                    {
                        try
                        {
                            final IProgressMonitor myMonitor = monitor;
                            Doc2ModelParser parser = new Doc2ModelParser(currentFileSystem.getAbsolutePath(), model, page1.getOutputModel(), null, false);
                            EObject result = parser.parse(new ProgressionObserver()
                            {
                                public void worked(int i)
                                {
                                    myMonitor.worked(i);
                                }

                                public void warningOrErrorsOccurs()
                                {

                                }

                                public void setMax(int max)
                                {
                                    myMonitor.beginTask("Import", max);
                                }

                                public void notifyNoElementsFounded()
                                {
                                }

                                public boolean isCanceled()
                                {
                                    return myMonitor.isCanceled();
                                }
                            });
                            // post processes
                            assignLevel(myMonitor, result);
                            if (Constants.REQUIREMENT_EXTENSION.equals(page1.getModelType()))
                            {
                                assignAttributeConfiguration(myMonitor,result);
                            }
                            myMonitor.done();
                        }
                        catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    /**
                     * Assign attribute configuration. for attribute type
                     * 
                     * @param myMonitor the my monitor
                     * @param result the result
                     */
                    private void assignAttributeConfiguration(IProgressMonitor myMonitor, EObject result)
                    {
                        if (result instanceof RequirementProject)
                        {
                            myMonitor.beginTask("Load attribute configuration", 1);
                            RequirementProject project = (RequirementProject) result;
                            project.setAttributeConfiguration(CurrentPreferenceHelper.getConfigurationInWorkspace());
                            project.getChapter().add(RequirementFactory.eINSTANCE.createProblemChapter());
                            project.getChapter().add(RequirementFactory.eINSTANCE.createTrashChapter());
                            project.getChapter().add(RequirementFactory.eINSTANCE.createUntracedChapter());
                            try
                            {
                                result.eResource().save(Collections.EMPTY_MAP);
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                    /**
                     * Assign level.
                     * 
                     * @param myMonitor the my monitor
                     * @param result the result
                     */
                    private void assignLevel(final IProgressMonitor myMonitor, EObject result)
                    {
                        // sysml or uml so we apply eannotations
                        if (result != null)
                        {
                            if (page1.getLevel() != null && page1.getLevel().length() > 0)
                            {
                                myMonitor.beginTask("Assign level", 1);
                                Resource r = result.eResource();
                                for (Iterator<EObject> i = r.getAllContents(); i.hasNext();)
                                {
                                    EObject tmp = i.next();
                                    if (tmp instanceof EModelElement)
                                    {
                                        EModelElement element = (EModelElement) tmp;
                                        EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
                                        annotation.setSource("http://www.topcased.org/author");
                                        element.getEAnnotations().add(annotation);
                                        annotation.getDetails().put("author", page1.getLevel());
                                    }
                                }
                                try
                                {
                                    r.save(Collections.EMPTY_MAP);
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
                return false;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                return false;
            }
        }
        // re fill lists
        for (Mapping m : page3.getListMapping())
        {
            m.getElement().setSelected(false);
            page3.getListAttributes().add(m.getAttribute());
        }

        /** Save preferences **/

        // Pref from the first page
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectDocument.PREFERENCE_FOR_INPUT_DOC, page1.getInputDocument());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectDocument.PREFERENCE_FOR_OUTPUT_MODEL, page1.getOutputModel());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectDocument.PREFERENCE_FOR_LEVEL, page1.getLevel());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectDocument.PREFERENCE_FOR_STEREO, page1.getStereotypeName());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectDocument.PREFERENCE_FOR_PROFILE, page1.getProfileURI());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectDocument.PREFERENCE_FOR_MODEL_TYPE, page1.getModelType());

        // Pref from the second page
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectFormat.PREFERENCE_FOR_CHAPTER, page2.getIsHiearachical());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectFormat.PREFERENCE_FOR_VALUE_TO_RECOGNIZE_REQ, page2.getValueToRecognizeReq());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectFormat.PREFERENCE_FOR_LIST_RECOGNIZED_ELEMENT, page2.getListAttributesPref());

        // Pref from the third page

        // Save only if it is requirement model
        if (Constants.REQUIREMENT_EXTENSION.equals(page1.getModelType()))
        {
            Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageMapping.PREFERENCE_FOR_LIST_ATTRIBUT, page3.getListAttributesPref());
        }
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageMapping.PREFERENCE_FOR_LIST_MAPPING, page3.getListMappingPref());

        return true;
    }

    private String getPathForDebug(String level)
    {
        String result = null ;
        Pattern p = Pattern.compile(CONSTANT_DEBUG + " (.*)");
        Matcher m = p.matcher(level);
        if (m.matches())
        {
            result = m.group(1);
        }
        return result;
    }

    /**
     * Gets the stereotype.
     * 
     * @return the stereotype
     */
    public Stereotype getStereotype()
    {
        return stereotype;
    }

    /**
     * Sets the stereotype.
     * 
     * @param stereotype the new stereotype
     */
    public void setStereotype(Stereotype stereotype)
    {
        this.stereotype = stereotype;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
     */
    @Override
    public IWizardPage getNextPage(IWizardPage page)
    {

        IWizardPage newPage = super.getNextPage(page);
        // From page 1 to page 2
        if (newPage instanceof ImportRequirementWizardPageSelectFormat)
        {
            if (page1.getInputDocument().endsWith(".docx") || page1.getInputDocument().endsWith(".odt"))
            {
                page2.setIsSpreadsheet(false);
            }
            else
            {
                page2.setIsSpreadsheet(true);
            }
            String inputDocument = page1.getInputDocument();
            if (inputDocument != null)
            {
                if (inputDocument.contains("file:"))
                {
                    currentFileSystem = new File(URI.createURI(inputDocument).toFileString());
                }
                else if (inputDocument.contains("platform:"))
                {
                    currentFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(URI.createURI(page1.getInputDocument()).toPlatformString(true)));
                    currentFileSystem = currentFile.getLocation().toFile();
                }
                else
                {
                    currentFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(URI.createURI(page1.getInputDocument()).toFileString()));
                    currentFileSystem = currentFile.getLocation().toFile();
                }
            }
            page2.setDocumentFile(currentFileSystem);
        }
        else if (newPage instanceof ImportRequirementWizardPageMapping)
        {
            // From page 2 to page 3

            // Load pref attributes
            page3.clearListAttributes();
            if (Constants.REQUIREMENT_EXTENSION.equals(page1.getModelType()))
            {
                page3.setIsRequirementModel(true);
                page3.loadPreferenceAttributes();
            }
            else
            {
                page3.setIsRequirementModel(false);
                // if(Constants.UML_EXTENSION.equals(page1.getModelType()))
                // {
                // listAttributes.add(new AttributeUml("Name", false, "Class"));
                // }
                // else
                // {
                // listAttributes.add(new AttributeSysml("Name", false, "Requirement"));
                // }
                if (Constants.SYSML_EXTENSION.equals(page1.getModelType()))
                {
                    manageSysml();
                }
                if (Constants.UML_EXTENSION.equals(page1.getModelType()) || Constants.SYSML_EXTENSION.equals(page1.getModelType()))
                {
                    manageProfiles();
                }
            }
            page3.refreshLists();
            // Load pref mapping
            if (page1.loadMappingPref())
            {
                page3.loadPreferecencesMapping();
            }
            else
            {
                page3.clearListMapping();
            }
        }
        return newPage;
    }

    private void manageProfiles()
    {
        if (page1.getProfile() != null && page1.getStereotype() != null)
        {
            String profileName = page1.getProfile().getName();
            // Get all the properties
            Iterator<Property> iter = page1.getStereotype().getAllAttributes().iterator();
            while (iter.hasNext())
            {
                Property next = iter.next();
                if (next.getName() != null && !next.getName().contains("base_"))
                {
                    if (Constants.UML_EXTENSION.equals(page1.getModelType()))
                    {
                        if (!isRef(next) || (next.getType() != null && next.getType().getName() != null && "class".equals(next.getType().getName().toLowerCase())))
                        {
                            AttributeUml uml = new AttributeUml(next.getName(), isRef(next), profileName, next.getName(), next.getType().getName());
                            if (!contains(listAttributes, uml))
                            {
                                listAttributes.add(uml);
                            }
                        }
                    }
                    else
                    {
                        AttributeSysml sysML = new AttributeSysml(next.getName(), isRef(next), profileName, next.getName(), next.getType().getName());
                        if (!contains(listAttributes, sysML))
                        {
                            listAttributes.add(sysML);
                        }
                    }
                }
            }
        }
    }

    private void manageSysml()
    {
        LinkedList<Attribute> defaultList = new LinkedList<Attribute>();
        Attribute text = new AttributeSysml("text", false, "Requirement");
        defaultList.add(text);
        defaultList.add(new AttributeSysmlReference("Dependency", true, "Requirement", "Dependency"));
        defaultList.add(new AttributeSysmlReference("Derive", true, "Requirement", "DeriveReqt"));
        defaultList.add(new AttributeSysmlReference("Refine", true, "Requirement", "Refine"));
        defaultList.add(new AttributeSysmlReference("Satisfy", true, "Requirement", "Satisfy"));
        defaultList.add(new AttributeSysmlReference("Copy", true, "Requirement", "Copy"));
        defaultList.add(new AttributeSysmlReference("Trace", true, "Requirement", "Trace"));
        Collection<Attribute> attributesInMaping = new LinkedList<Attribute>();
        for (Attribute a : defaultList)
        {
            if (!contains(listAttributes, a) && !contains(attributesInMaping, a))
            {
                listAttributes.add(a);
            }
        }
        for (Mapping m : page3.getListMapping())
        {
            attributesInMaping.add(m.getAttribute());
        }
    }

    /**
     * Checks if is ref.
     * 
     * @param next the next
     * 
     * @return true, if is ref
     */
    private boolean isRef(Property next)
    {
        boolean isRef = next.getType() != null && !(next.getType() instanceof DataType);
        return isRef;
    }

    /**
     * Contains.
     * 
     * @param listAttributes the list attributes
     * @param att the att
     * 
     * @return true, if successful
     */
    public static boolean contains(Collection<Attribute> listAttributes, Attribute att)
    {
        for (Attribute a : listAttributes)
        {
            if (a.getName() != null && a.getName().equals(att.getName()) && a.getSource() != null && a.getSource().equals(att.getSource()) && a.getOriginalName() != null
                    && a.getOriginalName().equals(att.getOriginalName()))
            {
                return true;
            }
        }
        return false;
    }

}
