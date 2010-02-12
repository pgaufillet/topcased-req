/*****************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.preferences;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This class provides the feature to compute the requirement identifier with the Requirements naming's format of the
 * project's property.<br>
 * 
 * Update : 12th may 2009.<br>
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public class ComputeRequirementIdentifier
{
    public static final ComputeRequirementIdentifier INSTANCE = new ComputeRequirementIdentifier();

    public static final long STEP_IDENT = 10;

    private static final String DEFAULT_HIERARCHICAL_ELEMENT_NAME = "xxx";

    private IPreferenceStore preferenceStore;

    private String initialFormat;

    private String currentFormat;

    private Boolean numberAllDocument;

    private int nRequirement;

    /**
     * Computes the identifier of the new requirement
     * 
     * @param domain The editing domain to use
     * @param hierarchicalElt : the hierarchical element
     * @param source : the identifier of requirement
     * @param nextIndex The next index to use to name the {@link CurrentRequirement}
     * @return the new composed identifier
     */
    public String computeIdent(EditingDomain domain, HierarchicalElement hierarchicalElt, String source, long nextIndex)
    {
        currentFormat = initialFormat;

        // Number's formatter
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(5);
        nf.setGroupingUsed(false);

        // Determine the number in the system
        return computeFullIdentifier(domain, hierarchicalElt, source, nf.format(nextIndex));
    }

    /**
     * Replace the key word in the format
     * 
     * @param map : the key words to replace in the format
     * 
     * @return the new identifier
     */
    private String convert(Map<Integer, String> map)
    {
        for (int i = 0; i < NamingRequirementPreferenceHelper.KEY_WORDS.length; i++)
        {
            if (map.get(i) != null)
            {
                if (map.get(i).length() > 0)
                {
                    currentFormat = currentFormat.replace(NamingRequirementPreferenceHelper.KEY_WORDS[i], map.get(i));
                }
                else
                {
                    currentFormat = currentFormat.replace(NamingRequirementPreferenceHelper.KEY_WORDS[i], "");
                }
            }
        }
        return currentFormat;
    }

    /**
     * Computes the current requirement identifier including the {number} field
     * 
     * @param domain The editing domain to use
     * @param element The target hierarchical element
     * @param source The identifier of the source
     * 
     * @return the current requirement identifier
     */
    private String computeFullIdentifier(EditingDomain domain, HierarchicalElement element, String source, String number)
    {
        Resource requirement = RequirementUtils.getRequirementModel(domain);
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(NamingRequirementPreferenceHelper.PROJECT, ((RequirementProject) requirement.getContents().get(0)).getIdentifier());
        map.put(NamingRequirementPreferenceHelper.HIERARCHICAL_ELEMENT, getHierarchicalElementIdentifier(element));
        map.put(NamingRequirementPreferenceHelper.UPSTREAM_IDENT, source);
        map.put(NamingRequirementPreferenceHelper.NUMBER, number);
        return convert(map);
    }

    /**
     * Get the identifier of the target element
     * 
     * @param hierarchicalElt : the target hierarchical element
     * 
     * @return the identifier of the target element
     */
    private String getHierarchicalElementIdentifier(HierarchicalElement hierarchicalElt)
    {
        String result = "";

        if (hierarchicalElt.getElement() != null)
        {
            EObject obj = hierarchicalElt.getElement();
            for (EAttribute attribute : obj.eClass().getEAllAttributes())
            {
                if (attribute.getName().equals(EcorePackage.eINSTANCE.getENamedElement_Name().getName()))
                {
                    result = (String) obj.eGet(attribute);
                    break;
                }
            }
        }
        else
        {
            result += DEFAULT_HIERARCHICAL_ELEMENT_NAME;
        }

        return result;
    }

    /**
     * Gets the PreferenceStore of the project's property
     * 
     * @param project
     * 
     * @return the PreferenceStore of the project's property
     */
    public void setPreferenceStore(Resource requirementModel)
    {
        if (requirementModel == null)
        {
            return;
        }

        IFile file = getFile(requirementModel);
        if (file == null)
        {
            return;
        }

        IProject project = file.getProject();
        Preferences root = Platform.getPreferencesService().getRootNode();
        try
        {
            if (root.node(ProjectScope.SCOPE).node(project.getName()).nodeExists(RequirementCorePlugin.getId()))
            {
                this.preferenceStore = new ScopedPreferenceStore(new ProjectScope(project), RequirementCorePlugin.getId());
            }
            else
            {
                this.preferenceStore = RequirementCorePlugin.getDefault().getPreferenceStore();

            }
        }
        catch (BackingStoreException e)
        {
            RequirementCorePlugin.log(e);
            return;
        }

        this.initialFormat = this.preferenceStore.getString(NamingRequirementPreferenceHelper.NAMING_FORMAT_REQUIREMENT_STORE);
        this.numberAllDocument = this.preferenceStore.getBoolean(NamingRequirementPreferenceHelper.NUMBER_REQUIREMENT_STORE);

        if (this.numberAllDocument)
        {
            this.setnRequirement(countRequirement(requirementModel));
        }
    }

    /**
     * Gets the IFile of a resource
     * 
     * @param resource
     * 
     * @return the IFile of a resource
     */
    private static IFile getFile(Resource resource)
    {
        URI uri = resource.getURI();
        uri = resource.getResourceSet().getURIConverter().normalize(uri);
        String scheme = uri.scheme();
        if ("platform".equals(scheme) && uri.segmentCount() > 1 && "resource".equals(uri.segment(0)))
        {
            StringBuffer platformResourcePath = new StringBuffer();
            for (int j = 1; j < uri.segmentCount(); ++j)
            {
                platformResourcePath.append('/');
                platformResourcePath.append(uri.segment(j));
            }
            return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformResourcePath.toString()));
        }
        else if ("file".equals(scheme))
        {
            StringBuffer platformResourcePath = new StringBuffer();
            for (int j = 0; j < uri.segmentCount(); ++j)
            {
                platformResourcePath.append('/');
                platformResourcePath.append(uri.segment(j));
            }
            return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformResourcePath.toString()));

        }
        return null;
    }

    /**
     * Counts the Requirements in the Resource
     * 
     * @param resource
     * 
     * @return the number of Requirements in the Resource
     */
    private int countRequirement(Resource resource)
    {
        int n = 0;
        TreeIterator<EObject> treeIt = EcoreUtil.<EObject> getAllContents(resource, true);
        while (treeIt.hasNext())
        {
            EObject current = treeIt.next();
            if (current instanceof Requirement)
            {
                n++;
            }
        }
        return n;
    }

    public void setnRequirement(int nRequirement)
    {
        this.nRequirement = nRequirement;
    }

    public int getnRequirement()
    {
        return nRequirement;
    }

}
