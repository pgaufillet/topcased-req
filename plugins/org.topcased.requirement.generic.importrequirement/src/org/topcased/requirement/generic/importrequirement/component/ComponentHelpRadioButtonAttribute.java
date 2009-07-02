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
package org.topcased.requirement.generic.importrequirement.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.topcased.requirement.generic.importrequirement.ui.NotifyElement;

/**
 * The Class ComponentHelpRadioButtonAttribute.
 */
public class ComponentHelpRadioButtonAttribute extends ComponentHelp
{

    /** The radios. */
    Button[] radios;

    /**
     * Instantiates a new component help radio button attribute.
     * 
     * @param parentElement the parent element
     * @param parent the parent
     * @param toolkit the toolkit
     * @param style the style
     * @param helpText the help text
     */
    public ComponentHelpRadioButtonAttribute(NotifyElement parentElement, Composite parent, FormToolkit toolkit, int style, String helpText)
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
        radios[0] = toolkit.createButton(this, "Text", SWT.RADIO);
        radios[0].addSelectionListener(new SelectionListener()
        {

            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                pageParent.handleModelChange();
            }
        });
        radios[1] = toolkit.createButton(this, "Reference", SWT.RADIO);
        radios[1].addSelectionListener(new SelectionListener()
        {

            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                pageParent.handleModelChange();
            }
        });

        radios[0].setSelection(true);
    }

    /**
     * Checks if is reference.
     * 
     * @return true, if is reference
     */
    public boolean isReference()
    {
        return radios[1].getSelection();
    }

}
