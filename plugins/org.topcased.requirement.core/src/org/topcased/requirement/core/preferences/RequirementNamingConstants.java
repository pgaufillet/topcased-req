/*****************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
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

import org.topcased.requirement.core.extensions.DefaultRequirementIdentifierVariables;

/**
 * An interface defining constants used in naming of requirement.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public interface RequirementNamingConstants
{

    // keys
    String REQUIREMENT_NAMING_FORMAT = "namingFormatRequirement"; //$NON-NLS-1$
    
    String REQUIREMENT_MINIMUM_DIGITS = "minimumDigitsRequirement"; //$NON-NLS-1$

    String REQUIREMENT_STEP_INDEX = "requirementStepIndex"; //$NON-NLS-1$

    String REQUIREMENT_COUNTING_ALGORITHM = "requirementAlgorithm"; //$NON-NLS-1$

    // default values
    String DEFAULT_NAMING_FORMAT = "E_" + DefaultRequirementIdentifierVariables.PROJECT_VAR + "_" + DefaultRequirementIdentifierVariables.HIERARCHICAL_ELEMENT_VAR + "_" + DefaultRequirementIdentifierVariables.INDEX_VAR; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    String DEFAULT_COUNTING_ALGORITHM = "Default Algorithm"; //$NON-NLS-1$

    int DEFAULT_INDEX_STEP = 10;
    
    int DEFAULT_MINIMUM_DIGITS = 5;
}
