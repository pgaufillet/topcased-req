/*****************************************************************************
 * Copyright (c) 2011 Atos.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Anass RADOUANI (Atos) anass.radouani@atos.net - Initial API and implementation
 *
 *****************************************************************************/

package org.topcased.requirement.document.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.topcased.requirement.document.ui.NotifyElement;

public class ComponentHelpCheckButton extends ComponentHelp
{

    protected Button checkButton;
    private String text;
    
    
    public ComponentHelpCheckButton(NotifyElement parentElement, Composite parent, FormToolkit toolkit, int style, String helpText)
    {
        super(parentElement, parent, toolkit, style, helpText, 3);
    }

    @Override
    void createContent(FormToolkit toolkit)
    {
        checkButton = toolkit.createButton(this, text, SWT.CHECK);
        checkButton.addSelectionListener(new SelectionListener()
        {
            
            public void widgetSelected(SelectionEvent e)
            {
                if (pageParent != null)
                {
                    pageParent.handleModelChange();
                }
            }
            
            public void widgetDefaultSelected(SelectionEvent e)
            {
                if (pageParent != null)
                {
                    pageParent.handleModelChange();
                }
            }
        });
        checkButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    }

    /**
     * Sets the value text.
     * 
     * @param text the new value text
     */
    public void setValueText(String text)
    {
        checkButton.setText(text);
        this.text = text;
    }

    /**
     * get the check button
     * 
     * @return
     */
    public Button getButton()
    {
        return checkButton;
    }
    
    /**
     * return the selection of check button
     * 
     * @return
     */
    public boolean getSelection()
    {
        return checkButton.getSelection();
    }
    
    /**
     * set selection of check button
     * 
     * @param selected
     */
    public void setSelection(boolean selected)
    {
        checkButton.setSelection(selected);
    }
    
}
