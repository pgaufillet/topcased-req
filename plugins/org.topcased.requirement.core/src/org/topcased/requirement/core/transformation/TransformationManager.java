/*****************************************************************************
 * Copyright (c) 2012 Atos.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthieu Boivineau - Initial API and implementation
 *    
 ******************************************************************************/
package org.topcased.requirement.core.transformation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.transformation.ITransformation.Provider;


public class TransformationManager{

    
    private static TransformationManager instance = null;
    
    /**
     * Returns the singleton instance of the class
     * @return the singleton instance
     */
    public static TransformationManager getInstance(){
        if(instance == null){
            instance = new TransformationManager();
        }
        return instance;
    }
    
    /**
     * Returns all the providers available for a corresponding extension
     * @param extension the extension
     * @return all the providers available
     */
    public List<Provider> getProviders(String extension){
        List<Provider> providersList = new ArrayList<Provider>();
        IConfigurationElement[] descriptors = Platform.getExtensionRegistry().getConfigurationElementsFor(RequirementCorePlugin.PLUGIN_ID, "updateTransformationProvider");
        for (IConfigurationElement d : descriptors){
            try{
                Provider p = (Provider) d.createExecutableExtension("providerInstance");
                for(String ext:p.getExtensions()){
                    if(ext.equals(extension)){
                        providersList.add(p);
                    }
                }
            } catch (CoreException e){
                e.printStackTrace();
            }
        }
        return providersList;
    }
    
}
