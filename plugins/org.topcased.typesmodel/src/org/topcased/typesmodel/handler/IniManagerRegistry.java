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

package org.topcased.typesmodel.handler;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;
import org.topcased.typesmodel.Activator;
import org.topcased.typesmodel.model.inittypes.Column;
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

    private Pattern attribute = Pattern.compile(Messages.AttributeRegex);
    private Pattern attributeName = Pattern.compile(Messages.AttributeName);
    private Pattern style = Pattern.compile(Messages.StyleRegex);
    private Pattern styleName = Pattern.compile(Messages.StyleName);
    private Pattern styleLabel = Pattern.compile(Messages.StyleLabel);
    private Pattern column = Pattern.compile(Messages.ColumnRegex);
    private Pattern columnName = Pattern.compile(Messages.ColumnName);
    private Pattern requirement = Pattern.compile(Messages.RequirementRegex);
    private Pattern requirementStyle = Pattern.compile(Messages.RequirementStyle);
    private Pattern requirementColumn = Pattern.compile(Messages.RequirementColumn);
    private Pattern hierarchical = Pattern.compile(Messages.Hierarchical);
    private Pattern endText = Pattern.compile(Messages.EndText);
    
    
    private boolean fileAdded(IFile resource)
    {
        IniFileParser parser = new IniFileParser(resource);
        String[] types = parser.getTypes();
        boolean duplicata;
        if (types == null)
        {
            return false;
        }
        for (String type : types)
        {
            Map<String, Type> allElements = new HashMap<String, Type>();
            boolean hieraricalFound = false;
            boolean endTextFound = false;
            Type id = null;
            String endText = null;

            duplicata = exists(resource, type);

            if (!duplicata)
            {
                DocumentType documentType = InittypesFactory.eINSTANCE.createDocumentType();
                documentType.setName(type);
                documentType.setDocumentPath(resource.getFullPath().toString());

                Section section = parser.getElements(type);
                for (Entry<String, String> element : section.entrySet())
                {
                    if (attribute.matcher(element.getKey()).matches())
                    {
                        manageElement(allElements, element,InittypesPackage.Literals.REGEX,InittypesPackage.Literals.REGEX__EXPRESSION);
                    }
                    else if (attributeName.matcher(element.getKey()).matches())
                    {
                        manageElement(allElements, element,InittypesPackage.Literals.REGEX,InittypesPackage.Literals.TYPE__NAME);
                    }
                    else if (column.matcher(element.getKey()).matches())
                    {
                        manageElement(allElements, element,InittypesPackage.Literals.COLUMN,InittypesPackage.Literals.REGEX__EXPRESSION);
                    }
                    else if (columnName.matcher(element.getKey()).matches())
                    {
                        manageElement(allElements, element,InittypesPackage.Literals.COLUMN,InittypesPackage.Literals.TYPE__NAME);
                    }
                    else if (style.matcher(element.getKey()).matches())
                    {
                        manageElement(allElements, element,InittypesPackage.Literals.STYLE,InittypesPackage.Literals.REGEX__EXPRESSION);
                    }
                    else if (styleName.matcher(element.getKey()).matches())
                    {
                        manageElement(allElements, element,InittypesPackage.Literals.STYLE,InittypesPackage.Literals.TYPE__NAME);
                    }
                    else if (styleLabel.matcher(element.getKey()).matches())
                    {
                        manageElement(allElements, element,InittypesPackage.Literals.STYLE,InittypesPackage.Literals.STYLE__LABEL);
                    }
                    else if (requirement.matcher(element.getKey()).matches())
                    {
                        if (id == null)
                        {
                            id = manageId(element,InittypesPackage.Literals.REGEX, InittypesPackage.Literals.REGEX__EXPRESSION);
                        }
                    }
                    else if (requirementStyle.matcher(element.getKey()).matches())
                    {
                        if (id == null)
                        {
                            id = manageId(element,InittypesPackage.Literals.STYLE,InittypesPackage.Literals.STYLE__LABEL);
                        }
                    }
                    else if (requirementColumn.matcher(element.getKey()).matches())
                    {
                        if (id == null)
                        {
                            id = manageId(element,InittypesPackage.Literals.COLUMN, InittypesPackage.Literals.REGEX__EXPRESSION);
                        }
                    }
                    else if (hierarchical.matcher(element.getKey()).matches())
                    {
                        if (!hieraricalFound)
                        {
                            if ("True".equalsIgnoreCase(element.getValue())) { //$NON-NLS-1$
                                documentType.setHierarchical(true);
                            }
                            else
                            {
                                documentType.setHierarchical(false);
                            }
                            hieraricalFound = true;
                        }
                    }
                    else if (this.endText.matcher(element.getKey()).matches())
                    {
                        if (!endTextFound)
                        {
                            endText = element.getValue();
                        }
                    }
                }
                documentType.getTypes().addAll(allElements.values());
                documentType.setId(id);
                documentType.setTextType(endText);
                typeModel.getDocumentTypes().add(documentType);
            }
        }
        Activator.getDefault().debug(dump());
        return true;
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
    private Regex manageId(Entry<String, String> element, EClass eclass, EStructuralFeature feature)
    {
        
        Regex type = (Regex) eclass.getEPackage().getEFactoryInstance().create(eclass);
        type.eSet(feature,element.getValue());
        return (Regex) type;
    }

    /**
     * @param allElements
     * @param element
     */
    private void manageElement(Map<String, Type> allElements, Entry<String, String> element, EClass eclass, EStructuralFeature feature)
    {
        if (element.getValue().isEmpty())
        {
            Activator.getDefault().getLog().log(new Status(Status.WARNING, Activator.PLUGIN_ID, "the element : " + element.getKey() + "=" + element.getValue() + " was ignored because it's empty")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return;
        }
        String elementId = element.getKey().replace("Name", ""); //$NON-NLS-1$ //$NON-NLS-2$
        elementId = elementId.replace("Label", "");//$NON-NLS-1$ //$NON-NLS-2$
        if (!allElements.containsKey(elementId))
        {
            Regex regex = create(eclass);
            regex.eSet(feature,element.getValue());
            allElements.put(elementId, regex);
        }
        else if (allElements.containsKey(elementId) && (allElements.get(elementId)).eGet(feature) == null)
        {
            (allElements.get(elementId)).eSet(feature,element.getValue());
        }
        if (allElements.get(elementId) instanceof Column)
        {
            Column column = (Column) allElements.get(elementId);
            column.setNumber(Integer.parseInt(elementId.replace("Column", ""))); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    
    @SuppressWarnings("unchecked")
    public <T> T create (EClass eclass)
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

    private boolean exists(IFile resource, String type)
    {
        for (DocumentType document : typeModel.getDocumentTypes())
        {
            if (resource.getLocation().toString().equals(document.getDocumentPath()) && type.equals(document.getName()))
            {
                return true;
            }
        }
        return false;
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

    public static void save(IFile typesFile, Collection<Type> types, Type id, boolean hierarchical, String endText)
    {
//            path.
//        IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(typesFile);
        
        URI uri = typesFile.getLocationURI();
        
        Ini ini = new Wini();
        Section section = ini.add("Types");
        section.add("Names", typesFile.getName().replace(".types", ""));
        section = ini.add(typesFile.getName().replace(".types", ""));
        section.add("Hierarchical", hierarchical);
        if (endText != null && !endText.isEmpty())
        {
            section.add("EndText", endText);
        }
        if (id instanceof Column)
        {
            section.add("Requirement" + ((Column)id).getNumber() + "Column", ((Column) id).getExpression());
        }
        else if (id instanceof Style)
        {
            section.add("Requirement1Style", ((Style) id).getLabel());
        }
        else if (id instanceof Regex)
        {
            section.add("Requirement1", ((Regex) id).getExpression());
        }
        
        int i = 0;
        
        for (Type type : types)
        {
            if (type instanceof Column)
            {
                Column column = (Column) type;
                section.add("Column"+column.getNumber()+"Name", column.getName());
                if (column.getExpression() != null && !column.getExpression().isEmpty())
                {
                    section.add("Column"+column.getNumber(), column.getExpression());
                }
            }
            else if (type instanceof Style)
            {
                Style style = (Style) type;
                section.add("Style" + i + "Name", style.getName());
                section.add("Style" + i + "Label", style.getLabel());
                if (style.getExpression() != null  && !style.getExpression().isEmpty())
                {
                    section.add("Style" + i, style.getExpression());
                }
                i++;
            }
            else if (type instanceof Regex)
            {
                Regex regex = (Regex) type;
                section.add("Attribute" + i + "Name", regex.getName());
                section.add("Attribute" + i, regex.getExpression());
                i++;
            }
        }
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
    
}
