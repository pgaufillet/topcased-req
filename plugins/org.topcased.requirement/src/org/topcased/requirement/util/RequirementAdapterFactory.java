/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.topcased.requirement.*;

import ttm.AttributeOwner;
import ttm.Element;
import ttm.Project;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter <code>createXXX</code>
 * method for each class of the model. <!-- end-user-doc -->
 * @see org.topcased.requirement.RequirementPackage
 * @generated
 */
public class RequirementAdapterFactory extends AdapterFactoryImpl
{
    /**
     * The cached model package.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected static RequirementPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public RequirementAdapterFactory()
    {
        if (modelPackage == null)
        {
            modelPackage = RequirementPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc --> This
     * implementation returns <code>true</code> if the object is either the model's package or is an instance object of
     * the model. <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object)
    {
        if (object == modelPackage)
        {
            return true;
        }
        if (object instanceof EObject)
        {
            return ((EObject) object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected RequirementSwitch<Adapter> modelSwitch = new RequirementSwitch<Adapter>()
    {
        @Override
        public Adapter caseRequirementProject(RequirementProject object)
        {
            return createRequirementProjectAdapter();
        }

        @Override
        public Adapter caseHierarchicalElement(HierarchicalElement object)
        {
            return createHierarchicalElementAdapter();
        }

        @Override
        public Adapter caseCurrentRequirement(CurrentRequirement object)
        {
            return createCurrentRequirementAdapter();
        }

        @Override
        public Adapter caseAttribute(Attribute object)
        {
            return createAttributeAdapter();
        }

        @Override
        public Adapter caseAttributeConfiguration(AttributeConfiguration object)
        {
            return createAttributeConfigurationAdapter();
        }

        @Override
        public Adapter caseConfiguratedAttribute(ConfiguratedAttribute object)
        {
            return createConfiguratedAttributeAdapter();
        }

        @Override
        public Adapter caseAttributeValue(AttributeValue object)
        {
            return createAttributeValueAdapter();
        }

        @Override
        public Adapter caseDefaultAttributeValue(DefaultAttributeValue object)
        {
            return createDefaultAttributeValueAdapter();
        }

        @Override
        public Adapter caseIdentifiedElement(IdentifiedElement object)
        {
            return createIdentifiedElementAdapter();
        }

        @Override
        public Adapter caseSpecialChapter(SpecialChapter object)
        {
            return createSpecialChapterAdapter();
        }

        @Override
        public Adapter caseTextAttribute(TextAttribute object)
        {
            return createTextAttributeAdapter();
        }

        @Override
        public Adapter caseObjectAttribute(ObjectAttribute object)
        {
            return createObjectAttributeAdapter();
        }

        @Override
        public Adapter caseUpstreamModel(UpstreamModel object)
        {
            return createUpstreamModelAdapter();
        }

        @Override
        public Adapter caseAttributeLink(AttributeLink object)
        {
            return createAttributeLinkAdapter();
        }

        @Override
        public Adapter caseAttributeAllocate(AttributeAllocate object)
        {
            return createAttributeAllocateAdapter();
        }

        @Override
        public Adapter caseUntracedChapter(UntracedChapter object)
        {
            return createUntracedChapterAdapter();
        }

        @Override
        public Adapter caseProblemChapter(ProblemChapter object)
        {
            return createProblemChapterAdapter();
        }

        @Override
        public Adapter caseTrashChapter(TrashChapter object)
        {
            return createTrashChapterAdapter();
        }

        @Override
        public Adapter caseRequirement(Requirement object)
        {
            return createRequirementAdapter();
        }

        @Override
        public Adapter caseAnonymousRequirement(AnonymousRequirement object)
        {
            return createAnonymousRequirementAdapter();
        }

        @Override
        public Adapter caseEModelElement(EModelElement object)
        {
            return createEModelElementAdapter();
        }

        @Override
        public Adapter caseElement(Element object)
        {
            return createElementAdapter();
        }

        @Override
        public Adapter caseAttributeOwner(AttributeOwner object)
        {
            return createAttributeOwnerAdapter();
        }

        @Override
        public Adapter caseTtm_IdentifiedElement(ttm.IdentifiedElement object)
        {
            return createTtm_IdentifiedElementAdapter();
        }

        @Override
        public Adapter caseProject(Project object)
        {
            return createProjectAdapter();
        }

        @Override
        public Adapter defaultCase(EObject object)
        {
            return createEObjectAdapter();
        }
    };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target)
    {
        return modelSwitch.doSwitch((EObject) target);
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.RequirementProject <em>Project</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.RequirementProject
     * @generated
     */
    public Adapter createRequirementProjectAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.HierarchicalElement <em>Hierarchical Element</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.HierarchicalElement
     * @generated
     */
    public Adapter createHierarchicalElementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.CurrentRequirement <em>Current Requirement</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.CurrentRequirement
     * @generated
     */
    public Adapter createCurrentRequirementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.Attribute <em>Attribute</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.Attribute
     * @generated
     */
    public Adapter createAttributeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.AttributeConfiguration <em>Attribute Configuration</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we
     * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.AttributeConfiguration
     * @generated
     */
    public Adapter createAttributeConfigurationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.ConfiguratedAttribute <em>Configurated Attribute</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we
     * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.ConfiguratedAttribute
     * @generated
     */
    public Adapter createConfiguratedAttributeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.AttributeValue <em>Attribute Value</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.AttributeValue
     * @generated
     */
    public Adapter createAttributeValueAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.DefaultAttributeValue <em>Default Attribute Value</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we
     * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.DefaultAttributeValue
     * @generated
     */
    public Adapter createDefaultAttributeValueAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.IdentifiedElement <em>Identified Element</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.IdentifiedElement
     * @generated
     */
    public Adapter createIdentifiedElementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.SpecialChapter <em>Special Chapter</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.SpecialChapter
     * @generated
     */
    public Adapter createSpecialChapterAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.TextAttribute
     * <em>Text Attribute</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily
     * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc
     * -->
     * 
     * @return the new adapter.
     * @see org.topcased.requirement.TextAttribute
     * @generated
     */
    public Adapter createTextAttributeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.ObjectAttribute <em>Object Attribute</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.ObjectAttribute
     * @generated
     */
    public Adapter createObjectAttributeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.UpstreamModel
     * <em>Upstream Model</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily
     * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc
     * -->
     * 
     * @return the new adapter.
     * @see org.topcased.requirement.UpstreamModel
     * @generated
     */
    public Adapter createUpstreamModelAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.AttributeLink
     * <em>Attribute Link</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily
     * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc
     * -->
     * 
     * @return the new adapter.
     * @see org.topcased.requirement.AttributeLink
     * @generated
     */
    public Adapter createAttributeLinkAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.AttributeAllocate <em>Attribute Allocate</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.AttributeAllocate
     * @generated
     */
    public Adapter createAttributeAllocateAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.UntracedChapter <em>Untraced Chapter</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.UntracedChapter
     * @generated
     */
    public Adapter createUntracedChapterAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.ProblemChapter <em>Problem Chapter</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.ProblemChapter
     * @generated
     */
    public Adapter createProblemChapterAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.TrashChapter
     * <em>Trash Chapter</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily
     * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc
     * -->
     * 
     * @return the new adapter.
     * @see org.topcased.requirement.TrashChapter
     * @generated
     */
    public Adapter createTrashChapterAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.Requirement <em>Requirement</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.Requirement
     * @generated
     */
    public Adapter createRequirementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.topcased.requirement.AnonymousRequirement <em>Anonymous Requirement</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * @return the new adapter.
     * @see org.topcased.requirement.AnonymousRequirement
     * @generated
     */
    public Adapter createAnonymousRequirementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.EModelElement <em>EModel Element</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's
     * useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.emf.ecore.EModelElement
     * @generated
     */
    public Adapter createEModelElementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link ttm.Element <em>Element</em>}'.
     * <!-- begin-user-doc --> This
     * default implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
     * inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see ttm.Element
     * @generated
     */
    public Adapter createElementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link ttm.AttributeOwner <em>Attribute Owner</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see ttm.AttributeOwner
     * @generated
     */
    public Adapter createAttributeOwnerAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link ttm.IdentifiedElement <em>Identified Element</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see ttm.IdentifiedElement
     * @generated
     */
    public Adapter createTtm_IdentifiedElementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link ttm.Project <em>Project</em>}'.
     * <!-- begin-user-doc --> This
     * default implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
     * inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * @return the new adapter.
     * @see ttm.Project
     * @generated
     */
    public Adapter createProjectAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc --> This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter()
    {
        return null;
    }

} // RequirementAdapterFactory
