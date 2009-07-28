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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * The Class Utils.
 */
public class Utils
{

    public static void error(String string)
    {
        MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", string);
    }
    
    public static Object get(Object o, String propertyName) {
        if (o == null || o.getClass() == null)
        {
           return null ; 
        }
        if (propertyName == null || propertyName.length() == 0)
        {
           return null ; 
        }   
        Class c = o.getClass() ;
        Field field = null;
        Object result = null ;
        try {
           try
           {
              field = c.getDeclaredField(propertyName);
           }
           catch (NoSuchFieldException e) {
           }
           while (c.getSuperclass() != null && result == null)
           {
              c = c.getSuperclass();
              try
              {
                 field = c.getDeclaredField(propertyName);
              }
              catch (NoSuchFieldException e) {
              }
           }
        } catch (SecurityException e) {
           e.printStackTrace();
        } 
        if (field != null)
        {
           field.setAccessible(true);
           try {
              result = field.get(o);
           } catch (IllegalArgumentException e) {
              e.printStackTrace();
           } catch (IllegalAccessException e) {
              e.printStackTrace();
           }
           field.setAccessible(false);
        }
        return result;
     }

}
