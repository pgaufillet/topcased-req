/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/

package org.topcased.requirement.core.extensions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.facilities.extensions.AbstractExtensionManager;
import org.topcased.requirement.core.internal.RequirementCorePlugin;

/**
 * Define the manager of the extension point "requirementIdentifierDefinition" who provide
 * the way to change the count algorithm of current requirements and add key words to current requirements identifier
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class RequirementIdentifierDefinitionManager extends AbstractExtensionManager
{

    /** constant representing the name of the extension point */
    private static final String REQUIREMENT_IDENTIFIER_DEFINITION_EXTENSION_POINT = "requirementIdentifierDefinition"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the value given to the uri. */
    static final String ATT_URI = "uri"; //$NON-NLS-1$

    /** Value of the extension point attribute corresponding to the class. */
    static final String ATT_CLASS = "class"; //$NON-NLS-1$

    /** the shared instance */
    private static RequirementIdentifierDefinitionManager manager;

    /** Map of an uri of the metamodel and the identifier definition for it */
    public Map<String, IRequirementIdentifierDefinition> mapClass;

    /**
     * Private constructor
     */
    private RequirementIdentifierDefinitionManager()
    {
        super(RequirementCorePlugin.getId() + "." + REQUIREMENT_IDENTIFIER_DEFINITION_EXTENSION_POINT);
        mapClass = new HashMap<String, IRequirementIdentifierDefinition>();
        readRegistry();
    }

    /**
     * Gets the shared instance.
     * 
     * @return the requirement identifier definition manager
     */
    public static RequirementIdentifierDefinitionManager getInstance()
    {
        if (manager == null)
        {
            manager = new RequirementIdentifierDefinitionManager();
        }
        return manager;
    }

    /**
     * @see org.topcased.facilities.extensions.AbstractExtensionManager#addExtension(org.eclipse.core.runtime.IExtension)
     */
    @Override
    protected void addExtension(IExtension extension)
    {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (IConfigurationElement confElt : elements)
        {
            String model = confElt.getAttribute(ATT_URI);
            IRequirementIdentifierDefinition definition;
            try
            {
                definition = (IRequirementIdentifierDefinition) confElt.createExecutableExtension(ATT_CLASS);
                mapClass.put(model, definition);
            }
            catch (CoreException e)
            {
                RequirementCorePlugin.log(e);
            }

        }

    }

    /**
     * @see org.topcased.facilities.extensions.AbstractExtensionManager#removeExtension(org.eclipse.core.runtime.IExtension)
     */
    @Override
    protected void removeExtension(IExtension extension)
    {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (IConfigurationElement confElt : elements)
        {
            String elt = confElt.getAttribute(ATT_URI);
            mapClass.remove(elt);
        }

    }

    /**
     * This method return the requirement identifier definition for a given metamodel uri
     * 
     * @param uri the metamodel uri
     * @return the identifier definition
     */
    public IRequirementIdentifierDefinition getIdentifierDefinition(String uri)
    {
        if (mapClass.containsKey(uri))
        {
            return mapClass.get(uri);
        }
        return null;
    }

    /**
     * This method return the requirement identifier definition for a given editingDomain
     * 
     * @param the editing domain
     * @return the identifier definition
     */
    public IRequirementIdentifierDefinition getIdentifierDefinition(EditingDomain editingDomain)
    {
        IRequirementIdentifierDefinition definition = null;
        EList<Resource> resources = editingDomain.getResourceSet().getResources();
        for (Resource resource : resources)
        {
            EList<EObject> roots = resource.getContents();
            for (EObject root : roots)
            {
                String uri = EcoreUtil.getURI(root.eClass().getEPackage()).trimFragment().toString();
                IRequirementIdentifierDefinition tempDef = getIdentifierDefinition(uri);
                if (tempDef != null)
                {
                    definition = tempDef;
                }
            }
        }
        return definition;
    }

}
