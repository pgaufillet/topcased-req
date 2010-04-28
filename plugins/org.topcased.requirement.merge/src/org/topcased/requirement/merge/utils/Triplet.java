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
 *  Caroline Bourdeu d'Aguerre (ATOS ORIGIN INTEGRATION) caroline.bourdeudaguerre@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.merge.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.topcased.modeler.diagrams.model.Diagrams;
import org.topcased.requirement.RequirementProject;

public class Triplet
{

    private EObject model;

    private Diagrams di;

    private RequirementProject requirement;

    private boolean isSubModel;

    public Triplet(EObject model, Diagrams di, RequirementProject requirement, boolean isSubModel)
    {
        super();
        this.model = model;
        // resolve models to get external references
        EcoreUtil.resolveAll(model.eResource().getResourceSet());
        this.di = di;
        this.requirement = requirement;
        this.isSubModel = isSubModel;
    }

    public EObject getModel()
    {
        return model;
    }

    public Diagrams getDiagram()
    {
        return di;
    }

    public RequirementProject getRequirement()
    {
        return requirement;
    }

    public boolean isSubModel()
    {
        return isSubModel;
    }

    @Override
    public String toString()
    {
        String s = "Triplet: ";
        s += "\nModel: " + this.model.toString();
        s += "\nDiagram: " + this.di.toString();
        s += "\nReq: " + this.requirement.toString() + "\n";

        return s;
    }

}
