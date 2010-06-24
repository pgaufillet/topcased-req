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
package org.topcased.requirement.core.providers;

import ttm.Requirement;

/**
 * Internal class used to store the line to display in the table of the requirement property section
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class RequirementCRegistry
{
    // current requirement
    private org.topcased.requirement.Requirement cr;

    // upstream requirement
    private Requirement up;

    public RequirementCRegistry(org.topcased.requirement.Requirement current, Requirement upstream)
    {
        up = upstream;
        cr = current;
    }

    public org.topcased.requirement.Requirement getCurrent()
    {
        return cr;
    }

    public Requirement getUpstream()
    {
        return up;
    }
}


