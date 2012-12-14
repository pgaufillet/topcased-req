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
 *  Matthieu BOIVINEAU (Atos) matthieu.boivineau@atos.net - Deletion parameters parsing added
 *  														Deletion parameters dialog management added 
 *
 *****************************************************************************/

package org.topcased.typesmodel.handler;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;
import org.topcased.typesmodel.Activator;
import org.topcased.typesmodel.Messages;
import org.topcased.typesmodel.model.inittypes.Column;
import org.topcased.typesmodel.model.inittypes.DeletionParameters;
import org.topcased.typesmodel.model.inittypes.DeletionParemeter;
import org.topcased.typesmodel.model.inittypes.DocumentType;
import org.topcased.typesmodel.model.inittypes.InittypesFactory;
import org.topcased.typesmodel.model.inittypes.InittypesPackage;
import org.topcased.typesmodel.model.inittypes.Regex;
import org.topcased.typesmodel.model.inittypes.Style;
import org.topcased.typesmodel.model.inittypes.Type;
import org.topcased.typesmodel.model.inittypes.TypeModel;
import org.topcased.typesmodel.model.inittypes.provider.DocumentTypeItemProvider;
import org.topcased.typesmodel.model.inittypes.provider.InittypesItemProviderAdapterFactory;
import org.topcased.typesmodel.model.inittypes.provider.RegexItemProvider;
import org.topcased.typesmodel.ui.ComboInputDialog;
import org.topcased.typesmodel.ui.DeletionParametersDialog;
import org.topcased.windows.ini.actions.IniFileParser;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 *  Create type model 
 */
public class IniManagerRegistry implements IResourceVisitor, IResourceDeltaVisitor
{

	enum kind {
		ADD, MODIFY, DELETE
	}

	private TypeModel typeModel;;

	/**
	 * return the ini manager registry
	 */
	public IniManagerRegistry()
	{
		this.typeModel = InittypesFactory.eINSTANCE.createTypeModel();
	}

	public boolean visit(IResource resource) throws CoreException
	{
		if (resource.isAccessible())
		{
			visit(resource, kind.ADD);
			return true;
		}
		return !(resource instanceof IProject);
	}

	/**
	 * perform addition, suppression or modification of a resource
	 * @param resource the impacted resource
	 * @param type the changement type
	 */
	public void visit(IResource resource, kind type)
	{
		if (Messages.Types.equals(resource.getFileExtension()) && resource instanceof IFile)
		{
			switch (type)
			{
			case ADD:
				if (fileAdded((IFile) resource))
				{
					Activator.getDefault().debug(Messages.NewFile + resource.toString());
				}
				break;
			case DELETE:
				if (fileDeleted((IFile) resource))
				{
					Activator.getDefault().debug(Messages.DeletedFile + resource.toString());
				}
				break;
			case MODIFY:
				fileUpdated((IFile) resource);
				Activator.getDefault().debug(Messages.ModifiedFile + resource.toString());
				break;
			default:
				break;
			}
		}
	}

	private static Pattern attributePattern = Pattern.compile(Messages.AttributeRegex);
	private static Pattern attributeNamePattern = Pattern.compile(Messages.AttributeName);
	private static Pattern attributeIsTextPattern = Pattern.compile("Attribute\\d+IsText"); //$NON-NLS-1$
	private static Pattern stylePattern = Pattern.compile(Messages.StyleRegex);
	private static Pattern styleNamePattern = Pattern.compile(Messages.StyleName);
	private static Pattern styleLabelPattern = Pattern.compile(Messages.StyleLabel);
	private static Pattern styleIsTextPattern = Pattern.compile("Style\\d+IsText"); //$NON-NLS-1$
	private static Pattern columnPattern = Pattern.compile(Messages.ColumnRegex);
	private static Pattern columnNamePattern = Pattern.compile(Messages.ColumnName);
	private static Pattern columnIsTextPattern = Pattern.compile("Column\\d+IsText"); //$NON-NLS-1$
	private static Pattern requirementPattern = Pattern.compile(Messages.RequirementRegex);
	private static Pattern requirementStylePattern = Pattern.compile(Messages.RequirementStyle);
	private static Pattern requirementColumnPattern = Pattern.compile(Messages.RequirementColumn);
	private static Pattern hierarchicalPattern = Pattern.compile(Messages.Hierarchical);
	private static Pattern endTextPattern = Pattern.compile(Messages.EndText);
	private static Pattern textRegexPattern = Pattern.compile("DescriptionRegex"); //$NON-NLS-1$
	private static Pattern deletionParameterIdRegex = Pattern.compile(Messages.IniManagerRegistry_DeletionParameterIdRegex);
	
