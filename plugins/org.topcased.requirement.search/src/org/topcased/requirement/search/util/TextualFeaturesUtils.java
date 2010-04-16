package org.topcased.requirement.search.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.topcased.requirement.RequirementPackage;

import ttm.TtmPackage;

public class TextualFeaturesUtils

{

    public TextualFeaturesUtils()
    {
    }

    private static TextualFeaturesUtils instance;

    public static TextualFeaturesUtils instance()
    {
        return instance == null ? instance = new TextualFeaturesUtils() : instance;
    }

    public List<ETypedElement> getParticipantTextualTypedElement()
    {
        List<ETypedElement> list = new ArrayList<ETypedElement>();

        list.addAll(getRequirementPackageParticipantTextualTypedElement());

        return list;
    }

    public List<ETypedElement> getRequirementPackageParticipantTextualTypedElement()
    {
        return Arrays.asList(new ETypedElement[] {RequirementPackage.Literals.CONFIGURATED_ATTRIBUTE__NAME, RequirementPackage.Literals.IDENTIFIED_ELEMENT__IDENTIFIER,
                RequirementPackage.Literals.IDENTIFIED_ELEMENT__SHORT_DESCRIPTION, RequirementPackage.Literals.ATTRIBUTE_VALUE__VALUE, RequirementPackage.Literals.ATTRIBUTE__NAME,
                RequirementPackage.Literals.TEXT_ATTRIBUTE__VALUE, TtmPackage.Literals.IDENTIFIED_ELEMENT__IDENT, TtmPackage.Literals.IDENTIFIED_ELEMENT__SHORT_DESCRIPTION,
                TtmPackage.Literals.TEXT__VALUE});
    }

    public List<ETypedElement> getOwnedETypedElementsFromEObject(EObject obj)
    {
        List<ETypedElement> list = new ArrayList<ETypedElement>();
        for (ETypedElement e : getParticipantTextualTypedElement())
        {
            if (e instanceof EStructuralFeature)
            {
                try
                {
                    Object o = obj.eGet((EStructuralFeature) e);
                    if (o instanceof String)
                    {
                        list.add(e);
                    }
                }
                catch (Throwable t)
                {
                    // ugly
                }
            }
        }
        return list;
    }

    public String getTextFromETypedElement(EObject obj, ETypedElement elem)
    {
        if (elem instanceof EStructuralFeature)
        {
            Object o = obj.eGet((EStructuralFeature) elem);
            if (o instanceof String)
            {
                return (String) o;
            }
        }
        return null;
    }

    public String getTextFromEStructuralFeatureIfAny(EObject obj)
    {
        List<ETypedElement> lst = getOwnedETypedElementsFromEObject(obj);
        if (!lst.isEmpty())
        {
            ETypedElement elem = lst.get(0);
            if (elem instanceof EStructuralFeature)
            {
                Object o = obj.eGet((EStructuralFeature) elem);
                if (o instanceof String)
                {
                    return (String) o;
                }
            }
        }
        return null;
    }

    public void setTextForEStructuralFeatureIfAny(EObject obj, Object val)
    {
        for (ETypedElement e : getParticipantTextualTypedElement())
        {
            if (e instanceof EStructuralFeature)
            {
                try
                {
                    if (val instanceof String)
                    {
                        obj.eSet((EStructuralFeature) e, val);
                    }
                }
                catch (Throwable ex)
                {
                    // TODO: User to handle exception in an elegant way
                }
            }
        }
    }

    public void setTextForETypedElement(EObject obj, ETypedElement elem, Object val)
    {
        if (elem instanceof EStructuralFeature)
        {
            try
            {
                if (val instanceof String)
                {
                    obj.eSet((EStructuralFeature) elem, val);
                }
            }
            catch (Throwable ex)
            {
                // TODO: User to handle exception in an elegant way
            }
        }
    }
}