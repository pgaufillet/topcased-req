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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.extensions.IRequirementIdentifierVariables;
import org.topcased.requirement.core.extensions.RequirementIdentifierVariablesManager;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This class provides the feature to compute the requirement identifier with the Requirements naming's format of the
 * project's property.<br>
 * 
 * Update : 12th may 2009.<br>
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class ComputeRequirementIdentifier
{
    public static final ComputeRequirementIdentifier INSTANCE = new ComputeRequirementIdentifier();

    private IPreferenceStore preferenceStore;

    private String initialFormat;

    private String currentFormat;
    
    // Parameters used to compute the full identifier:
    
    private EditingDomain editingDomain;
    
    private HierarchicalElement hierarchicalElement;
    
    private String upstreamIdentifier;
    
    private long requirementIndex;

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
        editingDomain = domain;
        hierarchicalElement = hierarchicalElt;
        upstreamIdentifier = source;
        requirementIndex = nextIndex;
      
        // Determine the number in the system
        return computeFullIdentifier();
    }

    /**
     * Replace the key word in the format
     * 
     * @param map : the key words to replace in the format
     * 
     * @return the new identifier
     */
    private String convert(Map<String, String> map)
    {
        for (String keyWord : NamingRequirementPreferenceHelper.KEY_WORDS)
        {
            if (map.get(keyWord) != null)
            {
                if (map.get(keyWord).length() > 0)
                {
                    currentFormat = currentFormat.replace(keyWord, map.get(keyWord));
                }
                else
                {
                    currentFormat = currentFormat.replace(keyWord, "");
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
    private String computeFullIdentifier()
    {
        Map<String, String> map = new HashMap<String, String>();

        //Variables added by extension point
        Collection<IRequirementIdentifierVariables> variables = RequirementIdentifierVariablesManager.getInstance().getIdentifierVariables();
        if (variables != null)
        {
            for (IRequirementIdentifierVariables vars : variables)
            {
                map = vars.setValuesToVariables(editingDomain, map);       
            }     
        }
        
        return convert(map);
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

        IFile file = RequirementUtils.getFile(requirementModel);
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
    }
    
    public HierarchicalElement getIdentifierHierarchicalElement()
    {
        return hierarchicalElement;
    }
    
    public String getIdentifierUpstreamIdent()
    {
        return upstreamIdentifier;
    }
    
    public long getIdentifierRequirementIndex()
    {
        return requirementIndex;
    }
    
//
//    /**
//     * Counts the Requirements in the Resource
//     * 
//     * @param resource
//     * 
//     * @return the number of Requirements in the Resource
//     */
//    private int countRequirement(Resource resource)
//    {
//        int n = 0;
//        TreeIterator<EObject> treeIt = EcoreUtil.<EObject> getAllContents(resource, true);
//        while (treeIt.hasNext())
//        {
//            EObject current = treeIt.next();
//            if (current instanceof Requirement)
//            {
//                n++;
//            }
//        }
//        return n;
//    }
//
//    public void setnRequirement(int nRequirement)
//    {
//        this.nRequirement = nRequirement;
//    }
//
//    public int getnRequirement()
//    {
//        return nRequirement;
//    }

}