	private static Pattern deletionParameterDescriptionRegex = Pattern.compile(Messages.IniManagerRegistry_DeletionParameterDescriptionRegex);
	private static Pattern deletionParameterAttributeName = Pattern.compile(Messages.IniManagerRegistry_DeletionParameterAttributeName);
	private static Pattern deletionParameterAttributeRegex = Pattern.compile(Messages.IniManagerRegistry_DeletionParameterAttributeRegex);
	private static Pattern filterParameterAttributeName = Pattern.compile(Messages.IniManagerRegistry_FilterParameterAttributeName);
	private static Pattern filterParameterAttributeRegex = Pattern.compile(Messages.IniManagerRegistry_FilterParameterAttributeRegex);
	private static Pattern isAndAttributes = Pattern.compile(Messages.IniManagerRegistry_AttributesIsAnd);
	
	private boolean fileAdded(IFile resource)
	{
		Map<String, DocumentType> documentTypes = parseTypesFile(resource);

		typeModel.getDocumentTypes().addAll(documentTypes.values());

		return true;
	}

	public static Map<String, DocumentType> parseTypesFile(IFile resource) {
		Map<String, DocumentType> documentTypes = new HashMap<String, DocumentType>();
		IniFileParser parser = new IniFileParser(resource);
		String[] types = parser.getTypes();

		if (types == null)
		{
			return documentTypes;
		}
		for (String type : types)
		{
			Section section = parser.getElements(type);
			if (section != null) {
				Map<String, Type> allElements = new HashMap<String, Type>();
				Map<String, DeletionParemeter> allDeletionParameters = new HashMap<String, DeletionParemeter>();
				Map<String, DeletionParemeter> allFilterParameters = new HashMap<String, DeletionParemeter>();
				Type id = null;

				DocumentType documentType = InittypesFactory.eINSTANCE.createDocumentType();
				documentType.setName(type);
				documentType.setDocumentPath(resource.getFullPath().toString());
				DeletionParameters createDeletionParameters = InittypesFactory.eINSTANCE.createDeletionParameters();
				documentType.setDeletionParameters( createDeletionParameters);

				for (Entry<String, String> element : section.entrySet())
				{
					if (attributePattern.matcher(element.getKey()).matches())
					{
						manageElement(allElements, element,InittypesPackage.Literals.REGEX,InittypesPackage.Literals.REGEX__EXPRESSION);
					}
					else if (attributeIsTextPattern.matcher(element.getKey()).matches())
					{
						manageElement(allElements, element,InittypesPackage.Literals.REGEX,InittypesPackage.Literals.TYPE__IS_TEXT);
					}
					else if (attributeNamePattern.matcher(element.getKey()).matches())
					{
						manageElement(allElements, element,InittypesPackage.Literals.REGEX,InittypesPackage.Literals.TYPE__NAME);
					}
					else if (columnPattern.matcher(element.getKey()).matches())
					{
						manageElement(allElements, element,InittypesPackage.Literals.COLUMN,InittypesPackage.Literals.REGEX__EXPRESSION);
					}
					else if (columnIsTextPattern.matcher(element.getKey()).matches())
					{
						manageElement(allElements, element,InittypesPackage.Literals.COLUMN,InittypesPackage.Literals.TYPE__IS_TEXT);
					}
					else if (columnNamePattern.matcher(element.getKey()).matches())
					{
						manageElement(allElements, element,InittypesPackage.Literals.COLUMN,InittypesPackage.Literals.TYPE__NAME);
					}
					else if (stylePattern.matcher(element.getKey()).matches())
					{
						manageElement(allElements, element,InittypesPackage.Literals.STYLE,InittypesPackage.Literals.REGEX__EXPRESSION);
					}
					else if (styleIsTextPattern.matcher(element.getKey()).matches())
					{
						manageElement(allElements, element,InittypesPackage.Literals.STYLE,InittypesPackage.Literals.TYPE__IS_TEXT);
					}
					else if (styleNamePattern.matcher(element.getKey()).matches())
					{
						manageElement(allElements, element,InittypesPackage.Literals.STYLE,InittypesPackage.Literals.TYPE__NAME);
					}
					else if (styleLabelPattern.matcher(element.getKey()).matches())
					{
						manageElement(allElements, element,InittypesPackage.Literals.STYLE,InittypesPackage.Literals.STYLE__LABEL);
					}
					else if (isAndAttributes.matcher(element.getKey()).matches())
					{
						createDeletionParameters.setIsAnd(Boolean.valueOf(element.getValue()));
					}
					else if (requirementPattern.matcher(element.getKey()).matches())
					{
						if (id == null)
						{
							id = manageId(element,InittypesPackage.Literals.REGEX, InittypesPackage.Literals.REGEX__EXPRESSION);
						} else if (id instanceof Style)
						{
							String labelTemp = ((Style) id).getLabel();
							id = manageId(element,InittypesPackage.Literals.STYLE, InittypesPackage.Literals.REGEX__EXPRESSION);
							((Style)id).setLabel(labelTemp);
						}
					}
					else if (requirementStylePattern.matcher(element.getKey()).matches())
					{
						if (id == null)
						{
							id = manageId(element,InittypesPackage.Literals.STYLE,InittypesPackage.Literals.STYLE__LABEL);
						} 
						else if (id instanceof Regex && !(id instanceof Style))
						{
							String expressionTemp = ((Regex) id).getExpression();
							id = manageId(element,InittypesPackage.Literals.STYLE,InittypesPackage.Literals.STYLE__LABEL);
							((Style)id).setExpression(expressionTemp);
						}
					}
					else if (requirementColumnPattern.matcher(element.getKey()).matches())
					{
						if (id == null)
						{
							id = manageId(element,InittypesPackage.Literals.COLUMN, InittypesPackage.Literals.REGEX__EXPRESSION);
							Pattern patternColumn = Pattern.compile("Requirement(\\d*)Column"); //$NON-NLS-1$
							Matcher m = patternColumn.matcher(element.getKey());
							if (m.matches())
							{
								((Column) id).setNumber(Integer.parseInt(m.group(1)));
							}

						}
					}
					else if (hierarchicalPattern.matcher(element.getKey()).matches())
					{
						if ("True".equalsIgnoreCase(element.getValue())) { //$NON-NLS-1$
							documentType.setHierarchical(true);
						}
						else
						{
							documentType.setHierarchical(false);
						}
					}
					else if (endTextPattern.matcher(element.getKey()).matches())
					{
						documentType.setTextType(element.getValue());
					}
					else if (textRegexPattern.matcher(element.getKey()).matches())
					{
						documentType.setTextRegex(element.getValue());
					}
					else if (deletionParameterIdRegex.matcher(element.getKey()).matches())
					{
						documentType.getDeletionParameters().setRegexId(element.getValue());
					}
					else if (deletionParameterDescriptionRegex.matcher(element.getKey()).matches())
					{
						documentType.getDeletionParameters().setRegexDescription(element.getValue());
					}
					else if (deletionParameterAttributeName.matcher(element.getKey()).matches())
					{
						manageDeletionParameters(allDeletionParameters,element,InittypesPackage.Literals.DELETION_PAREMETER,InittypesPackage.Literals.DELETION_PAREMETER__NAME_ATTRIBUTE);
					}
					else if (deletionParameterAttributeRegex.matcher(element.getKey()).matches())
					{
						manageDeletionParameters(allDeletionParameters,element,InittypesPackage.Literals.DELETION_PAREMETER,InittypesPackage.Literals.DELETION_PAREMETER__REGEX_ATTRIBUTE);
					}
					else if (filterParameterAttributeName.matcher(element.getKey()).matches())
					{
						manageDeletionParameters(allFilterParameters,element,InittypesPackage.Literals.DELETION_PAREMETER,InittypesPackage.Literals.DELETION_PAREMETER__NAME_ATTRIBUTE);
					}
					else if (filterParameterAttributeRegex.matcher(element.getKey()).matches())
					{
						manageDeletionParameters(allFilterParameters,element,InittypesPackage.Literals.DELETION_PAREMETER,InittypesPackage.Literals.DELETION_PAREMETER__REGEX_ATTRIBUTE);
					}
				}
				documentType.getTypes().addAll(allElements.values());
				documentType.getDeletionParameters().getRegexAttributes().addAll(allDeletionParameters.values());
				documentType.getDeletionParameters().getFilterRegexAttributes().addAll(allFilterParameters.values());
				documentType.setId(id);

				documentTypes.put(type, documentType);
			}
		}

		return documentTypes;
	}

