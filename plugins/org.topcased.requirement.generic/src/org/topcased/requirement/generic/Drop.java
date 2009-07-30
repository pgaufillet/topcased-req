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
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) tristan.faure@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.generic;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ReflectiveItemProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.ui.PlatformUI;
import org.topcased.modeler.edit.DiagramEditPart;
import org.topcased.modeler.edit.EMFGraphEdgeEditPart;
import org.topcased.modeler.edit.EMFGraphNodeEditPart;
import org.topcased.requirement.generic.preferences.PreferencesHelper;
import org.topcased.sam.requirement.Attribute;
import org.topcased.sam.requirement.AttributeAllocate;
import org.topcased.sam.requirement.AttributeConfiguration;
import org.topcased.sam.requirement.AttributeLink;
import org.topcased.sam.requirement.AttributesType;
import org.topcased.sam.requirement.ConfiguratedAttribute;
import org.topcased.sam.requirement.CurrentRequirement;
import org.topcased.sam.requirement.HierarchicalElement;
import org.topcased.sam.requirement.ObjectAttribute;
import org.topcased.sam.requirement.RequirementFactory;
import org.topcased.sam.requirement.RequirementPackage;
import org.topcased.sam.requirement.RequirementProject;
import org.topcased.sam.requirement.TextAttribute;
import org.topcased.sam.requirement.core.preferences.ComputeRequirementIdentifier;
import org.topcased.sam.requirement.core.preferences.NamingRequirementPreferenceHelper;
import org.topcased.sam.requirement.core.utils.RequirementUtils;
import org.topcased.sam.requirement.core.views.current.CurrentPage;
import org.topcased.sam.requirement.core.views.current.CurrentRequirementView;
import org.topcased.sam.requirement.core.views.upstream.UpstreamPage;
import org.topcased.sam.requirement.core.views.upstream.UpstreamRequirementView;
import org.topcased.ttm.Document;
import org.topcased.ttm.Requirement;
import org.topcased.ttm.Section;

/**
 * Class Drop
 * @author tfaure
 *
 */
public class Drop extends AbstractTransferDropTargetListener
{
    static RequirementFactory factory = RequirementFactory.eINSTANCE;

    private EditPart currentPart;

    private static Requirement source;

    private static TypeCacheAdapter typeCacheAdapter;

    public Drop(EditPartViewer viewer)
    {
        super(viewer, GenericTransfer.getInstance());
    }

    /**
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#createTargetRequest()
     */
    protected Request createTargetRequest()
    {
        return new Request("selection");
    }

