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
package org.topcased.requirement.document.doc2model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.topcased.doc2model.parser.Doc2ModelParser;
import org.topcased.doc2model.parser.ProgressionObserver;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.extensions.IImportDocument;
import org.topcased.requirement.core.preferences.CurrentPreferenceHelper;
import org.topcased.requirement.document.checker.DescriptionChecker;
import org.topcased.requirement.document.elements.RecognizedElement;
import org.topcased.requirement.document.elements.Regex;
import org.topcased.requirement.document.elements.Style;
import org.topcased.requirement.document.utils.Constants;
import org.topcased.requirement.document.utils.DocumentTypeParser;
import org.topcased.typesmodel.model.inittypes.DocumentType;

import doc2modelMapping.doc2model;

/**
 * Import a requirement document without using the wizard
 *
 */
public class Doc2ModelImportDocument implements IImportDocument
{
    private DocumentType type;
    private URI document;
    private URI outputModel;
    
    
    public Doc2ModelImportDocument()
    {
    }
    
    private void getDocument(IProgressMonitor monitor)
    {
        String pathForDebug = null; //getPathForDebug(page1.getLevel());
        
        DocumentTypeParser parser = new DocumentTypeParser(type);
        
        String description = parser.getDescription();
        
        if (description != null)
        {
            DescriptionChecker.setEndText(description);
        }
        
        String idType = getIdType();
        RecognizedElement id = parser.getIdentification(idType);
        
        if (id instanceof Style)
        {
            DescriptionChecker.setStyleIdent(((Style) id).getStyle());
        }
        else if (id instanceof Regex)
        {
            DescriptionChecker.setReqIdent(((Regex) id).getRegex());
        }
        
        
        
        
        /** Process **/
        Doc2ModelCreator d2mc = new Doc2ModelCreator(parser.getMapping(idType), Constants.REQUIREMENT_EXTENSION, Constants.COLUMN_TYPE.equals(getIdType()), null, null, parser.getIsHiearachical(),
                id, pathForDebug);
        final doc2model model = d2mc.createDoc2Model();
        if (model != null)
        {
            IRunnableWithProgress runnable = new IRunnableWithProgress()
            {
                
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                    try
                    {
                        
                        IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(document.toFileString()));
                        final IProgressMonitor myMonitor = monitor;
                        Doc2ModelParser parser = new Doc2ModelParser(file.getLocationURI().getPath(), model, outputModel.toString(), null, false);
                        EObject result = parser.parse(new ProgressionObserver()
                        {
                            public void worked(int i)
                            {
                                if (myMonitor != null)
                                {
                                myMonitor.worked(i);
                                }
                            }
                            
                            public void warningOrErrorsOccurs()
                            {
                                
                            }
                            
                            public void setMax(int max)
                            {
                                if (myMonitor != null)
                                {
                                myMonitor.beginTask("Import", max);
                                }
                            }
                            
                            public void notifyNoElementsFounded()
                            {
                            }
                            
                            @SuppressWarnings("null")
                            public boolean isCanceled()
                            {
                                if (myMonitor != null)
                                {
                                    return false;
                                } 
                                return myMonitor.isCanceled();
                                
                            }
                        });
                        // post processes
                        assignAttributeConfiguration(myMonitor, result);
                        if (myMonitor != null)
                        {
                            myMonitor.done();
                        }
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
                        if (myMonitor != null)
                        {
                            myMonitor.beginTask("Load attribute configuration", 1);   
                        }
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
            };
            try
            {
                runnable.run(monitor);
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private String getIdType()
    {
        if (document == null)
        {
            return null;
        }
        if ("docx".equals(document.fileExtension()) || "odt".equals(document.fileExtension()))
        {
            return Constants.REGEX_STYLE_TYPE;
        }
            return Constants.COLUMN_TYPE;
    }
    
    /**
     * Import the requirement file at the specified uri
     * 
     * @param type the document type used to configure the import requirement
     * @param document the document used to import the requirement
     * @param outputModel the output requirement file
     * @param monitor the progress monitor
     */
    public void getDocument(DocumentType type, URI document, URI outputModel, IProgressMonitor monitor)
    {
        this.type = type;
        this.document = document;
        this.outputModel = outputModel;
        getDocument(monitor);

    }

}