	public static List<String> parseCommaSeparated(String strToParse) {
		LinkedList<String> strList = new LinkedList<String>();
		String[] strs = strToParse.split(","); //$NON-NLS-1$
		for (String str : strs) {
			if (!"".equals(str)) { //$NON-NLS-1$
				strList.add(str);
			}
		}
		return strList;
	}

	public static String serializeCommaSeparated(List<String> strList) {
		StringBuilder b = new StringBuilder();
		for (String str : strList) {
			b.append(str);
			b.append(',');
		}
		// remove last ,
		if (!strList.isEmpty()) {
			b.deleteCharAt(b.length() - 1);
		}
		return b.toString();
	}

	protected static DeletionParameters getOrInitDeletionParameters(DocumentType documentType) {
		DeletionParameters deletionParameters = documentType.getDeletionParameters();
		if (deletionParameters == null) {
			deletionParameters = InittypesFactory.eINSTANCE.createDeletionParameters();
			documentType.setDeletionParameters(deletionParameters);
		}
		return deletionParameters;
	}

	public String dump ()
	{
		InittypesItemProviderAdapterFactory adapterFactory = new InittypesItemProviderAdapterFactory();
		DocumentTypeItemProvider provider = new DocumentTypeItemProvider(adapterFactory);
		RegexItemProvider provider3 = new RegexItemProvider(adapterFactory);
		StringBuilder builder = new StringBuilder();
		builder.append("Type Model >\n"); //$NON-NLS-1$
		for (DocumentType d : typeModel.getDocumentTypes())
		{
			builder.append("\tId : ").append(provider.getText(d)).append(" : ").append(d.getTextType()).append(", hierarchical : ").append(d.isHierarchical()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			builder.append("\t\t").append(provider3.getText(d.getId())).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
			for (Type t : d.getTypes())
			{
				builder.append("\t\t").append(provider3.getText(t)).append(", ").append("\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		return builder.toString();
	}

	/**
	 * @param element
	 * @return
	 */
	private static Regex manageId(Entry<String, String> element, EClass eclass, EStructuralFeature feature)
	{

		Regex type = (Regex) eclass.getEPackage().getEFactoryInstance().create(eclass);
		type.eSet(feature,element.getValue());
		return (Regex) type;
	}

	/**
	 * @param allElements
	 * @param element
	 */
	private static void manageElement(Map<String, Type> allElements, Entry<String, String> element, EClass eclass, EStructuralFeature feature)
	{
		if (element.getValue() == null || element.getValue().length() == 0)
		{
			Activator.getDefault().getLog().log(new Status(Status.WARNING, Activator.PLUGIN_ID, "the element : " + element.getKey() + "=" + element.getValue() + " was ignored because it's empty")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return;
		}
		String elementId = element.getKey().replace("Name", ""); //$NON-NLS-1$ //$NON-NLS-2$
		elementId = elementId.replace("IsText", ""); //$NON-NLS-1$ //$NON-NLS-2$
		elementId = elementId.replace("Label", "");//$NON-NLS-1$ //$NON-NLS-2$
		if (!allElements.containsKey(elementId))
		{
			Regex regex = create(eclass);
			if (InittypesPackage.Literals.TYPE__IS_TEXT.equals(feature))
			{
				regex.eSet(feature,Boolean.parseBoolean(element.getValue()));
			} else {
				regex.eSet(feature,element.getValue());   
			}
			allElements.put(elementId, regex);
		}
		else if (allElements.containsKey(elementId))
		{
			if (InittypesPackage.Literals.TYPE__IS_TEXT.equals(feature))
			{
				(allElements.get(elementId)).eSet(feature,Boolean.parseBoolean(element.getValue()));
			} else {
				(allElements.get(elementId)).eSet(feature,element.getValue());
			}
		}
		if (allElements.get(elementId) instanceof Column)
		{
			Column column = (Column) allElements.get(elementId);
			column.setNumber(Integer.parseInt(elementId.replace("Column", ""))); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	
	private static void manageDeletionParameters(Map<String, DeletionParemeter> allDeletionParameters, Entry<String, String> element, EClass eclass, EStructuralFeature feature)
	{
		if (element.getValue() == null || element.getValue().length() == 0)
		{
			Activator.getDefault().getLog().log(new Status(Status.WARNING, Activator.PLUGIN_ID, "The deletion parameter : " + element.getKey() + "=" + element.getValue() + " has been ignored because it is empty")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return;
		}
		String elementId = element.getKey().replace("Name", ""); //$NON-NLS-1$ //$NON-NLS-2$
		elementId = elementId.replace("Regex", ""); //$NON-NLS-1$ //$NON-NLS-2$
		if (!allDeletionParameters.containsKey(elementId))
		{
			DeletionParemeter param = create(eclass);
			param.eSet(feature,element.getValue());   
			allDeletionParameters.put(elementId, param);
		}
		else if (allDeletionParameters.containsKey(elementId))
		{
			(allDeletionParameters.get(elementId)).eSet(feature,element.getValue());
		}
	}


	@SuppressWarnings("unchecked")
	public static <T> T create (EClass eclass)
	{
		return (T) eclass.getEPackage().getEFactoryInstance().create(eclass);
	}

	private boolean fileDeleted(IFile resource)
	{
		boolean result = false;
		Iterator<DocumentType> iter = typeModel.getDocumentTypes().iterator();

		while (iter.hasNext())
		{
			DocumentType dT = (DocumentType) iter.next();
			if (dT.getDocumentPath().equals(resource.getFullPath().toString()))
			{
				iter.remove();
				result = true;
			}
		}
		return result;
	}

	private void fileUpdated(IFile resource)
	{
		fileDeleted(resource);
		fileAdded(resource);
	}

	public boolean visit(IResourceDelta delta) throws CoreException
	{
		if (delta.getResource() != null)
		{
			switch (delta.getKind())
			{
			case IResourceDelta.ADDED:
				visit(delta.getResource());
				break;
			case IResourceDelta.REMOVED:
				visit(delta.getResource(), kind.DELETE);
				break;
			case IResourceDelta.CHANGED:
				visit(delta.getResource(), kind.MODIFY);
				break;
			}

		}
		return true;
	}

	/**
	 * return the type model
	 * @return type model
	 */
	public TypeModel getTypeModel()
	{
		return typeModel;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getElementsByType(DocumentType documentType, final EClass eclass)
	{
		return (List<T>) Lists.newArrayList(Iterables.filter(documentType.getTypes(), new Predicate<Type>()
			{
			public boolean apply(Type input)
			{
				return eclass.equals(input.eClass());
			}
			}));
	}
	
	public static void save(IFile typesFile, DocumentType documentType)
	{
		save(typesFile, Collections.singleton(documentType));
	}

	public static void save(IFile typesFile, Collection<DocumentType> documentTypes)
	{
		Ini ini = new Wini();

		StringBuilder typeNames = new StringBuilder();

		if (!documentTypes.isEmpty()) {
			for (DocumentType documentType : documentTypes) {
				typeNames.append(documentType.getName());
				typeNames.append(',');
				createDocumentTypeSection(ini, documentType);
			}
			// remove last ,
			typeNames.deleteCharAt(typeNames.length() - 1);
		}

		Section section = ini.add("Types"); //$NON-NLS-1$
		section.add("Names", typeNames); //$NON-NLS-1$

		URI uri = typesFile.getLocationURI();

		try
		{

			ini.store(new File(uri));

			if (!typesFile.exists())
			{
				typesFile.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
		
	}

	private static void createDocumentTypeSection(Ini ini, DocumentType documentType) {
		Section section = ini.add(documentType.getName());
		section.add("Hierarchical", documentType.isHierarchical()); //$NON-NLS-1$

		DeletionParameters deletionParameters = documentType.getDeletionParameters();
		if (deletionParameters != null) {
			section.add("DeletionParameterIdRegex", deletionParameters.getRegexId()); //$NON-NLS-1$
			section.add("DeletionParameterDescriptionRegex", deletionParameters.getRegexDescription()); //$NON-NLS-1$
			section.add("DeletionIsAnd",deletionParameters.isIsAnd()); //$NON-NLS-1$
			// All the deletion parameters of the attributes
			int iAttr = 0;
			for(DeletionParemeter delParam:documentType.getDeletionParameters().getRegexAttributes()){
				section.add("DeletionParameterAttribute"+iAttr+"Name", delParam.getNameAttribute()); //$NON-NLS-1$ //$NON-NLS-2$
				section.add("DeletionParameterAttribute"+iAttr+"Regex", delParam.getRegexAttribute()); //$NON-NLS-1$ //$NON-NLS-2$
				iAttr++;
			}
			for(DeletionParemeter filterParam:documentType.getDeletionParameters().getFilterRegexAttributes()){
				section.add("FilterParameterAttribute"+iAttr+"Name", filterParam.getNameAttribute()); //$NON-NLS-1$ //$NON-NLS-2$
				section.add("FilterParameterAttribute"+iAttr+"Regex", filterParam.getRegexAttribute()); //$NON-NLS-1$ //$NON-NLS-2$
				iAttr++;
			}
		}

		String endText = documentType.getTextType();
		if (endText != null && endText.length() > 0)
		{
			section.add("EndText", endText); //$NON-NLS-1$
		}
		String descriptionRegex = documentType.getTextRegex();
		if (descriptionRegex != null && descriptionRegex.length()>0)
		{
			section.add("DescriptionRegex", descriptionRegex); //$NON-NLS-1$
		}

		Type id = documentType.getId();
		if (id instanceof Column)
		{
			section.add("Requirement" + ((Column)id).getNumber() + "Column", ((Column) id).getExpression()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else if (id instanceof Style)
		{
			section.add("Requirement1Style", ((Style) id).getLabel()); //$NON-NLS-1$
			section.add("Requirement1", ((Style) id).getExpression()); //$NON-NLS-1$
		}
		else if (id instanceof Regex)
		{
			section.add("Requirement1", ((Regex) id).getExpression()); //$NON-NLS-1$
		}

		int i = 0;

		for (Type type : documentType.getTypes())
		{
			if (type instanceof Column)
			{
				Column column = (Column) type;
				section.add("Column"+column.getNumber()+"Name", column.getName()); //$NON-NLS-1$ //$NON-NLS-2$
				section.add("Column"+column.getNumber()+"IsText", column.isIsText()); //$NON-NLS-1$ //$NON-NLS-2$
				if (column.getExpression() != null && column.getExpression().length() > 0)
				{
					section.add("Column"+column.getNumber(), column.getExpression()); //$NON-NLS-1$
				}

			}
			else if (type instanceof Style)
			{
				Style style = (Style) type;
				section.add("Style" + i + "Name", style.getName()); //$NON-NLS-1$ //$NON-NLS-2$
				section.add("Style" + i + "Label", style.getLabel()); //$NON-NLS-1$ //$NON-NLS-2$
				section.add("Style" + i + "IsText", style.isIsText()); //$NON-NLS-1$ //$NON-NLS-2$
				if (style.getExpression() != null  && style.getExpression().length() > 0)
				{
					section.add("Style" + i, style.getExpression()); //$NON-NLS-1$
				}
				i++;
			}
			else if (type instanceof Regex)
			{
				Regex regex = (Regex) type;
				section.add("Attribute" + i + "Name", regex.getName()); //$NON-NLS-1$ //$NON-NLS-2$
				section.add("Attribute" + i, regex.getExpression()); //$NON-NLS-1$
				section.add("Attribute" + i + "IsText", regex.isIsText()); //$NON-NLS-1$ //$NON-NLS-2$
				i++;
			}
		}
	}
	
	
	/**
	 * This method opens a deletion parameters edition dialog. The dialog is filled according to the given deletion parameters.
	 * @param deletionParameters Deletion parameters used for the default values of the dialog. If null, there is no default value (empty) 
	 * @return The edited deletion parameters
	 */
	public static DeletionParameters openDeletionParametersEditionDialog(DeletionParameters deletionParameters)
	{
		DeletionParametersDialog deletionParametersDialog = new DeletionParametersDialog(Display.getDefault().getActiveShell(), deletionParameters);
		if (deletionParametersDialog.open() == Dialog.OK)
		{
			// The modified deletion parameters are collected and returned
			return deletionParametersDialog.getDeletionParameters();
		}
		return null;
	}
	
	/**
	 * This method opens a deletion parameters edition dialog. The dialog is filled according to the given ".type" file.
	 * @param typesFile .type file used for the default values of the dialog. If null, there is no default value (empty) and deletion parameters are created from scratch 
	 * @param saveTypeFile If true, the given file is modified according to the user's modifications 
	 * @return The edited deletion parameters
	 */
	public static DeletionParameters openDeletionParametersEditionDialog(IFile typesFile, boolean saveTypeFile)
	{
		// Case we need to create deletion parameters that to the dialog
		if(typesFile == null)
		{
			DeletionParametersDialog deletionParametersDialog = new DeletionParametersDialog(Display.getDefault().getActiveShell(), null);
			if (deletionParametersDialog.open() == Dialog.OK)
			{
				return deletionParametersDialog.getDeletionParameters();
			}
		}
		// Case we need to edit an existing file
		else
		{
			Map<String, DocumentType> documentTypes = IniManagerRegistry.parseTypesFile(typesFile);
			Set<String> documentTypeNames = documentTypes.keySet();

			if (!documentTypeNames.isEmpty())
			{
				DocumentType selectedDocumentType = null;
				if (documentTypeNames.size() == 1)
				{
					selectedDocumentType = documentTypes.get(documentTypeNames.iterator().next());
				}
				else 
				{
					ComboInputDialog selectDocTypeDialog = new ComboInputDialog(Display.getDefault().getActiveShell(), "Document type to edit", "Please select the document type you want to edit :", documentTypeNames.iterator().next(), documentTypeNames.toArray(new String[documentTypeNames.size()])); //$NON-NLS-1$ //$NON-NLS-2$
					if (selectDocTypeDialog.open() == Dialog.OK)
					{
						// A documentType is chosen from the .type file
						selectedDocumentType = documentTypes.get(selectDocTypeDialog.getValue());
					}
				}
				if (selectedDocumentType != null)
				{
					DeletionParametersDialog deletionParametersDialog = new DeletionParametersDialog(Display.getDefault().getActiveShell(), selectedDocumentType.getDeletionParameters());
					if (deletionParametersDialog.open() == Dialog.OK)
					{
						// The modified deletion parameters are collected
						DeletionParameters deletionParameters = deletionParametersDialog.getDeletionParameters();
						if(saveTypeFile){
							selectedDocumentType.setDeletionParameters(deletionParameters);
							IniManagerRegistry.save(typesFile, documentTypes.values());
							try
							{
								typesFile.refreshLocal(IResource.DEPTH_ZERO, null);
							}
							catch (CoreException e) {}
						}
						return deletionParameters;
					}
				}
			}
		}
		return null;
	}
	
	 

}
