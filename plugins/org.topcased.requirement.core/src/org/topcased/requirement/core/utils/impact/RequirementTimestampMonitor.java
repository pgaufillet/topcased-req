/***********************************************************************************************************************
 * Copyright (c) 2011 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Philippe ROLAND (Atos) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.utils.impact;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.teamhistory.TeamHistoryException;
import org.topcased.requirement.teamhistory.TeamHistoryManager;

/**
 * Handles timestamping of upstreamModels, offering impact analysis when changed are detected
 * 
 * @author proland
 */
public class RequirementTimestampMonitor
{
    public static final String HASH_KEY = "LastAnalyzedHash";

    public static final String REVISION_KEY = "LastAnalyzedRevision";

    /**
     * Checks model hash and latest revision. Offers to lead user to history view for impact analysis as needed
     * 
     * @param model the upstream model to check
     * @return in some special cases (first load) returns Command to be executed, saving current hash and revision in
     *         the model's annotations
     * @throws InterruptedException
     */
    public Command getOnLoadCommand(UpstreamModel model) throws InterruptedException
    {
        if (model == null)
        {
            return null;
        }
        RequirementProject project = (RequirementProject) model.eContainer();
        String hash = hashModel(model);
        IFile modelFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(model.eResource().getURI().toPlatformString(true)));
        String currentRevision = getRevision(modelFile);
        EAnnotation annotation = findHashAnnotation(project);

        // if no hash-bearing annotation is found, create one
        if (annotation == null)
        {
            return new UpdateCreateHashCommand(project, hash, currentRevision);
        }
        // otherwise, if hash is different, offer impact analysis
        else if (!annotation.getDetails().get(HASH_KEY).equals(hash))
        {
            String lastAnalyzedRevision = annotation.getDetails().get(REVISION_KEY);
            askUser(model, lastAnalyzedRevision, currentRevision);
        }
        return null;
    }

    /**
     * Hashes the model
     * 
     * @param model the model
     * @return the model's MD5 hash
     */
    public static String hashModel(UpstreamModel model)
    {
        Resource r = model.eResource();
        InputStream stream = null;
        try
        {
            stream = r.getResourceSet().getURIConverter().createInputStream(r.getURI());
        }
        catch (IOException e2)
        {
            RequirementCorePlugin.log(e2);
        }
        String md5Str = null;
        try
        {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buf = new byte[10240];
            while (stream.read(buf) >= 0)
            {
                md.update(buf);
            }
            stream.close();
            byte[] res = md.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < res.length; i++)
            {
                hexString.append(Integer.toHexString(0xFF & res[i]));
            }
            md5Str = hexString.toString();
        }
        catch (NoSuchAlgorithmException e1)
        {
            RequirementCorePlugin.log(e1);
        }
        catch (IOException e)
        {
            RequirementCorePlugin.log(e);
        }
        return md5Str;
    }

    private void askUser(UpstreamModel model, String lastAnalyzedRevision, String currentRevision)
    {
        Resource modelResource = model.eResource();
        IFile modelFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(modelResource.getURI().toPlatformString(true)));
        if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(), Messages.getString("ImpactAnalysisDialog.title"),
                String.format(Messages.getString("ImpactAnalysisDialog.message"), lastAnalyzedRevision)))
        {

            showHistoryView(modelFile, lastAnalyzedRevision);
        }
    }

    private static EAnnotation findHashAnnotation(RequirementProject project)
    {
        for (EAnnotation annotation : project.getEAnnotations())
        {
            if (annotation.getDetails().keySet().contains(HASH_KEY))
            {
                return annotation;
            }

        }
        return null;
    }

    private String getRevision(IResource resource)
    {
        try
        {
            return TeamHistoryManager.getCurrentRevisionLabel(resource);
        }
        catch (TeamHistoryException e)
        {
            RequirementCorePlugin.log(e);
            return null;
        }
    }

    private void showHistoryView(IResource modelResource, String lastAnalyzedRevision)
    {
        IResource r = (IResource) modelResource;
        try
        {
            TeamHistoryManager.showHistoryView(r, TeamHistoryManager.getCurrentRevisionLabel(r));
        }
        catch (TeamHistoryException e)
        {
            RequirementCorePlugin.log(e);
        }
    }

    /**
     * Creates or updates the project's upstream model's annotations to include the given hash and revision
     * 
     * @param project the project
     * @param modelHash the hash
     * @param revision the revision
     */
    public static void createUpdateAnnotation(RequirementProject project, String modelHash, String revision)
    {
        EAnnotation annotation = findHashAnnotation(project);
        // if no hash-bearing annotation is found, create hash
        if (annotation == null)
        {
            EcoreFactory factory = EcoreFactory.eINSTANCE;
            annotation = factory.createEAnnotation();
            project.getEAnnotations().add(annotation);
        }
        if (modelHash != null)
        {
            annotation.getDetails().put(HASH_KEY, modelHash);
        }
        if (revision != null)
        {
            annotation.getDetails().put(REVISION_KEY, revision);
        }
    }

    private class UpdateCreateHashCommand extends AbstractCommand
    {

        private RequirementProject project;

        private String modelHash;

        private String revision;

        public UpdateCreateHashCommand(RequirementProject project, String modelHash, String revision)
        {
            this.project = project;
            this.modelHash = modelHash;
            this.revision = revision;
        }

        @Override
        public boolean canExecute()
        {
            return true;
        }

        public void execute()
        {
            createUpdateAnnotation(project, modelHash, revision);
        }

        public void redo()
        {
        }

        public void undo()
        {
        }

    }
}
