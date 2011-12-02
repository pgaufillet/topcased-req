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
 *  Caroline Bourdeu d'Aguerre (ATOS ORIGIN INTEGRATION) caroline.bourdeudaguerre@atosorigin.com - Initial API and implementation$
 *  Anass RADOUANI (Atos) anass.radouani@atos.net - adding setForeground method
 *
 *****************************************************************************/
package org.topcased.requirement.document.component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.topcased.requirement.document.ui.NotifyElement;

/**
 * The Class ComponentHelpTextField.
 */
public class ComponentHelpTextField extends ComponentHelp
{

    /** The input. */
    protected Text input;

    /** The input value. */
    protected String inputValue = "";

    /** The pattern. */
    protected Pattern pattern = null;

    /** The do handle. */
    private boolean doHandle = true;

    // Constructors call by client
    /**
     * Instantiates a new component help text field.
     * 
     * @param parentElement the parent element
     * @param parent the parent
     * @param toolkit the toolkit
     * @param style the style
     * @param helpText the help text
     */
    public ComponentHelpTextField(NotifyElement parentElement, Composite parent, FormToolkit toolkit, int style, String helpText)
    {
        super(parentElement, parent, toolkit, style, helpText, 1);
    }

    /**
     * Instantiates a new component help text field.
     * 
     * @param parentElement the parent element
     * @param parent the parent
     * @param toolkit the toolkit
     * @param style the style
     * @param helpText the help text
     * @param regex the regex
     */
    public ComponentHelpTextField(NotifyElement parentElement, Composite parent, FormToolkit toolkit, int style, String helpText, String regex)
    {
        super(parentElement, parent, toolkit, style, helpText, 1);
        // Compile the regex
        try
        {
            pattern = Pattern.compile(regex);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Constructors call by inherits classes
    /**
     * Instantiates a new component help text field.
     * 
     * @param parentElement the parent element
     * @param parent the parent
     * @param toolkit the toolkit
     * @param style the style
     * @param helpText the help text
     * @param numColumn the num column
     */
    protected ComponentHelpTextField(NotifyElement parentElement, Composite parent, FormToolkit toolkit, int style, String helpText, int numColumn)
    {
        super(parentElement, parent, toolkit, style, helpText, numColumn);
    }

    /**
     * Instantiates a new component help text field.
     * 
     * @param parentElement the parent element
     * @param parent the parent
     * @param toolkit the toolkit
     * @param style the style
     * @param helpText the help text
     * @param numColumn the num column
     * @param regex the regex
     */
    public ComponentHelpTextField(NotifyElement parentElement, Composite parent, FormToolkit toolkit, int style, String helpText, int numColumn, String regex)
    {
        super(parentElement, parent, toolkit, style, helpText, numColumn);
        // Compile the regex
        try
        {
            pattern = Pattern.compile(regex);
        }
        catch (Exception e)
        {
            // Activator.log(e);
            e.printStackTrace();
        }
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
        input = toolkit.createText(this, inputValue, SWT.BORDER);
        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        input.setLayoutData(data);
        input.addModifyListener(new ModifyListener()
        {

            public void modifyText(ModifyEvent e)
            {
                if (doHandle)
                {
                    inputValue = input.getText();
                    pageParent.handleModelChange();
                }
                else
                {
                    doHandle = true;
                }
            }
        });
    }

    /**
     * Checks if is valid.
     * 
     * @return true, if is valid
     */
    public boolean isValid()
    {
        boolean isValid = true;
        // if a regex is defined
        if (pattern != null)
        {
            // if the input is valid according to the regex
            Matcher m = pattern.matcher(inputValue);
            if (!m.matches())
            {
                input.setBackground(new Color(Display.getDefault(), 255, 151, 151));
                isValid = false;
            }

        }
        if (isValid)
        {
            input.setBackground(null);
        }
        return isValid;
    }

    /**
     * Sets the text enable.
     * 
     * @param ok the new text enable
     */
    public void setTextEnable(boolean ok)
    {
        input.setEnabled(ok);
    }

    /**
     * Gets the input.
     * 
     * @return the input
     */
    public String getInput()
    {
        return input.getText();

    }

    // Set the text value of
    /**
     * Sets the value text.
     * 
     * @param text the new value text
     */
    public void setValueText(String text)
    {
        input.setText(text);
        inputValue = text;
    }

    // Set the text value of
    /**
     * Sets the value text.
     * 
     * @param text the text
     * @param handleChange the handle change
     */
    public void setValueText(String text, boolean handleChange)
    {
        if (handleChange)
        {
            doHandle = false;
        }
        input.setText(text);
        inputValue = text;
    }

    /**
     * Sets the editable.
     * 
     * @param editable the new editable
     */
    public void setEditable(boolean editable)
    {
        input.setEditable(editable);
    }

    /**
     * set foreground of text field
     * 
     * @param color
     */
    public void setTextForeground(Color color)
    {
        input.setForeground(color);
    }
    
}
