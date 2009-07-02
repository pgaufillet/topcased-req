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
package org.topcased.requirement.generic.importrequirement.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.topcased.requirement.generic.importrequirement.component.ComponentHelpTextField;
import org.topcased.requirement.generic.importrequirement.utils.Messages;

/**
 * The Class NewColumnPopup.
 */
public class NewColumnPopup extends PopupRegexDialog
{

    /** The column input. */
    private String columnInput;

    /** The component help. */
    private ComponentHelpTextField componentHelp;

    /**
     * Instantiates a new new column popup.
     * 
     * @param parentShell the parent shell
     */
    protected NewColumnPopup(Shell parentShell)
    {
        super(parentShell);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.doc2model.requirement.ui.PopupRegexDialog#createContent(org.eclipse.ui.forms.widgets.FormToolkit,
     * org.eclipse.swt.widgets.Composite)
     */
    @Override
    void createContent(FormToolkit toolkit, Composite compo)
    {
        getShell().setText("New Column"); //$NON-NLS-1$
        toolkit.createLabel(compo, "Column: "); //$NON-NLS-1$
        String helpText = Messages.NewColumnPopup_COLUMN;
        componentHelp = new ComponentHelpTextField(this, compo, toolkit, SWT.NONE, helpText);
        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        componentHelp.setLayoutData(data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.ui.PopupRegexDialog#handleModelChange()
     */
    public void handleModelChange()
    {
        super.handleModelChange();
        try
        {
            if (Integer.parseInt(componentHelp.getInput()) >= 0)
            {
                super.handleModelChange();
                columnInput = componentHelp.getInput();
            }
            else if (componentHelp.getInput() != "") //$NON-NLS-1$
            {
                componentHelp.setValueText(""); //$NON-NLS-1$
            }
        }
        catch (NumberFormatException e)
        {
            if (componentHelp.getInput() != "") //$NON-NLS-1$
            {
                componentHelp.setValueText(""); //$NON-NLS-1$
            }
        }
    }

    /**
     * Gets the column input.
     * 
     * @return the column input
     */
    public int getColumnInput()
    {
        return Integer.parseInt(columnInput);
    }

}
