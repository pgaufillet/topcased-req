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

/**
 * An interface defining constants used in naming of requirement.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public interface RequirementNamingConstants
{

    // keys
    String REQUIREMENT_NAMING_FORMAT = "namingFormatRequirement";

    String REQUIREMENT_STEP_INDEX = "requirementStepIndex";

    String REQUIREMENT_COUNTING_ALGORITHM = "requirementAlgorithm";

    // default values
    String DEFAULT_NAMING_FORMAT = "E_{project}_{hierarchical element}_{number}";

    String DEFAULT_COUNTING_ALGORITHM = "Default Algorithm";

    int DEFAULT_INDEX_STEP = 10;
}
