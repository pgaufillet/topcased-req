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

package org.topcased.requirement.sam.policies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.extensions.IRequirementIdentifierDefinition;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * @author maudrain
 *
 */
public class SAMRequirementIdentifierDefinition implements IRequirementIdentifierDefinition
{

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementIdentifierDefinition#addValuesToPatterns(org.eclipse.emf.edit.domain.EditingDomain, java.util.Map)
     */
    public Map<String, String> addValuesToPatterns(EditingDomain editingDomain, Map<String, String> alreadyCreatedMap)
    {
        Resource requirement = RequirementUtils.getRequirementModel(editingDomain);
        
        alreadyCreatedMap.put("{Upstream Model Ident}", ((RequirementProject) requirement.getContents().get(0)).getUpstreamModel().getIdent());
        return alreadyCreatedMap;
    }

    /**
     * @see org.topcased.requirement.core.extensions.IRequirementIdentifierDefinition#addPatterns()
     */
    public List<String> addPatterns()
    {   
        List<String> keyWords = new ArrayList<String>();
        keyWords.add("{Upstream Model Ident}");
        return keyWords;
    }

}
