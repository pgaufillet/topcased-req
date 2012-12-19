/***********************************************************************************************************************
 * Copyright (c) 2012 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Matthieu BOIVINEAU (Atos) 
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.compare;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.engine.AbstractSimilarityChecker;
import org.eclipse.emf.compare.match.engine.GenericMatchEngine;
import org.eclipse.emf.compare.match.engine.internal.EcoreIDSimilarityChecker;
import org.eclipse.emf.ecore.EObject;

import ttm.Attribute;
import ttm.Document;
import ttm.HierarchicalElement;
import ttm.Requirement;
import ttm.Section;

public class ReqMatchEngine extends GenericMatchEngine
{
    @Override   
    protected AbstractSimilarityChecker prepareChecker()
    {
        return new EcoreIDSimilarityChecker(filter, super.prepareChecker())
        {
            @Override
            public boolean isSimilar(EObject obj1, EObject obj2) throws FactoryException 
            {
               boolean result = super.isSimilar(obj1, obj2);
               if (!result)
               {
                   if (obj1 instanceof Section && obj2 instanceof Section)
                   {
                       return getQualifiedName((Section) obj1).equals(getQualifiedName((Section) obj2));
                   }
               }
               return result;
            }

            @Override
            public double absoluteMetric(EObject obj1, EObject obj2) throws FactoryException
            {
                if (obj1 instanceof Attribute && obj2 instanceof Attribute){
                    Attribute a1 = (Attribute) obj1 ;
                    Attribute a2 = (Attribute) obj2 ;
                    if (a1.eContainer() instanceof Requirement && a2.eContainer() instanceof Requirement)
                    {
                        Requirement req1 = (Requirement) a1.eContainer();
                        Requirement req2 = (Requirement) a2.eContainer();
                        if (getQualifiedName(req1).equals(getQualifiedName(req2))){
                            if (a1.getName() != null && a1.getName().equals(a2.getName()) && a1.getValue() != null && a1.getValue().equals(a2.getValue()))
                            {
                                return 1 ;
                            }
                            else if (a1.getName() != null && a1.getName().equals(a2.getName()) && a1.getValue() == null && a2.getValue() == null)
                            {
                                return 1 ;
                            }
                            else if (a1.getName() == null && a2.getName()== null && a1.getValue() == null && a2.getValue() == null)
                            {
                                return 1 ;
                            }
                            else
                            {
                                return 0 ;
                            }
                        }
                    }
                }
                double result = super.absoluteMetric(obj1, obj2);
                if (result != 1)
                {
                    result = isSimilar(obj1, obj2) ? 1 : result ;
                }
                return result ;
            }
            
        };
        
    }
    
    public static String getQualifiedName(Attribute s)
    {
        String qn = "";
        if (s.getParent() instanceof HierarchicalElement)
        {
            HierarchicalElement h = (HierarchicalElement) s.getParent();
            qn = getQualifiedName(h);
        }
        if (qn.length() != 0){
            qn = qn + "::" ;
        }
        qn = qn + s.getName() + "::";
        qn = qn + getValue(s);
        return qn;
    }
    
    private static String getValue(Attribute s)
    {
        return s.getValue();
    }

    public static String getQualifiedName(HierarchicalElement s)
    {
        if (s == null || s.getIdent() == null)
        {
            return "" ;
        }
        EObject parent = s.eContainer();
        StringBuilder result = new StringBuilder(s.getIdent());
        while (parent instanceof HierarchicalElement && !(parent instanceof Document))
        {
            HierarchicalElement s2 = (HierarchicalElement) parent ;
            result.insert(0, s2.getIdent() + "::");
            parent = s2.eContainer();
        }
        return result.toString();
    }

}
