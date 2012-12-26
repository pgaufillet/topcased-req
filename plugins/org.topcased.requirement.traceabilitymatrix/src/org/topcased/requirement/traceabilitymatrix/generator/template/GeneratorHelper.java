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
 *  Cyril MARCHIVE (Atos) cyril.marchive@atos.net
 *
 *****************************************************************************/
package org.topcased.requirement.traceabilitymatrix.generator.template;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.papyrus.core.modelsetquery.IModelSetQueryAdapter;
import org.eclipse.papyrus.core.modelsetquery.ModelSetQuery;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.core.utils.RequirementUtils;

import ttm.Document;
import ttm.Requirement;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

/**
 * Helper for the requirement export workflow.
 * 
 * @author nsamson
 */
public class GeneratorHelper
{

    private static final AdapterFactoryLabelProvider ITEM_PROVIDER = new AdapterFactoryLabelProvider(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));

	public static final String CELL_START = "<Cell><Data ss:Type='String'>";

    public static final String CELL_END = "</Data></Cell>";

    public static final String COLUMNS_START = "\n<Row>";

    public static final String COLUMNS_END = "\n</Row>";

    /**
     * This service is necessary to work with controlled upstream model
     * 
     * @param project
     * @return
     */
    public static List<Document> getAllDocuments(final RequirementProject project)
    {
        return project.getUpstreamModel().getDocuments();
    }

    /**
     * Returns a string for the display of attribute names at the top of the table.
     * 
     * @param allAttribute
     * @return
     */
    public static String configuration(final SortedSet<Attribute> allAttribute)
    {
        final StringBuilder result = new StringBuilder();

        for (Attribute s : allAttribute)
        {
            if (!(s instanceof AttributeLink))
            {
                result.append(addColumn(s.getName()));
            }
        }

        return result.toString();
    }

    public static Collection<Setting> getCrossReferences(EObject source)
    {
        Collection<Setting> collection = Collections.<Setting> emptyList();
        if (source == null)
        {
            return collection;
        }
        ECrossReferenceAdapter adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(source);
        if (adapter != null)
        {
            collection = adapter.getNonNavigableInverseReferences(source);
        }
        return collection;
    }

    /**
     * Returns the details for the excel export of the given requirement.
     * 
     * @param requirement the upstream requirement
     * @param document the document
     * @param allAttribute All attributes
     * @return the html details
     */
    public static String details(final Requirement requirement, Document document, SortedSet<Attribute> allAttribute)
    {
        final StringBuilder result = new StringBuilder();

        final List<Setting> cReqs = Lists.newArrayList(Iterables.filter(getCrossReferences(requirement), new Predicate<Setting>()
        {
            public boolean apply(Setting input)
            {
                return input.getEObject() instanceof AttributeLink;
            }
        }));
        IModelSetQueryAdapter q = ModelSetQuery.getExistingTypeCacheAdapter(requirement);
        AttributeConfiguration conf = Iterators.filter(q.getReachableObjectsOfType(requirement, RequirementPackage.Literals.ATTRIBUTE_CONFIGURATION).iterator(), AttributeConfiguration.class).next();
        if (conf == null)
        {
            EObject top = EcoreUtil.getRootContainer(requirement);
            if (top != null)
            {
                conf = RequirementUtils.getAttributeConfiguration(top.eResource());
            }
        }

        if (cReqs.isEmpty())
        {
            result.append(COLUMNS_START);
            result.append(addColumn(document.getIdent()));
            result.append(addColumn(requirement.getIdent()));
            result.append(COLUMNS_END);
        }
        else
        {
            for (final Setting set : cReqs)
            {
                AttributeLink link = (AttributeLink) set.getEObject();
                CurrentRequirement cReq = (CurrentRequirement) link.eContainer();
                result.append("\n" + COLUMNS_START);
                StringBuilder attributesResult = new StringBuilder();
                boolean isPartial = false;

                HashMap<String, Attribute> attributesMap = new HashMap<String, Attribute>();

                for (Attribute att : cReq.getAttribute())
                {
                    attributesMap.put(att.getName(), att);
                }

                for (Attribute confAtt : allAttribute)
                {
                    Attribute att = attributesMap.get(confAtt.getName());
                    if (att != null)
                    {
                        String attRepresentation = "";
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
                            attributesResult.append(addColumn(attRepresentation));
                        }
                    }
                    else
                    {
                        attributesResult.append(addColumn(""));
                    }
                }
                result.append(addColumn(document.getIdent()));
                result.append(addColumn(requirement.getIdent()));
                result.append(addColumn(new Boolean(isPartial).toString()));
                result.append(addColumn(cReq.getIdentifier()));
                result.append(addColumn(link.getName()));
                EObject object = null;
                if (cReq.eContainer() instanceof HierarchicalElement)
                {
                    object = ((HierarchicalElement) cReq.eContainer()).getElement();
                }
                else
                {
                    object = cReq.eContainer();
                }
                result.append(addColumn(getDisplayableName(object)));
                result.append(addColumn(getQualifiedName(object.eContainer())));

                // Some special characters encoded in hexadecimel
                // are not displayed in Excel
                // This algorithm retrieve these characters and convert its into char

                /*
                 * Pattern pattern = Pattern.compile("&#x.{1,2};");
                 * 
                 * if (cReq.getShortDescription() != null) { Matcher m = pattern.matcher(cReq.getShortDescription());
                 * while (m.find()) { String chaine = m.group(); int entier = Integer.parseInt(chaine.substring(3,
                 * chaine.lastIndexOf(';')), 16); System.out.println(chaine + " : " + (char) entier + " -> OK"); } }
                 */

                result.append(cReq.getShortDescription() == null ? "" : addColumn(cReq.getShortDescription()));
                result.append(attributesResult);
                result.append("\n" + COLUMNS_END);
            }
        }
        return result.toString();
    }

    private static String getQualifiedName(EObject object)
    {
        StringBuilder builder = new StringBuilder();
        EObject tmp = object;
        int i = 0;
        while (tmp != null)
        {
            if (i != 0)
            {
                builder.insert(0, "::");
            }
            builder.insert(0, getDisplayableName(tmp));
            tmp = tmp.eContainer();
            i++;
        }
        return builder.toString();
    }

    private static String getDisplayableName(final EObject object)
    {
        if (object != null)
        {
            return ITEM_PROVIDER.getText(object);
        }
        else
        {
            return "";
        }
    }

    public static String addColumn(String header)
    {
        return "\n\t" + CELL_START + "<![CDATA[" + header + "]]>" + CELL_END;
    }

}
