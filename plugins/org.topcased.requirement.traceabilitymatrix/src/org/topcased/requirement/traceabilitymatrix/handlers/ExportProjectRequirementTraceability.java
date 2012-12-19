/*****************************************************************************
 * Copyright (c) 2012 Atos.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Laurent DEVERNAY (Atos) laurent.devernay@tos.net -
 *  Cyril MARCHIVE (Atos) cyril.marchive@atos.net 
 *
 *****************************************************************************/
package org.topcased.requirement.traceabilitymatrix.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.papyrus.core.modelsetquery.IModelSetQueryAdapter;
import org.eclipse.papyrus.core.modelsetquery.ModelSetQuery;
import org.eclipse.papyrus.core.modelsetquery.impl.ModelSetQueryAdapterSizeMatters;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.traceabilitymatrix.Activator;
import org.topcased.requirement.traceabilitymatrix.generator.template.GeneratorHelper;

import ttm.Document;
import ttm.TtmPackage;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

public class ExportProjectRequirementTraceability
{
    private EObject currObject = null;

    private IContainer project = null;

    private boolean openFile = false;

    private String destinationFolder = null;

    public ExportProjectRequirementTraceability(String textProject, String textOutput, boolean openFile)
    {
        this.openFile = openFile;
        this.destinationFolder = textOutput;
        // We fetch the ressource selected to get its project
        IWorkspaceRoot wks = ResourcesPlugin.getWorkspace().getRoot();
        // IPath path = new Path(textProject.getText());
        Path path = new Path(textProject);
        if (path.segmentCount() == 1)
        {
            project = wks.getProject(path.lastSegment());
        }
        else
        {
            project = wks.getFolder(path);
        }
        // project = res.getProject();
        final ResourceSet set = new ResourceSetImpl();
        set.eAdapters().add(new ModelSetQueryAdapterSizeMatters());
        set.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
        set.getLoadOptions().put(XMLResource.OPTION_DEFER_ATTACHMENT, true);
        try
        {
            project.accept(new IResourceVisitor()
            {

                public boolean visit(IResource resource) throws CoreException
                {
                    if (Messages.ExportProjectRequirementTraceability_REQUIREMENT_FILE_EXTENSION.equals(resource.getFileExtension()))
                    {
                        set.getResource(URI.createPlatformResourceURI(resource.getFullPath().toString(), true), true);
                    }
                    return true;
                }
            });
            currObject = set.getResources().get(0).getContents().get(0);
        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        try
        {
            String ligne;
            // Creation of the destination file
            DateFormat dateFormat = new SimpleDateFormat(Messages.ExportProjectRequirementTraceability_DATE_FORMAT);
            String fileName = project.getName() + "_" + dateFormat.format(new Date()) + Messages.ExportProjectRequirementTraceability_XLS_EXTENSION; //$NON-NLS-1$
            File newTextFile = new File(project.getLocationURI().getPath() + File.separator + fileName); //$NON-NLS-1$
            newTextFile.createNewFile();
            StringBuffer buff = new StringBuffer();
            // Header and footer for the XML file
            BufferedReader header = new BufferedReader(new InputStreamReader(
                    Activator.getDefault().getBundle().getResource(Messages.ExportProjectRequirementTraceability_HEADER).openStream(), Messages.ExportProjectRequirementTraceability_CHARSET)); 
            BufferedReader footer = new BufferedReader(new InputStreamReader(
                    Activator.getDefault().getBundle().getResource(Messages.ExportProjectRequirementTraceability_FOOTER).openStream(), Messages.ExportProjectRequirementTraceability_CHARSET)); 
            // We add the XML/Excel header to the file
            while ((ligne = header.readLine()) != null)
            {
                buff.append(ligne);
            }

            buff = export(buff);
            // We add the XML/Excel footer to the file
            while ((ligne = footer.readLine()) != null)
            {
                buff.append(ligne);
            }
            // This content is added to the file
            FileOutputStream fos = new FileOutputStream(newTextFile);
            Writer out = new BufferedWriter(new OutputStreamWriter(fos, Messages.ExportProjectRequirementTraceability_CHARSET));
            out.write(buff.toString());
            out.flush();
            out.close();
            project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());

            // The file is opened with Excel
            if (openFile)
            {
                IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(destinationFolder + "/" + fileName)); //$NON-NLS-1$
                file.getParent().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
                IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file);
            }
            ResourceSet set = currObject.eResource().getResourceSet();
            for (Resource r : set.getResources())
            {
                try
                {
                    r.unload();
                }
                catch (Exception e)
                {
                }
            }
            set.getResources().clear();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private StringBuffer export(StringBuffer buff)
    {

        // Get the current selection
        if (currObject instanceof EObject)
        {
            EObject obj = (EObject) currObject;
            IModelSetQueryAdapter q = ModelSetQuery.getExistingTypeCacheAdapter(obj);
            if (q == null)
            {
                obj.eResource().getResourceSet().eAdapters().add(new ModelSetQueryAdapterSizeMatters());
            }

            // Iterator containing all upstream requirements
            Iterator<RequirementProject> allRequirementProjects = (Iterator<RequirementProject>) Iterators.filter(
                    q.getReachableObjectsOfType(obj, RequirementPackage.Literals.REQUIREMENT_PROJECT).iterator(), RequirementProject.class);
            Iterator<Document> alldocuments = (Iterator<Document>) Iterators.filter(q.getReachableObjectsOfType(obj, TtmPackage.Literals.DOCUMENT).iterator(), Document.class);
            SortedSet<Attribute> allAttribute = new TreeSet<Attribute>(new Comparator<Attribute>()
            {
                public int compare(Attribute arg0, Attribute arg1)
                {
                    if (arg0 != null && arg1 != null)
                    {
                        return arg0.getName().compareTo(arg1.getName());
                    }
                    return 0;
                }
            });
            allAttribute.addAll(Sets.newHashSet(Iterators.filter(q.getReachableObjectsOfType(obj, RequirementPackage.Literals.ATTRIBUTE).iterator(), Attribute.class)));
            RequirementProject requirementProject = (RequirementProject) allRequirementProjects.next();
            buff.append("<Worksheet ss:Name='" + requirementProject.getIdentifier() + "'><Table>"); //$NON-NLS-1$ //$NON-NLS-2$
            buff.append(GeneratorHelper.COLUMNS_START);
            buff.append(GeneratorHelper.addColumn(Messages.ExportProjectRequirementTraceability_DOCUMENT_COL_TITLE));
            buff.append(GeneratorHelper.addColumn(Messages.ExportProjectRequirementTraceability_UPSTREAM_COL_TITLE));
            buff.append(GeneratorHelper.addColumn(Messages.ExportProjectRequirementTraceability_PARTIAL_COL_TITLE));
            buff.append(GeneratorHelper.addColumn(Messages.ExportProjectRequirementTraceability_CURRENT_ID_COL_TITLE));
            buff.append(GeneratorHelper.addColumn(Messages.ExportProjectRequirementTraceability_LINK_COL_TITLE));
            buff.append(GeneratorHelper.addColumn(Messages.ExportProjectRequirementTraceability_ASSOCIATED_COL_TITLE));
            buff.append(GeneratorHelper.addColumn(Messages.ExportProjectRequirementTraceability_ASSOCIATED_PATH_COL_TITLE));
            buff.append(GeneratorHelper.addColumn(Messages.ExportProjectRequirementTraceability_DESCRIPTION_COL_TITLE));
            buff.append(GeneratorHelper.configuration(allAttribute));
            buff.append(GeneratorHelper.COLUMNS_END);
            while (alldocuments.hasNext())
            {
                Document document = (Document) alldocuments.next();
                Iterator<EObject> ite = document.eAllContents();
                while (ite.hasNext())
                {
                    EObject hierarchicalElement = (EObject) ite.next();
                    if (hierarchicalElement instanceof ttm.Requirement)
                    {
                        ttm.Requirement req = (ttm.Requirement) hierarchicalElement;
                        buff.append(GeneratorHelper.details(req, document, allAttribute));
                    }
                }
            }
            buff.append("</Table></Worksheet>"); //$NON-NLS-1$
            return buff;
        }
        return null;
    }
}