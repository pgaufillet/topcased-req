/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.topcased.requirement.*;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * @generated
 */
public class RequirementFactoryImpl extends EFactoryImpl implements RequirementFactory
{
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public static RequirementFactory init()
    {
        try
        {
            RequirementFactory theRequirementFactory = (RequirementFactory) EPackage.Registry.INSTANCE.getEFactory("http://org.topcased.requirement/1.0"); //$NON-NLS-1$ 
            if (theRequirementFactory != null)
            {
                return theRequirementFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new RequirementFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public RequirementFactoryImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass)
    {
        switch (eClass.getClassifierID())
        {
            case RequirementPackage.REQUIREMENT_PROJECT:
                return createRequirementProject();
            case RequirementPackage.HIERARCHICAL_ELEMENT:
                return createHierarchicalElement();
            case RequirementPackage.CURRENT_REQUIREMENT:
                return createCurrentRequirement();
            case RequirementPackage.ATTRIBUTE_CONFIGURATION:
                return createAttributeConfiguration();
            case RequirementPackage.CONFIGURATED_ATTRIBUTE:
                return createConfiguratedAttribute();
            case RequirementPackage.ATTRIBUTE_VALUE:
                return createAttributeValue();
            case RequirementPackage.DEFAULT_ATTRIBUTE_VALUE:
                return createDefaultAttributeValue();
            case RequirementPackage.TEXT_ATTRIBUTE:
                return createTextAttribute();
            case RequirementPackage.OBJECT_ATTRIBUTE:
                return createObjectAttribute();
            case RequirementPackage.UPSTREAM_MODEL:
                return createUpstreamModel();
            case RequirementPackage.ATTRIBUTE_LINK:
                return createAttributeLink();
            case RequirementPackage.ATTRIBUTE_ALLOCATE:
                return createAttributeAllocate();
            case RequirementPackage.UNTRACED_CHAPTER:
                return createUntracedChapter();
            case RequirementPackage.PROBLEM_CHAPTER:
                return createProblemChapter();
            case RequirementPackage.TRASH_CHAPTER:
                return createTrashChapter();
            case RequirementPackage.ANONYMOUS_REQUIREMENT:
                return createAnonymousRequirement();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue)
    {
        switch (eDataType.getClassifierID())
        {
            case RequirementPackage.ATTRIBUTES_TYPE:
                return createAttributesTypeFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue)
    {
        switch (eDataType.getClassifierID())
        {
            case RequirementPackage.ATTRIBUTES_TYPE:
                return convertAttributesTypeToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public RequirementProject createRequirementProject()
    {
        RequirementProjectImpl requirementProject = new RequirementProjectImpl();
        return requirementProject;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public HierarchicalElement createHierarchicalElement()
    {
        HierarchicalElementImpl hierarchicalElement = new HierarchicalElementImpl();
        return hierarchicalElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public CurrentRequirement createCurrentRequirement()
    {
        CurrentRequirementImpl currentRequirement = new CurrentRequirementImpl();
        return currentRequirement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public AttributeConfiguration createAttributeConfiguration()
    {
        AttributeConfigurationImpl attributeConfiguration = new AttributeConfigurationImpl();
        return attributeConfiguration;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ConfiguratedAttribute createConfiguratedAttribute()
    {
        ConfiguratedAttributeImpl configuratedAttribute = new ConfiguratedAttributeImpl();
        return configuratedAttribute;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public AttributeValue createAttributeValue()
    {
        AttributeValueImpl attributeValue = new AttributeValueImpl();
        return attributeValue;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public DefaultAttributeValue createDefaultAttributeValue()
    {
        DefaultAttributeValueImpl defaultAttributeValue = new DefaultAttributeValueImpl();
        return defaultAttributeValue;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public TextAttribute createTextAttribute()
    {
        TextAttributeImpl textAttribute = new TextAttributeImpl();
        return textAttribute;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ObjectAttribute createObjectAttribute()
    {
        ObjectAttributeImpl objectAttribute = new ObjectAttributeImpl();
        return objectAttribute;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public UpstreamModel createUpstreamModel()
    {
        UpstreamModelImpl upstreamModel = new UpstreamModelImpl();
        return upstreamModel;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public AttributeLink createAttributeLink()
    {
        AttributeLinkImpl attributeLink = new AttributeLinkImpl();
        return attributeLink;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public AttributeAllocate createAttributeAllocate()
    {
        AttributeAllocateImpl attributeAllocate = new AttributeAllocateImpl();
        return attributeAllocate;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public UntracedChapter createUntracedChapter()
    {
        UntracedChapterImpl untracedChapter = new UntracedChapterImpl();
        return untracedChapter;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ProblemChapter createProblemChapter()
    {
        ProblemChapterImpl problemChapter = new ProblemChapterImpl();
        return problemChapter;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public TrashChapter createTrashChapter()
    {
        TrashChapterImpl trashChapter = new TrashChapterImpl();
        return trashChapter;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public AnonymousRequirement createAnonymousRequirement()
    {
        AnonymousRequirementImpl anonymousRequirement = new AnonymousRequirementImpl();
        return anonymousRequirement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public AttributesType createAttributesTypeFromString(EDataType eDataType, String initialValue)
    {
        AttributesType result = AttributesType.get(initialValue);
        if (result == null)
            throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return result;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String convertAttributesTypeToString(EDataType eDataType, Object instanceValue)
    {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public RequirementPackage getRequirementPackage()
    {
        return (RequirementPackage) getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static RequirementPackage getPackage()
    {
        return RequirementPackage.eINSTANCE;
    }

} // RequirementFactoryImpl
