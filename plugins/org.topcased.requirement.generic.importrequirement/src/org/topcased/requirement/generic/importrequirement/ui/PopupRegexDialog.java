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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
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
import org.topcased.regexviewer.views.RegexViewerComposite;
import org.topcased.requirement.generic.importrequirement.component.ComponentHelpTextField;
import org.topcased.requirement.generic.importrequirement.utils.Messages;

/**
 * The Class PopupRegexDialog.
 */
public class PopupRegexDialog extends Dialog implements NotifyElement
{

    /** The regex. */
    private RegexViewerComposite regex;

    /** The regex input. */
    String regexInput = ""; //$NON-NLS-1$

    /** The component help text field. */
    ComponentHelpTextField componentHelpTextField;

    /** The sec. */
    Section sec;

    /** The form. */
    private Form form;

    /**
     * Instantiates a new popup regex dialog.
     * 
     * @param parentShell the parent shell
     */
    protected PopupRegexDialog(Shell parentShell)
    {
        super(parentShell);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell)
    {
        super.configureShell(newShell);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent)
    {

        getShell().setText("New Regex"); //$NON-NLS-1$

        // getShell().setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(new FillLayout());

        FormToolkit toolkit = new FormToolkit(Display.getDefault());
        form = toolkit.createForm(composite);
        form.getBody().setLayout(new GridLayout());
        toolkit.decorateFormHeading(form);

        sec = toolkit.createSection(form.getBody(), Section.TITLE_BAR);
        sec.setText("Regular Expression"); //$NON-NLS-1$
        sec.setLayoutData(new GridData(SWT.FILL, SWT.Expand, true, false, 1, 1));
        Composite compo = toolkit.createComposite(sec);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        compo.setLayout(layout);
        sec.setClient(compo);
        createContent(toolkit, compo);

        // Create field for Regex
        toolkit.createLabel(compo, "Regex: "); //$NON-NLS-1$
        String helpText = Messages.PopupRegexDialog_REGEX;
        componentHelpTextField = new ComponentHelpTextField(this, compo, toolkit, SWT.NONE, helpText);
        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        componentHelpTextField.setLayoutData(data);

        // Add regex viewer component to the dialog
        regex = new RegexViewerComposite(form.getBody(), SWT.NONE, RegexViewerComposite.EXPANDABLE | RegexViewerComposite.MATCH | RegexViewerComposite.DESCRIPTION | RegexViewerComposite.GROUP);
        regex.setDescription("Regex Test Area"); //$NON-NLS-1$
        regex.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        return composite;
    }

    /**
     * Gets the bounds.
     * 
     * @return the bounds
     */
    public static Rectangle getBounds()
    {
        Display display = Display.getDefault();
        Rectangle bounds = display.getBounds();
        Rectangle result = new Rectangle(bounds.x + bounds.width / 4, bounds.y + bounds.height / 4, bounds.width / 2, bounds.height / 2);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#getConstrainedShellBounds(org.eclipse.swt.graphics.Rectangle)
     */
    @Override
    protected Rectangle getConstrainedShellBounds(Rectangle preferredSize)
    {
        return getBounds();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.ui.NotifyElement#handleModelChange()
     */
    public void handleModelChange()
    {
        regexInput = componentHelpTextField.getInput();
        if (!"".equals(regexInput)) //$NON-NLS-1$
        {
            try
            {
                Pattern.compile(regexInput, Pattern.MULTILINE);
                form.setMessage(null, IMessageProvider.NONE);
                this.getButton(IDialogConstants.OK_ID).setEnabled(true);
            }
            catch (PatternSyntaxException e)
            {
                form.setMessage("Regex doesn't compile", IMessageProvider.ERROR); //$NON-NLS-1$
                this.getButton(IDialogConstants.OK_ID).setEnabled(false);
            }
        }
        else
        {
            form.setMessage(null, IMessageProvider.NONE);
            this.getButton(IDialogConstants.OK_ID).setEnabled(true);
        }
    }

    /**
     * Gets the regex input.
     * 
     * @return the regex input
     */
    public String getRegexInput()
    {
        return regexInput;
    }

    /**
     * Creates the content.
     * 
     * @param toolkit the toolkit
     * @param compo the compo
     */
    void createContent(FormToolkit toolkit, Composite compo)
    {
    }
}