    @Override
    public void drop(DropTargetEvent event)
    {
        if (currentPart != null)
        {
            EObject eobject = null;
            source = getSource(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection());
            if (currentPart instanceof EMFGraphNodeEditPart)
            {
                EMFGraphNodeEditPart emfPart = (EMFGraphNodeEditPart) currentPart;
                eobject = emfPart.getEObject();
            }
            else if (currentPart instanceof EMFGraphEdgeEditPart)
            {
                EMFGraphEdgeEditPart emfPart = (EMFGraphEdgeEditPart) currentPart;
                eobject = emfPart.getEObject();
            }
            if (eobject != null)
            {
                CurrentRequirementView view = (CurrentRequirementView) CurrentRequirementView.getInstance();
                if (view.getCurrentPage() instanceof CurrentPage)
                {
                    CurrentPage page = (CurrentPage) view.getCurrentPage();
                    RequirementProject obj = (RequirementProject) page.getViewer().getInput();
                    addNewRequirement(eobject, obj);
                    page.getViewer().refresh();
                    if (((UpstreamRequirementView) UpstreamRequirementView.getInstance()).getCurrentPage() instanceof UpstreamPage)
                    {
                        UpstreamPage page2 = (UpstreamPage) ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).getCurrentPage();
                        ((TreeViewer)page2.getViewer()).refresh(source);

                    }
                }
            }
        }
    }

    /**
     * Construct a tree between eobject and stop or obj
     * 
     * @param stop
     * @param eobject
     * @param obj
     * @return
     */
    private static HierarchicalElement constructTree(CompoundCommand command, HierarchicalElement stop, EObject eobject, RequirementProject obj)
    {
        EObject parent = eobject;
        HierarchicalElement aNew = null;
        HierarchicalElement hier = null;
        HierarchicalElement topDown = null;
        while (parent != null)
        {
            if (stop != null)
            {
                if (stop.getSamElement() == parent)
                {
                    command.appendIfCanExecute(AddCommand.create(AdapterFactoryEditingDomain.getEditingDomainFor(stop), stop, RequirementPackage.Literals.HIERARCHICAL_ELEMENT__CHILDREN, hier));
                    // stop.getChildren().add(hier);
                    break;
                }
            }
            hier = factory.createHierarchicalElement();
            if (aNew != null)
            {
                hier.getChildren().add(aNew);
            }
            if (topDown == null)
            {
                topDown = hier;
            }
            hier.setSamElement(parent);
            aNew = hier;
            parent = parent.eContainer();
            if (parent == null)
            {
                command.appendIfCanExecute(AddCommand.create(AdapterFactoryEditingDomain.getEditingDomainFor(obj), obj, RequirementPackage.Literals.REQUIREMENT_PROJECT__HIERARCHICAL_ELEMENT, hier));
                // obj.getHierarchicalElement().add(hier);
            }
        }
        return topDown;
    }

    /**
     * Adds the new requirement. managing a correct hierarchy
     * 
     * @param eobject the eobject
     * @param obj the obj
     */
    public static void addNewRequirement(EObject eobject, RequirementProject obj)
    {
        // get an existing element
        if (obj == null)
        {
            return;
        }
        CompoundCommand command = new CompoundCommand();
        HierarchicalElement hier = getHierarchicalElement(eobject, obj.getHierarchicalElement());
        // if it doesn't exist we create the structure
        if (hier == null)
        {
            hier = constructTree(command, null, eobject, obj);
        }
        else
        {
            if (hier.getSamElement() != eobject)
            {
                hier = constructTree(command, hier, eobject, obj);
            }
        }
        if (hier != null)
        {
            addNewRequirement(eobject, obj, command, hier);

        }
    }

    public static void addNewRequirement(EObject eobject, RequirementProject obj, CompoundCommand command, HierarchicalElement hier)
    {
        if (hier.getSamElement() != eobject)
        {
            HierarchicalElement subhier = factory.createHierarchicalElement();
            hier.getChildren().add(subhier);
            subhier.setSamElement(eobject);
        }
        CurrentRequirement req = createRequirement();
        command.append(AddCommand.create(AdapterFactoryEditingDomain.getEditingDomainFor(obj), hier, RequirementPackage.Literals.HIERARCHICAL_ELEMENT__REQUIREMENT, req));
        // hier.getRequirement().add(req);
        // RequirementUtils.saveResource(obj.eResource());
        AdapterFactoryEditingDomain.getEditingDomainFor(obj).getCommandStack().execute(command);
        // we call set ident after execution to have the requirement store in the resource
        setIdent(source, req, hier, eobject);
    }

    /**
     * Sets the ident.
     * 
     * @param source2 the source2
     * @param req the req
     * @param hier the hier
     * @param target the target
     */
    public static void setIdent(Requirement source2, CurrentRequirement req, HierarchicalElement hier, EObject target)
    {
        EditingDomain editingDomainFor = AdapterFactoryEditingDomain.getEditingDomainFor(source2);
        Resource resource = RequirementUtils.getRequirementModel(editingDomainFor);
        ComputeRequirementIdentifier.INSTANCE.setPreferenceStore(resource);
        String identBeforeAttributesProcess = "";
        // if we use the new algo max based
        if (PreferencesHelper.getNewAlgo())
        {
            // we call the default mechanism without transforming number
            identBeforeAttributesProcess = callIdentString(editingDomainFor, hier, req.getIdentifier());
            // we transform the number getting max using format
            identBeforeAttributesProcess = processCount(editingDomainFor, hier, identBeforeAttributesProcess);
        }
        else
        {
            // we use sam behavior
            identBeforeAttributesProcess = ComputeRequirementIdentifier.INSTANCE.computeIdent(editingDomainFor, hier, req.getIdentifier(), 1);
        }
        // we apply the attributes not defined for sam
        identBeforeAttributesProcess = applyAttributes(identBeforeAttributesProcess, target);
        req.setIdentifier(identBeforeAttributesProcess);
    }

    /**
     * Call ident string. Reflexive method to call private methods
     * 
     * @param editingDomainFor the editing domain for
     * @param hier the hier
     * @param identifier the identifier
     * 
     * @return the string
     */
    private static String callIdentString(EditingDomain editingDomainFor, HierarchicalElement hier, String identifier)
    {
        String result = "";
        try
        {
            Method[] ms = ComputeRequirementIdentifier.class.getDeclaredMethods();
            Method m = null;
            for (Method tmp : ms)
            {
                if (tmp.getName().equals("computeFullIdentifier"))
                {
                    m = tmp;
                    break;
                }
            }
            if (m != null)
            {
                setFormatToInstance();
                m.setAccessible(true);
                result = (String) m.invoke(ComputeRequirementIdentifier.INSTANCE, editingDomainFor, hier, identifier, null);
                m.setAccessible(false);
            }
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Sets the format to instance.
     * 
     * We inject the format to instance
     */
    private static void setFormatToInstance()
    {
        try
        {
            Field f = ComputeRequirementIdentifier.class.getDeclaredField("currentFormat");
            Field initialFormt = ComputeRequirementIdentifier.class.getDeclaredField("initialFormat");
            if (f != null && initialFormt != null)
            {
                f.setAccessible(true);
                initialFormt.setAccessible(true);
                f.set(ComputeRequirementIdentifier.INSTANCE, initialFormt.get(ComputeRequirementIdentifier.INSTANCE));
                initialFormt.setAccessible(false);
                f.setAccessible(false);
            }
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Process count. we replace the {number} parameter by the max plus step
     * 
     * @param editingDomainFor the editing domain for
     * @param hier the hier
     * @param format the format
     * 
     * @return the string
     */
    private static String processCount(EditingDomain editingDomainFor, HierarchicalElement hier, String format)
    {
        String result = "";
        if (format.contains("{number}"))
        {
            result = format.replace("{number}", getMaxPlusOne(editingDomainFor, hier));
        }
        return result;
    }

    private static String getFormat()
    {
        return getPreferenceStore().getString(NamingRequirementPreferenceHelper.NAMING_FORMAT_REQUIREMENT_STORE);
    }

    private static IPreferenceStore getPreferenceStore()
    {
        IPreferenceStore result = null;
        try
        {
            Field f = ComputeRequirementIdentifier.class.getDeclaredField("preferenceStore");
            if (f != null)
            {
                f.setAccessible(true);
                result = (IPreferenceStore) f.get(ComputeRequirementIdentifier.INSTANCE);
                f.setAccessible(false);
            }
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Gets the max plus one. compute the max using the format of the requirement
     * 
     * @param editingDomainFor the editing domain for
     * @param hier the hier
     * 
     * @return the max plus one
     */
    private static String getMaxPlusOne(EditingDomain editingDomainFor, HierarchicalElement hier)
    {
        String result = "";
        int max = 0;
        Resource r = hier.eResource();
        Collection<EObject> currents = getElements(RequirementPackage.Literals.CURRENT_REQUIREMENT,r);
        for (EObject e : currents)
        {
            if (e instanceof CurrentRequirement)
            {
                CurrentRequirement current = (CurrentRequirement) e;
                int value = getNumberOfCurrent(current);
                if (value > max)
                {
                    max = value;
                }
            }
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(5);
        nf.setGroupingUsed(false);
        result = nf.format(max + PreferencesHelper.getStep());
        return result;
    }

    public static Collection<EObject> getElements(EClass c, Resource r)
    {
        if (typeCacheAdapter == null || !r.getResourceSet().eAdapters().contains(typeCacheAdapter))
        {
            if (typeCacheAdapter == null)
            {
                typeCacheAdapter = new TypeCacheAdapter();
            }
            r.getResourceSet().eAdapters().add(typeCacheAdapter);
        }
        return typeCacheAdapter.getReachableObjectsOfType(RequirementPackage.Literals.CURRENT_REQUIREMENT, r.getResourceSet()) ;
//        return typeCacheAdapter.getReachableObjectsOfType(object, type) ;
    }
    
    private static int getNumberOfCurrent(CurrentRequirement current)
    {
        int value = -1;
        String format = getFormat();
        String regex = format.replace("{number}", "(\\d*)");
        regex = regex.replaceAll("\\{[^\\{]*\\}", "\\\\w*");
        Pattern pat = Pattern.compile(regex);
        Matcher m = pat.matcher(current.getIdentifier());
        if (m.matches())
        {
            if (m.groupCount() > 0)
            {
                String number = m.group(1);
                value = Integer.valueOf(number);
            }
        }
        return value;
    }

    private static String applyAttributes(String identBeforeAttributesProcess, EObject target)
    {

        String result = identBeforeAttributesProcess;
        ReflectiveItemProvider provider = new ReflectiveItemProvider(new ReflectiveItemProviderAdapterFactory());
        if (identBeforeAttributesProcess.contains("model name"))
        {
            result = result.replaceAll("\\{model name\\}", getLabel(EcoreUtil.getRootContainer(target), provider));
        }
        if (identBeforeAttributesProcess.contains("resource name"))
        {
            URI uri = target.eResource().getURI().trimFileExtension();
            result = result.replaceAll("\\{resource name\\}", uri.segment(uri.segmentCount() - 1).toString());
        }
        return result;
    }

    /**
     * Gets the label.
     * 
     * @param o the o
     * @param provider the provider
     * 
     * @return the label
     */
    public static String getLabel(EObject o, ReflectiveItemProvider provider)
    {
        EClass eClass = o.eClass();
        EAttribute result = null;
        for (EAttribute eAttribute : eClass.getEAllAttributes())
        {
            if (!eAttribute.isMany() && eAttribute.getEType().getInstanceClass() != FeatureMap.Entry.class)
            {
                if ("name".equalsIgnoreCase(eAttribute.getName()))
                {
                    result = eAttribute;
                    break;
                }
                else if (result == null)
                {
                    result = eAttribute;
                }
                else if (eAttribute.getEAttributeType().getInstanceClass() == String.class && result.getEAttributeType().getInstanceClass() != String.class)
                {
                    result = eAttribute;
                }
            }
        }
        String toReturn = "";
        if (result != null)
        {
            toReturn = (String) o.eGet(result);
            if (toReturn == null)
            {
                toReturn = provider.getText(o);
            }
        }
        else
        {
            toReturn = provider.getText(o);
        }
        return toReturn;
    }

    /**
     * Gets the hierarchical element.
     * 
     * @param eobject the eobject
     * @param hierarchicalElement the hierarchical element
     * 
     * @return the hierarchical element
     */
    private static HierarchicalElement getHierarchicalElement(EObject eobject, EList<HierarchicalElement> hierarchicalElement)
    {
        HierarchicalElement result = null;
        for (HierarchicalElement e : hierarchicalElement)
        {
            if (EcoreUtil.isAncestor(e.getSamElement(), eobject))
            {
                result = getHierarchicalElement(eobject, e.getChildren());
                if (result == null)
                {
                    result = e;
                }
            }
        }
        return result;
    }

    /**
     * Gets the source.
     * 
     * @param object the object
     * 
     * @return the source
     */
    private Requirement getSource(ISelection object)
    {
        if (object instanceof IStructuredSelection)
        {
            Collection< ? > selection = ((IStructuredSelection) object).toList();
            Set<Object> sortedSelection = new HashSet<Object>();
            for (Object obj : selection)
            {
                // this selection comes from the upstream view. It means that all requirements need to be extracted
                if (obj instanceof Document || obj instanceof Section)
                {
                    return (Requirement) RequirementUtils.getUpstream((EObject) obj).iterator().next();
                }
                else
                {
                    return (Requirement) obj;
                }
            }
        }
        return null;
    }

    /**
     * Creates the requirement.
     * 
     * @return the current requirement
     */
    private static CurrentRequirement createRequirement()
    {
        CurrentRequirement req = factory.createCurrentRequirement();
        req.setIdentifier(source.getIdent());
        // we set to true impacted to have the possibility to delete
        req.setImpacted(false);
        EObject rootContainer = EcoreUtil.getRootContainer(source);
        if (rootContainer instanceof RequirementProject)
        {
            RequirementProject project = (RequirementProject) rootContainer;
            AttributeConfiguration config = project.getAttributeConfiguration();
            if (config != null && !config.getListAttributes().isEmpty())
            {
                for (ConfiguratedAttribute attribute : config.getListAttributes())
                {
                    org.topcased.sam.requirement.Attribute newAttribute = null;
                    if (AttributesType.TEXT_VALUE == attribute.getType().getValue())
                    {
                        newAttribute = createAttributeText(attribute, source);
                    }
                    else if (AttributesType.ALLOCATE_VALUE == attribute.getType().getValue())
                    {
                        newAttribute = createAttributeAllocate(attribute.getName());
                    }
                    else if (req instanceof CurrentRequirement && AttributesType.LINK_VALUE == attribute.getType().getValue())
                    {
                        newAttribute = createAttributeLink(attribute.getName(), source);
                    }
                    else if (AttributesType.OBJECT_VALUE == attribute.getType().getValue())
                    {
                        newAttribute = createAttributeReference(attribute.getName());
                    }
                    if (newAttribute != null)
                    {
                        req.getAttribute().add(newAttribute);
                    }
                }
            }
        }
        else
        {
            // default old behaviour
            req.getAttribute().add(createAttribute(RequirementPackage.Literals.ATTRIBUTE_ALLOCATE, "#Allocate", ""));
            req.getAttribute().add(createAttribute(RequirementPackage.Literals.TEXT_ATTRIBUTE, "#Covered_by", ""));
            req.getAttribute().add(createAttribute(RequirementPackage.Literals.TEXT_ATTRIBUTE, "#Justify", ""));
            req.getAttribute().add(createAttribute(RequirementPackage.Literals.ATTRIBUTE_LINK, "#Link_to", source));
            req.getAttribute().add(createAttribute(RequirementPackage.Literals.TEXT_ATTRIBUTE, "#Maturity", "TBD"));
        }
        return req;

    }

    /**
     * Creates an <b>{@link ObjectAttribute}</b> object.
     * 
     * @param name The attribute name
     * @return the attribute object created
     */
    private static ObjectAttribute createAttributeReference(String name)
    {
        ObjectAttribute att = RequirementFactory.eINSTANCE.createObjectAttribute();
        att.setName(name);
        return att;
    }

    /**
     * Creates an <b>{@link AttributeAllocate}</b> object.
     * 
     * @param name The attribute name
     * @return the attribute allocate created
     */
    private static AttributeAllocate createAttributeAllocate(String name)
    {
        AttributeAllocate att = RequirementFactory.eINSTANCE.createAttributeAllocate();
        att.setName(name);
        return att;
    }

    /**
     * Creates a <b>{@link TextAttribute}</b> with the default values or with the value of the same attribute from the
     * source requirement
     * 
     * @param attName : the attribute name
     * @param reqSource : the source requirement
     * @return the attribute text created
     */
    private static TextAttribute createAttributeText(ConfiguratedAttribute attribute, Requirement reqSource)
    {
        TextAttribute newAtt = RequirementFactory.eINSTANCE.createTextAttribute();
        newAtt.setName(attribute.getName());
        String attributeSourceValue = isExist(newAtt.getName(), reqSource);
        if (attributeSourceValue != null)
        {
            newAtt.setValue(attributeSourceValue);
        }
        else
        {
            String value = attribute.getDefaultValue() != null ? attribute.getDefaultValue().getValue().getValue() : "";
            newAtt.setValue(value);
        }
        return newAtt;
    }

    /**
     * Search the attribute value in the upstream requirement
     * 
     * @param attribute The attribute to search
     * @param upstream The upstream requirement
     * @return The value of the attribute
     */
    private static String isExist(String attribute, Requirement upstream)
    {
        String result = null;
        if (upstream != null)
        {
            for (org.topcased.ttm.Attribute attr : ((Requirement) upstream).getAttributes())
            {
                if (attr.getName().equalsIgnoreCase(attribute))
                {
                    result = attr.getValue();
                }
            }
        }
        return result;
    }

    /**
     * Creates the <b>{@link LinkAttributes}</b> corresponding to the upstream requirement dropped.
     * 
     * @param reqSource The upstream requirement
     * @return collection of link attribute
     */
    public static Collection<AttributeLink> createAttributeLink(Requirement reqSource)
    {
        Collection<AttributeLink> result = new ArrayList<AttributeLink>();
        AttributeConfiguration configuration = RequirementUtils.getAttributeConfiguration(AdapterFactoryEditingDomain.getEditingDomainFor(reqSource));
        for (ConfiguratedAttribute attribute : configuration.getListAttributes())
        {
            if (attribute.getType().getValue() == AttributesType.LINK_VALUE)
            {
                result.add(createAttributeLink(attribute.getName(), reqSource));
            }
        }
        return result;
    }

    /**
     * Creates an <b>{@link AttributeLink}</b> object. The value is set to the reqSource requirement
     * 
     * @param name The attribute name
     * @param reqSource The source requirement
     * @return the attribute link created
     */
    private static AttributeLink createAttributeLink(String name, Requirement reqSource)
    {
        AttributeLink att = RequirementFactory.eINSTANCE.createAttributeLink();
        att.setName(name);
        att.setValue(reqSource);
        att.setPartial(false);
        return att;
    }

    private static Attribute createAttribute(EClass toCreate, String name, EObject value)
    {
        Attribute attr = (Attribute) factory.create(toCreate);
        attr.setName(name);
        if (attr instanceof AttributeLink)
        {
            AttributeLink text = (AttributeLink) attr;
            text.setValue(value);
            text.setPartial(false);
        }
        return attr;
    }

    private static Attribute createAttribute(EClass toCreate, String name, String value)
    {
        Attribute attr = (Attribute) factory.create(toCreate);
        attr.setName(name);
        if (attr instanceof TextAttribute)
        {
            TextAttribute text = (TextAttribute) attr;
            text.setValue(value);
        }
        return attr;
    }

    @Override
    protected EditPart getTargetEditPart()
    {

        EditPart currentPartTmp = super.getTargetEditPart();
        if (currentPartTmp instanceof DiagramEditPart)
        {
            currentPart = null;
        }
        else
        {
            currentPart = currentPartTmp;
        }
        return currentPart;
    }

    @Override
    public void dragOver(DropTargetEvent event)
    {
        ISelection selection = ((LocalSelectionTransfer) getTransfer()).getSelection();
        Collection< ? > source = extractDragSource(selection);

        event.detail = DND.DROP_COPY;

        for (Object s : source)
        {
            if (!(s instanceof Requirement) && !(s instanceof org.topcased.sam.requirement.Requirement))
            {
                event.operations = DND.DROP_NONE;
                event.detail = DND.DROP_NONE;
            }
        }
        super.dragOver(event);
    }

    protected Collection< ? > extractDragSource(Object object)
    {
        // Transfer the data and convert the structured selection to a collection of objects.
        if (object instanceof IStructuredSelection)
        {
            Collection< ? > selection = ((IStructuredSelection) object).toList();
            Set<Object> sortedSelection = new HashSet<Object>();
            for (Object obj : selection)
            {
                // this selection comes from the upstream view. It means that all requirements need to be extracted
                if (obj instanceof Document || obj instanceof Section)
                {
                    Collection<Requirement> allRequirements = RequirementUtils.getUpstream((EObject) obj);
                    for (Requirement requirement : allRequirements)
                    {
                        sortedSelection.add(requirement);
                    }
                }
                else
                {
                    sortedSelection.add(obj);
                }
            }
            return sortedSelection;
        }
        else
        {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    protected void updateTargetRequest()
    {
    }

}
