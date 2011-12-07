/***********************************************************************************************************************
 * Copyright (c) 2011 Atos.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mathieu VELTEN (Atos) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.teamhistory;

/**
 * Exceptions for TeamHistoryManager operations
 * 
 * @author mvelten
 */
public class TeamHistoryException extends Exception
{

    private static final long serialVersionUID = 2739768968046041200L;

    public TeamHistoryException(String msg)
    {
        super(msg);
    }

    public TeamHistoryException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    public TeamHistoryException(Throwable cause)
    {
        super(cause);
    }

}
