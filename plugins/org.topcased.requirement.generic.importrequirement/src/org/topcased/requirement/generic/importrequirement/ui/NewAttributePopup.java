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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.topcased.requirement.generic.importrequirement.component.ComponentHelpRadioButtonAttribute;
import org.topcased.requirement.generic.importrequirement.component.ComponentHelpTextField;
import org.topcased.requirement.generic.importrequirement.utils.Messages;

/**
 * The Class NewAttributePopup.
 */
public class NewAttributePopup extends Dialog implements NotifyElement
{

    /** The component help text field. */
    private ComponentHelpTextField componentHelpTextField;

    /** The attribute name. */
    private String attributeName;

    /** The is reference. */
    private boolean isReference;

    /** The component help radio button. */
    private ComponentHelpRadioButtonAttribute componentHelpRadioButton;

    /**
     * Instantiates a new new attribute popup.
     * 
     * @param shell the shell
     */
    protected NewAttributePopup(Shell shell)
    {
        super(shell);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.ui.NotifyElement#handleModelChange()
     */
    public void handleModelChange()
    {
        attributeName = componentHelpTextField.getInput();
        isReference = componentHelpRadioButton.isReference();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent)
    {

        getShell().setText("New Attribute"); //$NON-NLS-1$

        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(new FillLayout());
        FormToolkit toolkit = new FormToolkit(Display.getDefault());
        Form form = toolkit.createForm(composite);
        form.getBody().setLayout(new GridLayout());
        toolkit.decorateFormHeading(form);

        Section sec = toolkit.createSection(form.getBody(), Section.TITLE_BAR);
        sec.setText("Enter Attribute name"); //$NON-NLS-1$
        sec.setLayoutData(new GridData(SWT.FILL, SWT.Expand, true, false, 1, 1));
        Composite compo = toolkit.createComposite(sec);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        compo.setLayout(layout);
        sec.setClient(compo);

        // Create field for Regex
        toolkit.createLabel(compo, "Name: "); //$NON-NLS-1$
        String helpText = Messages.NewAttributePopup_ATTRIBUTE_NAME;
        componentHelpTextField = new ComponentHelpTextField(this, compo, toolkit, SWT.NONE, helpText);
        componentHelpTextField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        // Create radio button for type
        toolkit.createLabel(compo, "Type: "); //$NON-NLS-1$
        helpText = Messages.NewAttributePopup_ATTRIBUTE_REF_OR_TEXT;
        componentHelpRadioButton = new ComponentHelpRadioButtonAttribute(this, compo, toolkit, SWT.NONE, helpText);
        componentHelpTextField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        return composite;
    }

    /**
     * Gets the attribute name.
     * 
     * @return the attribute name
     */
    public String getAttributeName()
    {
        return attributeName;
    }

    /**
     * Checks if is reference.
     * 
     * @return true, if is reference
     */
    public boolean isReference()
    {
        return isReference;
    }

}
