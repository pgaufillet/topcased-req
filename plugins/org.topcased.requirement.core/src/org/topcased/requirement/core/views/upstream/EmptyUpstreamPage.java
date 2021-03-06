/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.views.upstream;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.Page;
import org.topcased.requirement.core.internal.Messages;

/**
 * Empty upstream requirement page
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class EmptyUpstreamPage extends Page implements IUpstreamRequirementPage
{

    private Composite composite;

    /**
     * @see org.eclipse.ui.part.Page#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent)
    {
        composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        Label text = new Label(composite, SWT.NONE);
        text.setText(Messages.getString("EmptyUpstreamPage.0")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.ui.part.Page#getControl()
     */
    @Override
    public Control getControl()
    {
        return composite;
    }

    /**
     * @see org.eclipse.ui.part.Page#setFocus()
     */
    @Override
    public void setFocus()
    {
        // Do nothing
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection)
    {
        // TODO Auto-generated method stub
        
    }
}
