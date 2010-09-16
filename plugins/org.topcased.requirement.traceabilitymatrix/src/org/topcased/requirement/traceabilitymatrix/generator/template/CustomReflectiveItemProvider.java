/*****************************************************************************
 * Copyright (c) 2010 Atos Origin
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Mathieu Velten (Atos Origin) mathieu.velten@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.traceabilitymatrix.generator.template;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ReflectiveItemProvider;

public class CustomReflectiveItemProvider extends ReflectiveItemProvider
{

    public CustomReflectiveItemProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);
    }

    @Override
    public String getText(Object object)
    {
      EObject eObject = (EObject)object;
      EClass eClass = eObject.eClass();
      String label = format(capName(eClass.getName()), ' ');

      EStructuralFeature feature = getLabelFeature(eClass);
      if (feature != null)
      {
        Object value = eObject.eGet(feature);
        if (value != null)
        {
          return "&lt;" + label + "&gt; " + value.toString();
        }
      }
      return label;
    }

}
