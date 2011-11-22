/***********************************************************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Olivier Melois (Atos) <a href="mailto:olivier.melois@atos.net">olivier.melois@atos.net</a>"
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.presentation;

import org.eclipse.emf.common.ui.ViewerPane;

public interface IRequestActivationHandler
{
    /**
     * Method used to set a ViewerPane as the current one
     * 
     * @param pane
     */
    public void requestActivation(ViewerPane pane);
}
