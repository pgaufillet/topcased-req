/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/
package org.topcased.requirement.core.handlers;

import org.topcased.requirement.core.commands.CreateAnonymousReqCommand;
import org.topcased.requirement.core.commands.CreateRequirementCommand;
import org.topcased.requirement.core.internal.Messages;

/**
 * Implements FR#2396
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class CreateAnonymousRequirementHandler extends CreateRequirementHandler
{
    /**
     * @see org.topcased.requirement.core.handlers.CreateRequirementHandler#getCreateCommand()
     */
    @Override
    protected CreateRequirementCommand getCreateCommand()
    {
        return new CreateAnonymousReqCommand(Messages.getString("CreateAnonymousRequirementAction.0")); //$NON-NLS-1$
    }

}
