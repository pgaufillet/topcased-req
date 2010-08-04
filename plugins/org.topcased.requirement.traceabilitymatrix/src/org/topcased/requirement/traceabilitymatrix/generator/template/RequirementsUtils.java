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
 *  Nicolas SAMSON (ATOS ORIGIN INTEGRATION) nicolas.samson@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.traceabilitymatrix.generator.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.utils.RequirementUtils;

import ttm.Requirement;

/**
 * Class that manages objects of the requirement metamodel.
 * (org.topcased.sam.requirement)
 * 
 * @author nsamson
 * 
 */
public class RequirementsUtils {

	private static List<CurrentRequirement> getCReq(final EObject object) {
		final List<CurrentRequirement> cReqs = new ArrayList<CurrentRequirement>();
		if (object instanceof HierarchicalElement) {
			final HierarchicalElement element = (HierarchicalElement) object;
			for (final org.topcased.requirement.Requirement req : element.getRequirement()) {
				if (req instanceof CurrentRequirement) {
					cReqs.add((CurrentRequirement) req);
				}
			}
			for (final HierarchicalElement child : element.getChildren()) {
				if (child instanceof CurrentRequirement) {
					cReqs.add((CurrentRequirement) child);
				} else {
					cReqs.addAll(getCReq(child));
				}
			}

		}
		return cReqs;
	}

	private static List<CurrentRequirement> getCurrentRequirements(
			final RequirementProject project) {
		final List<CurrentRequirement> cReqs = new ArrayList<CurrentRequirement>();

		for (final EObject element : project.eContents()) {
			cReqs.addAll(getCReq(element));
		}

		return cReqs;
	}

	/**
	 * Returns current requirements linked to the given upstream requirement.
	 * 
	 * @param requirement
	 *            upstream requirement
	 * @return the current requirements
	 */
	public static List<CurrentRequirement> getLinkedCurrentRequirements(
			final Requirement requirement) {
		final List<CurrentRequirement> links = new ArrayList<CurrentRequirement>();
		for (final CurrentRequirement cReq : getCurrentRequirements(RequirementUtils.getRequirementProject(requirement.eResource()))) {
            for (final Attribute att : cReq.getAttribute()) {
                if (att instanceof AttributeLink) {
                    final EObject value = ((AttributeLink) att).getValue();
                    if (value.equals(requirement)) {
                        links.add(cReq);
                    }
                }
            }
        }
        return links;
	}

}
