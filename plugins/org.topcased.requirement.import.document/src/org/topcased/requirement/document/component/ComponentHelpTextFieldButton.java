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

import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.topcased.requirement.document.ui.ImportRequirementWizardPageSelectDocument;
import org.topcased.requirement.document.ui.NotifyElement;
import org.topcased.requirement.document.ui.SelectStereotypeDialog;

/**
 * The Class ComponentHelpTextFieldButton.
 */
public class ComponentHelpTextFieldButton extends ComponentHelpTextField
{

    /** The dialog type. */
    private int dialogType;

    /** The b. */
    Button b;

    /**
     * Instantiates a new component help text field button.
     * 
     * @param parentElement the parent element
     * @param parent the parent
     * @param toolkit the toolkit
     * @param style the style
     * @param helpText the help text
     * @param dialog the dialog
     */
    public ComponentHelpTextFieldButton(NotifyElement parentElement, Composite parent, FormToolkit toolkit, int style, String helpText, int dialog)
    {
        super(parentElement, parent, toolkit, style, helpText, 2);
        this.dialogType = dialog;
    }

    /**
     * Instantiates a new component help text field button.
     * 
     * @param parentElement the parent element
     * @param parent the parent
     * @param toolkit the toolkit
     * @param style the style
     * @param helpText the help text
     * @param dialog the dialog
     * @param regex the regex
     */
    public ComponentHelpTextFieldButton(NotifyElement parentElement, Composite parent, FormToolkit toolkit, int style, String helpText, int dialog, String regex)
    {
        super(parentElement, parent, toolkit, style, helpText, 2, regex);
        this.dialogType = dialog;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.doc2model.requirement.component.ComponentHelpTextField#createContent(org.eclipse.ui.forms.widgets
     * .FormToolkit)
     */
    @Override
    void createContent(FormToolkit toolkit)
    {
        super.createContent(toolkit);
        b = toolkit.createButton(this, "Browse", SWT.PUSH);
        b.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                if (dialogType == -1)
                {
                    if (pageParent instanceof ImportRequirementWizardPageSelectDocument)
                    {
                        BusyIndicator.showWhile(Display.getDefault(), new Runnable()
                        {
                            public void run()
                            {
                                SelectStereotypeDialog dialog = new SelectStereotypeDialog(getShell(), Display.getDefault().getCursorLocation(), pageParent,
                                        ((ImportRequirementWizardPageSelectDocument) pageParent).getModelType());
                                dialog.open();
                            }
                        });

                    }
                }
                else
                {
                    ResourceDialog dialog = new ResourceDialog(getShell(), "File selection", dialogType | SWT.SINGLE);
                    if (dialog.open() == ResourceDialog.OK)
                    {
                        input.setText(dialog.getURIText());
                        pageParent.handleModelChange();
                    }
                }
            }
        });
    }

    /**
     * Sets the button enable.
     * 
     * @param ok the new button enable
     */
    public void setButtonEnable(boolean ok)
    {
        b.setEnabled(ok);
    }

}
