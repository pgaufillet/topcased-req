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

package org.topcased.requirement.bundle.papyrus.resourceloading;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.papyrus.core.resourceloading.ILoadingStrategyExtension;
import org.eclipse.papyrus.resource.ModelSet;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.util.RequirementResource;

/**
 *  Load Upstream requirement if needed
 */
public class UpstreamRequirementsExtender implements ILoadingStrategyExtension
{

    public boolean loadResource(ModelSet modelSet, URI uri)
    {
        if (uri == null || modelSet == null)
        {
            return false;
        }
        if (!RequirementResource.FILE_EXTENSION.equals(uri.fileExtension()))
        {
            return false;
        }
        for (Resource r : modelSet.getResources())
        {
            if (RequirementResource.FILE_EXTENSION.equals(r.getURI().fileExtension()))
            {
                if (r.getContents().size() > 0 && r.getContents().get(0) instanceof RequirementProject)
                {
                    RequirementProject requirementProject = (RequirementProject) r.getContents().get(0);
                    Object upstreamModel = requirementProject.eGet(RequirementPackage.Literals.REQUIREMENT_PROJECT__UPSTREAM_MODEL, false);
                    if (upstreamModel instanceof EObject)
                    {
                        EObject upstream = (EObject) upstreamModel;

                        if (upstreamModel != null && upstream.eIsProxy() && EcoreUtil.getURI(upstream).trimFragment().equals(uri.trimFragment()))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
