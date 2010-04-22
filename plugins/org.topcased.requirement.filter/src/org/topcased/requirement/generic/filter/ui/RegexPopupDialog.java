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
 *  Amine Bouchikhi (ATOS ORIGIN INTEGRATION) amine.bouchikhi@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.generic.filter.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.topcased.regexviewer.views.RegexViewerComposite;

/**
 * The Class RegexPopupDialog.
 */
public class RegexPopupDialog extends Dialog
{

    /** The composite. */
    private Composite composite;

    /** The regex. */
    private RegexViewerComposite regex;

    /** The text. */
    private Text regexText;

    private Text attributeNameText;

    /** The value. */
    private String value;

    private String nameValue;

    /**
     * Instantiates a new regex popup dialog.
     * 
     * @param parentShell the parent shell
     */
    protected RegexPopupDialog(Shell parentShell)
    {
        super(parentShell);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed()
    {
        try
        {
            Pattern.compile(regexText.getText());
            if (attributeNameText != null)
            {
                if (attributeNameText.getText() != "")
                {
                    if (regexText.getText() != "")
                    {

                        super.okPressed();
                    }
                    else
                    {
                        MessageDialog.openWarning(getShell(), "Syntax error", "please enter the regular expression. \n");
                    }
                }
                else
                {
                    MessageDialog.openWarning(getShell(), "Syntax error", "please enter the attribute name. \n");
                }

            }
        }
        catch (PatternSyntaxException e)
        {
            MessageDialog.openWarning(getShell(), "Syntax error", "please enter a correct regular expression : \n" + e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
     */
    @Override
    protected Point getInitialSize()
    {
        return super.getInitialSize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.requirement.generic.filter.ui.CustomDialog#getInitialLocation(org.eclipse.swt.graphics.Point)
     */
    @Override
    protected Point getInitialLocation(Point initialSize)
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point(screenSize.width / 2 - getDialogArea().getSize().x / 2, screenSize.height / 2 - getDialogArea().getSize().y / 2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Define your regular expression");
        composite = (Composite) super.createDialogArea(parent);
        FormToolkit toolkit = new FormToolkit(Display.getDefault());
        Form form = toolkit.createForm(composite);
        form.getBody().setLayout(new GridLayout());
        toolkit.decorateFormHeading(form);

        Section sec = toolkit.createSection(form.getBody(), Section.EXPANDED | Section.TITLE_BAR | Section.DESCRIPTION);
        sec.setText("Regular Expression");
        sec.setDescription("This dialog allows to edit your own regular expression." + "\nPlease fill the text field bellow"
                + "\nTo obtain a correct regular expression you can use the regex viewer to help you" + "\nDon't forget that the any case in regular expression is : .*");
        sec.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        Composite compo = toolkit.createComposite(sec);
        compo.setLayout(new GridLayout(2, false));
        sec.setClient(compo);
        Label name = toolkit.createLabel(compo, "Attribute Name : ");
        attributeNameText = toolkit.createText(compo, "", SWT.BORDER);
        attributeNameText.setFocus();
        Label l = toolkit.createLabel(compo, "Regular expression : ");
        regexText = toolkit.createText(compo, "", SWT.BORDER);

        regexText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                regex.setRegex(regexText.getText());
                value = regexText.getText();
            }
        });
        attributeNameText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                nameValue = attributeNameText.getText();
            }
        });

        attributeNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 0, 0));
        regexText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        regex = new RegexViewerComposite(form.getBody(), SWT.NONE, RegexViewerComposite.EXPANDED | RegexViewerComposite.MATCH);
        regex.setText("Type in this area a sample string to test your regex");
        regex.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        composite.pack();
        return composite;
    }

    /**
     * Gets the user value.
     * 
     * @return the user value
     */
    public String getUserRegex()
    {
        return value;
    }

    public String getUserAttrName()
    {
        return nameValue;
    }

}
