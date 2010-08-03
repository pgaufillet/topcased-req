/*****************************************************************************
 * Copyright (c) 2010 Rockwell Collins.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Vincent Hemery (Atos Origin) - Initial API and implementation
 *    
 ******************************************************************************/
package org.topcased.requirement.core.extensions.internal;

/**
 * An enumerate class which handles priority for a style evaluator. Values are ordered from max priority to min.
 */
public enum Priority {
    Highest, High, Medium, Low, Lowest;
    /**
     * Get the value corresponding to the text or the default value.
     * 
     * @param text text for a priority value
     * @return Priority value
     */
    public static Priority getValue(String text)
    {
        try
        {
            Priority result = valueOf(text);
            if (result != null)
            {
                return result;
            }
        }
        catch (IllegalArgumentException e1)
        {
            // do nothing, default value is returned
        }
        catch (NullPointerException e2)
        {
            // do nothing, default value is returned
        }
        return Priority.Lowest;
    }
}
