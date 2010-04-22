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
 *  Amine Bouchikhi (ATOS ORIGIN INTEGRATION) amine.bouchikhi@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.generic.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.topcased.requirement.RequirementProject;

import ttm.Attribute;
import ttm.Requirement;

/**
 * The Class FilterProcess.
 */
public class FilterProcess
{

    /** The expressions map. */

    /** The regex map. */
    private Map<String, Pattern> mapRegex = new HashMap<String, Pattern>();

    /** The pathes. */
    private IPath[] pathes;

    /** true if all expressions must be matched. */
    private boolean algoAnd;

    private final List<String> attributes;

    private final List<String> regexes;

    private final String nameRegex;

    /**
     * Instantiates a new filter process.
     * 
     * @param expressions map
     * @param match all elements
     * @param path of requirements file(s) to filter
     */
    public FilterProcess(List<String> attributes, List<String> regexes, String nameRegex, boolean andSelected, IPath... ipathes)
    {
        this.attributes = attributes;
        this.regexes = regexes;
        this.nameRegex = nameRegex;
        pathes = ipathes;
        algoAnd = andSelected;
    }

    /**
     * Execute.
     */
    public void execute()
    {
        execute(new NullProgressMonitor());
    }

    public void execute(IProgressMonitor monitor)
    {
        initRegexes();
        for (IPath path : pathes)
        {
            execute(path, monitor);
        }
    }

    /**
     * Inits the regexes.
     */
    private void initRegexes()
    {
        for (String s : regexes)
        {
            try
            {
                Pattern p = Pattern.compile(s);
                mapRegex.put(s, p);
            }
            catch (PatternSyntaxException e)
            {
                log(e);
            }
        }
        try
        {
            Pattern p = Pattern.compile(nameRegex);
            mapRegex.put(nameRegex, p);
        }
        catch (PatternSyntaxException e)
        {
            log(e);
        }
    }

    /**
     * Log.
     * 
     * @param e the e
     */
    private void log(Exception e)
    {
        Activator.getDefault().getLog().log(new Status(Status.WARNING, Activator.PLUGIN_ID, "exception", e));
    }

    /**
     * Execute.
     * 
     * @param path the path
     * @param monitor
     */
    private void execute(IPath path, IProgressMonitor monitor)
    {
        if (monitor == null)
        {
            monitor = new NullProgressMonitor();
        }
        RequirementProject reqP = getRequirementsProject(URI.createPlatformResourceURI(path.makeAbsolute().toString(), true).toString());
        if (reqP != null)
        {
            browse(reqP, monitor);
            try
            {
                reqP.eResource().save(Collections.EMPTY_MAP);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Browse requirements model.
     * 
     * @param reqP the req p
     * @param monitor
     */
    private void browse(RequirementProject reqP, IProgressMonitor monitor)
    {
        CompoundCommand command = new CompoundCommand();
        EditingDomain domain = new AdapterFactoryEditingDomain(new ReflectiveItemProviderAdapterFactory(), new BasicCommandStack());
        int total = 0;
        // count nb elements
        for (TreeIterator<EObject> i = reqP.eAllContents(); i.hasNext(); i.next())
        {
            total++;
        }
        monitor.beginTask("Filter ...", total);
        try
        {
            for (TreeIterator<EObject> i = reqP.eAllContents(); i.hasNext();)
            {
                EObject next = i.next();
                if (next instanceof Requirement)
                {
                    boolean isGood = algoAnd ? true : false;
                    Requirement req = (Requirement) next;
                    boolean validName = true;
                    if (nameRegex != null && nameRegex != "")
                    {
                        validName = mapRegex.get(nameRegex).matcher(req.getIdent()).matches();
                    }
                    for (Attribute a : req.getAttributes())
                    {
                        for (int index = 0; index < regexes.size(); index++)
                        {
                            String regex = regexes.get(index);
                            if (attributes.get(index) != null && attributes.get(index).equals(a.getName()))
                            {
                                String value = (a.getValue().length() > 0 ? a.getValue() : "");
                                if (algoAnd)
                                {
                                    isGood = isGood && mapRegex.get(regex).matcher(value).matches();
                                }
                                else
                                {
                                    isGood = isGood || mapRegex.get(regex).matcher(value).matches();
                                }
                            }
                        }
                    }
                    if (algoAnd)
                    {
                        isGood = isGood && validName;
                    }
                    else
                    {
                        isGood = isGood || validName;
                    }
                    if (!isGood)
                    {
                        command.append(DeleteCommand.create(domain, req));
                    }

                }
                monitor.worked(1);
            }
            monitor.beginTask("Delete ...", 1);
            domain.getCommandStack().execute(command);
            monitor.worked(1);
        }
        finally
        {
            monitor.done();
        }
    }

    /**
     * Gets the requirements project.
     * 
     * @param requirementsPath the requirements path
     * 
     * @return the requirements project
     */
    public RequirementProject getRequirementsProject(String requirementsPath)
    {
        RequirementProject requirements = null;
        if (requirementsPath != null)
        {

            URI uri = URI.createURI(requirementsPath);

            ResourceSet set = new ResourceSetImpl();

            Resource requirementsResources = set.getResource(uri, true);
            if (requirementsResources.getContents().get(0) instanceof RequirementProject)
            {
                requirements = (RequirementProject) requirementsResources.getContents().get(0);
            }
        }
        return requirements;

    }

}
