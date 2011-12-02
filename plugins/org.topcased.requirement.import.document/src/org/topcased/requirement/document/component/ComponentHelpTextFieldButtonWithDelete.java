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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.topcased.requirement.document.ui.NotifyElement;

/**
 * The Class ComponentHelpTextFieldButtonWithDelete.
 */
public class ComponentHelpTextFieldButtonWithDelete extends ComponentHelpTextField
{

    /** The has to be delete. */
    private boolean hasToBeDelete = false;

    /** The do handle. */
    private boolean doHandle = true;

    /**
     * Instantiates a new component help text field button with delete.
     * 
     * @param parentElement the parent element
     * @param parent the parent
     * @param toolkit the toolkit
     * @param style the style
     * @param helpText the help text
     */
    public ComponentHelpTextFieldButtonWithDelete(NotifyElement parentElement, Composite parent, FormToolkit toolkit, int style, String helpText)
    {
        super(parentElement, parent, toolkit, style, helpText, 2);
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
        input = toolkit.createText(this, inputValue, SWT.BORDER);
        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        input.setLayoutData(data);
        input.setEditable(false);
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

        Button b = toolkit.createButton(this, "", SWT.PUSH);
        b.setImage(new Image(Display.getDefault(), PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE).getImageData()));
        b.addSelectionListener(new SelectionListener()
        {

            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                hasToBeDelete = true;
                pageParent.handleModelChange();
            }

        });

    }

    /**
     * Sets the checks for to be delete.
     * 
     * @param hasToBeDelete the new checks for to be delete
     */
    public void setHasToBeDelete(boolean hasToBeDelete)
    {
        this.hasToBeDelete = hasToBeDelete;
    }

    /**
     * Checks if is delete.
     * 
     * @return true, if is delete
     */
    public boolean isDelete()
    {
        return hasToBeDelete;
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

}
