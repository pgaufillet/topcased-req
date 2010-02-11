/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.topcased.requirement.*;

import ttm.AttributeOwner;
import ttm.Element;
import ttm.Project;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.topcased.requirement.RequirementPackage
 * @generated
 */
public class RequirementSwitch<T>
{
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static RequirementPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequirementSwitch()
    {
        if (modelPackage == null)
        {
            modelPackage = RequirementPackage.eINSTANCE;
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    public T doSwitch(EObject theEObject)
    {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(EClass theEClass, EObject theEObject)
    {
        if (theEClass.eContainer() == modelPackage)
        {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        }
        else
        {
            List<EClass> eSuperTypes = theEClass.getESuperTypes();
            return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch(eSuperTypes.get(0), theEObject);
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(int classifierID, EObject theEObject)
    {
        switch (classifierID)
        {
            case RequirementPackage.REQUIREMENT_PROJECT: {
                RequirementProject requirementProject = (RequirementProject) theEObject;
                T result = caseRequirementProject(requirementProject);
                if (result == null)
                    result = caseIdentifiedElement(requirementProject);
                if (result == null)
                    result = caseEModelElement(requirementProject);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.HIERARCHICAL_ELEMENT: {
                HierarchicalElement hierarchicalElement = (HierarchicalElement) theEObject;
                T result = caseHierarchicalElement(hierarchicalElement);
                if (result == null)
                    result = caseIdentifiedElement(hierarchicalElement);
                if (result == null)
                    result = caseEModelElement(hierarchicalElement);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.CURRENT_REQUIREMENT: {
                CurrentRequirement currentRequirement = (CurrentRequirement) theEObject;
                T result = caseCurrentRequirement(currentRequirement);
                if (result == null)
                    result = caseRequirement(currentRequirement);
                if (result == null)
                    result = caseIdentifiedElement(currentRequirement);
                if (result == null)
                    result = caseEModelElement(currentRequirement);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.ATTRIBUTE: {
                Attribute attribute = (Attribute) theEObject;
                T result = caseAttribute(attribute);
                if (result == null)
                    result = caseEModelElement(attribute);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.ATTRIBUTE_CONFIGURATION: {
                AttributeConfiguration attributeConfiguration = (AttributeConfiguration) theEObject;
                T result = caseAttributeConfiguration(attributeConfiguration);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.CONFIGURATED_ATTRIBUTE: {
                ConfiguratedAttribute configuratedAttribute = (ConfiguratedAttribute) theEObject;
                T result = caseConfiguratedAttribute(configuratedAttribute);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.ATTRIBUTE_VALUE: {
                AttributeValue attributeValue = (AttributeValue) theEObject;
                T result = caseAttributeValue(attributeValue);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.DEFAULT_ATTRIBUTE_VALUE: {
                DefaultAttributeValue defaultAttributeValue = (DefaultAttributeValue) theEObject;
                T result = caseDefaultAttributeValue(defaultAttributeValue);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.IDENTIFIED_ELEMENT: {
                IdentifiedElement identifiedElement = (IdentifiedElement) theEObject;
                T result = caseIdentifiedElement(identifiedElement);
                if (result == null)
                    result = caseEModelElement(identifiedElement);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.SPECIAL_CHAPTER: {
                SpecialChapter specialChapter = (SpecialChapter) theEObject;
                T result = caseSpecialChapter(specialChapter);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.TEXT_ATTRIBUTE: {
                TextAttribute textAttribute = (TextAttribute) theEObject;
                T result = caseTextAttribute(textAttribute);
                if (result == null)
                    result = caseAttribute(textAttribute);
                if (result == null)
                    result = caseEModelElement(textAttribute);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.OBJECT_ATTRIBUTE: {
                ObjectAttribute objectAttribute = (ObjectAttribute) theEObject;
                T result = caseObjectAttribute(objectAttribute);
                if (result == null)
                    result = caseAttribute(objectAttribute);
                if (result == null)
                    result = caseEModelElement(objectAttribute);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.UPSTREAM_MODEL: {
                UpstreamModel upstreamModel = (UpstreamModel) theEObject;
                T result = caseUpstreamModel(upstreamModel);
                if (result == null)
                    result = caseProject(upstreamModel);
                if (result == null)
                    result = caseTtm_IdentifiedElement(upstreamModel);
                if (result == null)
                    result = caseAttributeOwner(upstreamModel);
                if (result == null)
                    result = caseElement(upstreamModel);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.ATTRIBUTE_LINK: {
                AttributeLink attributeLink = (AttributeLink) theEObject;
                T result = caseAttributeLink(attributeLink);
                if (result == null)
                    result = caseObjectAttribute(attributeLink);
                if (result == null)
                    result = caseAttribute(attributeLink);
                if (result == null)
                    result = caseEModelElement(attributeLink);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.ATTRIBUTE_ALLOCATE: {
                AttributeAllocate attributeAllocate = (AttributeAllocate) theEObject;
                T result = caseAttributeAllocate(attributeAllocate);
                if (result == null)
                    result = caseObjectAttribute(attributeAllocate);
                if (result == null)
                    result = caseAttribute(attributeAllocate);
                if (result == null)
                    result = caseEModelElement(attributeAllocate);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.UNTRACED_CHAPTER: {
                UntracedChapter untracedChapter = (UntracedChapter) theEObject;
                T result = caseUntracedChapter(untracedChapter);
                if (result == null)
                    result = caseSpecialChapter(untracedChapter);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.PROBLEM_CHAPTER: {
                ProblemChapter problemChapter = (ProblemChapter) theEObject;
                T result = caseProblemChapter(problemChapter);
                if (result == null)
                    result = caseSpecialChapter(problemChapter);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.TRASH_CHAPTER: {
                TrashChapter trashChapter = (TrashChapter) theEObject;
                T result = caseTrashChapter(trashChapter);
                if (result == null)
                    result = caseSpecialChapter(trashChapter);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.REQUIREMENT: {
                Requirement requirement = (Requirement) theEObject;
                T result = caseRequirement(requirement);
                if (result == null)
                    result = caseIdentifiedElement(requirement);
                if (result == null)
                    result = caseEModelElement(requirement);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case RequirementPackage.ANONYMOUS_REQUIREMENT: {
                AnonymousRequirement anonymousRequirement = (AnonymousRequirement) theEObject;
                T result = caseAnonymousRequirement(anonymousRequirement);
                if (result == null)
                    result = caseRequirement(anonymousRequirement);
                if (result == null)
                    result = caseIdentifiedElement(anonymousRequirement);
                if (result == null)
                    result = caseEModelElement(anonymousRequirement);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            default:
                return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Project</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Project</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRequirementProject(RequirementProject object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Hierarchical Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Hierarchical Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseHierarchicalElement(HierarchicalElement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Current Requirement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Current Requirement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCurrentRequirement(CurrentRequirement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttribute(Attribute object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute Configuration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute Configuration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttributeConfiguration(AttributeConfiguration object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Configurated Attribute</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Configurated Attribute</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConfiguratedAttribute(ConfiguratedAttribute object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute Value</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute Value</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttributeValue(AttributeValue object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Default Attribute Value</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Default Attribute Value</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDefaultAttributeValue(DefaultAttributeValue object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Identified Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Identified Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIdentifiedElement(IdentifiedElement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Special Chapter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Special Chapter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSpecialChapter(SpecialChapter object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Text Attribute</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Text Attribute</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTextAttribute(TextAttribute object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Object Attribute</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Object Attribute</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseObjectAttribute(ObjectAttribute object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Upstream Model</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Upstream Model</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUpstreamModel(UpstreamModel object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute Link</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute Link</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttributeLink(AttributeLink object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute Allocate</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute Allocate</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttributeAllocate(AttributeAllocate object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Untraced Chapter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Untraced Chapter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUntracedChapter(UntracedChapter object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Problem Chapter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Problem Chapter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProblemChapter(ProblemChapter object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Trash Chapter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Trash Chapter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTrashChapter(TrashChapter object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Requirement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Requirement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRequirement(Requirement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Anonymous Requirement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Anonymous Requirement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAnonymousRequirement(AnonymousRequirement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EModel Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EModel Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEModelElement(EModelElement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElement(Element object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute Owner</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute Owner</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttributeOwner(AttributeOwner object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Identified Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Identified Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTtm_IdentifiedElement(ttm.IdentifiedElement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Project</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Project</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProject(Project object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    public T defaultCase(EObject object)
    {
        return null;
    }

} //RequirementSwitch
