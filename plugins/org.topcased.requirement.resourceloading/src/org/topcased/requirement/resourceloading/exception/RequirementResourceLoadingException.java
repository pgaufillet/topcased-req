/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.resourceloading.exception;
/**
 * Special exception of this plugin.
 * @author Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 *
 */
public class RequirementResourceLoadingException extends RuntimeException
{

    /**
     * ID.
     */
    private static final long serialVersionUID = -4684288122651196501L;

    public RequirementResourceLoadingException()
    {
        super();
    }

    public RequirementResourceLoadingException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
    }

    public RequirementResourceLoadingException(String arg0)
    {
        super(arg0);
    }

    public RequirementResourceLoadingException(Throwable arg0)
    {
        super(arg0);
    }

    
}
