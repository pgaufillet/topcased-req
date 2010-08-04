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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ReflectiveItemProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;

import ttm.Requirement;


/**
 * Helper for the requirement export workflow.
 * 
 * @author nsamson
 */
public class GeneratorHelper {

	/**
	 * Returns the details for the excel export of the given requirement.
	 * 
	 * @param requirement
	 *            the upstream requirement
	 * @return the html details
	 */
	public static String details(final Requirement requirement) {
		final StringBuilder result = new StringBuilder();

		final List<CurrentRequirement> cReqs = RequirementsUtils
				.getLinkedCurrentRequirements(requirement);

		result
				.append("<td rowspan=\"" + cReqs.size()
						+ "\" colspan=\"1\" align=\"center\">"
						+ requirement.getIdent());
		String lSep = "";
		for (final CurrentRequirement cReq : cReqs) {
			result.append(lSep);
			result.append("<td rowspan=\"1\" colspan=\"1\">"
					+ cReq.getIdentifier() + "</td>");
			final EObject object = ((HierarchicalElement) cReq.eContainer())
					.getElement();
			result.append("<td rowspan=\"1\" colspan=\"1\">");
			if (object != null)
			{
			    result.append(getDisplayableName(object));
			}
			result.append("</td>");
			lSep = "<tr>";
		}
		result.append("</td>");
		return result.toString();
	}

	private static String getDisplayableName(final EObject object) {
	    String string = new ReflectiveItemProvider(new ReflectiveItemProviderAdapterFactory()).getText(object);
	    return string ;
	}
}
