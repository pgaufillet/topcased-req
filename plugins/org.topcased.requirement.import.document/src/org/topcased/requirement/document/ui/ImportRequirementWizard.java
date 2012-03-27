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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.topcased.doc2model.parser.Doc2ModelParser;
import org.topcased.doc2model.parser.ProgressionObserver;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.preferences.CurrentPreferenceHelper;
import org.topcased.requirement.core.preferences.RequirementPreferenceConstants;
import org.topcased.requirement.document.Activator;
import org.topcased.requirement.document.checker.DescriptionChecker;
import org.topcased.requirement.document.doc2model.Doc2ModelCreator;
import org.topcased.requirement.document.elements.Attribute;
import org.topcased.requirement.document.elements.Mapping;
import org.topcased.requirement.document.elements.PageController;
import org.topcased.requirement.document.elements.RecognizedElement;
import org.topcased.requirement.document.elements.RecognizedTree;
import org.topcased.requirement.document.elements.Regex;
import org.topcased.requirement.document.elements.Style;
import org.topcased.requirement.document.utils.Constants;

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

    /** The file */
    private File currentFileSystem;

    /** the controller */
    private PageController pageController;

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
        
        pageController = new PageController(this);
        
        List<WizardPage> pages = pageController.createPages(valueForInput, currentFile != null ? currentFile.getLocation().toFile() : null, tree, listAttributes);
        for (WizardPage wizardPage : pages) {
			addPage(wizardPage);
		}
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish()
    {
        String pathForDebug = getPathForDebug(pageController.getLevel());

        if (pageController.getDescriptionState())
        {
            if (pageController.isDescriptionText())
            {
                DescriptionChecker.setEndText(pageController.getDescriptionEndText());
            }
            if (pageController.isDescriptionRegex())
            {
                DescriptionChecker.setRegDescription(pageController.getDescriptionRegex());
            }
        }
        RecognizedElement id = pageController.getIdentification();
        if (id instanceof Style)
        {
            DescriptionChecker.setStyleIdent(((Style) id).getStyle());
            String regex = ((Style) id).getRegex();
            if (regex != null)
            {
                DescriptionChecker.setReqIdent(regex);
            }
        }
        else if (id instanceof Regex)
        {
            DescriptionChecker.setReqIdent(((Regex) id).getRegex());
        }

        /** Process **/
        Doc2ModelCreator d2mc = new Doc2ModelCreator(pageController.getListMapping(), pageController.getModelType(), pageController.isSpreadsheet(), pageController.getProfileURI(), pageController.getStereotype(), pageController.isHierarchical(),
                pageController.getIdentification(), pathForDebug);
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
                            Doc2ModelParser parser = new Doc2ModelParser(currentFileSystem.getAbsolutePath(), model, pageController.getOutputModel(), null, false);
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
                            if (Constants.REQUIREMENT_EXTENSION.equals(pageController.getModelType()))
                            {
                                assignAttributeConfiguration(myMonitor, result);
                            }
                            IFile file = getFile(pageController.getOutputModel());
                            if (file != null && file.exists())
                            {
                                try
                                {
                                    file.getParent().refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
                                }
                                catch (CoreException e)
                                {
                                }
                            }
                            if (pageController.isAttachRequirement())
                            {
                                myMonitor.beginTask("Attaching requirement", 3);
                                if (!getPageController().attachRequirement(myMonitor).get())
                                {
                                    throw new InvocationTargetException(new Exception("Action Cancelled"));
                                }
                            }
                            myMonitor.done();
                        }
                        catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                        finally
                        {
                            DescriptionChecker.rollback();
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
                            if (pageController.getLevel() != null && pageController.getLevel().length() > 0)
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
                                        annotation.getDetails().put("author", pageController.getLevel());
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
        for (Mapping m : pageController.getListMapping())
        {
            m.getElement().setSelected(false);
            pageController.getListAttributes().add(m.getAttribute());
        }

        /** Save preferences **/

        // Pref from the first page
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectDocument.PREFERENCE_FOR_INPUT_DOC, pageController.getInputDocument());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectDocument.PREFERENCE_FOR_OUTPUT_MODEL, pageController.getOutputModel());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectDocument.PREFERENCE_FOR_LEVEL, pageController.getLevel());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectDocument.PREFERENCE_FOR_STEREO, pageController.getStereotypeName());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectDocument.PREFERENCE_FOR_PROFILE, pageController.getProfileURI());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectDocument.PREFERENCE_FOR_MODEL_TYPE, pageController.getModelType());

        // Pref from the second page
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectFormat.PREFERENCE_FOR_CHAPTER, pageController.isHierarchical());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectFormat.PREFERENCE_FOR_VALUE_TO_RECOGNIZE_REQ, pageController.getValueToRecognizeReq());
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageSelectFormat.PREFERENCE_FOR_LIST_RECOGNIZED_ELEMENT, pageController.getListAttributesPref());

        // Pref from the third page

        // Save only if it is requirement model
        if (Constants.REQUIREMENT_EXTENSION.equals(pageController.getModelType()))
        {
            Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageMapping.PREFERENCE_FOR_LIST_ATTRIBUT, pageController.getListAttributesPref());
        }
        Activator.getDefault().getPluginPreferences().setValue(ImportRequirementWizardPageMapping.PREFERENCE_FOR_LIST_MAPPING, pageController.getListMappingPref());

        IPreferenceStore preferenceStorePlugIn = RequirementCorePlugin.getDefault().getPreferenceStore();
        if (!preferenceStorePlugIn.getBoolean(RequirementPreferenceConstants.IMPORT_REQUIREMENT_WITHOUT_DIALOG))
        {
            MessageDialog dialog = new MessageDialog(getShell(), "Information", null, "The .requirement file is generated in : " + pageController.getOutputModel(), MessageDialog.INFORMATION,
                    new String[] {IDialogConstants.OK_LABEL}, Window.OK);
            dialog.open();
        }

        return true;
    }

    /**
     * Transforms an String path to IFile
     * 
     * @return
     */
    public static IFile getFile(String argPath)
    {
        URI uri = URI.createURI(argPath);
        String path = null;
        if (uri.isFile())
        {
            path = "/" + uri.deresolve(URI.createURI(EcorePlugin.getWorkspaceRoot().getLocationURI().toString() + "/"), false, false, true).toString();
        }
        else
        {
            path = uri.toString();
        }
        IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
        return file;
    }

    private String getPathForDebug(String level)
    {
        String result = null;
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

    /**
     * Checks if is ref.
     * 
     * @param next the next
     * 
     * @return true, if is ref
     */
    public static boolean isRef(Property next)
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

    /**
     * Gets the page Controller
     * 
     * @return
     */
    public PageController getPageController()
    {
        return pageController;
    }

    /**
     * The current File System Variable
     * 
     * @param currentFileSystem
     */
    public void setCurrentFileSystem(File currentFileSystem)
    {
        this.currentFileSystem = currentFileSystem;
        pageController.setDocumentFile(currentFileSystem);
    }

    /**
     * Sets the current File Variable
     * 
     * @param currentFile
     */
    public void setCurrentFile(IFile currentFile)
    {
        this.currentFile = currentFile;
    }

}
