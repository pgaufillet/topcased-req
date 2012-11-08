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

import ttm.Document;
import ttm.HierarchicalElement;
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
                double result = super.absoluteMetric(obj1, obj2);
                if (result != 1)
                {
                    result = isSimilar(obj1, obj2) ? 1 : result ;
                }
                return result ;
            }
            
        };
        
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
