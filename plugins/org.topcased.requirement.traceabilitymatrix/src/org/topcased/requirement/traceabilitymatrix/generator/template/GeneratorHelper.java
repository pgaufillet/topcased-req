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

import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.AttributesType;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.core.utils.RequirementUtils;

import ttm.Document;
import ttm.Requirement;

/**
 * Helper for the requirement export workflow.
 * 
 * @author nsamson
 */
public class GeneratorHelper
{

    private static final String CLOSE_CELL_TAG = "</td>";

    private static final String OPEN_CELL_TAG = "<td rowspan=\"1\" colspan=\"1\">";

    /**
     * This service is necessary to work with controlled upstream model
     * @param project
     * @return
     */
    public static List<Document> getAllDocuments(final RequirementProject project)
    {
    	return project.getUpstreamModel().getDocuments();
    }
    
    /**
     * Returns a string for the display of attribute names at the top of the table.
     * @param project
     * @return
     */
    public static String configuration(final RequirementProject project)
    {
        final StringBuilder result = new StringBuilder();

        AttributeConfiguration conf = project.getAttributeConfiguration();

        for (ConfiguratedAttribute confAtt : conf.getListAttributes())
        {
            if (!confAtt.getType().equals(AttributesType.LINK))
            {
                result.append("<th>" + confAtt.getName() + "</th>");
            }
        }

        return result.toString();
    }

    /**
     * Returns the details for the excel export of the given requirement.
     * 
     * @param requirement the upstream requirement
     * @return the html details
     */
    public static String details(final Requirement requirement)
    {
        final StringBuilder result = new StringBuilder();

        final List<CurrentRequirement> cReqs = RequirementsUtils.getLinkedCurrentRequirements(requirement);

        AttributeConfiguration conf = RequirementUtils.getAttributeConfiguration(requirement.eResource());
        if (conf == null)
        {
        	EObject top = EcoreUtil.getRootContainer(requirement);
        	if (top != null)
        	{
        		conf = RequirementUtils.getAttributeConfiguration(top.eResource());
        	}
        }

        if (cReqs.isEmpty()) {
            result.append("<td align=\"center\">" + requirement.getIdent() + "</td><td colspan=\"" + new Integer(3 + conf.getListAttributes().size()).toString() + "\"></td>");
            result.append("</td>");
        } else {    
            String lSep = ""; 
            for (final CurrentRequirement cReq : cReqs)
            {
                StringBuilder attributesResult = new StringBuilder();
                boolean isPartial = false;

                HashMap<String, Attribute> attributesMap = new HashMap<String, Attribute>();

                for (Attribute att : cReq.getAttribute())
                {
                    attributesMap.put(att.getName(), att);
                }

                for (ConfiguratedAttribute confAtt : conf.getListAttributes())
                {
                    Attribute att = attributesMap.get(confAtt.getName());
                    if (att != null)
                    {

                        if (att instanceof AttributeLink)
                        {
                            // don't display Linkto attributes since the info is already present*
                            // retrieve the partial attribute if it is the Linkto associated with the currently treated
                            // upstream
                            AttributeLink linkAtt = (AttributeLink) att;

                            Object value = linkAtt.getValue();
                            if (requirement.equals(value))
                            {
                                isPartial = linkAtt.getPartial();
                            }
                        }
                        else
                        {
                            String attRepresentation = "";
                            if (att instanceof ObjectAttribute)
                            {
                                ObjectAttribute objAtt = (ObjectAttribute) att;

                                Object value = objAtt.getValue();

                                if (value instanceof Requirement)
                                {
                                    attRepresentation = ((Requirement) value).getIdent();
                                }
                                else if (value != null)
                                {
                                    attRepresentation = value.toString();
                                }

                            }
                            else if (att instanceof TextAttribute)
                            {
                                TextAttribute textAtt = (TextAttribute) att;
                                attRepresentation = textAtt.getValue();
                            }
                            attributesResult.append(OPEN_CELL_TAG + attRepresentation + CLOSE_CELL_TAG);
                        }
                    }
                }

                result.append(lSep);
                result.append("<td rowspan=\"1\" colspan=\"1\" align=\"center\">" + requirement.getIdent());
                result.append(OPEN_CELL_TAG + isPartial + CLOSE_CELL_TAG);

                result.append(OPEN_CELL_TAG + cReq.getIdentifier() + CLOSE_CELL_TAG);

                 EObject object = null ;
                if(cReq.eContainer() instanceof HierarchicalElement)
                {
                	object = ((HierarchicalElement) cReq.eContainer()).getElement();
                }
                else
                {
                	object = cReq.eContainer();
                }
                result.append(OPEN_CELL_TAG + getDisplayableName(object) + CLOSE_CELL_TAG);

                result.append(OPEN_CELL_TAG + cReq.getShortDescription() + CLOSE_CELL_TAG);

                result.append(attributesResult);
                result.append("</td>");
                lSep = "<tr>";     
            }
        }
        
        return result.toString();
    }

    private static String getDisplayableName(final EObject object)
    {
        if (object != null)
        {
            return new CustomReflectiveItemProvider(new ReflectiveItemProviderAdapterFactory()).getText(object);
        }
        else
        {
            return "";
        }
    }
}
