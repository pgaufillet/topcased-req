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
 *  Caroline Bourdeu d'Aguerre (ATOS ORIGIN INTEGRATION) caroline.bourdeudaguerre@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.document.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.topcased.requirement.document.ui.NotifyElement;

/**
 * The Class ComponentHelpRadioButton.
 */
public class ComponentHelpRadioButton extends ComponentHelp
{

    /** The radios. */
    Button[] radios;

    /**
     * The Constructor.
     * 
     * @param parentElement (notify element)
     * @param parent (composite parent)
     * @param toolkit FormToolkit
     * @param style the style
     * @param helpText the help text
     */
    public ComponentHelpRadioButton(NotifyElement parentElement, Composite parent, FormToolkit toolkit, int style, String helpText)
    {
        super(parentElement, parent, toolkit, style, helpText, 2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.doc2model.requirement.component.ComponentHelp#createContent(org.eclipse.ui.forms.widgets.FormToolkit
     * )
     */
    @Override
    void createContent(FormToolkit toolkit)
    {
        radios = new Button[2];
        radios[0] = toolkit.createButton(this, "hierarchical", SWT.RADIO);
        radios[1] = toolkit.createButton(this, "flat", SWT.RADIO);

        radios[0].setSelection(true);
    }

    /**
     * Checks if is hierarchical.
     * 
     * @return true, if is hierarchical
     */
    public boolean isHierarchical()
    {
        return radios[0].getSelection();
    }

    /**
     * Sets the hierachical.
     * 
     * @param isHierarchical the new hierachical
     */
    public void setHierachical(boolean isHierarchical)
    {
        if (isHierarchical)
        {
            radios[0].setSelection(true);
            radios[1].setSelection(false);
        }
        else
        {
            radios[0].setSelection(false);
            radios[1].setSelection(true);
        }
    }
}
