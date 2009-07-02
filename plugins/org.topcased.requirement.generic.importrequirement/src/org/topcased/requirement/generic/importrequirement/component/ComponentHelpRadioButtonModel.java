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
import org.topcased.requirement.generic.importrequirement.utils.Constants;

/**
 * The Class ComponentHelpRadioButtonModel.
 */
public class ComponentHelpRadioButtonModel extends ComponentHelp
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
    public ComponentHelpRadioButtonModel(NotifyElement parentElement, Composite parent, FormToolkit toolkit, int style, String helpText)
    {
        super(parentElement, parent, toolkit, style, helpText, 3);
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
        radios = new Button[3];
        radios[0] = toolkit.createButton(this, Constants.REQUIREMENT_EXTENSION, SWT.RADIO);
        radios[1] = toolkit.createButton(this, Constants.UML_EXTENSION, SWT.RADIO);
        radios[2] = toolkit.createButton(this, Constants.SYSML_EXTENSION, SWT.RADIO);

        for (int i = 0; i < 3; i++)
        {
            radios[i].addSelectionListener(new SelectionListener()
            {

                public void widgetDefaultSelected(SelectionEvent e)
                {
                }

                public void widgetSelected(SelectionEvent e)
                {
                    pageParent.handleModelChange();
                }
            });
        }

        radios[0].setSelection(true);
    }

    /**
     * Checks if is requirement.
     * 
     * @return true, if is requirement
     */
    public boolean isRequirement()
    {
        return radios[0].getSelection();
    }

    /**
     * Checks if is uml.
     * 
     * @return true, if is uml
     */
    public boolean isUml()
    {
        return radios[1].getSelection();
    }

    /**
     * Sets the selection.
     * 
     * @param value the new selection
     */
    public void setselection(int value)
    {
        for (int i = 0; i < 3; i++)
        {
            radios[i].setSelection(false);
        }
        radios[value].setSelection(true);

    }

}
